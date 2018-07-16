package com.lly.mylibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
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
    LinearGradient linearGradient;

    private int mOffsetValue;

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
        Log.v("test", "itemWidth:=" + itemWidth);
    }

    public TabIndicatorView(Context context) {
        super(context);
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mIndicatorPaint.setColor(Color.RED);
        mIndicatorPaint.setStrokeCap(Paint.Cap.SQUARE);
//        mIndicatorPaint.setStrokeWidth(20);

        linearGradient = new LinearGradient(startX, 0,
                endY, 0, new int[]{Color.YELLOW, Color.BLUE, Color.YELLOW}, null, LinearGradient.TileMode.CLAMP);
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
        linearGradient = new LinearGradient(startX, 0,
                endY, 0, new int[]{Color.YELLOW, Color.RED}, null, LinearGradient.TileMode.CLAMP);
        mIndicatorPaint.setShader(linearGradient);
        canvas.drawRoundRect(startX, 0, endY, getHeight(), 10, 10, mIndicatorPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, height);
    }

    public void updateIndicator(int position, float positionOffset) {
        final int roundedPosition = Math.round(position + positionOffset);
        startX = (position * itemWidth);
        endY = (int) (startX + itemWidth + (itemWidth * 2 * positionOffset));
        if (endY - startX >= (itemWidth * 2)) {
            startX += (itemWidth * 2) * (positionOffset - 0.5f);
            endY = (roundedPosition + 1) * itemWidth;
        }
        invalidate();
    }
}