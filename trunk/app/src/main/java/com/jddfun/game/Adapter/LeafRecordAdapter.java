package com.jddfun.game.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.Utils.TimeUtils;
import com.jddfun.game.bean.BeetingRecordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MACHINE on 2017/4/8.
 */

public class LeafRecordAdapter extends RecyclerView.Adapter<LeafRecordAdapter.MyViewHolder> {

    private List<BeetingRecordBean> mDatas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;

    public LeafRecordAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public void appendDatas(List<BeetingRecordBean> data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<BeetingRecordBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(LeafRecordAdapter.MyViewHolder holder, final int position) {
        BeetingRecordBean mRechargeBean = mDatas.get(position);
        holder.adapter_time.setText(mRechargeBean.getCreateTime().split(" ")[1]);
        holder.adapter_money.setText(mRechargeBean.getName());
        if (mRechargeBean.getChangeMoney() > 0) {
            holder.adapter_money_type.setTextColor(Color.parseColor("#92d152"));
            holder.adapter_money_type.setText("+" + String.valueOf(mRechargeBean.getChangeMoney()));
        } else {
            holder.adapter_money_type.setTextColor(Color.parseColor("#ff524c"));
            holder.adapter_money_type.setText(String.valueOf(mRechargeBean.getChangeMoney()));
        }

        if (TimeUtils.judgeTime(mRechargeBean.getCreateTime(), TimeUtils.TimeType3)) {
            holder.adapter_date.setText("今日");
        } else {
            char[] i = mRechargeBean.getCreateTime().substring(8, 10).toCharArray();
            if (String.valueOf(i[0]).equals("0")) {
                holder.adapter_date.setText(mRechargeBean.getCreateTime().substring(9, 10) + "日");
            } else {
                holder.adapter_date.setText(mRechargeBean.getCreateTime().substring(8, 10) + "日");
            }
        }
    }

    @Override
    public LeafRecordAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.adapter_leaf_record, parent, false);
        LeafRecordAdapter.MyViewHolder holder = new LeafRecordAdapter.MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView adapter_time;
        private final TextView adapter_date;
        private final TextView adapter_money;
        private final TextView adapter_money_type;

        public MyViewHolder(View view) {
            super(view);
            adapter_time = (TextView) view.findViewById(R.id.adapter_time);
            adapter_date = (TextView) view.findViewById(R.id.adapter_date);
            adapter_money = (TextView) view.findViewById(R.id.adapter_money);
            adapter_money_type = (TextView) view.findViewById(R.id.adapter_money_type);
        }

    }
}
