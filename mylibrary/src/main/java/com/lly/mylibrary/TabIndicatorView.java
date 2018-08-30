package com.lly.mylibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.view.View;

import java.util.List;

public class TabIndicatorView extends View {

    /**
     * 画笔绘制
     */
    private Paint mIndicatorPaint;

    /**
     * 是否需要渐变
     */
    private boolean isGradient = true;


    private int[] colors;

    /**
     * 圆角半径
     */
    private int mRoundRadius = 5;

    /**
     * 绘制起点
     */
    private int left;

    /**
     * 绘制终点
     */
    private int endY;

    private List<Integer> mItemWidth;
    private LinearGradient mLinearGradient;

    private float mPositionOffset;

    private float mOffset;

    public TabIndicatorView(Context context) {
        super(context);
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        colors = new int[2];
    }

    public void setGradient(boolean gradient) {
        isGradient = gradient;
    }

    public void setItemWidth(List<Integer> mItemWidth) {
        this.mItemWidth = mItemWidth;
    }


    public void setColors(int startColor, int endColor) {
        this.colors[0] = startColor;
        this.colors[1] = endColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawRoundRect(canvas);

    }

    /**
     * 绘制支持圆角矩形
     */
    private void onDrawRoundRect(Canvas canvas) {
        canvas.drawRoundRect(left, 0, endY, getHeight(), mRoundRadius, mRoundRadius, mIndicatorPaint);
    }


    /**
     * 更新指示器位置
     *
     * @param position       位置
     * @param positionOffset 当前偏移量
     */
    public void updateNewIndicator(int position, float positionOffset) {
        if (mItemWidth == null) {
            return;
        }
//        if (positionOffset == 0) {
//            return;
//        }
        final int roundedPosition = Math.round(position + positionOffset);

        boolean isNext = positionOffset > mPositionOffset;
        mPositionOffset = positionOffset;


        int curWidth = mItemWidth.get(position);
        int nextWidth = 0;
        if (position + 1 < mItemWidth.size()) {
            nextWidth = mItemWidth.get(position + 1);
        } else {
            nextWidth = mItemWidth.get(mItemWidth.size() - 1);
        }

        final int doubleItemWidth = curWidth + nextWidth;

        left = getPositionLeft(position);
        endY = (int) (left + curWidth + ((doubleItemWidth * 2) * positionOffset));

        if (endY - left >= doubleItemWidth) {
            left = (int) (left + (curWidth * ((1 / (1 - mOffset)) * (positionOffset - mOffset))));
            endY = getPositionLeft(position + 2);
        } else {
            mOffset = positionOffset;
        }

        if (isGradient) {
            updateGradientValue();
        } else {
            mIndicatorPaint.setColor(colors[0]);
        }
        invalidate();
    }


    private int getPositionLeft(int position) {
        int width = 0;
        if (position <= mItemWidth.size()) {
            for (int i = 0; i < position; i++) {
                width += mItemWidth.get(i);
            }
        }
        return width;
    }

    /**
     * 更新渐变的Value
     */
    private void updateGradientValue() {
        mLinearGradient = new LinearGradient(left, 0,
                endY, 0, colors, null, LinearGradient.TileMode.CLAMP);
        mIndicatorPaint.setShader(mLinearGradient);
    }
}