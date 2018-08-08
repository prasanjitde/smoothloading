package com.prasanjit.smoothloadinglib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SmoothLoadingView extends AppCompatTextView implements View.OnClickListener{

    String TAG = getClass().getName();
    private Paint backgroundPaint, paintText, linePaint;
    String labelPaint = "Some text";
    String labelPaintLoad = "Loading...";
    int cornerRadius;
    int canvasWidth = 96;
    int canvasHeight = 48;
    int actualWidth;
    int actualHeight;
    boolean isAnimated = false;
    int repeatCounter = 0;
    Context context;
    int backgroundColor, textColor, loadingCount;
    String textForButton, textForLoading, textForError;

    private OnSmoothLoadingClickEventListener loadingClickEventListener;

    public SmoothLoadingView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public SmoothLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public interface OnSmoothLoadingClickEventListener{
        void onSmoothButtonClicked(SmoothLoadingView view);
        void onSmoothAnimateCompleted(SmoothLoadingView view);
    }

    public void setOnSmoothLoadingClickEventListener(OnSmoothLoadingClickEventListener loadingClickEventListener){
        this.loadingClickEventListener = loadingClickEventListener;
    }

    private void init(AttributeSet attrs) {

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SmoothLoadingView);
        backgroundColor = ta.getColor(R.styleable.SmoothLoadingView_button_background, Color.GREEN);
        textColor = ta.getColor(R.styleable.SmoothLoadingView_button_text_color, Color.WHITE);
        textForButton = ta.getString(R.styleable.SmoothLoadingView_button_text);
        textForLoading = ta.getString(R.styleable.SmoothLoadingView_button_text_loading);
        textForError = ta.getString(R.styleable.SmoothLoadingView_button_text_error);
        loadingCount = ta.getInt(R.styleable.SmoothLoadingView_button_loading_count, 10);

        backgroundPaint.setColor(backgroundColor);
        paintText.setColor(textColor);
        ta.recycle();

        // text size of 16 sp
        paintText.setTextSize(Math.round(16f * getResources().getDisplayMetrics().scaledDensity));

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
        linePaint.setStrokeWidth(1f);

        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // paint.setStyle(Paint.Style.FILL);

        // float radius = size / 2f;
        // canvas.drawCircle(radius, radius, radius, paint);
        // canvas.drawColor(getResources().getColor(android.R.color.holo_green_light));

        int offset = 32;

        actualWidth = getResources().getDisplayMetrics().widthPixels;
        actualHeight = getResources().getDisplayMetrics().heightPixels;

        // horizontal center
        final float centerX = actualWidth * 0.5f;

        Log.i(TAG, "canvas width " + actualWidth + " * " + "canvas height " + actualHeight);

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        RectF rectF = new RectF(offset, offset, canvasWidth - offset, canvasHeight - offset);

        cornerRadius = Math.round(2f * getResources().getDisplayMetrics().density);

        Log.i(TAG, "corner radius " + cornerRadius);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, backgroundPaint);

        // Draw baseline.
        final float baselineY = Math.round(canvasHeight * 0.6f);
        // canvas.drawLine(0, baselineY, canvasWidth, baselineY, linePaint);

        // canvas.drawText(labelPaint, rectF.left + offset, rectF.top - offset, paintText);
        // canvas.drawRect(0 , 0, 480 , 196, paint);
        // drawLabel(canvas);
        if(isAnimated) {
            drawText(canvas, paintText, textForLoading);
            // drawTextAnother(canvas, paintText, labelPaintLoad, centerX, baselineY);
        }else {
            drawText(canvas, paintText, textForButton);
            // drawTextAnother(canvas, paintText, textForButton, centerX, baselineY);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int measureSpec) {
        // Log.i(TAG, "MeasureSpec height " + measureSpec);
        //determine height
        int size = getPaddingTop() + getPaddingBottom();
        size += paintText.getFontSpacing();
        Log.i(TAG, "Height " + size);
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec) {
        // Log.i(TAG, "MeasureSpec width " + measureSpec);
        //determine width
        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        paintText.getTextBounds(textForButton, 0, textForButton.length(), bounds);
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

    private void drawText(Canvas canvas, Paint paint, String text) {
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

    private void drawTextAnother(Canvas canvas, Paint paint, String text, float centerX, float baselineY){
        // Measure the width of text to display.
        final float textWidth = paint.measureText(text);
        // Figure out an x-coordinate that will center the text in the canvas.
        final float textX = Math.round(centerX - textWidth * 0.5f);

        // Draw.
        canvas.drawText(text, textX, baselineY, paint);
    }

    private void setAnimate(){
        repeatCounter++;
        ValueAnimator widthAnimator = ValueAnimator.ofInt(1, canvasWidth/8);
        widthAnimator.setDuration(500);
        widthAnimator.setInterpolator(new DecelerateInterpolator());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // canvasWidth = (int) animation.getAnimatedValue();
                // vasHeight = (int) animation.getAnimatedValue();
                SmoothLoadingView.this.setTranslationX((int) animation.getAnimatedValue());
                Log.i(TAG, "Value " + (int) animation.getAnimatedValue());
                if(canvasWidth/8 == (int) animation.getAnimatedValue()){
                    if(repeatCounter < loadingCount) {
                        setReverseAnimate();
                    }else {
                        setAnimateFast();
                    }
                }
                // SmoothLoadingView.this.setTranslationY((float) animation.getAnimatedValue());
                SmoothLoadingView.this.invalidate();
            }
        });
        // widthAnimator.setRepeatCount(5);
        widthAnimator.start();
        invalidate();
    }

    private void setReverseAnimate(){
        repeatCounter++;
        ValueAnimator widthAnimator = ValueAnimator.ofInt(canvasWidth/8, 1);
        widthAnimator.setDuration(500);
        widthAnimator.setInterpolator(new DecelerateInterpolator());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // canvasWidth = (int) animation.getAnimatedValue();
                // vasHeight = (int) animation.getAnimatedValue();
                SmoothLoadingView.this.setTranslationX((int) animation.getAnimatedValue());
                Log.i(TAG, "Value " + (int) animation.getAnimatedValue());
                if(1 == (int) animation.getAnimatedValue()){
                    if(repeatCounter < loadingCount) {
                        setAnimate();
                    }else {
                        setAnimateFast();
                    }
                }
                // SmoothLoadingView.this.setTranslationY((float) animation.getAnimatedValue());
                SmoothLoadingView.this.invalidate();
            }
        });
        // widthAnimator.setRepeatCount(5);
        widthAnimator.start();
        invalidate();
    }

    private void setAnimateFast(){
        repeatCounter++;
        ValueAnimator widthAnimator = ValueAnimator.ofInt(1, actualWidth);
        widthAnimator.setDuration(200);
        widthAnimator.setInterpolator(new DecelerateInterpolator());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                SmoothLoadingView.this.setTranslationX((int) animation.getAnimatedValue());
                if(actualWidth == (int) animation.getAnimatedValue()){
                    fadeOutFast();
                }
                SmoothLoadingView.this.invalidate();
            }
        });
        // widthAnimator.setRepeatCount(5);
        widthAnimator.start();
        invalidate();
    }

    private void fadeOutFast(){
        ValueAnimator widthAnimator = ValueAnimator.ofFloat(0f, 1f);
        widthAnimator.setDuration(100);
        widthAnimator.setInterpolator(new DecelerateInterpolator());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fadeValue = (float) animation.getAnimatedValue();
                SmoothLoadingView.this.setAlpha(fadeValue);
                SmoothLoadingView.this.invalidate();
                if((float) animation.getAnimatedValue() == 1f){
                    if(loadingClickEventListener != null){
                        loadingClickEventListener.onSmoothAnimateCompleted(SmoothLoadingView.this);
                    }
                }
            }
        });
        // widthAnimator.setRepeatCount(5);
        widthAnimator.start();
        invalidate();
    }

    private void fadeIn(){
        ValueAnimator widthAnimator = ValueAnimator.ofFloat(0f, 1f);
        widthAnimator.setDuration(10000);
        widthAnimator.setInterpolator(new DecelerateInterpolator());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fadeValue = (float) animation.getAnimatedValue();
                SmoothLoadingView.this.setAlpha(fadeValue);
                if(fadeValue == 1f){
                    fadeOut();
                }
                SmoothLoadingView.this.invalidate();
            }
        });
        // widthAnimator.setRepeatCount(5);
        widthAnimator.start();
        invalidate();
    }

    private void fadeOut(){
        ValueAnimator widthAnimator = ValueAnimator.ofFloat(0f, 1f);
        widthAnimator.setDuration(5000);
        widthAnimator.setInterpolator(new DecelerateInterpolator());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fadeValue = (float) animation.getAnimatedValue();
                SmoothLoadingView.this.setAlpha(fadeValue);
                if(fadeValue == 0.25f){
                    fadeIn();
                }
                SmoothLoadingView.this.invalidate();
            }
        });
        // widthAnimator.setRepeatCount(5);
        widthAnimator.start();
        invalidate();
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "On click");

        if(loadingClickEventListener != null){
            loadingClickEventListener.onSmoothButtonClicked(SmoothLoadingView.this);
        }

        if(isAnimated == false) {
            isAnimated = true;
            setAnimate();
            fadeIn();
        }else {
            // do nothing
        }
    }

    public void setComplete(){

    }
}
