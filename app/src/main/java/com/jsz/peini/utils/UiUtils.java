package com.jsz.peini.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jsz.peini.PeiNiApp;


/**
 * 描述	      封装和ui相关的操作
 */
public class UiUtils {
    /**
     * ----====================================================================================
     */
    @SuppressWarnings("deprecation")
    public static int[] getScreenDispaly(Context context) {
        int width = getScreenWidth(context);//手机屏幕的宽度
        int height = getScreenHeight(context);//手机屏幕的高度
        float density = context.getResources().getDisplayMetrics().density;
        LogUtil.d("手机的密度是多少===" + density);
        int result[] = {width, height};
        return result;
    }

    /**
     * 得到上下文
     */
    public static Context getContext() {
        return PeiNiApp.context;
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串信息
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到String.xml中的字符串数组信息
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到Color.xml中的颜色信息
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到应用程序包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }


    /**
     * 加载布局
     */
    public static View inflate(Activity activity, int id) {
        return View.inflate(activity, id, null);
    }
    /**
     * 加载布局
     */
    public static View inflate(Context context, int id) {
        return View.inflate(context, id, null);
    }


    /**
     * 加载Adapter布局
     */
    public static View inflateRecyclerviewAdapter(Context activity, int id, ViewGroup parent) {
        return LayoutInflater.from(activity).inflate(id, parent, false);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 鑾峰緱灞忓箷楂樺害
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 鑾峰緱灞忓箷瀹藉害
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取View的缩略图
     *
     * @param view
     * @return ImageView
     */
    public static ImageView getDrawingCacheView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(PeiNiApp.context);
        iv.setImageBitmap(cache);
        return iv;
    }
}
