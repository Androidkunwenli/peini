package com.jsz.peini.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.ui.activity.home.HomeActivity;

import butterknife.InjectView;
import butterknife.OnClick;

public class GuidanceActivity extends BaseNotSlideActivity implements NonGestureLockInterface {
    @InjectView(R.id.iv_guidance_image)
    ImageView mImageView;

    private int mCurrentIndex = 0;

    private int[] mImageArray = {R.mipmap.guidance1, R.mipmap.guidance2, R.mipmap.guidance3, R.mipmap.guidance4};

    private Context mContext;

    @Override
    public int initLayoutId() {
        return R.layout.activity_guidance;
    }

    @Override
    public void initView() {
        mContext = this;
    }

    @Override
    public void initData() {
        Glide.with(mContext).load(mImageArray[mCurrentIndex]).into(mImageView);
    }

    @Override
    public void showAllVisual(boolean b) {
        super.showAllVisual(true);
    }

    @OnClick({R.id.iv_guidance_image, R.id.tv_jump_over})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_guidance_image:
                showNextImage();
                break;
            case R.id.tv_jump_over:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void showNextImage() {
        if (mCurrentIndex + 1 < mImageArray.length) {
            mCurrentIndex++;
            mImageView.setImageResource(mImageArray[mCurrentIndex]);
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean isGestureLock() {
        return false;
    }
}