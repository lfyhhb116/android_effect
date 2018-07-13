package com.lfy.likenum.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lfy.likenum.R;

public class IikeImageView extends View implements View.OnClickListener {

    private Paint paint;
    private Bitmap likeUnselected, likeSelected, likeSelectedShining;
    private int width, height;
    private boolean isIllume;

    //缩放动画的时间
    private static final int SCALE_DURING = 150;
    //圆圈扩散动画的时间
    private static final int RADIUS_DURING = 100;

    private static final float SCALE_MIN = 0.9f;
    private static final float SCALE_MAX = 1f;

    public IikeImageView(Context context) {
        super(context);
        init();
    }

    public IikeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IikeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public IikeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint();
        likeUnselected = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        likeSelected = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        likeSelectedShining = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);

        width = likeSelected.getWidth();
        height = likeSelected.getHeight() + likeSelectedShining.getHeight();

        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isIllume) {
            canvas.drawBitmap(likeSelectedShining, 5, 0, paint);
            canvas.drawBitmap(likeSelected, 0, 28, paint);
        } else {
            canvas.drawBitmap(likeUnselected, 0, 28, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, height);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, height);
        } else {
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    public void onClick(View v) {
        startAnim();
    }

    public void startAnim() {
        if (isIllume) {
            startThumbDarkenAnim();
        } else {
            startThumbLightAnim();
        }
    }

    private void setLightThumbScale(float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        likeSelected = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        likeSelected = Bitmap.createBitmap(likeSelected, 0, 0, likeSelected.getWidth(), likeSelected.getHeight(), matrix, true);
        postInvalidate();
    }

    private void setDarkenThumbScale(float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        likeUnselected = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        likeUnselected = Bitmap.createBitmap(likeUnselected, 0, 0, likeUnselected.getWidth(), likeUnselected.getHeight(), matrix, true);
        postInvalidate();
    }

    /**
     * 拇指变亮：灰色拇指变小后，红色黑拇指变大
     */
    private void startThumbLightAnim() {

        ObjectAnimator darkenThumbScale = ObjectAnimator.ofFloat(this, "darkenThumbScale", SCALE_MAX, SCALE_MIN);
        darkenThumbScale.setDuration(SCALE_DURING);
        darkenThumbScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isIllume = true;
            }
        });

        ObjectAnimator lightThumbScale = ObjectAnimator.ofFloat(this, "lightThumbScale", SCALE_MIN, SCALE_MAX);
        lightThumbScale.setDuration(SCALE_DURING);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(darkenThumbScale).after(lightThumbScale);
        animatorSet.start();
    }

    /**
     * 拇指变灰：灰色拇指变小后，红色黑拇指变大
     */
    private void startThumbDarkenAnim() {
        ObjectAnimator lightThumbScale = ObjectAnimator.ofFloat(this, "lightThumbScale", SCALE_MIN, SCALE_MAX);
        lightThumbScale.setDuration(SCALE_DURING);
        lightThumbScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                isIllume = false;
            }
        });

        lightThumbScale.start();
    }
}
