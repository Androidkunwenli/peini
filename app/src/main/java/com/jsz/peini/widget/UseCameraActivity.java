package com.jsz.peini.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;

import java.io.File;

/**
 * 照片生成的目录在 sd卡的/hong/image/camera/.. .jpg
 *
 * @author baozi
 */
public class UseCameraActivity extends AppCompatActivity implements NonGestureLockInterface {
    private String mImageFilePath;
    public static final String IMAGEFILEPATH = "ImageFilePath";
    public final static String IMAGE_PATH = "image_path";
    private AppCompatActivity mContext;
    public final static int GET_IMAGE_REQ = 5000;
    private static Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断 activity被销毁后 有没有数据被保存下来
        if (savedInstanceState != null) {
            mImageFilePath = savedInstanceState.getString(IMAGEFILEPATH);
            LogUtil.d("123---savedInstanceState", mImageFilePath);
            File mFile = new File(IMAGEFILEPATH);
            if (mFile.exists()) {
                Intent rsl = new Intent();
                rsl.putExtra(IMAGE_PATH, mImageFilePath);
                setResult(Activity.RESULT_OK, rsl);
                LogUtil.d("123---savedInstanceState", "图片拍摄成功");
                finish();
            } else {
                LogUtil.d("123---savedInstanceState", "图片拍摄失败");
            }
        }

        mContext = this;
        applicationContext = getApplicationContext();
        if (savedInstanceState == null) {
            initialUI();
        }

    }

    public void initialUI() {
        //根据时间生成 后缀为  .jpg 的图片
        long ts = System.currentTimeMillis();
        mImageFilePath = getCameraPath() + "capture_" + System.currentTimeMillis() + ".jpg";
        File out = new File(mImageFilePath);
        showCamera(out);

    }

    private void showCamera(File out) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri value;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            value = Uri.fromFile(out);
        } else {
            value = FileProvider.getUriForFile(mContext, "com.jsz.peini.fileprovider", out);//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, value); // set
        startActivityForResult(intent, GET_IMAGE_REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (GET_IMAGE_REQ == requestCode && resultCode == Activity.RESULT_OK) {

            Intent rsl = new Intent();
            rsl.putExtra(IMAGE_PATH, mImageFilePath);
            mContext.setResult(Activity.RESULT_OK, rsl);
            mContext.finish();

        } else {
            mContext.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ImageFilePath", mImageFilePath + "");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    public static String getCameraPath() {
        String filePath = Conversion.LOCAL_IMAGE_CACHE_PATH;
        File file = new File(filePath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = null;
        return filePath;
    }

    @Override
    public boolean isGestureLock() {
        return false;
    }

//    public static String getImageRootPath() {
//        String filePath = getAppRootPath() + "/image";
//        File file = new File(filePath);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        file = null;
//        return filePath;
//    }
//
//    public static String getAppRootPath() {
//        String filePath = "/hong";
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            filePath = Environment.getExternalStorageDirectory() + filePath;
//        } else {
//            filePath = applicationContext.getCacheDir() + filePath;
//        }
//        File file = new File(filePath);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        file = null;
//        File nomedia = new File(filePath + "/.nomedia");
//        if (!nomedia.exists())
//            try {
//                nomedia.createNewFile();
//            } catch (IOException e) {
//            }
//        return filePath;
//    }

}
//┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃ 　
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//┃　　　┃   神兽保佑　　　　　　　　
//┃　　　┃   代码无BUG！
//┃　　　┗━━━┓
//┃　　　　　　　┣┓
//┃　　　　　　　┏┛
//┗┓┓┏━┳┓┏┛
//  ┃┫┫　┃┫┫
//  ┗┻┛　┗┻┛
