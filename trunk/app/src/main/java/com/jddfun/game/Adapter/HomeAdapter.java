package com.jddfun.game.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jddfun.game.Act.SotingActivity;
import com.jddfun.game.Act.TaskAct;
import com.jddfun.game.Act.WealAct;
import com.jddfun.game.CustomView.ImageCycleView;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.View.AutoTextView;
import com.jddfun.game.View.PrecontractData;
import com.jddfun.game.View.RoundedImageView;
import com.jddfun.game.bean.Banner;
import com.jddfun.game.bean.Game;
import com.jddfun.game.bean.HomeItemData;
import com.jddfun.game.bean.Notice;
import com.jddfun.game.bean.UserPersonalBean;
import com.jddfun.game.manager.AccountManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhongliang on 2017/4/17.
 */
public class HomeAdapter extends RecyclerView.Adapter {

    public static final int TYPE_FOOT = 3;

    private Handler handler = new Handler();

    private List<HomeItemData> homeList = new ArrayList<>();

    private TextView yezi_icon;
    private TextView head_text;


    public void setData(List<HomeItemData> homeDatas) {
        homeList.clear();
        homeList.addAll(homeDatas);
        HomeItemData item = new HomeItemData();
        item.setType(HomeItemData.TYPE_FOOT);
        homeList.add(item);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (homeList.get(position).getType() == HomeItemData.TYPE_HEAD) {
            return HomeItemData.TYPE_HEAD;
        } else if (homeList.get(position).getType() == HomeItemData.TYPE_GAME) {
            return HomeItemData.TYPE_GAME;
        } else {
            return TYPE_FOOT;
        }
    }


    public void notifyYeZi() {
        UserPersonalBean user = AccountManager.getInstance().getCurrentUser();
        if (user != null) {
            if (yezi_icon != null && head_text != null && homeList != null && homeList.size() > 0 && homeList.get(0).getType() == HomeItemData.TYPE_HEAD) {
                HomeItemData homeItemData = homeList.get(0);
                homeItemData.setUserPersonalBean(user);
                String numStr = String.valueOf(user.getUseAmount());
//                long num = user.getUseAmount();
//                if (num > 10000) {
//                    numStr = num / 10000 + "W";
//                } else {
//                    numStr = num + "";
//                }
                head_text.setText(numStr);
            }
        } else {

            if (yezi_icon != null && head_text != null) {
                head_text.setText("0");
            }
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_header, parent, false);
                return new HeaderViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
                return new GameViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foot, parent, false);
                return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bindView(position);
        } else if (holder instanceof GameViewHolder) {
            ((GameViewHolder) holder).bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageCycleView imageCycle;
        private ImageView iv_msg;
        private com.jddfun.game.View.AutoTextView broad_text;
        private TextView shop_btn;
        private TextView task_btn;
        private TextView rank_btn;
        private TextView prize_btn;
        private TextView weal_btn;
        private View add_icon;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            initView();
        }

        private void initView() {
            head_text = (TextView) itemView.findViewById(R.id.head_text);
            yezi_icon = (TextView) itemView.findViewById(R.id.yezi_icon);
            imageCycle = (ImageCycleView) itemView.findViewById(R.id.imageCycle);
            iv_msg = (ImageView) itemView.findViewById(R.id.iv_msg);
            broad_text = (com.jddfun.game.View.AutoTextView) itemView.findViewById(R.id.broad_text);
            shop_btn = (TextView) itemView.findViewById(R.id.shop_btn);
            task_btn = (TextView) itemView.findViewById(R.id.task_btn);
            rank_btn = (TextView) itemView.findViewById(R.id.rank_btn);
            prize_btn = (TextView) itemView.findViewById(R.id.prize_btn);
            weal_btn = (TextView) itemView.findViewById(R.id.weal_btn);
            add_icon = itemView.findViewById(R.id.add_icon);
        }

        public void bindView(int position) {
            final HomeItemData homeItem = homeList.get(position);
            UserPersonalBean user = homeItem.getUserPersonalBean();
            if (user != null) {
                String numStr = String.valueOf(user.getUseAmount());
//                long num = user.getUseAmount();
//                if (num > 10000) {
//                    numStr = num / 10000 + "W";
//                } else {
//                    numStr = num + "";
//                }
                head_text.setText(numStr);
            } else {
                head_text.setText("0");
            }

            if (homeItem.getBanners() != null && homeItem.getBanners().size() > 0) {
                ImageCycleView.ImageCycleViewListener cycleViewListener = new ImageCycleView.ImageCycleViewListener() {
                    @Override
                    public void displayImage(String imageURL, final ImageView imageView) {
                        BitmapTypeRequest builder = Glide.with(itemView.getContext()).load(imageURL).asBitmap();
                        builder.diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.tacitly_approve).error(R.mipmap.tacitly_approve).into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                imageView.setImageBitmap(resource);
                            }
                        });
                        // GlideImgManager.glideLoader(itemView.getContext(), imageURL, imageView);
                    }

                    @Override
                    public void onImageClick(int position, View imageView) {
                        //首页banner位点击
                        MobclickAgent.onEvent(itemView.getContext(), "home_0001");
                        final Banner banner = homeItem.getBanners().get(position);
                        //跳转H5页面  0 不跳转，1内部跳转，2外部跳转
                        if (banner.getType() != 0) {
                            JDDUtils.jumpToH5Activity(itemView.getContext(), banner.getUrl(), banner.getName(), true);
                        }


                    }
                };
                PrecontractData precontractData = new PrecontractData();
                precontractData.initCarsuelView(precontractData.getDesc(homeItem.getBanners()), precontractData.getUrl(homeItem.getBanners()), imageCycle, cycleViewListener);
                imageCycle.setVisibility(View.VISIBLE);
            } else {
                imageCycle.setVisibility(View.GONE);
            }


            List<Notice> notices = homeItem.getNotices();
            handler.removeCallbacksAndMessages(null);
            if (notices != null && notices.size() > 0) {
                broad_text.setText(Html.fromHtml(notices.get(0).getRemark()));
                handler.postDelayed(new BroadRunnable(broad_text, notices), 3000);
                broad_text.setVisibility(View.VISIBLE);
                iv_msg.setVisibility(View.VISIBLE);
            } else {
                broad_text.setVisibility(View.GONE);
                iv_msg.setVisibility(View.GONE);
            }

            shop_btn.setOnClickListener(this);
            task_btn.setOnClickListener(this);
            rank_btn.setOnClickListener(this);
            prize_btn.setOnClickListener(this);
            weal_btn.setOnClickListener(this);
            add_icon.setOnClickListener(this);


        }

        @Override
        public void onClick(final View v) {

            if (v.getId() == R.id.shop_btn) {
                //商城按钮点击
                MobclickAgent.onEvent(itemView.getContext(), "home_0002");
                JDDUtils.jumpToH5Activity(itemView.getContext(), Constants.SHOP_URL, "商城", false);
            } else if (v.getId() == R.id.task_btn) {
                //任务按钮点击
                MobclickAgent.onEvent(itemView.getContext(), "home_0003");
                itemView.getContext().startActivity(new Intent(itemView.getContext(), TaskAct.class));
                //排行按钮点击
            } else if (v.getId() == R.id.rank_btn) {
                MobclickAgent.onEvent(itemView.getContext(), "home_0004");
                itemView.getContext().startActivity(new Intent(itemView.getContext(), SotingActivity.class));
            } else if (v.getId() == R.id.prize_btn) {
                //抽奖按钮点击
                MobclickAgent.onEvent(itemView.getContext(), "home_0006");
                JDDUtils.jumpToH5Activity(itemView.getContext(), Constants.PRIZE_URL, "", false);
            } else if (v.getId() == R.id.weal_btn) {
                //福利按钮点击
                JDDUtils.ifJumpToLoginAct(itemView.getContext(), new JDDUtils.JumpListener() {
                    @Override
                    public void jumpToTarget() {
                        MobclickAgent.onEvent(itemView.getContext(), "home_0007");
                        itemView.getContext().startActivity(new Intent(itemView.getContext(), WealAct.class));
                    }
                });
            } else if (v.getId() == R.id.add_icon) {
                JDDUtils.ifJumpToLoginAct(itemView.getContext(), new JDDUtils.JumpListener() {
                    @Override
                    public void jumpToTarget() {
                        JDDUtils.jumpToH5Activity(itemView.getContext(), Constants.SHOP_URL, "商城", false);
                    }
                });
            }


        }

        class BroadRunnable implements Runnable {

            int count = 0;
            private AutoTextView broad_text;
            private List<Notice> notices;

            public BroadRunnable(AutoTextView broad_text, List<Notice> notices) {
                this.broad_text = broad_text;
                this.notices = notices;
            }

            @Override
            public void run() {
                count++;
                broad_text.next();
                broad_text.setText(Html.fromHtml(notices.get(count % notices.size()).getRemark()));
                handler.postDelayed(this, 3000);
            }
        }

    }


    class GameViewHolder extends RecyclerView.ViewHolder {
        private TextView game_title;
        private TextView game_des;
        private RoundedImageView imageView;

        public GameViewHolder(View itemView) {
            super(itemView);
            initView();
        }

        private void initView() {
            game_title = (TextView) itemView.findViewById(R.id.game_title);
            game_des = (TextView) itemView.findViewById(R.id.game_des);
            imageView = (RoundedImageView) itemView.findViewById(R.id.imageView);

        }

        public void bindView(final int position) {
            final HomeItemData homeItem = homeList.get(position);

            final Game game = homeItem.getGame();
            if (game.getId() == -1) {
                game_title.setText("更多游戏");
                game_des.setText("敬请期待");
                imageView.setImageResource(R.mipmap.game_empty_icon);
            } else {
                game_title.setText(homeItem.getGame().getName());
                game_des.setText(homeItem.getGame().getRemark());
                BitmapTypeRequest builder = Glide.with(itemView.getContext()).load(homeItem.getGame().getIcon()).asBitmap();
                builder.diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.tacitly_approve).error(R.mipmap.tacitly_approve).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (game.getId() == -1) {
                        return;
                    }
                    //游戏入口点击
                    MobclickAgent.onEvent(itemView.getContext(), "home_0008");
                    JDDUtils.jumpToH5Activity(itemView.getContext(), homeItem.getGame().getUrl(), "", true);
                }
            });

        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }

    }


    /*
GridLayoutMangnger下header与footer处理
*/
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            //如果是header或footer，设置其充满整列
            ((GridLayoutManager) manager).setSpanSizeLookup(
                    new GridLayoutManager.SpanSizeLookup() {

                        @Override
                        public int getSpanSize(int position) {
                            int spanCount = ((GridLayoutManager) manager).getSpanCount();
                            if ((getItemViewType(position) == TYPE_FOOT || getItemViewType(position) == HomeItemData.TYPE_HEAD)) {
                                return spanCount;
                            } else {
                                return 1;
                            }
                        }
                    });
        }
    }


    /*
      StaggeredGridLayout下header与footer处理
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder, holder.getLayoutPosition());
        }
    }

    protected void handleLayoutIfStaggeredGridLayout(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOT || getItemViewType(position) == HomeItemData.TYPE_HEAD) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        }
    }


    private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            return true;
        }
        return false;
    }


}
