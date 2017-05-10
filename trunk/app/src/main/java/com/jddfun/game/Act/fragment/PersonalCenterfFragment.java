package com.jddfun.game.Act.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jddfun.game.Act.NoticeCentralityAct;
import com.jddfun.game.Adapter.PersonalCenterfAdapter;
import com.jddfun.game.R;
import com.jddfun.game.View.MyTabView;
import com.jddfun.game.bean.PushedMessagesBean;
import com.jddfun.game.bean.PushedMessagesInfo;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;

import static com.jddfun.game.R.id.empty_des1;
import static com.jddfun.game.R.id.empty_des2;

/**
 * 个人消息
 * Created by MACHINE on 2017/4/6.
 */

public class PersonalCenterfFragment extends BaseFragment {

    private RecyclerView mFrag_centerf_rl;

    private PersonalCenterfAdapter adapter;
    private int type;

    private TwinklingRefreshLayout refresh_layout;

    private LinearLayout mEmpty_view;
    private TextView mEmpty_des1;
    private TextView mEmpty_des2;

    public MyTabView myTabView;

    public void setMyTabView(MyTabView myTabView) {
        this.myTabView = myTabView;
    }


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getArguments().getInt("msg_type");
        setContentView(R.layout.personal_centerf_fragment);
        refresh_layout = getViewById(R.id.refresh_layout);
        mFrag_centerf_rl = getViewById(R.id.frag_centerf_rl);
        mEmpty_view = getViewById(R.id.empty_view);
        mEmpty_des1 = getViewById(empty_des1);
        mEmpty_des1.setText("居然没有消息");
        mEmpty_des2 = getViewById(empty_des2);
        mEmpty_des2.setText("你一定不积极哦");
        initAdapter();
        // getWorkData();
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new PersonalCenterfAdapter(getActivity(), new ArrayList<PushedMessagesBean.MessagesBean>(), "PersonalCenterfFragment", 0, myTabView, type + "");
        mFrag_centerf_rl.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mFrag_centerf_rl.setAdapter(adapter);
        mFrag_centerf_rl.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void setListener() {
        refresh_layout.startRefresh();
        refresh_layout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                getWorkData(true);

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWorkData(false);
                    }
                }, 2000);
            }
        });

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    int page = 1;
    int pageSize = 10;

    public void getWorkData(final boolean isRefresh) {

        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }


        PushedMessagesInfo mPushedMessagesInfo = new PushedMessagesInfo();
        mPushedMessagesInfo.setPage(page);
        mPushedMessagesInfo.setPageSize(pageSize);
        mPushedMessagesInfo.setValue(type);
        ServiceFactory.getInstance().createService(JDDApiService.class).getPushedMessages(mPushedMessagesInfo)
                .compose(RxUtils.<HttpResult<PushedMessagesBean>>defaultSchedulers())
                .compose(this.<HttpResult<PushedMessagesBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<PushedMessagesBean>() {

                    @Override
                    public void onSuccess(PushedMessagesBean list) {

                        if (list != null) {
                            if (isRefresh) {
                                adapter.setData(list.getMessages(), list.getUnReadCount());
                                refresh_layout.setEnableLoadmore(true);
                                refresh_layout.finishRefreshing();
                                if (list.getMessages().size() == 0) {
                                    refresh_layout.setVisibility(View.GONE);
                                    mEmpty_view.setVisibility(View.VISIBLE);
                                } else {
                                    refresh_layout.setVisibility(View.VISIBLE);
                                    mEmpty_view.setVisibility(View.GONE);
                                }
                            } else {
                                adapter.appendData(list.getMessages(), list.getUnReadCount());
                                refresh_layout.finishLoadmore();
                            }
                        } else {
                            refresh_layout.setVisibility(View.GONE);
                            mEmpty_view.setVisibility(View.VISIBLE);
                            onError(null, -1);
                        }

                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }
}
