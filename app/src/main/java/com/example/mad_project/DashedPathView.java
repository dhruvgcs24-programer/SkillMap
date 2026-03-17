package com.example.mad_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class DashedPathView extends View {
    private Paint paint;
    private Path path;

    public DashedPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFE0D7FF); // Light purple/lavender
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12f);
        paint.setAntiAlias(true);
        // Creates the dashed line effect
        paint.setPathEffect(new DashPathEffect(new float[]{30, 20}, 0));
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();

        path.reset();
        // Starts at top center
        path.moveTo(w / 2f, 0);
        // Creates the S-curve using Cubic Bezier points
        path.cubicTo(w * 1.3f, h * 0.25f, -w * 0.3f, h * 0.75f, w / 2f, h);

        canvas.drawPath(path, paint);
    }
}