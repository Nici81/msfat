package com.bitsworking.videoshowcase;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsworking.videoshowcase.datatypes.ShowcaseItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chris Hager <chris@linuxuser.at> on 07/09/14.
 */
public class VideoShowcaseActivity extends Activity implements Constants {
    private final String TAG = "VideoShowcaseActivity";
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoshowcase);

        if (HIDE_SYSTEM_UI) {
            hideSystemUi();
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if (visibility == 0) {
                        mHandler.postDelayed(mHideRunnable, 2000);
                    }
                }
            });
        }

        if (KEEP_SCREEN_ON) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (USE_WAKE_LOCK) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
            wakeLock.acquire();
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ShowcaseFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        // We doing this too stop user from exiting app, normally.
        // super.onBackPressed();
    }

    @Override
    protected void onPause() {
        // For long press home button (recent app activity or google now) or recent app button ...
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ShowcaseFragment extends Fragment {
        private final String TAG = "VideoShowcaseActivity#ShowcaseFragment";

        private ArrayList<ShowcaseItem> showcaseItems = null;

        private Handler mHandler = new Handler();
        private long lastTouchMs = 0;
        private int touchCount = 0;

        public ShowcaseFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_videoshowcase, container, false);

            final String dir = Tools.getSdCardDirectory();
            showcaseItems = getShowcaseFileListFromSDCard();
            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.LINK, dir, "https://www.aerzte-ohne-grenzen.at/spenden", "thumbnail_donate.png", "Jetzt Spenden"));
            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.LINK, dir, "http://www.break-the-silence.at", "thumbnail_breakthesilence.png", "Break The Silence"));
            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.LINK, dir, "https://www.aerzte-ohne-grenzen.at/newsletter/newsletter-abonnieren", "thumbnail_newsletter.png", "Newsletter Abonnieren"));

            GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
            gridview.setAdapter(new VideoGridAdapter(getActivity(), showcaseItems));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    ShowcaseItem item = showcaseItems.get(position);
                    Log.v(TAG, String.format("onListItemClick: pos=%s, uri=%s", position, item.getResourceUri()));

                    if (item.type == ShowcaseItem.SHOWCASE_ITEM_TYPE.VIDEO_LOCAL) {
                        // Prepare Video Playback Intent
                        Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW);
                        intentToPlayVideo.setDataAndType(item.getResourceUri(), "video/*");
                        intentToPlayVideo.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);

                        // Check and launch Intent
                        if (Tools.isCallable(getActivity(), intentToPlayVideo)) {
                            startActivity(intentToPlayVideo);
                        } else {
                            Toast.makeText(getActivity(), "No app can handle this file", Toast.LENGTH_LONG).show();
                        }

                    } else if (item.type == ShowcaseItem.SHOWCASE_ITEM_TYPE.LINK) {
                        Intent intent = new Intent(getActivity(), BrowserActivity.class);
                        intent.putExtra("url", item.getResourceString());
                        startActivity(intent);

                    }
                }
            });

            // Add Exit handler
            ImageView ivMsf = (ImageView) rootView.findViewById(R.id.img_msf);
            ivMsf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastTouchMs > 0) {
                        long msDiff = System.currentTimeMillis() - lastTouchMs;
                        touchCount = (msDiff > 1000) ? 0 : ++touchCount;
                        Log.v(TAG, "" + touchCount);

                        if (touchCount > 8) {
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.postDelayed(new ExitDialogRunnable(), 1000);
                        }
                    }
                    lastTouchMs = System.currentTimeMillis();
                }
            });
            return rootView;
        }

        public class ExitDialogRunnable implements Runnable {
            @Override
            public void run() {
                final EditText input = new EditText(getActivity());
                input.setHint(getString(R.string.password));
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.dialog_title_exit))
                        .setView(input)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String s = input.getText().toString();
                                if (s.equals(PASSWORD_EXIT)) {
                                    getActivity().finish();
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();
            }
        }

        private ArrayList<ShowcaseItem> getShowcaseFileListFromSDCard() {
            ArrayList<ShowcaseItem> items = new ArrayList<ShowcaseItem>();

            String dir = Tools.getSdCardDirectory();
            Log.v(TAG, "sd dir: " + dir);
            File root = new File(dir);

            ArrayList<String> fileList = Tools.getFiles(root);

            // Build Showcase Items
            for (int i = 0; i < fileList.size(); i++) {
                String fn = fileList.get(i);
                if (fn.startsWith("video")) {
                    String videoId = fn.substring(0, fn.indexOf("_"));
                    String thumbFn = "thumbnail_" + videoId + ".png";

                    ShowcaseItem item = new ShowcaseItem(
                            ShowcaseItem.SHOWCASE_ITEM_TYPE.VIDEO_LOCAL,
                            dir, fn, thumbFn, null);

                    items.add(item);
                    Log.v(TAG, "Showcase Item: " + item);
                }
            }

            return items;
        }
    }

    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideSystemUi();
        }
    };

}
