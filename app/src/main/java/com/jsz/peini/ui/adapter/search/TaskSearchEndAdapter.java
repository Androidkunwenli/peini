package com.jsz.peini.ui.adapter.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jsz.peini.R;
import com.jsz.peini.ui.activity.filter.TaskSearchActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 15089 on 2017/2/13.
 */
public class TaskSearchEndAdapter extends RecyclerView.Adapter {

    private static final int TASK_SEARCH_ONE = 0;
    private static final int TASK_SEARCH_TOW = 1;
    private static final int TASK_SEARCH = 2001;
    private final TaskSearchActivity mActivity;
    private final ArrayList<PoiInfo> mPoiInfos;
    private final double mXpoint;
    private final double mYpoint;

    public TaskSearchEndAdapter(TaskSearchActivity activity, ArrayList<PoiInfo> poiInfos, double xpoint, double ypoint) {
        mActivity = activity;
        mPoiInfos = poiInfos;
        mXpoint = xpoint;
        mYpoint = ypoint;
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

    private void showViewImformation(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        PoiInfo poiInfo = mPoiInfos.get(position);
        final String name = poiInfo.name;
        viewHolder.mSearchAddress.setText(name);
        final String address = poiInfo.address;
        viewHolder.mSearchAddressInformation.setText(address);
        LatLng location = poiInfo.location;
        final double latitude = location.latitude;
        final double longitude = location.longitude;
        final String distanceText;
        double distance = getDistance(mXpoint, mYpoint, latitude, longitude);
        if (distance < 1000) {
            distanceText = new DecimalFormat("0").format(distance) + "m";
        } else {
            double distanceDouble = distance / 1000;
            distanceText = new DecimalFormat("0.00").format(distanceDouble) + "km";
        }
        viewHolder.mSearchAddressDistance.setText(distanceText);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClick(name, distanceText, address, latitude, longitude);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPoiInfos.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.search_address)
        TextView mSearchAddress;
        @InjectView(R.id.search_address_distance)
        TextView mSearchAddressDistance;
        @InjectView(R.id.search_address_information)
        TextView mSearchAddressInformation;

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

    /**
     * 补充：计算两点之间真实距离
     *
     * @return 米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        LatLng latLng = new LatLng(longitude1, latitude1);
        LatLng latLng1 = new LatLng(longitude2, latitude2);
        return DistanceUtil.getDistance(latLng, latLng1);
    }

    private OnitemClickListener mListener;

    public void setOnitemClickListener(OnitemClickListener listener) {
        mListener = listener;
    }

    public interface OnitemClickListener {
        void itemClick(String name, String distance, String address, double latitude, double longitude);

    }
}
