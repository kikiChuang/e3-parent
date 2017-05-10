package com.jddfun.game.Act.base;


import android.app.ProgressDialog;

import com.trello.rxlifecycle.components.support.RxFragment;

import rx.Subscription;

/**
 * Created by xuhongliang on 2017/4/12.
 */

public class JDDBaseFragment extends RxFragment {


    protected ProgressDialog loadingDialog = null;

    public void showLoading(boolean cancelble, String str) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(getContext());
            loadingDialog.setCancelable(cancelble);

        }
        loadingDialog.show();
        loadingDialog.setMessage(str);
    }

    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }
}
