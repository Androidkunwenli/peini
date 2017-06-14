package com.jsz.peini.ui.activity.square;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.UserInfoByOtherId;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.activity.report.ReportActivity;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.ui.view.TextProgressBar;
import com.jsz.peini.ui.view.square.TranslucentScrollView;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jsz.peini.R.id.credit;
import static com.jsz.peini.R.id.square_more;

public class TaSquareActivity extends BaseActivity implements TranslucentScrollView.OnScrollChangedListener {
    @InjectView(R.id.tv_ta_square_title)
    TextView mTvTaSquareTitle;
    @InjectView(R.id.tv_ta_square_name)
    TextView mTvTaSquareName;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.square_bj)
    ImageView mSquareBj;
    @InjectView(R.id.imageHead)
    CircleImageView mImageHead;
    @InjectView(R.id.sex)
    ImageView mSex;
    @InjectView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @InjectView(R.id.isConcern)
    RadioButton mIsConcern;
    @InjectView(R.id.nickname)
    TextView mNickname;
    @InjectView(R.id.signWord)
    TextView mSignWord;
    @InjectView(R.id.agenowProvinceTextnowCityText)
    TextView mAgenowProvinceTextnowCityText;
    @InjectView(R.id.myConcernmyFans)
    TextView mMyConcernmyFans;
    @InjectView(R.id.imageList)
    RecyclerView mImageList;
    @InjectView(R.id.lableList)
    TagFlowLayout mLableList;
    @InjectView(R.id.userLoginId)
    TextView mUserLoginId;
    @InjectView(R.id.goldList)
    ImageView mGoldList;
    @InjectView(R.id.buyList)
    ImageView mBuyList;
    @InjectView(R.id.integrityList)
    ImageView mIntegrityList;
    @InjectView(R.id.credittext)
    TextView mCredittext;
    @InjectView(credit)
    TextProgressBar mCredit;
    @InjectView(R.id.sellerBigType)
    ImageView mSellerBigType;
    @InjectView(R.id.taskName)
    TextView mTaskName;
    @InjectView(R.id.taskScore)
    TextView mTaskScore;
    @InjectView(R.id.isIdcard)
    ImageView mIsIdcard;
    @InjectView(R.id.isPhone)
    ImageView mIsPhone;
    @InjectView(R.id.content)
    TextView mContent;
    @InjectView(R.id.squareLastInfo)
    ImageView mSquareLastInfo;
    @InjectView(R.id.square_toolbar)
    LinearLayout mSquareToolbar;
    @InjectView(R.id.mi_details)
    LinearLayout mMiDetails;
    @InjectView(R.id.square_more)
    ImageView mSquareMore;
    @InjectView(R.id.sv_ta_square_title)
    TranslucentScrollView mSvTaSquareTitle;
    @InjectView(R.id.ll_ta_square_title)
    LinearLayout mLlTaSquareTitle;
    @InjectView(R.id.ll_mi_square_imagelist)
    LinearLayout mLlmiSquareImagelist;
    @InjectView(R.id.tv_mi_square_imagelistsize)
    TextView mTvMiSquareImagelistsize;
    @InjectView(R.id.squareLastInfo1)
    ImageView mSquareLastInfo1;
    @InjectView(R.id.squareLastInfo2)
    ImageView mSquareLastInfo2;
    @InjectView(R.id.squareLastInfo3)
    ImageView mSquareLastInfo3;
    @InjectView(R.id.square_image)
    LinearLayout mSquareImage;
    private TaSquareActivity mActivity;
    private Popwindou mPopwindou;
    private boolean mIsConcernSelector;
    /**
     * 他人空间 id
     */
    private String mOtherId;
    private String cellPhone;
    private Intent mIntent;
    private String mId;
    private String mHead;
    private String mSex1;
    private String mAge;
    private String mNickname1;
    private List<String> mBlackListFromServer;
    private Drawable mIvBackDrawable;
    private String mIndustryText = "";
    private int mSize;
    private String infoBeanGoldList;
    private String infoBeanBuyList;
    private String infoBeanIntegrityList;

    @Override
    public int initLayoutId() {
        return R.layout.activity_ta_square;
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

        mIvBackDrawable = mIvBack.getDrawable();
        mIvBack.setImageDrawable(Conversion.tintDrawable(mIvBackDrawable, ColorStateList.valueOf(Color.WHITE)));


        Intent intent = getIntent();
        mOtherId = intent.getStringExtra(Conversion.USERID);
        LogUtil.d(getLocalClassName(), "他人空间ID---> " + mOtherId + "");
        mSvTaSquareTitle.setOnScrollChangedListener(mActivity);
        mLlTaSquareTitle.setBackgroundColor(Color.argb(0, 249, 249, 249));
    }


    @Override
    public void initData() {
        initNetWork();
    }

    /**
     * 网络访问
     */
    private void initNetWork() {
        mDialog.show();
        RetrofitUtil.createService(SquareService.class)
                .getUserInfoByOtherId(mOtherId, mUserToken)
                .enqueue(new Callback<UserInfoByOtherId>() {
                    @Override
                    public void onResponse(Call<UserInfoByOtherId> call, final Response<UserInfoByOtherId> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            UserInfoByOtherId body = response.body();
                            int resultCode = body.getResultCode();
                            if (resultCode == 1) {
                                initShowData(body);
                            } else if (resultCode == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (resultCode == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoByOtherId> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        mDialog.dismiss();
                    }
                });
    }

    /**
     * 展示数据
     *
     * @param userAllInfo
     */
    private void initShowData(final UserInfoByOtherId userAllInfo) {
        UserInfoByOtherId.OtherInfoBean otherInfo = userAllInfo.getOtherInfo();
        int credit = otherInfo.getCredit();
        mCredit.setProgress(credit);
        mCredittext.setText(otherInfo.getCredit() > 80 ? "信誉值 - 高" : "信誉值 - 低");
        mMyConcernmyFans.setText("粉丝" + otherInfo.getMyFans() + " | " + "关注" + otherInfo.getMyConcern());
        /**关注的点击事件*/
        mIsConcernSelector = otherInfo.getIsConcern() == 1 ? true : false;
        mIsConcern.setChecked(mIsConcernSelector);
        mIsConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsConcernSelector = !mIsConcernSelector;
                mIsConcern.setChecked(mIsConcernSelector);
                if (mIsConcernSelector) {
                    LogUtil.d(getLocalClassName(), "关注" + mIsConcernSelector);
                    ConcernSuccess();
                } else {
                    LogUtil.d(getLocalClassName(), "取消" + mIsConcernSelector);
                    ConcernSuccess();
                }
            }
        });
        UserInfoByOtherId.UserInfoBean infoBean = userAllInfo.getUserInfo();
        String accCode = infoBean.getAccCode();
        mUserLoginId.setText(StringUtils.isNull(accCode) ? "" : accCode);
        mAge = infoBean.getAge();
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
        //性别
        mSex1 = infoBean.getSex() + "";
        mSex.setImageResource(mSex1.equals("1") ? R.mipmap.sqnan : R.mipmap.sqnv);
        //头像
        mHead = infoBean.getImageHead();
        ViewTreeObserver vto2 = mImageHead.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImageHead.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = mImageHead.getWidth();
                int height = mImageHead.getHeight();
                LogUtil.d("宽=" + width + "高" + height);
                GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + mHead, mImageHead, String.valueOf(mSex1), width, height);
            }
        });
        //专业
        mIndustryText = StringUtils.isNull(infoBean.getIndustryText()) ? "" : infoBean.getIndustryText();
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
        //姓名
        mNickname1 = infoBean.getNickname();
        if (!TextUtils.isEmpty(mNickname1)) {
            mTvTaSquareName.setVisibility(View.VISIBLE);
            mTvTaSquareTitle.setVisibility(View.VISIBLE);
            mTvTaSquareName.setText(mNickname1);
            mNickname.setText(mNickname1);
        }
        mSignWord.setText(infoBean.getSignWord());
        infoBeanGoldList = infoBean.getGoldList();
        mGoldList.setVisibility(!"0".equals(infoBeanGoldList) ? View.VISIBLE : View.GONE);
        infoBeanBuyList = infoBean.getBuyList();
        mBuyList.setVisibility(!"0".equals(infoBeanBuyList) ? View.VISIBLE : View.GONE);
        infoBeanIntegrityList = infoBean.getIntegrityList();
        mIntegrityList.setVisibility(!"0".equals(infoBeanIntegrityList) ? View.VISIBLE : View.GONE);
         /*-------------相册----------------*/
        List<UserInfoByOtherId.ImageListBean> imageList = userAllInfo.getImageList();
        mSize = imageList.size();
        String imgCnt = userAllInfo.getImgCnt();
        switch (mSize) {
            case 1:
            case 4:
                View fourView = UiUtils.inflate(mActivity, R.layout.square_imagelist_four);
                ImageView imagefour1 = (ImageView) fourView.findViewById(R.id.iv_square_1);
                ImageView imagefour2 = (ImageView) fourView.findViewById(R.id.iv_square_2);
                ImageView imagefour3 = (ImageView) fourView.findViewById(R.id.iv_square_3);
                ImageView imagefour4 = (ImageView) fourView.findViewById(R.id.iv_square_4);
                FrameLayout fourcanhide = (FrameLayout) fourView.findViewById(R.id.fl_canhide);
                if (mSize == 1) {
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
                mTvMiSquareImagelistsize.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.backselect), null);
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
                if (mSize == 2) {
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
                mTvMiSquareImagelistsize.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.backselect), null);
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
                if (mSize == 3) {
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
                mTvMiSquareImagelistsize.setCompoundDrawables(null, null, getResources().getDrawable(R.mipmap.backselect), null);
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
                mTvMiSquareImagelistsize.setText("暂无照片");
                mTvMiSquareImagelistsize.setCompoundDrawables(null, null, null, null);
                mLlmiSquareImagelist.removeAllViews();
                mLlmiSquareImagelist.addView(four);
                break;
        }

//        LinearLayoutManager layout = new LinearLayoutManager(mActivity);
//        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mImageList.setLayoutManager(layout);
//        TaImageListAdapter adapter = new TaImageListAdapter(mActivity, imageList);
//        adapter.addFootView(UiUtils.inflate(mActivity, R.layout.mi_release_photo_foot));
//        mImageList.setAdapter(adapter);
//        adapter.setOnClicPhotokListener(new TaImageListAdapter.OnClicPhotokListener() {
//            @Override
//            public void mPhotoItemkListener(int position) {
//                TaPhotoImageActivity.actionShow(mActivity, mOtherId);
//            }
//
//            @Override
//            public void FootViewItemkListener(int position) {
//                Toasty.normal(mActivity, "他人相册没有照片!").show();
////                startActivity(new Intent(mActivity, TaPhotoImageActivity.class));
////                TaPhotoImageActivity.actionShow(mActivity, mOtherId);
//            }
//        });
         /*-------------认证----------------*/
        mIsIdcard.setImageResource("1".equals(infoBean.getIsIdcard()) ? R.mipmap.idcard2 : R.mipmap.idcard1);
        mIsPhone.setImageResource("1".equals(infoBean.getIsPhone()) ? R.mipmap.phone2 : R.mipmap.phone1);
        /*-============广场===============*/
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
        UserInfoByOtherId.TaskLastInfoBean taskLastInfo = userAllInfo.getTaskLastInfo();
        String taskName = taskLastInfo.getTaskName();
        String taskId = taskLastInfo.getTaskId();
        String sellerBigType = taskLastInfo.getSellerTypeImg();
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + sellerBigType, mSellerBigType, "7");
        mTaskName.setText(StringUtils.isNoNull(taskName) ? taskName : "");
        String taskScore = "达成率" + (StringUtils.isNoNull(taskLastInfo.getTaskScore()) ? taskLastInfo.getTaskScore() : "0") + "%";
        mTaskScore.setText(taskScore);
        //他人token
        mId = userAllInfo.getUserInfo().getId();
        //他人手机号
        cellPhone = userAllInfo.getUserInfo().getUserPhone();

    }

    @Override
    protected void initListener() {
        mLlmiSquareImagelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSize == 0) {
                    Toasty.normal(mActivity, "他人相册没有照片!").show();
                    return;
                }
                TaPhotoImageActivity.actionShow(mActivity, mOtherId);
            }
        });
        mTvMiSquareImagelistsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSize == 0) {
                    Toasty.normal(mActivity, "他人相册没有照片!").show();
                    return;
                }
                TaPhotoImageActivity.actionShow(mActivity, mOtherId);
            }
        });

    }

    /**
     * 添加关注
     */
    private void ConcernSuccess() {
        RetrofitUtil.createService(SquareService.class).
                goConcern(mUserToken, mOtherId)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean successfulBean = response.body();
                            if (successfulBean.getResultCode() == 1) {

                            } else if (successfulBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (successfulBean.getResultCode() == 0) {
                                Toasty.normal(mActivity, successfulBean.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /**
     * 点击事件
     */
    boolean mContains;

    @OnClick({R.id.square_more, R.id.ta_task, R.id.ta_square_hiscredit, R.id.ta_square, R.id.mi_details, R.id.imageHead})
    public void onClick(View view) {
        switch (view.getId()) {
            case square_more:
                getServerName();
                squareTaMoreShowPop();
                break;
            case R.id.ta_task:
                //他的任务
                mIntent = new Intent(mActivity, TaTaskActivity.class);
                mIntent.putExtra(Conversion.NAME, mNickname1);
                mIntent.putExtra(Conversion.AGE, mAge);
                mIntent.putExtra(Conversion.SEX, mSex1);
                mIntent.putExtra(Conversion.LABEL, mIndustryText);
                mIntent.putExtra(Conversion.IMAGE, mHead);
                mIntent.putExtra(Conversion.USERID, mOtherId);
                //排行榜
                mIntent.putExtra(Conversion.GOLD, infoBeanGoldList);
                mIntent.putExtra(Conversion.BUY, infoBeanBuyList);
                mIntent.putExtra(Conversion.INTEGRITY, infoBeanIntegrityList);
                startActivity(mIntent);
                break;
            case R.id.ta_square:
                //他的空间
                mIntent = new Intent(mActivity, TaSquareHomepageActivity.class);
                mIntent.putExtra(Conversion.TOKEN, mId);
                startActivity(mIntent);
                break;
            case R.id.ta_square_hiscredit:
                //他的信用
                mIntent = new Intent(mActivity, TaSquareHiscreditActivity.class);
                mIntent.putExtra(Conversion.USERID, mOtherId);
                startActivity(mIntent);
                break;
            case R.id.imageHead: //查看大图
                showImageHead();
                break;
            case R.id.mi_details: //他人资料
                mIntent = new Intent(mActivity, TaSquareMessageActivity.class);
                mIntent.putExtra(Conversion.USERID, mId);
                mIntent.putExtra(Conversion.TYPE, "2");
                startActivity(mIntent);
                break;
        }
    }

    private void getServerName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBlackListFromServer = EMClient.getInstance().contactManager().getBlackListUsernames();
                if (!TextUtils.isEmpty(cellPhone) && mBlackListFromServer != null && mBlackListFromServer.size() > 0) {
                    for (String s : mBlackListFromServer) {
                        if (cellPhone.equals(s)) {
                            mContains = true;
                            return;
                        } else {
                            mContains = false;
                        }
                    }
                } else {
                    mContains = false;
                }
            }
        }).start();
    }

    private void showImageHead() {
//        final Popwindou popwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_ta_square));
//        View view = UiUtils.inflate(mActivity, R.layout.fragment_image);
//        popwindou.init(view, Gravity.CENTER, true);
//        DragPhotoView photoView = (DragPhotoView) view.findViewById(R.id.image);
//        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + mHead, photoView, mSex1);
//        photoView.setOnExitListener(new DragPhotoView.OnExitListener() {
//            @Override
//            public void onExit(DragPhotoView dragPhotoView, float v, float v1, float v2, float v3) {
//                LogUtil.d("他人头像" + v + "----" + v1 + "-----" + v2 + "------" + v3);
//                popwindou.dismiss();
//            }
//        });
//        photoView.setOnTapListener(new DragPhotoView.OnTapListener() {
//            @Override
//            public void onTap(DragPhotoView dragPhotoView) {
//                popwindou.dismiss();
//            }
//        });
        ArrayList<ImageListBean> mList = new ArrayList<>();
        ImageListBean listBean = new ImageListBean();
        listBean.setImageSrc(mHead);
        listBean.setId(0);
        mList.add(listBean);
        Intent intent = new Intent(mActivity, LargerImageActivity.class);
        intent.putExtra(Conversion.LARGERIMAGEACTIVITY, mList);
        intent.putExtra(Conversion.SHOWINDEX, 0);
        intent.putExtra(Conversion.TYPE, 0);
        mActivity.startActivity(intent);
    }

    /**
     * 他人空间弹窗
     */
    private void squareTaMoreShowPop() {
        mPopwindou = new Popwindou(mActivity, mActivity.findViewById(R.id.square_ta));
        View view = UiUtils.inflate(mActivity, R.layout.pop);
        mPopwindou.init(view, Gravity.BOTTOM, true);
        view.findViewById(R.id.item_dismis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopwindou.dismiss();
            }
        });
        TextView mPullTheBlack = (TextView) view.findViewById(R.id.item_popupwindows1);
        if (mContains) {
            mPullTheBlack.setText("取消拉黑");
        } else {
            mPullTheBlack.setText("拉黑");
        }
        mPullTheBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopwindou.dismiss();
                //第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false，则我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
                if (mContains) {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("温馨提示")
                            .setMessage("确认取消拉黑该用户吗?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        EMClient.getInstance().contactManager().removeUserFromBlackList(cellPhone);
                                        Toasty.normal(mActivity, "成功!").show();
                                        getServerName();
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("温馨提示")
                            .setMessage("确认拉黑该用户吗?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        EMClient.getInstance().contactManager().addUserToBlackList(cellPhone, false);
                                        Toasty.normal(mActivity, "成功!").show();
                                        getServerName();
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                    }
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
        });
        view.findViewById(R.id.item_popupwindows2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //举报人
                mIntent = new Intent(mActivity, ReportActivity.class);
                mIntent.putExtra(Conversion.TYPE, "2");
                startActivity(mIntent);
                mPopwindou.dismiss();
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
        mLlTaSquareTitle.setBackgroundColor(Color.argb((int) (offset * 254), 249, 249, 249));

        if (offset == 0) {
            mIvBack.setImageDrawable(Conversion.tintDrawable(mIvBackDrawable, ColorStateList.valueOf(Color.WHITE)));
        } else {
            mIvBack.setImageDrawable(Conversion.tintDrawable(mIvBackDrawable, ColorStateList.valueOf(Color.argb((int) (offset * 254), 0, 0, 0))));
        }
        //header背景图Y轴偏移
        //imgHead.setTranslationY(scrollY / 2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIvBack.setImageDrawable(Conversion.tintDrawable(mIvBackDrawable, ColorStateList.valueOf(Color.BLACK)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIvBack.setImageDrawable(Conversion.tintDrawable(mIvBackDrawable, ColorStateList.valueOf(Color.BLACK)));
    }
}
