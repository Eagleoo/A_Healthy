package com.example.administrator.steps_count.step;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


public class ProgressView extends View {
    private int mTrackColor;
    private int mTrackWidth = 20;
    private int mProgressColor ;
    private int mProgressTextColor;
    private int mProgressTextSize = 60;

    private Paint mTrackPaint, mProgressPaint, mTextPaint;

    private float mCurrentProgress, mMaxProgress;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();

    }

    public void setColor1(){
        mTrackColor = Color.parseColor("#FFED97");
        mProgressColor = Color.parseColor("#00A600");
        mProgressTextColor=Color.BLACK;
        initPaint();
    }

    public void setColor2(){
        mTrackColor = Color.parseColor("#FFED97");
        mProgressColor = Color.parseColor("#FF0000");
        mProgressTextColor= Color.parseColor("#FF0000");
        initPaint();
    }

    public void setColor3(){
        mTrackColor = Color.parseColor("#97CBFF");
        mProgressColor = Color.parseColor("#0080FF");
        mProgressTextColor=Color.parseColor("#000079");
        initPaint();
    }
    public void setColor4(){
        mTrackColor = Color.parseColor("#8E8E8E");
        mProgressColor = Color.parseColor("#5B4B00");
        mProgressTextColor=Color.parseColor("#844200");
        initPaint();
    }

    private void initPaint() {

        //轨道画笔
        mTrackPaint = new Paint();
        mTrackPaint.setColor(mTrackColor);
        mTrackPaint.setAntiAlias(true);
        mTrackPaint.setStrokeWidth(mTrackWidth);
        mTrackPaint.setStyle(Paint.Style.STROKE);
        mTrackPaint.setStrokeCap(Paint.Cap.ROUND);
        //进度条画笔
        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mTrackWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mProgressTextSize);
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            width = Utils.dp2px(getContext(), 160);
            height = Utils.dp2px(getContext(), 140);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //控件中心位置
        int center = getWidth() / 2;
        //轨道圆弧半径
        int radius = getWidth() / 2 - mTrackWidth / 2;

        //画轨道
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(rectF, 135, 270, false, mTrackPaint);

        //画进度条
        float sweepAngle = (mCurrentProgress / mMaxProgress) * 270;
        canvas.drawArc(rectF, 135, sweepAngle, false, mProgressPaint);

        //画文字
        String text = (int)mCurrentProgress + "";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);

        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        float x = center - rect.width() / 2;
//        float dy = fontMetrics.ascent / 2;
        float dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline = getHeight() / 2 + dy; //确定文字绘制的基线高度
        canvas.drawText(text, 0, text.length(), x, baseline, mTextPaint);
    }

    /**
     * 设置当前进度
     * @param progress
     */
    public void setCurrentProgress(float progress) {
        if(progress<mMaxProgress){
            //属性动画更新进度，刷新界面
            ValueAnimator animator = ValueAnimator.ofFloat(0, progress);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mCurrentProgress = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(2000);
            animator.start();
        }
        else {
            //属性动画更新进度，刷新界面
            ValueAnimator animator = ValueAnimator.ofFloat(0, mMaxProgress);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mCurrentProgress = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(2000);
            animator.start();
        }

    }

    /**
     * 设置最大进度
     * @param maxProgress
     */
    public void setMaxProgress(float maxProgress) {
        this.mMaxProgress = maxProgress;
    }
}

