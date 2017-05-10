package com.jddfun.game.Act;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.R;
import com.jddfun.game.Utils.UtilsTools;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 关于我们
 * Created by MACHINE on 2017/4/6.
 */

public class AboutUsAct extends JDDBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_iv_back)
    ImageView iv_iv_back;
    @BindView(R.id.tv_activity_title)
    TextView tv_activity_title;
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        iv_iv_back.setImageResource(R.mipmap.left_back);
        tv_activity_title.setText("关于我们");
        iv_iv_back.setOnClickListener(this);

        tv_version.setText("V " + UtilsTools.getInstance().getVersionName(JDDApplication.mApp));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_iv_back:
                finish();
                break;
        }
    }
}
