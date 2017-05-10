package com.jddfun.game.Act;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.R;
import com.jddfun.game.Utils.GlideImgManager;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 查看大图
 * Created by MACHINE on 2017/4/25.
 */

public class BigPictureAct extends JDDBaseActivity {
    @BindView(R.id.act_big_Img)
    ImageView act_big_Img;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mUrl = getIntent().getStringExtra("url");
        GlideImgManager.glideLoader(BigPictureAct.this,mUrl,act_big_Img, 1);
        act_big_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
