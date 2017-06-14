package com.jsz.peini.ui.activity.setting;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.ui.activity.login.ServiceActivity;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.PeiNiUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by kunwe on 2016/12/6.
 */
public class AboutUsActivity extends BaseActivity {
    @InjectView(R.id.aboutus_e_mail)
    TextView aboutusEMail;
    @InjectView(R.id.aboutus_peini)
    TextView aboutusPeini;
    @InjectView(R.id.aboutus_weixin)
    TextView aboutusWeixin;
    @InjectView(R.id.aboutus_pingjia)
    TextView aboutusPingjia;
    @InjectView(R.id.aboutus_ideas)
    TextView aboutusIdeas;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.tv_version_name)
    TextView mTvVersionName;
    private Popwindou mPopwindou;
    private AboutUsActivity mActivity;
    private Intent mIntent;
    private int requesCodes = 1;

    @Override
    public int initLayoutId() {
        return R.layout.activity_aboutus;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("关于我们");
        mTvVersionName.setText("软件版本 : " + String.valueOf(PeiNiUtils.getVersionName(mActivity)));
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.aboutus_e_mail, R.id.aboutus_peini, R.id.aboutus_weixin, R.id.aboutus_pingjia, R.id.aboutus_ideas, R.id.aboutus_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutus_e_mail: //邮箱
                popShowViewWeChat(1);
                break;
            case R.id.aboutus_peini: //官网
                popShowViewWeChat(2);
//                http://www.91peini.com
//                mIntent = new Intent(mActivity, KunShareActivity.class);
//                startActivity(mIntent);
                break;
            case R.id.aboutus_weixin: //微信
                popShowViewWeChat(3);
                break;
            case R.id.aboutus_pingjia: //评价
                if ("samsung".equals(Build.BRAND)) {
                    Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + getPackageName());
                    Intent goToMarket = new Intent();
                    goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
                    goToMarket.setData(uri);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toasty.normal(mActivity, "您的系统中没有安装应用市场").show();
                    }
                } else {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toasty.normal(mActivity, "您的系统中没有安装应用市场").show();
                    }
                }

                break;
            case R.id.aboutus_call: //陪你客服
                popShowViewWeChat(4);
                break;
            case R.id.aboutus_ideas: //反馈
                mIntent = new Intent(mActivity, ServiceActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    private void popShowViewWeChat(final int i) {
        mPopwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_aboutus));
        View view = UiUtils.inflate(mActivity, R.layout.pop_wechat);
        mPopwindou.init(view, Gravity.BOTTOM, true);
        TextView mTvPeiniId = (TextView) view.findViewById(R.id.tv_peini_id);
        TextView tv_wechat = (TextView) view.findViewById(R.id.tv_wechat);
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        switch (i) {
            case 1:
                mTvPeiniId.setText("官方邮箱: marketing@91peini.com");
                cm.setText("marketing@91peini.com");
                break;
            case 2:
                mTvPeiniId.setText("官方网站: http://www.91peini.com");
                cm.setText("http://www.91peini.com");
                break;
            case 3:
                mTvPeiniId.setText("官方微信: peini-app");
                cm.setText("peini-app");
                break;
            case 4:
                mTvPeiniId.setText("400-612-6226");
                tv_wechat.setText("拨打客服电话");
                break;
        }
        tv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i) {
                    case 1:
                        Toasty.success(mActivity, "复制成功  marketing@91peini.com").show();

                        break;
                    case 2:
                        Toasty.success(mActivity, "复制成功  http://www.91peini.com").show();

                        break;
                    case 3:
                        Toasty.success(mActivity, "复制成功  peini-app").show();
                        break;
                    case 4:
                        isCall();
                        break;
                }
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopwindou.dismiss();
            }
        });
    }

    /**
     * 打电话的方法  动态权限
     */
    private void isCall() {
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //权限未获得
            //用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                isCall(); //重新请求一次
            } else {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, requesCodes);
            }
        } else {
            //权限已获得
            //拨打电话
            final String sellerPhone = "4006126226";
            if (StringUtils.isNull(sellerPhone)) {
                Toasty.normal(mActivity, "没有电话号码!").show();
                return;
            }
            new AlertDialog.Builder(mActivity)
                    .setMessage(sellerPhone)
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + sellerPhone);
                            intent.setData(data);
                            startActivity(intent);
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
    }

    //打电话的动态权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == requesCodes) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isCall();
            } else {
                // Permission Denied
                Toast.makeText(mActivity, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
