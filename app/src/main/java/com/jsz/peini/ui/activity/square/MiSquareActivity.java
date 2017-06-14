package com.jsz.peini.ui.activity.square;


import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.square.UpdateUserBgImgBean;
import com.jsz.peini.model.square.UserInfoByOtherId;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.news.FansActivity;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.ui.view.TextProgressBar;
import com.jsz.peini.ui.view.square.TranslucentScrollView;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpDataUtils;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ProgressActivity;
import com.jsz.peini.widget.UseCameraActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiSquareActivity extends BaseActivity implements TranslucentScrollView.OnScrollChangedListener, NonGestureLockInterface {

    private int PHOTO_REQUEST_CUT = 100;//截取图片
    private int RESULT_LOAD_IMAGE2 = 200;
    private int RESULT_LOAD_IMAGE = 300;
    private int PHOTO_BACKGROUND = 400;//更换背景

    @InjectView(R.id.imageHead)
    CircleImageView mImageHead;
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;
    @InjectView(R.id.sex)
    ImageView mSex;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.nickname)
    TextView mNickname;
    @InjectView(R.id.signWord)
    TextView mSignWord;
    @InjectView(R.id.agenowProvinceTextnowCityText)
    TextView mAgenowProvinceTextnowCityText;
    @InjectView(R.id.myFans)
    TextView mMyFans;
    @InjectView(R.id.myConcern)
    TextView mMyConcern;
    @InjectView(R.id.goldList)
    ImageView mGoldList;
    @InjectView(R.id.buyList)
    ImageView mBuyList;
    @InjectView(R.id.integrityList)
    ImageView mIntegrityList;
    @InjectView(R.id.credittext)
    TextView mCredittext;
    @InjectView(R.id.credit)
    TextProgressBar mCredit;
    @InjectView(R.id.signStatusText)
    TextView mSignStatus;
    @InjectView(R.id.taskScoreName)
    TextView mTaskScoreName;
    @InjectView(R.id.selfCountText)
    TextView mSelfCount;
    @InjectView(R.id.isIdcard)
    ImageView mIsIdcard;
    @InjectView(R.id.isPhone)
    ImageView mIsPhone;
    @InjectView(R.id.gold)
    TextView mGold;
    @InjectView(R.id.score)
    TextView mScore;
    @InjectView(R.id.lableList)
    TagFlowLayout mLableList;
    @InjectView(R.id.content)
    TextView mContent;
    @InjectView(R.id.square_image)
    LinearLayout mSquareImage;
    @InjectView(R.id.squareLastInfo)
    ImageView mSquareLastInfo;
    @InjectView(R.id.squareLastInfo1)
    ImageView mSquareLastInfo1;
    @InjectView(R.id.squareLastInfo2)
    ImageView mSquareLastInfo2;
    @InjectView(R.id.squareLastInfo3)
    ImageView mSquareLastInfo3;
    @InjectView(R.id.sellerBigType)
    ImageView mSellerBigType;
    @InjectView(R.id.taskName)
    TextView mTaskName;
    @InjectView(R.id.square_toolbar)
    LinearLayout mSquareToolbar;
    @InjectView(R.id.square_more)
    ImageView mSquareMore;
    @InjectView(R.id.userLoginId)
    TextView mUserLoginId;
    @InjectView(R.id.square_bj)
    ImageView mSquareBj;
    @InjectView(R.id.ll_isIdcard)
    LinearLayout mLlIsIdcard;
    @InjectView(R.id.ll_isPhone)
    LinearLayout mLlIsPhone;
    @InjectView(R.id.jinbi_fen_wealth)
    LinearLayout mJinbiFenWealth;
    @InjectView(R.id.square_progressbar)
    LinearLayout mSquareProgressbar;
    /*任务点击条目*/
    @InjectView(R.id.mi_task)
    LinearLayout mMiTask;
    @InjectView(R.id.tv_mi_square_title)
    TextView mTvMiSquareTitle;
    @InjectView(R.id.ll_mi_title)
    LinearLayout mLlMiTitle;
    @InjectView(R.id.sv_mi_square_translucentscrollview)
    TranslucentScrollView mSvMiSquareTranslucentscrollview;
    @InjectView(R.id.ll_mi_square_imagelist)
    LinearLayout mLlmiSquareImagelist;
    @InjectView(R.id.tv_mi_square_imagelistsize)
    TextView mTvMiSquareImagelistsize;

    private MiSquareActivity mActivity;
    public Intent mIntent;
    private Uri mCroppedImageUri;
    private Drawable mIvBackDrawable;

    private boolean isNonShowGestureLock = true;
    private boolean isResumeFromSelectBackground = false;

    public static void actionShow(Context context) {
        if (SpUtils.isCompleteUserInfo(context)) {
            context.startActivity(new Intent(context, MiSquareActivity.class));
        } else {
            CompleteUserInfoActivity.actionShow(context, SpUtils.getUserToken(context));
        }
    }

    @Override
    public int initLayoutId() {
        return R.layout.activity_mi_square;
    }

    @Override
    public void initView() {
        super.initView();
        mActivity = this;

        mSquareToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        mSvMiSquareTranslucentscrollview.setOnScrollChangedListener(mActivity);
        mLlMiTitle.setBackgroundColor(Color.argb(0, 249, 249, 249));

        mIvBackDrawable = mIvBack.getDrawable();
        mIvBack.setImageDrawable(Conversion.tintDrawable(mIvBackDrawable, ColorStateList.valueOf(Color.WHITE)));
        //初始化数据
        String mySquare = (String) SpDataUtils.get(mActivity, "mySquare" , "");
        if (!TextUtils.isEmpty(mySquare)) {
            UserInfoByOtherId userInfo = new Gson().fromJson(mySquare, UserInfoByOtherId.class);
            initShowData(userInfo);
        } else {
            mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "正在拼命加载中...");
            mSvMiSquareTranslucentscrollview.setVisibility(View.GONE);
        }
        //结束初始化
    }

    private void initNetWork() {
        RetrofitUtil.createService(SquareService.class)
                .getUserAllInfo(SpUtils.getUserToken(mActivity))
                .enqueue(new Callback<UserInfoByOtherId>() {
                    @Override
                    public void onResponse(Call<UserInfoByOtherId> call, final Response<UserInfoByOtherId> response) {
                        if (response.isSuccessful()) {
                            UserInfoByOtherId body = response.body();
                            int resultCode = response.body().getResultCode();
                            if (resultCode == 1) {
                                SpDataUtils.put(mActivity, "mySquare", new Gson().toJson(body));
                                initShowData(body);
                            } else if (resultCode == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserInfoByOtherId> call, Throwable t) {
                    }
                });
    }

    private void initShowData(UserInfoByOtherId userAllInfo) {
        final UserInfoByOtherId.UserInfoBean infoBean = userAllInfo.getUserInfo();
        //性别显示
        final String sex = infoBean.getSex();
        if (StringUtils.isNoNull(sex)) {
            LogUtil.d("我的性别sex==" + sex);
            mSex.setImageResource("1".equals(sex) ? R.mipmap.sqnan : R.mipmap.sqnv);
        } else {
            mSex.setImageResource(R.mipmap.sqnan);
        }
        //账号
        LogUtil.d("我的账号UserLoginId==" + sex);
        String UserLoginId = infoBean.getAccCode();
        mUserLoginId.setText(UserLoginId);
         /*名字*/
        String nickname = infoBean.getNickname();
        mNickname.setText(nickname);
        //签名
        mSignWord.setText(infoBean.getSignWord());
        //头像  和  背景
        final String imageHead = infoBean.getImageHead();
        ViewTreeObserver vto2 = mImageHead.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImageHead.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = mImageHead.getWidth();
                int height = mImageHead.getHeight();
                LogUtil.d("宽=" + width + "高" + height);
                GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageHead, mImageHead, String.valueOf(sex), width, height);
            }
        });

        //背景图片
        String spaceBgImg = infoBean.getSpaceBgImg();
        if (StringUtils.isNoNull(spaceBgImg)) {
            if (spaceBgImg.contains("sysBgImgs")) {
                System.out.println("包含");
                GlideImgManager.loadImage(mActivity, IpConfig.HttpPeiniIp + spaceBgImg, mSquareBj, "6");
            } else if (spaceBgImg.contains("spaceBgImg")) {
                GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + spaceBgImg, mSquareBj, "6");
                System.out.println("不包含");
            } else {
                mSquareBj.setImageResource(R.mipmap.ic_launchers);
            }
        }
        StringBuffer buffer = new StringBuffer();
        if (!TextUtils.isEmpty(infoBean.getAge())) {
            buffer.append(infoBean.getAge() + "岁");
        }
        if (!TextUtils.isEmpty(infoBean.getNowProvinceText())) {
            buffer.append("-" + infoBean.getNowProvinceText());
        }
        if (!TextUtils.isEmpty(infoBean.getNowCityText())) {
            buffer.append(" " + infoBean.getNowCityText());
        }
        mAgenowProvinceTextnowCityText.setText(buffer.toString());
        mGoldList.setVisibility(!"0".equals(infoBean.getGoldList()) ? View.VISIBLE : View.GONE);
        mBuyList.setVisibility(!"0".equals(infoBean.getBuyList()) ? View.VISIBLE : View.GONE);
        mIntegrityList.setVisibility(!"0".equals(infoBean.getIntegrityList()) ? View.VISIBLE : View.GONE);
        UserInfoByOtherId.OtherInfoBean otherInfo = userAllInfo.getOtherInfo();
        mSignStatus.setText(1 == otherInfo.getSignStatus() ? "已签到" : "未签到");
        mSelfCount.setText("完整度" + otherInfo.getSelfCount() + "%");
        int credit = otherInfo.getCredit();
        mCredit.setProgress(credit);
        mCredittext.setText(credit > 80 ? "信誉值 - 高" : "信誉值 - 低");
        mMyFans.setText("粉丝 " + otherInfo.getMyFans());
        mMyConcern.setText("关注 " + otherInfo.getMyConcern());
        /*-------------金币----------------*/
        BigDecimal Gold = otherInfo.getGold();
        mGold.setText(Conversion.setFormatNum(String.valueOf(Gold)));
        String Score = otherInfo.getScore();
        mScore.setText(Conversion.setFormatNum(String.valueOf(Score)));
        /*-------------标签----------------*/
        List<UserInfoByOtherId.LableListBean> list = userAllInfo.getLableList();
        final LayoutInflater mInflater = LayoutInflater.from(this);
        mLableList.setAdapter(new TagAdapter<UserInfoByOtherId.LableListBean>(list) {
            @Override
            public View getView(FlowLayout parent, int position, UserInfoByOtherId.LableListBean s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv2,
                        mLableList, false);
                tv.setText(s.getLabelName());
                return tv;
            }
        });
        mLableList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mActivity, MiLabelActivity.class);
                startActivity(mIntent);
            }
        });
        /*-------------相册----------------*/
        List<UserInfoByOtherId.ImageListBean> imageList = userAllInfo.getImageList();
        int size = imageList.size();
        String imgCnt = userAllInfo.getImgCnt();
        Drawable drawable = getResources().getDrawable(R.drawable.backselect);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        switch (size) {
            case 1:
            case 4:
                View fourView = UiUtils.inflate(mActivity, R.layout.square_imagelist_four);
                ImageView imagefour1 = (ImageView) fourView.findViewById(R.id.iv_square_1);
                ImageView imagefour2 = (ImageView) fourView.findViewById(R.id.iv_square_2);
                ImageView imagefour3 = (ImageView) fourView.findViewById(R.id.iv_square_3);
                ImageView imagefour4 = (ImageView) fourView.findViewById(R.id.iv_square_4);
                FrameLayout fourcanhide = (FrameLayout) fourView.findViewById(R.id.fl_canhide);
                if (size == 1) {
                    fourcanhide.setVisibility(View.GONE);
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(0).getImageSrc(), imagefour1, "");
                    mTvMiSquareImagelistsize.setText(imgCnt + "张照片,查看全部");
                } else {
                    fourcanhide.setVisibility(View.VISIBLE);
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(0).getImageSrc(), imagefour1, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(1).getImageSrc(), imagefour2, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(2).getImageSrc(), imagefour3, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(3).getImageSrc(), imagefour4, "");
                    mTvMiSquareImagelistsize.setText(imgCnt + "张照片,查看全部");
                }
                mLlmiSquareImagelist.removeAllViews();
                mLlmiSquareImagelist.addView(fourView);
                mTvMiSquareImagelistsize.setCompoundDrawables(null, null, drawable, null);
                break;
            case 2:
            case 5:
                View five = UiUtils.inflate(mActivity, R.layout.square_imagelist_five);
                ImageView imagefive1 = (ImageView) five.findViewById(R.id.iv_square_1);
                ImageView imagefive2 = (ImageView) five.findViewById(R.id.iv_square_2);
                ImageView imagefive3 = (ImageView) five.findViewById(R.id.iv_square_3);
                ImageView imagefive4 = (ImageView) five.findViewById(R.id.iv_square_4);
                ImageView imagefive5 = (ImageView) five.findViewById(R.id.iv_square_5);
                FrameLayout fivecanhide = (FrameLayout) five.findViewById(R.id.fl_canhide);
                if (size == 2) {
                    fivecanhide.setVisibility(View.GONE);
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(0).getImageSrc(), imagefive1, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(1).getImageSrc(), imagefive2, "");
                    mTvMiSquareImagelistsize.setText(imgCnt + "张照片,查看全部");
                } else {
                    fivecanhide.setVisibility(View.VISIBLE);
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(0).getImageSrc(), imagefive1, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(1).getImageSrc(), imagefive2, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(2).getImageSrc(), imagefive3, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(3).getImageSrc(), imagefive4, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(4).getImageSrc(), imagefive5, "");
                    mTvMiSquareImagelistsize.setText(imgCnt + "张照片,查看全部");
                }
                mLlmiSquareImagelist.removeAllViews();
                mLlmiSquareImagelist.addView(five);
                mTvMiSquareImagelistsize.setCompoundDrawables(null, null, drawable, null);
                break;
            case 3:
            case 6:
                View six = UiUtils.inflate(mActivity, R.layout.square_imagelist_six);
                ImageView imagesix1 = (ImageView) six.findViewById(R.id.iv_square_1);
                ImageView imagesix2 = (ImageView) six.findViewById(R.id.iv_square_2);
                ImageView imagesix3 = (ImageView) six.findViewById(R.id.iv_square_3);
                ImageView imagesix4 = (ImageView) six.findViewById(R.id.iv_square_4);
                ImageView imagesix5 = (ImageView) six.findViewById(R.id.iv_square_5);
                ImageView imagesix6 = (ImageView) six.findViewById(R.id.iv_square_6);
                FrameLayout sixcanhide = (FrameLayout) six.findViewById(R.id.fl_canhide);
                if (size == 3) {
                    sixcanhide.setVisibility(View.GONE);
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(0).getImageSrc(), imagesix1, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(1).getImageSrc(), imagesix2, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(2).getImageSrc(), imagesix3, "");
                    mTvMiSquareImagelistsize.setText(imgCnt + "张照片,查看全部");
                } else {
                    sixcanhide.setVisibility(View.VISIBLE);
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(0).getImageSrc(), imagesix1, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(1).getImageSrc(), imagesix2, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(2).getImageSrc(), imagesix3, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(3).getImageSrc(), imagesix4, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(4).getImageSrc(), imagesix5, "");
                    GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageList.get(5).getImageSrc(), imagesix6, "");
                    mTvMiSquareImagelistsize.setText(imgCnt + "张照片,查看全部");
                }
                mLlmiSquareImagelist.removeAllViews();
                mLlmiSquareImagelist.addView(six);
                mTvMiSquareImagelistsize.setCompoundDrawables(null, null, drawable, null);
                break;
            case 0:
            default:
                View four = UiUtils.inflate(mActivity, R.layout.square_imagelist_four);
                ImageView image1 = (ImageView) four.findViewById(R.id.iv_square_1);
                ImageView ivSquareWutu = (ImageView) four.findViewById(R.id.iv_square_wutu);
                FrameLayout canhide = (FrameLayout) four.findViewById(R.id.fl_canhide);
                image1.setVisibility(View.GONE);
                canhide.setVisibility(View.GONE);
                ivSquareWutu.setVisibility(View.VISIBLE);
                mTvMiSquareImagelistsize.setText("您还没有照片,点击上传");
                mTvMiSquareImagelistsize.setCompoundDrawables(null, null, null, null);
                mLlmiSquareImagelist.removeAllViews();
                mLlmiSquareImagelist.addView(four);
                break;
        }

        /*-------------认证----------------*/
        mIsIdcard.setImageResource("1".equals(infoBean.getIsIdCard()) ? R.mipmap.idcard2 : R.mipmap.idcard1);
        mIsPhone.setImageResource("1".equals(infoBean.getIsPhone()) ? R.mipmap.phone2 : R.mipmap.phone1);
        mLlIsIdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("1".equals(infoBean.getIsIdCard())) {
                    return;
                }
                /**身份认证认证*/
                startActivity(new Intent(mActivity, IdentityAuthenticationActivity.class));
            }
        });
        mLlIsPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if ("1".equals(infoBean.getIsPhone())) {
//                    return;
//                }
                /**手机认证*/
                startActivity(new Intent(mActivity, PhoneAuthenticationActivity.class));
            }
        });

        /*广场信息*/
        UserInfoByOtherId.SquareLastInfoBean squareLastInfo = userAllInfo.getSquareLastInfo();
        if (squareLastInfo != null) {
            mContent.setVisibility(View.VISIBLE);
            mSquareLastInfo.setVisibility(View.VISIBLE);
            if (StringUtils.isNoNull(squareLastInfo.getContent())) {
                mContent.setText(squareLastInfo.getContent());
            } else {
                mContent.setVisibility(View.GONE);
            }
            if (StringUtils.isNoNull(squareLastInfo.getImageSrc())) {
                mSquareLastInfo.setVisibility(View.GONE);
                mSquareLastInfo1.setVisibility(View.GONE);
                mSquareLastInfo2.setVisibility(View.GONE);
                mSquareLastInfo3.setVisibility(View.GONE);
                String imageSrc = squareLastInfo.getImageSrc();
                String[] splitImageSrc = imageSrc.split(",");
                try {
                    mSquareImage.setVisibility(splitImageSrc.length > 0 ? View.VISIBLE : View.GONE);
                    switch (splitImageSrc.length) {
                        case 1:
                            mSquareLastInfo.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[0], mSquareLastInfo, "");
                            break;
                        case 2:
                            mSquareLastInfo.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[0], mSquareLastInfo, "");
                            mSquareLastInfo1.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[1], mSquareLastInfo1, "");
                            break;
                        case 3:
                            mSquareLastInfo.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[0], mSquareLastInfo, "");
                            mSquareLastInfo1.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[1], mSquareLastInfo1, "");
                            mSquareLastInfo2.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[2], mSquareLastInfo2, "");
                            break;
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                            mSquareLastInfo.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[0], mSquareLastInfo, "");
                            mSquareLastInfo1.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[1], mSquareLastInfo1, "");
                            mSquareLastInfo2.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[2], mSquareLastInfo2, "");
                            mSquareLastInfo3.setVisibility(View.VISIBLE);
                            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + splitImageSrc[3], mSquareLastInfo3, "");
                            break;
                        default:
                            mSquareImage.setVisibility(View.GONE);
                            break;
                    }
                } catch (Exception e) {
                    LogUtil.e("空间图片", e.getMessage());
                }
            } else {
                mSquareImage.setVisibility(View.GONE);
            }
        } else {
            mContent.setVisibility(View.GONE);
            mSquareImage.setVisibility(View.GONE);
        }

        /*===============商家==================*/
        UserInfoByOtherId.TaskLastInfoBean taskLastInfo = userAllInfo.getTaskLastInfo();
        String taskName = taskLastInfo.getTaskName();
        mTaskName.setText(StringUtils.isNoNull(taskName) ? taskName : "");
        mTaskScoreName.setText("达成率" + (StringUtils.isNoNull(taskLastInfo.getTaskScore()) ? taskLastInfo.getTaskScore() : "0") + "%");
        String sellerBigType = taskLastInfo.getSellerTypeImg();
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + sellerBigType, mSellerBigType, "7");
        //显示视图
        mPaProgress.showContent();
        mSvMiSquareTranslucentscrollview.setVisibility(View.VISIBLE);
    }
    /*点击事件*/

    @Override
    protected void initListener() {
        mLlmiSquareImagelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mActivity, MiPhotoAlbumManagementActivity.class);
                startActivity(mIntent);
            }
        });
        mTvMiSquareImagelistsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mActivity, MiPhotoAlbumManagementActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @OnClick({R.id.imageHead, R.id.updata_autograph, R.id.mi_square, R.id.square_more, R.id.signStatus,
            R.id.LabelActivity, R.id.selfCount, R.id.jinbi_fen_wealth, R.id.square_progressbar,
            R.id.square_bj, R.id.mi_task, R.id.mi_details, R.id.rl_coupon, R.id.myFans, R.id.myConcern})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.square_more: //更多
                break;
            case R.id.imageHead: //点击更换头像
                showPopselector();
                break;
            case R.id.square_bj: //点击背景
                mIntent = new Intent(mActivity, MyBackgroundActivity.class);
                startActivityForResult(mIntent, PHOTO_BACKGROUND);
                break;
            case R.id.signStatus: //签到界面
                mIntent = new Intent(mActivity, MiSignActivity.class);
                startActivity(mIntent);
                break;
            case R.id.jinbi_fen_wealth: //财富
//                mIntent = new Intent(mActivity, MiWealthActivity.class);
                mIntent = new Intent(mActivity, MyWealthActivity.class);
                mIntent.putExtra(Conversion.TYPE, 1);
                startActivity(mIntent);
                break;
            case R.id.square_progressbar: //信用
                mIntent = new Intent(mActivity, MyCreditActivity.class);
                startActivity(mIntent);
                break;
            case R.id.mi_task://我的任务列表
                mIntent = new Intent(mActivity, MiTaskActivity.class);
                startActivity(mIntent);
                break;
//            case R.id.mi_details://我的详细资料界面
            case R.id.selfCount://我的详细资料界面
                mIntent = new Intent(mActivity, TaSquareMessageActivity.class);
                mIntent.putExtra(Conversion.TYPE, "1");
                startActivity(mIntent);
                break;
            case R.id.LabelActivity://我的标签选择
                mIntent = new Intent(mActivity, MiLabelActivity.class);
                startActivity(mIntent);
                break;
            case R.id.mi_square://跳转到我的空间看看
                mIntent = new Intent(mActivity, MiSquareHomepageActivity.class);
                mIntent.putExtra(Conversion.TYPE, "1");
                mIntent.putExtra(Conversion.TOKEN, mUserToken);
                startActivity(mIntent);
                break;
            case R.id.updata_autograph://跳转到我的签名修改
//                mIntent = new Intent(this, NickNameActivity.class);
//                mIntent.putExtra("type", "1");
//                mIntent.putExtra("title", "修改签名");
//                startActivity(mIntent);
                NickNameActivity.actionShow(mActivity, 1, mSignWord.getText().toString().trim());
                break;
            case R.id.rl_coupon: //优惠券
                startActivity(new Intent(this, MiCouponActivity.class));
                break;
            case R.id.myFans:
                startActivity(new Intent(mActivity, FansActivity.class));
                break;
            case R.id.myConcern:
                Intent intent = new Intent(mActivity, MiAttentionActivity.class);
                intent.putExtra(MiAttentionActivity.EXTRA_JUMP_FLAG, false);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isNonShowGestureLock = true;
        if (isResumeFromSelectBackground) {
            isResumeFromSelectBackground = false;
        } else {
            initNetWork();
        }
    }

    private void showPopselector() {
        final Popwindou popwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_mi_square));
        View view = UiUtils.inflate(mActivity, R.layout.pop);
        popwindou.init(view, Gravity.BOTTOM, true);
        TextView item_popupwindows1 = (TextView) view.findViewById(R.id.item_popupwindows1);
        TextView item_popupwindows2 = (TextView) view.findViewById(R.id.item_popupwindows2);
        TextView item_dismis = (TextView) view.findViewById(R.id.item_dismis);
        item_popupwindows1.setText("拍照");
        item_popupwindows2.setText("从手机相册选择");
        item_dismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popwindou.dismiss();
            }
        });
        //拍照
        item_popupwindows1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 在启动拍照之前最好先判断一下sdcard是否可用
                 */
                String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(mActivity,
                            UseCameraActivity.class);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE2);
                    isNonShowGestureLock = false;
                } else {
                    Toasty.normal(mActivity, "sdcard不可用").show();
                }
                popwindou.dismiss();
            }
        });
        //相册选择
        item_popupwindows2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mIntent.setType("image/*");
                isNonShowGestureLock = false;
                startActivityForResult(mIntent, RESULT_LOAD_IMAGE);
                popwindou.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_BACKGROUND) {
            isResumeFromSelectBackground = true;
            if (resultCode == RESULT_OK && null != data) {
                String imageUrl = data.getStringExtra("img");
                boolean isSelectedFileUrl = data.getBooleanExtra("isFileUrl", false);
                if (isSelectedFileUrl) {
                    updateUserBgImg(imageUrl);
                } else {
                    setBackgroundImage(imageUrl);
                }
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            crop(selectedImage, false);
//            startPhotoZoom(new File(selectedImage.getPath()));
        }
        if (requestCode == RESULT_LOAD_IMAGE2 && null != data) {
            String extra = data.getStringExtra(UseCameraActivity.IMAGE_PATH);
            String correction = Conversion.correctImage(extra);
//            crop(Uri.parse(extra), true);
            startPhotoZoom(new File(correction));
        }
        if (requestCode == PHOTO_REQUEST_CUT && resultCode == RESULT_OK && data != null) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mCroppedImageUri != null && StringUtils.isNoNull(mCroppedImageUri.getPath())) {
                        initUpdateUserImageHead(mCroppedImageUri.getPath());
                    }
                }
            }).start();
        }
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file) {
        LogUtil.i("TAG", getImageContentUri(this, file) + "裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(this, file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 800);
            intent.putExtra("aspectY", 800);
            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 800);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", false);
            mCroppedImageUri = Uri.fromFile(new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "crop_" + System.currentTimeMillis() + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImageUri);//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /*
     * 剪切图片 相册选择
     */
    private void crop(Uri uri, boolean isAfterCapture) {
        if (uri == null) {
            LogUtil.i("alanjet", "The uri is not exist.");
        }
        LogUtil.d("tempUri" + uri);

        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        Uri imageUri;
        if (isAfterCapture && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && uri != null) {
            File file = new File(uri.getPath());
            imageUri = FileProvider.getUriForFile(mActivity, "com.jsz.peini.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = uri;
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 800);
        intent.putExtra("aspectY", 800);
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        mCroppedImageUri = Uri.fromFile(new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "crop_" + System.currentTimeMillis() + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void initUpdateUserImageHead(final String bitmap) {
        final File file = new File(bitmap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RetrofitUtil.createService(SquareService.class)
                .updateUserImageHead(part, mUserToken)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {

                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                LogUtil.d("更换头像成功" + response.body().toString());
                                initNetWork();
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                        Toasty.error(mActivity, Conversion.NETWORKERROR).show();
                        finish();
                    }
                });
    }

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        //Y轴偏移量
        float scrollY = who.getScrollY();
        //变化率
        float headerBarOffsetY = getResources().getDimension(R.dimen.y300) - getResources().getDimension(R.dimen.y100);//Toolbar与header高度的差值
        float offset = Math.abs(1 - Math.max((headerBarOffsetY - scrollY) / headerBarOffsetY, 0f));
        //Toolbar背景色透明度
        LogUtil.d("滑动的距离==" + offset);
        mLlMiTitle.setBackgroundColor(Color.argb((int) (offset * 254), 249, 249, 249));
        if (offset == 0) {
            mIvBack.setImageDrawable(Conversion.tintDrawable(mIvBackDrawable, ColorStateList.valueOf(getResources().getColor(R.color.colorf9f9f9))));
        } else {
            mIvBack.setImageDrawable(Conversion.tintDrawable(mIvBackDrawable, ColorStateList.valueOf(Color.argb((int) (offset * 254), 0, 0, 0))));
        }
        //header背景图Y轴偏移
        //imgHead.setTranslationY(scrollY / 2);
    }

    @Override
    public boolean isGestureLock() {
        return isNonShowGestureLock;
    }


    /**
     * 选择备选图片设置空间背景
     */
    private void setBackgroundImage(String image) {
        RetrofitUtil.createService(SquareService.class)
                .updateUserBgImg(mUserToken, image)
                .enqueue(new Callback<UpdateUserBgImgBean>() {
                    @Override
                    public void onResponse(Call<UpdateUserBgImgBean> call, Response<UpdateUserBgImgBean> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResultCode() == 1) {

                                String imageUrl = null;
                                if (response.body().getSpaceBgImg().contains("sysBgImgs")) {
                                    imageUrl = IpConfig.HttpPeiniIp + response.body().getSpaceBgImg();
                                } else if (response.body().getSpaceBgImg().contains("spaceBgImg")) {
                                    imageUrl = IpConfig.HttpPic + response.body().getSpaceBgImg();
                                }

                                Drawable preDrawable = mSquareBj.getDrawable();
                                Glide.with(mActivity).load(imageUrl)
                                        .placeholder(preDrawable)
                                        .error(preDrawable)
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mSquareBj);

                            } else if (response.body().getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (response.body().getResultCode() == 0) {
                                Toasty.normal(mActivity, response.body().getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<UpdateUserBgImgBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    private void updateUserBgImg(String bitmap) {
        final File file = new File(bitmap);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RetrofitUtil.createService(SquareService.class)
                .updateUserBgImg(mUserToken, part, "")
                .enqueue(new Callback<UpdateUserBgImgBean>() {
                    @Override
                    public void onResponse(Call<UpdateUserBgImgBean> call, Response<UpdateUserBgImgBean> response) {
                        if (response.isSuccessful()) {
                            UpdateUserBgImgBean body = response.body();
                            if (body.getResultCode() == 1) {

                                Drawable preDrawable = mSquareBj.getDrawable();
                                Glide.with(mActivity).load(IpConfig.HttpPic + response.body().getSpaceBgImg())
                                        .placeholder(preDrawable)
                                        .error(preDrawable)
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mSquareBj);

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
                    public void onFailure(Call<UpdateUserBgImgBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }
}
