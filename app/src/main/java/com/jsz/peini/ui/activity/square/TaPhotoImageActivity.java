package com.jsz.peini.ui.activity.square;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.UserImageAllByUserId;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.adapter.square.TaPhotoAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 他人相册
 * Created by huizhe.ju on 2017/2/25.
 */
public class TaPhotoImageActivity extends BaseActivity {

    public static final String EXTRA_OTHER_USER_ID = "extra_other_user_id";

    @InjectView(R.id.title)
    TextView mTvTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private Context mContext;
    private TaPhotoAdapter mTaPhotoAdapter;
    private List<UserImageAllByUserId.UserImageListBean> mAllImagesList;

    private boolean mListShouldInit = true;
    private ArrayList<ImageListBean> mList;

    private String mOtherUserId;
    private int mPageNumber = 1;

    public static void actionShow(Context context, String userId) {
        Intent intent = new Intent(context, TaPhotoImageActivity.class);
        intent.putExtra(EXTRA_OTHER_USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    public int initLayoutId() {
        return R.layout.activity_taphotoimage;
    }

    @Override
    public void initView() {
        mContext = this;
        mTvTitle.setText("他人相册");
    }

    @Override
    public void initData() {
        mOtherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);
        if (TextUtils.isEmpty(mOtherUserId)) {
            Toasty.normal(mContext, "数据异常").show();
            return;
        }
        mAllImagesList = new ArrayList<>();
        mTaPhotoAdapter = new TaPhotoAdapter(mContext, mAllImagesList);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mSwipeTarget.setAdapter(mTaPhotoAdapter);

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


        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                        //当屏幕停止滚动，加载图片
                        try {
                            if (mContext != null) Glide.with(mContext).resumeRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                        //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                    case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to hong final position while not under outside control.
                        //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                        try {
                            if (mContext != null) Glide.with(mContext).pauseRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        mTaPhotoAdapter.setOnClickPhotoListener(new TaPhotoAdapter.OnClickPhotoListener() {
            @Override
            public void setClickPhoto(int position, int index) {
                int showIndex = 0;
                if (mList == null) {
                    mList = new ArrayList<>();
                    mListShouldInit = true;
                }
                if (mListShouldInit) {
                    mList.clear();
                }

                for (int i = 0; i < mAllImagesList.size(); i++) {
                    List<ImageListBean> userImageAll = mAllImagesList.get(i).getUserImageAll();
                    if (mListShouldInit) {
                        mList.addAll(userImageAll);
                    }

                    if (i < position && userImageAll != null) {
                        showIndex += userImageAll.size();
                    }
                }
                showIndex += index;
                mListShouldInit = false;

                Intent intent = new Intent(mContext, LargerImageActivity.class);
                intent.putExtra(Conversion.LARGERIMAGEACTIVITY, mList);
                intent.putExtra(Conversion.SHOWINDEX, showIndex);
                intent.putExtra(Conversion.TYPE, 1);
                mContext.startActivity(intent);
            }
        });
        inItNetWork(true);
    }

    /**
     * 点击事件
     */
    @OnClick(R.id.toolbar)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar: //返回的事件
                finish();
                break;
        }
    }

    private void inItNetWork(final boolean isFirst) {
        if (isFirst) {
            mPageNumber = 1;
        } else {
            mPageNumber++;
        }
        RetrofitUtil.createService(SquareService.class)
                .getUserImageAllByUserId(mOtherUserId, mPageNumber, 5)
                .enqueue(new Callback<UserImageAllByUserId>() {
                    @Override
                    public void onResponse(Call<UserImageAllByUserId> call, Response<UserImageAllByUserId> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        if (response.isSuccessful()) {
                            UserImageAllByUserId body = response.body();
                            if (body != null) {
                                int resultCode = body.getResultCode();
                                if (resultCode == 1) {
                                    List<UserImageAllByUserId.UserImageListBean> userImageList = response.body().getUserImageList();
                                    mListShouldInit = true;
                                    if (isFirst) {
                                        mAllImagesList.clear();
                                        mAllImagesList.addAll(userImageList);
                                    } else {
                                        mAllImagesList.addAll(userImageList);
                                    }
                                    mTaPhotoAdapter.notifyDataSetChanged();
                                } else if (resultCode == 9) {
                                    LoginDialogUtils.isNewLogin((TaPhotoImageActivity) mContext);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserImageAllByUserId> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉

                    }
                });
    }
}
