package com.jsz.peini.utils.Bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.jsz.peini.listener.ImageCompressListener;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;

import net.bither.util.NativeUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import es.dmoral.toasty.Toasty;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

/**
 * Created 赵亚坤 th on 2017/1/4.
 */

public class BitmapAndStringUtils {
    public static String saveBitmap(Context context, String path, int size) {
        File file = new File(Conversion.LOCAL_IMAGE_CACHE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return NativeUtil.compressBitmap(path, Conversion.LOCAL_IMAGE_CACHE_PATH + "/squareImage" + System.currentTimeMillis() + ".jpg");
    }

    /**
     * 鲁班压缩
     */
    public static void saveBitmaps(final Context context, final String path, int size, final ImageCompressListener listener) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;  // 屏幕宽度（像素）
        int height = displayMetrics.heightPixels;  // 屏幕高度（像素）
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        int length = output.toByteArray().length;
        if (length > 500 * 1024) { //大于300k压缩
            Luban.compress(context, new File(path))
                    .setMaxWidth(width)              // limit image width
                    .setMaxHeight(height)           // limit image height
                    .putGear(Luban.CUSTOM_GEAR)     // use CUSTOM GEAR compression mode
                    .launch(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            String filePath = file.getPath();
//                            LogUtil.d("压缩成功了" + filePath + "\n" + file.getAbsolutePath());
                            listener.onCompressSuccess(filePath);

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toasty.normal(context, "压缩失败!").show();
                            listener.onCompressFailed(path, e.getMessage());
                        }
                    });
        } else {
            listener.onCompressSuccess(path);
        }
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据图片要显示的宽和高，对图片进行压缩，避免OOM
     *
     * @param path
     * @param width  要显示的imageview的宽度
     * @param height 要显示的imageview的高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
        // 获取图片的宽和高，并不把他加载到内存当中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, width, height);
        //使用获取到的inSampleSize再次解析图片(此时options里已经含有压缩比 options.inSampleSize，再次解析会得到压缩后的图片，不会oom了 )
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
     *
     * @param options
     * @param reqWidth  要显示的imageview的宽度
     * @param reqHeight 要显示的imageview的高度
     * @return
     * @compressExpand 这个值是为了像预览图片这样的需求，他要比所要显示的imageview高宽要大一点，放大才能清晰
     */
    private static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width >= reqWidth || height >= reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(width * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }

    /**
     * zhaoyakun 压缩算法
     */
    public static String sString() {
        return "";
    }

    public static Bitmap getBitmap(String imgPath, int reqWidth, int reqHeight) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.outWidth = reqWidth;
        newOpts.outHeight = reqHeight;
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }
}
