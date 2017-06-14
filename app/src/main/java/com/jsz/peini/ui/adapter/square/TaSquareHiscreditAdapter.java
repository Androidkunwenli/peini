package com.jsz.peini.ui.adapter.square;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.TaCreditBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.UiUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jsz.peini.utils.UiUtils.getResources;

/**
 * Created by lenovo on 2017/5/9.
 */

public class TaSquareHiscreditAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private final List<TaCreditBean> mTaCreditBeen;

    public TaSquareHiscreditAdapter(Context context, List<TaCreditBean> taCreditBeen) {
        mContext = context;
        mTaCreditBeen = taCreditBeen;
    }

    /*显示数据*/
    private void showView(ViewHolder helper, TaCreditBean mCreditBean) {
        //我的数据
        TaCreditBean.MyCreditBean myCredit = mCreditBean.getMyCredit();
        //他人数据
        TaCreditBean.OtherCreditBean otherCredit = mCreditBean.getOtherCredit();
        /*我的信用数据*/
        int creditNum = myCredit.getCreditNum();
        int idcardNum = myCredit.getIdcardNum();
        int selfNum = myCredit.getSelfNum();
        int taskNum = myCredit.getTaskNum();
        String imgHead = myCredit.getImgHead();
        String nickName = myCredit.getNickName();
        String updateTime = myCredit.getUpdateTime();

        /*他人信用数据*/
        int creditNum1 = otherCredit.getCreditNum();
        int idcardNum1 = otherCredit.getIdcardNum();
        int selfNum1 = otherCredit.getSelfNum();
        int taskNum1 = otherCredit.getTaskNum();
        String imgHead1 = otherCredit.getImgHead();
        String nickName1 = otherCredit.getNickName();
        String updateTime1 = otherCredit.getUpdateTime();
        int mi = creditNum + idcardNum + selfNum + taskNum;
        int ta = creditNum1 + idcardNum1 + selfNum1 + taskNum1;
        //信用对比
        if (mi == ta) {
            helper.mMiType.setImageResource(R.drawable.pingju);
            helper.mTaType.setImageResource(R.drawable.pingju);
            helper.mMiTaskNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color_fail));
            helper.mMiSelfNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color_fail));
            helper.mMiIdcardNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color_fail));

            helper.mTaTaskNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color_fail));
            helper.mTaSelfNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color_fail));
            helper.mTaIdcardNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color_fail));

        } else if (mi > ta) {
            helper.mMiType.setImageResource(R.drawable.shengli);
            helper.mTaType.setImageResource(R.drawable.baibei);
            helper.mMiTaskNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color));
            helper.mMiSelfNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color));
            helper.mMiIdcardNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color));

            helper.mTaTaskNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color_fail));
            helper.mTaSelfNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color_fail));
            helper.mTaIdcardNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color_fail));
        } else if (mi < ta) {
            helper.mMiType.setImageResource(R.drawable.baibei);
            helper.mTaType.setImageResource(R.drawable.shengli);

            helper.mMiTaskNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color_fail));
            helper.mMiSelfNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color_fail));
            helper.mMiIdcardNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.un_progressbar_color_fail));

            helper.mTaTaskNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color));
            helper.mTaSelfNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color));
            helper.mTaIdcardNumPb.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_color));
        }
        helper.mMiTaskNumPb.setProgress(100 - taskNum);
        helper.mMiSelfNumPb.setProgress(100 - selfNum);
        helper.mMiIdcardNumPb.setProgress(100 - idcardNum);

        helper.mTaTaskNumPb.setProgress(taskNum1);
        helper.mTaSelfNumPb.setProgress(selfNum1);
        helper.mTaIdcardNumPb.setProgress(idcardNum1);

        helper.mMiNickName.setText(nickName);
        helper.mTaNickName.setText(nickName1);

        GlideImgManager.loadImage(mContext, IpConfig.HttpPic + imgHead, helper.mMiImgHead);
        GlideImgManager.loadImage(mContext, IpConfig.HttpPic + imgHead1, helper.mTaImgHead);

        helper.mMiCreditNum.setText(creditNum + "分");
        helper.mTaCreditNum.setText(creditNum1 + "分");

        helper.mMiTaskNum.setText(taskNum + "%");
        helper.mTaTaskNum.setText(taskNum1 + "%");

        helper.mMiSelfNum.setText(selfNum + "%");
        helper.mTaSelfNum.setText(selfNum1 + "%");

        helper.mMiIdcardNum.setText(idcardNum + "%");
        helper.mTaIdcardNum.setText(idcardNum1 + "%");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(UiUtils.inflateRecyclerviewAdapter(mContext, R.layout.item_tasquarehiscredt, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mTaCreditBeen != null && mTaCreditBeen.size() != 0) {
            TaCreditBean mCreditBean = mTaCreditBeen.get(position);
            showView((ViewHolder) holder, mCreditBean);
        }
    }

    @Override
    public int getItemCount() {
        return Conversion.getSize(mTaCreditBeen);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.mi_type)
        ImageView mMiType;
        @InjectView(R.id.mi_imgHead)
        CircleImageView mMiImgHead;
        @InjectView(R.id.mi_nickName)
        TextView mMiNickName;
        @InjectView(R.id.mi_creditNum)
        TextView mMiCreditNum;
        @InjectView(R.id.ta_type)
        ImageView mTaType;
        @InjectView(R.id.ta_imgHead)
        CircleImageView mTaImgHead;
        @InjectView(R.id.ta_nickName)
        TextView mTaNickName;
        @InjectView(R.id.ta_creditNum)
        TextView mTaCreditNum;
        @InjectView(R.id.mi_selfNum)
        TextView mMiSelfNum;
        @InjectView(R.id.ta_selfNum)
        TextView mTaSelfNum;
        @InjectView(R.id.mi_selfNum_pb)
        ProgressBar mMiSelfNumPb;
        @InjectView(R.id.ta_selfNum_pb)
        ProgressBar mTaSelfNumPb;
        @InjectView(R.id.mi_idcardNum)
        TextView mMiIdcardNum;
        @InjectView(R.id.ta_idcardNum)
        TextView mTaIdcardNum;
        @InjectView(R.id.mi_idcardNum_pb)
        ProgressBar mMiIdcardNumPb;
        @InjectView(R.id.ta_idcardNum_pb)
        ProgressBar mTaIdcardNumPb;
        @InjectView(R.id.mi_taskNum)
        TextView mMiTaskNum;
        @InjectView(R.id.ta_taskNum)
        TextView mTaTaskNum;
        @InjectView(R.id.mi_taskNum_pb)
        ProgressBar mMiTaskNumPb;
        @InjectView(R.id.ta_taskNum_pb)
        ProgressBar mTaTaskNumPb;
        @InjectView(R.id.textView6)
        TextView mTextView6;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
