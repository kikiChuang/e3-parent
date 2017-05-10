package com.jddfun.game.Act.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.View.MyTabView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import static com.jddfun.game.R.id.frag_share_vp;

/**
 * 分享圈
 * Created by MACHINE on 2017/3/22.
 */

public class ShareFragment extends BaseFragment implements View.OnClickListener {
    private String[] list_title = {"全部", "我"};
    private ArrayList<Fragment> list_fragment;
    private ViewPager mFrag_share_vp;

    @Override
    protected void onFirstUserVisible() {

    }


    public void setViewPagePa() {
        if (mFrag_share_vp != null) {
            mFrag_share_vp.setCurrentItem(0);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_share);

        MyTabView frag_share_myTb = getViewById(R.id.frag_share_myTb);
        mFrag_share_vp = getViewById(frag_share_vp);
        ImageView iv_iv_back = getViewById(R.id.iv_iv_back);
        ImageView ivHeadRight = getViewById(R.id.iv_head_right);
        RelativeLayout fl_right = getViewById(R.id.fl_right);
        TextView tv_activity_title = getViewById(R.id.tv_activity_title);

        iv_iv_back.setVisibility(View.GONE);
        ivHeadRight.setImageResource(R.mipmap.question_icon);
        ivHeadRight.setVisibility(View.VISIBLE);
        tv_activity_title.setText("分享圈");


        AllMesFragment mAllMesFragment = new AllMesFragment();
        MyMesFragment mMyMesFragment = new MyMesFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(mAllMesFragment);
        list_fragment.add(mMyMesFragment);

        fl_right.setOnClickListener(this);

        mFrag_share_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    JDDUtils.ifJumpToLoginAct(getActivity(), new JDDUtils.JumpListener() {
                        @Override
                        public void jumpToTarget() {
                            ((MyMesFragment)list_fragment.get(1)).startRefresh();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        frag_share_myTb.setViewPager(mFrag_share_vp, list_title, getActivity(), list_fragment);

        frag_share_myTb.setOncli(new MyTabView.isOnclick() {
            @Override
            public void leftOnclick() {
            }

            @Override
            public void rightOnclick() {
                MobclickAgent.onEvent(getActivity(), "share_0002");
            }
        });


    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_right:
                JDDUtils.jumpToH5Activity(getActivity(), Constants.HELP_CENTER + "7", "帮助中心", false);
                break;
        }
    }
}
