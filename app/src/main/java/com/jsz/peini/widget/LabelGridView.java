package com.jsz.peini.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by ljb on 2015/9/8.
 */
public class LabelGridView extends GridView {

    public LabelGridView(Context context) {
        super(context);
    }

    public LabelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 屏蔽GridView滑动
     * 因为在外层会套ScrollView ,让GridView直接全部显示 */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
