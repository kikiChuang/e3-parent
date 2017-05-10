package com.jddfun.game.Act;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.Act.fragment.PersonalCenterfFragment;
import com.jddfun.game.R;
import com.jddfun.game.View.MyTabView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 消息中心
 * Created by MACHINE on 2017/4/6.
 */

public class NoticeCentralityAct extends JDDBaseActivity implements View.OnClickListener {
    @BindView(R.id.act_notice_tb)
    MyTabView act_notice_tb;
    @BindView(R.id.frag_notice_vp)
    ViewPager frag_notice_vp;
    @BindView(R.id.tv_activity_title)
    TextView tv_activity_title;
    @BindView(R.id.iv_back_rl)
    RelativeLayout iv_back_rl;
    @BindView(R.id.act_head_head)
    LinearLayout act_head_head;
    @BindView(R.id.iv_iv_back)
    ImageView iv_iv_back;


    private String[] list_title = {"个人消息", "通知"};
    private ArrayList<Fragment> list_fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_centrality);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        act_head_head.setBackgroundResource(R.mipmap.head_bag);
        iv_iv_back.setImageResource(R.mipmap.left_back);
        tv_activity_title.setTextColor(Color.parseColor("#ffffff"));
        tv_activity_title.setText("消息中心");

        iv_back_rl.setOnClickListener(this);
        list_fragment = new ArrayList<>();
        Bundle bundle1 = new Bundle();

        PersonalCenterfFragment mPersonalCenterfFragment1 = new PersonalCenterfFragment();
        mPersonalCenterfFragment1.setMyTabView(act_notice_tb);
        //2 个人消息
        bundle1.putInt("msg_type", 2);
        mPersonalCenterfFragment1.setArguments(bundle1);
        list_fragment.add(mPersonalCenterfFragment1);

        Bundle bundle2 = new Bundle();
        PersonalCenterfFragment mPersonalCenterfFragment2 = new PersonalCenterfFragment();
        mPersonalCenterfFragment2.setMyTabView(act_notice_tb);
        //1 系统消息
        bundle2.putInt("msg_type", 1);
        mPersonalCenterfFragment2.setArguments(bundle2);
        list_fragment.add(mPersonalCenterfFragment2);

        act_notice_tb.setViewPager(frag_notice_vp, list_title, this, list_fragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_rl:
                finish();
                break;
        }
    }

}
