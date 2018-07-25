package com.prasanjit.smoothloadinglib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class SmoothLoadingView extends AppCompatTextView {

    private Paint paint;
    private int size = 320;

    public SmoothLoadingView(Context context) {
        super(context);
        init(null);
    }

    public SmoothLoadingView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(android.R.color.holo_green_light));
        paint.setStyle(Paint.Style.FILL);

        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);
    }
}
