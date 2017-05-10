package com.jddfun.game.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.bean.SortingInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MACHINE on 2017/4/27.
 */

public class MagnateFragmentAdatper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SortingInfo.RichListBean> mDatas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private RecyclerView.ViewHolder mHolder;

    public MagnateFragmentAdatper(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public void appendDatas(List<SortingInfo.RichListBean> richList) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(richList);
        notifyDataSetChanged();
    }

    public void setData(List<SortingInfo.RichListBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        SortingInfo.RichListBean rankingListBean = mDatas.get(position);


        ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_name.setText(String.valueOf(rankingListBean.getNickName()));
        ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_amount.setText(String.valueOf(rankingListBean.getAmount()));

        if (rankingListBean.getIndex() == 1) {
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon.setVisibility(View.VISIBLE);
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon_data.setVisibility(View.GONE);
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon.setImageResource(R.mipmap.first);
        } else if (rankingListBean.getIndex() == 2) {
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon.setVisibility(View.VISIBLE);
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon_data.setVisibility(View.GONE);
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon.setImageResource(R.mipmap.second);
        } else if (rankingListBean.getIndex() == 3) {
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon.setVisibility(View.VISIBLE);
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon_data.setVisibility(View.GONE);
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon.setImageResource(R.mipmap.third);
        } else {
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon.setVisibility(View.GONE);
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon_data.setVisibility(View.VISIBLE);
            ((MagnateFragmentAdatper.MyViewHolder) holder).frag_profit_magnate_icon_data.setText(String.valueOf(rankingListBean.getIndex()));
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adatper_magnate_item, parent, false);
        MagnateFragmentAdatper.MyViewHolder holder = new MagnateFragmentAdatper.MyViewHolder(view);
        return holder;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView frag_profit_magnate_icon;
        private final TextView frag_profit_magnate_icon_data;
        private final TextView frag_profit_magnate_name;
        private final TextView frag_profit_magnate_amount;

        public MyViewHolder(View view) {
            super(view);
            frag_profit_magnate_icon = (ImageView) view.findViewById(R.id.frag_profit_magnate_icon);
            frag_profit_magnate_icon_data = (TextView) view.findViewById(R.id.frag_profit_magnate_icon_data);
            frag_profit_magnate_name = (TextView) view.findViewById(R.id.frag_profit_magnate_name);
            frag_profit_magnate_amount = (TextView) view.findViewById(R.id.frag_profit_magnate_amount);
        }
    }
}