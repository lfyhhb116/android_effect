package com.lfy.dividing_rule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lfy.dividing_rule.util.ScreenUtil;
import com.lfy.dividing_rule.widget.Ruler;

public class MainActivity extends AppCompatActivity implements Ruler.OnDistanceListener {

    private TextView mainTv;
    private double value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int screenWidth = ScreenUtil.onScreenWidth(this);
        mainTv = findViewById(R.id.main_tv);
        mainTv.setText(String.valueOf((screenWidth / 2 / 30) * 0.1 + 50));
        value = screenWidth / 2;

        Ruler ruler = findViewById(R.id.main_ruler);
        ruler.setDistanceListener(this);
    }

    @Override
    public void onDistance(int distance) {
        if (distance < 0) {
            value += -distance;
        } else {
            value -= distance;
        }
        mainTv.setText(String.valueOf((value / 30) * 0.1 + 50));
    }
}
