package com.bitsworking.videoshowcase;

import android.app.Activity;
import android.app.Fragment;
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

            WebView webview = (WebView) rootView.findViewById(R.id.webView);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setWebViewClient(new LimitingWebViewClient());
            webview.loadUrl(getArguments().getString("url"));
            return rootView;
        }

        // Don't allow external links
        private class LimitingWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.v(TAG, "host: " + Uri.parse(url).getHost());

                if ((Uri.parse(url).getHost().equals("www.aerzte-ohne-grenzen.at")) || (Uri.parse(url).getHost().equals("www.break-the-silence.at"))) {
                    // This is my web site, so do not override; let my WebView load the page
                    return false;
                }

                // Otherwise, the link is not for a page on my site, so do nothing
                Toast.makeText(getActivity(), "Outside links are not supported", Toast.LENGTH_LONG).show();
                return true;
            }
        }
    }
}
