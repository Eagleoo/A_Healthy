package com.example.administrator.steps_count.mall;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by PC on 2018/4/2.
 */

public class My_Scrollview extends ScrollView {
    private OnScrollChangeListener mOnScrollChangeListener;

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }
    public My_Scrollview(Context context) {
        super(context);
    }

    public My_Scrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public My_Scrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public interface OnScrollChangeListener{
        void onScrollChanged(My_Scrollview scrollView, int x, int y, int oldx, int oldy);
    }
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(mOnScrollChangeListener!=null){
            mOnScrollChangeListener.onScrollChanged(this,x,y,oldx,oldy);
        }
    }


}
