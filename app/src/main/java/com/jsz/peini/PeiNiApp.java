package com.jsz.peini;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.igexin.sdk.PushManager;
import com.jsz.peini.base.CrashHandler;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.gen.DaoMaster;
import com.jsz.peini.gen.DaoSession;
import com.jsz.peini.gen.HistoryHotBeanDao;
import com.jsz.peini.gen.SellerListHotBeanDao;
import com.jsz.peini.san.getui.IntentService;
import com.jsz.peini.san.getui.PushService;
import com.jsz.peini.ui.activity.LockActivity;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.PeiNiUtils;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.time.TimeUtils;
import com.mcxiaoke.packer.helper.PackerNg;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.UUID;

/**
 * Created by jinshouzhi on 2016/11/25.
 */

public class PeiNiApp extends MultiDexApplication {
    private int count = 0;
    private static boolean isGestureLocking = false;
    public static Context context = null;
    private static String mChannelNo = null;

    /**
     * 获取渠道号
     * eg. official_version_1010 return 1010
     * eg. baidu_1020 return 1020
     *
     * @return 渠道号
     */
    public static String getChannelNo() {
        return mChannelNo;
    }

    private void setChannelNo() {
        String channelNo = PackerNg.getMarket(context, "official_version_1010");
        if (!TextUtils.isEmpty(channelNo) && channelNo.length() > 4) {
            String temp = channelNo.substring(channelNo.length() - 4, channelNo.length());
            if (temp.matches("\\d{4}")) {
                mChannelNo = temp;
            }
        }
        if (TextUtils.isEmpty(mChannelNo)) {
            mChannelNo = "1010";
        }
    }

    public static void setIsGestureLocking(boolean isGestureLocking) {
        PeiNiApp.isGestureLocking = isGestureLocking;
    }

    /**
     * 数据库
     */
    public static HistoryHotBeanDao HistoryHotBeanDao;
    public static SellerListHotBeanDao sSellerListHotBeanDao;
    /**
     * 程序锁
     */
    public String mLock;
    /**
     * 微信appAY
     */
    public static String WXAPIPAY = "wx4a856dc3b666fe4d";

    static {
        Config.DEBUG = true;
        PlatformConfig.setWeixin("wx4a856dc3b666fe4d", "f220a197606f1189f30eb204daf48fba");
        PlatformConfig.setQQZone("1105797941", "wOUQgJx4vNW3Tivj");
        PlatformConfig.setSinaWeibo("3759549608", "e1699a7ad6d8088c6d89f5e4fa54a9f7", "http://sns.whalecloud.com/sina2/callback");
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //崩溃处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        Stetho.initializeWithDefaults(this);
        context = getContext();
//        /**全局Activity*/
        isrRegisterActivityLifecycleCallbacks();
        /**环信*/
        initHuanXin();
        /**友盟*/
        getUMShareAPI();
        /**个推*/
        initGetUi();
        /**数据库*/
        initDao();

        Log.i("UniquePsuedoID", getUniquePsuedoID());
        setChannelNo();
        Log.i("channelNo", getChannelNo());
    }

    public Context getContext() {
        return getApplicationContext();
    }

    /**
     * 全局Activity 的监听
     */
    private void isrRegisterActivityLifecycleCallbacks() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStopped(Activity activity) {
                if (activity instanceof NonGestureLockInterface) {
                    if (!((NonGestureLockInterface) activity).isGestureLock()) {
                        return;
                    }
                }
                count--;
                if (count == 0) {
                    LogUtil.d("**********切到后台**********");
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (activity instanceof NonGestureLockInterface) {
                    if (!((NonGestureLockInterface) activity).isGestureLock()) {
                        return;
                    }
                }
                if (count == 0) {
                    LogUtil.d("**********切到前台**********");
                    mLock = (String) SpUtils.get(getContext(), "lock", "");
                    if (StringUtils.isNoNull(mLock) && !isGestureLocking) {
                        Intent intent = new Intent(getContext(), LockActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                    }
                }
                count++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
        });
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }


    /**
     * 初始化数据库
     */
    private void initDao() {
        LogUtil.d("陪你App 初始化的时间----------" + TimeUtils.getCurrentTime());
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "peini_db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryHotBeanDao = daoSession.getHistoryHotBeanDao();
        sSellerListHotBeanDao = daoSession.getSellerListHotBeanDao();
        LogUtil.d("陪你App 初始化结束的时间的时间----------" + TimeUtils.getCurrentTime());
    }

    /**
     * 初始化友盟
     */
    private UMShareAPI getUMShareAPI() {
        return UMShareAPI.get(context);
    }

    /**
     * 初始化个推
     */
    private void initGetUi() {
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(context, PushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(context, IntentService.class);
        //开启推送
        PushManager.getInstance().turnOnPush(getApplicationContext());
    }

    /**
     * 初始化环信
     */
    private void initHuanXin() {
        EMOptions options = new EMOptions();
        options.setAutoLogin(true);
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = PeiNiUtils.getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(context, options);
        //easeui初始化
        EaseUI.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial;

        String m_szDevIDShort = "35" +          //35开头 看起来像是合法的id
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
