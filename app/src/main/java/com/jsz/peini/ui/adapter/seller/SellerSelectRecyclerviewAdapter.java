package com.jsz.peini.ui.adapter.seller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.address.HotBean;
import com.jsz.peini.utils.UiUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2017/2/10.
 */
public class SellerSelectRecyclerviewAdapter extends RecyclerView.Adapter {

    public Activity mActivity;
    private List<HotBean> mDistrictList;
    private int mI;
    private int mSelect = -1;

    public SellerSelectRecyclerviewAdapter(Activity activity, List<HotBean> districtList, int i) {
        mActivity = activity;
        mDistrictList = districtList;
        mI = i;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderOne(LayoutInflater.from(mActivity).inflate(mI == 1 ? R.layout.item_typeofoperation : R.layout.seller_listview_item_right, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolderOne holderOne = (ViewHolderOne) holder;
        HotBean hotBean = mDistrictList.get(position);
        holderOne.mTowName.setText(hotBean.getPlaceName());
        holderOne.mTowName.setTextColor(position == mSelect ? UiUtils.getResources().getColor(R.color.RED_FB4E30) : UiUtils.getResources().getColor(R.color.text333));
        final int placeCode = hotBean.getPlaceCode();
        holderOne.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.ItemClickListener(position, placeCode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDistrictList.size();
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        @InjectView(R.id.tow_name)
        TextView mTowName;
        @InjectView(R.id.root_item)
        LinearLayout mRootItem;

        public ViewHolderOne(View view) {
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
    }

    public void setSelect(int select) {
        mSelect = select;
        notifyDataSetChanged();
    }
}
