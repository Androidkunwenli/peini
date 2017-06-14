package com.jsz.peini.ui.activity.seller;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.eventbus.SelectorSellerBean;
import com.jsz.peini.model.seller.SellerMessageInfoBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.seller.SellerService;
import com.jsz.peini.ui.activity.pay.PaythebillActivity;
import com.jsz.peini.ui.activity.report.ReportActivity;
import com.jsz.peini.ui.activity.task.SelectSellerActivity;
import com.jsz.peini.ui.adapter.seller.AdvertiseListStringAdapter;
import com.jsz.peini.ui.fragment.seller.SellerPaySuccessFragment;
import com.jsz.peini.utils.CacheActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.ShareUtils;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.UrlUtils;
import com.jsz.peini.widget.ProgressActivity;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellerMessageActivity extends BaseActivity {
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;
    @InjectView(R.id.sv_show_seller)
    ScrollView mSvShowSeller;
    @InjectView(R.id.seller_message_toolbar)
    LinearLayout mSellerMessageToolbar;
    @InjectView(R.id.ll_share)
    LinearLayout mLlShare;
    @InjectView(R.id.seller_title_title)
    TextView mSellerTitleTitle;
    @InjectView(R.id.iv_viewpager_bj)
    ImageView mViewpagerBj;
    @InjectView(R.id.seller_report)
    LinearLayout mSellerReport;
    @InjectView(R.id.seller_share)
    LinearLayout mSellerShare;
    @InjectView(R.id.imageList)
    RollPagerView mImageList;
    @InjectView(R.id.sellerName)
    TextView mSellerName;
    @InjectView(R.id.tv_remindtext)
    TextView mTvRemindtext;
    @InjectView(R.id.price)
    TextView mPrice;
    @InjectView(R.id.distance)
    TextView mDistance;
    @InjectView(R.id.districtNamesellerAddress)
    TextView mDistrictNamesellerAddress;
    @InjectView(R.id.sellerPhone)
    ImageView mSellerPhone;
    @InjectView(R.id.sellerMeal)
    TextView mSellerMeal;
    @InjectView(R.id.sellerCondition)
    TextView mSellerCondition;
    @InjectView(R.id.sellerServer)
    TextView mSellerServer;
    @InjectView(R.id.isWifi)
    ImageView mIsWifi;
    @InjectView(R.id.isParking)
    ImageView mIsParking;
    @InjectView(R.id.couponJb)
    TextView mCouponJb;
    @InjectView(R.id.couponMj)
    TextView mCouponMj;
    @InjectView(R.id.seller_message_text)
    WebView mSellerMessageText;
    @InjectView(R.id.seller_image)
    ImageView mSellerImage;
    @InjectView(R.id.sellermessage_paythebill)
    TextView mSellermessagePaythebill;
    @InjectView(R.id.maidan)
    LinearLayout mMaiDan;
    @InjectView(R.id.youhui)
    LinearLayout mYouHui;
    @InjectView(R.id.sellerScore)
    RatingBar mSellerScore;
    @InjectView(R.id.ll_weather_order)
    LinearLayout mLlWeatherOrder;
    @InjectView(R.id.ll_discount)
    LinearLayout mLlDiscount;//营业时间布局
    @InjectView(R.id.tv_weekNum)
    TextView mTvWeekNum;
    @InjectView(R.id.tv_opList)
    TextView mTvOpList;
    @InjectView(R.id.tv_weekNum2)
    TextView mTvWeekNum2;
    @InjectView(R.id.tv_opList2)
    TextView mTvOpList2;
    @InjectView(R.id.ll_time1_6)
    LinearLayout mLlTime16;
    @InjectView(R.id.ll_time6_7)
    LinearLayout mLlTime67;
    @InjectView(R.id.iv_remindtext)
    ImageView mIvRemindtext;

    private AdvertiseListStringAdapter selleradapter;
    private int requesCodes = 1;
    public SellerMessageActivity mActivity;

    /**
     * 广告信息
     */
    List<String> mAdvertiseListBeen = new ArrayList<>();
    private String mId;
    private SellerMessageInfoBean.SellerInfoBean mInfoBean;
    //    public List<SellerMessageInfoBean.SellerInfoBean.ImageListBean> mImageListBeen;
    private boolean mEquals;
    private int mMInfoBeanId;
    private String mName;
    private String mLabelsName;
    private String mStoreXpoint;
    private String mStoreYpoint;
    private String mInformation;
    private String[] mSplit;
    private boolean mBoolean;
    private String mSellerHead;
    private String mOrderid = "";
    private String type = "";

    @Override
    public int initLayoutId() {
        return R.layout.activity_seller_message;
    }

    @Override
    public void initView() {
        mActivity = this;
        mSellerMessageToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_success), "正在拼命加载中...");
        mSvShowSeller.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        //设置高度
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(UiUtils.getScreenWidth(mActivity), UiUtils.getScreenWidth(mActivity) / 2);
        mImageList.setLayoutParams(params);

        FrameLayout.LayoutParams mViewpagerBjParams = (FrameLayout.LayoutParams) mViewpagerBj.getLayoutParams();
        mViewpagerBjParams.height = UiUtils.getScreenWidth(mActivity) / 2;
        mViewpagerBj.setLayoutParams(mViewpagerBjParams);

        //设置透明度
        mImageList.setAnimationDurtion(500);
        selleradapter = new AdvertiseListStringAdapter(mImageList, mActivity, mAdvertiseListBeen);
        mImageList.setAdapter(selleradapter);
        mImageList.setHintView(new ColorPointHintView(this, Conversion.FB4E30, Color.WHITE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBoolean = getIntent().getBooleanExtra(Conversion.BOOLEAN, true);
        if (mBoolean) {
            mSellermessagePaythebill.setVisibility(View.VISIBLE);
        } else {
            mSellermessagePaythebill.setVisibility(View.GONE);
        }
        String mChoice = getIntent().getStringExtra(Conversion.CHOICE) + "";
        //是否是选择优惠买单了
        mEquals = mChoice.equals("Choice");
        if (mEquals) {
            mSellermessagePaythebill.setText("就选这家");
        } else {
            mSellermessagePaythebill.setText("买单");
        }
        type = getIntent().getStringExtra(Conversion.TYPE);
        mId = getIntent().getStringExtra(Conversion.ID);
        if (!TextUtils.isEmpty(mId)) {
            if ("1".equals(type)) {
                String[] split = mId.split(",");
                mOrderid = split[0];
                mId = split[1];
                initNetWork();
            } else {
                initNetWork();
            }
        } else {
            finish();
        }


    }

    /*商家详情访问*/
    private void initNetWork() {
        RetrofitUtil.createService(SellerService.class)
                .getSellerInfo(SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity), mId, type, mOrderid)
                .enqueue(new Callback<SellerMessageInfoBean>() {
                    @Override
                    public void onResponse(Call<SellerMessageInfoBean> call, Response<SellerMessageInfoBean> response) {
                        mSvShowSeller.setVisibility(View.VISIBLE);
                        mPaProgress.showContent();
                        if (response.isSuccessful()) {
                            SellerMessageInfoBean body = response.body();
                            if (body.getResultCode() == 1) {
                                if (mBoolean) {
                                    mSellermessagePaythebill.setVisibility(View.VISIBLE);
                                } else {
                                    mSellermessagePaythebill.setVisibility(View.GONE);
                                }
                                mLlShare.setVisibility(View.VISIBLE);
                                mSellerTitleTitle.setText("商家详情");
                                //删除添加
                                mPaProgress.removeAllViews();
                                mPaProgress.addView(mSvShowSeller);
                                mInfoBean = body.getSellerInfo();
                                if (mInfoBean != null) {
                                    inItSetView();
                                }
                            } else if (body.getResultCode() == 101) {
                                mSellermessagePaythebill.setVisibility(View.GONE);
                                mLlShare.setVisibility(View.GONE);
                                mSellerTitleTitle.setText("商家评价");
                                //删除替换
                                mPaProgress.removeAllViews();
                                addFragment(body.getSellerInfo().getSellerName());
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SellerMessageInfoBean> call, Throwable t) {
                        mPaProgress.showContent();
                        mSvShowSeller.setVisibility(View.GONE);
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    private void addFragment(String sellerName) {
        //new出EaseChatFragment或其子类的实例
        SellerPaySuccessFragment sellerPaySuccessFragment = new SellerPaySuccessFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putString(Conversion.ORDERID, mOrderid);
        args.putString(Conversion.SELLERINFOID, mId);
        args.putString(Conversion.SELLERINFONAME, sellerName);
        sellerPaySuccessFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.pa_progress, sellerPaySuccessFragment).commit();
    }

    /**
     * 填充数据
     */
    boolean isNotifyDataSetChanged = true;

    private void inItSetView() {
       /*显示的头像*/
        if (isNotifyDataSetChanged) {
            mAdvertiseListBeen.clear();
            String imageSrc = mInfoBean.getImageSrc();
            mSplit = imageSrc.split(",");
            for (String a : mSplit) {
                if (!TextUtils.isEmpty(a)) {
                    mAdvertiseListBeen.add(a);
                }
                LogUtil.d("商家详情图片---" + a);
            }
            if (mSplit.length > 0) {
                mViewpagerBj.setVisibility(View.GONE);
            }
            selleradapter.notifyDataSetChanged();
            isNotifyDataSetChanged = false;//这里只执行一次
        }
        //增加营业时间
        List<SellerMessageInfoBean.SellerInfoBean.OpTimesBean> discount = mInfoBean.getOpTimes();
        if (discount != null && discount.size() > 0) {
            mLlDiscount.setVisibility(View.VISIBLE);
            switch (discount.size()) {
                case 1:
                    mLlTime16.setVisibility(View.VISIBLE);
                    mLlTime67.setVisibility(View.GONE);
                    StringBuilder builder = new StringBuilder();
                    SellerMessageInfoBean.SellerInfoBean.OpTimesBean opTimesBean = discount.get(0);
                    mTvWeekNum.setText(opTimesBean.getWeekNum() + ":");
                    List<SellerMessageInfoBean.SellerInfoBean.OpTimesBean.OpListBean> opList = opTimesBean.getOpList();
                    for (int i = 0; i < opList.size(); i++) {
                        SellerMessageInfoBean.SellerInfoBean.OpTimesBean.OpListBean opListBean = opList.get(i);
                        builder.append(opListBean.getODesc() + ":" + opListBean.getOTime() + (i % 2 == 1 ? (i == opList.size() - 1 ? "" : "\n") : "   "));
                    }
                    mTvOpList.setText(builder.toString());
                    break;
                case 2:
                    mLlTime16.setVisibility(View.VISIBLE);
                    mLlTime67.setVisibility(View.VISIBLE);
                    StringBuilder stringBuilderer = new StringBuilder();
                    SellerMessageInfoBean.SellerInfoBean.OpTimesBean opTimesBean1 = discount.get(0);
                    mTvWeekNum.setText(opTimesBean1.getWeekNum() + ":");
                    List<SellerMessageInfoBean.SellerInfoBean.OpTimesBean.OpListBean> opListBeen = opTimesBean1.getOpList();
                    for (int i = 0; i < opListBeen.size(); i++) {
                        SellerMessageInfoBean.SellerInfoBean.OpTimesBean.OpListBean opListBean = opListBeen.get(i);
                        stringBuilderer.append(opListBean.getODesc() + ":" + opListBean.getOTime() + (i % 2 == 1 ? (i == opListBeen.size() - 1 ? "" : "\n") : "   "));
                    }
                    mTvOpList.setText(stringBuilderer.toString());

                    StringBuilder stringBuilder = new StringBuilder();
                    SellerMessageInfoBean.SellerInfoBean.OpTimesBean opTimesBean2 = discount.get(1);
                    mTvWeekNum2.setText(opTimesBean2.getWeekNum() + ":");
                    List<SellerMessageInfoBean.SellerInfoBean.OpTimesBean.OpListBean> opListBeen1 = opTimesBean2.getOpList();
                    for (int i = 0; i < opListBeen1.size(); i++) {
                        SellerMessageInfoBean.SellerInfoBean.OpTimesBean.OpListBean opListBean = opListBeen.get(i);
                        stringBuilder.append(opListBean.getODesc() + ":" + opListBean.getOTime() + (i % 2 == 1 ? (i == opListBeen1.size() - 1 ? "" : "\n") : "   "));
                    }
                    mTvOpList2.setText(stringBuilderer.toString());

                    break;
                default:
                    mLlTime16.setVisibility(View.GONE);
                    mLlTime67.setVisibility(View.GONE);
                    mLlDiscount.setVisibility(View.GONE);
                    break;
            }
        } else {
            mLlDiscount.setVisibility(View.GONE);
        }

        /*评分*/
        float countSelected = mInfoBean.getSellerScore() / 20f;
        if (countSelected > 0) {
            mSellerScore.setRating(countSelected);
        }
        /*名称*/
        mName = mInfoBean.getSellerName();
        mSellerName.setText(mName);
        /*多少钱一个人*/
        String price = "   ¥ " + mInfoBean.getPrice() + "/人";
        mPrice.setText(price);

        boolean weatherOrder = mInfoBean.getWeatherorder() == 1;
        String remindText = mInfoBean.getRemindText();
        if (weatherOrder && !TextUtils.isEmpty(remindText)) {
            mLlWeatherOrder.setVisibility(View.VISIBLE);
            mIvRemindtext.setVisibility(View.VISIBLE);
            mTvRemindtext.setText(remindText);
        } else {
            mIvRemindtext.setVisibility(View.GONE);
            mLlWeatherOrder.setVisibility(View.GONE);
        }

        /*地址*/
        String addressStr = (StringUtils.isNull(mInfoBean.getDistrictName()) ? "" : mInfoBean.getDistrictName())
                + mInfoBean.getSellerAddress();
        mDistrictNamesellerAddress.setText(addressStr);
        //口味,环境,服务
        mSellerMeal.setText("口味:" + new DecimalFormat("#.0").format(mInfoBean.getSellerMeal() / 20f));
        mSellerCondition.setText("环境:" + new DecimalFormat("#.0").format(mInfoBean.getSellerCondition() / 20f));
        mSellerServer.setText("服务:" + new DecimalFormat("#.0").format(mInfoBean.getSellerServer() / 20f));
        //停车 wifi
        mIsParking.setImageResource(mInfoBean.getIsParking() == 0 ? R.drawable.noparking : R.drawable.park);
        mIsWifi.setImageResource(mInfoBean.getIsWifi() == 0 ? R.drawable.nowifi : R.drawable.wifi);
        /*描述*/
        mInformation = mInfoBean.getInformation();
        mSellerMessageText.loadDataWithBaseURL("fake://not/needed", mInformation, "text/html", "utf-8", "");
        /*优惠*/
        String couponJb = mInfoBean.getCouponJb();
        if (StringUtils.isNoNull(couponJb)) {
            mCouponJb.setText(couponJb);
            mCouponJb.setVisibility(View.VISIBLE);
            mMaiDan.setVisibility(View.VISIBLE);
        } else {
            mMaiDan.setVisibility(View.GONE);
        }
        String couponMj = mInfoBean.getCouponMj();
        if (StringUtils.isNoNull(couponMj)) {
            mCouponMj.setText(couponMj);
            mCouponMj.setVisibility(View.VISIBLE);
            mYouHui.setVisibility(View.VISIBLE);
        } else {
            mYouHui.setVisibility(View.GONE);
        }

        /*距离*/
        String distanceStr;
        int distanceInt = mInfoBean.getDistance();
        if (distanceInt < 1000) {
            distanceStr = distanceInt + "m";
        } else {
            float size = distanceInt / 1000f;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
            distanceStr = df.format(size) + "km";//返回的是String类型的
        }
        mDistance.setText(distanceStr);
        //商家id
        mMInfoBeanId = mInfoBean.getId();
        //业态
        LogUtil.d("业态" + mInfoBean.toString());
        mLabelsName = mInfoBean.getSellerTypeImg();

        mStoreXpoint = mInfoBean.getXpoint();
        mStoreYpoint = mInfoBean.getYpoint();
        //头像
        mSellerHead = mInfoBean.getSellerHead();
//        //显示布局
//        mSvShowSeller.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.districtNamesellerAddress, R.id.sellerPhone, R.id.seller_report, R.id.seller_share, R.id.sellermessage_paythebill})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.districtNamesellerAddress:
//                openMapApp();
                openWebMap();
                break;
            case R.id.sellerPhone:
                isCall();
                break;
            case R.id.seller_report:
                Intent reportIntent = new Intent(this, ReportActivity.class);
                reportIntent.putExtra("type", "3");
                reportIntent.putExtra("reportId", mId);
                startActivity(reportIntent);
                break;
            case R.id.seller_share:
                ShowShred();
                break;
            case R.id.sellermessage_paythebill:
                if (mEquals) {
                    CacheActivity.finishSingleActivityByClass(SelectSellerActivity.class);
                    SelectorSellerBean event = new SelectorSellerBean(String.valueOf(mMInfoBeanId), mLabelsName, mName);
                    EventBus.getDefault().post(event);
                    LogUtil.d("点击了就选这家按钮");
                    finish();
                } else {
                    LogUtil.d("点击了买单");
                    Intent intent = new Intent(this, PaythebillActivity.class);
                    intent.putExtra(Conversion.TYPE, "1");
                    intent.putExtra(Conversion.SELLERINFOID, mMInfoBeanId + "");
                    intent.putExtra(Conversion.SELLERNMAE, mName + "");
                    startActivity(intent);
                }
                break;
        }
    }

    private void openWebMap() {
//        String addressStr = "http://api.map.baidu.com/marker?location=" +
//                mStoreXpoint + "," + mStoreYpoint + "&title=我的位置&content=米线&output=html";
//        String addressStr = "http://api.map.baidu.com/direction?origin=latlng:"+mXpoint+","+mYpoint +"|name:我的位置" +
//                "&destination="+mName+"&mode=driving&region=石家庄&output=html&src=陪你PN";
        //                        SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity),
        String addressStr = "http://api.map.baidu.com/direction?origin=latlng:" + SpUtils.getXpoint(mActivity) + "," + SpUtils.getYpoint(mActivity) + "|name:我的位置" +
                "&destination=latlng:" + mStoreXpoint + "," + mStoreYpoint + "|name:" + mName + "&mode=driving&region=石家庄&output=html&src=陪你PN";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(addressStr));
        mActivity.startActivity(intent);
    }

    private void openMapApp() {
        if (isAvailable(mActivity, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mStoreXpoint + "," + mStoreYpoint);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            mActivity.startActivity(mapIntent);
        } else {
            Toast.makeText(mActivity, "您尚未安装谷歌地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mActivity.startActivity(intent);
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return true:安装了指定的软件, false:没有安装
     */
    public static boolean isAvailable(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    //显示分享底部分享栏目
    private void ShowShred() {
        String shareTitle = mInfoBean.getShareTitle();
        String shareContent = mInfoBean.getShareContent();
        if (TextUtils.isEmpty(shareTitle) && TextUtils.isEmpty(shareContent)) {
            Toasty.normal(mActivity, "该店铺暂无分享内容").show();
            return;
        }
        String imagePath = UrlUtils.encode(IpConfig.HttpPic + mSellerHead);
        UMImage image = new UMImage(mActivity, imagePath);//网络图片
        String url = IpConfig.HttpPeiniIp + "share/sellerDetail?sellerId=" + mId;
        UMWeb web = new UMWeb(url);
        web.setTitle(shareTitle);//标题
        web.setThumb(image);
        web.setDescription(shareContent);//描述
        new ShareUtils(mActivity, web);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }
    /* UMImage image = new UMImage(mActivity, IpConfig.HttpPic + mSellerImage.get(0).getImageSrc());//网络图片
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressFormat = Bitmap.CompressFormat.PNG;
        String url = IpConfig.HttpPeiniIp + "share/taskDetail?taskId=" + mTaskId;
        final UMWeb web = new UMWeb(url);
        web.setTitle(mNickName);//标题
        web.setThumb(image);
        web.setDescription(mTaskDesc);//描述
        new ShareUtils(mActivity, web);*/


    /**
     * 打电话的方法  动态权限
     */
    private void isCall() {
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //权限未获得
            //用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                isCall(); //重新请求一次
            } else {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, requesCodes);
            }
        } else {
            //权限已获得
            //拨打电话
            final String sellerPhone = mInfoBean.getSellerPhone();
            if (StringUtils.isNull(sellerPhone)) {
                Toasty.normal(mActivity, "没有电话号码!").show();
                return;
            }
            new AlertDialog.Builder(mActivity)
                    .setMessage(sellerPhone)
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + sellerPhone);
                            intent.setData(data);
                            startActivity(intent);
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

    //打电话的动态权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == requesCodes) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isCall();
            } else {
                // Permission Denied
                Toast.makeText(SellerMessageActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

}
