package com.jsz.peini.ui.adapter.seller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.search.SearchHot;
import com.jsz.peini.ui.activity.search.IsSearchActivity;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lenovo on 2017/3/4.
 */
public class SellerSearchEndAdapter extends RecyclerView.Adapter {

    private static final int TASK_SEARCH_ONE = 0;
    private static final int TASK_SEARCH_TOW = 1;
    private final IsSearchActivity mActivity;
    private final List<SearchHot.SellerListBean> mSearchBean;

    public SellerSearchEndAdapter(IsSearchActivity activity, List<SearchHot.SellerListBean> searchBean) {
        mActivity = activity;
        mSearchBean = searchBean;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TASK_SEARCH_ONE;
        } else {
            return TASK_SEARCH_TOW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TASK_SEARCH_ONE) {
            return new ViewHolderHead(LayoutInflater.from(mActivity).inflate(R.layout.item_task_search_end_one, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_task_search_end_tow, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            showViewImformation(holder, position - 1);
        }
    }

    private void showViewImformation(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        SearchHot.SellerListBean bean = mSearchBean.get(position);
        int distance = bean.getDistance();
        final String id = bean.getId();
        int listNum = bean.getListNum();
        final String sellerAddress = bean.getSellerAddress();
        final String sellerName = bean.getSellerName();
        final int searchType = bean.getSearchType();
        /***/
        switch (searchType) {
            case 1:
                viewHolder.mLlTypeOne.setVisibility(View.VISIBLE);
                viewHolder.mLlTypeTow.setVisibility(View.GONE);
                viewHolder.mSearchAddress.setText(sellerName);

                String distanceStr;
                if (distance < 1000) {
                    distanceStr = distance + "m";
                } else {
                    float size = distance / 1000f;
                    DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
                    distanceStr = df.format(size) + "km";//返回的是String类型的
                }
                viewHolder.mSearchAddressDistance.setText(distanceStr);

                viewHolder.mSearchAddressInformation.setText(sellerAddress);
                viewHolder.mIvImage.setImageResource(R.mipmap.map);
                break;
            case 2:
                viewHolder.mLlTypeTow.setVisibility(View.VISIBLE);
                viewHolder.mLlTypeOne.setVisibility(View.GONE);
                viewHolder.mTvResult.setText("约" + listNum + "个结果");
                viewHolder.mTvNickName.setText(sellerName);
                viewHolder.mIvImage.setImageResource(R.mipmap.search);
                break;
            default:
                break;
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position, searchType, sellerName, id,sellerAddress);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchBean.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_nickName)
        TextView mTvNickName;
        @InjectView(R.id.tv_result)
        TextView mTvResult;
        @InjectView(R.id.ll_type_tow)
        LinearLayout mLlTypeTow;
        @InjectView(R.id.search_address)
        TextView mSearchAddress;
        @InjectView(R.id.search_address_distance)
        TextView mSearchAddressDistance;
        @InjectView(R.id.search_address_information)
        TextView mSearchAddressInformation;
        @InjectView(R.id.ll_type_one)
        LinearLayout mLlTypeOne;
        @InjectView(R.id.iv_image)
        ImageView mIvImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    class ViewHolderHead extends RecyclerView.ViewHolder {
        public ViewHolderHead(View view) {
            super(view);
        }
    }

    private OnItemClickListener mListener;

    public void setItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int i, int position, String sellerName, String id, String sellerAddress);
    }
}
