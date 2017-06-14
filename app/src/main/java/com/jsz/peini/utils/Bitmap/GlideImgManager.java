package com.jsz.peini.utils.Bitmap;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsz.peini.R;
import com.jsz.peini.ui.view.GlideRoundTransform;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by th on 2017/1/6.
 */

public class GlideImgManager {

    /*//加载网络图片（普通）
        GlideImgManager.loadImage(this, imageArr[0], imageview1);

        //加载网络图片（圆角）
        GlideImgManager.loadRoundCornerImage(this, imageArr[1], imageview2);

        //加载网络图片（圆形）
        GlideImgManager.loadManCircleImage(this, imageArr[2], imageview3);

        //加载项目中的图片
        GlideImgManager.loadImage(this, R.mipmap.ic_launcher, imageview4);

        //加载网络图片（GIF）
        String gifUrl = "http://ww4.sinaimg.cn/mw690/bcc93f49gw1f6r1nce77jg207x07sx6q.gif";
        GlideImgManager.loadGifImage(this, gifUrl, imageview5);  */

        /*    //加载进度监听
            private void loadListener() {
                Glide.with(this)
                        .load(imageArr[0])
                        .into(new GlideDrawableImageViewTarget(imageview6) {
                            @Override
                            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                super.onResourceReady(drawable, anim);
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        });
            }*/

    /**
     * 高斯模糊
     */
    public static void loadImageBlur(Context context, String url, ImageView view, String s) {
        Glide.with(context.getApplicationContext())
                .load(url)
                .error("1".equals(s) ? R.mipmap.ic_nan : R.mipmap.ic_nv)
                .bitmapTransform(new BlurTransformation(context.getApplicationContext(), 25)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(view);
        //.crossFade(1000)
    }

    /**
     * 加载网络图片（普通）
     */
    public static void loadImage(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext()).load(url).placeholder(emptyImg).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(erroImg).into(iv);
        //原生 API
    }
    /**
     *设置图片大小
     */

    /**
     * 加载网络图片（普通）
     */
    public static void loadImage(Context context, String url, ImageView iv) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    /**
     * 空间展示头像
     */
    public static void loadImage(Context context, String url, ImageView iv, int x, int y) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext()).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).override(x, y).into(iv);
    }

    /**
     * 加载网络图片（普通）
     */
    public static void loadImage(Context context, int url, ImageView iv) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext()).load(url).into(iv);
    }

    /**
     * 加载网络图片（普通）
     * 1==男
     * /n
     * 2= 女
     * /n
     * 3 = 广告
     * /n
     * 4 = 积分界面
     * /n
     * 5 = 默认方形图
     */
    public static void loadImage(Context context, String url, ImageView iv, String sex) {
        if (context == null || (context instanceof Activity && ((Activity) context).isFinishing())) {
            return;
        }
        switch (sex) {
            case "1"://男
                Glide.with(context.getApplicationContext()).load(url).error(R.mipmap.ic_nan).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            case "2"://女
                Glide.with(context.getApplicationContext()).load(url).error(R.mipmap.ic_nv).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            case "3"://广告
                Glide.with(context.getApplicationContext()).load(url).error(R.drawable.ic_peini_banser).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            case "4"://积分
                Glide.with(context.getApplicationContext()).load(url).error(R.drawable.ic_peini_jifen).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            case "5"://默认方形图
                Glide.with(context.getApplicationContext()).load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_peini_fangxing).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            case "6"://空间背景默认图
                Glide.with(context.getApplicationContext()).load(url)
                        .error(R.drawable.square_bj).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            case "7"://原型默认图空白图
                Glide.with(context.getApplicationContext()).load(url).error(R.mipmap.white_bj_circular).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            case "8"://空间无图去上传图片去吧
                Glide.with(context.getApplicationContext()).load(url).error(R.drawable.wutu).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            case "9"://空间无图去上传图片去吧
                Glide.with(context.getApplicationContext()).load(url).error(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
            default:
                Glide.with(context.getApplicationContext()).load(url).error(R.drawable.ic_peini_fangxing).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                break;
        }
    }

    /**
     * 加载网络图片（GIF）
     */
    public static void loadGifImage(Context context, String url, ImageView iv) {
        Glide.with(context.getApplicationContext()).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.addphotos).error(R.mipmap.addphotos).into(iv);
    }

    /**
     * 加载网络图片（圆角）
     */
    public static void loadRoundCornerImage(Context context, String url, ImageView iv) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext()).load(url).placeholder(R.mipmap.addphotos).error(R.mipmap.addphotos).transform(new GlideRoundTransform(context, 10)).into(iv);
    }


    /**
     * 加载项目中的图片
     */
    public static void loadImage(Context context, final File file, final ImageView imageView) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageHead, String sex, int width, int height) {
        switch (sex) {
            case "1"://男
                Glide.with(context.getApplicationContext()).load(url).error(R.mipmap.ic_nan).centerCrop().override(width, height).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageHead);
                break;
            case "2"://女
                Glide.with(context.getApplicationContext()).load(url).error(R.mipmap.ic_nv).centerCrop().override(width, height).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageHead);
                break;
        }
    }
}
