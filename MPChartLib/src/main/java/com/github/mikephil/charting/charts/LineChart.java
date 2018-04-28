
package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.renderer.LineChartRenderer;

/**
 * Chart that draws lines, surfaces, circles, ...
 * 
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    private boolean startAtZero;
    private boolean drawYValues;
    private boolean drawBorder;
    private boolean drawVerticalGrid;
    private int gridColor;
    private float gridWidth;
    private Typeface valueTypeface;

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
    }
    
    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if(mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }

    public void setStartAtZero(boolean startAtZero) {
        this.startAtZero = startAtZero;
    }

    public void setDrawYValues(boolean drawYValues) {
        this.drawYValues = drawYValues;
    }

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    public void setDrawVerticalGrid(boolean drawVerticalGrid) {
        this.drawVerticalGrid = drawVerticalGrid;
    }

    public void setGridColor(int gridColor) {
        this.gridColor = gridColor;
    }

    public void setGridWidth(float gridWidth) {
        this.gridWidth = gridWidth;
    }

    public void setValueTypeface(Typeface valueTypeface) {
        this.valueTypeface = valueTypeface;
    }
}
