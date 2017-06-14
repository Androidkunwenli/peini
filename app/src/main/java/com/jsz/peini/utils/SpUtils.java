package com.jsz.peini.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jsz.peini.PeiNiApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static com.jsz.peini.utils.Conversion.SHARED_PREFERENCES_FIRST_INSTALL_FLAG;

/**
 * Created by jinshouzhi on 2016/11/25.
 */

public class SpUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "mpeini_data";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 保存serverB
     */
    public static void putServerB(Context context, String serverB) {

        String complexB = AesUtils.aesDecrypt(serverB, Conversion.aesPass);
        int serverBInt = Conversion.getServerBFromString(complexB);

        put(context, "serverB", serverBInt);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    /**
     * 经度   X
     *
     * @param context
     * @return
     */
    public static String getXpoint(Context context) {
        return (String) get(context, "xpoint", "38.048684");
    }

    /**
     * 纬度    Y
     *
     * @param context
     * @return
     */
    public static String getYpoint(Context context) {
        return (String) get(context, "ypoint", "114.520828");
    }

    /**
     * 纬度    用户信息
     *
     * @param context
     * @return
     */
    public static String getUserToken(Context context) {
        return (String) get(context, "mUserToken", "");
    }

    /**
     * 是否读过支付处理
     *
     * @param context
     * @return
     */
    public static boolean getFirstPay(Context context) {
        return (boolean) get(context, "FirstPay", false);
    }

    /**
     * 获取性别
     */
    public static String getSex(Context context) {
        return (String) get(context, "sex", "1");
    }

    /**
     * 是否有支付密码
     *
     * @param context
     * @return
     */
    public static boolean getPayPassWord(Context context) {
        return (boolean) get(context, "PayPassWord", false);
    }

    /**
     * 用户手机号
     */
    public static String getPhone(Context context) {
        return (String) get(context, "phone", "");
    }

    /*用户姓名*/
    public static String getNickname(Context context) {
        return (String) get(context, "nickname", "");
    }

    /*用户姓名*/
    public static void setNickname(Context context, String nickname) {
        put(context, "nickname", nickname);
    }

    /**
     * SpUtils.put(mActivity, "nickname", response.getUserInfo().getNickname() + "");
     * SpUtils.put(mActivity, "imageHead", response.getUserInfo().getImageHead() + "");
     */
    /*用户头像*/
    public static String getImageHead(Context context) {
        return (String) get(context, "imageHead", "");
    }

    /*保存用户头像*/
    public static void setImageHead(Context context, String imageHend) {
        put(context, "imageHead", imageHend);
    }

    public static boolean isInstalledRightNow(Context context) {
        int lastVersion = (int) get(context, SHARED_PREFERENCES_FIRST_INSTALL_FLAG, 0);
        int currentVersion = PeiNiUtils.getVersionCode(context);
        return currentVersion != lastVersion;
    }

    public static void setInstalledAndOpened(Context context) {
        if (context == null) {
            context = PeiNiApp.context;
        }
        int versionCode = PeiNiUtils.getVersionCode(context);
        put(context, SHARED_PREFERENCES_FIRST_INSTALL_FLAG, versionCode);
    }

    /**
     * 是否完善用户信息
     *
     * @param context Context
     * @return true:已完善， false:未完善
     */
    public static boolean isCompleteUserInfo(Context context) {
        String addUserInfo = (String) get(context, "addUserInfo", "");
        return "1".equals(addUserInfo);
    }
}
