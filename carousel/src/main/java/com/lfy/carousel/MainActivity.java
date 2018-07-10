package com.lfy.carousel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Runnable {

    private static final String TAG = "MainActivity";

    private ViewPager bannerViewPager;
    private LinearLayout dotView;

    private int[] bannerArray = {R.mipmap.shuffling_one, R.mipmap.shuffling_two, R.mipmap.shuffling_three, R.mipmap.shuffling_four};
    private boolean mIsUserTouched = false;

    private int mBannerPosition = 0;

    private Timer mTimer = new Timer();

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!mIsUserTouched) {
                runOnUiThread(MainActivity.this);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mTimer.schedule(mTimerTask, 3000, 3000);
    }


    public void initView() {
        bannerViewPager = findViewById(R.id.banner_vp);
        dotView = findViewById(R.id.dot_view);

        for (int i = 0; i < bannerArray.length; i++) {
            ImageView imageView = new ImageView(this);

            LinearLayout.LayoutParams viewPagerImageViewParams = new LinearLayout.LayoutParams(30, 30);
            viewPagerImageViewParams.setMargins(0, 0, 10, 0);
            imageView.setLayoutParams(viewPagerImageViewParams);
            imageView.setImageResource(R.mipmap.indicator_unchecked);

            dotView.addView(imageView);
        }


        BannerAdapter adapter = new BannerAdapter(this);
        bannerViewPager.setAdapter(adapter);
        bannerViewPager.addOnPageChangeListener(adapter);

        bannerViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        mIsUserTouched = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        mIsUserTouched = false;
                        break;
                }

                return false;
            }
        });

    }


    public void setIndicator(int position) {
        position = (position - 1 + DEFAULT_BANNER_SIZE) % DEFAULT_BANNER_SIZE;

        for (int i = 0; i < dotView.getChildCount(); i++) {
            ((ImageView) dotView.getChildAt(i)).setImageResource(R.mipmap.indicator_unchecked);
        }

        ((ImageView) dotView.getChildAt(position)).setImageResource(R.mipmap.indicator_checked);

    }


    @Override
    public void run() {
        mBannerPosition = (mBannerPosition + 1 + DEFAULT_BANNER_SIZE) % DEFAULT_BANNER_SIZE;

        Log.i(TAG, "mBannerPosition:" + mBannerPosition);

        if (mBannerPosition == FAKE_BANNER_SIZE - 1) {
            bannerViewPager.setCurrentItem(1, false);
        } else {
            bannerViewPager.setCurrentItem(mBannerPosition, false);
        }

    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();

        super.onDestroy();
    }

    private final static int DEFAULT_BANNER_SIZE = 4;
    private final static int FAKE_BANNER_SIZE = DEFAULT_BANNER_SIZE + 2;


    class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private Context context;

        public BannerAdapter(Context context) {
            this.context = context;
        }


        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            position = (position - 1 + DEFAULT_BANNER_SIZE) % DEFAULT_BANNER_SIZE;

            ImageView imageView = new ImageView(context);
            ViewGroup.LayoutParams viewPagerImageViewParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(viewPagerImageViewParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(bannerArray[position]);

            final int index = position;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "click banner position :" + index, Toast.LENGTH_SHORT).show();
                }
            });

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {

            int mCurItem = bannerViewPager.getCurrentItem();
            Log.i(TAG, "finish update before, mCurItem=" + mCurItem);
            if (mCurItem == 0) {
                mCurItem = DEFAULT_BANNER_SIZE;
                bannerViewPager.setCurrentItem(mCurItem, false);
            } else if (mCurItem == FAKE_BANNER_SIZE - 1) {
                mCurItem = 1;
                bannerViewPager.setCurrentItem(mCurItem, false);
            }

            Log.i(TAG, "finish update after, mCurItem=" + mCurItem);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "page selected, position=" + position);

            mBannerPosition = position;
            setIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
