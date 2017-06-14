package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.square.TaTaskBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.ui.adapter.square.TaTaskAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ProgressActivity;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by th on 2017/2/8.
 */
public class TaTaskActivity extends BaseActivity {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.iv_imageHead)
    CircleImageView mIvImageHead;
    @InjectView(R.id.tv_nickName)
    TextView mTvNickName;
    @InjectView(R.id.tv_sex)
    ImageView mTvSex;
    @InjectView(R.id.tv_age)
    TextView mTvAge;
    @InjectView(R.id.tv_industry)
    TextView mTvIndustry;
    @InjectView(R.id.iv_gold)
    ImageView mIvGold;
    @InjectView(R.id.iv_buy)
    ImageView mIvBuy;
    @InjectView(R.id.iv_integrity)
    ImageView mIvIntegrity;
    @InjectView(R.id.ll_age_sex)
    LinearLayout mLlAgeSex;
    @InjectView(R.id.iv_toolbar_image)
    ImageView mIvToolbarImage;
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;

    private TaTaskActivity mActivity;
    List<TaTaskBean.TaskInfoByUserIdListBean> mList = new ArrayList<>();

    private TaTaskAdapter mTaTaskAdapter;
    private String mUserId;
    private String name;
    private String label;
    private String sex;
    private String age;
    private String image;
    private int page = 1;

    @Override
    public int initLayoutId() {
        return R.layout.activity_tatask;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("他人任务");
        mTitle.setTextColor(UiUtils.getResources().getColor(R.color.white000));
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mKuntoolbar.setBackgroundResource(R.color.RED_FB4E30);
        mToolbar.setBackgroundResource(R.color.RED_FB4E30);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra(Conversion.USERID);
        name = intent.getStringExtra(Conversion.NAME);
        age = intent.getStringExtra(Conversion.AGE);
        sex = intent.getStringExtra(Conversion.SEX);
        label = intent.getStringExtra(Conversion.LABEL);
        image = intent.getStringExtra(Conversion.IMAGE);

//        Drawable drawable = mIvToolbarImage.getDrawable();
//        mIvToolbarImage.setImageDrawable(Conversion.tintDrawable(drawable, ColorStateList.valueOf(Color.WHITE)));

        mIvGold.setVisibility(!"0".equals(intent.getStringExtra(Conversion.GOLD)) ? View.VISIBLE : View.GONE);
        mIvBuy.setVisibility(!"0".equals(intent.getStringExtra(Conversion.BUY)) ? View.VISIBLE : View.GONE);
        mIvIntegrity.setVisibility(!"0".equals(intent.getStringExtra(Conversion.INTEGRITY)) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initData() {
        mTvNickName.setText(name);
        mTvAge.setText(age + "岁");
        mTvIndustry.setText(label);
        /**男女*/
        switch (sex) {
            case "1":
                mTvSex.setImageResource(R.mipmap.nan);
                mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nan);
                break;
            case "2":
                mTvSex.setImageResource(R.mipmap.nv);
                mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nv);
                break;
            default:
                break;
        }
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + image, mIvImageHead, sex);
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mTaTaskAdapter = new TaTaskAdapter(mActivity, mList);
        mSwipeTarget.setAdapter(mTaTaskAdapter);
        /**上啦加载*/
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEnqueue(true);
            }
        });
/**下拉刷新*/
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getEnqueue(false);
            }
        });
    }

    @Override
    protected void initListener() {
        mTaTaskAdapter.setItemClickListener(new TaTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int id) {
                Intent intent = new Intent(mActivity, TaskDetailActivity.class);
                intent.putExtra(Conversion.ID, String.valueOf(id));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initInternet() {
        //只要发布中的任务
        getEnqueue(true);
    }

    private void getEnqueue(final boolean isFirst) {
        if (isFirst) {
            page = 1;
        } else {
            page++;
        }
        RetrofitUtil.createService(TaskService.class).getTaskInfoByUserId(mUserId, "1", "1", page, "3").enqueue(new RetrofitCallback<TaTaskBean>() {
            @Override
            public void onSuccess(Call<TaTaskBean> call, Response<TaTaskBean> response) {
                mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                if (response.isSuccessful()) {
                    LogUtil.d("他人列表数据访问成功--");
                    TaTaskBean bean = response.body();
                    if (bean.getResultCode() == 1) {
                        if (isFirst) {
                            mList.clear();
                            mList.addAll(bean.getTaskInfoByUserIdList());
                        } else {
                            mList.addAll(bean.getTaskInfoByUserIdList());
                        }
                        mTaTaskAdapter.notifyDataSetChanged();
                    } else if (bean.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    } else if (bean.getResultCode() == 0) {
                        Toasty.normal(mActivity, bean.getResultDesc()).show();
                    } else {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                    if (mList != null && mList.size() == 0) {
                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "这个人很懒，当前没有发布过任务!");
                    } else {
                        mPaProgress.showContent();
                    }
                }
            }

            @Override
            public void onFailure(Call<TaTaskBean> call, Throwable t) {
                mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
            }
        });
    }
}
