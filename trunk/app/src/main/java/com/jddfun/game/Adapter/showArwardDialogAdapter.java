package com.jddfun.game.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.bean.ProfitRulesBean;

import java.util.List;

/**
 * Created by MACHINE on 2017/4/11.
 */

public class showArwardDialogAdapter extends RecyclerView.Adapter<showArwardDialogAdapter.MyViewHolder> {

    private List<ProfitRulesBean.RuleDetailsBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public showArwardDialogAdapter(Context context,List<ProfitRulesBean.RuleDetailsBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final showArwardDialogAdapter.MyViewHolder holder, final int position) {
        ProfitRulesBean.RuleDetailsBean ruleDetailsBean = mDatas.get(position);
        holder.centerf_condition.setText(ruleDetailsBean.getCondition());
        holder.centerf_awardsName.setText(ruleDetailsBean.getAwardsName());

    }

    @Override
    public showArwardDialogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.show_arward_dialog_adapter, parent, false);
        showArwardDialogAdapter.MyViewHolder holder = new showArwardDialogAdapter.MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        private final TextView centerf_condition;
        private final TextView centerf_awardsName;

        public MyViewHolder(View view) {
            super(view);
            centerf_condition = (TextView) view.findViewById(R.id.centerf_condition);
            centerf_awardsName = (TextView) view.findViewById(R.id.centerf_awardsName);
        }

    }
}
