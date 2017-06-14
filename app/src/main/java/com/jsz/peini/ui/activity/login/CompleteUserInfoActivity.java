package com.jsz.peini.ui.activity.login;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.login.LoginSuccess;
import com.jsz.peini.presenter.login.LoginService;
import com.jsz.peini.ui.transformation.RoundCornerTransformation;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.CacheActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.KeyBoardUtils;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.widget.UseCameraActivity;
import com.jsz.peini.widget.data.DatePickerDialog;
import com.jsz.peini.widget.data.DateUtil;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 完善资料
 */
public class CompleteUserInfoActivity extends BaseNotSlideActivity implements NonGestureLockInterface {

    private static final String EXTRA_USER_ID_FLAG = "extra_user_id_flag";
    private static final int PHOTO_REQUEST_CUT = 100;//截取图片
    private static final int RESULT_LOAD_IMAGE2 = 200; //拍照
    private static final int RESULT_LOAD_IMAGE = 300; //选择图片

    @InjectView(R.id.ll_activity_complete_info)
    LinearLayout mRootView;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.iv_real_avatar)
    ImageView mIvRealAvatar;
    @InjectView(R.id.et_nickname)
    EditText mEtNickname;
    @InjectView(R.id.tv_nickname_hint)
    TextView mTvNicknameHint;
    @InjectView(R.id.tv_sex)
    TextView mTvSex;
    @InjectView(R.id.tv_birthday)
    TextView mTvBirthday;

    private String mUserId;
    private String mBirthday;
    private int mAge = 0;
    private String mNickName;
    private String mSex = "0";//性别默认

    private CompleteUserInfoActivity mActivity;
    private int mAnInt;
    private Uri mCroppedImageUri;

    public static void actionShow(Context context, String userId) {
        Intent intent = new Intent(context, CompleteUserInfoActivity.class);
        intent.putExtra(EXTRA_USER_ID_FLAG, userId);
        context.startActivity(intent);
    }

    @Override
    public int initLayoutId() {
        return R.layout.activity_complete_info;
    }

    @Override
    public void initView() {
        mActivity = this;
        if (!CacheActivity.activityList.contains(mActivity)) {
            CacheActivity.addActivity(mActivity);
        }
        mTitle.setText("完善信息");
        mUserId = getIntent().getStringExtra(EXTRA_USER_ID_FLAG);

        mEtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mTvNicknameHint.setVisibility(View.VISIBLE);
                } else {
                    mTvNicknameHint.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.toolbar, R.id.iv_real_avatar, R.id.ll_sex, R.id.ll_birthday, R.id.btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.iv_real_avatar:
                showPopSelector();
                break;
            case R.id.ll_sex:
                mTvSex.requestFocus();
                KeyBoardUtils.hideKeyBoard(mActivity, mEtNickname);
                selectManOrWoMen(mTvSex);
                break;
            case R.id.ll_birthday:
                mTvBirthday.requestFocus();
                KeyBoardUtils.hideKeyBoard(mActivity, mEtNickname);
                showDate(mTvBirthday);
                break;
            case R.id.btn_done:
                confirmToCompleteInfo();
                break;
        }
    }

    private void showDate(TextView textView) {
        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.CHINA);
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM", Locale.CHINA);
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd", Locale.CHINA);
        Date currentDate = new Date(System.currentTimeMillis());
        String mYear = yearFormatter.format(currentDate);
        String mMM = monthFormatter.format(currentDate);
        String mdd = dayFormatter.format(currentDate);
        mAnInt = Integer.parseInt(mYear);
        showDateDialog(DateUtil.getDateForString((mAnInt - 18) + "-" + mMM + "-" + mdd), textView);
    }

    private void showDateDialog(List<Integer> date, final TextView textView) {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                String text = dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                        + (dates[2] > 9 ? dates[2] : ("0" + dates[2]));
                textView.setText(text);
                mBirthday = text;
                mAge = mAnInt - dates[0];
                LogUtil.d("mAge---" + mAge);
            }

            @Override
            public void onCancel() {
                LogUtil.d("日历选择--");
            }
        }).setSelectYear(date.get(0) - 1).setSelectMonth(date.get(1) - 1).setSelectDay(date.get(2) - 1);
        builder.setMaxYear(DateUtil.getYear());
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        builder.create().show();
    }

    private void selectManOrWoMen(final TextView textView) {
        final Popwindou window = new Popwindou(mActivity, mRootView);
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.select_man_or_woman, null);
        window.init(view, Gravity.BOTTOM, true);
        view.findViewById(R.id.item_popupwindows_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSex = "1";
                textView.setText("男");
                window.dismiss();
            }
        });
        view.findViewById(R.id.item_popupwindows_woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSex = "2";
                textView.setText("女");
                window.dismiss();
            }
        });
        view.findViewById(R.id.item_popupwindows_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSex = "0";
                textView.setText(null);
                window.dismiss();
            }
        });
    }

    private void confirmToCompleteInfo() {
        if (mCroppedImageUri == null || TextUtils.isEmpty(mCroppedImageUri.getPath())) {
            Toasty.normal(mActivity, "请上传头像").show();
            return;
        }
        mNickName = mEtNickname.getText().toString().trim();
        int charSequenceCount = countChineseChar(mNickName);
        if (mNickName.length() + charSequenceCount < 3                  //最少3个字符
                || mNickName.length() + charSequenceCount > 16) {       //最多16个字符
            Toasty.normal(mActivity, "输入文字长度不符合要求").show();
            return;
        }
        if ("0".equals(mSex)) {
            Toasty.normal(mActivity, "您尚未设置性别").show();
            return;
        }
        if (TextUtils.isEmpty(mBirthday)) {
            Toasty.normal(mActivity, "您尚未设置生日").show();
            return;
        }
        if (mAge < 18) {
            Toasty.normal(mActivity, "年龄不够18岁").show();
            return;
        }

        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("性别、年龄一经注册不可修改")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        addUserInfo();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //完善信息
    private void addUserInfo() {
        mDialog.show();
        File file = new File(mCroppedImageUri.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RetrofitUtil.createHttpsService(LoginService.class)
                .addUserInfoNew(mUserId, mSex, mBirthday, mNickName, mAge, part,
                        Conversion.getRandomappA(), PeiNiApp.getUniquePsuedoID(), "1")
                .enqueue(new RetrofitCallback<LoginSuccess>() {
                    @Override
                    public void onSuccess(Call<LoginSuccess> call, Response<LoginSuccess> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            LoginSuccess loginSuccess = response.body();
                            SpUtils.putServerB(mActivity, loginSuccess.getServerB());
                            if (loginSuccess.getResultCode() == 1) {
                                saveUserInfoAndJump(loginSuccess);
                            } else if (loginSuccess.getResultCode() == 0) {
                                Toasty.normal(mActivity, loginSuccess.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginSuccess> call, Throwable t) {
                        mDialog.dismiss();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    private void saveUserInfoAndJump(LoginSuccess response) {
        if (response.getAddUserInfo() == 1) {
            SpUtils.put(mActivity, "nickname", response.getUserInfo().getNickname() + "");
            SpUtils.put(mActivity, "imageHead", response.getUserInfo().getImageHead() + "");
            SpUtils.put(mActivity, "sex", response.getUserInfo().getSex() + "");
        }
        SpUtils.put(mActivity, "mUserToken", String.valueOf(response.getUserToken()));
        SpUtils.put(mActivity, "id", response.getUserInfo().getId() + "");
        SpUtils.put(mActivity, "userLoginId", response.getUserInfo().getUserLoginId() + "");
        SpUtils.put(mActivity, "phone", response.getUserInfo().getUserPhone() + "");
        SpUtils.put(mActivity, "addUserInfo", response.getAddUserInfo() + "");

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isNonShowGestureLock = true;
    }

    private void showPopSelector() {
        final BottomSheetDialog mDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_photograph, null);
        view.findViewById(R.id.item_popupwindows_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//选择图片
                selectPhoto();
                mDialog.dismiss();
            }
        });
        view.findViewById(R.id.item_popupwindows_Photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//拍照
                photoGraph();
                mDialog.dismiss();
            }
        });
        view.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(view);
        mDialog.show();
    }

    /**
     * 选择照片
     */
    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        isNonShowGestureLock = false;
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    /**
     * 拍照
     */
    public void photoGraph() {
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
            Intent intent = new Intent(mActivity, UseCameraActivity.class);
            startActivityForResult(intent, RESULT_LOAD_IMAGE2);
            isNonShowGestureLock = false;
        } else {
            Toasty.normal(mActivity, "sdcard不可用").show();
        }
    }

    private boolean isNonShowGestureLock = true;

    @Override
    public boolean isGestureLock() {
        return isNonShowGestureLock;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            crop(selectedImage, false);
        }
        if (requestCode == RESULT_LOAD_IMAGE2 && null != data) {
            String extra = data.getStringExtra(UseCameraActivity.IMAGE_PATH);
            String correction = Conversion.correctImage(extra);
            startPhotoZoom(new File(correction));
        }
        if (requestCode == PHOTO_REQUEST_CUT && resultCode == RESULT_OK && data != null) {
            Glide.with(mActivity).load(mCroppedImageUri)
                    .transform(new RoundCornerTransformation(mActivity))
                    .into(mIvRealAvatar);
        }
    }

    /*
    * 剪切图片 相册选择
    */
    private void crop(Uri uri, boolean isAfterCapture) {
        if (uri == null) {
            LogUtil.i("alanjet", "The uri is not exist.");
        }
        LogUtil.d("tempUri" + uri);

        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        Uri imageUri;
        if (isAfterCapture && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && uri != null) {
            File file = new File(uri.getPath());
            imageUri = FileProvider.getUriForFile(mActivity, "com.jsz.peini.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = uri;
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 800);
        intent.putExtra("aspectY", 800);
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        mCroppedImageUri = Uri.fromFile(new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "crop_" + System.currentTimeMillis() + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file) {
        LogUtil.i("TAG", getImageContentUri(this, file) + "裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(this, file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 800);
            intent.putExtra("aspectY", 800);
            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 800);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", false);
            mCroppedImageUri = Uri.fromFile(new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "crop_" + System.currentTimeMillis() + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImageUri);//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 计算中文字符
     *
     * @param sequence
     * @return
     */
    private int countChineseChar(CharSequence sequence) {

        if (TextUtils.isEmpty(sequence)) {
            return 0;
        }
        int charNum = 0;
        for (int i = 0; i < sequence.length(); i++) {
            char word = sequence.charAt(i);
            if (isChineseChar(word)) {//中文
                charNum++;
            }
        }
        return charNum;
    }

    /**
     * 判断是否是中文
     *
     * @param c
     * @return
     */
    public static boolean isChineseChar(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
}