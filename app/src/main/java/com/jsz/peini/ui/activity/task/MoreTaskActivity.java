package com.jsz.peini.ui.activity.task;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.tabulation.TaskListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.adapter.task.TaskItemAdapter;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/20.
 */
public class MoreTaskActivity extends BaseActivity {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.tabuiation_viewpager)
    ViewPager mTabuiationViewpager;
    public TaskItemAdapter mAdapter;
    public Activity mActivity;
    public List<View> mViews;
    public Intent mIntent;
    String idStr;
    private List<TaskListBean.TaskAllListBean> mTaskAllList = new ArrayList<>();

    @Override
    public int initLayoutId() {
        return R.layout.activity_moretask;
    }

    @Override
    public void initView() {
        mTitle.setText("多任务列表");
        mActivity = this;
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViews = new ArrayList<>();
        idStr = getIntent().getStringExtra("idStr");
        LogUtil.d("idstr" + idStr);
    }

    private String mSort = "1";
    private String mIsIdcard = "";
    private String mIsVideo = "";
    private String mOtherHignAge = "";
    private String mOtherHignheight = "";
    private String mOtherLowAge = "";
    private String mOtherLowheight = "";
    private String mOtherSex = "";
    private String mSellerType = "";

    @Override
    public void initData() {
        mAdapter = new TaskItemAdapter(mActivity);
        mTabuiationViewpager.setAdapter(mAdapter);//写法不变
        mTabuiationViewpager.setOffscreenPageLimit(2);//>=3
        mTabuiationViewpager.setPageMargin(50);//设置page间间距，自行根据需求设置
        //setPageTransformer 决定动画效果
        mTabuiationViewpager.setPageTransformer(true, new ScaleInTransformer());
        mAdapter.setOnItemClickListener(new TaskItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int id) {
                mIntent = new Intent(mActivity, TaskDetailActivity.class);
                int value = mTaskAllList.get(position).getId();
                mIntent.putExtra("id", value + "");
                startActivity(mIntent);
                LogUtil.d("列表点击的界面" + "第几个 -->  " + position + "\n id---" + value);
            }
        });
        initNetWork();
    }

    private void initNetWork() {
        RetrofitUtil.createService(TaskService.class)
                .selectTaskInfoBySort(
                        mUserToken,
                        SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity),
                        mSort,
                        IpConfig.cityCode,
                        idStr,
                        mOtherSex,
                        mOtherLowAge,
                        mOtherHignAge,
                        mOtherLowheight,
                        mOtherHignheight,
                        mIsVideo,
                        mIsIdcard,
                        mSellerType)
                .enqueue(new Callback<TaskListBean>() {
                    @Override
                    public void onResponse(Call<TaskListBean> call, Response<TaskListBean> response) {
                        if (response.isSuccessful()) {
                            TaskListBean body = response.body();
                            if (body.getResultCode() == 1) {
                                mTaskAllList.clear();
                                mTaskAllList.addAll(body.getTaskAllList());
                                mAdapter.setTaskAllList(mTaskAllList);
                                mTabuiationViewpager.setCurrentItem(mAdapter.getStartPageIndex(), true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskListBean> call, Throwable t) {
                    }
                });
    }

}
