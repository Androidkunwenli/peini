package com.jsz.peini.ui.view;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.listener.OnPasswordInputFinish;
import com.jsz.peini.ui.activity.setting.PayPasswordActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Belong to the Project —— MyPayUI Created by WangJ on 2015/11/25 15:39.
 */
public class PasswordView extends RelativeLayout implements
        View.OnClickListener {
    Context context;
    public String strPassword; // 输入的密码
    public TextView[] tvList; // 用数组保存6个TextView，为什么用数组？
    // 因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）
    private GridView gridView; // 用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能
    public ArrayList<Map<String, String>> valueList; // 有人可能有疑问，为何这里不用数组了？
    // 因为要用Adapter中适配，用数组不能往adapter中填充

    private ImageView imgCancel;
    private TextView tvForget;
    public int currentIndex = -1; // 用于记录当前输入密码格位置

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.layout_popup_bottom, null);

        valueList = new ArrayList<Map<String, String>>();
        tvList = new TextView[6];

        imgCancel = (ImageView) view.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(this);

        tvForget = (TextView) view.findViewById(R.id.tv_forgetPwd);
        tvForget.setOnClickListener(this);

        tvList[0] = (TextView) view.findViewById(R.id.tv_pass1);
        tvList[1] = (TextView) view.findViewById(R.id.tv_pass2);
        tvList[2] = (TextView) view.findViewById(R.id.tv_pass3);
        tvList[3] = (TextView) view.findViewById(R.id.tv_pass4);
        tvList[4] = (TextView) view.findViewById(R.id.tv_pass5);
        tvList[5] = (TextView) view.findViewById(R.id.tv_pass6);

        gridView = (GridView) view.findViewById(R.id.gv_keybord);

        setView();

        addView(view); // 必须要，不然不显示控件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
//                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_forgetPwd:
                context.startActivity(new Intent(context, PayPasswordActivity.class));
//                Toast.makeText(context, "Forget", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setView() {
        /* 初始化按钮上应该显示的数字 */
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<String, String>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", "");
            } else if (i == 12) {
                map.put("name", "<<");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            }
            valueList.add(map);
        }

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < 11 && position != 9) { // 点击0~9按钮
                    if (currentIndex >= -1 && currentIndex < 5) { // 判断输入位置————要小心数组越界
                        tvList[++currentIndex].setText(valueList.get(position)
                                .get("name"));
                    }
                } else {
                    if (position == 11) { // 点击退格键
                        if (currentIndex - 1 >= -1) { // 判断是否删除完毕————要小心数组越界
                            tvList[currentIndex--].setText("");
                        }
                    }
                }
            }
        });
    }

    // 设置监听方法，在第6位输入完成后触发
    public void setOnFinishInput(final OnPasswordInputFinish pass) {
        tvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {
                    strPassword = ""; // 每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱
                    for (int i = 0; i < 6; i++) {
                        strPassword += tvList[i].getText().toString().trim();
                    }
                    pass.inputFinish(); // 接口中要实现的方法，完成密码输入完成后的响应逻辑
                }
            }
        });
    }

    /* 获取输入的密码 */
    public String getStrPassword() {
        return strPassword;
    }

    /* 暴露取消支付的按钮，可以灵活改变响应 */
    public ImageView getCancelImageView() {
        return imgCancel;
    }

    /* 暴露忘记密码的按钮，可以灵活改变响应 */
    public TextView getForgetTextView() {
        return tvForget;
    }

    // GrideView的适配器
    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return valueList.size();
        }

        @Override
        public Object getItem(int position) {
            return valueList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_gride, null);
                viewHolder = new ViewHolder();
                viewHolder.btnKey = (TextView) convertView
                        .findViewById(R.id.btn_keys);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.btnKey.setText(valueList.get(position).get("name"));
            if (position == 9) {
                viewHolder.btnKey
                        .setBackgroundResource(R.drawable.selector_key_del);
                viewHolder.btnKey.setEnabled(false);
            }
            if (position == 11) {
                viewHolder.btnKey
                        .setBackgroundResource(R.drawable.selector_key_del);
            }

            return convertView;
        }
    };

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView btnKey;
    }
}
