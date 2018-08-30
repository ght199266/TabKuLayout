package com.lly.mylibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
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
    private int mMode;

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

    private float mIndicatorHeight = 10;

    /**
     * 文字大小
     */
    private float tab_font_size = 15;

    /**
     * 文字颜色
     */
    private ColorStateList mColorStateList;


    /**
     * 指示器是否渐变
     */
    private boolean isGradient;

    /**
     * 指示器起始颜色
     */
    private int mTabIndicator_start_color;

    /**
     * 指示器结束颜色
     */
    private int mTabIndicator_end_color;


    private ViewPager mViewpager;
    private LinearLayout mTabContainer;
    private TabIndicatorView mIndicatorView;

    private LinearLayout mViewGroup;

    //Tab数量
    private int mTabChildCount;
    private PagerAdapter mPagerAdapter;


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

    public void setupWithViewPager(ViewPager mViewpager) {
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
            tabKuView.setMaxLines(1);
            tabKuView.setTextSize(tab_font_size);
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
            mTabContainer.addView(tabKuView, tabKuViewParams);
        }
    }

    private void updateTabContainerLayoutParams() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTabContainer.getLayoutParams();
        params.height = (int) (getHeight() - mIndicatorHeight);
        params.width = LayoutParams.MATCH_PARENT;
        mTabContainer.setLayoutParams(params);
    }

    public TabKuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabKuLayout_styleable);

        tab_font_size = px2sp(context, typedArray.getDimension(R.styleable.TabKuLayout_styleable_tab_font_size, 12));

        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.TabKuLayout_styleable_tabIndicatorHeight, 2);

        int tabSelectedTextColor = typedArray.getColor(R.styleable.TabKuLayout_styleable_tabSelectedTextColor, 0);
        mColorStateList = createColorStateList(Color.GRAY, tabSelectedTextColor);


        mTabIndicator_start_color = typedArray.getColor(R.styleable.TabKuLayout_styleable_tabIndicator_start_color, Color.RED);

        if (typedArray.hasValue(R.styleable.TabKuLayout_styleable_tabIndicator_end_color)) {
            isGradient = true;
            mTabIndicator_end_color = typedArray.getColor(R.styleable.TabKuLayout_styleable_tabIndicator_end_color, 0);
        }

        mMode = typedArray.getInt(R.styleable.TabKuLayout_styleable_tab_Mode, MODE_FIXED);

        Log.v("test", "mMode:=" + mMode);

        typedArray.recycle();

        mViewGroup = new LinearLayout(context);
        mViewGroup.setOrientation(LinearLayout.VERTICAL);

        mTabContainer = new LinearLayout(context);
        mIndicatorView = new TabIndicatorView(context);
        mIndicatorView.setGradient(isGradient);
        mIndicatorView.setColors(mTabIndicator_start_color, mTabIndicator_end_color);


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mMode == MODE_SCROLLABLE ? LayoutParams.WRAP_CONTENT : getWidth();
                mViewGroup.addView(mTabContainer, 0, new LinearLayout.LayoutParams(width, (int) (getHeight() - mIndicatorHeight)));
                mViewGroup.addView(mIndicatorView, 1, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) mIndicatorHeight));
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                addView(mViewGroup, new HorizontalScrollView.LayoutParams(1080, LayoutParams.MATCH_PARENT));
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

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */

    public int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
