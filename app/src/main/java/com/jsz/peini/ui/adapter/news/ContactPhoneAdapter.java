package com.jsz.peini.ui.adapter.news;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.news.UserContactBean;


public class ContactPhoneAdapter extends BaseQuickAdapter<UserContactBean, BaseViewHolder> {


    public ContactPhoneAdapter() {
        super(R.layout.item_contact_phone);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserContactBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_phone, item.getPhone());
        helper.setText(R.id.tv_foller_invite, item.getIsFollerOrInvite() == 0 ? "邀请" : "关注");
        helper.setImageBitmap(R.id.iv_contact_image, item.getBitmap());
    }
}
