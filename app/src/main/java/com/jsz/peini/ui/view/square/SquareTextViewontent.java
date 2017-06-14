package com.jsz.peini.ui.view.square;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by th on 2016/12/24.
 */

public class SquareTextViewontent extends TextView {
    public SquareTextViewontent(Context context) {
        super(context);
    }

    public SquareTextViewontent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextViewontent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int height = getLineHeight() * getLineCount();
//        LogUtil.i("这个是高度", "--> " + height);
//        setHeight(height);
    }
}