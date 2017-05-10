package com.jddfun.game.Act.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.jddfun.game.Adapter.MagnateFragmentAdatper;
import com.jddfun.game.R;
import com.jddfun.game.bean.SortingInfo;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;

/**
 * 富豪榜
 * Created by MACHINE on 2017/4/26.
 */

public class MagnateFragment extends BaseFragment{

    private RecyclerView mFrag_magnate_rl;
    private MagnateFragmentAdatper mMMagnateFragmentAdatper;
    private TextView mFrag_magnate_left;
    private TextView mFrag_magnate_ranking;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_magnate);
        mFrag_magnate_rl = getViewById(R.id.frag_magnate_rl);
        mFrag_magnate_left = getViewById(R.id.frag_magnate_left);
        mFrag_magnate_ranking = getViewById(R.id.frag_magnate_ranking);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMMagnateFragmentAdatper = new MagnateFragmentAdatper(getActivity());
        mFrag_magnate_rl.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mFrag_magnate_rl.setAdapter(mMMagnateFragmentAdatper);
        mFrag_magnate_rl.setItemAnimator(new DefaultItemAnimator());

        getWorkData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public void getWorkData() {
        ServiceFactory.getInstance().createService(JDDApiService.class).init()
                .compose(RxUtils.<HttpResult<SortingInfo>>defaultSchedulers())
                .compose(this.<HttpResult<SortingInfo>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<SortingInfo>() {
                    @Override
                    public void onSuccess(SortingInfo list) {
                        mMMagnateFragmentAdatper.setData(list.getRichList());
                        mFrag_magnate_left.setText(String.valueOf(list.getUseAmount()));
                        if(list.getRichMe() == 0){
                            mFrag_magnate_ranking.setText("继续努力");
                        }else{
                            mFrag_magnate_ranking.setText(String.valueOf(list.getRichMe()));
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }
}
