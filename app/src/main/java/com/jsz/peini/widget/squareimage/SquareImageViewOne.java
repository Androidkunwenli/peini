package com.jsz.peini.widget.squareimage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.jsz.peini.utils.UiUtils;

/**
 * Created by lenovo on 2017/3/26.
 */

public class SquareImageViewOne extends AppCompatImageView {

    private Context mContext;

    public SquareImageViewOne(Context context) {
        super(context);
        mContext = context;
    }

    public SquareImageViewOne(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public SquareImageViewOne(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(UiUtils.getScreenWidth(mContext), (int) (UiUtils.getScreenWidth(mContext) / 1.5));
    }
}
