package com.lfy.dividing_rule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Ruler extends View {

    private Paint paint;
    //尺间隔的距离
    private int interval = 30;
    //尺数量
    private int number = 501;

    private int startY = 500;
    private int stopY = 550;
    private int stopYAliquot = 600;

    private float[] pts = new float[number * 4];
    private float[] textPointX = new float[51];

    private int mLastX;

    public Ruler(Context context) {
        super(context);
        init(context);
    }

    public Ruler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Ruler(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Ruler(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.parseColor("#E3E3E3"));
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);

        for (int i = 0; i < number; i++) {
            pts[i * 4] = interval * i;
            pts[i * 4 + 1] = startY;
            pts[i * 4 + 2] = interval * i;
            if (i % 10 == 0) {
                pts[i * 4 + 3] = stopYAliquot;
                textPointX[i / 10] = interval * i;
            } else {
                pts[i * 4 + 3] = stopY;
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, startY, interval * (number - 1), startY, paint);
        canvas.drawLines(pts, paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(50);

        for (int i = 0; i < textPointX.length; i++) {
            String value = String.valueOf(50 + i);
            float textWidth = paint.measureText(value);
            canvas.drawText(String.valueOf(50 + i), textPointX[i] - textWidth / 2, stopYAliquot + 100, paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                Log.i("hhb lfy", "move, deltaX:" + deltaX);
                scrollBy(-deltaX, 0);
                if (distanceListener != null) {
                    distanceListener.onDistance(deltaX);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (distanceListener != null) {
                    distanceListener.onDistance(x - mLastX);
                }
                break;
        }
        mLastX = x;
        return true;
    }

    private OnDistanceListener distanceListener;

    public void setDistanceListener(OnDistanceListener distanceListener) {
        this.distanceListener = distanceListener;
    }

    public interface OnDistanceListener {
        void onDistance(int distance);
    }

}
