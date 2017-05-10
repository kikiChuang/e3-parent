package com.jddfun.game.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.jddfun.game.R;
import com.jddfun.game.Utils.GlideImgManager;
import com.jddfun.game.View.anim.MagicFlyLinearLayout;

/**
 * Created by xuhongliang on 2017/4/28.
 */

public class CombineSuccessDialog extends CommonDialog implements DialogInterface.OnDismissListener, Runnable {

    private ImageView aureole_image;
    private ImageView imgae_icon;
    private TextView des_01;
    private TextView des_02;
    private Handler handler = new Handler();

    private Animation roateAnim;
    private ScaleAnimation scaleAnim;
    private ScaleAnimation scaleAnim01;
    private ScaleAnimation scaleAnim02;
    private String imageUrl;
    private String decrisName;
    private MagicFlyLinearLayout leaf_layout;
    private int[] drawableRes = null;


    public CombineSuccessDialog(Context context, String imageUrl, String decrisName) {
        super(context);
        this.imageUrl = imageUrl;
        this.decrisName = decrisName;
        init();
    }

    private void init() {
        drawableRes = new int[]
                {R.mipmap.petal_01, R.mipmap.petal_02, R.mipmap.petal_03,
                        R.mipmap.petal_04, R.mipmap.petal_05, R.mipmap.petal_06,
                        R.mipmap.petal_07, R.mipmap.petal_08, R.mipmap.petal_09,
                        R.mipmap.petal_10, R.mipmap.petal_11, R.mipmap.petal_12,
                        R.mipmap.petal_13, R.mipmap.petal_14, R.mipmap.petal_15,

                };
        roateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.aureole_roate);
        scaleAnim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim01 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim02 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(500);//设置动画持续时间
        scaleAnim01.setDuration(500);
        scaleAnim02.setDuration(500);
        //匀速
        LinearInterpolator lin = new LinearInterpolator();
        roateAnim.setInterpolator(lin);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.compound_success_layout, null);
        initView(view);
        setContentView(view);
        setOnDismissListener(this);
    }


    private void initView(View root) {
        aureole_image = (ImageView) root.findViewById(R.id.aureole_image);
        des_02 = (TextView) root.findViewById(R.id.des_02);
        leaf_layout = (MagicFlyLinearLayout) root.findViewById(R.id.leaf_layout);
        imgae_icon = (ImageView) root.findViewById(R.id.imgae_icon);
        des_01 = (TextView) root.findViewById(R.id.des_01);
        final ViewGroup.LayoutParams relative = imgae_icon.getLayoutParams();
        Glide.with(getContext())
                .load(imageUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        relative.height = bitmap.getHeight();
                        relative.width = bitmap.getWidth();
                        imgae_icon.setLayoutParams(relative);
                        imgae_icon.setImageBitmap(bitmap);
                    }

                });
        des_01.setText(decrisName + "已合成");
        for (int i = 0; i < drawableRes.length; i++) {
            leaf_layout.addDrawable(drawableRes[i]);
        }

    }

    public void show() {
        super.show();
        //开始动画
        startAnim();
    }

    private void startAnim() {
        aureole_image.startAnimation(roateAnim);
        des_02.startAnimation(scaleAnim01);
        imgae_icon.startAnimation(scaleAnim);
        des_01.startAnimation(scaleAnim02);
        for (int i = 0; i < 20; i++) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    leaf_layout.flying();
                }
            }, i * 100);
        }
        //3秒后消失
        handler.postDelayed(this, 3000);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (aureole_image != null) {
            aureole_image.clearAnimation();
        }
    }

    @Override
    public void run() {
        dismiss();
    }
}
