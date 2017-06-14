package com.jsz.peini.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.view.GlideRoundTransform;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.SpUtils;

@SuppressLint("ValidFragment")
public class ImageHendDialogFragment extends DialogFragment implements View.OnClickListener {
    //    private Fragment mFragment;
//    private Activity mActivity;
    private Context mContext;
    private SelectPhotoMonitorDialogListener mListener;
    //如果是 1 就是选择照片  2 就是我知道了
    private int mModel = 1;
    //图片地址
    private String mImageHend;
    private ImageView mIvImage;
    private ImageView mIvType;
    private TextView mTvTitle;
    private TextView mTvContentOne;
    private TextView mTvContentTwo;
    private TextView mTvButtonName;
    private Intent mIntent;
    private int mWidth;
    private int mHeight;

//    SelectPhotoMonitorDialogListener listener;

    /**
     * 没有图片
     */
    public ImageHendDialogFragment(Context context, SelectPhotoMonitorDialogListener listener, int model) {
        mContext = context;
        mListener = listener;
        mModel = model;
    }

    public interface SelectPhotoMonitorDialogListener {
        /**
         * 选择照片
         */
        void SelectPhoto();

        /**
         * 拍照
         */
        void PhotoGraph();
        /**
         *图片上传了
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_edit_name, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        mIvImage = (ImageView) view.findViewById(R.id.iv_image);
        mIvType = (ImageView) view.findViewById(R.id.iv_type);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvContentOne = (TextView) view.findViewById(R.id.tv_content_one);
        mTvContentTwo = (TextView) view.findViewById(R.id.tv_content_two);
        mTvButtonName = (TextView) view.findViewById(R.id.tv_button_name);
        mTvButtonName.setOnClickListener(this);
        if (mModel == 1) {
            mTvTitle.setText(Html.fromHtml("<font color=#333333>" + "请换上" + "<font/>" + "<font color=#FB4E30>" + "真实头像" + "<font/>"));
            mTvContentOne.setText("由于头像不真实,你被" + "屏蔽" + "了");
            mTvContentTwo.setText(Html.fromHtml("<font color=#FB4E30>" + "无法参加任务互动," + "<font/>" + "<font color=#999999>" + "快去换个新头像吧~" + "<font/>"));
            mTvButtonName.setText("更换头像");
            mIvType.setImageResource(R.mipmap.image_hend_error);
        } else if (mModel == 2) {
            mTvTitle.setText("头像仍在审核中");
            mTvContentOne.setText("请您耐心等待审核");
            mTvContentTwo.setText("通过后即可参加任务互动中~");
            mTvButtonName.setText("我知道了");
            mIvType.setImageResource(R.mipmap.image_hend_looding);
        } else if (mModel == 3) {
            mTvTitle.setText("新头像已提交人工审核!");
            mTvContentOne.setText("审核结果将通过陪你小助手通知您,请注");
            mTvContentTwo.setText("意查收哦~");
            mTvButtonName.setText("我知道了");
            mIvType.setImageResource(R.mipmap.image_hend_looding);
        }
//        getDialog().setCancelable(true);
//        getDialog().setCanceledOnTouchOutside(false);
        mImageHend = SpUtils.getImageHead(mContext);
        String url;
        if (mImageHend.contains("PEINI_CACHE")) {
            url = mImageHend;
        } else {
            url = IpConfig.HttpPic + mImageHend;
        }
        Glide.with(mContext.getApplicationContext())
                .load(url)
                .transform(new GlideRoundTransform(mContext, dip2px(3)))
                .error("1".equals(SpUtils.getSex(mContext)) ? R.mipmap.ic_nan : R.mipmap.ic_nv)
                .into(mIvImage);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = mWidth - dip2px(60);
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
        return view;
    }

    /**
     * 设置图片
     *
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        if (mIvImage != null && !TextUtils.isEmpty(imageUrl)) {
            Glide.with(mContext.getApplicationContext())
                    .load(imageUrl)
                    .transform(new GlideRoundTransform(mContext, dip2px(3)))
                    .error("1".equals(SpUtils.getSex(mContext)) ? R.mipmap.ic_nan : R.mipmap.ic_nv)
                    .into(mIvImage);
            mModel = 3;
            mTvTitle.setText("新头像已提交人工审核!");
            mTvContentOne.setText("审核结果将通过陪你小助手通知您,请注");
            mTvContentTwo.setText("意查收哦~");
            mTvButtonName.setText("我知道了");
            mIvType.setImageResource(R.mipmap.image_hend_looding);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_button_name:
                if (mModel == 1) {
                    onFinishEditDialog();
                } else if (mModel == 2) {
                    dismiss();
                } else if (mModel == 3) {
                    dismiss();
                }
                break;
        }
    }

    public void onFinishEditDialog() {
        final BottomSheetDialog mPopwindou = new BottomSheetDialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_photograph, null);
        view.findViewById(R.id.item_popupwindows_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//选择
//                listener = (SelectPhotoMonitorDialogListener) mContext;
                mListener.SelectPhoto();
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.item_popupwindows_Photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//拍照
//                listener = (SelectPhotoMonitorDialogListener) mContext;
                mListener.PhotoGraph();
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//选择
                mPopwindou.dismiss();
            }
        });
        mPopwindou.setContentView(view);
        mPopwindou.show();
    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        listener = (SelectPhotoMonitorDialogListener) context;
//    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
