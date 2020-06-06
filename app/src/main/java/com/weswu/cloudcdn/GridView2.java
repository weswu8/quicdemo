package com.weswu.cloudcdn;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridView2 extends GridView {
    public boolean isOnMeasure;
    public GridView2(Context context) {
        super(context);
    }

    public GridView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public GridView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }
}