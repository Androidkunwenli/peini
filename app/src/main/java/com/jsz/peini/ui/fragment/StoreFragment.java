package com.jsz.peini.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
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
import com.jsz.peini.ui.activity.home.HomeActivity;
import com.jsz.peini.ui.activity.pay.PaythebillActivity;
import com.jsz.peini.ui.activity.search.IsSearchActivity;
import com.jsz.peini.ui.activity.seller.SellerMessageActivity;
import com.jsz.peini.ui.activity.web.WebAllActivity;
import com.jsz.peini.ui.adapter.TestNormalAdapter;
import com.jsz.peini.ui.adapter.seller.OneRecyclerviewAdapter;
import com.jsz.peini.ui.adapter.seller.SellerSelectRecyclerviewAdapter;
import com.jsz.peini.ui.adapter.seller.SellerSubAdapter;
import com.jsz.peini.ui.adapter.seller.StoreAdapter;
import com.jsz.peini.ui.adapter.seller.TowRecyclerviewAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.ScreenUtil;
import com.jsz.peini.utils.SpDataUtils;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.FastScrollLinearLayoutManager;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * Created by kunwe on 2016/11/25.
 * 商家界面
 */

public class StoreFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private static final int MESSAGE_SCROLL_FLAG = 0;
    private static final int MESSAGE_SCROLL_AGAIN_FLAG = 1;
    @InjectView(R.id.rl_seller_title_bar)
    RelativeLayout mRlSellerTitleBar;
    @InjectView(R.id.iv_menu_imagehead_open)
    ImageView mIvMenuImageheadOpen;
    @InjectView(R.id.ll_is_search_activity)
    LinearLayout mLlIsSearchActivity;
    @InjectView(R.id.fl_seller_search_qrcode)
    FrameLayout mFlSellerSearchQrcode;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @InjectView(R.id.ll_store_item_filter)
    LinearLayout mLlStoreFilter;

    private View mBannerView;
    private ImageView mIvBannerSingle;
    private RollPagerView mRpvBannerMultiple;
    private View mTypeView;
    private RecyclerView mRvStoreTypes;
    private View mHeaderFilter;

    private StoreAdapter mQuickAdapter = null;

    private AdModel mAdModel;
    private SellerAddress mSellerAddress;
    private SellerCodesBySellerCodesBean mSellerCodesBean;

    private String mChoice;

    private int mRequestPageIndex = 1;
    private final int mRequestRows = 25;

    private String distance = "";
    private String sellerType = "";
    private String districtCode = "";
    private String labelsId = "";
    private String sort = "";

    private int mSellerTypeIndex = -1;

    public RecyclerView mOneRecyclerView, mTowRecyclerView;
    public PopupWindow mPop;
    public TowRecyclerviewAdapter mTowRecyclerviewAdapter;
    public SellerSelectRecyclerviewAdapter mSellerSelectRecyclerviewAdapter;
    public List<HotBean> mDistrictList = new ArrayList<>();

    /**
     * 屏幕高度
     */
    private int mScreenHeight = 0;
    /**
     * 状态栏高度
     */
    private int mStatusHeight = 0;
    /**
     * 标题栏高度
     */
    private int mTitleHeight = 0;

    /**
     * 距离选择
     */
    private int mDistance = 4;
    /**
     * 业态选择
     */
    private int mTypeOfOperation = 0;
    /**
     * 排序选择
     */
    private int mSort = 0;
    /**
     * 区域选择
     */
    private int mArea = 0;//区域
    private int mLocation = 0;//区域

    private ScrollHandler mScrollHandler;
    private Intent mIntent;


    @Override
    public View initViews() {
        //注册事件
        EventBus.getDefault().register(this);
        return View.inflate(mActivity, R.layout.fragment_store, null);
    }

    @Override
    public void initData() {
        mLlStoreFilter.setVisibility(View.GONE);
        /**给RecyclerView设置布局*/
        mSwipeTarget.setLayoutManager(new FastScrollLinearLayoutManager(mActivity));
        /**适配器*/
        mQuickAdapter = new StoreAdapter(mActivity);
        mQuickAdapter.setOnLoadMoreListener(this, mSwipeTarget);
        mQuickAdapter.setHeaderView(getHeaderBannerView());
        mQuickAdapter.setHeaderView(getHeaderTypeView(), 1);
        mQuickAdapter.setHeaderView(getHeaderFilterView(), 2);
        mQuickAdapter.setLoadMoreView(new CustomLoadMoreView());
        mSwipeTarget.setAdapter(mQuickAdapter);

        String getSellerInfoBySellerInfo = SpDataUtils.get(mActivity, "getSellerInfoBySellerInfo", "");
        if (!TextUtils.isEmpty(getSellerInfoBySellerInfo)) {
            SellerBean sellerBean = new Gson().fromJson(getSellerInfoBySellerInfo, SellerBean.class);
            List<SellerInfoBean> sellerInfo = sellerBean.getSellerInfo();
            if (sellerInfo != null && sellerInfo.size() != 0) {
                mQuickAdapter.setNewData(sellerInfo);
            }
        }
        //初始化数据完成
        mSwipeToLoadLayout.setLoadMoreEnabled(false);

        //获取屏幕高度
        mScreenHeight = ScreenUtil.getScreenHeight(mActivity);
        //获取状态栏高度
        Rect rectangle = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        mStatusHeight = rectangle.top;
        //标题栏高度 50dp
        mTitleHeight = ScreenUtil.dip2px(mActivity, 50);
        mScrollHandler = new ScrollHandler(this);
        initListener();

    }

    //点击事件侧滑栏的打开与关闭
    private OnFragmentInteractionListener mListener;

    public void setListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void initListener() {
        mIvMenuImageheadOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onFragmentInteraction();
                }
            }
        });
        mFlSellerSearchQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 0);
            }
        });
        mLlIsSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), IsSearchActivity.class);
                startActivity(mIntent);
            }
        });
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && placeHolderView != null) {
                    mQuickAdapter.removeFooterView(placeHolderView);
                    placeHolderView = null;
                }
                if (mSwipeToLoadLayout.isRefreshing()) {
                    mSwipeToLoadLayout.setRefreshing(false);
                }

                int headerFilterTop = getRawY(mHeaderFilter);
                mLlStoreFilter.setVisibility(headerFilterTop > mTitleHeight
                        || mQuickAdapter == null
                        || mQuickAdapter.getData().size() == 0
                        ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mAutoSmooth) {
                        mAutoSmooth = false;
                        mScrollHandler.sendEmptyMessage(MESSAGE_SCROLL_AGAIN_FLAG);
                    }
                }
            }
        });

        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestAdList();
                requestSellerDistrictList();
                requestSellerTypes();
                initRequestSellerInfo();
            }
        });

        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, SellerMessageActivity.class);
                intent.putExtra("id", String.valueOf(((SellerInfoBean) adapter.getData().get(position)).getId()));
                intent.putExtra(Conversion.CHOICE, mChoice);
                mActivity.startActivity(intent);
            }
        });
    }

    private View getHeaderBannerView() {
        mBannerView = mActivity.getLayoutInflater().inflate(R.layout.store_item_banner, null);
        mIvBannerSingle = (ImageView) mBannerView.findViewById(R.id.iv_store_banner_single_bj);
        mRpvBannerMultiple = (RollPagerView) mBannerView.findViewById(R.id.rpv_store_banner);
        String getAdvertise3 = (String) SpDataUtils.get(mActivity, "getAdvertise3", "");
        if (!TextUtils.isEmpty(getAdvertise3)) {
            mAdModel = new Gson().fromJson(getAdvertise3, AdModel.class);
            setBannerView();
        }
        return mBannerView;
    }

    private void setBannerView() {
        if (mAdModel == null || mAdModel.getAdvertiseList() == null || mAdModel.getAdvertiseList().size() == 0) {
            mBannerView.setVisibility(View.GONE);
        } else {
            mBannerView.setVisibility(View.VISIBLE);
        }
        final List<AdModel.AdvertiseListBean> advertiseList = mAdModel.getAdvertiseList();
        if (advertiseList.size() == 1) {
            mRpvBannerMultiple.setVisibility(View.GONE);
            mIvBannerSingle.setVisibility(View.VISIBLE);
            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + advertiseList.get(0).getAdImgUrl(), mIvBannerSingle, "3");
            mIvBannerSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdModel.AdvertiseListBean adItemBean = advertiseList.get(0);
                    if (adItemBean != null && !TextUtils.isEmpty(adItemBean.getAdLink())
                            && StringUtils.isHttpPath(adItemBean.getAdLink())) {
                        String userId = SpUtils.getUserToken(mActivity);
                        String xPoint = SpUtils.getXpoint(mActivity);
                        String yPoint = SpUtils.getYpoint(mActivity);
                        String targetUrl = adItemBean.getAdLink() + userId + "&xpoint=" + xPoint + "&ypoint=" + yPoint;
                        Intent intent = new Intent(mActivity, WebAllActivity.class);
                        intent.putExtra("adItemBean", adItemBean);
                        intent.putExtra("adLink", targetUrl);
                        mActivity.startActivity(intent);
                    }
                }
            });
        } else {
            mIvBannerSingle.setVisibility(View.GONE);
            mRpvBannerMultiple.setVisibility(View.VISIBLE);

            TestNormalAdapter mTestNormalAdapter = new TestNormalAdapter(mRpvBannerMultiple, mActivity, advertiseList);
            mRpvBannerMultiple.setAdapter(mTestNormalAdapter);
            mRpvBannerMultiple.setHintView(new ColorPointHintView(mActivity, Conversion.FB4E30, Color.WHITE));
            mRpvBannerMultiple.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    AdModel.AdvertiseListBean adItemBean = advertiseList.get(position);
                    if (adItemBean != null && !TextUtils.isEmpty(adItemBean.getAdLink())) {
                        if (StringUtils.isHttpPath(adItemBean.getAdLink())) {
                            String userId = SpUtils.getUserToken(mActivity);
                            String xPoint = SpUtils.getXpoint(mActivity);
                            String yPoint = SpUtils.getYpoint(mActivity);
                            String targetUrl = adItemBean.getAdLink() + userId + "&xpoint=" + xPoint + "&ypoint=" + yPoint;
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl));
                            Intent intent = new Intent(mActivity, WebAllActivity.class);
                            intent.putExtra("adItemBean", adItemBean);
                            intent.putExtra("adLink", targetUrl);
                            mActivity.startActivity(intent);
                        }
                    }
                }
            });
            mTestNormalAdapter.setData(advertiseList);
        }
    }

    private View getHeaderTypeView() {
        mTypeView = mActivity.getLayoutInflater().inflate(R.layout.store_item_types, null);
        mRvStoreTypes = (RecyclerView) mTypeView.findViewById(R.id.rv_store_item_types);
        //初始化数据
        String sellerHead = (String) SpDataUtils.get(mActivity, "getSellerCodesBySellerCodes", "");
        if (!TextUtils.isEmpty(sellerHead)) {
            mSellerCodesBean = new Gson().fromJson(sellerHead, SellerCodesBySellerCodesBean.class);
            setTypeView();
        }
        return mTypeView;
    }

    private void setTypeView() {
        if (mSellerCodesBean == null || mSellerCodesBean.getSellerCodes() == null
                || mSellerCodesBean.getSellerCodes().size() == 0) {
            mTypeView.setVisibility(View.GONE);
        } else {
            mRvStoreTypes.setPadding(UiUtils.dip2px(mActivity, 10), UiUtils.dip2px(mActivity, 5), UiUtils.dip2px(mActivity, 10), UiUtils.dip2px(mActivity, 5));
            mTypeView.setVisibility(View.VISIBLE);
        }
        final List<SellerCodesBySellerCodesBean.SellerCodesBean> sellerCodes = mSellerCodesBean.getSellerCodes();

        SellerSubAdapter sellerSubAdapter = new SellerSubAdapter(mActivity, sellerCodes);
        sellerSubAdapter.setOnItemClickListener(new SellerSubAdapter.OnItemClickListener() {
            @Override
            public void onClick(int index) {
                sellerType = sellerCodes.get(index).getId();
                if (sellerType == null) {
                    sellerType = "";
                }
                distance = "";
                districtCode = "";
                labelsId = "";
                sort = "";
                mSellerTypeIndex = index;
                //筛选
                mDistance = 4;
                mTypeOfOperation = 0;
                mSort = 0;
                mArea = 0;
                initRequestSellerInfo();
            }
        });
        mRvStoreTypes.setLayoutManager(new GridLayoutManager(mActivity, 5));
        mRvStoreTypes.setAdapter(sellerSubAdapter);
        if (mHeaderFilter != null) {
            mHeaderFilter.setVisibility(View.VISIBLE);
        }
    }

    private View getHeaderFilterView() {
        mHeaderFilter = mActivity.getLayoutInflater().inflate(R.layout.store_item_filter_header, null);
        TextView tvArea = (TextView) mHeaderFilter.findViewById(R.id.tv_store_filter_area_header);
        TextView tvDistance = (TextView) mHeaderFilter.findViewById(R.id.tv_store_filter_distance_header);
        TextView tvTypes = (TextView) mHeaderFilter.findViewById(R.id.tv_store_filter_types_header);
        TextView tvSort = (TextView) mHeaderFilter.findViewById(R.id.tv_store_filter_sort_header);
        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothMoveToHeaderFilter();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        sellerSellerData();
                    }
                }, 200);
            }
        });
        tvDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothMoveToHeaderFilter();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showSellerDistancePopup();
                    }
                }, 200);
            }
        });
        tvTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothMoveToHeaderFilter();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showSellerTypePopup();
                    }
                }, 200);
            }
        });
        tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothMoveToHeaderFilter();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showSortPopup();
                    }
                }, 200);
            }
        });
        mHeaderFilter.setVisibility(mSellerCodesBean != null ? View.VISIBLE : View.GONE);
        return mHeaderFilter;
    }

    private View getFootEmptyView() {
        return mActivity.getLayoutInflater().inflate(R.layout.store_item_empty, null);
    }

    private View placeHolderView;

    private void addSpaceHolderView(int height) {

        int spaceHeight = height;
        int showHeight = mScreenHeight - mStatusHeight - mTitleHeight;
        int rangeHeight = mSwipeTarget.computeVerticalScrollRange();
        int extentHeight = mSwipeTarget.computeVerticalScrollExtent();

        int headerHeight = 0;
        if (mBannerView != null && mBannerView.getVisibility() == View.VISIBLE) {
            headerHeight += mBannerView.getHeight();
        }
        if (mTypeView != null && mTypeView.getVisibility() == View.VISIBLE) {
            headerHeight += mTypeView.getHeight();
        }

        if (rangeHeight == extentHeight) {
            spaceHeight = headerHeight + showHeight - rangeHeight;
        }
        placeHolderView = new Space(mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, spaceHeight);
        placeHolderView.setLayoutParams(layoutParams);
        mQuickAdapter.setFooterView(placeHolderView, mQuickAdapter.getFooterLayoutCount());
    }

    private View getFooterView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.store_item_footer, null);
    }

    public RecyclerView getSwipeTarget() {
        return mSwipeTarget;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (null != bundle) {
            mChoice = bundle.getString(Conversion.CHOICE);
            LogUtil.d("mChoice" + mChoice);
            if (mAdModel == null) {
                requestAdList();
            } else {
                setBannerView();
            }
            if (mSellerAddress == null) {
                requestSellerDistrictList();
            }
            if (mSellerCodesBean == null) {
                requestSellerTypes();
            } else {
                setTypeView();
            }

            distance = "";
            sellerType = "";
            districtCode = "";
            labelsId = "";
            sort = "";
            mSellerTypeIndex = -1;
            initRequestSellerInfo();
            mRlSellerTitleBar.setVisibility(View.GONE);
        } else {
            mRlSellerTitleBar.setVisibility(View.VISIBLE);
        }
    }

    private void initRequestSellerInfo() {
        mRequestPageIndex = 1;
        requestSellerInfo();
    }

    /**
     * 网络访问
     */
    private void requestSellerInfo() {
        mLlStoreFilter.setVisibility(View.GONE);
        RetrofitUtil.createService(SellerService.class)
                .getSellerInfoBySellerInfo(mXpoint, mYpoint,
                        sellerType, distance, districtCode,
                        labelsId, sort, mRequestPageIndex, String.valueOf(mRequestRows),
                        IpConfig.cityCode)
                .enqueue(new Callback<SellerBean>() {
                    @Override
                    public void onResponse(Call<SellerBean> call, Response<SellerBean> response) {
                        mSwipeToLoadLayout.setRefreshing(false);
                        if (response.isSuccessful()) {
                            SellerBean sellerBean = response.body();
                            if (sellerBean.getResultCode() == 1) {
                                List<SellerInfoBean> sellerInfo = sellerBean.getSellerInfo();
                                if (mRequestPageIndex == 1) {
                                    SpDataUtils.put(mActivity, "getSellerInfoBySellerInfo", new Gson().toJson(sellerBean));
                                    mQuickAdapter.setNewData(sellerInfo);
                                } else {
                                    mQuickAdapter.addData(sellerInfo);
                                }
                                mQuickAdapter.loadMoreComplete();

                                if (sellerInfo != null
                                        && sellerInfo.size() == mRequestRows) {
                                    mRequestPageIndex += 1;
                                } else {
                                    mQuickAdapter.removeAllFooterView();
                                    if (mQuickAdapter.getData() != null && mQuickAdapter.getData().size() != 0) {
                                        mQuickAdapter.addFooterView(getFooterView());
                                    }
                                    mQuickAdapter.loadMoreEnd();
                                }

                            } else if (sellerBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                mQuickAdapter.loadMoreFail();
                                Toasty.error(mActivity, sellerBean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SellerBean> call, Throwable t) {
                        mLlStoreFilter.setVisibility(View.GONE);
                        mQuickAdapter.loadMoreFail();
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                });
    }

    /**
     * 商家位置选择
     */
    private void sellerSellerData() {
        if (mSellerAddress == null) {
            return;
        }
        View view = UiUtils.inflate(mActivity, R.layout.pop_tow_address);
        mPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) {
            @Override
            public void showAsDropDown(View anchor) {
                if (Build.VERSION.SDK_INT >= 24) {
                    Rect rect = new Rect();
                    anchor.getGlobalVisibleRect(rect);
                    int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                    setHeight(h);
                }
                super.showAsDropDown(anchor);
            }
        };
        mPop.setFocusable(true);
        mPop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bj_4c4c4c)));
        mPop.showAsDropDown(mLlStoreFilter);
//        mPop.update();

        view.findViewById(R.id.pop_tow_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPop.dismiss();
            }
        });
        /*左列表*/
        mOneRecyclerView = (RecyclerView) view.findViewById(R.id.one_recyclerview);
        mOneRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        final OneRecyclerviewAdapter oneRecyclerviewAdapter = new OneRecyclerviewAdapter(mActivity, mSellerAddress.getDistrictList());
        mOneRecyclerView.setAdapter(oneRecyclerviewAdapter);
        oneRecyclerviewAdapter.setOnItemClickListener(new OneRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int position, int countyId) {
                int select = position + 1;
                mArea = select;
                oneRecyclerviewAdapter.setSelect(select);
                mDistrictList.clear();
                List<SellerAddress.DistrictListBean.DistrictObjectBean> districtObject = mSellerAddress.getDistrictList().get(position).getDistrictObject();
                for (int i = 0; i < districtObject.size(); i++) {
                    mDistrictList.add(new HotBean(districtObject.get(i).getDistrictId(), districtObject.get(i).getDistrictName()));
                }
                mTowRecyclerviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void ItemClicknearbyListener(int position) {
                mArea = position;
                oneRecyclerviewAdapter.setSelect(position);
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
                initRequestSellerInfo();
                mPop.dismiss();
            }
        });

        //记录位置
        oneRecyclerviewAdapter.setSelect(mArea);
        if (mArea == 0) {
            mDistrictList.clear();
            List<SellerAddress.DistrictHotListBean> districtHotList = mSellerAddress.getDistrictHotList();
            for (int i = 0; i < districtHotList.size(); i++) {
                mDistrictList.add(new HotBean(districtHotList.get(i).getPlaceCode(), districtHotList.get(i).getPlaceName()));
            }
            mTowRecyclerviewAdapter.notifyDataSetChanged();
        } else {
            mDistrictList.clear();
            List<SellerAddress.DistrictListBean.DistrictObjectBean> districtObject = mSellerAddress.getDistrictList().get(mArea - 1).getDistrictObject();
            for (int i = 0; i < districtObject.size(); i++) {
                mDistrictList.add(new HotBean(districtObject.get(i).getDistrictId(), districtObject.get(i).getDistrictName()));
            }
            mTowRecyclerviewAdapter.notifyDataSetChanged();
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
        mPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) {
            @Override
            public void showAsDropDown(View anchor) {
                if (Build.VERSION.SDK_INT >= 24) {
                    Rect rect = new Rect();
                    anchor.getGlobalVisibleRect(rect);
                    int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                    setHeight(h);
                }
                super.showAsDropDown(anchor);
            }
        };
        mPop.setFocusable(true);
        mPop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bj_4c4c4c)));
        mPop.showAsDropDown(mLlStoreFilter);
//        mPop.update();
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
        mSellerSelectRecyclerviewAdapter = new SellerSelectRecyclerviewAdapter(mActivity, mDistrictList, i1);
        mTowRecyclerView.setAdapter(mSellerSelectRecyclerviewAdapter);
        switch (type) {
            case 2:
                mSellerSelectRecyclerviewAdapter.setSelect(mDistance);
                break;
            case 3:
                mSellerSelectRecyclerviewAdapter.setSelect(mTypeOfOperation);
                mTowRecyclerView.scrollToPosition(mTypeOfOperation);
                break;
            case 4:
                mSellerSelectRecyclerviewAdapter.setSelect(mSort);
                break;
        }
        mSellerSelectRecyclerviewAdapter.setOnItemClickListener(new SellerSelectRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(int position, int countyId) {
                switch (type) {
                    case 2:
                        if (countyId == 0) {
                            distance = "";
                        } else {
                            distance = String.valueOf(countyId);
                        }
                        mDistance = position;
                        break;
                    case 3:
                        sellerType = "";
                        labelsId = "0".equals(String.valueOf(countyId)) ? "" : String.valueOf(countyId);
                        mTypeOfOperation = position;
                        break;
                    case 4:
                        sort = "0".equals(String.valueOf(countyId)) ? "" : String.valueOf(countyId);
                        mSort = position;
                        break;
                }
                initRequestSellerInfo();
                mPop.dismiss();
            }
        });
    }

    private void showSellerDistancePopup() {
        mDistrictList.clear();
        int[] distanceArray = {1000, 3000, 5000, 10000, 0};
        for (int i = 0; i < distanceArray.length; i++) {
            if (i == distanceArray.length - 1) {
                mDistrictList.add(new HotBean(distanceArray[i], "全市"));
            } else {
                mDistrictList.add(new HotBean(distanceArray[i], distanceArray[i] / 1000 + " km"));
            }
        }
        sellerTypeofoperationData(1, 1, 2);
    }

    private void showSellerTypePopup() {
        if (mSellerCodesBean == null || mSellerCodesBean.getSellerCodes() == null
                || mSellerCodesBean.getSellerCodes().size() == 0) {
            return;
        }

        mDistrictList.clear();
        List<SellerCodesBySellerCodesBean.SellerCodesBean> sellerCodes = mSellerCodesBean.getSellerCodes();
        if (sellerCodes != null && sellerCodes.size() > mSellerTypeIndex) {
            mDistrictList.add(new HotBean(0, "全部"));
            if (mSellerTypeIndex == 0 || mSellerTypeIndex == -1) {
                for (int i = 0; i < sellerCodes.size(); i++) {
                    SellerCodesBySellerCodesBean.SellerCodesBean sellerCodesBean = sellerCodes.get(i);
                    List<SellerCodesBySellerCodesBean.SellerCodesBean.SubDataBean> subData = sellerCodesBean.getSubData();
                    for (int j = 0; j < subData.size(); j++) {
                        mDistrictList.add(new HotBean(Integer.parseInt(subData.get(j).getId()), subData.get(j).getName()));
                    }
                }
            } else {
                SellerCodesBySellerCodesBean.SellerCodesBean sellerCodesBean = sellerCodes.get(mSellerTypeIndex);
                List<SellerCodesBySellerCodesBean.SellerCodesBean.SubDataBean> subData = sellerCodesBean.getSubData();
                for (int j = 0; j < subData.size(); j++) {
                    mDistrictList.add(new HotBean(Integer.parseInt(subData.get(j).getId()), subData.get(j).getName()));
                }
            }
        }
        sellerTypeofoperationData(1, 3, 3);
    }

    private void showSortPopup() {
        mDistrictList.clear();
        mDistrictList.add(new HotBean(0, "离我最近"));
        mDistrictList.add(new HotBean(304, "人气最高"));
        mDistrictList.add(new HotBean(303, "评价最好"));
        mDistrictList.add(new HotBean(301, "人均最低"));
        mDistrictList.add(new HotBean(302, "人均最高"));
        sellerTypeofoperationData(1, 1, 4);
    }

    /**
     * 广告获取的接口
     */
    public void requestAdList() {
        RetrofitUtil.createService(Ad.class).getAdvertise("3").enqueue(new Callback<AdModel>() {
            @Override
            public void onResponse(Call<AdModel> call, Response<AdModel> response) {
                if (response.isSuccessful()) {
                    mAdModel = response.body();
                    if (mAdModel.getResultCode() == 1) {
                        SpDataUtils.put(mActivity, "getAdvertise3", new Gson().toJson(mAdModel));
                        setBannerView();
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

    /**
     * 获取商家位置筛选信息
     */
    private void requestSellerDistrictList() {
        RetrofitUtil.createService(SellerService.class).getDistrictList().enqueue(new Callback<SellerAddress>() {
            @Override
            public void onResponse(Call<SellerAddress> call, Response<SellerAddress> response) {
                if (response.isSuccessful()) {
                    mSellerAddress = response.body();
                    SpDataUtils.put(mActivity, "getDistrictList", new Gson().toJson(response.body()));
                    if (mSellerAddress.getResultCode() != 1) {
                        Toasty.normal(mActivity, mSellerAddress.getResultDesc()).show();
                        mSellerAddress = null;
                    }
                }
            }

            @Override
            public void onFailure(Call<SellerAddress> call, Throwable t) {
                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();

            }
        });
    }

    /**
     * 业态获取接口
     */
    private void requestSellerTypes() {
        RetrofitUtil.createService(SellerService.class)
                .getSellerCodesBySellerCodesBean()
                .enqueue(new Callback<SellerCodesBySellerCodesBean>() {
                    @Override
                    public void onResponse(Call<SellerCodesBySellerCodesBean> call, Response<SellerCodesBySellerCodesBean> response) {
                        if (response.isSuccessful()) {
                            mSellerCodesBean = response.body();
                            if (mSellerCodesBean.getResultCode() == 1) {
                                SpDataUtils.put(mActivity, "getSellerCodesBySellerCodes", new Gson().toJson(mSellerCodesBean));
                                setTypeView();
                            } else {
                                Toasty.normal(mActivity, mSellerCodesBean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SellerCodesBySellerCodesBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirst = true;
        EventBus.getDefault().unregister(this);
    }

    boolean isFirst = true;

    /*侧边栏界面关闭了*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(LoodingData loodingData) {
        if (loodingData.isLooding() && loodingData.getPage() == 3 && isFirst) {
            isFirst = false;
            if (mAdModel == null) {
                requestAdList();
            } else {
                setBannerView();
            }
            if (mSellerAddress == null) {
                requestSellerDistrictList();
            }
            if (mSellerCodesBean == null) {
                requestSellerTypes();
            } else {
                setTypeView();
            }

            distance = "";
            sellerType = "";
            districtCode = "";
            labelsId = "";
            sort = "";
            mSellerTypeIndex = -1;

            initRequestSellerInfo();
            mQuickAdapter.setEmptyView(getFootEmptyView());
            mQuickAdapter.setHeaderFooterEmpty(true, true);
            mSwipeTarget.smoothScrollToPosition(0);
        }
    }

    @OnClick({R.id.tv_store_filter_area, R.id.tv_store_filter_distance, R.id.tv_store_filter_types, R.id.tv_store_filter_sort})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_store_filter_area:
                sellerSellerData();
                break;
            case R.id.tv_store_filter_distance:
                showSellerDistancePopup();
                break;
            case R.id.tv_store_filter_types:
                showSellerTypePopup();
                break;
            case R.id.tv_store_filter_sort:
                showSortPopup();
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mQuickAdapter.removeAllFooterView();
        requestSellerInfo();
    }

    public final class CustomLoadMoreView extends LoadMoreView {
        @Override
        public int getLayoutId() {
            return R.layout.quick_view_load_more;
        }

        @Override
        protected int getLoadingViewId() {
            return R.id.load_more_loading_view;
        }

        @Override
        protected int getLoadFailViewId() {
            return R.id.load_more_load_fail_view;
        }

        /**
         * isLoadEndGone()为true，可以返回0
         * isLoadEndGone()为false，不能返回0
         */
        @Override
        protected int getLoadEndViewId() {
            return 0;
        }
    }

    /**
     * 距离屏幕顶部的距离
     *
     * @param v
     * @return
     */
    private int getRawY(View v) {
        if (v != null) {
            return v.getTop() + getParentY(v.getParent()) - getSystemBarHeight();
        }
        return 0;
    }

    private int getParentY(ViewParent parent) {
        if (parent != null && parent instanceof ViewGroup) {
            return ((ViewGroup) parent).getTop() + getParentY(parent.getParent());
        }
        return 0;
    }

    private int getSystemBarHeight() {
        Rect rectangle = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    private static class ScrollHandler extends Handler {
        private final WeakReference<StoreFragment> mStoreFragment;

        private ScrollHandler(StoreFragment fragment) {
            mStoreFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mStoreFragment.get() == null) {
                return;
            }
            if (msg.what == MESSAGE_SCROLL_FLAG) {
                mStoreFragment.get().smoothMove();
            } else if (msg.what == MESSAGE_SCROLL_AGAIN_FLAG) {
                mStoreFragment.get().smoothMoveAgain();
            }
        }
    }

    private void smoothMoveToHeaderFilter() {
        mScrollHandler.sendEmptyMessage(MESSAGE_SCROLL_FLAG);
    }

    private boolean mAutoSmooth = false;

    private void smoothMove() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mAutoSmooth = true;
                int firstTop = getRawY(mHeaderFilter) - mTitleHeight;
                mSwipeTarget.smoothScrollBy(0, firstTop);
            }
        });
    }

    private void smoothMoveAgain() {
        final int secondTop = getRawY(mHeaderFilter) - mTitleHeight;
        if (secondTop > 0) {
            addSpaceHolderView(secondTop);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mSwipeTarget.smoothScrollBy(0, secondTop);
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (!TextUtils.isEmpty(scanResult) && scanResult.contains("www")) {
                Intent intent = new Intent(getActivity(), PaythebillActivity.class);
                startActivity(intent);
            } else {
                Toasty.normal(getActivity(), "请扫描陪你专属支付二维码!").show();
            }
        }
    }
}
