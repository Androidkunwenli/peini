package com.jsz.peini.san.huanxin.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.model.EaseGifEmojiconData;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.eventbus.ReceivedMessageBean;
import com.jsz.peini.model.eventbus.UnreadHuanXinMsgCountBean;
import com.jsz.peini.model.setting.UserSmsCntBean;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.san.huanxin.activity.ChatHuanXinActivity;
import com.jsz.peini.ui.activity.news.SecretaryActivity;
import com.jsz.peini.ui.activity.news.SystemMessageActivity;
import com.jsz.peini.utils.EaseUtils;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 15089 on 2017/2/18.
 */
public class HuanXinFragment extends EaseConversationListFragment {

    private EMConversation mConversation;

    @Override
    protected void setUpView() {
        super.setUpView();
        //注册事件
        EventBus.getDefault().register(this);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mConversation = conversationListView.getItem(position);
                ChatHuanXinActivity.actionShow(getActivity(), mConversation.conversationId());
            }
        });
        conversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LogUtil.d("删除" + position);
                new AlertDialog.Builder(getActivity())
                        .setMessage("删除列表及记录")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(position);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });

        mLlSecretary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("陪你小助手");
                startActivity(new Intent(getContext(), SecretaryActivity.class));
                setSecretaryMessageNumber(0);
            }
        });
        mLlSystemMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SystemMessageActivity.class));
                setSystemMessageNumber(0);
            }
        });
        String phoneStr = (String) SpUtils.get(getContext(), "phone", "");
        EaseUtils.setUserInfoProvider(getActivity(), phoneStr, conversationListView);

        EaseUI.getInstance().setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data = EaseGifEmojiconData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if (emojicon.getIdentityCode().equals(emojiconIdentityCode)) {
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });
    }

    /**
     * 删除聊天列表记录
     */
    private void deleteItem(int position) {
        mConversation = conversationListView.getItem(position);
        String from = mConversation.conversationId();
        conversationList.remove(position);
        conversationListView.refresh();
        //删除和某个user会话，如果需要保留聊天记录，传false
        EMClient.getInstance().chatManager().deleteConversation(from, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMessageNumber(1);
        refreshMessageNumber(2);
    }

    /**
     * 刷新消息数量
     *
     * @param type 1、系统消息；2、小秘书
     */
    private void refreshMessageNumber(int type) {
        final int messageType = type;
        RetrofitUtil.createService(SettingService.class)
                .getUserSmsCnt(SpUtils.getUserToken(getActivity()), type)
                .enqueue(new RetrofitCallback<UserSmsCntBean>() {
                    @Override
                    public void onSuccess(Call<UserSmsCntBean> call, Response<UserSmsCntBean> response) {
                        if (response.isSuccessful()) {
                            UserSmsCntBean body = response.body();
                            if (body.getCode() == 1) {
                                if (messageType == 1) {
                                    setSystemMessageNumber(body.getNewMsg());
                                } else if (messageType == 2) {
                                    setSecretaryMessageNumber(body.getNewMsg());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserSmsCntBean> call, Throwable t) {
                    }
                });
    }

    /**
     * 环信消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(UnreadHuanXinMsgCountBean msgCountBean) {
        refresh();
    }

    /**
     * 系统消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(ReceivedMessageBean ReceivedMessageBean) {
        refreshMessageNumber(1);
        refreshMessageNumber(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }
}
