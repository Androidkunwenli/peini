package com.jsz.peini.utils;

import android.text.TextUtils;

/**
 * Created by th on 2017/1/4.
 */

public class StringUtils {
    public static boolean isNoNull(String s) {
        return !TextUtils.isEmpty(s) && !s.equals("null");
    }

    public static boolean isNull(String s) {
        return TextUtils.isEmpty(s) || s.equals("null");
    }

    public static boolean isHttpPath(String s) {
        s = s.trim();
        return !TextUtils.isEmpty(s) && (s.toLowerCase().startsWith("http://") || s.toLowerCase().startsWith("https://"));
    }

    public static String markByContainQuestion(String s) {
        return s.contains("?") ? "&" : "?";
    }
}
