package com.jsz.peini.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.address.HotBean;
import com.jsz.peini.model.address.SellerAddress;
import com.jsz.peini.model.eventbus.LoodingData;
import com.jsz.peini.model.seller.SellerBean;
import com.jsz.peini.model.seller.SellerCodesBySellerCodesBean;
import com.jsz.peini.model.seller.SellerInfoBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.ad.Ad;
import com.jsz.peini.presenter.seller.SellerService;
import com.jsz.peini.ui.activity.seller.SellerMessageActivity;
import com.jsz.peini.ui.adapter.seller.OneRecyclerviewAdapter;
import com.jsz.peini.ui.adapter.seller.SellerAdapter;
import com.jsz.peini.ui.adapter.seller.TowRecyclerviewAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.FastScrollLinearLayoutManager;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by kunwe on 2016/11/25.
 * 商家界面
 */

public class SellerFragment extends BaseFragment {
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @InjectView(R.id.image_1)
    ImageView mImage1;
    @InjectView(R.id.seller_seller)
    LinearLayout mSellerSeller;
    @InjectView(R.id.image_2)
    ImageView mImage2;
    @InjectView(R.id.seller_distance)
    LinearLayout mSellerDistance;
    @InjectView(R.id.image_3)
    ImageView mImage3;
    @InjectView(R.id.seller_typeofoperation)
    LinearLayout mSellerTypeofoperation;
    @InjectView(R.id.image_4)
    ImageView mImage4;
    @InjectView(R.id.seller_sort)
    LinearLayout mSellerSort;
    @InjectView(R.id.item_selector)
    LinearLayout mItemSelector;
    public SellerAdapter mAdapter;
    public LinearLayoutManager mLinearLayoutManager;
    public Intent mIntent;
    public SellerAddress mSellerAddress;
    public RecyclerView mOneRecyclerView, mTowRecyclerView;
    public PopupWindow mPop;
    public TowRecyclerviewAdapter mTowRecyclerviewAdapter;
    public SellerCodesBySellerCodesBean mSellerCodesBean;
    public List<HotBean> mDistrictList = new ArrayList<>();
    /**
     * 商家的初始化数据
     */
    public List<SellerInfoBean> mSellerInfo = new ArrayList<>();
    private List<SellerCodesBySellerCodesBean.SellerCodesBean> mSellerCodes = new ArrayList<>();
    private List<AdModel.AdvertiseListBean> mAdvertiseList = new ArrayList<>();
    private int[] distanceArray = {1000, 3000, 5000, 10000, 0};
    /*是否第一次刷新*/
    boolean isFirst = true;
    /*要访问的第几页*/
    int pageNow = 1;
    private String mChoice;
    /*距离*/
    private String distance = "";
    private String sellerType = "";
    private String districtCode = "";
    private String labelsId = "";
    private String sort = "";
    private int mSellerTypeIndex = -1;
    private AdModel mAdModel;

    @Override
    public View initViews() {
        //注册事件
        EventBus.getDefault().register(this);
        return View.inflate(mActivity, R.layout.fragment_sellers, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (null != bundle) {
            mChoice = bundle.getString(Conversion.CHOICE);
            LogUtil.d("mChoice" + mChoice);
            if (mAdModel == null) {
                getAdList();
            }
             /*商圈*/
            if (mSellerAddress == null) {
                initsellerSellerNetWorkData();
            }
             /*业态*/
            if (mSellerCodesBean == null) {
                sellerTypeofoperationNetWorkData();
            }
            initNetWork(true);
        }
    }

    @Override
    public void initData() {
        /**给Recyclerview设置布局*/
        mLinearLayoutManager = new FastScrollLinearLayoutManager(mActivity);
        mSwipeTarget.setLayoutManager(mLinearLayoutManager);
        /**适配器*/
        mAdapter = new SellerAdapter(mActivity, mSellerInfo, mAdvertiseList, mSellerCodes);
        mSwipeTarget.setAdapter(mAdapter);
        /**适配器的回调*/
        mAdapter.setOnClickListener(new SellerAdapter.OnClickListener() {
            @Override
            public void onClick(int i) {
                smoothMoveToPosition(i);
            }

            @Override
            public void onSellerSelectItemClick(int position) {
                SellerSelectItemClick(position);
            }

            @Override
            public void onSellerItemClick(int id, int position) {
                mIntent = new Intent(mActivity, SellerMessageActivity.class);
                mIntent.putExtra("id", id + "");
                mIntent.putExtra(Conversion.CHOICE, mChoice);
                mActivity.startActivity(mIntent);
            }

            @Override
            public void onClickSellerType(String id, int position) {
                sellerType = id;
                if (sellerType == null) {
                    sellerType = "";
                }
                isFirst = true;
                pageNow = 1;
                distance = "";
                districtCode = "";
                labelsId = "";
                sort = "";
                mSellerTypeIndex = position;
                initNetWork(true);
            }
        });
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mSwipeToLoadLayout.setRefreshing(false);
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                LogUtil.d("===================" + firstVisibleItemPosition + "");
                mItemSelector.setVisibility(firstVisibleItemPosition == 0 ? View.GONE : View.VISIBLE);
            }
        });

        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
//                sellerType = "";
//                districtCode = "";
//                distance = "";
//                labelsId = "";
                   /*广告*/
                if (mAdModel == null) {
                    getAdList();
                }
             /*商圈*/
                if (mSellerAddress == null) {
                    initsellerSellerNetWorkData();
                }
             /*业态*/
                if (mSellerCodesBean == null) {
                    sellerTypeofoperationNetWorkData();
                }
                pageNow = 1;
                isFirst = true;
                initNetWork(false);
            }
        });
        /**下拉刷新*/
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isFirst = false;
                pageNow++;
                initNetWork(false);
            }
        });
    }

    /**
     * 网络访问
     */
    private void initNetWork(boolean isShowProgress) {
        RetrofitUtil.createService(SellerService.class)
                .getSellerInfoBySellerInfo(mXpoint, mYpoint,
                        sellerType, distance, districtCode,
                        labelsId, sort, pageNow, Conversion.PAGESIZE,
                        IpConfig.cityCode)
                .enqueue(new Callback<SellerBean>() {
                    @Override
                    public void onResponse(Call<SellerBean> call, Response<SellerBean> response) {
                        if (response.isSuccessful()) {
                            mSwipeToLoadLayout.setRefreshing(false);
                            mSwipeToLoadLayout.setLoadingMore(false);
                            SellerBean sellerBean = response.body();
                            if (sellerBean.getResultCode() == 1) {
                                List<SellerInfoBean> sellerInfo = sellerBean.getSellerInfo();
                                if (isFirst) {
                                    mSellerInfo.clear();
                                    mSellerInfo.addAll(sellerInfo);
                                } else {
                                    mSellerInfo.addAll(sellerInfo);
                                }
                                if (null != mAdapter) {
                                    mAdapter.notifyItemChanged(0);

                                    mAdapter.notifyDataSetChanged();
                                }
                                LogUtil.d("商家选择界面返回的数据" + sellerBean.toString());
                            } else if (sellerBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, sellerBean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SellerBean> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);
                        mSwipeToLoadLayout.setLoadingMore(false);
                    }
                });
    }

    private void smoothMoveToPosition(int position) {
        int firstPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        if (position <= lastPosition) {
            int top = mSwipeTarget.getChildAt(position - firstPosition).getTop();
            mSwipeTarget.smoothScrollBy(0, top);
        }
    }

    /**
     * 点击时间的回调
     */
    private void SellerSelectItemClick(int position) {
        switch (position) {
            case 1:
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        sellerSellerData();
                    }
                }, 200);
                break;
            case 2:
                mDistrictList.clear();
                for (int i = 0; i < distanceArray.length; i++) {
                    if (i == distanceArray.length - 1) {
                        mDistrictList.add(new HotBean(distanceArray[i], "全市"));
                    } else {
                        mDistrictList.add(new HotBean(distanceArray[i], distanceArray[i] / 1000 + " km"));
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        sellerTypeofoperationData(1, 1, 2);
                    }
                }, 200);
                break;
            case 3:
                if (mSellerCodes == null || mSellerCodes.size() == 0) {
                    return;
                }
                mDistrictList.clear();
                if (mSellerCodes != null && mSellerCodes.size() > mSellerTypeIndex) {
                    if (mSellerTypeIndex == 0 || mSellerTypeIndex == -1) {
                        for (int i = 0; i < mSellerCodes.size(); i++) {
                            SellerCodesBySellerCodesBean.SellerCodesBean sellerCodesBean = mSellerCodes.get(i);
                            List<SellerCodesBySellerCodesBean.SellerCodesBean.SubDataBean> subData = sellerCodesBean.getSubData();
                            for (int j = 0; j < subData.size(); j++) {
                                mDistrictList.add(new HotBean(Integer.parseInt(subData.get(j).getId()), subData.get(j).getName()));
                            }
                        }
                    } else {
                        SellerCodesBySellerCodesBean.SellerCodesBean sellerCodesBean = mSellerCodes.get(mSellerTypeIndex);
                        List<SellerCodesBySellerCodesBean.SellerCodesBean.SubDataBean> subData = sellerCodesBean.getSubData();
                        for (int j = 0; j < subData.size(); j++) {
                            mDistrictList.add(new HotBean(Integer.parseInt(subData.get(j).getId()), subData.get(j).getName()));
                        }
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        sellerTypeofoperationData(1, 3, 3);
                    }
                }, 200);
                break;
            case 4:
                mDistrictList.clear();
                mDistrictList.add(new HotBean(0, "离我最近"));
                mDistrictList.add(new HotBean(304, "人气最高"));
                mDistrictList.add(new HotBean(303, "评价最好"));
                mDistrictList.add(new HotBean(301, "人均最低"));
                mDistrictList.add(new HotBean(302, "人均最高"));
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        sellerTypeofoperationData(1, 1, 4);
                    }
                }, 200);
                break;
        }
    }

    @OnClick({R.id.seller_seller, R.id.seller_distance, R.id.seller_typeofoperation, R.id.seller_sort})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seller_seller:
                if (mSellerAddress == null) {
                    return;
                }
                sellerSellerData();
                break;
            case R.id.seller_distance:
                mDistrictList.clear();
                for (int i = 0; i < distanceArray.length; i++) {
                    if (i == distanceArray.length - 1) {
                        mDistrictList.add(new HotBean(distanceArray[i], "全市"));
                    } else {
                        mDistrictList.add(new HotBean(distanceArray[i], distanceArray[i] / 1000 + " km"));
                    }
                }
                sellerTypeofoperationData(1, 1, 2);
                break;
            case R.id.seller_typeofoperation:
                if (mSellerCodes == null || mSellerCodes.size() == 0) {
                    return;
                }

                mDistrictList.clear();
                if (mSellerCodes != null && mSellerCodes.size() > mSellerTypeIndex) {
                    if (mSellerTypeIndex == 0 || mSellerTypeIndex == -1) {
                        for (int i = 0; i < mSellerCodes.size(); i++) {
                            SellerCodesBySellerCodesBean.SellerCodesBean sellerCodesBean = mSellerCodes.get(i);
                            List<SellerCodesBySellerCodesBean.SellerCodesBean.SubDataBean> subData = sellerCodesBean.getSubData();
                            for (int j = 0; j < subData.size(); j++) {
                                mDistrictList.add(new HotBean(Integer.parseInt(subData.get(j).getId()), subData.get(j).getName()));
                            }
                        }
                    } else {
                        SellerCodesBySellerCodesBean.SellerCodesBean sellerCodesBean = mSellerCodes.get(mSellerTypeIndex);
                        List<SellerCodesBySellerCodesBean.SellerCodesBean.SubDataBean> subData = sellerCodesBean.getSubData();
                        for (int j = 0; j < subData.size(); j++) {
                            mDistrictList.add(new HotBean(Integer.parseInt(subData.get(j).getId()), subData.get(j).getName()));
                        }
                    }
                }
                sellerTypeofoperationData(1, 3, 3);
                break;
            case R.id.seller_sort:
                mDistrictList.clear();
                mDistrictList.add(new HotBean(0, "离我最近"));
                mDistrictList.add(new HotBean(304, "人气最高"));
                mDistrictList.add(new HotBean(303, "评价最好"));
                mDistrictList.add(new HotBean(301, "人均最低"));
                mDistrictList.add(new HotBean(302, "人均最高"));
                sellerTypeofoperationData(1, 1, 4);
                break;
        }
    }


    /**
     * 商家业态选择
     *
     * @param i1
     * @param i
     * @param type 2.距离、3.业态、4.排序
     */
    private void sellerTypeofoperationData(int i1, int i, final int type) {
        View view = UiUtils.inflate(mActivity, R.layout.pop_one_recyclerview);
        mPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setFocusable(true);
//        mPop.setTouchable(true);
//        mPop.setOutsideTouchable(true);
        mPop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bj_4c4c4c)));
//        mPop.setAnimationStyle(R.style.mypopwindow_anim_style);
        mPop.showAsDropDown(mItemSelector);
        mPop.update();
        view.findViewById(R.id.pop_tow_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPop.dismiss();
            }
        });
                   /*右列表*/
        mTowRecyclerView = (RecyclerView) view.findViewById(R.id.one_recyclerview);
        //设置recyclerview高度
        if (i == 3) {
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mTowRecyclerView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
            linearParams.height = UiUtils.dip2px(mActivity, 230f);// 控件的宽强制设成30
            mTowRecyclerView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
        mTowRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mTowRecyclerviewAdapter = new TowRecyclerviewAdapter(mActivity, mDistrictList, i1);
        mTowRecyclerView.setAdapter(mTowRecyclerviewAdapter);
        mTowRecyclerviewAdapter.setOnItemClickListener(new TowRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int position, int countyId) {

                switch (type) {
                    case 2:
                        if (countyId == 0) {
                            distance = "";
                        } else {
                            distance = String.valueOf(countyId);
                        }
                        break;
                    case 3:
                        sellerType = "";
                        labelsId = String.valueOf(countyId);
                        break;
                    case 4:
                        sort = "0".equals(String.valueOf(countyId)) ? "" : String.valueOf(countyId);
                        break;
                }
                isFirst = true;
                initNetWork(true);
                mPop.dismiss();
            }
        });
    }

    /**
     * 商家位置选择
     */
    private void sellerSellerData() {
        View view = UiUtils.inflate(mActivity, R.layout.pop_tow_address);
        mPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setFocusable(true);
        mPop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bj_4c4c4c)));
//        mPop.setAnimationStyle(R.style.mypopwindow_anim_style);
        mPop.showAsDropDown(mItemSelector);
        mPop.update();

        view.findViewById(R.id.pop_tow_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPop.dismiss();
            }
        });
        /*左列表*/
        mOneRecyclerView = (RecyclerView) view.findViewById(R.id.one_recyclerview);
        mOneRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mDistrictList.clear();
        List<SellerAddress.DistrictHotListBean> districtHotList = mSellerAddress.getDistrictHotList();
        for (int i = 0; i < districtHotList.size(); i++) {
            mDistrictList.add(new HotBean(districtHotList.get(i).getPlaceCode(), districtHotList.get(i).getPlaceName()));
        }
        OneRecyclerviewAdapter oneRecyclerviewAdapter = new OneRecyclerviewAdapter(mActivity, mSellerAddress.getDistrictList());
        mOneRecyclerView.setAdapter(oneRecyclerviewAdapter);
        oneRecyclerviewAdapter.setOnItemClickListener(new OneRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int position, int countyId) {
//                ToastUtils.showToast(mActivity, position + "---" + countyId);
                mDistrictList.clear();
                List<SellerAddress.DistrictListBean.DistrictObjectBean> districtObject = mSellerAddress.getDistrictList().get(position).getDistrictObject();
                for (int i = 0; i < districtObject.size(); i++) {
                    mDistrictList.add(new HotBean(districtObject.get(i).getDistrictId(), districtObject.get(i).getDistrictName()));
                }
                mTowRecyclerviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void ItemClicknearbyListener(int position) {
//                ToastUtils.showToast(mActivity, position + "");
                mDistrictList.clear();
                List<SellerAddress.DistrictHotListBean> districtHotList = mSellerAddress.getDistrictHotList();
                for (int i = 0; i < districtHotList.size(); i++) {
                    mDistrictList.add(new HotBean(districtHotList.get(i).getPlaceCode(), districtHotList.get(i).getPlaceName()));
                }
                mTowRecyclerviewAdapter.notifyDataSetChanged();
            }
        });
                /*右列表*/
        mTowRecyclerView = (RecyclerView) view.findViewById(R.id.tow_recyclerview);
        mTowRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mTowRecyclerviewAdapter = new TowRecyclerviewAdapter(mActivity, mDistrictList, 0);
        mTowRecyclerView.setAdapter(mTowRecyclerviewAdapter);
        mTowRecyclerviewAdapter.setOnItemClickListener(new TowRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int position, int countyId) {
                districtCode = String.valueOf(countyId);
                isFirst = true;
                initNetWork(true);
                mPop.dismiss();
            }
        });
    }

    /**
     * 获取商家位置筛选信息
     */
    private void initsellerSellerNetWorkData() {
        RetrofitUtil.createService(SellerService.class).getDistrictList().enqueue(new Callback<SellerAddress>() {
            @Override
            public void onResponse(Call<SellerAddress> call, Response<SellerAddress> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 1) {
                        SellerAddress body = response.body();
                        if (body != null) {
                            mSellerAddress = body;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SellerAddress> call, Throwable t) {

            }
        });
    }

    /**
     * 业态获取接口
     */
    private void sellerTypeofoperationNetWorkData() {
        RetrofitUtil.createService(SellerService.class)
                .getSellerCodesBySellerCodesBean()
                .enqueue(new Callback<SellerCodesBySellerCodesBean>() {
                    @Override
                    public void onResponse(Call<SellerCodesBySellerCodesBean> call, Response<SellerCodesBySellerCodesBean> response) {
                        if (response.isSuccessful()) {
                            SellerCodesBySellerCodesBean body = response.body();
                            if (body.getResultCode() == 1) {
                                mSellerCodesBean = response.body();
                                if (mSellerCodesBean != null) {
                                    mSellerCodes.clear();
                                    mSellerCodes.addAll(mSellerCodesBean.getSellerCodes());
                                    if (null != mAdapter) {
                                        mAdapter.notifyItemChanged(0);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SellerCodesBySellerCodesBean> call, Throwable t) {

                    }
                });
    }

    /**
     * 广告获取的接口
     */
    public void getAdList() {
        RetrofitUtil.createService(Ad.class).getAdvertise("3").enqueue(new Callback<AdModel>() {
            @Override
            public void onResponse(Call<AdModel> call, Response<AdModel> response) {
                if (response.isSuccessful()) {
                    mAdModel = response.body();
                    if (mAdModel.getResultCode() == 1) {
                        mAdvertiseList.clear();
                        mAdvertiseList.addAll(mAdModel.getAdvertiseList());
                    } else {
                        Toasty.normal(mActivity, mAdModel.getResultDesc()).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AdModel> call, Throwable t) {
                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFires = true;
        EventBus.getDefault().unregister(this);
    }

    boolean isFires = true;

    /*侧边栏界面关闭了*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(LoodingData loodingData) {
        if (loodingData.isLooding() && loodingData.getPage() == 3 && isFires) {
            isFires = false;
            if (mAdModel == null) {
                getAdList();
            }
             /*商圈*/
            if (mSellerAddress == null) {
                initsellerSellerNetWorkData();
            }
             /*业态*/
            if (mSellerCodesBean == null) {
                sellerTypeofoperationNetWorkData();
            }
            initNetWork(true);
            mSwipeTarget.smoothScrollToPosition(0);
        }
    }
}
