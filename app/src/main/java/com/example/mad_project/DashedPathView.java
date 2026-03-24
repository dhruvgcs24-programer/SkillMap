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
    private Path path = new Path();
    private float startX, startY, endX, endY;

    public DashedPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFB39DDB); // Soft Purple
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{25, 25}, 0));
        paint.setAntiAlias(true);
    }

    // This method is called by the Activity once coordinates are calculated
    public void setPathPoints(float x1, float y1, float x2, float y2) {
        this.startX = x1;
        this.startY = y1;
        this.endX = x2;
        this.endY = y2;
        invalidate(); // Trigger a redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Only draw if the start and end points have been set
        if (startY != 0 || endY != 0) {
            path.reset();
            path.moveTo(startX, startY);

            // Creates a smooth S-curve between the two level buttons
            float midY = (startY + endY) / 2;
            path.cubicTo(
                    startX + 200, midY - 100, // Handle 1
                    endX - 200, midY + 100,   // Handle 2
                    endX, endY               // Destination
            );

            canvas.drawPath(path, paint);
        }
    }
}