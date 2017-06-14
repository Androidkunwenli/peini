package com.jsz.peini.ui.activity.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.gen.SellerListHotBean;
import com.jsz.peini.gen.SellerListHotBeanDao;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.filter.HotWordBean;
import com.jsz.peini.model.search.SearchHot;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.serach.SerachService;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.activity.seller.SellerMessageActivity;
import com.jsz.peini.ui.adapter.seller.SellerSearchAdapter;
import com.jsz.peini.ui.adapter.seller.SellerSearchEndAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IsSearchActivity extends BaseActivity {

    @InjectView(R.id.issearch_search)
    ImageView mIssearchSearch;
    @InjectView(R.id.iv_clean)
    ImageView mIvClean;
    @InjectView(R.id.phone_edt)
    EditText mPhoneEdt;
    @InjectView(R.id.issearch_button)
    Button mIssearchButton;
    @InjectView(R.id.search_pop_xia)
    View mSearchPopXia;
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
    private IsSearchActivity mActivity;
    private SellerSearchAdapter mSearchAdapter;
    private SellerSearchEndAdapter mSearchEndAdapter;
    private String mEditText = "";
    private SellerListHotBeanDao mSellerListHotBeanDao;
    private List<SellerListHotBean> mListHotBeen = new ArrayList<>();
    private Intent mIntent;
    private String mChoice;
    private String mType;

    @Override
    public int initLayoutId() {
        return R.layout.activity_issearchactivty;
    }

    List<HotWordBean.DataBean> mList = new ArrayList<>();
    List<SearchHot.SellerListBean> searchBean = new ArrayList<>();

    @Override
    public void initView() {
        mActivity = IsSearchActivity.this;
        mChoice = getIntent().getStringExtra(Conversion.CHOICE);
        mType = getIntent().getStringExtra(Conversion.TYPE);
        mSwipeToLoadLayout.setVisibility(View.GONE);
        mIssearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneEdt.setText("");
            }
        });

    }

    //回显历史数据
    @Override
    protected void onResume() {
        super.onResume();
        // 实例化PoiSearch对象
        mSellerListHotBeanDao = PeiNiApp.sSellerListHotBeanDao;
        /*获取mHotBeanDao*/
        mListHotBeen = mSellerListHotBeanDao.queryBuilder().orderDesc(SellerListHotBeanDao.Properties.Id).limit(11).list();
        LogUtil.d("商家搜索大小" + mListHotBeen.size());
        if (mSearchAdapter != null) {
            mSearchAdapter.setListHot(mListHotBeen);
        }
    }

    @Override
    public void initData() {
               /*基本数据*/
        mTaskSearchRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
        mSearchAdapter = new SellerSearchAdapter(mActivity, mList, mListHotBeen);
        mTaskSearchRecyclerview.setAdapter(mSearchAdapter);
        /*搜索数据*/
        //不可刷新跟多
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mSearchEndAdapter = new SellerSearchEndAdapter(mActivity, searchBean);
        mSwipeTarget.setAdapter(mSearchEndAdapter);

    }

    @Override
    protected void initListener() {

        mPhoneEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LogUtil.d("点击了搜索跳转界面==");
                    String sellername = mPhoneEdt.getText().toString().trim();
                    if (TextUtils.isEmpty(sellername)) {
                        Toasty.normal(mActivity, "请输入要搜索的商家、品类、或者商圈").show();
                        return true;
                    }
                    mIntent = new Intent(mActivity, SellerTabulationActivity.class);
                    if (!TextUtils.isEmpty(mChoice)) {
                        mIntent.putExtra(Conversion.CHOICE, mChoice);
                    }
                    mIntent.putExtra(Conversion.SELLERINFONAME, sellername);
                    startActivity(mIntent);
                    if (!TextUtils.isEmpty(mType) && "NoFinish".equals(mType)) {
                        finish();
                    }
                    return true;
                }
                return false;
            }
        });
        mIvClean.setVisibility(mEditText.length() > 0 ? View.VISIBLE : View.GONE);
        mPhoneEdt.addTextChangedListener(getWatcher());
        mSearchAdapter.setOnClickListener(new SellerSearchAdapter.OnClickListener() {
            @Override
            public void ItemHotClick(int id, int searchType, String sellerinfoid, String sellername) {
                // TODO: 2017/4/4  不知道 type 代表的什么 所以直接 type 存储为 = 2
                SellerListHotBean sellerListHotBean = new SellerListHotBean();
                sellerListHotBean.setSearchType(2);
                sellerListHotBean.setSellerName(sellerinfoid);
                sellerListHotBean.setSellerAddress(sellerinfoid);
                removeInsertedData(sellerListHotBean);
                mSellerListHotBeanDao.insert(sellerListHotBean);
                initSearchByHotWordNetWork(sellername);
                mIntent = new Intent(mActivity, SellerTabulationActivity.class);
                if (!TextUtils.isEmpty(mChoice)) {
                    mIntent.putExtra(Conversion.CHOICE, mChoice);
                }
                mIntent.putExtra(Conversion.SELLERINFONAME, sellername);
                startActivity(mIntent);
                if (!TextUtils.isEmpty(mType) && "NoFinish".equals(mType)) {
                    finish();
                }
            }

            @Override
            public void itemClick(int searchType, String sellerinfoid, String sellername) {
                /***/
                switch (searchType) {
                    case 1:
                        mIntent = new Intent(mActivity, SellerMessageActivity.class);
                        if (!TextUtils.isEmpty(mChoice)) {
                            mIntent.putExtra(Conversion.CHOICE, mChoice);
                        }
                        mIntent.putExtra(Conversion.ID, sellerinfoid);
                        startActivity(mIntent);
                        if (!TextUtils.isEmpty(mType) && "NoFinish".equals(mType)) {
                            finish();
                        }
                        break;
                    case 2:
                        mIntent = new Intent(mActivity, SellerTabulationActivity.class);
                        if (!TextUtils.isEmpty(mChoice)) {
                            mIntent.putExtra(Conversion.CHOICE, mChoice);
                        }
                        mIntent.putExtra(Conversion.SELLERINFONAME, sellername);
                        startActivity(mIntent);
                        if (!TextUtils.isEmpty(mType) && "NoFinish".equals(mType)) {
                            finish();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDetele() {
//                mSellerListHotBeanDao.deleteAll();
//                mSearchAdapter.setBottomCount(0);
                showDeleteConfirmDialog();
            }
        });

        mSearchEndAdapter.setItemClickListener(new SellerSearchEndAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int searchType, String sellerName, String id, String sellerAddress) {
                SellerListHotBean sellerListHotBean = new SellerListHotBean();
                sellerListHotBean.setSearchType(searchType);
                sellerListHotBean.setSellerName(sellerName);
                sellerListHotBean.setSellerInfoId(id);
                sellerListHotBean.setSellerAddress(sellerAddress);
                removeInsertedData(sellerListHotBean);
                initSearchByHotWordNetWork(sellerName);
                mSellerListHotBeanDao.insert(sellerListHotBean);
                /***/
                switch (searchType) {
                    case 1:
                        mIntent = new Intent(mActivity, SellerMessageActivity.class);
                        if (!TextUtils.isEmpty(mChoice)) {
                            mIntent.putExtra(Conversion.CHOICE, mChoice);
                        }

                        mIntent.putExtra(Conversion.ID, id);
                        startActivity(mIntent);
                        if (!TextUtils.isEmpty(mType) && "NoFinish".equals(mType)) {
                            finish();
                        }
                        break;
                    case 2:
                        mIntent = new Intent(mActivity, SellerTabulationActivity.class);
                        if (!TextUtils.isEmpty(mChoice)) {
                            mIntent.putExtra(Conversion.CHOICE, mChoice);
                        }
                        mIntent.putExtra(Conversion.SELLERINFONAME, sellerName);
                        startActivity(mIntent);
                        if (!TextUtils.isEmpty(mType) && "NoFinish".equals(mType)) {
                            finish();
                        }
                        break;
                    default:
                        break;
                }
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
                        mSellerListHotBeanDao.deleteAll();
                        mSearchAdapter.setBottomCount(0);
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

    /**
     * 查库去重
     */
    private void removeInsertedData(SellerListHotBean sellerListHotBean) {
        if (sellerListHotBean == null || mListHotBeen == null || mListHotBeen.size() == 0) {
            return;
        }
        try {
            for (SellerListHotBean hotBean : mListHotBeen) {
                if (hotBean.getSearchType() == 1) {
                    if (!TextUtils.isEmpty(sellerListHotBean.getSellerInfoId())
                            && sellerListHotBean.getSellerInfoId().equals(hotBean.getSellerInfoId())) {
                        mSellerListHotBeanDao.deleteByKey(hotBean.getId());
                        return;
                    }
                } else if (hotBean.getSearchType() == 2) {
                    if (!TextUtils.isEmpty(sellerListHotBean.getSellerName())
                            && sellerListHotBean.getSellerName().equals(hotBean.getSellerName())) {
                        mSellerListHotBeanDao.deleteByKey(hotBean.getId());
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initSearchByHotWordNetWork(String hotName) {
        RetrofitUtil.createService(TaskService.class)
                .searchByHotWord(hotName, 1)
                .enqueue(new RetrofitCallback<SuccessfulBean>() {
                    @Override
                    public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                    }
                });
    }

    @Override
    public void initInternet() {
        RetrofitUtil.createService(SerachService.class)
                .getHotWord().enqueue(new Callback<HotWordBean>() {
            @Override
            public void onResponse(Call<HotWordBean> call, Response<HotWordBean> response) {
                LogUtil.d("这个是什么线程" + (Looper.getMainLooper().getThread() == Thread.currentThread()));
                if (response.isSuccessful()) {
                    HotWordBean body = response.body();
                    if (body.getResultCode() == 1) {
                        mList.clear();
                        mList.addAll(body.getData());
                        mSearchAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<HotWordBean> call, Throwable t) {
            }
        });
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
                mEditText = mPhoneEdt.getText().toString().trim();
                if (mEditText.length() > 0) {
                    mSwipeToLoadLayout.setVisibility(View.VISIBLE);
                    initNetWork();
                    mIvClean.setVisibility(View.VISIBLE);
                } else {
                    mSwipeToLoadLayout.setVisibility(View.GONE);
                    mIvClean.setVisibility(View.GONE);
                }
            }
        };
    }

    private void initNetWork() {
        RetrofitUtil.createService(SerachService.class).searchFastSeller(SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity), mEditText).enqueue(new RetrofitCallback<SearchHot>() {
            @Override
            public void onSuccess(Call<SearchHot> call, Response<SearchHot> response) {
                if (response.isSuccessful()) {
                    SearchHot body = response.body();
                    if (body.getResultCode() == 1) {
                        searchBean.clear();
                        searchBean.addAll(body.getSellerList());
                        mSearchEndAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchHot> call, Throwable t) {

            }
        });
    }
}
