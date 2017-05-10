package com.jddfun.game.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jddfun.game.R;
import com.jddfun.game.Utils.GlideImgManager;

import java.util.ArrayList;

/**
 * Created by MACHINE on 2017/4/13.
 */

public class PictureAdapter extends BaseAdapter {


//    private final ArrayList<Bitmap> mPathBitmap;
    private ArrayList<String> ims;
    private final Context context;

    public PictureAdapter(Context context, ArrayList<String> list) {
        this.ims = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        if (ims.size() == 3) {
            return 3;
        }
        return ims.size()+1;
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
        if(position == ims.size()){
            viewHolder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.pic));
        }else{
            GlideImgManager.glideLoader(context, ims.get(position), viewHolder.image, 1);
        }
        return convertView;
    }

    public void updata(ArrayList<String> list) {
        this.ims = list;
        notifyDataSetChanged();
    }
    class ViewHolder {
        public ImageView image;
    }

}



