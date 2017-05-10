package com.jddfun.game.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jddfun.game.R;
import com.jddfun.game.Utils.GlideImgManager;

import java.util.List;

/**
 * Created by MACHINE on 2017/4/13.
 */

public class AllMesGradAdapter extends BaseAdapter {


    private List<String> ims;
    private final Context context;

    public AllMesGradAdapter(Context context, List<String> list) {
        this.ims = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return ims.size();
    }

    @Override
    public Object getItem(int position) {
        return ims.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.picture_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            GlideImgManager.glideLoader(context,ims.get(position), viewHolder.image, 1);
        return convertView;
    }
    class ViewHolder {
        public ImageView image;
    }
}

