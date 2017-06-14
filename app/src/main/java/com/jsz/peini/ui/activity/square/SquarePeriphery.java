package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.search.PoiInfoBean;
import com.jsz.peini.ui.adapter.square.SquarePeripheryAdapter;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2016/12/27.
 */
public class SquarePeriphery extends BaseActivity {
    private static final int RESULT_LOAD_IMAGE3 = 300;
    private static final String TAG = "SquarePeriphery";
    @InjectView(R.id.square_periphery_edittext)
    EditText mSquarePeripheryEdittext;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.issearch_search)
    ImageView mIssearchSearch;
    @InjectView(R.id.iv_delete)
    ImageView mIvDelete;
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private SquarePeriphery mActivity;
    private PoiSearch mPoiSearch;
    private double mXpoint;
    private double mYpoint;
    private GeoCoder mSearch;
    ArrayList<PoiInfoBean> mPoiInfos = new ArrayList<>();
    private SquarePeripheryAdapter mPeripheryAdapter;
    private String mEditText;
    private int mIndex = 0;
    private boolean IsFirst = true;
    private int mTotalPageNum;
    private int mPosition;
    private String mAddress;
    private String mPoiInfoSelete;

    @Override
    public int initLayoutId() {
        SDKInitializer.initialize(getApplicationContext());
        return R.layout.activity_square_periphery;
    }

    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        mTitle.setText("所在位置");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvDelete.setVisibility(View.GONE);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        // 实例化PoiSearch对象
        mPoiSearch = PoiSearch.newInstance();
        // 设置检索监听器
        mPoiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
        try {
            mXpoint = Double.parseDouble(SpUtils.getXpoint(mActivity));
            mYpoint = Double.parseDouble(SpUtils.getXpoint(mActivity));
        } catch (Exception e) {
            finish();
        }
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSquarePeripheryEdittext.setText("");
            }
        });
    }


    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        mAddress = intent.getStringExtra("Address");
        mPoiInfoSelete = intent.getStringExtra("poiInfoSelete");
        mPeripheryAdapter = new SquarePeripheryAdapter(mActivity, mPoiInfos);
        mPeripheryAdapter.addHeadView(UiUtils.inflate(mActivity, R.layout.noaddress_head));
        if (!TextUtils.isEmpty(mAddress)) {
            mPeripheryAdapter.setPoiInfoSelete(mAddress, mPoiInfoSelete);
        }
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeTarget.setAdapter(mPeripheryAdapter);
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        GetmSearch();
        mPeripheryAdapter.setListener(new SquarePeripheryAdapter.OnClickItemListener() {
            @Override
            public void ItemSeleteAddressListener(String Address, String poiInfoSelete, int position, double x, double y) {
                Intent intent = new Intent();
                intent.putExtra("poiInfoSelete", poiInfoSelete);
                intent.putExtra("Address", Address);
                intent.putExtra("latitude", x);
                intent.putExtra("longitude", y);
                setResult(RESULT_LOAD_IMAGE3, intent);
                mPosition = position;
                finish();
            }
        });
        mSquarePeripheryEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mEditText = mSquarePeripheryEdittext.getText().toString().trim();
                LogUtil.i(TAG, editable + "" + mEditText);
                if (editable.length() > 0) {
                    GetmPoiSearch();
                    mIvDelete.setVisibility(View.VISIBLE);
                    mSwipeToLoadLayout.setLoadMoreEnabled(true);
                } else {
                    mPoiInfos.clear();
                    mIvDelete.setVisibility(View.GONE);
                    mSwipeToLoadLayout.setLoadMoreEnabled(false);
                    GetmSearch();
                }
            }
        });
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                GetmPoiSearch();
                mIndex++;
                IsFirst = false;
            }
        });
    }

    public void GetmPoiSearch() {
        IsFirst = true;
        //构造请求参数，其中centerPt是自己的位置坐标
        LatLng latLng = new LatLng(
                Double.parseDouble(SpUtils.getXpoint(mActivity)),
                Double.parseDouble(SpUtils.getYpoint(mActivity)));
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(latLng);
        nearbySearchOption.keyword(mEditText);
        nearbySearchOption.radius(10000);// 检索半径，单位是米
        nearbySearchOption.pageNum(mIndex);
        nearbySearchOption.pageCapacity(30);
        mPoiSearch.searchNearby(nearbySearchOption);
    }

    private void GetmSearch() {
        // 反Geo搜索
        LatLng latLng = new LatLng(
                Double.parseDouble(SpUtils.getXpoint(mActivity)),
                Double.parseDouble(SpUtils.getYpoint(mActivity)));
        ReverseGeoCodeOption geoCodeOption = new ReverseGeoCodeOption();
        geoCodeOption.location(latLng);
        mSearch.reverseGeoCode(geoCodeOption);
    }

    /**
     * 关键词检索
     */
    OnGetPoiSearchResultListener onGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult result) {
            if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                LogUtil.d("mPoiSearch--------------", "未找到结果");
                mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                return;
            }
            //获取POI检索结果
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                List<PoiInfo> allAddr = result.getAllPoi();
                if (IsFirst) {
                    mPoiInfos.clear();
                }
                for (PoiInfo p : allAddr) {
                    LogUtil.d("mPoiSearch-------------", "p.name--->" + p.name + "p.phoneNum" + p.phoneNum + " -->p.address:" + p.address + "p.location" + p.location);
                    if (!mAddress.equals(p.name)) {
                        mPoiInfos.add(new PoiInfoBean(p.name, p.address, p.location));
                    }
                }
                mTotalPageNum = result.getTotalPageNum();
                LogUtil.d("mPoiSearch-----------", "总共查到" + result.getTotalPoiNum() + "个兴趣点, 分为" + mTotalPageNum + "页");
                mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                mPeripheryAdapter.setPoiInfoList(mPoiInfos);
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        }
    };
    /**
     * 周边?
     */
    OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                LogUtil.d("mSearch-------------", "抱歉，未能找到结果");
            } else {
                List<PoiInfo> poiList = result.getPoiList();
                if (poiList.size() != 0) {
                    if (mPoiInfos.size() != 0) {
                        mPoiInfos.clear();
                    }
                    for (PoiInfo p : poiList) {
                        LogUtil.d("mSearch----------", "p.name--->" + p.name + "p.phoneNum" + p.phoneNum + " -->p.address:" + p.address + "p.location" + p.location);
                        if (!mAddress.equals(p.name)) {
                            mPoiInfos.add(new PoiInfoBean(p.name, p.address, p.location));
                        }
                    }
                    mPeripheryAdapter.setPoiInfoList(mPoiInfos);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
        mSearch.destroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }
}
