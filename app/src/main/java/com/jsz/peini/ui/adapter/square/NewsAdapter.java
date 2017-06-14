package com.jsz.peini.ui.adapter.square;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.model.news.NewsList;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.MiAttentionActivity;
import com.jsz.peini.utils.LogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 15089 on 2017/2/23.
 */
public class NewsAdapter extends RecyclerView.Adapter {

    private final MiAttentionActivity mActivity;
    private List<NewsList.ObjectListBean> mObjectList;

    public NewsAdapter(MiAttentionActivity activity, List<NewsList.ObjectListBean> newsList) {
        mActivity = activity;
        mObjectList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.news_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (mObjectList != null && mObjectList.size() > 0) {
            final NewsList.ObjectListBean listBean = mObjectList.get(position);
            String DoubleDesc = listBean.getDoubleDesc() + "";
            viewHolder.mDoubleDesc.setText(DoubleDesc.equals("1") ? "双方关注" : "");
            Glide.with(mActivity).load(IpConfig.HttpPic + listBean.getImageHead()).into(viewHolder.mImageHead);
            viewHolder.mNickname.setText(listBean.getNickname());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtil.d(mActivity.getPackageName(), "news关注列表getId" + listBean.getId() + "关注列表getUserId" + listBean.getUserId());
                    mListener.ItemObjectList(listBean.getId(), listBean.getUserId(), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mObjectList != null && mObjectList.size() > 0) {
            return mObjectList.size();
        } else {
            return 0;
        }
    }

    private OnItemEntryClickListener mListener;

    public void setOnItemEntryClickListener(OnItemEntryClickListener listener) {
        mListener = listener;
    }

    public interface OnItemEntryClickListener {
        void ItemObjectList(int id, String userId, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.imageHead)
        CircleImageView mImageHead;
        @InjectView(R.id.nickname)
        TextView mNickname;
        @InjectView(R.id.doubleDesc)
        TextView mDoubleDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
