package com.jsz.peini.san.huanxin.fragment;

import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.model.EaseCustomEmojiconDatas;
import com.hyphenate.easeui.model.EaseGifEmojiconData;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.jsz.peini.R;
import com.jsz.peini.utils.LogUtil;

import java.util.Arrays;

/**
 * Created by 15089 on 2017/2/20.
 */

public class ChatFragment extends EaseChatFragment {

    @Override
    protected void setUpView() {
        super.setUpView();

        initEmojiconData();
        //设置item里的控件的点击事件
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                LogUtil.d("点击事件--头像 " + username);
                if (mAvatarClickListener != null) {
                    mAvatarClickListener.onUserAvatarClick(username);
                }
            }

            @Override
            public void onUserAvatarLongClick(String username) {

            }

            @Override
            public void onResendClick(final EMMessage message) {
                //重发消息按钮点击事件
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                //气泡框长按事件
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                //气泡框点击事件，EaseUI有默认实现这个事件，如果需要覆盖，return值要返回true
                return false;
            }
        });
    }

    private void initEmojiconData() {
        EaseEmojicon[] emojiconsCustom = EaseCustomEmojiconDatas.getData();
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity(R.mipmap.icronin, Arrays.asList(emojiconsCustom));
//        List<EaseEmojiconGroupEntity> list = new ArrayList<>();
//        list.add(emojiconGroupEntity);
//        inputMenu.init(list);
        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(emojiconGroupEntity);
        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EaseGifEmojiconData.getData());
    }

    public void setInputEnable(boolean enable) {
        if (enable) {
            mTvMask.setVisibility(View.GONE);
        } else {
            inputMenu.clearFocus();
            mTvMask.requestFocus();
            mTvMask.setVisibility(View.VISIBLE);
        }
    }

    public void setAvatarClickListener(AvatarClickListener listener) {
        this.mAvatarClickListener = listener;
    }

    private AvatarClickListener mAvatarClickListener = null;

    public interface AvatarClickListener {
        void onUserAvatarClick(String username);
    }
}
