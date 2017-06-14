package com.jsz.peini.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jandl on 2017/3/30.
 */

public class UrlUtils {

    /**
     * 对Url中的中文进行UrlEncode编码编码
     *
     * @param url 原url
     * @return 编码后的url
     */
    public static String encode(String url) {

        try {
            Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(url);
            while (matcher.find()) {
                String tmp = matcher.group();
                url = url.replaceAll(tmp, java.net.URLEncoder.encode(tmp, "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return url;
    }
}
