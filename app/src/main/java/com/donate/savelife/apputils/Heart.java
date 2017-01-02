package com.donate.savelife.apputils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by ravi on 31/12/16.
 */

public class Heart extends View {

    private final float size;
    private Path path;
    private Paint paint;

    public Heart(Context context, float size) {
        super(context);
        this.size = size;
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fill the canvas with background color
        canvas.drawColor(Color.WHITE);
        paint.setShader(null);

        float width = size;
        float height = size;

        // Starting point
        path.moveTo(width / 2, height / 5);

        // Upper left path
        path.cubicTo(5 * width / 14, 0,
                0, height / 15,
                width / 28, 2 * height / 5);

        // Lower left path
        path.cubicTo(width / 14, 2 * height / 3,
                3 * width / 7, 5 * height / 6,
                width / 2, height);

        // Lower right path
        path.cubicTo(4 * width / 7, 5 * height / 6,
                13 * width / 14, 2 * height / 3,
                27 * width / 28, 2 * height / 5);

        // Upper right path
        path.cubicTo(width, height / 15,
                9 * width / 14, 0,
                width / 2, height / 5);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);

    }
}
