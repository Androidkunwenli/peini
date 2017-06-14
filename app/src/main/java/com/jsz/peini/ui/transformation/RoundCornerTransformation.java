package com.jsz.peini.ui.transformation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.jsz.peini.utils.Size;

/**
 * 圆角矩形
 * Created by huizhe.ju on 2017/3/14.
 */
public class RoundCornerTransformation extends BitmapTransformation {
    private float mRadius = 0f;
    private boolean isCrop = false;

    public RoundCornerTransformation(Context context) {
        this(context, (int) (Resources.getSystem().getDisplayMetrics().density * 4));
    }

    public RoundCornerTransformation(Context context, int radius) {
        this(context, radius, false);
    }

    public RoundCornerTransformation(Context context, int radius, boolean crop) {
        super(context);
        mRadius = radius;
        isCrop = crop;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null) return null;

        Size size;
        if (isCrop) {
            size = calcAppropriateSize(source.getWidth(), source.getHeight(), outWidth, outHeight);
        } else {
            size = new Size(source.getWidth(), source.getHeight());
        }

        Bitmap result = pool.get(size.getWidth(), size.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size.getWidth(), size.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        if (isCrop) {
            int xOffset = (int) ((source.getWidth() - size.getWidth()) / 2f);
            int yOffset = (int) ((source.getHeight() - size.getHeight()) / 2f);
            Bitmap cropped = Bitmap.createBitmap(source, xOffset, yOffset, size.getWidth(), size.getHeight());
            paint.setShader(new BitmapShader(cropped, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        } else {
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        }
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, size.getWidth(), size.getHeight());
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);
        return result;
    }

    private static Size calcAppropriateSize(int width, int height, int outWidth, int outHeight) {
        float scale1 = width * 1f / height;
        float scale2 = outWidth * 1f / outHeight;

        if (scale1 == scale2) {
            return new Size(width, height);
        } else if (scale1 > scale2) {
            int resultW = (int) (height * scale2);
            return new Size(resultW, height);
        } else {
            int resultH = (int) (width / scale2);
            return new Size(width, resultH);
        }
    }

    @Override
    public String getId() {
        return "roundCornerTransformation";
    }
}
