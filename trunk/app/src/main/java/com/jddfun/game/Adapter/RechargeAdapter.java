package com.jddfun.game.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.Utils.TimeUtils;
import com.jddfun.game.bean.RechargeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MACHINE on 2017/4/8.
 */

public class RechargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RechargeBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public RechargeAdapter(Context context, List<RechargeBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }


    public void setData(List<RechargeBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    public void appendDatas(List<RechargeBean> data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyViewHolder mholder = (MyViewHolder) holder;
        RechargeBean mRechargeBean = mDatas.get(position);
        mholder.mRecharge_adapter_time.setText(mRechargeBean.getCreateTime() != null ? mRechargeBean.getCreateTime().split(" ")[1].substring(0, 5) : "");
        String timeMoon = mRechargeBean.getCreateTime() != null ? mRechargeBean.getCreateTime().substring(5, 11) : "";
        mholder.mRecharge_adapter_money.setText("¥" + String.valueOf(mRechargeBean.getThirdAmount()));
        mholder.mRecharge_adapter_type.setText("类型："+mRechargeBean.getName());
        mholder.mRecharge_adapter_money_type.setText(mRechargeBean.getStatus());

        if (timeMoon != "" && !"".equals(mRechargeBean.getCreateTime())) {
            if (TimeUtils.judgeTime(mRechargeBean.getCreateTime(), TimeUtils.TimeType)) {
                mholder.mRecharge_adapter_date.setText("今日");
            } else {
                mholder.mRecharge_adapter_date.setText(timeMoon.replace(".", "/").substring(1, timeMoon.replace(".", "/").length()).replace("-", "/"));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recharge_adapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mRecharge_adapter_time;
        TextView mRecharge_adapter_date;
        ImageView mRecharge_adapter_img;
        TextView mRecharge_adapter_money;
        TextView mRecharge_adapter_type;
        TextView mRecharge_adapter_money_type;
        private final View mRecharge_view;

        public MyViewHolder(View view) {
            super(view);
            mRecharge_adapter_time = (TextView) view.findViewById(R.id.recharge_adapter_time);
            mRecharge_adapter_date = (TextView) view.findViewById(R.id.recharge_adapter_date);
            mRecharge_adapter_img = (ImageView) view.findViewById(R.id.recharge_adapter_img);
            mRecharge_adapter_money = (TextView) view.findViewById(R.id.recharge_adapter_money);
            mRecharge_adapter_type = (TextView) view.findViewById(R.id.recharge_adapter_type);
            mRecharge_adapter_money_type = (TextView) view.findViewById(R.id.recharge_adapter_money_type);
            mRecharge_view = view.findViewById(R.id.recharge_view);
        }

    }
}


