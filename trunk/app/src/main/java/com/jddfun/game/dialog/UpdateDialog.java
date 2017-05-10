package com.jddfun.game.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.Utils.AndroidUtil;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.UpdateInfo;

public class UpdateDialog {

    public Dialog dialog;
    private UpdateInfo updateInfo;
    private Context context;
    private Button btn_download;
    private TextView tv_version;
    private TextView tv_content;
    private TextView tv_progress;
    private ProgressBar progressbar;


    public UpdateDialog(Context context, UpdateInfo updateInfo) {
        this.context = context;
        dialog = new Dialog(context);
        this.updateInfo = updateInfo;
        initDialog();
    }

    private void initDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = AndroidUtil.getDeviceWidth((Activity) context) - 100;
        dialogWindow.setAttributes(params);

        progressbar = (ProgressBar) dialog.findViewById(R.id.progressbar);
        tv_version = (TextView) dialog.findViewById(R.id.tv_version);
        tv_progress = (TextView) dialog.findViewById(R.id.tv_progress);
        tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        btn_download = (Button) dialog.findViewById(R.id.btn_download);
        tv_version.setText("v " + updateInfo.getVersion());
        String temp = updateInfo.getDescription().replace("\\n", "\n");
        updateInfo.setDescription(temp);
        tv_content.setText(updateInfo.getDescription());

        if (updateInfo.getForce() == 1) {
            //强制更新
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
        }


        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = updateInfo.getUrl();
                if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
                    UtilsTools.getInstance().show("下载地址不正确");
                    return;
                }

                UtilsTools.getInstance().show("开始下载");
                Intent intent = new Intent("android.intent.action.VIEW");
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });

    }

    public void showDialog() {
        dialog.show();
    }

    public void cancelDialog() {
        dialog.cancel();
    }

    public boolean dialogIsShow() {
        return dialog.isShowing();
    }


}
