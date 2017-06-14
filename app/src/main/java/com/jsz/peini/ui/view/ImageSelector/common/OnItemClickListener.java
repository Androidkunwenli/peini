package com.jsz.peini.ui.view.ImageSelector.common;


import com.jsz.peini.ui.view.ImageSelector.bean.Image;

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
public interface OnItemClickListener {

    int onCheckedClick(int position, Image image);

    void onImageClick(int position, Image image);
}
