package com.jsz.peini.ui.view.ImageSelector;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsz.peini.R;
import com.jsz.peini.listener.ImageCompressListener;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.view.ImageSelector.common.Callback;
import com.jsz.peini.ui.view.ImageSelector.common.Constant;
import com.jsz.peini.ui.view.ImageSelector.utils.FileUtils;
import com.jsz.peini.ui.view.ImageSelector.utils.StatusBarCompat;
import com.jsz.peini.utils.Bitmap.BitmapAndStringUtils;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.widget.FileRequestBody;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class ImgSelActivity extends FragmentActivity implements View.OnClickListener, Callback {

    public static final String INTENT_RESULT = "result";
    private static final int IMAGE_CROP_CODE = 1;
    private static final int STORAGE_REQUEST_CODE = 1;

    private ImgSelConfig config;

    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private Button btnConfirm;
    private LinearLayout ivBack;
    private String cropImagePath;
    private TextView mNumbar;
    private Button mUpload;
    private ImgSelFragment fragment;
    private ArrayList<String> result = new ArrayList<>();
    private TextView mUpland_numbar, mUpland_sign;
    private ProgressBar mUpland_numbar_progressbar;
    private ArrayList<String> mImageList;
    private LinearLayout mIsGon;

    public static void startActivity(Activity activity, ImgSelConfig config, int RequestCode) {
        Intent intent = new Intent(activity, ImgSelActivity.class);
        Constant.config = config;
        activity.startActivityForResult(intent, RequestCode);
    }

    public static void startActivity(Fragment fragment, ImgSelConfig config, int RequestCode) {
        Intent intent = new Intent(fragment.getActivity(), ImgSelActivity.class);
        Constant.config = config;
        fragment.startActivityForResult(intent, RequestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_sel);
        config = Constant.config;
        // Android 6.0 checkSelfPermission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        } else {
            fragment = ImgSelFragment.instance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fmImageList, fragment, null)
                    .commit();
        }

        initView();
        if (!FileUtils.isSdCardAvailable()) {
            Toast.makeText(this, getString(R.string.sd_disable), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mImageList = Constant.imageList;
        rlTitleBar = (RelativeLayout) findViewById(R.id.rlTitleBar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        ivBack = (LinearLayout) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        //显示
        mIsGon = (LinearLayout) findViewById(R.id.isGon);

        mUpland_numbar = (TextView) findViewById(R.id.upland_numbar); //个数
        mUpland_numbar_progressbar = (ProgressBar) findViewById(R.id.upland_numbar_progressbar); //进度
        mUpland_sign = (TextView) findViewById(R.id.upland_sign); //百分比

        //选择的数量
        mNumbar = (TextView) findViewById(R.id.selector_photo_number);
        //确定上传
        mUpload = (Button) findViewById(R.id.selector_determine_upload);
        int list = config.list;
        LogUtil.d("已选择图片的数量====" + list);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImageList.size() != 0) {
                    if (1 == config.mType) { //上传
                        setUserImage(mImageList);
                    } else if (2 == config.mType) {
                        Intent intent = getIntent();
                        intent.putStringArrayListExtra(Conversion.LIST, mImageList);
                        setResult(100, intent);
                        finish();
                        mImageList.clear();
                    }
                }
            }
        });

        if (config != null) {
            if (config.backResId != -1) {
                ivBack.setBackgroundResource(config.backResId);
            }
            if (config.statusBarColor != -1) {
                StatusBarCompat.compat(this, config.statusBarColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                        && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
            }
            rlTitleBar.setBackgroundColor(config.titleBgColor);
            tvTitle.setTextColor(config.titleColor);
            tvTitle.setText(config.title);
            mNumbar.setBackgroundColor(config.btnBgColor);
            mNumbar.setTextColor(config.btnTextColor);
            if (config.multiSelect) {
                /* + String.format(getString(R.string.confirm_format), config.btnText, Constant.imageList.size(), config.maxNum)*/
                mNumbar.setText("已选择 " + (Constant.imageList.size() + config.list) + " 张照片");
            } else {
                Constant.imageList.clear();
                mNumbar.setText(config.btnText);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnConfirm:
//                if (Constant.imageList != null && !Constant.imageList.isEmpty()) {
//                    exit();
//                }
                if (fragment == null || !fragment.hidePreview()) {
                    Constant.imageList.clear();
                    finish();
                }
                break;
            case R.id.ivBack:
                if (fragment == null || !fragment.hidePreview()) {
                    Constant.imageList.clear();
                    finish();
                }
                break;
        }

    }

    int uploadIndex = 0;
    int totalSize = 0;
    NumberFormat numberFormat = null;
    boolean uploadFailure = false;

    /*8我的相册 图片的上传*/
    private void setUserImage(final ArrayList<String> imageList) {
        LogUtil.d("这个是选择的几个图片", "个数--" + imageList.size());

        uploadIndex = 0;
        totalSize = imageList.size();

        mUpland_numbar_progressbar.setMax(totalSize);
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);

        mUpland_numbar_progressbar.setMax(totalSize);
        numberFormat = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        numberFormat.setMinimumFractionDigits(0);
        mUpland_numbar.setText("开始上传...");
        mIsGon.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int index = 0; !uploadFailure && index < totalSize; index++) {
                    String correction = Conversion.correctImage(imageList.get(index));
                    uplandImage(correction);
                }
            }
        }).start();

    }

    protected void showUploadFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ImgSelActivity.this);
        builder.setMessage("提示")
                .setTitle("上传照片失败，请稍后重试")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (fragment == null || !fragment.hidePreview()) {
                            Constant.imageList.clear();
                            finish();
                        }
                        finish();
                    }
                }).create().show();
    }


    private void uplandImage(final String imageList) {
        final RetrofitCallback<SuccessfulBean> callback = new RetrofitCallback<SuccessfulBean>() {
            @Override
            public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                //进度更新结束
                if (response.isSuccessful() && response.body().getResultCode() == 1) {
                    LogUtil.d("这个是上传成功的图片---", "成功" + response.body().toString());
                    uploadIndex++;
                    mUpland_numbar.setText("正在上传" + uploadIndex + "/" + totalSize);
                    mUpland_sign.setText(numberFormat.format((float) uploadIndex / totalSize));
                    mUpland_numbar_progressbar.setProgress(uploadIndex);

                    if (uploadIndex == totalSize) {
                        if (fragment == null || !fragment.hidePreview()) {
                            Constant.imageList.clear();
                            finish();
                        }
                        mIsGon.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                //进度更新结束
                LogUtil.d("这个是上传成功的图片---", "失败" + t.getMessage());
                if (!uploadFailure) {
                    uploadFailure = true;

                    if (fragment == null || !fragment.hidePreview()) {
                        Constant.imageList.clear();
                        finish();
                    }
                    mIsGon.setVisibility(View.GONE);
                    showUploadFailureDialog();
                }
            }

            @Override
            public void onLoading(long total, long progress) {
                super.onLoading(total, progress);
                //此处进行进度更新
                LogUtil.d("这个是上传成功的图片---", "总大小" + total + "进度条" + progress);
            }
        };
        BitmapAndStringUtils.saveBitmaps(this, imageList, 0, new ImageCompressListener() {
            @Override
            public void onCompressSuccess(String images) {
                File file = new File(images);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                //通过该行代码将RequestBody转换成特定的FileRequestBody
                FileRequestBody<SuccessfulBean> body = new FileRequestBody<>(requestBody, callback);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
                String userToken = (String) SpUtils.get(ImgSelActivity.this, "mUserToken", "");
                if (!"".equals(userToken)) {
                    RetrofitUtil.createService(SquareService.class).setUserImages(userToken, part).enqueue(callback);
                }
            }

            @Override
            public void onCompressFailed(String images, String msg) {
            }
        });
    }

    @Override
    public void onSingleImageSelected(String path) {
        if (config.needCrop) {
            crop(path);
        } else {
            Constant.imageList.add(path);
            exit();
        }
    }

    @Override
    public void onImageSelected(String path) {
//        mNumbar.setText("已选择 " + Constant.imageList.size() + " 张照片");
        mNumbar.setText("已选择 " + (Constant.imageList.size() + config.list) + " 张照片");
    }

    @Override
    public void onImageUnselected(String path) {
//        mNumbar.setText("已选择 " + Constant.imageList.size() + " 张照片");
        mNumbar.setText("已选择 " + (Constant.imageList.size() + config.list) + " 张照片");
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            if (config.needCrop) {
                crop(imageFile.getAbsolutePath());
            } else {
                Constant.imageList.add(imageFile.getAbsolutePath());
                exit();
            }
        }
    }

    @Override
    public void onPreviewChanged(int select, int sum, boolean visible) {
        if (visible) {
            tvTitle.setText(select + "/" + sum);
        } else {
            tvTitle.setText(config.title);
        }
    }

    private void crop(String imagePath) {
        File file = new File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.aspectX);
        intent.putExtra("aspectY", config.aspectY);
        intent.putExtra("outputX", config.outputX);
        intent.putExtra("outputY", config.outputY);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            Constant.imageList.add(cropImagePath);
            exit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void exit() {
        Intent intent = new Intent();
        result.clear();
        result.addAll(Constant.imageList);
        intent.putStringArrayListExtra(INTENT_RESULT, result);
        setResult(RESULT_OK, intent);

        if (!config.multiSelect) {
            Constant.imageList.clear();
        }

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fmImageList, ImgSelFragment.instance(), null)
                            .commitAllowingStateLoss();
                } else {
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment == null || !fragment.hidePreview()) {
            Constant.imageList.clear();
            super.onBackPressed();
        }
    }
}
