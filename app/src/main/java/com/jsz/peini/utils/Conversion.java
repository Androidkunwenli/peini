package com.jsz.peini.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.model.address.CityBean;
import com.jsz.peini.utils.Bitmap.BitmapAndStringUtils;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 15089 on 2017/2/18.
 */

public class Conversion {
    /*支付*/
    public static final int g = 1000001;
    public static final int p = 3000;
    public static final int a = 0;

    /*--------------------------------Int--------------------------------------*/
    /**
     * 支付
     */
    public static final int PAY_SUCCESS = 111;
    public static final int MONEY_SUCCESS = 222;
    /**
     * 访问数据成功
     */
    public static final int DATASUCCESS = 200;
    /**
     * 单纯的返回成功
     */
    public static final int DATA_SUCCESS = 201;

    /**
     * 地址的回调
     */
    public static String ADDRESS = "address";
    /**
     * 经度
     */
    public static String LATITUDE = "Latitude";
    /**
     * 纬度
     */
    public static String LONGITUDE = "Longitude";
    /**
     * 查看大图
     */
    public static String LARGERIMAGEACTIVITY = "LargerImageActivity";
    //第几个图片
    public static String SHOWINDEX = "showIndex";
    //数据
    public static String DATA = "data";

    /**
     * 主色调
     */
    public static int FB4E30 = UiUtils.getResources().getColor(R.color.RED_FB4E30);

    /*--------------------------------String--------------------------------------*/

    /**
     * 类型
     */
    public static String TYPE = "type";
    /**
     * 1任务买单；3到店买单；4打赏；5金币转赠
     */
    public static String ORDERTYPE = "ordertype";
    //token
    public static String TOKEN = "token";
    /**
     * 他人token
     */
    public static String USERID = "userId";
    /**
     * 选择
     */
    public static String CHOICE = "Choice";
    /**
     * 商家id
     */
    public static String SELLERINFOID = "sellerInfoId";
    /**
     * 任务id
     */
    public static String TASKID = "taskid";
    /**
     * 任务标题
     */
    public static String TITLE = "title";
    /**
     * 任务等id
     */
    public static String ID = "id";
    /**
     * 手机号
     */
    public static String PHONE = "phone";
//    /**
//     * 两个手机号获取任务  聊天界面
//     */
//    public static final String NAMESTR = "namestr";
    /**
     * 他人名字
     */
    public static String OTHERSRNMAE = "othersrnmae";
    /**
     * 界面传输的集合
     */
    public static String LIST = "list";
    /**
     * 排行榜
     */
    public static String RANKING = "Ranking";
    /**
     * 商家的名字
     */
    public static String SELLERINFONAME = "sellername";
    /**
     * 商家的业态
     */
    public static String SELLERBIGNAME = "sellerBigName";
    /**
     * 名字
     */
    public static String NAME = "name";
    /**
     * 年龄
     */
    public static String AGE = "age";
    /**
     * 性别
     */
    public static String SEX = "sex";
    /**
     * 标签
     */
    public static String LABEL = "Label";
    /**
     * 头像
     */
    public static String IMAGE = "image";
    /**
     * 商家名字
     */
    public static String SELLERNMAE = "sellername";
    /**
     * 订单号
     */
    public static String ORDERID = "orderid";
    /**
     * 备用订单号
     */
    public static String ORDERIDS = "orderids";
    /**
     * 数量
     */
    public static String NUMBAR = "numbar";
    /**
     * 商家的业态类型
     */
    public static String SELLERBIGTYPE = "sellerBigType";

    /**
     * 商家管理ID
     */
    public static String STORE_MANAGE_ID = "store_manage_id";
    /**
     * 网络请求的数据的大小
     */
    public static String PAGESIZE = "25";
    //      mSignWord.setText(infoBean.getSignWord());
//    infoBeanGoldList = infoBean.getGoldList();
//        mGoldList.setVisibility(!"0".equals(infoBeanGoldList) ? View.VISIBLE : View.GONE);
//    infoBeanBuyList = infoBean.getBuyList();
//        mBuyList.setVisibility(!"0".equals(infoBeanBuyList) ? View.VISIBLE : View.GONE);
//    infoBeanIntegrityList = infoBean.getIntegrityList();
//        mIntegrityList.setVisibility(!"0".equals(infoBeanIntegrityList) ? View.VISIBLE : View.GONE);
    public static String GOLD = "GOLD";

    public static String BUY = "BUY";

    public static String INTEGRITY = "INTEGRITY";


    public static final String WX_RESULT = "com.jsz.peini.WX_RESULT";
    public static final String WX_RESULT_CODE_FLAG = "wx_result_code_flag";
    public static final String WX_RESULT_EXT_DATA_FLAG = "wx_result_ext_data_flag";
    public static final String LOCAL_IMAGE_CACHE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/PEINI_CACHE/";
    /**
     * 取消任务手机号
     */
    public static String CANCELPHONENUMBER = "cancelphonenumber";
    /**
     * 取消任务的id
     */
    public static String CANCELTASKNUMBER = "canceltasknumber";


    public static String NETWORKERROR = "当前网络不可用，请检查您的设置";

    public static String SHARED_PREFERENCES_FIRST_INSTALL_FLAG = "shared_preferences_first_install_flag";
    public static String BOOLEAN = "boolean";
    public static String INFORMATION = "information";
    /**
     * 图片的是文件
     */
    public static String FILE = "file";
    /**
     * 个推返回的url
     */
    public static String URl = "url";
    public static String TEXT = "text";
    public static String BEAN = "Bean";


    /*--------------------------------Method--------------------------------------*/

    /**
     * 获取集合大小
     */
    public static int getSize(List list) {
        return (list != null) && list.size() > 0 ? list.size() : 0;
    }

    /**
     * 颜色转换
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 转换小数点剩余6位数
     */
    public static double getSixDecimal(String decimal) {
        DecimalFormat df = new DecimalFormat("######0.000000");
        return Double.parseDouble(df.format(decimal));
    }

    /**
     * 转换小数点剩余2位数
     *
     * @param decimal
     */
    public static double getTowDecimal(double decimal) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return Double.parseDouble(df.format(decimal));
    }

    //=================================================================
    private static String startStr = "cnbj";
    private static String endStr = "arh6m";
    public static String aesPass = "24jsz21sjzhb2017";
    //=================================================================
    /**
     int appA = (int) SpUtils.get(mActivity, "x", 0);
     hong = g ^ appA % p;
     int serverB = (int) SpUtils.get(mActivity, "serverB", 0);
     String Token = MD5Utils.encode((serverB ^ appA) + "");
     */
    /**
     * 获取appa
     */
    private static int getAppServer() {
        return (int) SpUtils.get(PeiNiApp.context, "x", 0);
    }

    /**
     * 访问网络传输的appA
     */
    public static String getNetAppA() {
        return aesEncode(Conversion.g ^ RandomappA() % Conversion.p);
    }

    /**
     * 网络传输的token
     */
    public static String getToken() {
        return MD5Utils.encode(((int) SpUtils.get(PeiNiApp.context, "serverB", 0) ^ getAppServer()) + "");
    }

    /* int x = new Random().nextInt(2500) + 1;
            hong = g ^ x % p;
            LogUtil.d("登录生成的随机数" + x + "登录传输的appA" + hong);
            SpUtils.put(mActivity, "x", x);*/
    private static int RandomappA() {
        int x = new Random().nextInt(2500) + 1;
        SpUtils.put(PeiNiApp.context, "x", x);
        return x;
    }

    public static String getRandomappA() {
        return aesEncode(Conversion.g ^ RandomappA() % Conversion.p);
    }

    private static String aesEncode(int appA) {
        System.out.println(appA);
        String afterEncrypt = AesUtils.aesEncrypt(startStr + appA + endStr, aesPass);
        System.out.println(afterEncrypt);
        String afterDecode = AesUtils.aesDecrypt(afterEncrypt, aesPass);
        System.out.println(afterDecode);

        return afterEncrypt;
    }

    public static int getServerBFromString(String content) {
        if (TextUtils.isEmpty(content)) {
            return -1;
        }
        Matcher matcher = Pattern.compile("cnbj([0-9]*)arh6m").matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group(1);
            try {
                return Integer.parseInt(tmp);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * 纠正照片的旋转角度
     *
     * @param context
     * @param imagePath
     */
    public static String correctImage(Context context, String imagePath) {
        int degree;
        if ((degree = getBitmapDegree(imagePath)) != 0) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;  // 屏幕宽度（像素）
            int height = displayMetrics.heightPixels;  // 屏幕高度（像素）
            Bitmap bitmap = BitmapAndStringUtils.decodeSampledBitmapFromPath(imagePath, width, height);
            if (bitmap == null) return imagePath;
            Bitmap resultBitmap = rotateBitmapByDegree(bitmap, degree);
            if (resultBitmap == null) return imagePath;
            try {
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(imagePath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            return compressHeadPhoto(resultBitmap);
        }
        return imagePath;
    }

    public static String correctImage(String imagePath) {
        return doRotateImageAndSaveStrategy2(imagePath);
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    private static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            int width = bm.getWidth();
            int height = bm.getHeight();
            LogUtil.d("旋转的宽" + width + "旋转的高" + height);
            returnBm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        } catch (OutOfMemoryError e) {
            LogUtil.d("旋转照片报错" + e.getMessage());
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 旋转后存储照片
     */

    private static String compressHeadPhoto(final Bitmap btImage) {
        FileOutputStream out = null;
        File file = new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "capture_sq" + System.currentTimeMillis() + ".jpg");
        try {
            out = new FileOutputStream(file);
            btImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            System.out.println("___________保存的__sd___下_______________________");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return file.getPath();
    }

    private static int outWidth = 0;//输出bitmap的宽
    private static int outHeight = 0;//输出bitmap的高

    //计算sampleSize
    private static int caculateSampleSize(String imgFilePath, int rotate) {
        outWidth = 0;
        outHeight = 0;
        int imgWidth = 0;//原始图片的宽
        int imgHeight = 0;//原始图片的高
        int sampleSize = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imgFilePath);
            BitmapFactory.decodeStream(inputStream, null, options);//由于options.inJustDecodeBounds位true，所以这里并没有在内存中解码图片，只是为了得到原始图片的大小
            imgWidth = options.outWidth;
            imgHeight = options.outHeight;
            //初始化
            outWidth = imgWidth;
            outHeight = imgHeight;
            //如果旋转的角度是90的奇数倍,则输出的宽和高和原始宽高调换
            if ((rotate / 90) % 2 != 0) {
                outWidth = imgHeight;
                outHeight = imgWidth;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        //计算输出bitmap的sampleSize
        while (imgWidth / sampleSize > outWidth || imgHeight / sampleSize > outHeight) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    /**
     * 图片处理
     */
    private static String doRotateImageAndSaveStrategy2(String filePath) {
        int rotate = getBitmapDegree(filePath);
        String photo = filePath;
        if (rotate == 0)
            return photo;
        //得到sampleSize
        int sampleSize = caculateSampleSize(filePath, rotate);
        if (outWidth == 0 || outHeight == 0)
            return photo;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        //适当调整颜色深度
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = false;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            Bitmap srcBitmap = BitmapFactory.decodeStream(inputStream, null, options);//加载原图
            //test
            Bitmap.Config srcConfig = srcBitmap.getConfig();
            int srcMem = srcBitmap.getRowBytes() * srcBitmap.getHeight();//计算bitmap占用的内存大小

            Bitmap destBitmap = rotateBitmapByDegree(srcBitmap, rotate);
            int destMem = srcBitmap.getRowBytes() * srcBitmap.getHeight();
            srcBitmap.recycle();

            //保存bitmap到文件（覆盖原始图片）
            photo = compressHeadPhoto(destBitmap);
            destBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return photo;
    }

    public static String toString(InputStream is) {
        return toString(is, "utf-8");
    }

    private static String toString(InputStream is, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else {
                    sb.append(line).append("\n");
                }
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.getMessage();
        }
        return sb.toString();
    }

    /**
     * 获取地址列表
     */
    public static List<CityBean> getCityData(Context context) {
        List<CityBean> mAreaCity = new ArrayList<>();
        try {
            String json = Conversion.toString(context.getAssets().open("City.json"));
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                String toString = jsonArray.getJSONObject(i).toString().trim();
                mAreaCity.add(new Gson().fromJson(toString, CityBean.class));
                System.out.println("解析出来的: " + toString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAreaCity;
    }

    public static String setFormatNum(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return "0";
        }
        if (numStr.contains(".")) {
            return numStr.replaceAll("\\.*0*$", "");
        } else {
            return numStr;
        }
    }

}
