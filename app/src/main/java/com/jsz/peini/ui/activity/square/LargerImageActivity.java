package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.service.DownLoadImageService;
import com.jsz.peini.service.ImageDownLoadCallBack;
import com.jsz.peini.ui.activity.report.ReportActivity;
import com.jsz.peini.ui.adapter.square.PhotoViewPager;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.ShareUtils;
import com.jsz.peini.utils.UiUtils;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class LargerImageActivity extends BaseNotSlideActivity {
    @InjectView(R.id.tv_vp)
    PhotoViewPager mTvVp;
    @InjectView(R.id.image_numbar)
    TextView mImageNumbar;
    /**
     * 图片数量
     */
    private ArrayList<ImageListBean> mListBeen;
    private LargerImageActivity mActivity;
    private int mShowIndex;
    private String TAG = "LargerImageActivity";
    private String mImageSrc;
    private int imageListBeanId;
    private Intent mIntent;
    private int mType;
    private boolean mIsFlie;

    @Override
    public int initLayoutId() {
        return R.layout.activity_larger_image;
    }

    @Override
    public void showAllVisual(boolean b) {
        super.showAllVisual(true);
    }

    @Override
    public void initView() {
        mActivity = this;
        mListBeen = (ArrayList<ImageListBean>) getIntent().getSerializableExtra(Conversion.LARGERIMAGEACTIVITY);
        mShowIndex = getIntent().getIntExtra(Conversion.SHOWINDEX, 0);
        mType = getIntent().getIntExtra(Conversion.TYPE, 0);
        mIsFlie = getIntent().getBooleanExtra(Conversion.FILE, false);
        mImageSrc = mListBeen.get(mShowIndex).getImageSrc();
        imageListBeanId = mListBeen.get(mShowIndex).getId();

        if (mListBeen == null && mListBeen.size() <= 0) {
            Toasty.normal(mActivity, "参数错误").show();
            finish();
        }

    }

    @Override
    public void initData() {
        if (null == mListBeen) {
            return;
        }
        if (mListBeen.size() <= 1) {
            mImageNumbar.setText("");
        } else {
            mImageNumbar.setText((mShowIndex + 1) + "/" + mListBeen.size());
        }
        mTvVp.setAdapter(new PagerAdapter() {
            // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
            @Override
            public int getCount() {
                return mListBeen.size();
            }

            // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
            @Override
            public void destroyItem(ViewGroup view, int position, Object object) {
                view.removeView((View) object);
            }

            // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
            @Override
            public Object instantiateItem(ViewGroup view, final int position) {
                final String getImageSmall = IpConfig.HttpPic + mListBeen.get(position).getImageSmall();//小图
                final String imageSrc = mListBeen.get(position).getImageSrc();
                final String getImageSrc = IpConfig.HttpPic + imageSrc;//大图
                View iamgeView = UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_photoview, view);
                final ImageView ivPreloadImage = (ImageView) iamgeView.findViewById(R.id.iv_preload_image);
                final ProgressBar pbImage = (ProgressBar) iamgeView.findViewById(R.id.pb_image);
                final PhotoView ivFormalImage = (PhotoView) iamgeView.findViewById(R.id.iv_formal_image);


                if (mIsFlie) {
                    Glide.with(mActivity.getApplicationContext())
                            .load(TextUtils.isEmpty(mListBeen.get(position).getImageSrc()) ? mListBeen.get(position).getImageSmall() : mListBeen.get(position).getImageSrc())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                    pbImage.setVisibility(View.GONE);
                                    ivPreloadImage.setVisibility(View.GONE);
                                    ivFormalImage.setImageBitmap(bitmap);
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    Toasty.normal(mActivity, "加载失败!").show();
                                }
                            });
                } else {
                    if (!TextUtils.isEmpty(getImageSmall)) {
                        GlideImgManager.loadImage(mActivity, getImageSmall, ivPreloadImage);
                        pbImage.setVisibility(View.VISIBLE);
                        Glide.with(mActivity.getApplicationContext())
                                .load(getImageSrc)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                        pbImage.setVisibility(View.GONE);
                                        ivPreloadImage.setVisibility(View.GONE);
                                        ivFormalImage.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                        super.onLoadFailed(e, errorDrawable);
                                        Glide.with(mActivity.getApplicationContext())
                                                .load(imageSrc)
                                                .asBitmap()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                        pbImage.setVisibility(View.GONE);
                                                        ivPreloadImage.setVisibility(View.GONE);
                                                        ivFormalImage.setImageBitmap(bitmap);
                                                    }

                                                    @Override
                                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                        super.onLoadFailed(e, errorDrawable);
                                                        pbImage.setVisibility(View.GONE);
                                                        ivFormalImage.setBackground(getResources().getDrawable(R.mipmap.addphotos));
                                                    }
                                                });
                                    }
                                });
                    }
                }

                ivFormalImage.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        mActivity.finish();
                    }
                });
                ivFormalImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showLog(getImageSrc);
                        return true;
                    }
                });
                view.addView(iamgeView);
                return iamgeView;
            }
        });

        mTvVp.setCurrentItem(mShowIndex);

        mTvVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageListBean imageListBean = mListBeen.get(position);
                mImageSrc = imageListBean.getImageSrc();
                imageListBeanId = imageListBean.getId();

                mImageNumbar.setText((position + 1) + "/" + mListBeen.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showLog(final String imageThumbUrl) {
        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        View dialogView = LayoutInflater.from(mActivity)
                .inflate(R.layout.layout_bottom, null);
        TextView tvTakePhoto = (TextView) dialogView.findViewById(R.id.tv_take_photo);
        TextView tvPhotoAlbum = (TextView) dialogView.findViewById(R.id.tv_photo_album);
        TextView tvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        TextView tvPhotoShare = (TextView) dialogView.findViewById(R.id.tv_photo_share);
        if (mType == 0) {
            tvPhotoAlbum.setVisibility(View.GONE);
        } else {
            if (imageListBeanId == 0) {
                tvPhotoAlbum.setVisibility(View.GONE);
            } else {
                tvPhotoAlbum.setVisibility(View.VISIBLE);
            }
        }
        tvPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageListBeanId == 0) {
                    return;
                }
                dialog.dismiss();
                //举报
                mIntent = new Intent(mActivity, ReportActivity.class);
                mIntent.putExtra(Conversion.TYPE, "5");
                mIntent.putExtra("reportId", String.valueOf(imageListBeanId));
                startActivity(mIntent);
            }
        });
        tvPhotoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mImageSrc)) {
                    return;
                }
                dialog.dismiss();
                //分享
                UMImage image = new UMImage(mActivity, IpConfig.HttpPic + mImageSrc);
                image.setThumb(image);
                new ShareUtils(mActivity, image);
            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "保存图片", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (TextUtils.isEmpty(mImageSrc)) {
                    return;
                }
                onDownLoad(imageThumbUrl);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();


    }

    /**
     * 启动图片下载线程
     */
    private void onDownLoad(String url) {
        DownLoadImageService service = new DownLoadImageService(getApplicationContext(), url,
                new ImageDownLoadCallBack() {
                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap, final String path) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.normal(mActivity, "保存成功\n" + path).show();
                            }
                        });
                        Log.i(TAG, "onDownLoadSuccess: " + "在这里执行图片保存方法" + path);
                    }

                    @Override
                    public void onDownLoadFailed() {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.normal(mActivity, "图片保存失败").show();
                            }
                        });
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }
}
