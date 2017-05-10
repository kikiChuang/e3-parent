package com.jddfun.game.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jddfun.game.Act.BigPictureAct;
import com.jddfun.game.R;
import com.jddfun.game.Utils.BaseUtil;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.View.RoundedImageView;
import com.jddfun.game.bean.CoterielistBean;
import com.jddfun.game.bean.PraiseInfo;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 全部
 * Created by MACHINE on 2017/4/12.
 */

public class AllMesFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CoterielistBean.ListBean> mDatas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private int quantity;
    Boolean flag = true;
    RecyclerView.ViewHolder mHode;
    private boolean showBottom = false;


    public AllMesFragmentAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);

    }

    public void appendDatas(List<CoterielistBean.ListBean> data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<CoterielistBean.ListBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
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

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position == mDatas.size()) {
            return;
        }
        final CoterielistBean.ListBean mMListBean = mDatas.get(position);
        if (null == mMListBean)
            return;
        switch (getItemViewType(position)) {
            case 1:
                ((raiderswHolder) holder).mAllmessage_head.setText(mMListBean.getTitle());
                if (!TextUtils.isEmpty(mMListBean.getRemark())) {
                    ((raiderswHolder) holder).mMAllmessage_context.setText(mMListBean.getRemark().replace("\\", " ").replace("n", "\n"));
                }
                ((raiderswHolder) holder).mCenterf_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((raiderswHolder) holder).mAllmessage_view.getVisibility() == View.GONE) {
                            MobclickAgent.onEvent(mContext, "share_0003");

                            ((raiderswHolder) holder).mAllmessage_view.setVisibility(View.VISIBLE);
                            ((raiderswHolder) holder).mAllmessage_context.setVisibility(View.VISIBLE);
                            ((raiderswHolder) holder).mAllmessage_ash_down.setImageResource(R.mipmap.ash_up);
                            notifyDataSetChanged();

                        } else {
                            ((raiderswHolder) holder).mAllmessage_view.setVisibility(View.GONE);
                            ((raiderswHolder) holder).mAllmessage_context.setVisibility(View.GONE);
                            ((raiderswHolder) holder).mAllmessage_ash_down.setImageResource(R.mipmap.ash_down);
                        }
                    }
                });
                break;
            case 2:
                ((MyViewHolder) holder).all_mes_title.setText(mMListBean.getTitle());
                ((MyViewHolder) holder).all_mes_context.setText(mMListBean.getRemark());
                ((MyViewHolder) holder).all_mes_help_data.setText(String.valueOf(mMListBean.getPraise()));
                if (mMListBean.getPlatCoterieImgList() != null && mMListBean.getPlatCoterieImgList().size() != 0 && !"".equals(mMListBean.getPlatCoterieImgList().get(0).getSmall())) {
                    BitmapTypeRequest builder = Glide.with(((MyViewHolder) holder).all_mes_Img.getContext()).load(mMListBean.getPlatCoterieImgList().get(0).getSmall()).asBitmap();
                    builder.diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.tacitly_approve).error(R.mipmap.tacitly_approve).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ((MyViewHolder) holder).all_mes_Img.setImageBitmap(resource);
                        }
                    });
                }

                ((MyViewHolder) holder).mAll_mes_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //攻略点击
                        MobclickAgent.onEvent(mContext, "share_0004");
                        JDDUtils.jumpToH5Activity(mContext, Constants.SHARE_DETAIL + mMListBean.getId(), "攻略详情",false);
                    }
                });

                if (mMListBean.getHavePraise() == 0) {
                    ((MyViewHolder) holder).all_mes_help_data.setTextColor(Color.parseColor("#808080"));
                    ((MyViewHolder) holder).mAll_mes_my_view_help_ok.setImageResource(R.mipmap.help);
                } else {
                    ((MyViewHolder) holder).all_mes_help_data.setTextColor(Color.parseColor("#ff524c"));
                    ((MyViewHolder) holder).mAll_mes_my_view_help_ok.setImageResource(R.mipmap.help_ok);
                }

                ((MyViewHolder) holder).mFrag_allmess_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWorkData(mDatas.get(position).getId(), position);
                    }
                });
                break;
            case 3:
                ((tipsHolder) holder).mIv_all_mes_two_name.setText(mMListBean.getNickName());
                ((tipsHolder) holder).mIv_all_mes_two_context.setText(mMListBean.getRemark());
                ((tipsHolder) holder).mIv_all_mes_two_help.setText(String.valueOf(mMListBean.getPraise()));
                ((tipsHolder) holder).mIv_all_mes_two_time.setText(mMListBean.getCreateTime());
                ((tipsHolder) holder).mIv_all_mes_two_auditing.setVisibility(View.GONE);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)(((tipsHolder) holder).mAll_mess_rl.getLayoutParams());

                if (!"".equals(mMListBean.getHeadImg())) {
                    Glide.with(mContext).load(mMListBean.getHeadImg()).into(((tipsHolder) holder).mIv_all_mes_two_icon);
                }

                if (mMListBean.getAwardsName().equals("0") || "".equals(mMListBean.getAwardsName())) {
                    ((tipsHolder) holder).mAll_mes_awardsName.setVisibility(View.GONE);
                    lp.setMargins(0, BaseUtil.dip2Px(mContext,10),0,0);
                    ((tipsHolder) holder).mAll_mess_rl.setLayoutParams(lp);
                } else {
                    ((tipsHolder) holder).mAll_mes_awardsName.setVisibility(View.VISIBLE);
                    ((tipsHolder) holder).mAll_mes_awards.setText("推荐·荣获" + mMListBean.getAwardsName() + "金叶子");
                    lp.setMargins(0, BaseUtil.dip2Px(mContext,25),0,0);
                    ((tipsHolder) holder).mAll_mess_rl.setLayoutParams(lp);
                }

                ((tipsHolder) holder).mIv_all_mes_two_context.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flag) {
                            flag = false;
                            ((tipsHolder) holder).mIv_all_mes_two_context.setEllipsize(null);
                            ((tipsHolder) holder).mIv_all_mes_two_context.setMaxLines(50);
                            notifyDataSetChanged();
                        } else {
                            flag = true;
                            ((tipsHolder) holder).mIv_all_mes_two_context.setEllipsize(TextUtils.TruncateAt.END);
                            ((tipsHolder) holder).mIv_all_mes_two_context.setMaxLines(3);
                        }
                    }
                });

                List<CoterielistBean.ListBean.PlatCoterieImgListBean> mlist = mMListBean.getPlatCoterieImgList();
                final List<String> strings = new ArrayList<>();
                for (int i = 0; i < mlist.size(); i++) {
                    strings.add(mlist.get(i).getOriginal());
                }
                ((tipsHolder) holder).mAll_mes_gridView.setAdapter(new AllMesGradAdapter(mContext, strings));

                if (mMListBean.getHavePraise() == 0) {
                    ((tipsHolder) holder).mIv_all_mes_two_help.setTextColor(Color.parseColor("#808080"));
                    ((tipsHolder) holder).mIv_all_mes_two_help_img.setImageResource(R.mipmap.help);
                } else {
                    ((tipsHolder) holder).mIv_all_mes_two_help.setTextColor(Color.parseColor("#ff524c"));
                    ((tipsHolder) holder).mIv_all_mes_two_help_img.setImageResource(R.mipmap.help_ok);
                }
                ((tipsHolder) holder).mAll_mess_hele.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWorkData(mDatas.get(position).getId(), position);
                    }
                });

                ((tipsHolder) holder).mAll_mes_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mContext.startActivity(new Intent(mContext, BigPictureAct.class).putExtra("url",strings.get(position)));
                    }
                });

                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == mDatas.size()) {
            return 4;
        } else {
            return mDatas.get(position).getType();
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = inflater.inflate(R.layout.raiders_item, parent, false);
                mHode = new raiderswHolder(view);
                break;
            case 2:
                view = inflater.inflate(R.layout.all_mes_fragment_adapter, parent, false);
                mHode = new MyViewHolder(view);
                break;
            case 3:
                view = inflater.inflate(R.layout.all_mes_fragment_two, parent, false);
                mHode = new tipsHolder(view);
                break;
            case 4:
                view = inflater.inflate(R.layout.item_buttom, parent, false);
                mHode = new FootHolder(view);
                break;
        }
        return mHode;
    }

    class raiderswHolder extends RecyclerView.ViewHolder {
        private final TextView mAllmessage_head;
        private final TextView mMAllmessage_context;
        private final RelativeLayout mCenterf_rl;
        private final ImageView mAllmessage_ash_down;
        private final View mAllmessage_view;
        private final TextView mAllmessage_context;

        public raiderswHolder(View view) {
            super(view);
            mAllmessage_head = (TextView) view.findViewById(R.id.allmessage_head);
            mMAllmessage_context = (TextView) view.findViewById(R.id.allmessage_context);
            mCenterf_rl = (RelativeLayout) view.findViewById(R.id.centerf_rl);
            mAllmessage_ash_down = (ImageView) view.findViewById(R.id.allmessage_ash_down);
            mAllmessage_view = view.findViewById(R.id.allmessage_view);
            mAllmessage_context = (TextView) view.findViewById(R.id.allmessage_context);
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView all_mes_title;
        private final TextView all_mes_context;
        private final RoundedImageView all_mes_Img;
        private final TextView all_mes_catch_sight_of_data;
        private final TextView all_mes_help_data;
        private final RelativeLayout mAll_mes_rl;
        private final LinearLayout mAll_mes_ll;
        private final LinearLayout mFrag_allmess_ll;
        private final ImageView mAll_mes_my_view_help_ok;

        public MyViewHolder(View view) {
            super(view);
            all_mes_Img = (RoundedImageView) view.findViewById(R.id.all_mes_Img);
            all_mes_title = (TextView) view.findViewById(R.id.all_mes_title);
            all_mes_context = (TextView) view.findViewById(R.id.all_mes_context);
            all_mes_catch_sight_of_data = (TextView) view.findViewById(R.id.all_mes_catch_sight_of_data);
            all_mes_help_data = (TextView) view.findViewById(R.id.all_mes_help_data);
            mAll_mes_ll = (LinearLayout) view.findViewById(R.id.all_mes_ll);
            mAll_mes_rl = (RelativeLayout) view.findViewById(R.id.all_mes_rl);
            mFrag_allmess_ll = (LinearLayout) view.findViewById(R.id.frag_allmess_ll);
            mAll_mes_my_view_help_ok = (ImageView) view.findViewById(R.id.all_mes_my_view_help_ok);

        }

    }


    class tipsHolder extends RecyclerView.ViewHolder {
        private final CircleImageView mIv_all_mes_two_icon;
        private final TextView mIv_all_mes_two_name;
        private final TextView mIv_all_mes_two_context;
        private final TextView mIv_all_mes_two_time;
        private final TextView mIv_all_mes_two_help;
        private final LinearLayout mAll_mes_awardsName;
        private final GridView mAll_mes_gridView;
        private final LinearLayout mAll_mess_hele;
        private final TextView mIv_all_mes_two_auditing;
        private final TextView mAll_mes_awards;
        private final RelativeLayout mAll_mess_rl;
        private final ImageView mIv_all_mes_two_help_img;

        public tipsHolder(View view) {
            super(view);

            mIv_all_mes_two_icon = (CircleImageView) view.findViewById(R.id.iv_all_mes_two_icon);
            mIv_all_mes_two_name = (TextView) view.findViewById(R.id.iv_all_mes_two_name);
            mIv_all_mes_two_context = (TextView) view.findViewById(R.id.iv_all_mes_two_context);
            mIv_all_mes_two_time = (TextView) view.findViewById(R.id.iv_all_mes_two_time);
            mIv_all_mes_two_help = (TextView) view.findViewById(R.id.iv_all_mes_two_help);
            mAll_mes_gridView = (GridView) view.findViewById(R.id.all_mes_GridView);
            mAll_mess_hele = (LinearLayout) view.findViewById(R.id.all_mess_hele);
            mAll_mes_awardsName = (LinearLayout) view.findViewById(R.id.all_mes_awardsName);
            mIv_all_mes_two_auditing = (TextView) view.findViewById(R.id.iv_all_mes_two_Auditing);
            mAll_mes_awards = (TextView) view.findViewById(R.id.all_mes_awards);
            mAll_mess_rl = (RelativeLayout) view.findViewById(R.id.all_mess_rl);
            mIv_all_mes_two_help_img = (ImageView) view.findViewById(R.id.iv_all_mes_two_help_img);
        }
    }



    class FootHolder extends RecyclerView.ViewHolder {

        public FootHolder(View itemView) {
            super(itemView);
        }
    }
    //点赞
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
                        if (code == 401) {
                            UtilsTools.getInstance().show("用户没有登录");
                        }
                    }
                });
    }
}
