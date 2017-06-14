package com.jsz.peini.ui.activity.square;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.SquareBean;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.adapter.square.ReleasePhotoAdapter;
import com.jsz.peini.ui.view.ImageSelector.ImageLoader;
import com.jsz.peini.ui.view.ImageSelector.ImgSelActivity;
import com.jsz.peini.ui.view.ImageSelector.ImgSelConfig;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.KeyBoardUtils;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.time.TimeUtils;
import com.jsz.peini.widget.UseCameraActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by th on 2016/12/27.
 */
public class SuareReleaseActivity extends BaseNotSlideActivity implements NonGestureLockInterface {
    private static final String TAG = "SuareReleaseActivity";
    private static final int RESULT_LOAD_IMAGE2 = 200;
    private static final int RESULT_LOAD_IMAGE = 100;
    private static final int RESULT_LOAD_IMAGE3 = 300;
    @InjectView(R.id.release_content)
    EditText mReleaseContent;
    @InjectView(R.id.release_photo)
    RecyclerView mReleasePhoto;
    @InjectView(R.id.release_address)
    TextView mReleaseAddress;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    private SuareReleaseActivity mActivity;
    List<String> mList = new ArrayList<>();
    private ReleasePhotoAdapter mPhotoAdapter;
    private Intent mIntent;
    /**
     * 返回的地址数据
     *
     * @deprecated mAddress 文字位置
     */
    private String mAddress = "";
    private String poiInfoSelete;


    private double mLatitude;
    private double mLongitude;
    public Popwindou mPop;
    private Uri mOutPutFileUri;
    private File mFileImage;

    private boolean isNonShowGestureLock = true;
    private int sign = 0;

    public int initLayoutId() {
        return R.layout.activity_suare_release;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mActivity = this;
        mTitle.setText("广场发布");
        mRightButton.setText("发布");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.hideKeyBoard(mActivity, mReleaseContent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        InitLodingPhoto();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRightButton.setClickable(true);
        mList.clear();
    }

    /**
     * 这个是加载图片的列表
     */
    private void InitLodingPhoto() {
        mPhotoAdapter = new ReleasePhotoAdapter(mActivity, mList);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 4);
        mReleasePhoto.setLayoutManager(manager);
        mReleasePhoto.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnPhotoFootClickListener(new ReleasePhotoAdapter.OnPhotoFootClickListener() {
            @Override
            public void FootClickListener(int position) {
                initSelect();
                KeyBoardUtils.hideKeyBoard(mActivity, mReleaseContent);
            }

            @Override
            public void deleteListener(final int position) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("确定要删除照片吗?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                mList.remove(position);
                                mPhotoAdapter.notifyItemRemoved(position);
                                mPhotoAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @OnClick({R.id.right_button, R.id.release_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_button:
                String Content = mReleaseContent.getText().toString().trim();
                if (TextUtils.isEmpty(Content) && mList.size() <= 0) {
                    Toasty.normal(mActivity, "请您输入发布信息或者上传照片!").show();
                    return;
                }
                KeyBoardUtils.hideKeyBoard(mActivity, mReleaseContent);
                if ("所在位置".equals(mAddress) || "不显示位置".equals(mAddress)) {
                    mAddress = "";
                }
                if (SpUtils.isCompleteUserInfo(mActivity)) {
                    mRightButton.setClickable(false);
                    initNetWork(SpUtils.getUserToken(mActivity), mLatitude, mLongitude, Content, mAddress);
                } else {
                    CompleteUserInfoActivity.actionShow(mActivity, SpUtils.getUserToken(mActivity));
                }

                break;
            case R.id.release_address:
                mIntent = new Intent(mActivity, SquarePeriphery.class);
                mIntent.putExtra("Address", mAddress);
                mIntent.putExtra("poiInfoSelete", poiInfoSelete);
                startActivityForResult(mIntent, RESULT_LOAD_IMAGE3);
                break;
        }
    }

    /**
     * 广场发布
     *
     * @param muId
     * @param latitude
     * @param longitude
     * @param trim
     * @param address
     */
    private void initNetWork(final String muId, double latitude, double longitude, final String trim, final String address) {
        DecimalFormat df = new DecimalFormat("######0.000000");
        final String sexLatitude = df.format(latitude);
        final String sexLongitude = df.format(longitude);
        LogUtil.i(TAG, "第一个参数" + muId);
        LogUtil.i(TAG, "第二个参数" + sexLatitude);
        LogUtil.i(TAG, "第三个参数" + sexLongitude);
        LogUtil.i(TAG, "第四个参数" + trim);
        LogUtil.i(TAG, "第五个参数" + address);
        Intent intentTemp = new Intent();
        final ArrayList<ImageListBean> imageListBeen = new ArrayList<>();
        for (String image : mList) {
            imageListBeen.add(new ImageListBean(image, false));
        }
        SquareBean.SquareListBean bean = new SquareBean.SquareListBean();
        bean.setUserId(SpUtils.getUserToken(mActivity));
        bean.setNickname(SpUtils.getNickname(mActivity));
        bean.setImageHead(SpUtils.getImageHead(mActivity));
        bean.setContent(trim);
        bean.setImageList(imageListBeen);
        bean.setSquareTime(TimeUtils.getSquareTime());
        bean.setAddress(address);
        bean.setLike(false);
        bean.setLatitude(sexLatitude);
        bean.setLongitude(sexLongitude);
        bean.setLocal(false);
        Bundle bundle = new Bundle();
        String toJson = new Gson().toJson(bean);
        bundle.putString(Conversion.BEAN, toJson);
        intentTemp.putExtras(bundle);
        setResult(3000, intentTemp);
        finish();
    }


    private void initSelect() {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_suare_release));
        View view = UiUtils.inflate(mActivity, R.layout.item_popupwindows_selete);
        mPop.init(view, Gravity.BOTTOM, true);
        TextView tvSelectPhone = (TextView) view.findViewById(R.id.item_popupwindows_camera);
        tvSelectPhone.setText("从手机相册选择");
        tvSelectPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //从相册选择
                mPop.dismiss();
                isOpenPhoto();
            }
        });

        TextView bt2 = (TextView) view
                .findViewById(R.id.item_popupwindows_Photo); //拍照
        TextView bt3 = (TextView) view
                .findViewById(R.id.item_popupwindows_cancel);

        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  //拍照
                String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
                if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
                    Intent intent = new Intent(mActivity,
                            UseCameraActivity.class);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE2);

                    isNonShowGestureLock = false;
                } else {
                    Toast.makeText(mActivity, "sdcard不可用", Toast.LENGTH_SHORT).show();
                }
                mPop.dismiss();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //取消
                mPop.dismiss();
            }
        });
    }

    /**
     * 打开相册
     */
    private void isOpenPhoto() {
        // 自由配置选项
        ImgSelConfig config = new ImgSelConfig.Builder(mActivity, new ImageLoader() {
            // 自定义图片加载器
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        }).multiSelect(true)
                // 第一个是否显示相机
                .needCamera(false)
                // 最大选择图片数量
                .maxNum(9)
                //选择图片还是上传图片
                .setType(2)
                //已选照片
                .setList(mList.size())
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_LOAD_IMAGE && null != data) {
            final ArrayList<String> list = data.getStringArrayListExtra(Conversion.LIST);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (String images : list) {
                        if (mList.size() < 9) {
                            mList.add(images);
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPhotoAdapter.setPhotoList(mList);
                                }
                            });
                        }
                    }
                }
            }).start();
        } else if (requestCode == RESULT_LOAD_IMAGE2 && data != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String extra = data.getStringExtra(UseCameraActivity.IMAGE_PATH);
                        String correction = Conversion.correctImage(extra);
                        if (mList.size() < 9) {
                            mList.add(correction);
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPhotoAdapter.setPhotoList(mList);
                                }
                            });
                        }
                    } catch (Exception e) {
                        LogUtil.d("相机拍照返回的数据错误" + e.getMessage());
                        finish();
                    }

                }
            }).start();
        } else if (requestCode == RESULT_LOAD_IMAGE3 && null != data) {
            Bundle bundle = data.getExtras();
            if (null != bundle) {
                mAddress = bundle.getString("Address");
                poiInfoSelete = bundle.getString("poiInfoSelete");
                mLatitude = bundle.getDouble("latitude");
                mLongitude = bundle.getDouble("longitude");
                mReleaseAddress.setText(mAddress);
                mReleaseAddress.setTextColor(getResources().getColor(R.color.text999));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isNonShowGestureLock = true;
    }

    @Override
    public boolean isGestureLock() {
        return isNonShowGestureLock;
    }
}
