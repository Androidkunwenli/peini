package com.jsz.peini.ui.activity.setting;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.address.CityBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

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
 * Created by th on 2016/12/10.
 */
public class MerchantsSettledActivity extends BaseActivity {
    @InjectView(R.id.setting_address)
    TextView settingAddress;
    @InjectView(R.id.setting_regist_address)
    LinearLayout settingRegistAddress;//地址
    @InjectView(R.id.setting_selectaddress)
    LinearLayout settingSelectaddress; //地址
    @InjectView(R.id.setting_nomenclature)
    EditText settingNomenclature;
    //类型
    @InjectView(R.id.setting_type)
    TextView settingType;
    @InjectView(R.id.setting_regist_type)
    LinearLayout settingRegistType;
    @InjectView(R.id.setting_name)
    EditText settingName;
    @InjectView(R.id.setting_telephone)
    EditText settingTelephone;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    /**
     * 地址列表
     */
    private List<CityBean> mAreaCity = new ArrayList<>();
    private String provinceId = "";
    private String cityId = "";


    private TextView choosePhoto;
    private TextView takePhoto;
    private TextView cancel;
    public MerchantsSettledActivity mActivity;
    private Popwindou mPop;
    private WheelView mWheelViewMain, mWheelViewSub;
    private int mPosition;
    private String provinceName;
    private String cityName;

    /*---------------------------*/

    @Override
    public int initLayoutId() {
        return R.layout.activity_mechantssettled;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("入驻申请");
        mRightButton.setText("提交");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        mAreaCity = Conversion.getCityData(mActivity);
    }

    @OnClick({R.id.setting_regist_address, R.id.setting_regist_type, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_regist_address:
                if (mAreaCity != null && mAreaCity.size() > 0) {
                    showPopwindow(settingAddress);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.setting_regist_type:
                show();
                break;
            case R.id.right_button:
                String NomenclatureText = settingNomenclature.getText().toString().trim();
                String NameText = settingName.getText().toString().trim();
                String TelephoneText = settingTelephone.getText().toString().trim();
                if (TextUtils.isEmpty(NomenclatureText)) {
                    Toasty.normal(mActivity, "请输入公司名称!").show();
                    return;
                }
                if (TextUtils.isEmpty(NameText)) {
                    Toasty.normal(mActivity, "请输入姓名!").show();
                    return;
                }
                if (TextUtils.isEmpty(TelephoneText)) {
                    Toasty.normal(mActivity, "请您输入手机号!").show();
                    return;
                }
                if (TextUtils.isEmpty(TelephoneText) || TelephoneText.length() != 11) {
                    Toasty.normal(mActivity, "请输入正确手机号手机号码!").show();
                    return;
                }
                if (TextUtils.isEmpty(provinceId)) {
                    Toasty.normal(mActivity, "请选择城市!").show();
                    return;
                }
                if (TextUtils.isEmpty(cityId)) {
                    Toasty.normal(mActivity, "请选择城市!").show();
                    return;
                }
                initNetWork(provinceId, cityId, NomenclatureText, tag, NameText, TelephoneText);
                break;
        }
    }

    private void initNetWork(String cityNeme1, String cityNeme, String neme, String trim1, String s, String trim) {
        RetrofitUtil.createService(SettingService.class).sellerJoin(mUserToken, cityNeme, cityNeme1, neme, trim1, s, trim)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, body.getResultDesc()).show();
                                finish();
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {

                    }
                });
    }

    /**
     * 自定义控件  弹窗
     */
    String tag;

    public void show() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.dialog_layout, null);
        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        cancel = (TextView) inflate.findViewById(R.id.btn_cancel);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.i("选择", "" + 1);
                settingType.setText("商户");
                dialog.dismiss();
                tag = "1";
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.i("选择", "" + 2);
                settingType.setText("个体");
                dialog.dismiss();
                tag = "2";
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(inflate);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 两级列表联动
     *
     * @param textView
     */
    private void showPopwindow(final TextView textView) {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_mechantssettled));
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
                textView.setText(provinceName + " " + cityName);
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
        mWheelViewMain.setWheelData(addressMainDatas());
        mWheelViewMain.setStyle(style);
        mWheelViewMain.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mPosition = position;
                provinceName = (String) o;
                provinceId = String.valueOf(mAreaCity.get(position).getId());
                LogUtil.d("城市" + provinceName + provinceId);


            }
        });

        /**第二级*/
        mWheelViewSub.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelViewSub.setSkin(WheelView.Skin.Holo);
        mWheelViewSub.setWheelSize(5);
        mWheelViewSub.setStyle(style);
        mWheelViewSub.setWheelData(createSubDatas().get(addressMainDatas().get(mWheelViewMain.getSelection())));
        mWheelViewMain.join(mWheelViewSub);
        mWheelViewMain.joinDatas(createSubDatas());
        mWheelViewSub.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                cityName = (String) o;
                cityId = String.valueOf(mAreaCity.get(mPosition).getProvinceObject().get(position).getId());
                LogUtil.d("市区" + cityName + cityId);
            }
        });

    }

    /**
     * 地址的第一集
     */
    private List<String> addressMainDatas() {
        ArrayList<String> mStringList = new ArrayList<>();
        mStringList.clear();
        for (int i = 0; i < mAreaCity.size(); i++) {
            mStringList.add(mAreaCity.get(i).getProvinceName());
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
    private HashMap<String, List<String>> createSubDatas() {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        String[] strings = new String[mAreaCity.size()];
        String[][] ss = new String[mAreaCity.size()][];
        for (int i = 0; i < mAreaCity.size(); i++) {
            CityBean mAreaCityBean = mAreaCity.get(i);
            strings[i] = (mAreaCityBean.getProvinceName());
            ss[i] = new String[mAreaCityBean.getProvinceObject().size()];
            List<CityBean.ProvinceObjectBean> mProvinceObject = mAreaCityBean.getProvinceObject();
            for (int j = 0; j < mProvinceObject.size(); j++) {
                ss[i][j] = mProvinceObject.get(j).getCityName();
            }
        }
        for (int i = 0; i < strings.length; i++) {
            map.put(strings[i], Arrays.asList(ss[i]));
        }
        return map;
    }
}
