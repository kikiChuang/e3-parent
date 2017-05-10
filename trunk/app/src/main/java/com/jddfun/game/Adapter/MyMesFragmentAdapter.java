package com.jddfun.game.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Act.BigPictureAct;
import com.jddfun.game.R;
import com.jddfun.game.Utils.BaseUtil;
import com.jddfun.game.Utils.GlideImgManager;
import com.jddfun.game.bean.CoteriemineInfo;
import com.jddfun.game.bean.PraiseInfo;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.jddfun.game.R.id.iv_all_mes_two_help;


/**
 * 我的
 * Created by MACHINE on 2017/4/12.
 */

public class MyMesFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CoteriemineInfo.ListBean> mDatas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    boolean flag = true;
    private boolean showBottom = false;
    public static int TYPE_NOMRAL = 1;
    public static int TYPE_BOTTOM = 2;
    private RecyclerView.ViewHolder mHolder;

    public MyMesFragmentAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getItemCount() {
        if (showBottom) {
            return mDatas.size() + 1;
        } else {
            return mDatas.size();
        }
    }


    public void showBottom(boolean showBottom) {
        this.showBottom = showBottom;
        notifyDataSetChanged();
    }

    public void appendDatas(List<CoteriemineInfo.ListBean> data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<CoteriemineInfo.ListBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDatas.size()) {
            return TYPE_BOTTOM;
        } else {
            return TYPE_NOMRAL;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position == mDatas.size()) {
            return;
        }
        final MyViewHolder holders = (MyViewHolder) holder;
        CoteriemineInfo.ListBean mMListBean = mDatas.get(position);

        GlideImgManager.glideLoader(mContext, mMListBean.getHeadImg(), holders.mIv_all_mes_two_icon, 0);
        holders.mIv_all_mes_two_context.setText(mMListBean.getRemark());
        holders.mIv_all_mes_two_help.setText(String.valueOf(mMListBean.getPraise()));
        holders.mIv_all_mes_two_time.setText(mMListBean.getCreateTime());
        holders.mIv_all_mes_two_name.setText(mMListBean.getNickName());
        holders.mIv_all_mes_two_auditing.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holders.mAll_mess_rl.getLayoutParams();

        if ("".equals(mMListBean.getAwardsName()) || mMListBean.getAwardsName().equals("0")) {
            holders.mAll_mes_awardsName.setVisibility(View.GONE);
            layoutParams.setMargins(0, BaseUtil.dip2Px(mContext, 10), 0, 0);
            holders.mAll_mess_rl.setLayoutParams(layoutParams);
        } else {
            holders.mAll_mes_awards.setText("推荐·荣获" + mMListBean.getAwardsName() + "金叶子");
            holders.mAll_mes_awardsName.setVisibility(View.VISIBLE);
            layoutParams.setMargins(0, BaseUtil.dip2Px(mContext, 25), 0, 0);
            holders.mAll_mess_rl.setLayoutParams(layoutParams);
        }

        List<CoteriemineInfo.ListBean.PlatCoterieImgListBean> mMlist = mMListBean.getPlatCoterieImgList();
        final List<String> mStrings = new ArrayList<>();
        for (int i = 0; i < mMlist.size(); i++) {
            mStrings.add(mMlist.get(i).getOriginal());
        }
        holders.mAll_mes_gridView.setAdapter(new AllMesGradAdapter(mContext, mStrings));

        holders.mIv_all_mes_two_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    flag = false;
                    holders.mIv_all_mes_two_context.setEllipsize(null);
                    holders.mIv_all_mes_two_context.setMaxLines(50);
                    notifyDataSetChanged();
                } else {
                    flag = true;
                    holders.mIv_all_mes_two_context.setEllipsize(TextUtils.TruncateAt.END);
                    holders.mIv_all_mes_two_context.setMaxLines(3);
                }
            }
        });

        if (mMListBean.getHavePraise() == 1) {
            holders.mIv_all_mes_two_help.setTextColor(Color.parseColor("#ff524c"));
            holders.mIv_all_mes_two_help_img.setImageResource(R.mipmap.help_ok);
        } else {
            holders.mIv_all_mes_two_help.setTextColor(Color.parseColor("#808080"));
            holders.mIv_all_mes_two_help_img.setImageResource(R.mipmap.help);
            holders.mAll_mess_hele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getWorkData(mDatas.get(position).getId(), position);
                }
            });
        }

        if (mMListBean.getStatus() == 1) {
            holders.mIv_all_mes_two_auditing.setText("待审核");
            holders.mIv_all_mes_two_auditing.setBackgroundResource(R.drawable.act_identifying_pitch);
            holders.mAll_mess_hele.setClickable(false);
        } else if (mMListBean.getStatus() == 3) {
            holders.mIv_all_mes_two_auditing.setText("审核不通过");
            holders.mIv_all_mes_two_auditing.setBackgroundResource(R.drawable.act_identifying_code);
            holders.mAll_mess_hele.setClickable(false);
        } else if (mMListBean.getStatus() == 2) {
            holders.mIv_all_mes_two_auditing.setVisibility(View.GONE);
            holders.mAll_mess_hele.setClickable(true);
        }


        holders.mAll_mes_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.startActivity(new Intent(mContext, BigPictureAct.class).putExtra("url",mStrings.get(position)));
            }
        });

    }


    public void getWorkData(int Value, final int pos) {
        PraiseInfo mPraiseInfo = new PraiseInfo();
        mPraiseInfo.setValue(Value);
        ServiceFactory.getInstance().createService(JDDApiService.class).praise(mPraiseInfo)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(((RxAppCompatActivity) mContext).<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String list) {
                        mDatas.get(pos).setHavePraise(1);
                        mDatas.get(pos).setPraise(mDatas.get(pos).getPraise() + 1);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_NOMRAL) {
            view = inflater.inflate(R.layout.all_mes_fragment_two, parent, false);
            mHolder = new MyViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_buttom, parent, false);
            mHolder = new FootHolder(view);
        }
        return mHolder;
    }

    class FootHolder extends RecyclerView.ViewHolder {

        public FootHolder(View itemView) {
            super(itemView);
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView mIv_all_mes_two_icon;
        private final TextView mIv_all_mes_two_name;
        private final TextView mIv_all_mes_two_context;
        private final TextView mIv_all_mes_two_time;
        private final TextView mIv_all_mes_two_help;
        private final LinearLayout mAll_mes_awardsName;
        private final GridView mAll_mes_gridView;
        private final LinearLayout mAll_mess_hele;
        private final ImageView mIv_all_mes_two_help_img;
        private final TextView mIv_all_mes_two_auditing;
        private final LinearLayout mAmm_mes_frag_ll;
        private final TextView mAll_mes_awards;
        private final RelativeLayout mAll_mess_rl;

        public MyViewHolder(View view) {
            super(view);
            mIv_all_mes_two_icon = (CircleImageView) view.findViewById(R.id.iv_all_mes_two_icon);
            mIv_all_mes_two_name = (TextView) view.findViewById(R.id.iv_all_mes_two_name);
            mIv_all_mes_two_context = (TextView) view.findViewById(R.id.iv_all_mes_two_context);
            mIv_all_mes_two_time = (TextView) view.findViewById(R.id.iv_all_mes_two_time);
            mIv_all_mes_two_help = (TextView) view.findViewById(iv_all_mes_two_help);
            mAll_mes_gridView = (GridView) view.findViewById(R.id.all_mes_GridView);
            mAll_mes_awardsName = (LinearLayout) view.findViewById(R.id.all_mes_awardsName);
            mAll_mess_hele = (LinearLayout) view.findViewById(R.id.all_mess_hele);
            mIv_all_mes_two_help_img = (ImageView) view.findViewById(R.id.iv_all_mes_two_help_img);
            mIv_all_mes_two_auditing = (TextView) view.findViewById(R.id.iv_all_mes_two_Auditing);
            mAmm_mes_frag_ll = (LinearLayout) view.findViewById(R.id.amm_mes_frag_ll);
            mAll_mes_awards = (TextView) view.findViewById(R.id.all_mes_awards);
            mAll_mess_rl = (RelativeLayout) view.findViewById(R.id.all_mess_rl);
        }
    }
}
