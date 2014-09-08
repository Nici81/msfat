package com.bitsworking.videoshowcase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bitsworking.videoshowcase.datatypes.ShowcaseItem;

import java.util.ArrayList;

/**
 * Created by Chris Hager <chris@linuxuser.at> on 08/09/14.
 */
public class VideoGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ShowcaseItem> mShowcaseItems;

    public VideoGridAdapter(Context c, ArrayList<ShowcaseItem> showcaseItems) {
        mContext = c;
        mShowcaseItems = showcaseItems;
    }

    public int getCount() {
        return mShowcaseItems.size();
    }

    public Object getItem(int position) {
        return mShowcaseItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ShowcaseItem item = mShowcaseItems.get(position);


        if (convertView == null) {  // if it's not recycled, initialize some attributes
            convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_showcase, parent, false);
        }

        // Lookup view for data population
        TextView tv0 = (TextView) convertView.findViewById(R.id.text);
        tv0.setText(item.text);

        ImageView iv0 = (ImageView) convertView.findViewById(R.id.thumbnail);
        if (item.getThumbnailFullFilename() == null) {
            iv0.setImageBitmap(null);
//            iv0.setVisibility(View.GONE);
        } else {
            Bitmap bmp = BitmapFactory.decodeFile(item.getThumbnailFullFilename());
            iv0.setImageBitmap(bmp);
//            iv0.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
