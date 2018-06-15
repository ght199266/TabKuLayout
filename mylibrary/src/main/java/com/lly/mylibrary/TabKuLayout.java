package com.lly.mylibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * TabKuLayout[v 1.0.0]
 * classes:com.lly.tabkulayout.TabKuLayout
 *
 * @author lileiyi
 * @date 2018/6/14
 * @time 15:08
 * @description
 */

public class TabKuLayout extends LinearLayout {


    private Paint mIndicatorPaint;

    private int mIndicatorHeight;

    private ViewPager mViewpager;

    private LinearLayout mTabContainer;

    private TabIndicatorView mIndicatorView;

    public TabKuLayout(Context context) {
        this(context, null);
    }

    public TabKuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setViewpager(ViewPager mViewpager) {
        this.mViewpager = mViewpager;

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v("test", "positionOffset:=" + positionOffset);
                Log.v("test", "positionOffsetPixels:=" + positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                if (mIndicatorView != null) {
                    mIndicatorView.setTranslationX(position * 112 + 20);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public TabKuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mTabContainer = new LinearLayout(context);
        for (int i = 0; i < 5; i++) {
            mTabContainer.addView(new TabKuView(context));
        }
        mTabContainer.setGravity(Gravity.CENTER_VERTICAL);
        mIndicatorView = new TabIndicatorView(context);
        super.addView(mTabContainer, 0, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 110));
        super.addView(mIndicatorView, 1, new LinearLayout.LayoutParams(60, 10));

        mIndicatorView.setBackgroundColor(Color.RED);
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
        mIndicatorPaint.setColor(Color.RED);
        mIndicatorPaint.setStrokeWidth(10);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabContainer.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mTabContainer.getChildCount(); i++) {
                    View v = mTabContainer.getChildAt(i);
                    Log.v("test", "getPaddingLeft:=" + v.getPaddingLeft());
                    Log.v("test", "left:=" + v.getLeft());
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawLine(0, getHeight() - 50, 60, getHeight() - 50, mIndicatorPaint);
    }

    private class TabIndicatorView extends View {

        public TabIndicatorView(Context context) {
            super(context);
        }

        public TabIndicatorView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TabIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = getDefaultSize(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec);
            Log.v("test", "height:=" + height);
            setMeasuredDimension(widthMeasureSpec, height);
        }
    }

    private class TabKuView extends AppCompatTextView {


        public TabKuView(Context context) {
            super(context);
            setText("item1");
            setPadding(20, 0, 20, 0);
            setGravity(Gravity.CENTER);
        }

        public TabKuView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public TabKuView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    }
}
