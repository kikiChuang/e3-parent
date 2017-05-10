package com.jddfun.game.Act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.Utils.PrefManager;
import com.jddfun.game.R;
import com.jddfun.game.CustomView.ImageCycleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeAct extends JDDBaseActivity {


    @BindView(R.id.btn_skip)
    Button mBtnSkip;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.layoutDots)
    LinearLayout mLayoutDots;
    private PrefManager prefManager;
    private TextView[] dots;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在setContentView()前检查是否第一次运行
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }


        //让状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //添加欢迎页面
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
        };
        //添加点
        addBottomDots(0);

        //让状态栏透明
        changeStatusBarColor();
        myViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(myViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);
//        initWelcomePage();

        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    mViewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            //改变下一步按钮text  “NEXT”或“GOT IT”
            if (position == layouts.length - 1) {
                mBtnNext.setText(getString(R.string.start));
                mBtnSkip.setVisibility(View.GONE);
            } else {
                mBtnNext.setText(getString(R.string.next));
                mBtnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, HomeAct.class));
        finish();
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        mLayoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));//圆点
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            mLayoutDots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    /**
     * 让状态栏变透明
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initWelcomePage() {
        ArrayList<String> strings = new ArrayList<>();
        ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

            @Override
            public void displayImage(String imageURL, ImageView imageView) {

            }

            @Override
            public void onImageClick(int position, View imageView) {

            }
        };

//        ImageData imageData = new ImageData();
//        imageData.initCarsuelView(imageData.getDesc(strings), imageData.getUrl(strings), mWelcomePage, mAdCycleViewListener);
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
