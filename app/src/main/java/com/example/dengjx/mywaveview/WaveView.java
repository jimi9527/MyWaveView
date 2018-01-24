package com.example.dengjx.mywaveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dengjx on 2018/1/9.
 */

public class WaveView extends View {

    private static final String TAG = "WaveView";
    // 波纹创建的速度
    private int mSpeed = 500;
    private int mDuration = 2000;
    // 初始时波纹的半径
    private float mInitRadius = 50f;
    // 最大波纹的半径
    private float mMaxRadius = 250f;

    private float mMaxRadiusRate = 0.85f;
    private boolean mIsRunning;
    private List<Circle> mCircleList = new ArrayList<>();
    private long mLastCreateTime;
    private Paint mPaint;

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()){
            Circle circle = iterator.next();
            if(System.currentTimeMillis() - circle.mCreateTime < mDuration){
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(getWidth() / 2 ,getHeight() / 2 ,circle.getCurrentRadius(),mPaint);
            }else {
                iterator.remove();
            }
        }
        if(mCircleList.size() > 0){
            postInvalidateDelayed(10);
        }

    }

    private void newCircle(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - mLastCreateTime < mSpeed ){
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }
    public void start(){
        if(!mIsRunning){
            mIsRunning = true;
            mCreateCircle.run();
        }
    }


    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
             if(mIsRunning){
                 newCircle();
                 postDelayed(mCreateCircle,mSpeed);
             }
        }
    };

    class Circle{
        private long mCreateTime;
        public Circle(){
            mCreateTime = System.currentTimeMillis();
        }
        public int getAlpha (){
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f/ mDuration;
            return (int) ((1.0f - percent) * 255);
        }
        public float getCurrentRadius(){
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitRadius + percent * (mMaxRadius - mInitRadius);
        }
    }

}
