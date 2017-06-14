package com.jsz.peini.ui.activity.search;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.address.HotBean;
import com.jsz.peini.model.address.SellerAddress;
import com.jsz.peini.model.seller.SellerCodesBySellerCodesBean;
import com.jsz.peini.model.seller.SellerInfoBean;
import com.jsz.peini.model.seller.SellerTabulationBean;
import com.jsz.peini.presenter.search.SearchService;
import com.jsz.peini.presenter.seller.SellerService;
import com.jsz.peini.ui.activity.seller.SellerMessageActivity;
import com.jsz.peini.ui.adapter.seller.IsSearchAdapter;
import com.jsz.peini.ui.adapter.seller.OneRecyclerviewAdapter;
import com.jsz.peini.ui.adapter.seller.TowRecyclerviewAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ProgressActivity;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerTabulationActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
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
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;
    /**
     * 搜索的数据集合
     */
    private List<SellerInfoBean> mSellerInfo = new ArrayList<>();
    /**
     *
     */
    public List<HotBean> mDistrictList = new ArrayList<>();
    /**
     *
     */
    private int[] distanceArray = {1000, 3000, 5000, 10000, 0};
    private String mSellerinfoname;
    private SellerTabulationActivity mActivity;
    private IsSearchAdapter mIsSearchAdapter;
    private Intent mIntent;
    private PopupWindow mPop;
    private RecyclerView mOneRecyclerView, mTowRecyclerView;
    private SellerAddress mSellerAddress;
    private TowRecyclerviewAdapter mTowRecyclerviewAdapter;
    private SellerCodesBySellerCodesBean mSellerCodesBean;
    private List<SellerCodesBySellerCodesBean.SellerCodesBean> mSellerCodes = new ArrayList<>();
    //是否是发布任务
    private String mChoice;
    /**
     * 入参
     */
    private String distance;
    private String sellerType;
    private String labelsId;
    private String sort;
    private String districtCode;

    @Override
    public int initLayoutId() {
        return R.layout.seller_tabulation;
    }

    @Override
    public void initView() {
        mActivity = this;
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.setText("商家搜索 ");
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChoice = getIntent().getStringExtra(Conversion.CHOICE);
        mSellerinfoname = getIntent().getStringExtra(Conversion.SELLERINFONAME);
//        initNetWorkScreen();
    }

    int pageNumber = 1;

    @Override
    public void initData() {
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mIsSearchAdapter = new IsSearchAdapter(mActivity, mSellerInfo);
        mSwipeTarget.setAdapter(mIsSearchAdapter);

        /**上啦加载*/
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                inItNetWork(true);
            }
        });
        /**下拉刷新*/
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                inItNetWork(false);
            }
        });
    }

    @Override
    protected void initListener() {
        mIsSearchAdapter.setItemClickListener(new IsSearchAdapter.ItemClickListener() {
            @Override
            public void onSellerItemClick(int id, int position) {
                mIntent = new Intent(mActivity, SellerMessageActivity.class);
                if (!TextUtils.isEmpty(mChoice)) {
                    mIntent.putExtra(Conversion.CHOICE, mChoice);
                }
                mIntent.putExtra(Conversion.ID, String.valueOf(id));
                startActivity(mIntent);
                finish();
            }
        });
    }

    @Override
    public void initInternet() {
        inItNetWork(true);
    }

    private void inItNetWork(final boolean isfirst) {
        if (isfirst) {
            pageNumber = 1;
        } else {
            pageNumber++;
        }
        RetrofitUtil.createService(SearchService.class)
                .searchAllSeller(mSellerinfoname, SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity), pageNumber, "10")
                .enqueue(new RetrofitCallback<SellerTabulationBean>() {
                    @Override
                    public void onSuccess(Call<SellerTabulationBean> call, Response<SellerTabulationBean> response) {
                        mSwipeToLoadLayout.setRefreshing(false);
                        mSwipeToLoadLayout.setLoadingMore(false);
                        if (response.isSuccessful()) {
                            SellerTabulationBean body = response.body();
                            if (body.getResultCode() == 1) {
                                List<SellerInfoBean> sellerList = body.getSellerList();
                                if (isfirst) {
                                    mSellerInfo.clear();
                                    mSellerInfo.addAll(sellerList);
                                } else {
                                    mSellerInfo.addAll(sellerList);
                                }
                                if (mSellerInfo.size() > 0) {
                                    mIsSearchAdapter.notifyDataSetChanged();
                                    mPaProgress.showContent();
                                } else {
                                    mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "搜索列表暂无数据~");
                                }
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SellerTabulationBean> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);
                        mSwipeToLoadLayout.setLoadingMore(false);
                    }
                });
    }

    @OnClick({R.id.seller_seller, R.id.seller_distance, R.id.seller_typeofoperation, R.id.seller_sort})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seller_seller:
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
                sellerTypeofoperationNetWorkData();
                break;
            case R.id.seller_sort:
                mDistrictList.clear();
                mDistrictList.add(new HotBean(304, "人气最高"));
                mDistrictList.add(new HotBean(303, "评价最好"));
                mDistrictList.add(new HotBean(301, "人均最低"));
                mDistrictList.add(new HotBean(302, "人均最高"));
                sellerTypeofoperationData(1, 1, 4);
                break;
        }
    }

    /**
     * 商家位置选择
     */

    private void sellerSellerData() {
        if (null == mSellerAddress) {
            return;
        }
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
                mDistrictList.clear();
                List<SellerAddress.DistrictListBean.DistrictObjectBean> districtObject = mSellerAddress.getDistrictList().get(position).getDistrictObject();
                for (int i = 0; i < districtObject.size(); i++) {
                    mDistrictList.add(new HotBean(districtObject.get(i).getDistrictId(), districtObject.get(i).getDistrictName()));
                }
                mTowRecyclerviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void ItemClicknearbyListener(int position) {
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
                inItNetWork(true);
                mPop.dismiss();
            }
        });
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
                        sort = String.valueOf(countyId);
                        break;
                }
                inItNetWork(true);
                mPop.dismiss();
            }
        });
    }

    /**
     * 业态获取接口
     */
    private void sellerTypeofoperationNetWorkData() {
        if (mSellerCodes == null || mSellerCodes.size() == 0) {
            return;
        }
        mDistrictList.clear();
        for (int i = 0; i < mSellerCodes.size(); i++) {
            SellerCodesBySellerCodesBean.SellerCodesBean sellerCodesBean = mSellerCodes.get(i);
            List<SellerCodesBySellerCodesBean.SellerCodesBean.SubDataBean> subData = sellerCodesBean.getSubData();
            for (int j = 0; j < subData.size(); j++) {
                mDistrictList.add(new HotBean(Integer.parseInt(subData.get(j).getId()), subData.get(j).getName()));
            }
        }
        sellerTypeofoperationData(1, 3, 3);
    }

    private void initNetWorkScreen() {
        /**
         * 获取商家位置筛选信息
         */
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
        RetrofitUtil.createService(SellerService.class)
                .getSellerCodesBySellerCodesBean()
                .enqueue(new Callback<SellerCodesBySellerCodesBean>() {
                    @Override
                    public void onResponse(Call<SellerCodesBySellerCodesBean> call, Response<SellerCodesBySellerCodesBean> response) {
                        if (response.isSuccessful()) {
                            mSellerCodesBean = response.body();
                            if (mSellerCodesBean.getResultCode() == 1) {
                                if (mSellerCodesBean != null) {
                                    mSellerCodes.clear();
                                    mSellerCodes.addAll(mSellerCodesBean.getSellerCodes());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SellerCodesBySellerCodesBean> call, Throwable t) {

                    }
                });
    }
}
