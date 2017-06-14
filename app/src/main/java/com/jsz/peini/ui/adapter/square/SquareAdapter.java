package com.jsz.peini.ui.adapter.square;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.square.CommentListBean;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.LikeListBean;
import com.jsz.peini.model.square.SquareBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.SquareNewActivity;
import com.jsz.peini.ui.activity.web.WebAllActivity;
import com.jsz.peini.ui.adapter.TestNormalAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ExpandableTextView;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by th on 2016/12/23.
 */
public class SquareAdapter extends RecyclerView.Adapter {

    private int NEWADDITEM = 3;
    private int FOOTVIEWITEM = 2;
    private int ADDOFLATE = 0;
    private int ADDITEM = 1;

    private static final String TAG = "SquareAdapter 广场适配器";

    private Activity mActivity;

    private List<SquareBean.SquareListBean> mList;//空间返回的大数据
    private List<AdModel.AdvertiseListBean> mAdModels;//广告的信息
    private SquareBean.SquareListBean mSquareListBean; // 基本的业务模型

    private int mSpId;
    private SquareCommentListAdapter mSquareCommentListAdapter;
    private SquareLikeAdapter mSquareLikeAdapter;
    private TestNormalAdapter mSelleradapter;
    private SparseBooleanArray mCollapsedStatus;
    private String mType;
    private String mUserToken;
    //刷新
    private int mIslooding = 0;
    //显示未读消息数的
    private String mNewData = "0";

    public SquareAdapter(Activity activity, List<SquareBean.SquareListBean> list, List<AdModel.AdvertiseListBean> adModels, String type) {
        mActivity = activity;
        mList = list;
        mAdModels = adModels;
        mCollapsedStatus = new SparseBooleanArray();
        mType = type;
        mUserToken = SpUtils.getUserToken(mActivity);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ADDOFLATE;
        } else if (getItemCount() - 1 == position) {
            return FOOTVIEWITEM;
        } else if (position == 1) {
            return NEWADDITEM;
        } else {
            return ADDITEM;
        }
    }

    @Override
    public int getItemCount() {
        return Conversion.getSize(mList) + 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ADDOFLATE) {
            return new HeadViewHolder(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.square_head, parent));
        } else if (viewType == FOOTVIEWITEM) {
            return new FootViewHolder(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.quick_view_load_more, parent));
        } else if (viewType == NEWADDITEM) {
            return new NewViewHolder(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_square_new, parent));
        } else {
            return new ViewHolder(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_square, parent));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            setHeadViewHolderBean((HeadViewHolder) holder, position);
        } else if (holder instanceof ViewHolder) {
            setViewHolderBean((ViewHolder) holder, position - 2);
        } else if (holder instanceof FootViewHolder) {
            setFootViewBean((FootViewHolder) holder);
        } else if (holder instanceof NewViewHolder) {
            setNewViewHolder((NewViewHolder) holder);
        }
    }

    private void setNewViewHolder(final NewViewHolder holder) {
        if (!TextUtils.isEmpty(mNewData)) {
            isShowNew(holder, true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(new Intent(mActivity, SquareNewActivity.class));
                    isShowNew(holder, false);
                }
            });
        } else {
            isShowNew(holder, false);
        }
    }

    private void isShowNew(NewViewHolder holder, boolean isShow) {
        if (isShow) {
            holder.mLlSquareNew.setVisibility(View.VISIBLE);
            holder.mLlSquareNewItem.setVisibility(View.VISIBLE);
            holder.mIvSquareNewImage.setVisibility(View.VISIBLE);
            holder.mTvSquareNew.setVisibility(View.VISIBLE);
        } else {
            holder.mLlSquareNew.setVisibility(View.GONE);
            holder.mTvSquareNew.setVisibility(View.GONE);
            holder.mLlSquareNewItem.setVisibility(View.GONE);
            holder.mIvSquareNewImage.setVisibility(View.GONE);
        }
    }

    //尾部局
    private void setFootViewBean(FootViewHolder holder) {
        if (mList.size() == 0) {
            holder.mLoadMoreLoadingView.setVisibility(View.GONE);
            holder.mLoadMoreLoadEndView.setVisibility(View.GONE);
            holder.mLoadMoreLoadFailView.setVisibility(View.GONE);
        }
        switch (mIslooding) {
            case 0://正在加载
                holder.mLoadMoreLoadingView.setVisibility(View.VISIBLE);
                holder.mLoadMoreLoadEndView.setVisibility(View.GONE);
                holder.mLoadMoreLoadFailView.setVisibility(View.GONE);
                break;
            case 1://没有跟多数据了
                holder.mLoadMoreLoadingView.setVisibility(View.GONE);
                holder.mLoadMoreLoadEndView.setVisibility(View.VISIBLE);
                holder.mLoadMoreLoadFailView.setVisibility(View.GONE);
                break;
            case 2: //加载失败
                holder.mLoadMoreLoadingView.setVisibility(View.GONE);
                holder.mLoadMoreLoadEndView.setVisibility(View.GONE);
                holder.mLoadMoreLoadFailView.setVisibility(View.VISIBLE);
                break;
            case 3://空布局
                holder.mLoadMoreLoadingView.setVisibility(View.GONE);
                holder.mLoadMoreLoadEndView.setVisibility(View.GONE);
                holder.mLoadMoreLoadFailView.setVisibility(View.GONE);
                break;
        }
        if (mIslooding == 0) {
            mListener.loadMoreData(holder);
        }
        holder.mLoadMoreLoadFailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setLoodingNetWork();
            }
        });
    }

    private void setViewHolderBean(ViewHolder holder, final int index) {
        holder.mSquareButtonZan.setImageResource(R.drawable.nozan);
        if (mList != null && mList.size() > 0) {
            mSquareListBean = mList.get(index);
            //获取宽度
            //地址
            String address = mSquareListBean.getAddress();
            if (StringUtils.isNoNull(address)) {
                holder.mSquareAddress.setVisibility(View.VISIBLE);
                holder.mSquareAddress.setText(address);
            } else {
                holder.mSquareAddress.setVisibility(View.GONE);
            }
            /**是否点过赞呀*/
            holder.mSquareButtonZan.setImageResource(mSquareListBean.isLike() ? R.drawable.zan : R.drawable.nozan);

            /**是否有评论和点赞*/
            if ((mSquareListBean.getLikeList() == null || mSquareListBean.getLikeList().size() == 0) && (mSquareListBean.getLikeList() == null || mSquareListBean.getCommentList().size() == 0)) {
                holder.mSquareBj.setVisibility(View.GONE);
            } else {
                holder.mSquareBj.setVisibility(View.VISIBLE);
            }

            //事件
            String squareTime = mSquareListBean.getSquareTime();
            holder.mSquareTime.setText(StringUtils.isNoNull(squareTime) ? squareTime : "");
            //名字
            holder.mSquareNickname.setText(mSquareListBean.getNickname());

            if (!TextUtils.isEmpty(mSquareListBean.getUserId()) && mSquareListBean.getUserId().equals(mUserToken)) {
                holder.mSquareDeleteItem.setVisibility(View.VISIBLE);
                //删除按钮
                holder.mSquareDeleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtil.e(TAG, "点击了删除的按钮");
                        mListener.OnDelete(index);
                    }
                });
            } else {
                holder.mSquareDeleteItem.setVisibility(View.GONE);
            }

            //名字
            holder.mSquareNickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onID(index, mSquareListBean.getId() + "", mSquareListBean.getUserId() + "");
                }
            });

            //头像  性别
            String PIC = IpConfig.HttpPic + mSquareListBean.getImageHead();
            GlideImgManager.loadImage(mActivity, PIC, holder.mSquareImageead, "2");

            //头像的点击事件
            holder.mSquareImageead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onID(index, mSquareListBean.getId() + "", mSquareListBean.getUserId() + "");
                    LogUtil.d(mActivity.getPackageName(), "==========" + mSquareListBean.getUserId());
                }
            });

            /*广场的信息*/
            String content = mSquareListBean.getContent();
            holder.mSquareContent.setText(content, mCollapsedStatus, index);
            /*图片的列表*/
            if (mSquareListBean.getImageList() != null && mSquareListBean.getImageList().size() > 0) {
                int measuredWidth = holder.mSquareIpc.getMeasuredWidth();
                SquareImageAdapter imageAdapter = new SquareImageAdapter(mActivity, (ArrayList<ImageListBean>) mSquareListBean.getImageList(), measuredWidth);
                holder.mSquareIpc.setLayoutManager(new GridLayoutManager(mActivity, 3));
                holder.mSquareIpc.setAdapter(imageAdapter);
                holder.mSquareIpc.setVisibility(View.VISIBLE);
            } else {
                holder.mSquareIpc.setVisibility(View.GONE);
            }

            /*点赞的列表*/
            if (mSquareListBean.getLikeList() != null) {
                holder.mPeopleLike.setVisibility(View.VISIBLE);
                final List<LikeListBean> mLikeList = mSquareListBean.getLikeList();
                int size = mLikeList.size();
                if (size > 0) {
                    holder.mPeopleLike.setVisibility(View.VISIBLE);
                    holder.mPeopleLike.setText("等" + size + "人赞过");
                } else {
                    holder.mPeopleLike.setVisibility(View.GONE);
                }
                mSquareLikeAdapter = new SquareLikeAdapter(mActivity, mLikeList);
                //设置布局管理器
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                holder.mSquareLike.setLayoutManager(linearLayoutManager);
                holder.mSquareLike.setAdapter(mSquareLikeAdapter);
                holder.mSquareLike.setVisibility(View.VISIBLE);
                holder.mPeopleLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.setOnItemLikeListClickListener(mLikeList, index);
                    }
                });
            } else {
                holder.mPeopleLike.setVisibility(View.GONE);
                holder.mSquareLike.setVisibility(View.GONE);
            }
            if (mSquareListBean.getCommentList() != null && mSquareListBean.getCommentList().size() > 0) {
                mSpId = mSquareListBean.getId();
                mSquareCommentListAdapter = new SquareCommentListAdapter(mActivity, mSquareListBean.getCommentList());
                LinearLayoutManager layout = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
                holder.mSquareCommentlist.setLayoutManager(layout);
                holder.mSquareCommentlist.setAdapter(mSquareCommentListAdapter);
                holder.mSquareCommentlist.setVisibility(View.VISIBLE);
                mSquareCommentListAdapter.setOnCommentClickListener(new SquareCommentListAdapter.OnCommentClickListener() {
                    @Override
                    public void OnUserId(String id, String userId, int position) {
                        mListener.OnUserId(id, userId, position, mSpId);
                    }

                    @Override
                    public void OnToUserId(String id, String toUserId, int position) {
                        mListener.OnToUserId(id, toUserId, position, mSpId);
                    }

                    @Override
                    public void OnContent(String id, String UserId, String UserNickname, String toUserId, String ToUserNickname, String Content, int position, boolean longItemContent) {
                        mListener.OnContent(id, UserId, UserNickname, toUserId, ToUserNickname, Content, position, mSpId, index, longItemContent);
                        LogUtil.i("id", "" + mSpId);
                    }

                    @Override
                    public void OnItemClick(String id, String Content, int position, boolean longItemContent) {
                        mListener.OnItemClick(id, Content, position, mSpId, longItemContent);
                    }
                });
            } else {
                holder.mSquareCommentlist.setVisibility(View.GONE);
            }
            //按钮是否可以点击
            holder.mSquareButtonNews.setEnabled(mSquareListBean.getLocal());
            holder.mSquareButtonZan.setEnabled(mSquareListBean.getLocal());
            holder.mSquareDeleteItem.setEnabled(mSquareListBean.getLocal());
            //点击的评论的按钮
            holder.mSquareButtonNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //评论的按钮
                    LogUtil.e(TAG, "点击了评论的按钮");
                    mListener.OnContentId(index);
                }
            });

            //这个点赞的按钮
            holder.mSquareButtonZan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //点赞
                    LogUtil.e(TAG, "点击了点赞的按钮");
                    mListener.OnLike(index);
                }
            });

        }
    }

    private void setHeadViewHolderBean(HeadViewHolder holder, int position) {
        if (!"1".equals(mType) && !"2".equals(mType)) {
            holder.mSliderVp.setVisibility(View.VISIBLE);
            holder.mIvViewPagerBj.setVisibility(View.VISIBLE);
            if (null != mAdModels) {
                if (mAdModels.size() >= 2) {
                    holder.mSliderVp.setVisibility(View.VISIBLE);
                    holder.mIvViewPagerBj.setVisibility(View.GONE);
                    mSelleradapter = new TestNormalAdapter(holder.mSliderVp, mActivity, mAdModels);
                    holder.mSliderVp.setAdapter(mSelleradapter);
                    holder.mSliderVp.setHintView(new ColorPointHintView(mActivity, Conversion.FB4E30, Color.WHITE));
                    mSelleradapter.setData(mAdModels);
                    holder.mSliderVp.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            AdModel.AdvertiseListBean adItemBean = mAdModels.get(position);
                            if (adItemBean != null && !TextUtils.isEmpty(adItemBean.getAdLink())) {
                                if (StringUtils.isHttpPath(adItemBean.getAdLink())) {
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
                        }
                    });
                } else if (mAdModels.size() == 0) {
                    holder.mSliderVp.setVisibility(View.GONE);
                    holder.mIvViewPagerBj.setVisibility(View.GONE);
                } else {
                    holder.mSliderVp.setVisibility(View.GONE);
                    holder.mIvViewPagerBj.setVisibility(View.VISIBLE);
                    if (mAdModels.size() == 1) {
                        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + mAdModels.get(0).getAdImgUrl(), holder.mIvViewPagerBj, "3");
                        holder.mIvViewPagerBj.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mAdModels != null && mAdModels.size() > 0) {
                                    AdModel.AdvertiseListBean adItemBean = mAdModels.get(0);
                                    if (adItemBean != null && !TextUtils.isEmpty(adItemBean.getAdLink())) {
                                        if (StringUtils.isHttpPath(adItemBean.getAdLink())) {
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
                                }
                            }
                        });
                    } else {
                        holder.mIvViewPagerBj.setBackgroundResource(R.drawable.ic_peini_banser);
                    }
                }
            }
        } else {
            holder.mIvViewPagerBj.setVisibility(View.GONE);
            holder.mSliderVp.setVisibility(View.GONE);
        }
    }

    //网络更新点赞
    public void setLikeList(List<LikeListBean> likeList, int index) {
        SquareBean.SquareListBean squareListBean = mList.get(index);
        squareListBean.setLike(!squareListBean.isLike());
        List<LikeListBean> likeList1 = squareListBean.getLikeList();
        if (null != likeList1) {
            likeList1.clear();
            likeList1.addAll(likeList);
            notifyItemChanged(index + 2);
        }
    }

    //网络更新点赞
    public void setLikeList(int index) {
        SquareBean.SquareListBean squareListBean = mList.get(index);
        List<LikeListBean> likeList1 = squareListBean.getLikeList();
        if (null != likeList1) {
            boolean like = squareListBean.isLike();
            squareListBean.setLike(!like);
            String nickname = SpUtils.getNickname(mActivity);
            String imageHead = SpUtils.getImageHead(mActivity);
            String userToken = SpUtils.getUserToken(mActivity);
            String sex = SpUtils.getSex(mActivity);
            if (like) {
                for (int i = 0; i < likeList1.size(); i++) {
                    if (imageHead.equals(likeList1.get(i).getImageHead())) {
                        likeList1.remove(i);
                    }
                }
            } else {
                likeList1.add(0, new LikeListBean(nickname, imageHead, userToken, sex));
            }
            notifyItemChanged(index + 2);
        }
    }

    //评论
    public void setCommentList(List<CommentListBean> commentList, int index) {
        SquareBean.SquareListBean squareListBean = mList.get(index);
        List<CommentListBean> commentListBeen = squareListBean.getCommentList();
        if (commentListBeen != null) {
            commentListBeen.clear();
            commentListBeen.addAll(commentList);
            notifyItemChanged(index + 2);
        }
    }

    //删除广场
    public void setDeleteContent(int index, int position) {
        SquareBean.SquareListBean squareListBean = mList.get(index);
        List<CommentListBean> commentListBeen = squareListBean.getCommentList();
        commentListBeen.remove(position);
        notifyItemChanged(index + 2);
    }

    //显示尾部局
    public void setLooding(int isLooding) {
        mIslooding = isLooding;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.square_imageead)
        CircleImageView mSquareImageead;
        @InjectView(R.id.square_nickname)
        TextView mSquareNickname;
        @InjectView(R.id.ll_weight)
        LinearLayout mLlWeight;
        @InjectView(R.id.square_content)
        ExpandableTextView mSquareContent;
        @InjectView(R.id.square_address)
        TextView mSquareAddress;
        @InjectView(R.id.square_squareTime)
        TextView mSquareTime;
        @InjectView(R.id.square_imageview)
        RecyclerView mSquareIpc;
        @InjectView(R.id.square_like)
        RecyclerView mSquareLike;
        @InjectView(R.id.square_commentlist)
        RecyclerView mSquareCommentlist;
        /**
         * 删除按钮
         */
        @InjectView(R.id.square_delete_item)
        TextView mSquareDeleteItem;
        /**
         * 评论按钮
         */
        @InjectView(R.id.square_button_news)
        ImageView mSquareButtonNews;
        /**
         * 点赞
         */
        @InjectView(R.id.square_button_zan)
        ImageView mSquareButtonZan;
        /**
         * 背景
         */
        @InjectView(R.id.sq_bj)
        LinearLayout mSquareBj;
        /**
         * 几个人点赞了
         */
        @InjectView(R.id.people_like)
        TextView mPeopleLike;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

            mSquareIpc.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            });
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.loading_progress)//进度条
                ProgressBar mLoadingProgress;
        @InjectView(R.id.loading_text)//正在加载
                TextView mLoadingText;
        @InjectView(R.id.load_more_loading_view)//正在加载
                LinearLayout mLoadMoreLoadingView;
        @InjectView(R.id.tv_prompt) //点我重试
                TextView mTvPrompt;
        @InjectView(R.id.load_more_load_fail_view)//加载失败
                FrameLayout mLoadMoreLoadFailView;
        @InjectView(R.id.load_more_load_end_view) //没有更多数据了
                FrameLayout mLoadMoreLoadEndView;

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.slider_vp)
        RollPagerView mSliderVp;
        @InjectView(R.id.ll_head_square)
        LinearLayout mLlHeadSquare;
        @InjectView(R.id.iv_viewpager_bj)
        ImageView mIvViewPagerBj;

        HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    class NewViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ll_square_new)
        LinearLayout mLlSquareNew;
        @InjectView(R.id.ll_square_new_item)
        LinearLayout mLlSquareNewItem;
        @InjectView(R.id.iv_square_new_image)
        CircleImageView mIvSquareNewImage;
        @InjectView(R.id.tv_square_new_index)
        TextView mTvSquareNew;

        NewViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private OnCommentClickListener mListener;

    public void setOnCommentClickListener(OnCommentClickListener listener) {
        mListener = listener;
    }

    public interface OnCommentClickListener {
        /**
         * 这个是前面的id--和索引
         */
        void OnUserId(String id, String userId, int position, int SpId);

        /**
         * 后面的 id --- 索引
         */

        void OnToUserId(String id, String toUserId, int position, int SpId);

        /**
         * 这个是内容
         */
        void OnContent(String id, String UserId, String UserNickname, String toUserId, String ToUserNickname, String Content, int position, int SpId, int index, boolean longItemContent);

        /**
         * 这个是整个条目的点击事件
         */
        void OnItemClick(String id, String Content, int position, int SpId, boolean longItemContent);

        /**
         * 这个是删除的按钮
         */
        void OnDelete(int index);

        /**
         * 这个是评论的接口
         */
        void OnContentId(int index);

        /**
         * 点赞的按钮
         */
        void OnLike(int index);

        void onID(int index, String id, String userId);

        /**
         * 点赞更多的点赞列表
         */
        void setOnItemLikeListClickListener(List<LikeListBean> likeList, int index);

        /**
         * 自动加载
         *
         * @param holder
         */
        void loadMoreData(RecyclerView.ViewHolder holder);

        /**
         * 点击加载
         */
        void setLoodingNetWork();
    }
}
