package com.jsz.peini.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by th on 2017/1/23.
 */

public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLoadMore() {
        setText("正在加载");
    }

    @Override
    public void onPrepare() {
        setText("你-2");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                setText("松开加载");
            } else {
                setText("上拉加载");
            }
        } else {
            setText("关闭加载");
        }
    }

    @Override
    public void onRelease() {
        setText("-8");
    }

    @Override
    public void onComplete() {
        setText("加载结束");
    }

    @Override
    public void onReset() {
        setText("-7");
    }
}