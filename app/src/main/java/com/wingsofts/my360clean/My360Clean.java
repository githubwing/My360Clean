package com.wingsofts.my360clean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 360内存清理模仿
 * Created by wing on 16/1/9.
 */
public class My360Clean extends View {
    private int mWidth;
    private int mHeight;

    private int mLineY = 600;

    private boolean isDrawBack = false;

    private int mBitmapX;
    private int mBitmapY;

    private int mWindowHeight;
    private int mFlyPercent = 100;
    //x坐标为线段中点
    Point supPoint = new Point(350, mLineY);
    private int mBackPercent;

    public My360Clean(Context context) {
        this(context, null);
    }

    public My360Clean(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public My360Clean(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wm = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowHeight = wm.getDefaultDisplay().getHeight();


    }

    private int endX;
    private int endY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = 200;
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = 200;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), com.wingsofts.my360clean.R.drawable.mb);


        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

//        BitmapFactory.decodeResource(getResources(), com.wingsofts.my360clean.R.drawable.t,opt);
//        int bgWidth = opt.outWidth;
//        int bgHeight = opt.outHeight;

        //按线的长度缩放背景图
//        Log.e("wing",bgWidth + " " +scale+"");
        opt.inSampleSize= 2;
//        opt.outWidth = 500;
        opt.inJustDecodeBounds = false;
        Bitmap background =BitmapFactory.decodeResource(getResources(), com.wingsofts.my360clean.R.drawable.t,opt);


        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(20);
        Path path = new Path();

        //坐标写死微调。。。别打我
        canvas.drawBitmap(background,100,mLineY - background.getHeight()+100,p);

        Point endPoint = new Point(600, mLineY);

        if (isDrawBack) {

            p.setColor(Color.YELLOW);
            path.moveTo(100, mLineY);
            path.quadTo(supPoint.x, mLineY + (supPoint.y - mLineY) * mFlyPercent / 100, endPoint.x, endPoint.y);
            canvas.drawPath(path, p);

            if(mFlyPercent >0) {
                canvas.drawBitmap(bitmap, mBitmapX, mBitmapY * mFlyPercent/100, p);
                mFlyPercent -=5;
                postInvalidateDelayed(10);
            }else {

                mFlyPercent = 100;
                isDrawBack = false;
            }

//            postInvalidateDelayed(10);
//            supPoint.x = 350;

        } else {

            p.setColor(Color.YELLOW);
            path.moveTo(100, mLineY);
            path.quadTo(supPoint.x, supPoint.y, endPoint.x, endPoint.y);
            canvas.drawPath(path, p);
//            canvas.drawPoint(supPoint.x, supPoint.y, p);

            mBitmapX = supPoint.x - bitmap.getWidth() / 2;
            mBitmapY = (supPoint.y -mLineY)/2 + mLineY- bitmap.getHeight();
            canvas.drawBitmap(bitmap, mBitmapX, mBitmapY, p);

        }

        p.setColor(Color.GRAY);
        canvas.drawCircle(100, endPoint.y, 10, p);
        canvas.drawCircle(endPoint.x,endPoint.y,10,p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:


//                supPoint.x = (int) event.getX();
                if(event.getY()>mLineY)
                supPoint.y = (int) event.getY();

                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                endX = (int) event.getX();
                endY = (int) event.getY();

                isDrawBack = true;
                invalidate();
                break;
        }
        return true;
    }
}
