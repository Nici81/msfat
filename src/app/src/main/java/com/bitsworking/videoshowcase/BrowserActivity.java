package com.bitsworking.videoshowcase;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Chris Hager <chris@linuxuser.at> on 08/09/14.
 */
public class BrowserActivity extends Activity {
    private final static String TAG = "BrowserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (savedInstanceState == null) {
            BrowserFragment fragment = new BrowserFragment();
            fragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
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
    public static class BrowserFragment extends Fragment {

        public BrowserFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_browser, container, false);

            WebView webView = (WebView) rootView.findViewById(R.id.webView);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebViewClient(new LimitingWebViewClient());
//            webView.clearCache(true);

            webView.loadUrl(getArguments().getString("url"));
            return rootView;
        }

        // Don't allow external links
        private class LimitingWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.v(TAG, String.format("url: %s, host: %s", url, Uri.parse(url).getHost()));

                if (Uri.parse(url).getHost() == null) {
                    // Don't allow
                    return true;
                }

                if ((Uri.parse(url).getHost().equals("www.aerzte-ohne-grenzen.at")) || (Uri.parse(url).getHost().equals("www.break-the-silence.at"))) {
                    // Allow
                    return false;
                }

                // Otherwise, the link is not for a page on my site, so disallow
                Toast.makeText(getActivity(), "Outside links are not supported", Toast.LENGTH_LONG).show();
                return true;
            }
        }
    }
}
