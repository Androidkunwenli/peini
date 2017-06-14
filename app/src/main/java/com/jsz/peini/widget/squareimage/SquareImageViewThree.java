package com.jsz.peini.widget.squareimage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.jsz.peini.utils.UiUtils;

/**
 * Created by lenovo on 2017/3/26.
 */

public class SquareImageViewThree extends AppCompatImageView {

    private Context mContext;

    public SquareImageViewThree(Context context) {
        super(context);
        mContext = context;
    }

    public SquareImageViewThree(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public SquareImageViewThree(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(UiUtils.getScreenWidth(mContext) / 3-5, UiUtils.getScreenWidth(mContext) / 3-5);
    }
}
