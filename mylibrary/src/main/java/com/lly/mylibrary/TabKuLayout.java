package com.lly.mylibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * TabKuLayout[v 1.0.0]
 * classes:com.lly.tabkulayout.TabKuLayout
 *
 * @author lileiyi
 * @date 2018/6/14
 * @time 15:08
 * @description
 */

public class TabKuLayout extends HorizontalScrollView {

    /**
     * 默认选中选中position
     */
    private static final int DEFAULT_SELECT_POSTION = 0;

    private static final int MODE_SCROLLABLE = 0;

    private static final int MODE_FIXED = 1;

    /**
     * 排序方式
     */
    private int mMode = MODE_SCROLLABLE;

    /**
     * tabView居左间距
     */
    private int mTabLeftPadding = 30;

    /**
     * tabView居右间距
     */
    private int mTabRightPadding = 30;

    /**
     * 指示器高度
     */

    private int mIndicatorHeight = 10;

    private ViewPager mViewpager;
    private LinearLayout mTabContainer;
    private TabIndicatorView mIndicatorView;


    private LinearLayout mViewGroup;

    //Tab数量
    private int mTabChildCount;
    private PagerAdapter mPagerAdapter;
    private ColorStateList mColorStateList;

    private List<Integer> itemsWidth = new ArrayList<>();

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
                updateIndicatorPosition(position, positionOffset);
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


    /**
     * 更新指示器位置信息
     */
    private void updateIndicatorPosition(int position, float positionOffset) {
        mIndicatorView.updateNewIndicator(position, positionOffset);
        scrollTo(calculateScrollXForTab(position, positionOffset), 0);
    }

    private int calculateScrollXForTab(int position, float positionOffset) {
        if (mMode == MODE_SCROLLABLE) {
            final View selectedChild = mTabContainer.getChildAt(position);
            final View nextChild = position + 1 < mTabContainer.getChildCount()
                    ? mTabContainer.getChildAt(position + 1)
                    : null;
            final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
            final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;

            // base scroll amount: places center of tab in center of parent
            int scrollBase = selectedChild.getLeft() + (selectedWidth / 2) - (getWidth() / 2);
            // offset amount: fraction of the distance between centers of tabs
            int scrollOffset = (int) ((selectedWidth + nextWidth) * 0.5f * positionOffset);

            return (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR)
                    ? scrollBase + scrollOffset
                    : scrollBase - scrollOffset;
        }
        return 0;
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
//            LinearLayout.LayoutParams tabKuViewParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
            LinearLayout.LayoutParams tabKuViewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            final int finalI = i;
            tabKuView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewpager.setCurrentItem(finalI);
                    tabKuView.setSelected(true);
                }
            });
            tabKuView.setTextColor(mColorStateList);
            tabKuView.setPadding(mTabLeftPadding, 0, mTabRightPadding, 0);
//            mTabContainer.setBackgroundColor(Color.RED);
            mTabContainer.addView(tabKuView, tabKuViewParams);
        }
    }

    private void updateTabContainerLayoutParams() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTabContainer.getLayoutParams();
        params.height = getHeight() - mIndicatorHeight;
        params.width = LayoutParams.MATCH_PARENT;
        mTabContainer.setLayoutParams(params);
    }

    public TabKuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setOrientation(VERTICAL);
        mViewGroup = new LinearLayout(context);
        mViewGroup.setOrientation(LinearLayout.VERTICAL);

        mTabContainer = new LinearLayout(context);
        mIndicatorView = new TabIndicatorView(context);

        mColorStateList = createColorStateList(Color.BLACK, Color.RED);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewGroup.addView(mTabContainer, 0, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, getHeight() - mIndicatorHeight));
                mViewGroup.addView(mIndicatorView, 1, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mIndicatorHeight));
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                addView(mViewGroup, new HorizontalScrollView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        });

        mTabContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mIndicatorView.setItemWidth(getChildItemWidth());
                setDefaultPosition(mViewpager.getCurrentItem());
            }
        });
    }

    /**
     * 默认选择的position
     *
     * @param position
     */
    private void setDefaultPosition(int position) {
        mViewpager.setCurrentItem(position);
        setSelectedTabView(position);
        updateIndicatorPosition(position, 0);
    }

    /**
     * 遍历获取每个item的宽度
     */
    private List<Integer> getChildItemWidth() {
        if (mTabContainer != null && mTabChildCount > 0) {
            itemsWidth.clear();
            for (int i = 0; i < mTabChildCount; i++) {
                itemsWidth.add(mTabContainer.getChildAt(i).getWidth());
            }
        }
        return itemsWidth;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

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
