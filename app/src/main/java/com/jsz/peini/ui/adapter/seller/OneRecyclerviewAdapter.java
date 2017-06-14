package com.jsz.peini.ui.adapter.seller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.address.SellerAddress;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2017/2/10.
 */
public class OneRecyclerviewAdapter extends RecyclerView.Adapter {

    private static final int ONE = 0;
    private static final int TOW = 1;
    private int HEAD = 1;
    private Activity mActivity;
    private List<SellerAddress.DistrictListBean> mDistrictHotList;
    private int area = -1;

    public OneRecyclerviewAdapter(Activity activity, List<SellerAddress.DistrictListBean> districtListBeen) {
        mActivity = activity;
        mDistrictHotList = districtListBeen;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ONE;
        } else {
            return TOW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ONE) {
            return new ViewHolderOne(LayoutInflater.from(mActivity).inflate(R.layout.seller_listview_item, parent, false));
        } else {
            return new ViewHolderTow(LayoutInflater.from(mActivity).inflate(R.layout.seller_listview_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderOne) {
            OneDataShowView(holder, position);
        } else if (holder instanceof ViewHolderTow) {
            TowDataShowView(holder, position);
        }
    }

    private void OneDataShowView(RecyclerView.ViewHolder holder, final int position) {
        ViewHolderOne holderOne = (ViewHolderOne) holder;
        holderOne.mOneName.setBackgroundResource(area == position ? R.color.white000 : R.color.backgroundf1f1f1);
        holderOne.mOneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.ItemClicknearbyListener(position);
            }
        });
    }

    private void TowDataShowView(RecyclerView.ViewHolder holder, final int position) {
        ViewHolderTow holderTow = (ViewHolderTow) holder;
        SellerAddress.DistrictListBean districtListBean = mDistrictHotList.get(position - 1);
        holderTow.mOneName.setText(districtListBean.getCountyName());
        holderTow.mOneName.setBackgroundResource(area == position ? R.color.white000 : R.color.backgroundf1f1f1);
        final int countyId = districtListBean.getCountyId();
        holderTow.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.ItemClickListener(position - 1, countyId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDistrictHotList.size() + HEAD;
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        @InjectView(R.id.one_name)
        TextView mOneName;

        public ViewHolderOne(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public class ViewHolderTow extends RecyclerView.ViewHolder {
        @InjectView(R.id.one_name)
        TextView mOneName;

        public ViewHolderTow(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        /*热门商圈*/
        void ItemClickListener(int position, int countyId);

        /*附近*/
        void ItemClicknearbyListener(int position);

    }

    public void setSelect(int select) {
        area = select;
        notifyDataSetChanged();
    }
}
