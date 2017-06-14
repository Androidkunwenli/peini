package com.jsz.peini.widget.loading;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jsz.peini.R;

/**
 * 加载中Dialog
 *
 * @author lexyhp
 */
public class LoadingDialog extends AlertDialog {

    private TextView tips_loading_msg;
    private int layoutResId;
    private String mText;

    /**
     * 构造方法
     *
     * @param context     上下文
     * @param layoutResId 要传入的dialog布局文件的id
     */
    public LoadingDialog(Context context, int layoutResId, int themeResId) {
        super(context, themeResId);
        this.layoutResId = layoutResId;
    }


    public LoadingDialog(Context context, int layoutResId) {
        super(context);
        this.layoutResId = layoutResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutResId);
        tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
        tips_loading_msg.setText(mText);

    }

    public void setText(String text) {
        mText = text;
    }
}
