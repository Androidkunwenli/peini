package com.jsz.peini.ui.adapter.square;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jsz.peini.R;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.MyBackgroundActivity;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.LogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2017/1/21.
 */
public class MiBackgroundAdapter extends BaseRecyclerViewAdapter {

    public MyBackgroundActivity mActivity;
    public List<String> mList;

    public MiBackgroundAdapter(MyBackgroundActivity activity, List<String> list) {
        super(list);
        mActivity = activity;
        mList = list;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        final String s = mList.get(position);
        String url = IpConfig.HttpPeiniIp + s;
        LogUtil.d("空间背景url+" + url);
        GlideImgManager.loadImage(mActivity, url, holder.mIvImage, "5");
//        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + SpUtils.get(mActivity, "imageHead", ""), holder.mIvImage);
        holder.mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnClickListener(s);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_selector_background, viewGroup, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivImage)
        ImageView mIvImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private ItemClickListener mListener;

    public void setItemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    public interface ItemClickListener {
        void OnClickListener(String img);
    }
}
