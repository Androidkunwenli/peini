package com.jsz.peini.ui.activity.square;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.utils.RetrofitUtil;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by th on 2017/2/7.
 */
public class NickNameActivity extends BaseActivity {

    private static final String EXTRA_TYPE_FLAG = "extra_type_flag";
    private static final String EXTRA_STRING_VALUE_FLAG = "extra_string_value_flag";

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.tv_update_tips)
    TextView mTvUpdateTips;
    @InjectView(R.id.nickname)
    EditText mNickname;
    @InjectView(R.id.delete_nickname)
    ImageView mDeleteNickname;

    private NickNameActivity mActivity;
    private int mType;
    private String mExtraValue;
    private int MIN_CHAR_NUM; //最少字符长度
    private int MAX_CHAR_NUM; //最大字符长度

    @Override
    public int initLayoutId() {
        return R.layout.activity_nickname;
    }

    /**
     * 跳转显示方法
     *
     * @param context    context
     * @param type       1.签名 2.昵称
     * @param extraValue 原来的值
     */
    public static void actionShow(Activity context, int type, String extraValue) {
        Intent intent = new Intent(context, NickNameActivity.class);
        intent.putExtra(EXTRA_TYPE_FLAG, type);
        intent.putExtra(EXTRA_STRING_VALUE_FLAG, extraValue);
        if (type == 1) {
            context.startActivity(intent);
        } else if (type == 2) {
            context.startActivityForResult(intent, 1);
        }
    }

    @Override
    public void initView() {
        mActivity = this;
        mRightButton.setText("完成");

        mType = getIntent().getIntExtra(EXTRA_TYPE_FLAG, -1);
        mExtraValue = getIntent().getStringExtra(EXTRA_STRING_VALUE_FLAG);
        String title = null;
        String updateTips = null;
        if (mType == 1) {
            title = "修改签名";
            MIN_CHAR_NUM = 4;
            MAX_CHAR_NUM = 46;
            updateTips = "签名长度为2~23个汉字,或4~46个字符";
            mNickname.setHint("请输入签名");
        } else if (mType == 2) {
            title = "修改昵称";
            MIN_CHAR_NUM = 3;
            MAX_CHAR_NUM = 16;
            updateTips = "昵称长度为2~8个汉字,或3~16个字符";
            mNickname.setHint("请输入昵称");
        }
        mTitle.setText(title);
        mTvUpdateTips.setText(updateTips);
        mNickname.setText(mExtraValue);
    }

    @Override
    public void initData() {
    }

    private void initBNetWork(String result) {
        mDialog.show();
        RetrofitUtil.createService(SquareService.class)
                .updateUserSignWord(mUserToken, result)
                .enqueue(new RetrofitCallback<SuccessfulBean>() {
                    @Override
                    public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                        mDialog.dismiss();
                    }
                });
    }

    @OnClick({R.id.right_button, R.id.delete_nickname, R.id.toolbar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_button:
                String result = mNickname.getText().toString().trim();

                int charSequenceCount = countChineseChar(result);
                if (result.length() + charSequenceCount < MIN_CHAR_NUM
                        || result.length() + charSequenceCount > MAX_CHAR_NUM) {
                    Toasty.normal(mActivity, "输入文字长度不符合要求").show();
                    return;
                }

                if (mType == 1) {
                    initBNetWork(result);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("name", result);
                    setResult(0, intent);
                    finish();
                }

                break;
            case R.id.delete_nickname:
                mNickname.setText(null);
                break;
            case R.id.toolbar:
                finish();
                break;
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
