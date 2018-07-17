package com.lly.mylibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
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

    /**
     * 默认选中选中position
     */
    private static final int DEFAULT_SELECT_POSTION = 0;

    /**
     * 指示器高度
     */
    private int mIndicatorHeight = 10;

    private ViewPager mViewpager;
    private LinearLayout mTabContainer;
    private TabIndicatorView mIndicatorView;

    //Tab数量
    private int mTabChildCount;
    private PagerAdapter mPagerAdapter;
    private ColorStateList mColorStateList;

    private int mItemWidth;


    public TabKuLayout(Context context) {
        this(context, null);
    }

    /**
     * 设置指示器的高度
     *
     * @param indicatorHeight
     */
    public void setIndicatorHeight(int indicatorHeight) {
        this.mIndicatorHeight = indicatorHeight;
//        updateTabContainerLayoutParams();
    }

    public TabKuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
                mIndicatorView.updateIndicator(position, positionOffset, mItemWidth);
            }

            @Override
            public void onPageSelected(int position) {
                setSelectedTabView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void getPagerAdapter() {
        mPagerAdapter = mViewpager.getAdapter();
        if (mPagerAdapter != null) {
            mTabChildCount = mPagerAdapter.getCount();
            addTabTextView();
        }
    }

    private void setSelectedTabView(int position) {
        final int tabCount = mTabContainer.getChildCount();
        if (position < tabCount) {
            for (int i = 0; i < tabCount; i++) {
                final View child = mTabContainer.getChildAt(i);
                child.setSelected(i == position);
            }
        }
    }

    /**
     * 添加文字
     */
    private void addTabTextView() {
        for (int i = 0; i < mTabChildCount; i++) {
            final TabKuView tabKuView = new TabKuView(getContext());
            tabKuView.setText(mPagerAdapter.getPageTitle(i));
            LinearLayout.LayoutParams tabKuViewParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
            final int finalI = i;
            tabKuView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewpager.setCurrentItem(finalI);
                    tabKuView.setSelected(true);
                }
            });
            tabKuView.setTextColor(mColorStateList);
            mTabContainer.addView(tabKuView, tabKuViewParams);
        }
    }

    public TabKuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mTabContainer = new LinearLayout(context);
        mIndicatorView = new TabIndicatorView(context);
        mColorStateList = createColorStateList(Color.BLACK, Color.RED);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addView(mTabContainer, 0, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, getHeight() - mIndicatorHeight));
                addView(mIndicatorView, 1, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mIndicatorHeight));
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mIndicatorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mViewpager.setCurrentItem(mViewpager.getCurrentItem());
                setSelectedTabView(mViewpager.getCurrentItem());
            }
        });
    }


    /**
     * 默认选中item
     *
     * @param position
     */
    private void setDefaultSelect(int position) {
        if (position < mTabContainer.getChildCount()) {
            TabKuView tabKuView = (TabKuView) mTabContainer.getChildAt(position);
            tabKuView.setSelected(true);
        }
        mIndicatorView.updateIndicator(position, 0, mItemWidth);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if (mTabContainer.getChildCount() > 0) {
            mItemWidth = width / mTabContainer.getChildCount();
        }
    }

    private void updateTabContainerLayoutParams() {
        LinearLayout.LayoutParams params = (LayoutParams) mTabContainer.getLayoutParams();
        params.height = getHeight() - mIndicatorHeight;
        params.width = LayoutParams.MATCH_PARENT;
        mTabContainer.setLayoutParams(params);
    }

    private class TabKuView extends AppCompatTextView {
        public TabKuView(Context context) {
            super(context);
            setGravity(Gravity.CENTER);
        }
    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;

        return new ColorStateList(states, colors);
    }
}
