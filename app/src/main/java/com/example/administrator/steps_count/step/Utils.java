package com.example.administrator.steps_count.step;

import android.content.Context;
import android.util.TypedValue;

public class Utils {
    /**
     * dp2px
     */
    //像素的转换
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.getResources().getDisplayMetrics());
    }
}
