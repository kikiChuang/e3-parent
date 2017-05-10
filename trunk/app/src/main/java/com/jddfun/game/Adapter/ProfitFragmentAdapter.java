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

public class ProfitFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SortingInfo.RankingListBean> mDatas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private RecyclerView.ViewHolder mHolder;

    public ProfitFragmentAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public void appendDatas(List<SortingInfo.RankingListBean> data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<SortingInfo.RankingListBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        SortingInfo.RankingListBean rankingListBean = mDatas.get(position);


        ((MyViewHolder)holder).mFrag_profit_personal_name.setText(String.valueOf(rankingListBean.getNickName()));
        ((MyViewHolder)holder).frag_profit_personal_amount.setText(String.valueOf(rankingListBean.getAmount()));
        ((MyViewHolder)holder).frag_profit_personal_awardsName.setText(rankingListBean.getAwardsName());

        if(rankingListBean.getIndex() == 1){
            ((MyViewHolder)holder).mFrag_profit_personal_icon.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).frag_profit_personal_icon_data.setVisibility(View.GONE);
            ((MyViewHolder)holder).mFrag_profit_personal_icon.setImageResource(R.mipmap.first);
        }else if(rankingListBean.getIndex() == 2){
            ((MyViewHolder)holder).mFrag_profit_personal_icon.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).frag_profit_personal_icon_data.setVisibility(View.GONE);
            ((MyViewHolder)holder).mFrag_profit_personal_icon.setImageResource(R.mipmap.second);
        }else if(rankingListBean.getIndex() == 3){
            ((MyViewHolder)holder).mFrag_profit_personal_icon.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).frag_profit_personal_icon_data.setVisibility(View.GONE);
            ((MyViewHolder)holder).mFrag_profit_personal_icon.setImageResource(R.mipmap.third);
        }else{
            ((MyViewHolder)holder).mFrag_profit_personal_icon.setVisibility(View.GONE);
            ((MyViewHolder)holder).frag_profit_personal_icon_data.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).frag_profit_personal_icon_data.setText(String.valueOf(rankingListBean.getIndex()));
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_profit_fragment, parent, false);
        ProfitFragmentAdapter.MyViewHolder holder = new ProfitFragmentAdapter.MyViewHolder(view);
        return holder;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mFrag_profit_personal_icon;
        private final TextView mFrag_profit_personal_name;
        private final TextView frag_profit_personal_amount;
        private final TextView frag_profit_personal_awardsName;
        private final TextView frag_profit_personal_icon_data;

        public MyViewHolder(View view) {
            super(view);
            mFrag_profit_personal_icon = (ImageView) view.findViewById(R.id.frag_profit_personal_icon);
            mFrag_profit_personal_name = (TextView) view.findViewById(R.id.frag_profit_personal_name);
            frag_profit_personal_amount = (TextView) view.findViewById(R.id.frag_profit_personal_amount);
            frag_profit_personal_awardsName = (TextView) view.findViewById(R.id.frag_profit_personal_awardsName);
            frag_profit_personal_icon_data = (TextView) view.findViewById(R.id.frag_profit_personal_icon_data);


        }

    }
}
