package com.jsz.peini.ui.activity.filter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.gen.HistoryHotBean;
import com.jsz.peini.gen.HistoryHotBeanDao;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.filter.HotWordBean;
import com.jsz.peini.model.search.LatLngBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.adapter.search.TaskSearchAdapter;
import com.jsz.peini.ui.adapter.search.TaskSearchEndAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 15089 on 2017/2/13.
 */
public class TaskSearchActivity extends BaseActivity {
    @InjectView(R.id.task_search_recyclerview)
    RecyclerView mTaskSearchRecyclerview;
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    @InjectView(R.id.phone_edt)
    EditText mSquarePeripheryEdittext;
    @InjectView(R.id.issearch_button)
    Button mClose;

    @InjectView(R.id.iv_clean)
    ImageView mIvClean;
    private TaskSearchActivity mActivity;

    List<HotWordBean.DataBean> mList = new ArrayList<>();

    ArrayList<PoiInfo> mPoiInfos = new ArrayList<>();

    private String mEditText = "";

    private int mIndex = 0;

    private PoiSearch mPoiSearch;

    private boolean IsFirst = true;

    private int mTotalPageNum;
    private TaskSearchEndAdapter mSearchEndAdapter;
    private TaskSearchAdapter mSearchAdapter;
    private HistoryHotBeanDao mHotBeanDao;
    /*获取的历史记录*/
    private List<HistoryHotBean> mHistoryHotBeen;

    @Override
    public int initLayoutId() {
        return R.layout.activity_tasksearch;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Conversion.DATASUCCESS:
                    HotWordBean wordBean = (HotWordBean) msg.obj;
                    LogUtil.d("这个是返回的热门" + wordBean.toString());
                    mList.clear();
                    List<HotWordBean.DataBean> data = wordBean.getData();
                    mList.addAll(data);
                    mSearchAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void initView() {
        mActivity = this;
        // 实例化PoiSearch对象
        mHotBeanDao = PeiNiApp.HistoryHotBeanDao;
        /*获取mHotBeanDao*/
        mHistoryHotBeen = mHotBeanDao.queryBuilder().orderDesc(HistoryHotBeanDao.Properties.Id).limit(10).list();
        LogUtil.d("historyHotBeen" + mHistoryHotBeen.toString());

        mPoiSearch = PoiSearch.newInstance();
        // 设置检索监听器
        mPoiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
        // 初始化搜索模块，注册事件监听
        mIvClean.setVisibility(mEditText.length() > 0 ? View.VISIBLE : View.GONE);
        mSquarePeripheryEdittext.addTextChangedListener(getWatcher());
        mSwipeToLoadLayout.setVisibility(View.GONE);
    }


    @NonNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mEditText = mSquarePeripheryEdittext.getText().toString().trim();
                if (isStartWithNumber(mEditText)) {
                    return;
                }
                mIvClean.setVisibility(mEditText.length() > 0 ? View.VISIBLE : View.GONE);
                if (mEditText.length() > 0) {
                    mSwipeToLoadLayout.setVisibility(View.VISIBLE);
                    GetmPoiSearch();
                } else {
                    mSwipeToLoadLayout.setVisibility(View.GONE);
                }
            }
        };
    }

    public static boolean isStartWithNumber(String string) {
        return !(string == null || string.length() == 0) && !(string.charAt(0) < 48 || string.charAt(0) > 57);
    }

    @Override
    public void initData() {
        /*基本数据*/
        mTaskSearchRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
        mSearchAdapter = new TaskSearchAdapter(mActivity, mList, mHistoryHotBeen);
        mTaskSearchRecyclerview.setAdapter(mSearchAdapter);
        /*搜索数据*/
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mSearchEndAdapter = new TaskSearchEndAdapter(mActivity, mPoiInfos, Double.parseDouble(SpUtils.getXpoint(mActivity)), Double.parseDouble(SpUtils.getYpoint(mActivity)));
        mSwipeTarget.setAdapter(mSearchEndAdapter);

        initHotWordNetWork();
    }

    /*获取搜索词*/
    private void initHotWordNetWork() {
        mDialog.show();
        RetrofitUtil.createService(TaskService.class)
                .getHotWord("2")
                .enqueue(new RetrofitCallback<HotWordBean>() {
                    @Override
                    public void onSuccess(Call<HotWordBean> call, Response<HotWordBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            HotWordBean body = response.body();
                            if (body.getResultCode() == 1) {
                                Message msg = new Message();
                                msg.what = Conversion.DATASUCCESS;
                                msg.obj = body;
                                mHandler.sendMessage(msg);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HotWordBean> call, Throwable t) {
                        mDialog.dismiss();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    @Override
    protected void initListener() {
        mSearchAdapter.setOnClickListener(new TaskSearchAdapter.OnClickListener() {
            @Override
            public void ItemHotClick(int id, String hotName, int hotNum) {
                LogUtil.d(id + hotName + hotNum);
                mSquarePeripheryEdittext.setText(hotName);
                initSearchByHotWordNetWork(hotName);
            }

            @Override
            public void itemClick(String name, String distance, String address, double latitude, double longitude) {
                LatLngBean latLng = new LatLngBean(latitude, longitude);
                EventBus.getDefault().post(latLng);
                mActivity.finish();
            }

            @Override
            public void onDetele() {
//                mHotBeanDao.deleteAll();
//                mSearchAdapter.setBottomCount(0);
                showDeleteConfirmDialog();
            }
        });
        mSearchEndAdapter.setOnitemClickListener(new TaskSearchEndAdapter.OnitemClickListener() {
            @Override
            public void itemClick(String name, String distance, String address, double latitude, double longitude) {
                HistoryHotBean hotBean = new HistoryHotBean(null, "", name, distance, address, latitude, longitude);
                removeInsertedData(name, latitude, longitude);
                mHotBeanDao.insert(hotBean);

                LatLngBean latLng = new LatLngBean(latitude, longitude);
                EventBus.getDefault().post(latLng);

                initSearchByHotWordNetWork(name);

                mActivity.finish();

            }
        });
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("您是否要清除搜索记录")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHotBeanDao.deleteAll();
                        mSearchAdapter.setBottomCount(0);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 查库去重
     */
    private void removeInsertedData(String name, double latitude, double longitude) {
        if (TextUtils.isEmpty(name) || mHistoryHotBeen == null || mHistoryHotBeen.size() == 0) {
            return;
        }
        try {
            for (HistoryHotBean hotBean : mHistoryHotBeen) {
                if (name.equals(hotBean.getName())
                        && latitude == hotBean.getLatitude()
                        && longitude == hotBean.getLongitude()) {
                    mHotBeanDao.deleteByKey(hotBean.getId());
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSearchByHotWordNetWork(String hotName) {
        RetrofitUtil.createService(TaskService.class)
                .searchByHotWord(hotName, 2)
                .enqueue(new RetrofitCallback<SuccessfulBean>() {
                    @Override
                    public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                    }
                });
    }

    private void GetmPoiSearch() {
        //构造请求参数，其中centerPt是自己的位置坐标
        LatLng latLng = new LatLng(
                Double.parseDouble(SpUtils.getXpoint(mActivity)),
                Double.parseDouble(SpUtils.getYpoint(mActivity)));
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(latLng);
        nearbySearchOption.keyword(mEditText);
        nearbySearchOption.radius(10000);// 检索半径，单位是米
        nearbySearchOption.pageNum(mIndex);
        nearbySearchOption.pageCapacity(20);
        mPoiSearch.searchNearby(nearbySearchOption);
    }

    /**
     * 关键词检索
     */
    OnGetPoiSearchResultListener onGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult result) {
            if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                return;
            }
            //获取POI检索结果
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                List<PoiInfo> allAddr = result.getAllPoi();
                if (allAddr.size() > 0) {
                    mSwipeToLoadLayout.setVisibility(View.VISIBLE);
                    mPoiInfos.clear();
                    mPoiInfos.addAll(allAddr);
                    mSearchEndAdapter.notifyDataSetChanged();
                }
                mTotalPageNum = result.getTotalPageNum();
                LogUtil.d("mPoiSearch-----------", "总共查到" + result.getTotalPoiNum() + "个兴趣点, 分为" + mTotalPageNum + "页");
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        }
    };


    @OnClick({R.id.issearch_button, R.id.iv_clean})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issearch_button:
                finish();
                break;
            case R.id.iv_clean:
                mSquarePeripheryEdittext.setText("");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(mSquarePeripheryEdittext.getText())) {
            mSquarePeripheryEdittext.setText("");
        } else {
            super.onBackPressed();
        }
    }
}
