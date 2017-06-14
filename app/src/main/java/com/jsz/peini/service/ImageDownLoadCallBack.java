package com.jsz.peini.service;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/4/10.
 */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(Bitmap bitmap, String path);

    void onDownLoadFailed();
}
