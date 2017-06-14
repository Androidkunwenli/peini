package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsz.peini.R;

import java.util.List;

/**
 * Created by th on 2017/2/8.
 */
public class KunSquareAdapter extends RecyclerView.Adapter {
    public int ITEM_TYPE_HEADER = 0;
    public int ITEM_TYPE_HEADER_TWO = 1;
    public final Activity mActivity;
    public final List<String> mList;

    public KunSquareAdapter(Activity activity, List<String> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new ViewHolderHead(LayoutInflater.from(mActivity).inflate(R.layout.item_head_square, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_square, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_HEADER_TWO;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolderHead extends RecyclerView.ViewHolder {
        public ViewHolderHead(View view) {
            super(view);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}
