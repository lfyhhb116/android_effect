package com.lfy.dividing_rule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lfy.dividing_rule.util.ScreenUtil;

public class Pointer extends View {
    private Paint paint;
    private int screenWidth;

    public Pointer(Context context) {
        super(context);
        init(context);
    }

    public Pointer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Pointer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Pointer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.parseColor("#FF0000"));
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);

        screenWidth = ScreenUtil.onScreenWidth(context);


        Log.i("hhb", "screenWidth:" + screenWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine((screenWidth / 2 / 30) * 30, 500, (screenWidth / 2 / 30) * 30, 650, paint);
    }
}
