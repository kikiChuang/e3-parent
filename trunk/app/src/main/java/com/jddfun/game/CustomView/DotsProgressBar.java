package com.jddfun.game.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.jddfun.game.R;

/**
 * 用于分段显示进度的进度条
 */
public class DotsProgressBar extends View {

    /**
     * 第一个圆点位置
     */
    private int mDotsOnePosition;
    /**
     * 第二个圆点位置
     */
    private int mDotsTwoPosition;
    /**
     * 第三个个圆点位置
     */
    private int mDotsTreePosition;

    /**
     * 进度条的点数
     */
    private int mDotsCount;

    /**
     * 进度条圆点的半径
     */
    private int mDotsRadius;

    /**
     * 进度条的宽度
     */
    private int mDotsProgressWidth;

    /**
     * 进度条宽度的一半
     */
    private int mDotsProgressWidthHalf;

    /**
     * 进度条的背景色
     */
    private int mDotsBackColor;

    /**
     * 进度条的前景色
     */
    private int mDotsFrontColor;

    /**
     * 进度条前进的速度
     */
    private int mSpeed;

    /**
     * 目前已经进行的时间
     */
    private int mPartTime;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 原先的进度在某个点
     */
    private int mOldPosition = 0;


    /**
     * 每段矩形的长度
     */
    private int mPartWidth = 0;

    /**
     * 使用插值器来获得进度值，这样不仅简化了获取进度值的过程，还可以向外提供可定制性
     */
    private Interpolator mInterpolator;


    /**
     * 标记进度条是否在动画中
     */
    private boolean mIsRunning = false;
    private DotsTurn mDotsTurn;

    public DotsProgressBar(Context context) {
        this(context, null);
    }

    public DotsProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotsProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotsProgressBar);
        mDotsCount = typedArray.getInt(R.styleable.DotsProgressBar_barDotsCount, 2);
        mDotsRadius = typedArray.getDimensionPixelSize(R.styleable.DotsProgressBar_barDotsRadius, dp2px(8));
        mDotsProgressWidth = typedArray.getDimensionPixelSize(R.styleable.DotsProgressBar_barProgressWidth, dp2px(4));
        if ((2 * mDotsRadius) < mDotsProgressWidth)
            mDotsProgressWidth = mDotsRadius * 2;
        mDotsProgressWidthHalf = mDotsProgressWidth / 2;
        mSpeed = typedArray.getInt(R.styleable.DotsProgressBar_barSpeed,60);
        mDotsBackColor = typedArray.getColor(R.styleable.DotsProgressBar_barBackColor, ContextCompat.getColor(context, R.color.bar_bg));
        mDotsFrontColor = typedArray.getColor(R.styleable.DotsProgressBar_barFrontColor, ContextCompat.getColor(context, R.color.bar_front));
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mInterpolator = new LinearInterpolator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, mDotsRadius * 2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mPaint.setColor(mDotsBackColor);
        canvas.drawRect(0, height / 2 - mDotsProgressWidthHalf, width, height / 2 + mDotsProgressWidthHalf, mPaint);
        mPaint.setColor(mDotsFrontColor);
        float percent = mInterpolator.getInterpolation(((float) mPartTime) / mSpeed);
        int[] params = getParams(percent);
        int start = mOldPosition * mPartWidth;
        canvas.drawRect(0, mDotsRadius - mDotsProgressWidthHalf, start, mDotsRadius + mDotsProgressWidthHalf, mPaint);
        canvas.drawCircle(start + params[0], mDotsRadius, 6, mPaint);
        if (mDotsTurn != null) {
            if (start + params[0] >= mDotsOnePosition) {
                mDotsTurn.mDotsTurn(3);
            }
            if (start + params[0] >= mDotsTwoPosition) {
                mDotsTurn.mDotsTurn(2);
            }
            if (start + params[0] >= mDotsTreePosition) {
                mDotsTurn.mDotsTurn(1);
            }
        }
        canvas.drawRect(start, mDotsRadius - mDotsProgressWidthHalf, start + params[0], mDotsRadius + mDotsProgressWidthHalf, mPaint);
        if (mPartTime < mSpeed) {
            mPartTime++;
        } else {
            mIsRunning = false;
        }
        postInvalidate();

    }

    public void startForward(int mPartWidth, DotsTurn mDotsTurns) {
        int width = getMeasuredWidth();
        mDotsOnePosition = width / 3 / 2;
        mDotsTwoPosition = width / 3 + width / 3 / 2;
        mDotsTreePosition = width / 3 * 2 + width / 3 / 2;

        if (mPartWidth == 1) {
            this.mPartWidth = mDotsTreePosition + mDotsOnePosition / 2;
        } else if (mPartWidth == 2) {
            this.mPartWidth = mDotsTwoPosition + mDotsOnePosition / 2;
        } else if (mPartWidth == 3) {
            this.mPartWidth = mDotsOnePosition + mDotsOnePosition / 2;
        } else if (mPartWidth == 4) {
            this.mPartWidth = mDotsOnePosition + mDotsOnePosition / 2;
        } else if (mPartWidth == 5) {
            this.mPartWidth = mDotsOnePosition / 2;
        }

        this.mDotsTurn = mDotsTurns;
        mPartTime = 0;
        mIsRunning = true;
        if (this.mDotsTurn != null) {
            postInvalidate();
        }
    }


    public interface DotsTurn {
        void mDotsTurn(int i);
    }


    private int[] getParams(float percent) {
        int[] params = new int[2];
        if (mOldPosition >= 0) {
            if (percent > 0.9) {
                params[0] = mPartWidth;
                params[1] = (int) (((percent - 0.9) / 0.1) * (mDotsRadius - mDotsProgressWidthHalf) + mDotsProgressWidthHalf);
            } else {
                params[0] = (int) ((percent / 0.9) * mPartWidth);
                params[1] = mDotsProgressWidthHalf;
            }
        } else {
            params[0] = 0;
            params[1] = (int) (percent * mDotsRadius);
        }
        return params;
    }

    /**
     * dp 转 px
     *
     * @param dpValue dp 值
     * @return 返回 px 值
     */
    private int dp2px(int dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
