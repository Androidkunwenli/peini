package com.jsz.peini.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.jsz.peini.R;
import com.jsz.peini.ui.view.Popwindou;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import es.dmoral.toasty.Toasty;

/**
 * Created by lenovo on 2017/3/11.
 */

public class ShareUtils {

    /**
     * 分享链接
     */
    public ShareUtils(final Activity mActivity, final UMWeb umWeb) {
        final BottomSheetDialog mPopwindou = new BottomSheetDialog(mActivity);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pop_share, null);
        final ShareAction shareAction = new ShareAction(mActivity);
        view.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭分享
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_wechatcircleoffriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isWeixinAvilible(mActivity)) {
                    Toasty.normal(mActivity, "请安装微信后重试!").show();
                    return;
                }
                //微信朋友圈分享
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isWeixinAvilible(mActivity)) {
                    Toasty.normal(mActivity, "请安装微信后重试!").show();
                    return;
                }
                //微信分享
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_qqcircleoffriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isQQClientAvailable(mActivity)) {
                    Toasty.normal(mActivity, "请安装QQ后重试!").show();
                    return;
                }
                //qq空间分享
                shareAction.setPlatform(SHARE_MEDIA.QZONE)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isQQClientAvailable(mActivity)) {
                    Toasty.normal(mActivity, "请安装QQ后重试!").show();
                    return;
                }
                shareAction.setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                //qq分享
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_xinlangcircleoffriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isWeiboInstalled(mActivity)) {
                    Toasty.normal(mActivity, "请安装微博后重试!").show();
                    return;
                }
                //新浪微博分享
                String title = umWeb.getDescription();
                umWeb.setDescription(title + "@陪你PN");
                shareAction.setPlatform(SHARE_MEDIA.SINA)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                mPopwindou.dismiss();
            }
        });
        mPopwindou.setContentView(view);
        mPopwindou.show();
    }

    /**
     * 分享图片
     */
    public ShareUtils(final Activity mActivity, final UMImage umWeb) {
        final BottomSheetDialog mPopwindou = new BottomSheetDialog(mActivity);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pop_share, null);
        final ShareAction shareAction = new ShareAction(mActivity);
        view.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭分享
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_wechatcircleoffriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isWeixinAvilible(mActivity)) {
                    Toasty.normal(mActivity, "请安装微信后重试!").show();
                    return;
                }
                //微信朋友圈分享
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isWeixinAvilible(mActivity)) {
                    Toasty.normal(mActivity, "请安装微信后重试!").show();
                    return;
                }
                //微信分享
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_qqcircleoffriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isQQClientAvailable(mActivity)) {
                    Toasty.normal(mActivity, "请安装QQ后重试!").show();
                    return;
                }
                //qq空间分享
                shareAction.setPlatform(SHARE_MEDIA.QZONE)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                mPopwindou.dismiss();
            }
        });
        final ShareContent shareContent = new ShareContent();
        shareContent.mText = "陪你";
        view.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isQQClientAvailable(mActivity)) {
                    Toasty.normal(mActivity, "请安装QQ后重试!").show();
                    return;
                }
                shareAction.setPlatform(SHARE_MEDIA.QQ)
                        .setShareContent(shareContent)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                //qq分享
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.share_xinlangcircleoffriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PeiNiUtils.isWeiboInstalled(mActivity)) {
                    Toasty.normal(mActivity, "请安装微博后重试!").show();
                    return;
                }
                //新浪微博分享
                shareAction.setPlatform(SHARE_MEDIA.SINA)
                        .withMedia(umWeb)
                        .setCallback(getListener(mActivity))
                        .share();
                mPopwindou.dismiss();
            }
        });
        mPopwindou.setContentView(view);
        mPopwindou.show();
    }

    @NonNull
    private UMShareListener getListener(final Activity mActivity) {
        return new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Toasty.success(mActivity, " 分享成功").show();

            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toasty.normal(mActivity, "分享失败").show();
                if (t != null) {
                    LogUtil.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toasty.normal(mActivity, "分享取消").show();
            }
        };
    }
}
