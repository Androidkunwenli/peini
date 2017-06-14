package com.jsz.peini;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import com.jsz.peini.utils.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.jsz.peini", appContext.getPackageName());

        LogUtil.d(bigDecimalConvertToString(BigDecimal.valueOf(40)));
        System.out.println("==========="+bigDecimalConvertToString(BigDecimal.valueOf(40)));
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
