package com.jsz.peini.ui.activity.square;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.address.CityBean;
import com.jsz.peini.model.address.PersonalDataBean;
import com.jsz.peini.model.square.UserInfoCodesBean;
import com.jsz.peini.model.square.UserInfoIdBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.ui.view.square.MessageProgressBar;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/4.
 */
public class TaSquareMessageActivity extends BaseActivity {

    @InjectView(R.id.msg_progress_bar)
    MessageProgressBar mMsgProgressBar;
    @InjectView(R.id.tv_nickname)
    TextView mTvNickname;
    @InjectView(R.id.tv_sex)
    TextView mTvSex;
    @InjectView(R.id.tv_age)
    TextView mTvAge;
    @InjectView(R.id.tv_height)
    TextView mTvHeight;
    @InjectView(R.id.tv_weight)
    TextView mTvWeight;
    @InjectView(R.id.tv_constellation)
    TextView mTvConstellation;
    @InjectView(R.id.tv_nation)
    TextView mTvNation;
    @InjectView(R.id.tv_emotion)
    TextView mTvEmotion;
    @InjectView(R.id.tv_income)
    TextView mTvIncome;
    @InjectView(R.id.tv_degree)
    TextView mTvDegree;
    @InjectView(R.id.tv_industry)
    TextView mTvIndustry;
    @InjectView(R.id.tv_house)
    TextView mTvHouse;
    @InjectView(R.id.tv_car)
    TextView mTvCar;
    @InjectView(R.id.tv_location)
    TextView mTvLocation;
    @InjectView(R.id.tv_hometown)
    TextView mTvHometown;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;

    private Activity mActivity;
    private Popwindou mPop;
    private WheelView mWheelView, mWheelViewMain, mWheelViewSub;

    private int mSign;

    private String mO;
    private int mSignAddress;
    /**
     * 地址
     */
    private List<CityBean> mAreaCity;
    /**
     * 基本资料信息
     */
    private List<PersonalDataBean> personalDataBeen = new ArrayList<>();
    private List<PersonalDataBean.NationListBean> mNationList;
    private List<PersonalDataBean.ConstellationListBean> mConstellationList;
    private List<PersonalDataBean.EmotionListBean> mEmotionList;
    private List<PersonalDataBean.DegreeListBean> mDegreeList;
    private List<PersonalDataBean.IndustryListBean> mIndustryList;
    private List<PersonalDataBean.HouseListBean> mHouseList;
    private List<PersonalDataBean.CarListBean> mCarList;


    private List<CityBean.ProvinceObjectBean> mProvinceObject;
    private CityBean mAreaCityBean;

    private int mPosition;
    //体重
    private String mWide = "";
    //身高
    private String mMHighNum;

    //地址
    private String mCurrentProvinceName = "";
    private String mCurrentProvinceId = "";
    private String mCurrentCityName = "";
    private String mCurrentCityId = "";
    private String mHometownProvinceName = "";
    private String mHometownProvinceId = "";
    private String mHometownCityName = "";
    private String mHometownCityId = "";
    private int mmConstellationListNum;
    private int mNationListNum;
    private int mEmotionListNum;
    //小收入
    private int mSmallIncome;
    //大收入
    private int mBigIncome;
    private int mDegreeListNum;
    private int mIndustryListNum;
    private int mHouseListNum;
    private int mCarListNum;

    private String mUserId;

    @Override
    public int initLayoutId() {
        return R.layout.activity_tasquare_message;
    }

    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        String type = getIntent().getStringExtra(Conversion.TYPE);
        mUserId = getIntent().getStringExtra(Conversion.USERID);
        switch (type) {
            case "1":
                mRightButton.setText("编辑");
                break;
            default:
                mRightButton.setText("");
                mRightButton.setVisibility(View.GONE);
                break;
        }
//        mToolbar.setBackgroundResource(R.color.RED_FB4E30);
//        mKuntoolbar.setBackgroundResource(R.color.RED_FB4E30);
//        mTitle.setTextColor(getResources().getColor(R.color.white000));
//        mRightButton.setTextColor(getResources().getColor(R.color.white000));
        inItNetWork();
    }

    /**
     * 获取网络信息数据
     */
    private void inItNetWork() {
//        //数据类型获取
//        RetrofitUtil.createService(SquareService.class)
//                .getUserInfoCodes()
//                .enqueue(new Callback<UserInfoCodesBean>() {
//                    @Override
//                    public void onResponse(Call<UserInfoCodesBean> call, Response<UserInfoCodesBean> response) {
//                        if (response.isSuccessful()) {
//                            int resultCode = response.body().getResultCode();
//                            if (resultCode == 1) {
//                                mUserInfoCodesResponse = response.body();
//                            } else if (resultCode == 9) {
//                                LoginDialogUtils.isNewLogin(mActivity);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserInfoCodesBean> call, Throwable t) {
//                    }
//                });
        /**
         *地址数据
         */
        mAreaCity = Conversion.getCityData(mActivity);
        /**
         *其他数据
         */
        try {
            String json = Conversion.toString(mActivity.getAssets().open("PersonalData.json"));
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                String toString = jsonArray.getJSONObject(i).toString().trim();
                personalDataBeen.add(new Gson().fromJson(toString, PersonalDataBean.class));
                System.out.println("解析出来的: " + toString);
            }
            mNationList = personalDataBeen.get(0).getNationList();
            mConstellationList = personalDataBeen.get(0).getConstellationList();
            mEmotionList = personalDataBeen.get(0).getEmotionList();
            mDegreeList = personalDataBeen.get(0).getDegreeList();
            mIndustryList = personalDataBeen.get(0).getIndustryList();
            mHouseList = personalDataBeen.get(0).getHouseList();
            mCarList = personalDataBeen.get(0).getCarList();

        } catch (Exception e) {
            e.printStackTrace();
            Toasty.normal(mActivity, "系统数据格式解析错误").show();
            finish();
        }

        getUserInfo();

    }

    private void getUserInfo() {
        //资料获取
        RetrofitUtil.createService(SquareService.class)
                .getUserInfo(StringUtils.isNull(mUserId) ? mUserToken : mUserId)
                .enqueue(new Callback<UserInfoIdBean>() {
                    @Override
                    public void onResponse(Call<UserInfoIdBean> call, final Response<UserInfoIdBean> response) {
                        if (response.isSuccessful()) {
                            LogUtil.d("我的资料== " + response.body().toString());
                            int resultCode = response.body().getResultCode();
                            if (resultCode == 1) {
                                updateUi(response.body().getUserInfo());
                            } else if (resultCode == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserInfoIdBean> call, Throwable t) {
                    }
                });
    }

    /**
     * 他人信息信息的填充
     */
    private void updateUi(UserInfoIdBean.UserInfoBean userInfo) {
        if (userInfo == null) {
            LogUtil.d(getLocalClassName(), "他人资料信息为空" + userInfo.toString());
            return;
        }
        LogUtil.d(getLocalClassName(), "" + userInfo.toString()); //他人资料数据

        String nickname = userInfo.getNickname(); //用户名字
        if (StringUtils.isNoNull(nickname)) {
            mTvNickname.setText(nickname);
            mTitle.setText(nickname);
        }
        int sex = userInfo.getSex();
        mTvSex.setText(sex == 1 ? "男" : "女");

        String age = String.valueOf(userInfo.getAge()); //年龄
        if (StringUtils.isNoNull(age) && !"0".equals(age)) {
            mTvAge.setText(age + "岁");
        }
        mMHighNum = userInfo.getHeight(); //身高
        if (StringUtils.isNoNull(mMHighNum) && !"0".equals(mMHighNum)) {
            mTvHeight.setText(mMHighNum + "cm");
        } else {
            mMHighNum = "";
        }
        mWide = userInfo.getWeight();//体重
        if (StringUtils.isNoNull(mWide) && !"0".equals(mWide)) {
            mTvWeight.setText(mWide + "kg");
        } else {
            mWide = "";
        }

        String nowProvinceText = userInfo.getNowProvinceText(); //地址
        String nowCityText = userInfo.getNowCityText();
        String nowCountyText = userInfo.getNowCountyText();
        mCurrentProvinceId = String.valueOf(userInfo.getNowProvince());
        mCurrentCityId = String.valueOf(userInfo.getNowCity());

        StringBuilder locationStringBuilder = new StringBuilder();
        if (StringUtils.isNoNull(nowProvinceText)) {
            locationStringBuilder.append(nowProvinceText);
        }
        if (StringUtils.isNoNull(nowCityText)) {
            locationStringBuilder.append(nowCityText);
        }
        if (StringUtils.isNoNull(nowCountyText)) {
            locationStringBuilder.append(nowCountyText);
        }
        mTvLocation.setText(locationStringBuilder.toString());

        String oldProvinceText = userInfo.getOldProvinceText(); //故乡
        String oldCityText = userInfo.getOldCityText();
        String oldCountyText = userInfo.getOldCountyText();
        mHometownProvinceId = String.valueOf(userInfo.getOldProvince());
        mHometownCityId = String.valueOf(userInfo.getOldCity());

        StringBuilder hometownStringBuilder = new StringBuilder();
        if (StringUtils.isNoNull(oldProvinceText)) {
            hometownStringBuilder.append(oldProvinceText);
        }
        if (StringUtils.isNoNull(oldCityText)) {
            hometownStringBuilder.append(oldCityText);
        }
        if (StringUtils.isNoNull(oldCountyText)) {
            hometownStringBuilder.append(oldCountyText);
        }
        mTvHometown.setText(hometownStringBuilder.toString());

        String constellationText = userInfo.getConstellationText(); //星座
        mmConstellationListNum = userInfo.getConstellation();
        if (StringUtils.isNoNull(constellationText)) {
            mTvConstellation.setText(constellationText);
        }

        String nationText = userInfo.getNationText();//汉族
        mNationListNum = userInfo.getNation();
        if (StringUtils.isNoNull(nationText)) {
            mTvNation.setText(nationText);
        }

        String emotionText = userInfo.getEmotionText();//单身
        mEmotionListNum = userInfo.getEmotion();
        if (StringUtils.isNoNull(emotionText)) {
            mTvEmotion.setText(emotionText);
        }

        String degreeText = userInfo.getDegreeText();//小学
        mDegreeListNum = userInfo.getDegree();
        if (StringUtils.isNoNull(degreeText)) {
            mTvDegree.setText(degreeText);
        }

        String industryText = userInfo.getIndustryText(); //行业
        mIndustryListNum = userInfo.getIndustry();
        if (StringUtils.isNoNull(industryText)) {
            mTvIndustry.setText(industryText);
        }

        String isHouseText = (String) userInfo.getIsHouseText();//购房
        mHouseListNum = userInfo.getIsHouse();
        if (StringUtils.isNoNull(isHouseText)) {
            mTvHouse.setText(isHouseText);
        }
        String isCarText = userInfo.getIsCarText(); //购车
        mCarListNum = userInfo.getIsCar();
        if (StringUtils.isNoNull(isCarText)) {
            mTvCar.setText(isCarText);
        }

        mSmallIncome = userInfo.getSmallIncome(); //收入
        mBigIncome = userInfo.getBigIncome();//收入高

        if (mSmallIncome != 0 && mBigIncome != 0) {
            if (mSmallIncome < mBigIncome) {
                mTvIncome.setText(mSmallIncome + "元 - " + mBigIncome + "元");
            } else if (mSmallIncome == mBigIncome) {
                if (mSmallIncome == 30001) {
                    mTvIncome.setText("30000元以上");
                } else if (mSmallIncome == 2000) {
                    mTvIncome.setText("2000元以下");
                } else {
                    mTvIncome.setText(mSmallIncome + "元");
                }
            }
        }
        int selfNum = userInfo.getSelfNum(); //资料完整度
        if (!TextUtils.isEmpty(String.valueOf(selfNum)) && selfNum != 0) {
            mMsgProgressBar.setProgress(selfNum);
        }
    }

    /**
     * 我的空间点击事件
     */
    @OnClick({R.id.ll_emotion, R.id.ll_nation, R.id.ll_height, R.id.ll_weight, R.id.ll_hometown,
            R.id.ll_income, R.id.ll_location, R.id.ll_house, R.id.ll_nickname,
            R.id.ll_industry, R.id.ll_degree, R.id.ll_constellation, R.id.ll_car,
            R.id.right_button, R.id.toolbar, R.id.ll_sex, R.id.ll_age})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_age:
                if (isCanEdit) {
                    return;
                }
                Toasty.normal(mActivity, "年龄一经注册不得修改!").show();
                break;
            case R.id.ll_sex:
                if (isCanEdit) {
                    return;
                }
                Toasty.normal(mActivity, "性别一经注册不得修改!").show();
                break;
            case R.id.ll_emotion://情感
                if (isCanEdit) {
                    return;
                }
                mSign = 1;
                PopWindowsWheelView(mTvEmotion);
                break;
            case R.id.ll_nation://民族
                if (isCanEdit) {
                    return;
                }
                mSign = 2;
                PopWindowsWheelView(mTvNation);
                break;
            case R.id.ll_height://身高
                if (isCanEdit) {
                    return;
                }
                mSign = 3;
                PopWindowsWheelView(mTvHeight);
                break;
            case R.id.ll_weight://体重
                if (isCanEdit) {
                    return;
                }
                mSign = 9;
                PopWindowsWheelView(mTvWeight);
                break;
            case R.id.ll_car://购车
                if (isCanEdit) {
                    return;
                }
                mSign = 4;
                PopWindowsWheelView(mTvCar);
                break;
            case R.id.ll_constellation://星座
                if (isCanEdit) {
                    return;
                }
                mSign = 5;
                PopWindowsWheelView(mTvConstellation);
                break;
            case R.id.ll_degree://学历
                if (isCanEdit) {
                    return;
                }
                mSign = 6;
                PopWindowsWheelView(mTvDegree);
                break;
            case R.id.ll_industry://行业
                if (isCanEdit) {
                    return;
                }
                mSign = 7;
                PopWindowsWheelView(mTvIndustry);
                break;
            case R.id.ll_house://房子
                if (isCanEdit) {
                    return;
                }
                mSign = 8;
                PopWindowsWheelView(mTvHouse);
                break;
            case R.id.ll_location: //所在地
                if (isCanEdit) {
                    return;
                }
                if (mAreaCity.size() == 0) {
                    return;
                }
                mSignAddress = 1;
                PopWindowsWheelViewTow(mTvLocation);
                break;
            case R.id.ll_hometown://故乡
                if (isCanEdit) {
                    return;
                }
                if (mAreaCity.size() == 0) {
                    return;
                }
                mSignAddress = 3;
                PopWindowsWheelViewTow(mTvHometown);
                break;
            case R.id.ll_income://月收入
                if (isCanEdit) {
                    return;
                }
                mSignAddress = 2;
                PopWindowsWheelViewTow(mTvIncome);
                break;
            case R.id.ll_nickname://昵称
                if (isCanEdit) {
                    return;
                }
//                mIntent = new Intent(mActivity, NickNameActivity.class);
//                mIntent.putExtra("type", "2");
//                mIntent.putExtra("title", "修改昵称");
//                startActivityForResult(mIntent, 1);
                NickNameActivity.actionShow(mActivity, 2, mTvNickname.getText().toString().trim());
                break;
            case R.id.right_button://提交
                if (isCanEdit) {
                    mRightButton.setText("完成");
                    isCanEdit = false;
                } else {
                    SubmitMiData();
                }
                break;
            case R.id.toolbar:
                finish();
                break;
        }
    }

    boolean isCanEdit = true;

    private void SubmitMiData() {
        mDialog.show();
        String name = mTvNickname.getText().toString().trim();
        RetrofitUtil.createService(SquareService.class)
                .updateUserInfo(
                        mUserToken,
                        name,
                        "",
                        mMHighNum,
                        mWide,
                        mCurrentProvinceId,
                        mCurrentCityId,
                        "",
                        mHometownProvinceId,
                        mHometownCityId,
                        "",
                        String.valueOf(mmConstellationListNum),
                        String.valueOf(mNationListNum),
                        String.valueOf(mEmotionListNum),
                        String.valueOf(mSmallIncome),
                        String.valueOf(mBigIncome),
                        String.valueOf(mDegreeListNum),
                        String.valueOf(mIndustryListNum),
                        String.valueOf(mHouseListNum),
                        String.valueOf(mCarListNum)
                ).enqueue(new Callback<UserInfoCodesBean>() {
            @Override
            public void onResponse(Call<UserInfoCodesBean> call, Response<UserInfoCodesBean> response) {
                if (response.isSuccessful()) {
                    mDialog.dismiss();
                    UserInfoCodesBean body = response.body();
                    int resultCode = body.getResultCode();
                    if (resultCode == 1) {
                        isCanEdit = true;
                        mRightButton.setText("编辑");
                        getUserInfo();
                    } else if (resultCode == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    } else {
                        Toasty.normal(mActivity, body.getResultDesc()).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoCodesBean> call, Throwable t) {
                Toasty.error(mActivity, Conversion.NETWORKERROR).show();
                mDialog.dismiss();
            }
        });
    }

    /**
     * 两级列表联动
     *
     * @param textView
     */
    private void PopWindowsWheelViewTow(final TextView textView) {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_tasquare_message));
        View viewTow = UiUtils.inflate(mActivity, R.layout.pop_tow_selector);
        mPop.init(viewTow, Gravity.BOTTOM, true);
        viewTow.findViewById(R.id.cancel_selector).setOnClickListener(new View.OnClickListener() { //取消
            @Override
            public void onClick(View view) {
                mPop.dismiss();
            }
        });
        viewTow.findViewById(R.id.ok_selector).setOnClickListener(new View.OnClickListener() { //确定
            @Override
            public void onClick(View view) {

                if (mSignAddress == 2) {
                    if (mSmallIncome < mBigIncome) {
                        mTvIncome.setText(mSmallIncome + "元 - " + mBigIncome + "元");
                    } else if (mSmallIncome == mBigIncome) {
                        if (mSmallIncome == 30001) {
                            mTvIncome.setText("30000元以上");
                        } else if (mSmallIncome == 2000) {
                            mTvIncome.setText("2000元以下");
                        } else {
                            mTvIncome.setText(mSmallIncome + "元");
                        }
                    }
                } else if (mSignAddress == 3) {
                    textView.setText(mHometownProvinceName + "" + mHometownCityName);
                } else {
                    textView.setText(mCurrentProvinceName + "" + mCurrentCityName);
                }
                mPop.dismiss();
            }
        });
        mWheelViewMain = (WheelView) viewTow.findViewById(R.id.main_wheelview);
        mWheelViewSub = (WheelView) viewTow.findViewById(R.id.sub_wheelview);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.backgroundf1f1f1);
        style.selectedTextSize = 20;
        style.textSize = 18;
        style.textAlpha = 0.5f;
        style.selectedTextZoom = 5;
        /**第一级*/
        mWheelViewMain.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelViewMain.setSkin(WheelView.Skin.Holo);
        mWheelViewMain.setWheelSize(5);
        mWheelViewMain.setWheelData(addressMainData());
        mWheelViewMain.setStyle(style);
        mWheelViewMain.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mPosition = position;
                if (mSignAddress == 2) {
                    if (position == mWheelViewMain.getWheelCount() - 1) {
                        mSmallIncome = 30001;
                    } else {
                        mSmallIncome = (position + 2) * 1000;
                    }
                    if (position == 0) {
                        mWheelViewSub.smoothScrollToPosition(0);
                    } else if (position > mWheelViewSub.getCurrentPosition()) {
                        mWheelViewSub.smoothScrollToPosition(position + 4);
                    }

                } else if (mSignAddress == 3) { //故乡
                    mHometownProvinceName = (String) o;
                    mHometownProvinceId = String.valueOf(mAreaCity.get(position).getProvinceId());
                } else {
                    mCurrentProvinceName = (String) o;
                    mCurrentProvinceId = String.valueOf(mAreaCity.get(position).getProvinceId());
                }

            }
        });

        /**第二级*/
        mWheelViewSub.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelViewSub.setSkin(WheelView.Skin.Holo);
        mWheelViewSub.setWheelSize(5);
        mWheelViewSub.setStyle(style);
        if (mSignAddress == 2) {
            mWheelViewSub.setWheelData(addressMainData());
        } else {
            mWheelViewSub.setWheelData(createSubData().get(addressMainData().get(mWheelViewMain.getSelection())));
            mWheelViewMain.join(mWheelViewSub);
            mWheelViewMain.joinDatas(createSubData());
        }
        mWheelViewSub.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {

                if (mSignAddress == 2) {
                    if (position == mWheelViewSub.getWheelCount() - 1) {
                        mSmallIncome = 30001;
                        mBigIncome = 30001;
                    } else {
                        mBigIncome = (position + 2) * 1000;
                    }
                    if (mWheelViewMain.getCurrentPosition() == 0) {
                        mWheelViewSub.smoothScrollToPosition(0);
                    } else if (position < mWheelViewMain.getCurrentPosition()) {
                        mWheelViewSub.smoothScrollToPosition(mWheelViewMain.getCurrentPosition() + 4);
                    }
                } else if (mSignAddress == 3) { //故乡
                    mHometownCityName = (String) o;
                    mHometownCityId = String.valueOf(mAreaCity.get(mPosition).getProvinceObject().get(position).getCityId());
                } else {
                    mCurrentCityName = (String) o;
                    mCurrentCityId = String.valueOf(mAreaCity.get(mPosition).getProvinceObject().get(position).getCityId());
                }
            }
        });

    }


    /**
     * 单个列表联动
     */
    private void PopWindowsWheelView(final TextView emotionText) {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_tasquare_message));
        View view = UiUtils.inflate(mActivity, R.layout.pop_one_selector);
        mPop.init(view, Gravity.BOTTOM, true);
        view.findViewById(R.id.cancel_selector).setOnClickListener(new View.OnClickListener() { //取消
            @Override
            public void onClick(View view) {
                mPop.dismiss();
            }
        });
        view.findViewById(R.id.ok_selector).setOnClickListener(new View.OnClickListener() { //确定
            @Override
            public void onClick(View view) {
                emotionText.setText(mO);
                mPop.dismiss();
            }
        });
        mWheelView = (WheelView) view.findViewById(R.id.one_wheelview);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.backgroundf1f1f1);
        style.selectedTextSize = 20;
        style.textSize = 18;
        style.textAlpha = 0.5f;
        style.selectedTextZoom = 5;
        mWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelView.setSkin(WheelView.Skin.Holo);
        mWheelView.setWheelSize(5);
        mWheelView.setWheelData(createMainData());
        mWheelView.setStyle(style);
        mWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                switch (mSign) {
                    case 1:
                        mEmotionListNum = mEmotionList.get(position).getCodesNum();
                        mO = (String) o;
                        break;
                    case 2:
                        mNationListNum = mNationList.get(position).getCodesNum();
                        mO = (String) o;
                        break;
                    case 3:
                        mMHighNum = position + 100 + "";
                        mO = (String) o;
                        break;
                    case 4:
                        mCarListNum = mCarList.get(position).getCodesNum();
                        mO = (String) o;
                        break;
                    case 5:
                        mmConstellationListNum = mConstellationList.get(position).getCodesNum();
                        mO = (String) o;
                        break;
                    case 6:
                        mDegreeListNum = mDegreeList.get(position).getCodesNum();
                        mO = (String) o;
                        break;
                    case 7:
                        mIndustryListNum = mIndustryList.get(position).getCodesNum();
                        mO = (String) o;
                        break;
                    case 8:
                        mHouseListNum = mHouseList.get(position).getCodesNum();
                        mO = (String) o;
                        break;
                    case 9:
                        mWide = (position + 30) + "";
                        mO = (String) o;
                        break;

                }
            }
        });
    }


    List<String> mStringList;

    private List createMainData() {
        mStringList = new ArrayList<>();
        mStringList.clear();
        switch (mSign) {
            case 1:
                for (int i = 0; i < mEmotionList.size(); i++) {
                    mStringList.add(mEmotionList.get(i).getCodesName());
                }
                break;
            case 2:
                for (int i = 0; i < mNationList.size(); i++) {
                    mStringList.add(mNationList.get(i).getCodesName());
                }
                break;
            case 3:
                for (int i = 100; i < 190; i++) {
                    mStringList.add(i + "cm");
                }
                break;
            case 4:
                for (int i = 0; i < mCarList.size(); i++) {
                    mStringList.add(mCarList.get(i).getCodesName());
                }
                break;
            case 5:
                for (int i = 0; i < mConstellationList.size(); i++) {
                    mStringList.add(mConstellationList.get(i).getCodesName());
                }
                break;
            case 6:
                for (int i = 0; i < mDegreeList.size(); i++) {
                    mStringList.add(mDegreeList.get(i).getCodesName());
                }
                break;
            case 7:
                for (int i = 0; i < mIndustryList.size(); i++) {
                    mStringList.add(mIndustryList.get(i).getCodesName());
                }
                break;
            case 8:
                for (int i = 0; i < mHouseList.size(); i++) {
                    mStringList.add(mHouseList.get(i).getCodesName());
                }
                break;
            case 9:
                for (int i = 30; i <= 1400; i++) {
                    mStringList.add(i + "kg");
                }
                break;
        }
        if (mStringList.size() > 0) {
            return mStringList;
        } else {
            return null;
        }
    }

    /**
     * 地址的第一集
     */
    private List addressMainData() {
        mStringList = new ArrayList<>();
        mStringList.clear();
        switch (mSignAddress) {
            case 1:
                for (int i = 0; i < mAreaCity.size(); i++) {
                    mStringList.add(mAreaCity.get(i).getProvinceName());
                }
                break;
            case 3:
                for (int i = 0; i < mAreaCity.size(); i++) {
                    mStringList.add(mAreaCity.get(i).getProvinceName());
                }
                break;
            case 2:
                for (int i = 2; i <= 31; i++) {
                    if (i == 31) {
                        mStringList.add("30000元以上");
                    } else if (i == 2) {
                        mStringList.add("2000元以下");
                    } else {
                        mStringList.add(i + "000元");
                    }
                }
                break;
        }
        if (mStringList.size() > 0) {
            return mStringList;
        } else {
            return null;
        }
    }

    /**
     * 地址联动第二级
     */

    private HashMap<String, List<String>> createSubData() {
        HashMap<String, List<String>> map = new HashMap<>();
        String[] strings = new String[mAreaCity.size()];
        String[][] ss = new String[mAreaCity.size()][];
        for (int i = 0; i < mAreaCity.size(); i++) {
            mAreaCityBean = mAreaCity.get(i);
            strings[i] = (mAreaCityBean.getProvinceName());
            ss[i] = new String[mAreaCityBean.getProvinceObject().size()];
            mProvinceObject = mAreaCityBean.getProvinceObject();
            for (int j = 0; j < mProvinceObject.size(); j++) {
                ss[i][j] = mProvinceObject.get(j).getCityName();
            }
        }
        for (int i = 0; i < strings.length; i++) {
            map.put(strings[i], Arrays.asList(ss[i]));
        }
        return map;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 0 && data != null) {
            String name = data.getStringExtra("name");
            if (StringUtils.isNoNull(name)) {
                mTvNickname.setText(name);
                mTitle.setText(name);
            }
        }
    }

}
