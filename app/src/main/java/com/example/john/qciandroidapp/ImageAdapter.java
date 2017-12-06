package com.example.john.qciandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context c) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            grid = new View(mContext);
            grid = layoutInflater.inflate(R.layout.gridlayout, null);
            grid.setLayoutParams(new GridView.LayoutParams(450, 450));
            grid.setPadding(8, 8, 8, 8);
        } else {
            grid = (View) convertView;
        }
        ImageView imageView = (ImageView)grid.findViewById(R.id.image);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView textView = (TextView)grid.findViewById(R.id.text);
        textView.setText(String.valueOf(mTextIds[position]));
        return grid;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.qci_events, R.drawable.qci_calendar,
            R.drawable.qci_jobs, R.drawable.qci_interview,
            R.drawable.qci_resources, R.drawable.qci_settings
    };

    private String[] mTextIds = {
            "Events", "Appointments", "Jobs@Sheridan", "Mock Interview",
            "Resources", "Settings"
    };
}