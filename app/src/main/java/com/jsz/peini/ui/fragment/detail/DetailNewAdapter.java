package com.jsz.peini.ui.fragment.detail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.square.ScoreHistoryListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.UiUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by th on 2017/1/18.
 */
public class DetailNewAdapter extends BaseQuickAdapter<ScoreHistoryListBean.DataBean, BaseViewHolder> {

    private final Activity mActivity;

    public DetailNewAdapter(Activity activity) {
        super(R.layout.item_detail);
        mActivity = activity;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ScoreHistoryListBean.DataBean dataBean) {
        holder.setText(R.id.createTime, dataBean.getCreateTime() + "");
        holder.setText(R.id.name, dataBean.getName() + "");
        String valNum = dataBean.getValNum();
        if (valNum.contains("-")) {
            holder.setText(R.id.valNum, valNum + "积分");
            holder.setImageResource(R.id.type, R.mipmap.hui_fen);
            holder.setTextColor(R.id.valNum, UiUtils.getResources().getColor(R.color.text999));
        } else {
            holder.setText(R.id.valNum, valNum + "积分");
            holder.setImageResource(R.id.type, R.mipmap.fen);
            holder.setTextColor(R.id.valNum, UiUtils.getResources().getColor(R.color.RED_FB4E30));
        }
        String url = IpConfig.HttpPic + dataBean.getImgSrc();
        Glide.with(mActivity.getApplicationContext())
                .load(url)
                .asBitmap()
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.setImageBitmap(R.id.imgSrc, resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        holder.setImageResource(R.id.imgSrc, R.mipmap.ic_launcher);
                    }
                });
    }
}
