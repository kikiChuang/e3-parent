package com.jddfun.game.Act.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.bean.PageReqParams;
import com.jddfun.game.bean.Weal;
import com.jddfun.game.dialog.CommonDialog;
import com.jddfun.game.dialog.DialogUtils;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xuhongliang on 2017/4/7.
 */

public class WealFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private WealAdapter wealAdapter;
    private View emptyView;
    private CommonDialog alertDialog;
    private Weal weal;
    private TwinklingRefreshLayout refresh_layout;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.weal_layout);
        refresh_layout = getViewById(R.id.refresh_layout);
        recyclerView = getViewById(R.id.recycle_view);
        emptyView = getViewById(R.id.empty_view);
        wealAdapter = new WealAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(wealAdapter);
        initRefresh();
    }

    private void initRefresh() {
        refresh_layout.startRefresh();
        refresh_layout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromNet(true);
                        refresh_layout.finishRefreshing();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromNet(false);
                        refreshLayout.finishLoadmore();
                    }
                }, 500);
            }
        });

    }

    private int page = 1;
    private int pageSize = 10;

    private void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }

        PageReqParams param = new PageReqParams();
        param.setPage(page);
        param.setPageSize(pageSize);
        ServiceFactory.getInstance().createService(JDDApiService.class).getWeals(param)
                .compose(RxUtils.<HttpResult<List<Weal>>>defaultSchedulers())
                .compose(this.<HttpResult<List<Weal>>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<List<Weal>>() {
                    @Override
                    public void onSuccess(List<Weal> list) {
                        if (list != null && list.size() > 0) {


                            if (isRefresh) {
                                wealAdapter.setData(list);
                                refresh_layout.setEnableLoadmore(true);
                                if (list.size() == 0) {
                                    refresh_layout.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                } else {
                                    refresh_layout.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                }
                            } else {
                                wealAdapter.appendDatas(list);
                                refresh_layout.finishLoadmore();
                            }

                        } else {
                            onError(null, -1);
                        }

                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        wealAdapter.setData(new ArrayList<Weal>());
                        emptyView.setVisibility(View.VISIBLE);
                    }

                });


    }


    @Override
    protected void setListener() {
        initEvent();
    }

    private void initEvent() {
        RxBus.getInstance()
                .toObservable(JDDEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<JDDEvent>bindToLifecycle())
                .subscribe(new Action1<JDDEvent>() {
                    @Override
                    public void call(JDDEvent Jddevent) {
                        //修改地址成功
                        if (Jddevent.getType() == JDDEvent.TYPE_MODIFY_ADDRESS_SUCCESS) {
                            if (alertDialog != null && weal != null) {
                                alertDialog.dismiss();
                                alertDialog = new CommonDialog(getActivity());
                                DialogUtils.wealSuccess(alertDialog, WealFragment.this, weal);
                            }
                        }
                    }
                });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    public void refreshData() {
        if (refresh_layout != null) {
            refresh_layout.startRefresh();
        }
    }

    class WealAdapter extends RecyclerView.Adapter {

        private ArrayList<Weal> list = new ArrayList();


        private void setData(List<Weal> weals) {
            list.clear();
            list.addAll(weals);
            notifyDataSetChanged();
        }

        public void appendDatas(List<Weal> data) {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weal_item_layout, parent, false);
            return new WealHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((WealHolder) holder).bindView(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class WealHolder extends RecyclerView.ViewHolder {

            private TextView time;
            private TextView mtype;
            private TextView prize_obj;
            private TextView status;

            public WealHolder(View itemView) {
                super(itemView);
                initView();
            }

            private void initView() {
                time = (TextView) itemView.findViewById(R.id.time);
                mtype = (TextView) itemView.findViewById(R.id.type);
                prize_obj = (TextView) itemView.findViewById(R.id.prize_obj);
                status = (TextView) itemView.findViewById(R.id.status);
            }

            public void bindView(int position) {
                final Weal weal = list.get(position);
                time.setText(weal.getCreateTime());
                mtype.setText(weal.getActivityName());
                prize_obj.setText(weal.getAwardsName());
                switch (weal.getReceiveStatus()) {
                    case 1:
                        status.setBackgroundResource(R.mipmap.weal_button_bg1);
                        status.setText("领取");
                        status.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case 2:
                        status.setBackgroundResource(R.mipmap.weal_button_bg2);
                        status.setText("审核中");
                        status.setTextColor(Color.parseColor("#ffffff"));
                        break;
                    case 3:
                        status.setBackgroundResource(R.mipmap.weal_button_bg3);
                        status.setText("查询");
                        status.setTextColor(Color.parseColor("#808080"));
                        break;
                }
                status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (weal.getReceiveStatus()) {
                            case 1:
                                status.setText("领取");
                                alertDialog = new CommonDialog(getActivity());
                                WealFragment.this.weal = weal;
                                DialogUtils.wealSuccess(alertDialog, WealFragment.this, weal);
                                break;
                            case 3:
                                DialogUtils.getPrizeInfo(new CommonDialog(getActivity()), WealFragment.this, weal);
                                break;
                        }
                    }
                });
            }
        }
    }
}
