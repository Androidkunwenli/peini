package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.square.SquareBean;
import com.jsz.peini.model.square.SquareNewData;
import com.jsz.peini.presenter.ad.Ad;

/**
 * Created by lenovo on 2017/6/10.
 */

public class SquareNewAdapter extends BaseQuickAdapter<SquareNewData.SquareNewList, BaseViewHolder> {

    private Activity mActivity;

    public SquareNewAdapter(Activity activity) {
        super(R.layout.item_square_new_entry);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, SquareNewData.SquareNewList item) {

    }
}
