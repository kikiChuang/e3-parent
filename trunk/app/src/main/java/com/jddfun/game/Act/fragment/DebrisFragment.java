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
import com.jddfun.game.bean.WealDebris;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhongliang on 2017/4/7.
 */

public class DebrisFragment extends BaseFragment {


    private RecyclerView recyclerView;
    private DebrisAdapter wealAdapter;
    private View emptyView;
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
        wealAdapter = new DebrisAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(wealAdapter);
        initRefresh();
    }

    @Override
    protected void setListener() {
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

    @Override
    protected void processLogic(Bundle savedInstanceState) {

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
        ServiceFactory.getInstance().createService(JDDApiService.class).getWealDebris(param)
                .compose(RxUtils.<HttpResult<List<WealDebris>>>defaultSchedulers())
                .compose(this.<HttpResult<List<WealDebris>>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<List<WealDebris>>() {
                    @Override
                    public void onSuccess(List<WealDebris> list) {
                        if (list != null && list.size() > 0) {
                            if (isRefresh) {
                                wealAdapter.setData(list);
                                refresh_layout.finishRefreshing();
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
                        refresh_layout.finishRefreshing();
                        wealAdapter.setData(new ArrayList<WealDebris>());
                        emptyView.setVisibility(View.VISIBLE);
                    }

                });


    }


    class DebrisAdapter extends RecyclerView.Adapter {

        private ArrayList<WealDebris> list = new ArrayList();


        private void setData(List<WealDebris> weals) {
            list.clear();
            list.addAll(weals);
            notifyDataSetChanged();
        }

        public void appendDatas(List<WealDebris> data) {
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
                final WealDebris weal = list.get(position);
                time.setText(weal.getCreateTime());
                mtype.setText(weal.getSource());
                prize_obj.setText(weal.getName() + "*" + weal.getNum());
                status.setBackgroundResource(R.mipmap.weal_button_bg3);
                status.setText("已发放");
                status.setTextColor(Color.parseColor("#808080"));
            }
        }
    }
}
