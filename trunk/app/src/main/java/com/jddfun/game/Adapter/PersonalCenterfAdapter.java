package com.jddfun.game.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.View.MyTabView;
import com.jddfun.game.bean.PushedMessagesBean;
import com.jddfun.game.bean.UpdateOneMessageStatusBean;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 * Created by MACHINE on 2017/4/10.
 */

public class PersonalCenterfAdapter extends RecyclerView.Adapter<PersonalCenterfAdapter.MyViewHolder> {

    private final String mString;
    private List<PushedMessagesBean.MessagesBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private String context = "。点击查询>>";
    private int quantity;

    private MyTabView myTabView;
    private String message_type;

    public PersonalCenterfAdapter(Context context, List<PushedMessagesBean.MessagesBean> datas, String type, int quantity, MyTabView myTabView, String message_type) {
        this.mContext = context;
        this.mDatas = datas;
        mString = type;
        inflater = LayoutInflater.from(mContext);
        this.quantity = quantity;
        this.myTabView = myTabView;
        this.message_type = message_type;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public void setData(List<PushedMessagesBean.MessagesBean> datas, int quantity) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(datas);
        this.quantity = quantity;
        notifyDataSetChanged();

        updateMsgCount();
    }

    public void appendData(List<PushedMessagesBean.MessagesBean> datas, int quantity) {
        if (mDatas != null) {
            mDatas.addAll(datas);
            this.quantity = this.quantity + quantity;
        }
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(final PersonalCenterfAdapter.MyViewHolder holder, final int position) {
        final PushedMessagesBean.MessagesBean mMPushedMessagesBean = mDatas.get(position);
        if (mMPushedMessagesBean.getMessageStatus() == 1) {
            holder.centerf_round_dot.setVisibility(View.INVISIBLE);
        } else {
            holder.centerf_round_dot.setVisibility(View.VISIBLE);
        }
        holder.centerf_head.setText(mMPushedMessagesBean.getTitle());

        if ("".equals(mMPushedMessagesBean.getLinkUrl())) {
            holder.centerf_context.setText(mMPushedMessagesBean.getContent());
            holder.centerf_context.setOnClickListener(null);
        } else {
            holder.centerf_context.setText(mMPushedMessagesBean.getContent() + context);
            SpannableStringBuilder builder = new SpannableStringBuilder(holder.centerf_context.getText().toString());
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#4d81a6"));
            builder.setSpan(blueSpan, mMPushedMessagesBean.getContent().length() + 1, holder.centerf_context.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.centerf_context.setText(builder);
            holder.centerf_context.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JDDUtils.jumpToH5Activity(mContext, mMPushedMessagesBean.getLinkUrl(), "消息详情",false);
                }
            });
        }


        holder.mCenterf_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.centerf_rl: //下展
                        if (holder.mCenterf_view.getVisibility() == View.GONE) {
                            holder.mCenterf_view.setVisibility(View.VISIBLE);
                            holder.centerf_context.setVisibility(View.VISIBLE);
                            holder.centerf_ash_down.setImageResource(R.mipmap.ash_up);
                            if (mMPushedMessagesBean.getMessageStatus() == 0) {
                                getWorkData(mMPushedMessagesBean.getMessageId(), holder.centerf_round_dot);
                            }
                        } else {
                            holder.mCenterf_view.setVisibility(View.GONE);
                            holder.centerf_context.setVisibility(View.GONE);
                            holder.centerf_ash_down.setImageResource(R.mipmap.ash_down);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public PersonalCenterfAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.personal_centerf_adapter, parent, false);
        PersonalCenterfAdapter.MyViewHolder holder = new PersonalCenterfAdapter.MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView centerf_round_dot;
        private final TextView centerf_head;
        private final ImageView centerf_ash_down;
        private final TextView centerf_context;
        private final View mCenterf_view;
        private final RelativeLayout mCenterf_rl;

        public MyViewHolder(View view) {
            super(view);
            centerf_round_dot = (TextView) view.findViewById(R.id.centerf_round_dot);
            centerf_head = (TextView) view.findViewById(R.id.centerf_head);
            centerf_ash_down = (ImageView) view.findViewById(R.id.centerf_ash_down);
            centerf_context = (TextView) view.findViewById(R.id.centerf_context);
            mCenterf_view = view.findViewById(R.id.centerf_view);
            mCenterf_rl = (RelativeLayout) view.findViewById(R.id.centerf_rl);
        }

    }

    private void updateMsgCount() {
        if (null == myTabView)
            return;
        if (TextUtils.equals("1", message_type)) {
            //系统消息
            if (quantity == 0) {
                myTabView.hideMsg(1);
            } else {
                myTabView.showDot(1);
            }

        } else if (TextUtils.equals("2", message_type)) {
            //个人消息
            if (quantity == 0) {
                myTabView.hideMsg(0);
            } else {
                myTabView.showDot(0);
            }
        }


    }

    public void getWorkData(int id, final View view) {
        UpdateOneMessageStatusBean mupdateOneMessageStatusBean = new UpdateOneMessageStatusBean();
        mupdateOneMessageStatusBean.setValue(id);
        ServiceFactory.getInstance().createService(JDDApiService.class).updateOneMessageStatus(mupdateOneMessageStatusBean)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(((RxAppCompatActivity) mContext).<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String list) {
                        view.setVisibility(View.INVISIBLE);
                        quantity = quantity - 1;

                        updateMsgCount();
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }
}
