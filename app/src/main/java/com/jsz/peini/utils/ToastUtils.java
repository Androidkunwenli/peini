package com.jsz.peini.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jsz.peini.R;

public class ToastUtils {

    public static void ToastAddress(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_item_address, null);//加载布局文件
        Toast toast = new Toast(context);//创建一个toast
        toast.setDuration(Toast.LENGTH_SHORT);          //设置toast显示时间，整数值
        toast.setGravity(Gravity.CENTER, 0, 0);    //toast的显示位置，这里居中显示
        toast.setView(view);     //設置其显示的view,
        toast.show();             //显示toast
    }

}
