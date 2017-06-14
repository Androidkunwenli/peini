package com.jsz.peini.listener;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/4/20.
 */

public interface ImageCompressListener {
    /**
     * 压缩成功
     *
     * @param images 已经压缩图片
     */
    void onCompressSuccess(String images);

    /**
     * 压缩失败
     *
     * @param images 压缩失败的图片
     * @param msg    失败的原因
     */
    void onCompressFailed(String images, String msg);
}
