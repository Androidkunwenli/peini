package com.jsz.peini.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jsz.peini.R;
import com.jsz.peini.ui.activity.TaskMessageActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;

import java.lang.reflect.Field;

public class FloatViewService extends Service {
    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;
    Button mFloatView;
    //触摸监听器
    private GestureDetector mGestureDetector;
    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private int mTouchStartX, mTouchStartY, mTouchCurrentX, mTouchCurrentY;
    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private int mStartX, mStartY, mStopX, mStopY;
    private boolean isMove;//判断悬浮窗是否移动

    private static final String TAG = "FloatViewService";
    public int mScreenWidth;
    public int mScreenHeight;
    public LinearLayout.LayoutParams mLayoutParams;
    public int mIntWidth;
    public int mIntHeight;
    private FloatViewService mService;
    private Intent mIntent;
    public String mTaskid = "";
    public String mTitle = "";
    public String mMtype = "";
    public String mPhone;
    private boolean isshow;

    @Override
    public void onCreate() {
        super.onCreate();
        mService = this;
        LogUtil.d("启动了悬浮窗按钮");
        createFloatView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTaskid = intent.getStringExtra(Conversion.TASKID);
        mPhone = intent.getStringExtra(Conversion.PHONE);
        isshow = intent.getBooleanExtra("isshow", true);
        if (isshow) {
            if (StringUtils.isNoNull(mPhone) && StringUtils.isNoNull(mTaskid)) {
                mIntent = new Intent(mService, TaskMessageActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                mIntent.putExtra(Conversion.TASKID, mTaskid);
                mIntent.putExtra(Conversion.PHONE, mPhone);
                startActivity(mIntent);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void createFloatView() {
        initWindow();
        initFloating();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

    /**
     * 初始化windowManager
     */
    private void initWindow() {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = getWindowManager(getApplicationContext());
        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
        //wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
//        wmParams.type = LayoutParams.TYPE_TOAST;//无需权限
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//需要权限，且在某些系统中还需要手动打开设置，比如miui
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        WindowManager windowManager = getWindowManager(getApplicationContext());
        mScreenWidth = windowManager.getDefaultDisplay().getWidth();
        mScreenHeight = windowManager.getDefaultDisplay().getHeight();
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = mScreenWidth;
        wmParams.y = mScreenHeight / 5;
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        mLayoutParams = (LinearLayout.LayoutParams) mFloatLayout.getLayoutParams();
        //设置悬浮窗口长宽数据
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mFloatView = (Button) mFloatLayout.findViewById(R.id.float_id);
        mFloatLayout.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mIntWidth = mFloatView.getMeasuredWidth() / 2;
        Log.i(TAG, "Width/2--->" + mIntWidth);
        mIntHeight = mFloatView.getMeasuredHeight() / 2;
        Log.i(TAG, "Height/2--->" + mIntHeight);
    }

    /**
     * 找到悬浮窗的图标，并且设置事件
     * 设置悬浮窗的点击、滑动事件
     */
    private void initFloating() {
        mGestureDetector = new GestureDetector(this, new MyOnGestureListener());
        //设置监听器
        mFloatView.setOnTouchListener(new FloatingListener());
        mFloatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FloatViewService.this, "onClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 悬浮窗监听器
     */
    private class FloatingListener implements OnTouchListener {
        @Override
        public boolean onTouch(View arg0, MotionEvent event) {

            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    mStartX = (int) event.getRawX();
                    mStartY = (int) event.getRawY();
                    Log.i(TAG, "onTouch: " + "mTouchStartX" + (mTouchStartX));
                    Log.i(TAG, "onTouch: " + "mTouchStartY" + (mTouchStartY));
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchCurrentX = (int) event.getRawX();
                    mTouchCurrentY = (int) event.getRawY();
                    wmParams.x += mTouchCurrentX - mTouchStartX;
                    wmParams.y += mTouchCurrentY - mTouchStartY;
                    Log.i(TAG, "onTouch: " + "mTouchCurrentX - mTouchStartX" + (mTouchCurrentX - mTouchStartX));
                    Log.i(TAG, "onTouch: " + "mTouchCurrentY - mTouchStartY" + (mTouchCurrentY - mTouchStartY));
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                    mTouchStartX = mTouchCurrentX;
                    mTouchStartY = mTouchCurrentY;
                    break;
                case MotionEvent.ACTION_UP:
                    mStopX = (int) event.getRawX();
                    mStopY = (int) event.getRawY();
                    if (Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1) {
                        isMove = true;
                    }
                    if (mStopX > mScreenWidth / 2 - mIntWidth) {
                        wmParams.x = mScreenWidth;
                        wmParams.y = mStopY - mIntHeight * 2 + statusBarHeight;
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                    } else {
                        wmParams.x = 0;
                        wmParams.y = mStopY - mIntHeight * 2 + statusBarHeight;
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                    }
                    Log.i(TAG, "onTouch: " + mStopX + "---" + mStopY);
                    break;
            }
            return mGestureDetector.onTouchEvent(event);  //此处必须返回false，否则OnClickListener获取不到监听
        }
    }

    /**
     * @tips :自己定义的手势监听类
     */
    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!isMove) {
//                Toast.makeText(getApplicationContext(), "你点击了悬浮窗", Toast.LENGTH_SHORT).show();
                mIntent = new Intent(mService, TaskMessageActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                mIntent.putExtra(Conversion.PHONE, (String) SpUtils.get(mService, Conversion.CANCELPHONENUMBER, ""));
                mIntent.putExtra(Conversion.TASKID, (String) SpUtils.get(mService, Conversion.CANCELTASKNUMBER, ""));
                startActivity(mIntent);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}