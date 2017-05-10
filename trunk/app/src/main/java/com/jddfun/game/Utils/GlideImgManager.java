package com.jddfun.game.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jddfun.game.R;

/**
 * Created by MACHINE on 2017/4/13.
 */

public class GlideImgManager {
    /**
     * load normal  for img
     *
     * @param url
     * @param iv
     */
    public static void glideLoader(Context context, String url, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(R.mipmap.tacitly_approve).error(R.mipmap.tacitly_approve).into(iv);
    }
    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param iv
     * @param tag
     */
    public static void glideLoader(Context context, String url, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(url).placeholder(R.mipmap.tacitly_approve).error(R.mipmap.tacitly_approve).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(url).placeholder(R.mipmap.tacitly_approve).error(R.mipmap.tacitly_approve).transform(new GlideRoundTransform(context,10)).into(iv);
        }
    }
}
