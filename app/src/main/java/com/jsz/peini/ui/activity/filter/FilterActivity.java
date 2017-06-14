package com.jsz.peini.ui.activity.filter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.CrtyBean;
import com.jsz.peini.model.eventbus.FilterReturnBean;
import com.jsz.peini.model.filter.FilterRecycleviewBean;
import com.jsz.peini.model.seller.SellerCodesBySellerCodesBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.seller.SellerService;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.ToastUtils;
import com.jsz.peini.utils.UiUtils;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.zhy.view.flowlayout.TagAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FilterActivity extends BaseActivity {

    @InjectView(R.id.filter_sort_distance)
    RadioButton mFilterSortDistance;
    @InjectView(R.id.filter_sort_time)
    RadioButton mFilterSortTime;
    @InjectView(R.id.filter_sort)
    RadioGroup mFilterSort;
    @InjectView(R.id.task_sex_no)
    RadioButton mTaskSexNo;
    @InjectView(R.id.task_sex_man)
    RadioButton mTaskSexMan;
    @InjectView(R.id.task_sex_woman)
    RadioButton mTaskSexWoman;
    @InjectView(R.id.task_sex)
    RadioGroup mTaskSex;
    @InjectView(R.id.filter_age)
    TextView mFilterAge;
    @InjectView(R.id.filter_age_ll)
    LinearLayout mFilterAgeLl;
    @InjectView(R.id.filter_height)
    TextView mFilterHeight;
    @InjectView(R.id.filter_height_ll)
    LinearLayout mFilterHeightLl;
    @InjectView(R.id.filter_address)
    TextView mFilterAddress;
    @InjectView(R.id.filter_address_ll)
    LinearLayout mFilterAddressLl;
    @InjectView(R.id.filter_ideoauthentication)
    CheckBox mFilterIdeoauthentication;
    @InjectView(R.id.filter_identityauthentication)
    CheckBox mFilterIdentityauthentication;
    @InjectView(R.id.activity_filter)
    LinearLayout mActivityFilter;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.filter_recycleview)
    RecyclerView mFilterRecycleview;
    private TagAdapter<String> mAdapter;//标签的适配器
    ArrayList<String> mList;

    /**
     * sortsort	Int		排序方式
     * （1时间（默认1）2距离）
     * otherSex	Int		性别（不限不传1男2女）
     * otherLowAge	Int		年龄最低限（没有不传）
     * otherHignAge	Int		年龄最高限（没有不传）
     * otherLowheight	Int		身高最低限（没有不传）
     * otherHignheight	Int		身高最高限（没有不传）
     * isVideo	Int		是否视频验证（1或不传）
     * isIdcard	Int		是否身份认证（1或不传）
     */
    String sort = "1";
    String otherSex = "";
    String otherLowAge = "";
    String otherHignAge = "";
    String otherLowheight = "";
    String otherHignheight = "";
    String isVideo = "";
    String isIdcard = "";
    public CrtyBean mCrtyBean;
    public FilterActivity mActivity;
    public int mMinimumAge;
    public int mMaximumAge;
    public int mPosition = 0;
    public int mPosition1 = 0;
    private String mTaskFilter = "";
    private int mProvinceId;
    private int mPosition2;
    private int mCityId;


    ArrayList<FilterRecycleviewBean> mBeanList = new ArrayList<>();
    private ArrayList<String> mArrayList;
    private ArrayList<Integer> mArrayAgeList;

    @Override
    public int initLayoutId() {
        return R.layout.activity_filter;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("筛选条件");
        mRightButton.setText("确定");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayList<FilterRecycleviewBean> filter = getIntent().getParcelableArrayListExtra("filter");
        if (null != filter && filter.size() > 0) {
            mBeanList.clear();
            mBeanList.addAll(filter);
        }
    }

    @Override
    public void initData() {
        initNetWork();
        initIalise();
    }

    private void initNetWork() {
        initCity();
        if (null != mBeanList && mBeanList.size() > 0) {
            initLabel();
            StringBuffer buffer = new StringBuffer();
                        /*写一个循环*/
            for (FilterRecycleviewBean recycleviewBean : mBeanList) {
                if (recycleviewBean.getInt() == 1) {
                    buffer.append(recycleviewBean.getId() + ",");
                }
            }
            mTaskFilter = buffer.toString();
            LogUtil.d("这个是筛选的东西" + mTaskFilter);
        } else {
            sellerCode();
        }
    }

    /**
     * 访问城市
     */
    private void initCity() {
        RetrofitUtil.createService(SettingService.class)
                .getCityList()
                .enqueue(new Callback<CrtyBean>() {
                    @Override
                    public void onResponse(Call<CrtyBean> call, Response<CrtyBean> response) {
                        if (response.isSuccessful()) {
                            CrtyBean body = response.body();
                            if (body.getResultCode() == 1) {
                                LogUtil.d("地址列表请求成功" + response.body().toString());
                                mCrtyBean = response.body();
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CrtyBean> call, Throwable t) {
                        LogUtil.d("地址列表请求失败" + t.getMessage());
                    }
                });
    }

    private void initIalise() {

        mFilterSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.filter_sort_distance:
                        LogUtil.i("筛选的距离", "这个是距离");
                        sort = "2";
                        break;
                    case R.id.filter_sort_time:
                        LogUtil.i("筛选的时间", "这个是时间");
                        sort = "1";
                        break;
                }
            }
        });
        mTaskSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.task_sex_no:
                        LogUtil.i("筛选的不限", "这个是不限");
                        otherSex = "";
                        break;
                    case R.id.task_sex_man:
                        LogUtil.i("筛选的性别", "这个是男");
                        otherSex = "1";
                        break;
                    case R.id.task_sex_woman:
                        LogUtil.i("筛选的性别", "这个是女");
                        otherSex = "2";
                        break;
                }
            }
        });
        mFilterIdeoauthentication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LogUtil.i("这个是视频认证", "选择了还是没有" + b);
                if (b) {
                    isVideo = "1";
                } else {
                    isVideo = "";
                }
            }
        });
        mFilterIdentityauthentication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LogUtil.i("这个是身份认证认证", "选择了还是没有" + b);
                if (b) {
                    isIdcard = "1";
                } else {
                    isIdcard = "";
                }

            }
        });
    }

    private void sellerCode() {
        RetrofitUtil.createService(SellerService.class)
                .getSellerCodesBySellerCodesBean()
                .enqueue(new Callback<SellerCodesBySellerCodesBean>() {
                    @Override
                    public void onResponse(Call<SellerCodesBySellerCodesBean> call, Response<SellerCodesBySellerCodesBean> response) {
                        if (response.isSuccessful()) {
                            SellerCodesBySellerCodesBean body = response.body();
                            if (body.getResultCode() == 1) {
                                List<SellerCodesBySellerCodesBean.SellerCodesBean> sellerCodes = body.getSellerCodes();
                                for (int i = 0; i < sellerCodes.size(); i++) {
                                    mBeanList.add(new FilterRecycleviewBean(i == 0 ? 1 : 0, sellerCodes.get(i).getName(), sellerCodes.get(i).getId()));
                                }
                                initLabel();
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SellerCodesBySellerCodesBean> call, Throwable t) {

                    }
                });
    }

    private void initLabel() {
        mFilterRecycleview.setLayoutManager(new GridLayoutManager(mActivity, 4));
        mFilterRecycleview.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.tv, parent));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                holder.setIsRecyclable(false);
                final ViewHolder viewHolder = (ViewHolder) holder;
                final FilterRecycleviewBean filterRecycleviewBean = mBeanList.get(position);
                viewHolder.mFilter_name.setText(filterRecycleviewBean.getString());
                if (filterRecycleviewBean.getInt() == 0) {
                    viewHolder.mFilter_name.setChecked(false);
                } else if (filterRecycleviewBean.getInt() == 1) {
                    viewHolder.mFilter_name.setChecked(true);
                }
                viewHolder.mFilter_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (position == 0) {
                            if (isChecked) {
                                mBeanList.set(position, new FilterRecycleviewBean(1, filterRecycleviewBean.getString(), filterRecycleviewBean.getId()));
                            } else {
                                mBeanList.set(position, new FilterRecycleviewBean(0, filterRecycleviewBean.getString(), filterRecycleviewBean.getId()));
                            }
                        } else {
                            mBeanList.set(0, new FilterRecycleviewBean(0, mBeanList.get(0).getString(), mBeanList.get(0).getId()));
                            if (isChecked) {
                                mBeanList.set(position, new FilterRecycleviewBean(1, filterRecycleviewBean.getString(), filterRecycleviewBean.getId()));
                            } else {
                                mBeanList.set(position, new FilterRecycleviewBean(0, filterRecycleviewBean.getString(), filterRecycleviewBean.getId()));
                            }
                        }
                        if (mBeanList.get(0).getInt() == 1) {
                            for (int i = 1; i < mBeanList.size(); i++) {
                                mBeanList.set(i, new FilterRecycleviewBean(0, mBeanList.get(i).getString(), mBeanList.get(i).getId()));
                            }
                        } else {
                            if (isChecked) {
                                mBeanList.set(position, new FilterRecycleviewBean(1, filterRecycleviewBean.getString(), filterRecycleviewBean.getId()));
                            } else {
                                mBeanList.set(position, new FilterRecycleviewBean(0, filterRecycleviewBean.getString(), filterRecycleviewBean.getId()));
                            }
                        }

                        StringBuffer buffer = new StringBuffer();
                        /*写一个循环*/
                        for (FilterRecycleviewBean recycleviewBean : mBeanList) {
                            if (recycleviewBean.getInt() == 1) {
                                buffer.append(recycleviewBean.getId() + ",");
                            }
                        }
                        mTaskFilter = buffer.toString();
                        LogUtil.d("这个是筛选的东西" + mTaskFilter);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        }, 100);
                    }
                });

            }

            @Override
            public int getItemCount() {
                return mBeanList.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {

                private CheckBox mFilter_name;

                public ViewHolder(View view) {
                    super(view);
                    mFilter_name = (CheckBox) view.findViewById(R.id.filter_name);
                }
            }
        });
    }

    /**
     * 年龄选择
     */
    public String mAge;

    private void PopupWindowAge(List<String> mainAge, final TextView filterAge, final int i) {
        final Popwindou popwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_filter));
        View view = UiUtils.inflate(mActivity, R.layout.pop_tow_selector);
        popwindou.init(view, Gravity.BOTTOM, true);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.backgroundf1f1f1);
        style.selectedTextSize = 20;
        style.textSize = 18;
        style.textAlpha = 0.5f;
        style.selectedTextZoom = 5;

        final WheelView mainWheelView = (WheelView) view.findViewById(R.id.main_wheelview);
        mainWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        mainWheelView.setSkin(WheelView.Skin.Holo);
        mainWheelView.setWheelSize(5);
        mainWheelView.setStyle(style);

        mainWheelView.setWheelData(mainAge);

        final WheelView subWheelView = (WheelView) view.findViewById(R.id.sub_wheelview);
        subWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        subWheelView.setSkin(WheelView.Skin.Holo);
        subWheelView.setWheelSize(5);
        subWheelView.setStyle(style);
        subWheelView.setWheelData(mainAge);
        mainWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                switch (i) {
                    case 1:
                        int onePosition = mainWheelView.getCurrentPosition();
                        int twoPosition = subWheelView.getCurrentPosition();
                        if (position == 0) {
                            subWheelView.smoothScrollToPosition(0);
                        } else if (position == mArrayAgeList.size() - 2) {
                            subWheelView.smoothScrollToPosition(mArrayAgeList.size() - 2);
                        } else if (onePosition > twoPosition) {
                            subWheelView.smoothScrollToPosition(position + 4);
                        }
                        mMinimumAge = mArrayAgeList.get(position);
                        LogUtil.d("年龄" + mMinimumAge + "   " + o);

                        break;
                    case 2:
                        if ("不限".equals(o)) {
                            mMinimumAge = 0;
                        } else {
                            mMinimumAge = position + 100 + 1;
                        }
                        break;
                }

                LogUtil.d("这个是前面选择的监听--->" + mMinimumAge);
            }
        });
        subWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                switch (i) {
                    case 1:
                        int onePosition = mainWheelView.getCurrentPosition();
                        int twoPosition = subWheelView.getCurrentPosition();
                        if (position == mArrayAgeList.size() - 1) {
                            mainWheelView.smoothScrollToPosition(mArrayAgeList.size() + 4);
                        } else if (onePosition > twoPosition) {
                            subWheelView.smoothScrollToPosition(onePosition + 4);
                        }
                        mMaximumAge = mArrayAgeList.get(position);
                        LogUtil.d("年龄" + mMaximumAge + "   " + o);
                        break;
                    case 2:
                        if ("不限".equals(o)) {
                            mMaximumAge = 0;
                        } else {
                            mMaximumAge = position + 100 + 1;
                        }
                        break;
                }
                LogUtil.d("这个是后面区选择的监听--->" + mMaximumAge);
            }
        });
        view.findViewById(R.id.cancel_selector).setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View view) {
                popwindou.dismiss();
            }
        });
        view.findViewById(R.id.ok_selector).setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View view) {
                switch (i) {
                    case 1:
                        if (mMinimumAge > mMaximumAge) {
                            Toasty.normal(mActivity, "开始年龄不能大于截止年龄或截止年龄不能小于起始年龄").show();
                            return;
                        }
                        if (mMinimumAge == 17 && mMaximumAge == 17) {
                            mAge = "不限";
                        } else if (mMinimumAge == 60 || mMaximumAge == 60) {
                            mAge = "60岁以上";
                            otherLowAge = String.valueOf(60);
                            otherHignAge = String.valueOf(180);
                        } else {
                            mAge = mMinimumAge + "岁 - " + mMaximumAge + "岁";
                            otherLowAge = mMinimumAge + "";
                            otherHignAge = mMaximumAge + "";
                        }
                        filterAge.setText(mAge);
                        popwindou.dismiss();
                        break;
                    case 2:

                        if (mMaximumAge == mMinimumAge && mMaximumAge == 0 && mMinimumAge == 0) {
                            otherLowAge = "";
                            otherHignAge = "";
                            filterAge.setText("不限");
                            popwindou.dismiss();
                        } else if (mMaximumAge - 2 >= mMinimumAge - 2 && mMaximumAge != 0 && mMinimumAge != 0) {
                            otherLowheight = mMinimumAge - 2 + "";
                            otherHignheight = mMaximumAge - 2 + "";
                            filterAge.setText((mMinimumAge - 2) + "cm - " + (mMaximumAge - 2) + "cm");
                            popwindou.dismiss();
                        } else {
                            Toasty.normal(mActivity, "请选择正确的身高阶段").show();
                        }
                        break;
                }

            }
        });

    }

    /**
     * 按钮的点击事件
     */
    @OnClick({R.id.filter_age_ll, R.id.filter_height_ll, R.id.filter_address_ll, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_age_ll:
                PopupWindowAge(OneAge(), mFilterAge, 1);
                break;
            case R.id.filter_height_ll:
                PopupWindowAge(createMainHeight(), mFilterHeight, 2);
                break;
            case R.id.filter_address_ll: //选择城市列表
//                if (mCrtyBean != null) {
//                    if (mCrtyBean.getAreaCity().size() > 1) {
//                        popsselector(mCrtyBean);
//                    } else {
//                        ToastUtils.ToastAddress(mActivity);
//                    }
//                }
                ToastUtils.ToastAddress(mActivity);
                break;
            case R.id.right_button:
                FilterReturnBean returnBean = new FilterReturnBean(sort, otherSex, otherLowAge,
                        otherHignAge, otherLowheight, otherHignheight, isVideo, isIdcard, mTaskFilter, IpConfig.cityCode);
                EventBus.getDefault().post(returnBean);
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("filter", mBeanList);
                setResult(0, intent);
                finish();

                break;
        }
    }

    private void popsselector(final CrtyBean crtyBean) {
        Popwindou popwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_filter));
        View view = UiUtils.inflate(mActivity, R.layout.pop_tow_selector);
        popwindou.init(view, Gravity.BOTTOM, true);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.backgroundf1f1f1);
        style.selectedTextSize = 20;
        style.textSize = 18;
        style.textAlpha = 0.5f;
        style.selectedTextZoom = 5;

        WheelView mainWheelView = (WheelView) view.findViewById(R.id.main_wheelview);
        mainWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        mainWheelView.setSkin(WheelView.Skin.Holo);
        mainWheelView.setWheelSize(5);
        mainWheelView.setWheelData(createMainDatas());
        mainWheelView.setStyle(style);

        WheelView subWheelView = (WheelView) view.findViewById(R.id.sub_wheelview);
        subWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        subWheelView.setSkin(WheelView.Skin.Holo);
        subWheelView.setWheelSize(5);
        subWheelView.setWheelData(createSubDatas().get(createMainDatas().get(mainWheelView.getSelection())));
        subWheelView.setStyle(style);
        mainWheelView.join(subWheelView);
        mainWheelView.joinDatas(createSubDatas());

        mainWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mPosition2 = position;
                LogUtil.d("这个是城市选择的监听" + position);
                mProvinceId = crtyBean.getAreaCity().get(position).getProvinceId();
            }
        });
        subWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                LogUtil.d("这个是市区选择的监听" + position);
                mCityId = crtyBean.getAreaCity().get(mPosition2).getProvinceObject().get(position).getCityId();
            }
        });

    }

    List<String> ProvinceName; //城市

    List<String> cityName;//市区

    private List<String> createMainDatas() {
        ProvinceName = new ArrayList<>();
        List<CrtyBean.AreaCityBean> areaCity = mCrtyBean.getAreaCity();
        for (int i = 0; i < areaCity.size(); i++) {
            ProvinceName.add(areaCity.get(i).getProvinceName());
        }
        return ProvinceName;
    }

    private HashMap<String, List<String>> createSubDatas() {
        HashMap<String, List<String>> map = new HashMap<>();
        cityName = new ArrayList<>();
        List<CrtyBean.AreaCityBean> areaCity = mCrtyBean.getAreaCity();
        for (int i = 0; i < areaCity.size(); i++) {
            List<CrtyBean.AreaCityBean.ProvinceObjectBean> provinceObject = areaCity.get(i).getProvinceObject();
            for (int j = 0; j < provinceObject.size(); j++) {
                cityName.add(provinceObject.get(j).getCityName());
            }
        }
        for (int i = 0; i < ProvinceName.size(); i++) {
            map.put(ProvinceName.get(i), cityName);
        }
        return map;
    }

    private List<String> createMainAge() {
        ProvinceName = new ArrayList<>();
        ProvinceName.add("不限");
        for (int i = 18; i <= 108; i++) {
            ProvinceName.add(i + " 岁");
        }
        return ProvinceName;
    }

    private List<String> OneAge() {
        mArrayList = new ArrayList<>();
        mArrayList.add("不限");
        for (int i = 18; i <= 59; i++) {
            mArrayList.add(i + "岁");
        }
        mArrayList.add("60以上");

        if (mArrayAgeList == null) {
            mArrayAgeList = new ArrayList<>();
            for (int i = 17; i <= 60; i++) {
                mArrayAgeList.add(i);
            }
        }

        return mArrayList;
    }

    private List<String> createMainHeight() {
        ProvinceName = new ArrayList<>();
        ProvinceName.add("不限");
        for (int i = 100; i < 300; i++) {
            ProvinceName.add(i + " cm");
        }
        return ProvinceName;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }
}
