package com.lfy.dividing_rule.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtil {

    public static int onScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

}
