package com.jsz.peini.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.eventbus.LoodingData;
import com.jsz.peini.model.eventbus.SquareReleaseRefreshSquarList;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.square.AddCommentListBean;
import com.jsz.peini.model.square.AddLikeListBean;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.LikeListBean;
import com.jsz.peini.model.square.SquareBean;
import com.jsz.peini.presenter.ad.Ad;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.threadpool.Priority;
import com.jsz.peini.threadpool.PriorityExecutor;
import com.jsz.peini.threadpool.PriorityRunnable;
import com.jsz.peini.ui.activity.square.LikeListActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.ui.adapter.square.SquareAdapter;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.KeyBoardUtils;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpDataUtils;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ProgressActivity;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import net.bither.util.NativeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kunwe on 2016/11/25.
 * 广场界面
 */

public class SquareFragment extends BaseFragment {

    @InjectView(R.id.et_input)
    EditText mEtInput;
    @InjectView(R.id.btn_send)
    Button mBtnSend;
    @InjectView(R.id.ll_input)
    LinearLayout mLlInput;
    //数据加载
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;

    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private String mUserId;
    private int mId;
    private int mPosition;
    private String mToUserId;
    private String mContent;
    private String mSpId;
    private boolean mIsShowEdittext;
    private Popwindou pop;

    private SquareAdapter mSquareAdapter;
    /**
     * 空间的数据
     */
    List<SquareBean.SquareListBean> mSquareList = new ArrayList<>();

    /**
     * 广场的广告信息
     */
    List<AdModel.AdvertiseListBean> mAdModels = new ArrayList<>();

    private String mString = "";
    private String mType = "0";
    private String otherId = "";
    private int mIndex;
    private LinearLayoutManager mManager;
    //状态栏高度
    private int mStatusHeight;

    @Override
    public View initViews() {
        //注册事件
        EventBus.getDefault().register(this);
        return UiUtils.inflate(mActivity, R.layout.fragment_square);
    }

    public RecyclerView getSwipeTarget() {
        return mSwipeTarget;
    }

    /*获取数据*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            mType = bundle.getString(Conversion.TYPE);
            otherId = bundle.getString(Conversion.TOKEN);
            initNetWorkAd();
            mPageNow = 1;
            initNetWork();
        }
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //初始化标题栏高度
        mTitleHeight = (int) (50 * getResources().getDisplayMetrics().density);
        //获取状态栏高度
        Rect rectangle = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        mStatusHeight = rectangle.top;
        //初始化屏幕高度
        mScreenHeight = UiUtils.getScreenHeight(mActivity);
        if ("1".equals(mType)) {
            mSwipeTarget.setBackgroundColor(getResources().getColor(R.color.white000));
        }
        mLlInput.setVisibility(View.GONE);
        //数据初始化
        Gson gson = new Gson();//本地
        String square = (String) SpDataUtils.get(mActivity, "square", "");
        if (!TextUtils.isEmpty(square)) {
            SquareBean squareBean = gson.fromJson(square, SquareBean.class);
            if (squareBean != null && squareBean.getSquareList() != null && squareBean.getSquareList().size() != 0) {
                List<SquareBean.SquareListBean> squareList = squareBean.getSquareList();
                mSquareList.addAll(squareList);
            }
        }
        //初始化结束
        mSquareAdapter = new SquareAdapter(mActivity, mSquareList, mAdModels, mType);
//        mManager = new FastScrollLinearLayoutManager(mActivity);
        mManager = new LinearLayoutManager(mActivity);
        mSwipeTarget.setLayoutManager(mManager);
        ((DefaultItemAnimator) mSwipeTarget.getItemAnimator()).setSupportsChangeAnimations(false);
        mSwipeTarget.setAdapter(mSquareAdapter);

        //滑动监听
        initModel();

        /**上拉加载*/
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initNetWorkAd();
                mPageNow = 1;
                initNetWork();
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        /**下拉刷新*/
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNetWork();
            }
        });
    }

    private int mPageNow = 1;
    private int mRequestRows = 10;

    /**
     * 发起网络请求
     */
    private void initNetWork() {
        /*获取空间数据*/
        RetrofitUtil.createService(SquareService.class)
                .getSquareInfoList(mUserToken, otherId, mType, mPageNow, mRequestRows)
                .enqueue(new Callback<SquareBean>() {
                    @Override
                    public void onResponse(Call<SquareBean> call, Response<SquareBean> response) {
                        if (response.isSuccessful()) {
                            int resultCode = response.body().getResultCode();
                            SquareBean body = response.body();
                            if (resultCode == 1) {
                                List<SquareBean.SquareListBean> squareList = body.getSquareList();
                                if (mPageNow == 1) {
                                    SpDataUtils.put(mContext, "square", new Gson().toJson(body));
                                    mSquareList.clear();
                                    mSquareList.addAll(squareList);
                                    if (!TextUtils.isEmpty(mType) && "1".equals(mType) && mSquareList.size() == 0) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "发个动态,让大家认识一下吧!");
                                    } else if (!TextUtils.isEmpty(mType) && "2".equals(mType) && mSquareList.size() == 0) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "这个人的广场空空如也!");
                                    } else if (!TextUtils.isEmpty(mType) && "0".equals(mType) && mSquareList.size() == 0) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "广场暂无数据!");
                                    } else {
                                        mPaProgress.showContent();
                                    }
                                } else {
                                    mSquareList.addAll(squareList);
                                }
                                mSquareAdapter.setLooding(0);
                                if (squareList.size() == mRequestRows) {
                                    mPageNow += 1;
                                } else {
                                    mSquareAdapter.setLooding(1);
                                }
                            } else if (resultCode == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (resultCode == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                                mSquareAdapter.setLooding(2);
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                mSquareAdapter.setLooding(2);
                            }
                        }
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉

                    }

                    @Override
                    public void onFailure(Call<SquareBean> call, Throwable t) {
                        mSquareAdapter.setLooding(2);
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                    }
                });

    }

    /**
     * 获取广告界面
     */
    private void initNetWorkAd() {
        RetrofitUtil.createService(Ad.class).getAdvertise("4").enqueue(new Callback<AdModel>() {
            @Override
            public void onResponse(Call<AdModel> call, Response<AdModel> response) {
                if (response.isSuccessful()) {
                    AdModel body = response.body();
                    if (body.getResultCode() == 1) {
                        mAdModels.clear();
                        mAdModels.addAll(body.getAdvertiseList());
                    }
                }
            }

            @Override
            public void onFailure(Call<AdModel> call, Throwable t) {
            }
        });
    }

    /**
     * 滑动的监听
     */
    public void initModel() {
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                        //当屏幕停止滚动，加载图片
                        try {
                            if (mActivity != null) Glide.with(mActivity).resumeRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                        //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                    case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to hong final position while not under outside control.
                        //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                        try {
                            if (mActivity != null) Glide.with(mActivity).pauseRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy != 0) {
                    isShowEditText(false, "");
                }
            }
        });
        //界面的回调
        mSquareAdapter.setOnCommentClickListener(new SquareAdapter.OnCommentClickListener() {
            @Override
            public void OnUserId(String id, String userId, int position, int SpId) {
                LogUtil.i("这个是主界面的回调", "-" + id + "-" + userId + "-" + position);
                startSquareActivity(userId);
            }

            @Override
            public void OnToUserId(String id, String toUserId, int position, int SpId) {
                startSquareActivity(toUserId);
            }

            @Override
            public void OnContent(String id, String UserId, String UserNickname, String toUserId,
                                  String ToUserNickname, String Content, int position, int SpId, int index, boolean longItemContent) {
                mString = UserId;
                mIndex = index;
                mId = mSquareList.get(index).getId();
                mPosition = position;
                mContent = Content;
                mSpId = mSquareList.get(index).getCommentList().get(position).getId() + "";
                mUserId = muId;
                mToUserId = mSquareList.get(index).getCommentList().get(position).getToUserId();
                if (longItemContent) {
                    Toasty.normal(mActivity, "长按吗" + position).show();
                } else {
                    if (UserId.equals(SpUtils.getUserToken(mActivity))) {
                        LogUtil.i("这个是广场界面", "这里要执行删除操作muId" + muId + "mUserId" + mUserId);
                        initSelect();
                    } else {
                        isShowEditText(true, UserNickname);
                    }
                }
            }

            @Override
            public void OnItemClick(String id, String Content, int position, int SpId, boolean longItemContent) {

            }

            @Override
            public void OnDelete(final int index) {
                mIndex = index;
                final String id = mSquareList.get(index).getId() + "";
                LogUtil.d("执行空间的删除操作--删除的ID--" + id);
                if (StringUtils.isNoNull(id)) {
                    new AlertDialog.Builder(mActivity)
                            .setMessage("确定删除吗？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    dialog.dismiss();
                                    initNetWorkDelete(id, index);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                    dialog.dismiss();
                                }
                            }).show();//在按键响应事件中显示此对话框
                }
            }

            @Override
            public void OnContentId(int index) {
//                mSwipeTarget.smoothScrollToPosition(index + 1);
                mString = "0";
                mId = mSquareList.get(index).getId();
                mUserId = muId;
                mToUserId = "0";
                mIndex = index;
                isShowEditText(true, "");
            }

            @Override
            public void OnLike(final int index) {
                final int id = mSquareList.get(index).getId();
                String userId = mSquareList.get(index).getUserId();
                LogUtil.e("点赞的按钮", "广场的ID" + id + "UserId" + userId);
                mSquareAdapter.setLikeList(index);
                ExecutorService executorService = new PriorityExecutor(5, false);
                PriorityRunnable priorityRunnable = new PriorityRunnable(Priority.NORMAL, new Runnable() {
                    @Override
                    public void run() {
                        goSquareLike(id + "", muId + "", index);
                    }
                });
                executorService.execute(priorityRunnable);
            }

            @Override
            public void onID(int index, String id, String userId) {
                startSquareActivity(mSquareList.get(index).getUserId() + "");
            }

            @Override
            public void setOnItemLikeListClickListener(List<LikeListBean> likeList, int index) {
                Intent intent = new Intent(mActivity, LikeListActivity.class);
                intent.putExtra(Conversion.LIST, (Serializable) likeList);
                startActivity(intent);
            }

            @Override
            public void loadMoreData(RecyclerView.ViewHolder holder) {//自动加载的回调
                initNetWork();
            }

            @Override
            public void setLoodingNetWork() {
                mSquareAdapter.setLooding(0);
                initNetWork();
            }
        });
    }

    /**
     * 标题栏高度
     */
    private int mTitleHeight = 0;
    /**
     * 屏幕高度
     */
    private int mScreenHeight = 0;

    /**
     * 跳转界面 到个人空间
     */
    private void startSquareActivity(String userId) {
        if (mUserToken.equals(userId)) {
            MiSquareActivity.actionShow(mActivity);
        } else {
            Intent intent = new Intent(mActivity, TaSquareActivity.class);
            LogUtil.d("点击后面人的id" + mUserToken + "------------" + userId);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
    }

    /**
     * 删除按钮
     *
     * @param id
     * @param index
     */
    private void initNetWorkDelete(String id, int index) {
        mSquareList.remove(index);
        mSquareAdapter.notifyDataSetChanged();
        RetrofitUtil.createService(SquareService.class).deleteSquareAll(SpUtils.getUserToken(mActivity), id)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                LogUtil.d("删除成功返回的数据" + response.body().toString());
                                if (mSquareList.size() == 0) {
                                    mPaProgress.showContent();
                                }
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
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


    @OnClick(R.id.btn_send)
    public void onClick() {
        String inputContentStr = mEtInput.getText().toString().trim();
        if (TextUtils.isEmpty(inputContentStr)) {
            Toasty.normal(mActivity, "请输入评论内容").show();
            isShowEditText(false, "");
            return;
        }
        initNetWorkCommon(mId + "", mUserToken, mString, inputContentStr);
        mEtInput.setText("");
        KeyBoardUtils.hideKeyBoard(mActivity, mEtInput);
        mLlInput.setVisibility(View.GONE);

    }

    /**
     * squareInfoId	Int		广场id
     * userId	Int		评论用户id
     * toUserId	Int		被回复用户id
     * content	String		评论内容
     * 这个是评论的数据
     */
    private void initNetWorkCommon(String squareInfoId, String mUserId, String mToUserId, String content) {
        LogUtil.i("这个是广场界面的唯一标识", "第一个参数" + squareInfoId);
        LogUtil.i("这个是广场界面的唯一标识", "第二个参数" + mUserId);
        LogUtil.i("这个是广场界面的唯一标识", "第三个参数" + mToUserId);
        LogUtil.i("这个是广场界面的唯一标识", "第四个参数" + content);
        RetrofitUtil.createService(SquareService.class)
                .goComment(squareInfoId, mUserId, mToUserId, content)
                .enqueue(new Callback<AddCommentListBean>() {
                    @Override
                    public void onResponse(Call<AddCommentListBean> call, Response<AddCommentListBean> response) {
                        if (response.isSuccessful()) {
                            AddCommentListBean body = response.body();
                            if (body.getResultCode() == 1) {
                                mSquareAdapter.setCommentList(body.getCommentList(), mIndex);
                                KeyBoardUtils.hideKeyBoard(mActivity, mEtInput);
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<AddCommentListBean> call, Throwable t) {
//                        ToastUtils.showToast(mActivity, t.getMessage());
                        Toasty.normal(mActivity, "网络不给力，请稍后重试").show();
                    }
                });
    }

    /**
     * 删除的按钮
     */
    private void initDelete(String spId, final int index, final int position) {
        RetrofitUtil.createService(SquareService.class)
                .delComment(spId, mUserToken)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                mSquareAdapter.setDeleteContent(index, position);
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
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
     * 这个 是点赞的按钮
     */
    private void goSquareLike(String squareId, String userId, final int index) {
        RetrofitUtil.createService(SquareService.class)
                .goSquareLike(squareId, userId)
                .enqueue(new Callback<AddLikeListBean>() {
                    @Override
                    public void onResponse(Call<AddLikeListBean> call, Response<AddLikeListBean> response) {
                        if (response.isSuccessful()) {
                            AddLikeListBean body = response.body();
                            if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() != 1) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<AddLikeListBean> call, Throwable t) {
                        Toasty.error(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    private void initSelect() {
        pop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.fragment_square));
        View mView = UiUtils.inflate(mActivity, R.layout.item_popupwindows_delete);
        pop.init(mView, Gravity.BOTTOM, true);
        mView.findViewById(R.id.item_popupwindows_Photo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LogUtil.d("这个是要删除的弹窗" + mId + " mspis" + mSpId + "mp" + mPosition);
                initDelete(mSpId, mIndex, mPosition);
                pop.dismiss();
            }
        });
        mView.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();

            }
        });
    }

    public void isShowEditText(boolean isShow, String userNickname) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString((TextUtils.isEmpty(userNickname) ? "评论" : "回复") + userNickname + ":");
        SpannableString nulls = new SpannableString(userNickname);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        mEtInput.setHint(new SpannedString(ss));
        mEtInput.setHintTextColor(getResources().getColor(R.color.text999));
        mLlInput.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mEtInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) mEtInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            mEtInput.setHint(new SpannedString(ss));
            imm.showSoftInput(mEtInput, 0);
        } else {
            mEtInput.setHint(new SpannedString(nulls));
            imm.hideSoftInputFromWindow(mEtInput.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFires = true;
        EventBus.getDefault().unregister(this);
    }

    /*发布广场成功*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(SquareReleaseRefreshSquarList squarList) {
        if (squarList.isRefresh()) {
            initNetWorkAd();
            mPageNow = 1;
            initNetWork();
            mSwipeTarget.scrollToPosition(0);
        }
    }

    /*侧边栏界面关闭了*/
    boolean isFires = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(LoodingData loodingData) {
        if (loodingData.isLooding() && loodingData.getPage() == 2 && isFires) {
            isFires = false;
            initNetWorkAd();
            mPageNow = 1;
            initNetWork();
            mSwipeTarget.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000 && data != null) {
            Bundle extra = data.getExtras();
            String string = extra.getString(Conversion.BEAN);
            if (!TextUtils.isEmpty(string)) {
                final SquareBean.SquareListBean squareListBean = new Gson().fromJson(string, SquareBean.SquareListBean.class);
                mSquareList.add(0, squareListBean);
                mSquareAdapter.notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<ImageListBean> imageList = squareListBean.getImageList();
                        inItUpdata(squareListBean.getUserId(), squareListBean.getLatitude(), squareListBean.getLongitude(), squareListBean.getContent(), squareListBean.getAddress(), imageList);
                    }
                }).start();
            }
        }
    }

    public void inItUpdata(String muId, String latitude, String longitude, String trim, String address, List<ImageListBean> imageList) {
        if (imageList != null && imageList.size() != 0) {
//            for (ImageListBean listBean : imageList) {
//                BitmapAndStringUtils.saveBitmaps(mActivity, listBean.getImageSmall(), 0, new ImageCompressListener() {
//                    @Override
//                    public void onCompressSuccess(String images) {
//                        LogUtil.d(images);
//                    }
//
//                    @Override
//                    public void onCompressFailed(String images, String msg) {
//
//                    }
//                });
//            }
            MultipartBody.Part[] parts = new MultipartBody.Part[imageList.size()];
            for (int i = 0; i < parts.length; i++) {
                File file = new File(NativeUtil.compressBitmap(imageList.get(i).getImageSmall(), Conversion.LOCAL_IMAGE_CACHE_PATH + "/squareImage" + System.currentTimeMillis() + ".jpg"));
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                parts[i] = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            }
            spaceUpload(muId, trim, address, latitude, longitude, parts);
        } else {
            spaceUpload(muId, trim, address, latitude, longitude);
        }
    }

    /*无图发布空间信息*/
    private void spaceUpload(String muId, String trim, String address, String sexLatitude, String sexLongitude) {
        RetrofitUtil.createService(SquareService.class).setSquareInfoBySquareInfo(SpUtils.getUserToken(mActivity), sexLatitude + "", sexLongitude + "", trim + "", address + "")
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                mPageNow = 1;
                                initNetWork();
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
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

    /*有图发布空间信息*/
    private void spaceUpload(String muId, String trim, String address, String sexLatitude, String sexLongitude, MultipartBody.Part[] parts) {
        RetrofitUtil.createService(SquareService.class).setSquareInfoBySquareInfo(SpUtils.getUserToken(mActivity), sexLatitude + "",
                sexLongitude + "", trim + "", address + "", parts).enqueue(new Callback<SuccessfulBean>() {
            @Override
            public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                if (response.isSuccessful()) {
                    SuccessfulBean body = response.body();
                    if (body.getResultCode() == 1) {
                        mPageNow = 1;
                        initNetWork();
                    } else if (body.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    } else if (body.getResultCode() == 0) {
                        Toasty.normal(mActivity, body.getResultDesc()).show();
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
}
