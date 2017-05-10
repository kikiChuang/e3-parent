package com.jddfun.game.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jddfun.game.R;
import com.jddfun.game.Utils.BaseUtil;
import com.jddfun.game.View.RoundedImageView;
import com.jddfun.game.View.ScaleInTransformer;

import java.util.ArrayList;

/**
 *
 *
 */
public class ImageCycleView extends LinearLayout {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图片轮播视图
     */
    private ViewPager mAdvPager = null;
    /**
     * 滚动图片视图适配
     */
    private ImageCycleAdapter mAdvAdapter;
    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mGroup;

    /**
     * 图片轮播指示个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示视图列表
     */
    private ImageView[] mImageViews = null;

    /**
     * 图片滚动当前图片下标
     */
    private int mImageIndex = 0;
    /**
     * 手机密度
     */
    private float mScale;
    private boolean isStop;
    private TextView imageName;
    private ArrayList<String> mImageDescList;
    private boolean pointInPager = false;

    /**
     * @param context
     */
    public ImageCycleView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.ImageCycleView,
                    0, 0);
            try {
                pointInPager = a.getBoolean(R.styleable.ImageCycleView_pointInPager, false);
            } finally {
                a.recycle();
            }
        }
        mScale = context.getResources().getDisplayMetrics().density;
        if (pointInPager) {
            LayoutInflater.from(context).inflate(R.layout.ad_cycle_view_in_mode, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
        }
        mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
        mAdvPager.setPageMargin(20);
        mAdvPager.setOffscreenPageLimit(3);
        mAdvPager.setPageTransformer(true, new ScaleInTransformer());
        mAdvPager.addOnPageChangeListener(new GuidePageChangeListener());
        mAdvPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        startImageTimerTask();
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
        // 滚动图片右下指示器视
        mGroup = (ViewGroup) findViewById(R.id.circles);
        imageName = (TextView) findViewById(R.id.viewGroup2);
    }


    /**
     * 装填图片数据
     *
     * @param :
     * @param imageCycleViewListener
     */
    public void setImageResources(ArrayList<String> imageDesList, ArrayList<String> imageUrlList, ImageCycleViewListener imageCycleViewListener) {
        mImageDescList = imageDesList;
        if (imageUrlList != null && imageUrlList.size() > 0) {
            this.setVisibility(View.VISIBLE);
        } else {
            this.setVisibility(View.GONE);
            return;
        }

        // 清除
        mGroup.removeAllViews();
        // 图片广告数量
        final int imageCount = imageUrlList.size();
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(mContext);
            //int imageParams = (int) (mScale * 10 + 0.5f);// XP与DP转换，适应应不同分辨率
            int imagePadding = (int) (mScale * 5 + 0.5f);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
           // mImageView.setScaleType(ScaleType.CENTER_CROP);
            mImageView.setLayoutParams(params);
            //mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);

            mImageViews[i] = mImageView;
            if (i == 0) {
                mImageViews[i].setBackgroundResource(R.drawable.banner_selected);
            } else {
                mImageViews[i].setBackgroundResource(R.drawable.banner_unselected);
            }
            mGroup.addView(mImageViews[i]);
        }

        imageName.setText(imageDesList.get(0));
        imageName.setTextColor(getResources().getColor(R.color.blue));
        mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageDesList, imageCycleViewListener);
        mAdvPager.setAdapter(mAdvAdapter);
        startImageTimerTask();
    }

    /**
     * 图片轮播(手动控制自动轮播与否，便于资源控件）
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播—用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片滚动
        mHandler.postDelayed(mImageTimerTask, 3000);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        isStop = true;
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (mImageViews != null) {
                mAdvPager.setCurrentItem(mAdvPager.getCurrentItem() + 1);
                if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
                    mHandler.postDelayed(mImageTimerTask, 3000);
                }

            }
        }
    };

    /**
     * 轮播图片监听
     *
     * @author minking
     */
    private final class GuidePageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                startImageTimerTask();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            index = index % mImageViews.length;
            // 设置当前显示的图片
            mImageIndex = index;
            // 设置图片滚动指示器背
            mImageViews[index].setBackgroundResource(R.drawable.banner_selected);
            imageName.setText(mImageDescList.get(index));
            for (int i = 0; i < mImageViews.length; i++) {
                if (index != i) {
                    mImageViews[i].setBackgroundResource(R.drawable.banner_unselected);
                }
            }
        }
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<RoundedImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private ArrayList<String> mAdList = new ArrayList<String>();
        private ArrayList<String> nameList = new ArrayList<String>();

        /**
         * 广告图片点击监听
         */
        private ImageCycleViewListener mImageCycleViewListener;

        private Context mContext;

        public ImageCycleAdapter(Context context, ArrayList<String> adList, ArrayList<String> nameList, ImageCycleViewListener imageCycleViewListener) {
            this.mContext = context;
            this.mAdList = adList;
            this.nameList = nameList;
            mImageCycleViewListener = imageCycleViewListener;
            mImageViewCacheList = new ArrayList<RoundedImageView>();
        }

        @Override
        public int getCount() {
//			return mAdList.size();
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imageUrl = mAdList.get(position % mAdList.size());


            final RoundedImageView imageView;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new RoundedImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                imageView.setCornerRadius((float)BaseUtil.dip2Px(container.getContext(),5));
                imageView.setScaleType(ScaleType.CENTER_CROP);
                imageView.setPadding(0,0,0,BaseUtil.dip2Px(mContext,0.5f));
                imageView.setBackgroundResource(R.drawable.show_view);

            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageCycleViewListener.onImageClick(position % mAdList.size(), v);
                }
            });
           // imageView.setTag(imageUrl);
            container.addView(imageView);
            mImageCycleViewListener.displayImage(imageUrl, imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            RoundedImageView view = (RoundedImageView) object;
            mAdvPager.removeView(view);
            mImageViewCacheList.add(view);

        }

    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageCycleViewListener {
        /**
         * 加载图片资源
         *
         * @param imageURL
         * @param imageView
         */
        void displayImage(String imageURL, ImageView imageView);

        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        void onImageClick(int position, View imageView);
    }


}
