package com.weswu.cloudcdn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class QuicGvAdapter extends BaseAdapter {
    private Context mContext;
    private static QuicGrabber quicGrabber;

    public QuicGvAdapter(Context mContext) {
        this.mContext = mContext;
        quicGrabber = new QuicGrabber(mContext);
    }

    @Override
    public int getCount() {
        return Images.count();
    }

    @Override
    public Object getItem(int position) {
        return Images.getUrl(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.quic_image_cell, parent, false);
            holder = new ViewHolder();
            holder.imgv = (ImageView) convertView.findViewById(R.id.quic_image);
            holder.imgv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imgv.setPadding(3, 3, 3, 3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(((GridView2) parent).isOnMeasure) {
            return convertView;
        }
        String url = Images.getUrl(position);
        android.util.Log.i("QuicGvAdapter", " URI: " + url);
        quicGrabber.grabFromUrl(holder.imgv, url);
        return convertView;
    }

    static class ViewHolder
    {
        ImageView imgv;
    }
}
