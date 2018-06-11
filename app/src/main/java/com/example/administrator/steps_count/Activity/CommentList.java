package com.example.administrator.steps_count.Activity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2018/5/21.
 */

public class CommentList extends ListView {
    public CommentList(Context context) {
        super(context);
    }

    public CommentList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int MesureHeight=MeasureSpec.makeMeasureSpec(400,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, MesureHeight);

    }
}
