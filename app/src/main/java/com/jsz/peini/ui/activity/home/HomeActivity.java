package com.jsz.peini.ui.activity.home;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.igexin.sdk.PushManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.JsonResponse;
import com.jsz.peini.model.eventbus.LoodingData;
import com.jsz.peini.model.eventbus.LoodingTabulationFragMentBean;
import com.jsz.peini.model.eventbus.UnreadHuanXinMsgCountBean;
import com.jsz.peini.model.filter.FilterRecycleviewBean;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.SquareBean;
import com.jsz.peini.model.square.UserInfoByOtherId;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.service.FloatViewService;
import com.jsz.peini.ui.Factory.FragmentFactory;
import com.jsz.peini.ui.activity.HelpActivity;
import com.jsz.peini.ui.activity.WebActivity;
import com.jsz.peini.ui.activity.filter.FilterActivity;
import com.jsz.peini.ui.activity.filter.TaskSearchActivity;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.news.ContactPhoneActivity;
import com.jsz.peini.ui.activity.search.IsSearchActivity;
import com.jsz.peini.ui.activity.square.MiSignActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.MyWealthActivity;
import com.jsz.peini.ui.activity.square.NickNameActivity;
import com.jsz.peini.ui.activity.square.SuareReleaseActivity;
import com.jsz.peini.ui.fragment.ManageFragment;
import com.jsz.peini.ui.fragment.MapFragment;
import com.jsz.peini.ui.fragment.SquareFragment;
import com.jsz.peini.ui.fragment.StoreFragment;
import com.jsz.peini.ui.view.NoScrollViewPager;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.CacheActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.KeyBoardUtils;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.deletedata.DeleteDataManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jsz.peini.R.id.title_bar_name_tabulation;

public class HomeActivity extends SlidingFragmentActivity implements View.OnClickListener, StoreFragment.OnFragmentInteractionListener {
    private static final int REQUEST_PERMISSION_CODE = 1021;

    private static final int FILTER = 1000; //筛选返回的数据
    /**
     * 地址搜索
     */
    private static final int TASK_SEARCH = 2000;

    private FrameLayout mHoemSquareRelease;
    public SlidingMenu slideMenu;
    private RelativeLayout home_squarefragment;
    private FragmentManager mFm;
    private RelativeLayout home_sellerfragment;
    private TextView title_bar_name;
    private FragmentTransaction mTransaction;
    private RelativeLayout home_homefragmen;
    private FrameLayout hoem_search_activity;
    private FrameLayout hoem_filter_activity;
    private TextView mTabulation;
    private RelativeLayout home_setting;
    private RelativeLayout home_fragment_ranking;
    private RelativeLayout home_fragment_manage;
    private ImageView mIvMorePoint;
    private Intent mIntent;
    public HomeActivity mActivity;

    public View mView;
    public TextView mName, mSignWord;
    public CircleImageView mMenu_imagehead;
    public ImageView mMenu_imageheadOpen;
    /**
     * mNoScrollViewPager 切换界面
     */
    public NoScrollViewPager mNoScrollViewPager;
    List<BaseFragment> mBaseFragments;
    private FrameLayout mTaskSearchActivity;
    private HomeViewPager mHomeViewPager;
    private RelativeLayout mMyWealth;
    private LinearLayout mLinearLayout;
    private boolean isExit = true;
    private boolean mRanking;
    private FragmentPagerAdapter adapter;
    private EditText mEditText;
    private RelativeLayout mRlHomeTitleBar;

    private InputMethodManager inputMethodManager;
    private ArrayList<FilterRecycleviewBean> mFilter = new ArrayList<>();
    private RelativeLayout home_help;
    private FragmentManager mSupportFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivity.finishActivity();
        setContentView(R.layout.activity_home);
        mActivity = this;
        ButterKnife.inject(this);
        //设置侧边栏宽度
        slideMenu = getSlidingMenu();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        slideMenu.setBehindOffset(width * 115 / 380);
        slideMenu.setFadeDegree(0.4f);
        slideMenu.setOffsetFadeDegree(0.4f);
        mView = UiUtils.inflate(mActivity, R.layout.layout_menu);
        setBehindContentView(mView);
        //请求相关权限
        requestPermission();
        //初始化控件
        initView();
        //初始化数据
        initData();
        //排行
        getIsRank();
        //签到界面
        getCouponInfoNotify();


    }

    private void requestPermission() {
        String[] permissionList = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissionList, REQUEST_PERMISSION_CODE);
            } else {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    isCacheFolderExists();
                }
                if (!canDrawOverlays()) {
                    showDrawOverlaysDialog();
                }
            }
        } else {
            isCacheFolderExists();
            if (!canDrawOverlays()) {
                showDrawOverlaysDialog();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            int grantedFlag = grantResults.length;
            for (int i = 0; i < grantResults.length; i++) {
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[i])
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    isCacheFolderExists();
                }
                boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (isTip) {//表明用户没有彻底禁止弹出权限请求
                        requestPermission();
                    } else {//表明用户已经彻底禁止弹出权限请求
                        dialog();
                    }
                    return;
                } else {
                    grantedFlag--;
                }
            }
            if (grantedFlag == 0) {
                if (!canDrawOverlays()) {
                    showDrawOverlaysDialog();
                }
                if (mBaseFragments != null && mBaseFragments.size() > 0) {
                    BaseFragment fragment = mBaseFragments.get(0);
                    if (fragment != null && fragment instanceof MapFragment) {
                        ((MapFragment) fragment).refreshLocation();
                    }
                }
            }
        }
    }

    private void dialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("请开启存储空间、所在位置、麦克风及摄像头权限后继续使用")
                .setCancelable(false)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create().show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestDrawOverLays() {
        if ("Meizu".equals(Build.BRAND)) {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
            intent.putExtra("packageName", mActivity.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + mActivity.getPackageName()));
            startActivity(intent);
        }
    }

    private boolean canDrawOverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(mActivity);
        } else {
            return checkCallingOrSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void showDrawOverlaysDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("新增桌面悬浮窗/开启应用发现新体验")
                .setCancelable(false)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestDrawOverLays();
                    }
                })
                .create().show();
    }

    /*加载View*/
    private void initView() {
        mBaseFragments = new ArrayList<>();
        mBaseFragments.add(FragmentFactory.setFragmentView(FragmentFactory.FRAGMENT_HOME));//首页
        mBaseFragments.add(FragmentFactory.setFragmentView(FragmentFactory.FRAGMEN_TABULATION));//列表
        mBaseFragments.add(FragmentFactory.setFragmentView(FragmentFactory.FRAGMENT_SQUARE));//广场
        mBaseFragments.add(FragmentFactory.setFragmentView(FragmentFactory.FRAGMENT_SELLER));//商家
        mBaseFragments.add(FragmentFactory.setFragmentView(FragmentFactory.FRAGMENT_RANKING));//排行榜
        mBaseFragments.add(FragmentFactory.setFragmentView(FragmentFactory.FRAGMENT_MANAGE));//店铺管理
        mBaseFragments.add(FragmentFactory.setFragmentView(FragmentFactory.FRAGMEN_SETTING));//设置
        /**侧边栏头像*/
        mMenu_imagehead = (CircleImageView) mView.findViewById(R.id.menu_imagehead);
//        mMenu_imagehead.setOnClickListener(this);
        //标题栏
        mRlHomeTitleBar = (RelativeLayout) findViewById(R.id.rl_home_title_bar);
        /*财富*/
        mMyWealth = (RelativeLayout) mView.findViewById(R.id.myWealth);
        mMyWealth.setOnClickListener(this);
        /**首页展示的头像*/
        mMenu_imageheadOpen = (ImageView) findViewById(R.id.menu_imagehead_open);
        mMenu_imageheadOpen.setOnClickListener(this);
        /**用户的名字*/
        mName = (TextView) mView.findViewById(R.id.menu_name);
        /**签名*/
        mSignWord = (TextView) mView.findViewById(R.id.signWord);
        mSignWord.setOnClickListener(this);
        /*点击头像*/
        mLinearLayout = (LinearLayout) mView.findViewById(R.id.icon_buju);
        mLinearLayout.setOnClickListener(this);
        //首页字体查找
        title_bar_name = (TextView) findViewById(R.id.title_bar_name);
        title_bar_name.setOnClickListener(this);
        /*先初始化首页*/
        //这个是要显示的列表
        mTabulation = (TextView) findViewById(R.id.title_bar_name_tabulation);
        mTabulation.setVisibility(View.VISIBLE);
        mTabulation.setOnClickListener(this);
        //搜索
        hoem_search_activity = (FrameLayout) findViewById(R.id.hoem_search_activity);
        hoem_search_activity.setOnClickListener(this);
        //筛选
        hoem_filter_activity = (FrameLayout) findViewById(R.id.hoem_filter_activity);
        hoem_filter_activity.setOnClickListener(this);
        hoem_filter_activity.setVisibility(View.VISIBLE);
        //广场
        home_squarefragment = (RelativeLayout) findViewById(R.id.home_squarefragment);
        home_squarefragment.setOnClickListener(this);
        //商家
        home_sellerfragment = (RelativeLayout) findViewById(R.id.home_sellerfragment);
        home_sellerfragment.setOnClickListener(this);
        ///首页
        home_homefragmen = (RelativeLayout) findViewById(R.id.home_homefragmen);
        home_homefragmen.setOnClickListener(this);
        //设置界面
        home_setting = (RelativeLayout) findViewById(R.id.home_setting);
        home_setting.setOnClickListener(this);
        //使用帮助
        home_help = (RelativeLayout) findViewById(R.id.home_help);
        home_help.setOnClickListener(this);

        //排行榜
        home_fragment_ranking = (RelativeLayout) findViewById(R.id.home_fragment_ranking);
        home_fragment_ranking.setOnClickListener(this);
        //店铺管理
        home_fragment_manage = (RelativeLayout) findViewById(R.id.home_fragment_manage);
        home_fragment_manage.setOnClickListener(this);
        //这个是发布按钮
        mHoemSquareRelease = (FrameLayout) findViewById(R.id.hoem_square_release);
        mHoemSquareRelease.setOnClickListener(this);

        //任务搜索按钮
        mTaskSearchActivity = (FrameLayout) findViewById(R.id.task_search_activity);
        mTaskSearchActivity.setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.et_home_edittext);

        mIvMorePoint = (ImageView) findViewById(R.id.iv_more_point);
        mIvMorePoint.setOnClickListener(this);

        /**Viewpager*/
        mNoScrollViewPager = (NoScrollViewPager) findViewById(R.id.home_vp);
        mSupportFragmentManager = mActivity.getSupportFragmentManager();
        mHomeViewPager = new HomeViewPager(mSupportFragmentManager, mBaseFragments);
        mNoScrollViewPager.setAdapter(mHomeViewPager);
////        mNoScrollViewPager.setOffscreenPageLimit(1);
//        int intExtra = getIntent().getIntExtra(Conversion.TYPE, 0);
//        mNoScrollViewPager.setCurrentItem(intExtra, false);
        //初始化文字大小
        title_bar_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        title_bar_name.setTextColor(getResources().getColor(R.color.text333));
        mTabulation.setTextColor(getResources().getColor(R.color.text999));
        mTabulation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        mNoScrollViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("测试代码", "onPageScrolled滑动中" + position);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.d("滑动到第几个了----" + position);
                switch (position) {
                    case 0:
                        //显示标题栏
                        mRlHomeTitleBar.setVisibility(View.VISIBLE);

                        title_bar_name.setClickable(true);
                        /*筛选*/
                        hoem_filter_activity.setVisibility(View.VISIBLE);
                        /*商家搜索*/
                        hoem_search_activity.setVisibility(View.GONE);
                        /*任务搜索*/
                        mTaskSearchActivity.setVisibility(View.VISIBLE);
                        /*广场发布*/
                        mHoemSquareRelease.setVisibility(View.GONE);
                         /*列表显示不显示*/
                        mTabulation.setVisibility(View.VISIBLE);

                        mTabulation.setText("列表");
                        title_bar_name.setText("首页");
                        title_bar_name.setVisibility(View.VISIBLE);//首页显示

                        //文字大小
                        title_bar_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        title_bar_name.setTextColor(getResources().getColor(R.color.text333));
                        mTabulation.setTextColor(getResources().getColor(R.color.text999));
                        mTabulation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        mIvMorePoint.setVisibility(View.GONE);
                        break;
                    case 1:
                        //显示标题栏
                        mRlHomeTitleBar.setVisibility(View.VISIBLE);

                        title_bar_name.setClickable(true);
                         /*筛选*/
                        hoem_filter_activity.setVisibility(View.VISIBLE);
                        /*商家搜索*/
                        hoem_search_activity.setVisibility(View.GONE);
                        /*任务搜索*/
                        mTaskSearchActivity.setVisibility(View.VISIBLE);
                        /*广场发布*/
                        mHoemSquareRelease.setVisibility(View.GONE);
                         /*列表显示不显示*/
                        mTabulation.setVisibility(View.VISIBLE);

                        mTabulation.setText("列表");
                        title_bar_name.setText("首页");
                        title_bar_name.setVisibility(View.VISIBLE);//首页显示

                        //文字大小
                        title_bar_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        title_bar_name.setTextColor(getResources().getColorStateList(R.color.text999));
                        mTabulation.setTextColor(getResources().getColorStateList(R.color.text333));
                        mTabulation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        mIvMorePoint.setVisibility(View.GONE);
                        break;
                    case 2:
                        //显示标题栏
                        mRlHomeTitleBar.setVisibility(View.VISIBLE);

                        title_bar_name.setClickable(false);
                         /*筛选*/
                        hoem_filter_activity.setVisibility(View.GONE);
                        /*商家搜索*/
                        hoem_search_activity.setVisibility(View.GONE);
                        /*任务搜索*/
                        mTaskSearchActivity.setVisibility(View.GONE);
                        /*广场发布*/
                        mHoemSquareRelease.setVisibility(View.VISIBLE);
                         /*列表显示不显示*/
                        mTabulation.setVisibility(View.GONE);

                        title_bar_name.setText("广场");
                        title_bar_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        title_bar_name.setTextColor(getResources().getColor(R.color.text333));
                        title_bar_name.setVisibility(View.VISIBLE);//首页显示
                        mIvMorePoint.setVisibility(View.GONE);
                        break;
                    case 3:
                        //显示标题栏
                        mRlHomeTitleBar.setVisibility(View.GONE);

                        title_bar_name.setClickable(false);
                           /*筛选*/
                        hoem_filter_activity.setVisibility(View.GONE);
                        /*商家搜索*/
                        hoem_search_activity.setVisibility(View.VISIBLE);
                         /*任务搜索*/
                        mTaskSearchActivity.setVisibility(View.GONE);
                        /*广场发布*/
                        mHoemSquareRelease.setVisibility(View.GONE);
                         /*列表显示不显示*/
                        mTabulation.setVisibility(View.GONE);

                        title_bar_name.setText("商家");
                        title_bar_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        title_bar_name.setTextColor(getResources().getColor(R.color.text333));
                        title_bar_name.setVisibility(View.VISIBLE);//首页显示
                        mIvMorePoint.setVisibility(View.GONE);
                        break;
                    case 4:
                        //显示标题栏
                        mRlHomeTitleBar.setVisibility(View.VISIBLE);

                        title_bar_name.setClickable(false);
                           /*筛选*/
                        hoem_filter_activity.setVisibility(View.GONE);
                        /*商家搜索*/
                        hoem_search_activity.setVisibility(View.GONE);
                         /*任务搜索*/
                        mTaskSearchActivity.setVisibility(View.GONE);
                        /*广场发布*/
                        mHoemSquareRelease.setVisibility(View.GONE);
                         /*列表显示不显示*/
                        mTabulation.setVisibility(View.GONE);

                        title_bar_name.setText("排行榜");
                        title_bar_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        title_bar_name.setTextColor(getResources().getColor(R.color.text333));
                        title_bar_name.setVisibility(View.VISIBLE);
                        mIvMorePoint.setVisibility(View.GONE);
                        break;
                    case 5:
                        //显示标题栏
                        mRlHomeTitleBar.setVisibility(View.VISIBLE);

                        title_bar_name.setClickable(false);
                           /*筛选*/
                        hoem_filter_activity.setVisibility(View.GONE);
                        /*商家搜索*/
                        hoem_search_activity.setVisibility(View.GONE);
                         /*任务搜索*/
                        mTaskSearchActivity.setVisibility(View.GONE);
                        /*广场发布*/
                        mHoemSquareRelease.setVisibility(View.GONE);
                         /*列表显示不显示*/
                        mTabulation.setVisibility(View.GONE);

                        title_bar_name.setText("店铺管理");
                        title_bar_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        title_bar_name.setTextColor(getResources().getColor(R.color.text333));
                        title_bar_name.setVisibility(View.VISIBLE);

                        if (mBaseFragments != null && mBaseFragments.size() > 5) {
                            ManageFragment storeManageFragment = (ManageFragment) mBaseFragments.get(5);
                            if (storeManageFragment != null) {
                                mIvMorePoint.setVisibility(storeManageFragment.isShouldShowMorePoint() ? View.VISIBLE : View.GONE);
                            }
                        }
                        break;
                    case 6:
                        //显示标题栏
                        mRlHomeTitleBar.setVisibility(View.VISIBLE);

                        title_bar_name.setClickable(false);
                           /*筛选*/
                        hoem_filter_activity.setVisibility(View.GONE);
                        /*商家搜索*/
                        hoem_search_activity.setVisibility(View.GONE);
                         /*任务搜索*/
                        mTaskSearchActivity.setVisibility(View.GONE);
                        /*广场发布*/
                        mHoemSquareRelease.setVisibility(View.GONE);
                        /*列表显示不显示*/
                        mTabulation.setVisibility(View.GONE);

                        title_bar_name.setText("设置");
                        title_bar_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        title_bar_name.setTextColor(getResources().getColor(R.color.text333));
                        title_bar_name.setVisibility(View.VISIBLE);
                        mIvMorePoint.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //正在滑动   pager处于正在拖拽中
                    Log.d("测试代码", "onPageScrollStateChanged=======正在滑动" + "SCROLL_STATE_DRAGGING");
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                    Log.d("测试代码", "onPageScrollStateChanged=======自动沉降" + "SCROLL_STATE_SETTLING");
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //空闲状态  pager处于空闲状态
                    Log.d("测试代码", "onPageScrollStateChanged=======空闲状态" + "SCROLL_STATE_IDLE");
                }
            }
        });
        mRlHomeTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoScrollViewPager == null
                        || (mNoScrollViewPager.getCurrentItem() != 2 && mNoScrollViewPager.getCurrentItem() != 3)
                        || mBaseFragments.size() <= 3) {
                    return;
                }
                RecyclerView swipeTarget = null;
                if (mNoScrollViewPager.getCurrentItem() == 2) {
                    swipeTarget = ((SquareFragment) mBaseFragments.get(2)).getSwipeTarget();
                } else if (mNoScrollViewPager.getCurrentItem() == 3) {
                    swipeTarget = ((StoreFragment) mBaseFragments.get(3)).getSwipeTarget();
                }
                if (swipeTarget != null) {
                    if (new Date().getTime() - lastPressTitleTime < 1000) {
                        final RecyclerView finalSwipeTarget = swipeTarget;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (lastPressTitleTime != 0) {
                                    finalSwipeTarget.scrollToPosition(0);
                                    lastPressTitleTime = 0;
                                }
                            }
                        }, 750);
                        swipeTarget.smoothScrollToPosition(0);
                    } else {
                        lastPressTitleTime = new Date().getTime();//重置lastPressTitleTime
                    }
                }
            }
        });
    }

    long lastPressTitleTime = 0;

    //发送广播传输的值
    int page = 0;

    /*加载数据*/
    private void initData() {
        if (canDrawOverlays() && StringUtils.isNoNull((String) SpUtils.get(mActivity, Conversion.CANCELPHONENUMBER, ""))
                && StringUtils.isNoNull((String) SpUtils.get(mActivity, Conversion.CANCELTASKNUMBER, ""))) {
            boolean serviceWork = isServiceWork(mActivity, "com.jsz.peini.service.FloatViewService");
            LogUtil.d("悬浮窗的服务是否开启了啊-----" + serviceWork);
            if (!serviceWork) {
                startService(new Intent(mActivity, FloatViewService.class));
            }
        }
        slideMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                EventBus.getDefault().post(new LoodingData(true, page));
                page = 0;
                LogUtil.d("侧边栏关闭了---开始加载数据吧");
            }
        });
        slideMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                LogUtil.d("侧边栏打开了");
                if (mNoScrollViewPager != null
                        && (mNoScrollViewPager.getCurrentItem() == 2 || mNoScrollViewPager.getCurrentItem() == 5)) {
                    hideSoftKeyboard();
                }
            }
        });

        String phone = SpUtils.getPhone(mActivity);
        boolean b = PushManager.getInstance().bindAlias(mActivity.getApplicationContext(), phone);
        if (b) {
            LogUtil.d("绑定别名成功");
        } else {
            LogUtil.d("绑定别名失败");
            PushManager.getInstance().bindAlias(mActivity.getApplicationContext(), phone);
        }
    }

    /**
     * 判断服务是否开启
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //首页的加载
            case R.id.title_bar_name:
                mNoScrollViewPager.setCurrentItem(0, false);
                break;
            //列表界面
            case title_bar_name_tabulation:
                EventBus.getDefault().post(new LoodingTabulationFragMentBean(true));
                mNoScrollViewPager.setCurrentItem(1, false);
                break;
            //广场
            case R.id.home_squarefragment:
                page = 2;
                slideMenu.toggle();
                mNoScrollViewPager.setCurrentItem(2, false);
                break;
            //商家
            case R.id.home_sellerfragment:
                page = 3;
                slideMenu.toggle();
                mNoScrollViewPager.setCurrentItem(3, false);
                break;
            //侧拉的首页
            case R.id.home_homefragmen:
                page = 0;
                slideMenu.toggle();
                mNoScrollViewPager.setCurrentItem(0, false);
                break;
            //排行榜界面
            case R.id.home_fragment_ranking:
                if (SpUtils.isCompleteUserInfo(mActivity)) {
                    page = 4;
                    mRanking = (boolean) SpUtils.get(mActivity, Conversion.RANKING, true);
                    showmRanking();
                } else {
                    CompleteUserInfoActivity.actionShow(mActivity, SpUtils.getUserToken(mActivity));
                }
                break;
            //店铺管理界面
            case R.id.home_fragment_manage:
                slideMenu.toggle();
                mNoScrollViewPager.setCurrentItem(5, false);
                break;
            //设置界面
            case R.id.home_setting:
                slideMenu.toggle();
                mNoScrollViewPager.setCurrentItem(6, false);
                break;
            //使用帮助
            case R.id.home_help:
                slideMenu.toggle();
                startActivity(new Intent(mActivity, HelpActivity.class));
                break;
            case R.id.icon_buju://跳转我的空间
                MiSquareActivity.actionShow(mActivity);
                break;
            case R.id.menu_imagehead_open:
                slideMenu.toggle();
                break;
            //搜索列表
            case R.id.hoem_search_activity:
                mIntent = new Intent(this, IsSearchActivity.class);
                startActivity(mIntent);
                break;
            //筛选列表
            case R.id.hoem_filter_activity:
                mIntent = new Intent(this, FilterActivity.class);
                if (null != mFilter && mFilter.size() > 0) {
                    mIntent.putExtra("filter", mFilter);
                }
                startActivityForResult(mIntent, FILTER);
                break;
            //广场发布按钮
            case R.id.hoem_square_release:
                if (SpUtils.isCompleteUserInfo(mActivity)) {
                    mIntent = new Intent(this, SuareReleaseActivity.class);
                    startActivityForResult(mIntent, 3000);
                } else {
                    CompleteUserInfoActivity.actionShow(mActivity, SpUtils.getUserToken(mActivity));
                }
                break;
            case R.id.task_search_activity:
                mIntent = new Intent(this, TaskSearchActivity.class);
                startActivityForResult(mIntent, TASK_SEARCH);
                break;
            case R.id.myWealth:
//                mIntent = new Intent(this, MiWealthActivity.class);
                mIntent = new Intent(this, MyWealthActivity.class);
                startActivityForResult(mIntent, TASK_SEARCH);
                break;
            case R.id.signWord:
//                mIntent = new Intent(this, NickNameActivity.class);
//                mIntent.putExtra("type", "1");
//                mIntent.putExtra("title", "修改签名");
//                startActivity(mIntent);
                NickNameActivity.actionShow(mActivity, 1, mSignWord.getText().toString().trim());
                break;
            //店铺管理界面右上角的更多按钮
            case R.id.iv_more_point:
                popupWindowsForStoreManage();
                break;
        }
    }

    /**
     * 是否开启排行榜
     */
    private void showmRanking() {
        if (mRanking) {
            slideMenu.toggle();
            mNoScrollViewPager.setCurrentItem(4, false);
        } else {
            new AlertDialog.Builder(mActivity)
                    .setCancelable(false)
                    .setTitle("排行榜")
                    .setMessage("是否前往[设置]开启排行榜功能")
                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mNoScrollViewPager.setCurrentItem(6, false);
                            slideMenu.toggle();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    String nickname = "";
    String imageHead = "";

    private void initShowView(UserInfoByOtherId userAllInfo) {
        UserInfoByOtherId.UserInfoBean userInfo = userAllInfo.getUserInfo();
        String userInfoNickname = userInfo.getNickname();
        String userInfoimageHead = userInfo.getImageHead();
        SpUtils.setImageHead(mActivity, userInfoimageHead);
        if (!TextUtils.isEmpty(userInfoimageHead) && !userInfoimageHead.equals(imageHead)) {
            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + userInfoimageHead, mMenu_imagehead, SpUtils.getSex(mActivity));
            imageHead = userInfoimageHead;
        }
        if (!TextUtils.isEmpty(userInfoNickname) && !userInfoNickname.equals(nickname)) {
            mName.setText(userInfoNickname);
            nickname = userInfoNickname;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
            onBackPressed();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

//    long lastPressTime = 0;

    public void onBackPressed() {
//        if (new Date().getTime() - lastPressTime < 2000) {
        //按返回键返回桌面
        deleteDataImage();
        //方式一：将此任务转向后台
        moveTaskToBack(false);

        //方式二：返回手机的主屏幕
    /*Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addCategory(Intent.CATEGORY_HOME);
    startActivity(intent);*/
//        } else {
//            lastPressTime = new Date().getTime();//重置lastPressTime
//            Toasty.normal(mActivity, "再按一次退出程序").show();
//        }
    }

    private void deleteDataImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("删除压缩后的图片===" + DeleteDataManager.deleteDirectory(Conversion.LOCAL_IMAGE_CACHE_PATH));
            }
        }).start();
    }

    private boolean isCacheFolderExists() {
        File file = new File(Conversion.LOCAL_IMAGE_CACHE_PATH);
        File noMediaFile = new File(Conversion.LOCAL_IMAGE_CACHE_PATH + ".nomedia");
        if (!file.exists()) {
            if (file.mkdir()) {
                if (!noMediaFile.exists()) {
                    try {
                        return noMediaFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onFragmentInteraction() {
        if (slideMenu != null) {
            slideMenu.toggle();
        }
    }

    /**
     * 数据适配器
     */
    private class HomeViewPager extends FragmentPagerAdapter {
        private FragmentManager mFm;
        private List<BaseFragment> mBaseFragments;

        private HomeViewPager(FragmentManager fm, List<BaseFragment> baseFragments) {
            super(fm);
            mFm = fm;
            mBaseFragments = baseFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mBaseFragments.get(position);
        }

        @Override
        public int getCount() {
            return Conversion.getSize(mBaseFragments);
        }

        @Override
        public Fragment instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container,
                    position);
            mFm.beginTransaction().show(fragment).commit();
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            Fragment fragment = mBaseFragments.get(position);
            mFm.beginTransaction().hide(fragment).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Glide.with(mActivity).pauseRequests();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    boolean imFirst = true;

    @Override
    protected void onStart() {
        super.onStart();
        loginHuanxin();

    }

    @Override
    protected void onResume() {
        super.onResume();
        int intExtra = getIntent().getIntExtra(Conversion.TYPE, 0);
        if (intExtra != 0 && null != mNoScrollViewPager && mBaseFragments != null && mBaseFragments.size() != 0) {
            switch (intExtra) {
                case 2: //跳转广场
                    mNoScrollViewPager.setCurrentItem(2, false);
                    EventBus.getDefault().post(new LoodingData(true, 2));
                    break;
                default:
                    mNoScrollViewPager.setCurrentItem(0, false);
                    break;
            }
            getIntent().removeExtra(Conversion.TYPE);
        }
        inItgetuserInfo();
        KeyBoardUtils.hideKeyBoard(mActivity, new View(mActivity));
    }

    //实现ConnectionListener接口
    private void loginHuanxin() {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        try {
            EMClient.getInstance().createAccount(SpUtils.getPhone(mActivity), "123456");//同步方法
        } catch (HyphenateException e) {
            e.printStackTrace();
            LogUtil.d("注册聊天服务器失败: " + e.getMessage());
        }
        try {
            EMClient.getInstance().login(SpUtils.getPhone(mActivity), "123456", getEmCallBack());
        } catch (Exception e) {
            DeleteDataManager.cleanApplicationData(mActivity, String.valueOf(getFilesDir().getPath()));
            finish();
        }
    }

    @NonNull
    private EMCallBack getEmCallBack() {
        return new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                LogUtil.d("登录聊天服务器成功！");
                imFirst = false;
            }

            @Override
            public void onProgress(int progress, String status) {
                LogUtil.d("正在登录聊天服务器！");
            }

            @Override
            public void onError(int code, String message) {
                LogUtil.d("登录聊天服务器失败！code" + code + "message" + message);
            }
        };
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            Collection<EMConversation> conversationValues = conversations.values();
            if (conversationValues.size() > 0) {
                int unreadCountSum = 0;
                for (EMConversation emConversation : conversationValues) {
                    if (emConversation != null) {
                        unreadCountSum += emConversation.getUnreadMsgCount();
                    }
                }
                EventBus.getDefault().post(new UnreadHuanXinMsgCountBean(unreadCountSum, messages.toString()));
            }

            //收到消息
            LogUtil.d("收到消息---" + messages.toString());
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            LogUtil.d("收到透传消息---" + messages.toString());
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            LogUtil.d("收到消息的回执---" + messages.toString());
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
            //收到已送达回执
            LogUtil.d("收到已送达回执---" + messages.toString());
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
//                        ToastUtils.showToast(mActivity, "显示帐号已经被移除");
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
//                        ToastUtils.showToast(mActivity, "显示帐号在其他设备登录");
                        // 显示帐号在其他设备登录
                        if (NetUtils.hasNetwork(mActivity)) {
//                            EMClient.getInstance().login(SpUtils.getPhone(mActivity), "123456", getEmCallBack());
                            //连接不到聊天服务器
                        }
                    } else {
                        if (NetUtils.hasNetwork(mActivity)) {
//                            EMClient.getInstance().login(SpUtils.getPhone(mActivity), "123456", getEmCallBack());
                            //连接不到聊天服务器
                        } else {
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }

    }

    //    获取我的个人信息
    private void inItgetuserInfo() {
        String url;
        String imageHead = SpUtils.getImageHead(mActivity);
        if (!TextUtils.isEmpty(imageHead)) {
            if (imageHead.contains("PEINI_CACHE")) {
                url = imageHead;
            } else {
                url = IpConfig.HttpPic + imageHead;
            }
            Glide.with(mActivity.getApplicationContext())
                    .load(url)
                    .error("1".equals(SpUtils.getSex(mActivity)) ? R.mipmap.ic_nan : R.mipmap.ic_nv)
                    .into(mMenu_imagehead);
        }
        getImageHend();
    }

    //请求下头像
    private void getImageHend() {
        RetrofitUtil.createService(SquareService.class)
                .getNicknameAndImgHead(SpUtils.getUserToken(mActivity))
                .enqueue(new Callback<UserInfoByOtherId>() {
                    @Override
                    public void onResponse(Call<UserInfoByOtherId> call, final Response<UserInfoByOtherId> response) {
                        if (response.isSuccessful()) {
                            UserInfoByOtherId userAllInfo = response.body();
                            if (userAllInfo.getResultCode() == 1) {
                                initShowView(userAllInfo);
                            } else if (userAllInfo.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (userAllInfo.getResultCode() == 0) {
                                Toasty.normal(mActivity, userAllInfo.getResultDesc()).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserInfoByOtherId> call, Throwable t) {
                    }
                });
    }

    private void getIsRank() {
        RetrofitUtil.createService(SquareService.class)
                .getSwitchRank(SpUtils.getUserToken(mActivity))
                .enqueue(new RetrofitCallback<JsonResponse>() {
                    @Override
                    public void onSuccess(Call<JsonResponse> call, Response<JsonResponse> response) {
                        if (response.isSuccessful()) {
                            JsonResponse body = response.body();
                            if (body.getResultCode() == 1) {
                                String isRank = body.getIsRank();
                                if (StringUtils.isNoNull(isRank) && "1".equals(isRank)) {
                                    SpUtils.put(mActivity, Conversion.RANKING, true);
                                } else {
                                    SpUtils.put(mActivity, Conversion.RANKING, false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {

                    }
                });
    }

    private void getCouponInfoNotify() {
        RetrofitUtil.createService(SquareService.class)
                .getCouponInfoNotify(SpUtils.getUserToken(mActivity))
                .enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, final Response<JsonResponse> response) {
                        if (response.isSuccessful()) {
                            JsonResponse body = response.body();
                            if (body.getResultCode() == 1) {
                                boolean guidedFlag = SpUtils.isInstalledRightNow(mActivity);
                                if (!guidedFlag && !body.isSign()) {
                                    mIntent = new Intent(mActivity, MiSignActivity.class);
                                    startActivity(mIntent);
                                }
                            }
                            SpUtils.setInstalledAndOpened(mActivity);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {
                        SpUtils.setInstalledAndOpened(mActivity);
                    }
                });
    }

    public void setMorePointIsShow(boolean isShow) {
        if (isShow) {
            mIvMorePoint.setVisibility(View.VISIBLE);
        } else {
            mIvMorePoint.setVisibility(View.GONE);
        }
    }

    private void popupWindowsForStoreManage() {
        final Popwindou popWindow = new Popwindou(mActivity, getWindow().getDecorView().getRootView());
        View view = UiUtils.inflate(mActivity, R.layout.pop_store_manage);
        popWindow.init(view, Gravity.BOTTOM, true);
        Button btnPayList = (Button) view.findViewById(R.id.btn_pay_list);
        Button btnSmsList = (Button) view.findViewById(R.id.btn_sms_list);
        Button btnExit = (Button) view.findViewById(R.id.btn_exit);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnPayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sellerId = (String) SpUtils.get(mActivity, Conversion.STORE_MANAGE_ID, "");
                WebActivity.actionShow(mActivity, 0, sellerId);
                popWindow.dismiss();
            }
        });
        btnSmsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sellerId = (String) SpUtils.get(mActivity, Conversion.STORE_MANAGE_ID, "");
                WebActivity.actionShow(mActivity, 1, sellerId);
                popWindow.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBaseFragments != null && mBaseFragments.size() > 5) {
                    ManageFragment storeManageFragment = (ManageFragment) mBaseFragments.get(5);
                    if (storeManageFragment != null) {
                        storeManageFragment.storeExit();
                    }
                }
                popWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });
    }

    void hideSoftKeyboard() {
        if (getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 || requestCode == 200 || requestCode == 300) {
            mBaseFragments.get(0).onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == 400 || requestCode == 500 || requestCode == 600) {
            mBaseFragments.get(1).onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == 3000 && data != null) {
            mBaseFragments.get(2).onActivityResult(requestCode, resultCode, data);
        } else if (null != data && requestCode == FILTER) {
            ArrayList<FilterRecycleviewBean> filter = data.getParcelableArrayListExtra("filter");
            if (null != filter && filter.size() > 0) {
                mFilter.clear();
                mFilter.addAll(filter);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

}

