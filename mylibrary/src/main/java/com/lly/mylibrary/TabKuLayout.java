package com.lly.mylibrary;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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

    private Context mContext;


    private int mIndicatorHeight = 20;

    private ViewPager mViewpager;
    private LinearLayout mTabContainer;
    private TabIndicatorView mIndicatorView;

    //Tab数量
    private int mTabChildCount;
    private PagerAdapter mPagerAdapter;

    private int lastValue;
    private int scrollOrientation = -1;

    //指示器距离底部距离
//    private int mIndicatorBottom = 10;

    public TabKuLayout(Context context) {
        this(context, null);
    }

    public TabKuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public void setViewpager(ViewPager mViewpager) {
        this.mViewpager = mViewpager;
        addOnPageChangeListener();
        getPagerAdapter();
    }

    /**
     * 监听viewpager滑动
     */
    private void addOnPageChangeListener() {
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset != 0) {
                    if (lastValue >= positionOffsetPixels) {
                        //右滑
                        scrollOrientation = 0;
                    } else if (lastValue < positionOffsetPixels) {
                        //左滑
                        scrollOrientation = 1;
                    }
                }
                lastValue = positionOffsetPixels;
                final int roundedPosition = Math.round(position + positionOffset);
                mIndicatorView.updateIndicator(position, positionOffset, positionOffsetPixels, scrollOrientation);
            }

            @Override
            public void onPageSelected(int position) {
//                Log.v("test", "onPageSelected:=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.v("test", "onPageScrollStateChanged:=" + mViewpager.getCurrentItem());
            }
        });
    }


    private void getPagerAdapter() {
        mPagerAdapter = mViewpager.getAdapter();
        if (mPagerAdapter != null) {
            mTabChildCount = mPagerAdapter.getCount();
            addTabTextView();
            addIndicatorView();
        }
    }

    private void addTabTextView() {
        for (int i = 0; i < mTabChildCount; i++) {
            TabKuView tabKuView = new TabKuView(mContext);
            tabKuView.setText(mPagerAdapter.getPageTitle(i));
            LinearLayout.LayoutParams tabKuViewParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
            mTabContainer.addView(tabKuView, tabKuViewParams);
        }
        super.addView(mTabContainer, 0, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     *
     */
    private void addIndicatorView() {
        mIndicatorView = new TabIndicatorView(mContext);
        super.addView(mIndicatorView, 1, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mIndicatorHeight));
    }

    public TabKuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mTabContainer = new LinearLayout(context);
        mTabContainer.setGravity(Gravity.CENTER_VERTICAL);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LinearLayout.LayoutParams params = (LayoutParams) mTabContainer.getLayoutParams();
        params.height = h - mIndicatorHeight;
        params.width = LayoutParams.WRAP_CONTENT;
        mTabContainer.setLayoutParams(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.v("test", "onWindowFocusChanged");
        if (hasWindowFocus) {
            LinearLayout.LayoutParams params = (LayoutParams) mTabContainer.getLayoutParams();
            params.height = getHeight() - mIndicatorHeight;
            params.width = LayoutParams.MATCH_PARENT;
            mTabContainer.setLayoutParams(params);
            mIndicatorView.setItemWidth(getWidth() / mTabContainer.getChildCount());
            mIndicatorView.updateIndicator(0, 0, 0, -1);
        }
    }


    private class TabKuView extends AppCompatTextView {
        public TabKuView(Context context) {
            super(context);
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
