package com.bitsworking.videoshowcase;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bitsworking.videoshowcase.datatypes.ShowcaseItem;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Chris Hager <chris@linuxuser.at> on 07/09/14.
 */
public class VideoShowcaseActivity extends Activity implements Constants {
    private final String TAG = "VideoShowcaseActivity";

    private ArrayList<String> fileList = null;
    private ArrayList<ShowcaseItem> showcaseItems = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoshowcase);

        updateShowcaseItems();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_videoshowcase, container, false);
            return rootView;
        }
    }

    private void updateShowcaseItems() {
        fileList = new ArrayList<String>();
        showcaseItems = new ArrayList<ShowcaseItem>();

        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + SD_DIRECTORY_VIDEOS;
        Log.v(TAG, "sd dir: " + dir);
        File root = new File(dir);

        getFiles(root);

        // Build SHOWCASE_ITEMS
        for (int i=0; i<fileList.size(); i++) {
            String fn = fileList.get(i);
//            Log.v(TAG, "file: " + fn);
            if (fn.startsWith("video")) {
                String videoId = fn.substring(0, fn.indexOf("_"));
                String thumbFn = "thumbnail_" + videoId + ".png";

                ShowcaseItem item = new ShowcaseItem(
                        ShowcaseItem.SHOWCASE_ITEM_TYPE.VIDEO_LOCAL,
                        fn, thumbFn);

                showcaseItems.add(item);
                Log.v(TAG, "Showcase Item: " + item);
            }
        }

//
//
//        try {
//            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.VIDEO_LOCAL, new URI("file:///test.jpg"), new URI("file:///test.mp4")));
//            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.VIDEO_LOCAL, new URI("file:///test2.jpg"), new URI("file:///test2.mp4")));
//            showcaseItems.add(new ShowcaseItem(ShowcaseItem.SHOWCASE_ITEM_TYPE.VIDEO_LOCAL, new URI("file:///test3.jpg"), new URI("file:///test3.mp4")));
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }

    private ArrayList<String> getFiles(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
//                    fileList.add(listFile[i].getName());
//                    getFiles(listFile[i]);

                } else {
//                    if (listFile[i].getName().endsWith(".png")
//                            || listFile[i].getName().endsWith(".jpg")
//                            || listFile[i].getName().endsWith(".jpeg")
//                            || listFile[i].getName().endsWith(".gif"))
//
//                    {
                        fileList.add(listFile[i].getName());
//                    }
                }

            }
        }
        return fileList;
    }
}
