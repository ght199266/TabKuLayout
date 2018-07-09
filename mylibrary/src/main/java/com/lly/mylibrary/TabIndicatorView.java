package com.lly.mylibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TabIndicatorView extends View {

    private Paint mIndicatorPaint;
    private int itemWidth;

    private int startX;
    private int endY;


    private int mOffsetValue;

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
        Log.v("test", "itemWidth:=" + itemWidth);
    }

    public TabIndicatorView(Context context) {
        super(context);
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(Color.RED);
        mIndicatorPaint.setStrokeWidth(15);
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
        canvas.drawLine(startX, 0, endY, 0, mIndicatorPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, height);
    }

    public void updateIndicator(int position, float positionOffset, int positionOffsetPixels, int scrollOrientation) {
        final int roundedPosition = Math.round(position + positionOffset);
        if (roundedPosition != -1) {
            if (roundedPosition == 0) {
                Log.v("test", "右滑动");
            } else {
                Log.v("test", "zuo滑动");
            }
        }
//        if (positionOffsetPixels < mOffsetValue) {
//        } else if (positionOffsetPixels > mOffsetValue) {
//            startX = position * itemWidth;
//            Log.v("test", "eny:=" + endY);
//            Log.v("test", "2222:=" + (roundedPosition * itemWidth) + itemWidth / 2);
//            if (endY < (roundedPosition * itemWidth) + itemWidth / 2) {
//                Log.v("test", "右边先动");
//                endY = (int) (startX + itemWidth + (itemWidth * positionOffset));
//            } else {
//                Log.v("test", "左边先动");
//                startX = (int) ((position * itemWidth) * positionOffset);
//            }
//        } else {
//            startX = position * itemWidth;
//            endY = (int) (startX + itemWidth + (itemWidth * positionOffset));
//        }
//        mOffsetValue = positionOffsetPixels;
//        invalidate();
    }
}