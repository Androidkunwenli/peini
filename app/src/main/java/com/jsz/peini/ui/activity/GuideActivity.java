package com.jsz.peini.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.ui.activity.login.IsLoginActivity;
import com.jsz.peini.utils.CacheActivity;
import com.jsz.peini.utils.Conversion;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.DynamicPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends BaseNotSlideActivity implements NonGestureLockInterface {
    @InjectView(R.id.tv_jump_in)
    TextView tvJumpIn;
    private RollPagerView guide_vp;
    ArrayList<Integer> mArrayList = new ArrayList<>();

    @Override
    public int initLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void showAllVisual(boolean b) {
        super.showAllVisual(true);
    }

    @Override
    public void initData() {


        guide_vp = (RollPagerView) findViewById(R.id.guide_vp);
        guide_vp.setHintView(new ColorPointHintView(this, Conversion.FB4E30, Color.WHITE));
        mArrayList.add(R.mipmap.guide_1);
        mArrayList.add(R.mipmap.guide_2);
        mArrayList.add(R.mipmap.guide_3);
        mArrayList.add(R.mipmap.guide_4);

        guide_vp.setAdapter(new TestNomalAdapter());

        guide_vp.getViewPager().setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mArrayList.size()-1) {
                    tvJumpIn.setText("进入");
                } else {
                    tvJumpIn.setText("跳过");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.tv_jump_in)
    public void onClick() {
        Intent intent = new Intent(GuideActivity.this, IsLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean isGestureLock() {
        return false;
    }

    private class TestNomalAdapter extends DynamicPagerAdapter {
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(mArrayList.get(position));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }
    }
}