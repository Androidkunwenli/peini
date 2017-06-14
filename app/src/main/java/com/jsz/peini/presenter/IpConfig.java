package com.jsz.peini.presenter;


public class IpConfig {

    /**
     * 基本网址
     */
    //正式
    public static String REMOTE_IP = "http://app.91peini.com/pnservice/";
    public static String REMOTE_HTTPS_IP = "https://app.91peini.com:6443/pnservice/";
    //测试
    /**
     * http://test.91peini.com/pnservice
     * https://test.91peini.com/pnservice
     */
    public static String LOCAL_IP = "http://192.168.150.183:50/pnservice/";
    public static String REMOTE_HTTPS_IP_TAST = "https://192.168.150.183:443/pnservice/";

    //测试
    static String HTTPS = "https://apk.91peini.com:6443/pnservice/";
    static String HTTP = "http://apk.91peini.com/pnservice/";

    //赋值
//    public static String HttpPeiniIp = LOCAL_IP;
//    public static String HttpsPeiniIp = REMOTE_HTTPS_IP_TAST;

    public static String HttpPeiniIp = HTTP;
    public static String HttpsPeiniIp = HTTPS;
//
//    public static String HttpPeiniIp = REMOTE_IP;
//    public static String HttpsPeiniIp = REMOTE_HTTPS_IP;

    /*-------------------------------------华丽的分割线---------------------------------------*/
    /**
     * 七牛云图片地址
     */
    public static String HttpPic = "http://okxm4c2gg.bkt.clouddn.com/";

    /**
     * 城市
     */
    public static String cityCode = "64";


    /**
     * 使用帮助
     */
    public static String HELPUEL = "g-index.html";
    public static String HELP = HttpPeiniIp + HELPUEL;

}
