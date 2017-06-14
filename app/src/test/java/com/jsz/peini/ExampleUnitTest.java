package com.jsz.peini;

import android.text.TextUtils;

import java.math.BigDecimal;


public class ExampleUnitTest {
    int g = 1000001;
    int p = 3000;
    int x = 200;
    int y = 34;
    int a = 0;
    int b = 0;
    int aa = 0;
    int bb = 0;

    public void addition_isCorrect() throws Exception {
        a = g ^ x % p;

        b = g ^ y % p;

        aa = b ^ x;

        bb = a ^ y;

        System.out.println(aa + "===" + bb+"==========="+bigDecimalConvertToString(BigDecimal.valueOf(40)));

    }

    /**
     * BigDecimal 类型转换为 String；
     *
     * @param bigDecimal bigDecimal
     * @return 截取结尾小数部分的0   eg.：1.000 -> 1； 1.100 -> 1.1
     */
    public static String bigDecimalConvertToString(BigDecimal bigDecimal) {
        String originStr = bigDecimal.toString();
        String resultStr = originStr.replaceAll("\\.*0*$", "");
        if (!TextUtils.isEmpty(resultStr)) {
            return resultStr;
        } else {
            return "0";
        }
    }
}