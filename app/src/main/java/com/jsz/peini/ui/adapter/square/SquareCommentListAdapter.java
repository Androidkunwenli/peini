package com.jsz.peini.ui.adapter.square;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.CommentListBean;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.ui.view.square.SquareTextViewontent;
import com.jsz.peini.utils.LogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.dmoral.toasty.Toasty;

import static com.jsz.peini.utils.UiUtils.getResources;

/**
 * Created by th on 2016/12/24.
 */
public class SquareCommentListAdapter extends BaseRecyclerViewAdapter {

    private List<CommentListBean> mList;
    private Context mActivity;

    public SquareCommentListAdapter(Context activity, List<CommentListBean> list) {
        super(list);
        mList = list;
        mActivity = activity;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        final CommentListBean commentListBean = mList.get(position);
        //前面的名字
        String userNickname = commentListBean.getUserNickname();
        //前面名字的长度
        int userNicknameLength = userNickname.length();
        //后面的名字
        String toUserNickname = commentListBean.getToUserNickname();
        //后面的名字长度
        int toUserNicknameLength = toUserNickname.length();

        final String content = commentListBean.getContent();
        SpannableString mSpannableString;
        if (!commentListBean.getToUserId().equals("0")) {
            //文本
            String item = userNickname + "回复" + toUserNickname + ": " + content;
            mSpannableString = new SpannableString(item);
            /*--------------------------颜色---------------------------------------------*/
            //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
            mSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text276396)), 0, userNicknameLength,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text276396)), userNicknameLength + 2, userNicknameLength + 2 + toUserNicknameLength,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            /*---------------------------点击事件-----------------------------------------*/
            mSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    LogUtil.i("评论的点击事件 前面的那个人-->", commentListBean.getUserId() + "点击人的");
                    String userId = commentListBean.getUserId();
                    mListener.OnUserId(commentListBean.getId() + "", userId + "", position);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.text276396));
                    ds.setUnderlineText(false);
                }
            }, 0, userNicknameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    String toUserId = commentListBean.getToUserId();
                    LogUtil.i("评论的点击事件 后面的那个人-->", toUserId + "点击人的");
                    mListener.OnToUserId(commentListBean.getId() + "", toUserId + "", position);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.text276396));
                    ds.setUnderlineText(false);
                }
            }, userNicknameLength + 2, userNicknameLength + 2 + toUserNicknameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mSquareComment.setText(mSpannableString);

        } else {

            String item = userNickname + ": " + content;
            mSpannableString = new SpannableString(item);
            //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
            mSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text276396)), 0, userNicknameLength,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {

                    String userId = commentListBean.getUserId();
                    LogUtil.i("评论的点击事件 前面的那个人-->", userId + "点击人的");
                    mListener.OnUserId(commentListBean.getId() + "", userId + "", position);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.text276396));
                    ds.setUnderlineText(false);
                }
            }, 0, userNicknameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mSquareComment.setText(mSpannableString);
        }
        holder.mSquareComment.setMovementMethod(LinkMovementMethod.getInstance());
        holder.mSquareComment.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        holder.mSquareComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.OnContent(commentListBean.getId() + "", commentListBean.getUserId() + "", commentListBean.getUserNickname(), commentListBean.getToUserId() + "", commentListBean.getToUserNickname(), content, position, true);
                return true;
            }
        });
        holder.mSquareComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.i("点击了评论按钮", "点击了第几个" + position);
                mListener.OnContent(commentListBean.getId() + "", commentListBean.getUserId() + "", commentListBean.getUserNickname(), commentListBean.getToUserId() + "", commentListBean.getToUserNickname(), content, position, false);
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        TextView view = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.square_commentlist, viewGroup, false);
//        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        RelativeLayout.LayoutParams lp = news RelativeLayout.LayoutParams(view.getMeasuredHeight(), view.getMeasuredHeight());
//        LogUtil.i("获取的高度",""+view.getMeasuredHeight());
//        LogUtil.i("获取的高度",""+  View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
//        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.square_comment)
        SquareTextViewontent mSquareComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    private OnCommentClickListener mListener;

    void setOnCommentClickListener(OnCommentClickListener listener) {
        mListener = listener;
    }

    interface OnCommentClickListener {
        /**
         * 这个是前面的id--和索引
         */
        void OnUserId(String id, String userId, int position);

        /**
         * 后面的 id --- 索引
         */
        void OnToUserId(String id, String toUserId, int position);

        /**
         * 这个是内容
         */
        void OnContent(String id, String UserId, String UserNickname, String toUserId, String ToUserNickname, String Content, int position, boolean longItemContent);

        /**
         * 这个是整个条目的点击事件
         */
        void OnItemClick(String id, String Content, int position, boolean longItemContent);
    }
}
