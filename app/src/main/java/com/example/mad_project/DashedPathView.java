package com.example.mad_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DashedPathView extends View {

    private Paint paint;
    private Path path = new Path();
    private List<Point> points = new ArrayList<>();

    private static class Point {
        float x, y;
        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public DashedPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFB39DDB); // Soft Purple
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{25, 25}, 0));
        paint.setAntiAlias(true);
    }

    public void clearPoints() {
        points.clear();
        invalidate();
    }

    public void addPathPoint(float x, float y) {
        points.add(new Point(x, y));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (points.size() < 2) return;

        path.reset();
        Point first = points.get(0);
        path.moveTo(first.x, first.y);

        for (int i = 0; i < points.size() - 1; i++) {
            Point start = points.get(i);
            Point end = points.get(i + 1);

            float midY = (start.y + end.y) / 2;
            // Alternate curve direction for a nice snake effect
            float offset = (i % 2 == 0) ? 200 : -200;
            
            path.cubicTo(
                    start.x + offset, midY - 100,
                    end.x - offset, midY + 100,
                    end.x, end.y
            );
        }

        canvas.drawPath(path, paint);
    }
}