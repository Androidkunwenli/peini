package com.jsz.peini.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.jsz.peini.R;

/**
 * Created by th on 2017/1/23.
 */

public class RefreshHeaderView extends LinearLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private Context mContext;
    private LinearLayout mView;
    private ImageView mImageView;
    private TextView mTextView;

    public RefreshHeaderView(Context context) {
        super(context);
        mContext = context;

    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_refresh_header, this, true);
        mImageView = (ImageView) mView.findViewById(R.id.iv_image);
        mTextView = (TextView) mView.findViewById(R.id.tv_text);
    }

    @Override
    public void onRefresh() {
//        setText("正在刷新");
//        setBackgroundResource(R.mipmap.nan);
        setTextViewText("正在刷新");
        setImageResource(R.drawable.zz_refresh);
    }

    @Override
    public void onPrepare() {
//        setText("你-2");


    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
//                setText("松开刷新");
//                setBackgroundResource(R.mipmap.nv);
                setTextViewText("松开刷新");
                setImageResource(R.drawable.sf_refresh);
            } else {
//                setText("下拉刷新");
                setTextViewText("下拉刷新");
                setImageResource(R.drawable.xl_refresh);
            }
        } else {
//            setText("关闭刷新");
            setTextViewText("刷新成功");
            System.out.println("-6");
        }
    }

    @Override
    public void onRelease() {
//        setText("-8");
        System.out.println("-8");

    }

    @Override
    public void onComplete() {
//        setText("刷新结束");
        setTextViewText("刷新成功");
        setImageResource(R.drawable.sc_refresh);
    }

    @Override
    public void onReset() {
//        setText("-7");
        setTextViewText("下拉刷新");
    }

    /**
     * 设置图片资源
     */
    public void setImageResource(int resId) {
        mImageView.setBackgroundResource(resId);
    }

    /**
     * 设置显示的文字
     */
    public void setTextViewText(String text) {
        mTextView.setText(text);
    }
}