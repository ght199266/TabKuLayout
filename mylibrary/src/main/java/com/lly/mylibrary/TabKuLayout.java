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


    private int mIndicatorHeight = 10;

    private ViewPager mViewpager;
    private LinearLayout mTabContainer;
    private TabIndicatorView mIndicatorView;

    //Tab数量
    private int mTabChildCount;
    private PagerAdapter mPagerAdapter;


    private ColorStateList mColorStateList;
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
                mIndicatorView.updateIndicator(position, positionOffset);
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
            addIndicatorView();
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
            final TabKuView tabKuView = new TabKuView(mContext);
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
        mColorStateList = createColorStateList(Color.BLACK, Color.RED);
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
        if (hasWindowFocus) {
            LinearLayout.LayoutParams params = (LayoutParams) mTabContainer.getLayoutParams();
            params.height = getHeight() - mIndicatorHeight;
            params.width = LayoutParams.MATCH_PARENT;
            mTabContainer.setLayoutParams(params);
            mIndicatorView.setItemWidth(getWidth() / mTabContainer.getChildCount());
            mIndicatorView.updateIndicator(0, 0);
            setSelectedTabView(0);
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
