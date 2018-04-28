package com.example.administrator.steps_count.tools;

import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.formatter.ValueFormatter;

import com.github.mikephil.charting.utils.ViewPortHandler;


/**
 * Created by Administrator on 2018/3/28/028.
 */

public class MyXFormatter implements ValueFormatter {
    private String[] mValues;

    public MyXFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
         return mValues[(int) value % mValues.length];
    }
}
