package com.jsz.peini.ui.activity.square;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.square.MiLabelBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.adapter.square.ChannelAdapter;
import com.jsz.peini.ui.adapter.square.ChannelDeleteAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.LabelGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by th on 2017/2/9.
 */
public class MiLabelActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.channel_text)
    TextView mChannelText;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.top_gridview)
    LabelGridView mTopGridview;
    @InjectView(R.id.bottom_gridview)
    LabelGridView mBottomGridview;
    public ArrayList<String> mBottomDatas = new ArrayList<>();
    public ArrayList<String> mTopDatas = new ArrayList<>();

    private ChannelDeleteAdapter mTopAdapter;
    private ChannelAdapter mBottomAdapter;
    public MiLabelActivity mActivity;
    private List<MiLabelBean.LabelInfoBean> mLabelInfoshow;
    private List<MiLabelBean.LabelInfoBean> mLabelInfo;

    @Override
    public int initLayoutId() {
        return R.layout.activity_milabel;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Conversion.DATASUCCESS:
                    MiLabelBean labelBean = (MiLabelBean) msg.obj;
                    LogUtil.d("我的标签返回的数据" + labelBean.toString());

                    mLabelInfoshow = labelBean.getLabelInfoshow();
                    mLabelInfo = labelBean.getLabelInfo();
                    mTopDatas.clear();
                    for (int i = 0; i < mLabelInfoshow.size(); i++) {
                        mTopDatas.add(mLabelInfoshow.get(i).getLabelName());
                    }
                    mBottomDatas.clear();
                    for (int i = 0; i < mLabelInfo.size(); i++) {
                        mBottomDatas.add(mLabelInfo.get(i).getLabelName());
                    }
                    LogUtil.d("labelInfoshow.size()" + mLabelInfoshow.size());
                    LogUtil.d("labelInfo.size()" + mLabelInfo.size());
                    mChannelText.setText("已选标签" + mLabelInfoshow.size() + "/3");
                    mTopAdapter.notifyDataSetChanged();
                    mBottomAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    private boolean idEdit = false;

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("我的标签");
        mRightButton.setText("编辑");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initNetWork();
    }

    @Override
    public void initData() {
        mTopAdapter = new ChannelDeleteAdapter(mTopDatas, this, idEdit);
        mBottomAdapter = new ChannelAdapter(mBottomDatas, this, idEdit);
        mTopGridview.setAdapter(mTopAdapter);
        mBottomGridview.setAdapter(mBottomAdapter);
        mTopGridview.setOnItemClickListener(this);
        mBottomGridview.setOnItemClickListener(this);

    }

    private void initNetWork() {
        RetrofitUtil.createService(SquareService.class)
                .getLabelInfoList(mUserToken)
                .enqueue(new RetrofitCallback<MiLabelBean>() {
                    @Override
                    public void onSuccess(Call<MiLabelBean> call, Response<MiLabelBean> response) {
                        if (response.isSuccessful()) {
                            MiLabelBean body = response.body();
                            Message msg = new Message();
                            msg.what = Conversion.DATASUCCESS;
                            msg.obj = body;
                            mHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<MiLabelBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /**
     * 标记：item是否正在移动
     */
    private boolean isMoving;
    private ViewGroup windowViewGroup;
    private View moveView;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!idEdit) {
            return;
        }
        if (isMoving) {

        } else if (parent.getId() == R.id.top_gridview) {
            onTopItemClick(view, position);
            mChannelText.setText("已选标签" + (mTopAdapter.getItem().size() - 1) + "/3");
            for (int i = 1; i < mTopAdapter.getItem().size(); i++) {
                LogUtil.d("获取到的标签--" + mTopAdapter.getItem().get(i));
            }
        } else if (parent.getId() == R.id.bottom_gridview) {
            onBottomItemClick(view, position);
            mChannelText.setText("已选标签" + (mTopAdapter.getItem().size()) + "/3");
            for (int i = 0; i < mTopDatas.size(); i++) {
                LogUtil.d("获取到的标签--" + mTopDatas.get(i));
            }
        }
    }

    /**
     * 底部gridView Item被点击
     */
    private void onBottomItemClick(View view, int position) {
        if (!idEdit) {
            return;
        }
        if (mTopAdapter.getItem().size() > 2) {
            Toasty.normal(mActivity, "您最多只能选择3个标签").show();
            return;
        }
        mBottomAdapter.setmRemovePosition(position);
        moveView = UiUtils.getDrawingCacheView(view);
        if (moveView != null) {

            final int[] startLocation = new int[2];
            view.getLocationInWindow(startLocation);

            mTopAdapter.setLastItemVisibility(false);
            String itemData = mBottomAdapter.getItem(position);
            mTopAdapter.addItem(itemData);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int[] endLocation = new int[2];
                    mTopGridview.getChildAt(mTopGridview.getLastVisiblePosition()).getLocationInWindow(endLocation);

                    moveItemAnim(startLocation, endLocation, mBottomGridview);
                }
            }, 50L);
        }
    }

    /**
     * 顶部gridView Item被点击
     */
    private void onTopItemClick(View view, int position) {
        if (!idEdit) {
            return;
        }
        //将点击的Item内容隐藏
        mTopAdapter.setmRemovePosition(position);

        //获取点击item的缩略图，用于动画平移
        moveView = UiUtils.getDrawingCacheView(view);
        if (moveView != null) {
            //获取起点坐标（ItemView的当前屏幕内坐标既是）
            final int[] startLocation = new int[2];
            view.getLocationInWindow(startLocation);

            //让底部的GridView暂时在末尾添加一个空白item站位
            mBottomAdapter.setLastItemVisibility(false);
            String itemData = mTopAdapter.getItem(position);
            mBottomAdapter.addItem(itemData);

            //此处需要延时一段时间，等待Adapter末尾创建完站位的item
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //获取终点坐标（底部GridView用于站位的最后一个item坐标既是）
                    int[] endLocation = new int[2];
                    mBottomGridview.getChildAt(mBottomGridview.getLastVisiblePosition()).getLocationInWindow(endLocation);

                    //开启平移动画
                    moveItemAnim(startLocation, endLocation, mTopGridview);
                }
            }, 50L);
        }
    }

    /**
     * Item平移动画
     */
    private void moveItemAnim(int[] startLocation, int[] endLocation, final GridView clickGridView) {

        //缩略图只是我们在内存中创建的，其还没有加载到我们的界面中，所以不可直接移动，先要进行处理
        initMoveView();

        TranslateAnimation transAnima = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        transAnima.setDuration(300);
        transAnima.setFillAfter(true);
        transAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving = true;
                moveView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isMoving = false;
                moveView.setVisibility(View.INVISIBLE);
                windowViewGroup.removeView(moveView);
                moveView = null;

                if (clickGridView == mTopGridview) {
                    //点击的是顶部GridView ， 那么底部的最后一个Item可以显示出来了
                    mBottomAdapter.setLastItemVisibility(true);
                    mBottomAdapter.notifyDataSetChanged();

                    //最后移除顶部被点击的Item
                    mTopAdapter.remove();

                } else if (clickGridView == mBottomGridview) {
                    mTopAdapter.setLastItemVisibility(true);
                    mTopAdapter.notifyDataSetChanged();
                    mBottomAdapter.remove();
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        moveView.startAnimation(transAnima);
    }


    /**
     * 对MoveView进行相关处理，并添加到我们的界面中
     */
    private void initMoveView() {
        //获取窗口容器
        windowViewGroup = (ViewGroup) getWindow().getDecorView();
        //再将moveView添加到父容器中
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        moveView.setLayoutParams(params);
        moveView.setVisibility(View.GONE);
        windowViewGroup.addView(moveView);
    }

    @OnClick(R.id.right_button)
    public void onClick() {
        if (!idEdit) {
            mRightButton.setText("完成");
            idEdit = true;
            mTopAdapter.setEdit(true);
        } else {
            initNetWorkSubmit();
        }
    }

    /**
     * 提交
     */
    private void initNetWorkSubmit() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mTopDatas.size(); i++) {
            String s = mTopDatas.get(i);
            for (int a = 0; a < mLabelInfoshow.size(); a++) {
                if (s.equals(mLabelInfoshow.get(a).getLabelName())) {
                    builder.append(mLabelInfoshow.get(a).getId() + ",");
                }
            }
            for (int b = 0; b < mLabelInfo.size(); b++) {
                if (s.equals(mLabelInfo.get(b).getLabelName())) {
                    builder.append(mLabelInfo.get(b).getId() + ",");
                }
            }
        }

        LogUtil.d("获取到的标签--" + builder.toString());
        initLabelInfoBylabelStateNetWork(builder.toString());
    }

    private void initLabelInfoBylabelStateNetWork(String s) {
        RetrofitUtil.createService(SquareService.class)
                .setLabelInfoBylabelState(mUserToken, s)
                .enqueue(new RetrofitCallback<SuccessfulBean>() {
                    @Override
                    public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, "修改标签成功").show();
                                mRightButton.setText("编辑");
                                idEdit = false;
                                mTopAdapter.setEdit(false);
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
