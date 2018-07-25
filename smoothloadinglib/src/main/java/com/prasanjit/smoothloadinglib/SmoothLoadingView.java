package com.prasanjit.smoothloadinglib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class SmoothLoadingView extends AppCompatTextView {

    String TAG = getClass().getName();
    private Paint paint;
    // private int size = 640;
    String labelPaint = "Some text";
    Paint paintText;

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
        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(72);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(android.R.color.holo_green_light));
        // paint.setStyle(Paint.Style.FILL);

        // float radius = size / 2f;
        // canvas.drawCircle(radius, radius, radius, paint);
        // canvas.drawColor(getResources().getColor(android.R.color.holo_green_light));

        int offset = 32;
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        Log.i(TAG, canvasWidth + " * " + canvasHeight);
        RectF rectF = new RectF(offset, offset, canvasWidth - offset, canvasHeight - offset);
        int cornerRadius = 25;
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        // canvas.drawText(labelPaint, rectF.left + offset, rectF.top - offset, paintText);
        // canvas.drawRect(0 , 0, 480 , 196, paint);
        // drawLabel(canvas);
        drawText(canvas, paintText, labelPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        Log.i(TAG, "MeasureSpec height " + measureSpec);
        //determine height
        int size = getPaddingTop() + getPaddingBottom();
        size += paintText.getFontSpacing();
        Log.i(TAG, "Height " + size);
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec) {
        Log.i(TAG, "MeasureSpec width " + measureSpec);
        //determine width
        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        paintText.getTextBounds(labelPaint, 0, labelPaint.length(), bounds);
        size += bounds.width();
        Log.i(TAG, "Width " + size);
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private void drawLabel(Canvas canvas) {

        int offset = 32;
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight() / 4;

        float x = getPaddingLeft();
        //the y coordinate marks the bottom of the text, so we need to factor in the height
        Rect bounds = new Rect(0, 0, 240, 60);
        paintText.getTextBounds(labelPaint, 0, labelPaint.length(), bounds);
        float y = getPaddingTop() + bounds.height();
        Log.i(TAG, "XY " + x + " * " + y);

        canvas.drawText(labelPaint, x, y, paintText);
    }

    private void drawText(Canvas canvas, Paint paint, String text){

        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

}
