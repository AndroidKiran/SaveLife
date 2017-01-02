package com.donate.savelife.apputils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * Created by ravi on 31/12/16.
 */

public class RoundedBackgroundSpan extends ReplacementSpan {

    private static final int CORNER_RADIUS = 8;
    private static final int PADDING_X = 12;
    private final int mBgColor;
    private int   mTextColor;

    public RoundedBackgroundSpan(int bgColor, int textColor) {
        mBgColor = bgColor;
        mTextColor = textColor;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (PADDING_X + paint.measureText(text.subSequence(start, end).toString()) + PADDING_X);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        float width = paint.measureText(text.subSequence(start, end).toString());
        RectF rect = new RectF(x, top, x + width + 2 * PADDING_X, bottom);
        paint.setColor(mBgColor);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(mTextColor);
        canvas.drawText(text, start, end, x + PADDING_X, y, paint);
    }
}