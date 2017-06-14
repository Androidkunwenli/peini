package com.jsz.peini.utils.time;

import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.widget.Button;

import com.jsz.peini.R;
import com.jsz.peini.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created 赵亚坤 th on 2017/1/3.
 * 时间格式化 - 倒计时
 */

public class TimeUtils {
    /**
     * 倒计时的工具类
     */
    public static void TimeCount(final Button view, long fen, long miao) {
        CountDownTimer timer = new CountDownTimer(fen, miao) {
            ColorStateList originalColor = view.getTextColors();

            @Override
            public void onTick(long millisUntilFinished) {
                view.setEnabled(false);
                view.setAllCaps(false);
                view.setTextColor(UiUtils.getColor(R.color.cccccc));
                view.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                view.setText("重新验证");
                view.setTextColor(originalColor);
                view.setEnabled(true);
            }
        };
        timer.start();
    }

    /*获取年*/
    public static String getYyyy() {
        SimpleDateFormat mYyyy = new SimpleDateFormat("yyyy");
        return mYyyy.format(new Date());
    }

    /*获取小时*/
    public static String getHh() {
        SimpleDateFormat mYyyy = new SimpleDateFormat("HH");
        return mYyyy.format(new Date());
    }

    /*获取分钟*/
    public static String getSs() {
        SimpleDateFormat mYyyy = new SimpleDateFormat("mm");
        return mYyyy.format(new Date());
    }

    /**
     * 时间格式 yyyy-MM-dd
     */
    public static final String FORMART1 = "yyyy-MM-dd";

    /**
     * 时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMART2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式 yyyyMMdd
     */
    public static final String FORMART3 = "yyyyMMdd";

    /**
     * 时间格式 yyyy年MM月dd日
     */
    public static final String FORMART4 = "yyyy年MM月dd日";

    /**
     * 时间格式 yyyy年MM月dd日 HH:mm:ss
     */
    public static final String FORMART5 = "yyyy年MM月dd日 HH:mm:ss";
    /**
     * 时间格式 yyyyMMddHHmmss
     */
    public static final String FORMART6 = "yyyyMMddHHmmss";

    /**
     * 时间格式 yyyy/MM/dd HH:mm:ss
     */
    public static final String FORMART7 = "yyyy/MM/dd HH:mm:ss";
    /**
     * 时间格式 yyyy.MM.dd HH:mm:ss
     */
    public static final String FORMART8 = "yyyy.MM.dd HH:mm:ss";
    private static SimpleDateFormat sdf;

    // 获取当前时间
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date submitTime = new Date(System.currentTimeMillis());// 获取当前时间
        String strSubmitTime = formatter.format(submitTime);
        return strSubmitTime;

    }


    // 获取当前时间
    public static String getSquareTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date submitTime = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(submitTime);

    }

    /**
     * 时间格式转换
     */
    public static String longToDate(long currentTime, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Long time = new Long(currentTime);
        String d = format.format(time);
        return d;
    }

    /**
     * 获取当前时间 FORMART1:yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentTime2() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date submitTime = new Date(System.currentTimeMillis());// 获取当前时间
        String strSubmitTime = formatter.format(submitTime);

        return strSubmitTime;

    }

    /**
     * 返回指定格式的当天日期 例如：getCurrentDate("yyyy-MM-dd");
     *
     * @param pattern 时间样式
     * @return 返回指定格式的当天日期
     */
    public static String getCurrentDate(String pattern) {
        return dateToString(new java.util.Date(), pattern);
    }

    /**
     * Date转换为String
     *
     * @param date
     * @param pattern
     * @return 字符串格式日期
     */
    public static String dateToString(java.util.Date date, String pattern) {
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
        } else {
            sdf.applyPattern(pattern);
        }
        return sdf.format(date);
    }

}
