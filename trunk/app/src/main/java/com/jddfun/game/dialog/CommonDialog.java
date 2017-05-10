package com.jddfun.game.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.jddfun.game.Act.HomeAct;
import com.jddfun.game.R;

/**
 * Created by xuhongliang on 2017/4/27.
 */

public class CommonDialog extends Dialog {

    public CommonDialog(Context context) {
        super(context);
        initDialog();
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog();
    }

    protected void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = HomeAct.device_with;
        window.setAttributes(params);
    }
}
