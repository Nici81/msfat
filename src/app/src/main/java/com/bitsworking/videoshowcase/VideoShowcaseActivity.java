package com.bitsworking.videoshowcase;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoshowcase);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ShowcaseFragment())
                    .commit();
        }
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
    public static class ShowcaseFragment extends ListFragment {
        private final String TAG = "VideoShowcaseActivity#ShowcaseFragment";

        private ArrayList<ShowcaseItem> showcaseItems = null;
        private ShowcaseItemAdapter listAdapter = null;

        public ShowcaseFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_videoshowcase, container, false);

            showcaseItems = getShowcaseFileListFromSDCard();
            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.LINK, null, "https://www.aerzte-ohne-grenzen.at/spenden", null, "Jetzt Spenden"));
            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.LINK, null, "http://www.break-the-silence.at", null, "Break The Silence"));
            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.INTERNAL, null, "newsletter", null, "Newsletter"));

            listAdapter = new ShowcaseItemAdapter(getActivity().getBaseContext(), showcaseItems);
            setListAdapter(listAdapter);

            return rootView;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id){
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

        public class ShowcaseItemAdapter extends ArrayAdapter<ShowcaseItem> {
            public ShowcaseItemAdapter(Context context, ArrayList<ShowcaseItem> showcaseItems) {
                super(context, R.layout.listitem_showcase, showcaseItems);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the data item for this position
                ShowcaseItem item = getItem(position);

                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_showcase, parent, false);
                }

                // Lookup view for data population
                TextView tv0 = (TextView) convertView.findViewById(R.id.text);
                tv0.setText(item.text);

                ImageView iv0 = (ImageView) convertView.findViewById(R.id.thumbnail);
                if (item.getThumbnailFullFilename() == null) {
                    iv0.setImageBitmap(null);
                    iv0.setVisibility(View.GONE);
                } else {
                    Bitmap bmp = BitmapFactory.decodeFile(item.getThumbnailFullFilename());
                    iv0.setImageBitmap(bmp);
                    iv0.setVisibility(View.VISIBLE);
                }

                // Populate the data into the template view using the data object
//                tv1.setText(item.directory + item.resourceFn);
//                tv2.setText(item.directory + item.thumbnailFn);

                // Return the completed view to render on screen
                return convertView;
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
}
