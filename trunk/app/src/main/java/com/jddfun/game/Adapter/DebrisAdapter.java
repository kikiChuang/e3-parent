package com.jddfun.game.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jddfun.game.R;
import com.jddfun.game.bean.Debris;
import com.jddfun.game.dialog.CommonDialog;
import com.jddfun.game.dialog.DialogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhongliang on 2017/4/26.
 */
public class DebrisAdapter extends RecyclerView.Adapter {

    private List<Debris> listData = new ArrayList<>();

    private RxAppCompatActivity context;

    public DebrisAdapter(RxAppCompatActivity context) {
        this.context = context;
    }

    public void setData(List<Debris> datas) {
        listData.clear();
        listData.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debris_item, parent, false);
        return new DebrisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DebrisViewHolder) holder).bindView(position, listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private class DebrisViewHolder extends RecyclerView.ViewHolder {

        private ImageView debris_icon;
        private TextView debris_count;

        public DebrisViewHolder(View view) {
            super(view);
            initView();
        }

        private void initView() {
            debris_icon = (ImageView) itemView.findViewById(R.id.debris_icon);
            debris_count = (TextView) itemView.findViewById(R.id.debris_count);
        }

        public void bindView(final int position, final Debris debris) {
            Glide.with(context).load(debris.getImage()).into(debris_icon);
            if (debris.canCompound()) {
                debris_count.setText(getSpannableString(String.valueOf(debris.getFragmentNum()), "/" + debris.getChangeNum()));
            } else {
                debris_count.setText(debris.getFragmentNum()+"/" + debris.getChangeNum());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showCompoundDialog(new CommonDialog(context), context, debris, position);
                }
            });

        }

        public SpannableStringBuilder getSpannableString(String str1, String str2) {
            String content;
            content = str1 + str2;
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            SpannableString dss;
            dss = new SpannableString(content);
            dss.setSpan(dss, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dss.setSpan(new ForegroundColorSpan(itemView.getContext().getResources().getColor(R.color.theme_color_03)), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dss.setSpan(new ForegroundColorSpan(itemView.getContext().getResources().getColor(R.color.text_color_02)), str1.length(), content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(dss);
            return ssb;
        }

    }


}
