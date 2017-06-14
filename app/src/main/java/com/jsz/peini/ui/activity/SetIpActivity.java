package com.jsz.peini.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jsz.peini.R;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.utils.RetrofitUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SetIpActivity extends AppCompatActivity implements NonGestureLockInterface {

    @InjectView(R.id.et_ip)
    EditText etIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ip);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_confirm, R.id.btn_default_ip_remote, R.id.btn_default_ip_local,
            R.id.btn_default_ip_local1, R.id.btn_default_ip_local2, R.id.btn_default_ip_local3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:

                String ip = etIp.getText().toString().trim();
                if (!TextUtils.isEmpty(ip)) {
                    IpConfig.HttpPeiniIp = ip;
                    RetrofitUtil.isUpdateIp = true;
                    SetIpActivity.this.finish();
                }

                break;
            case R.id.btn_default_ip_remote:

                etIp.setText(IpConfig.REMOTE_IP);
                break;
            case R.id.btn_default_ip_local:

                etIp.setText(IpConfig.LOCAL_IP);
                break;
            case R.id.btn_default_ip_local1:

                etIp.setText("http://192.168.150.182:8080/pnServer/");
                break;
            case R.id.btn_default_ip_local2:

                etIp.setText("http://192.168.150.254:8480/pnservice/");
                break;
            case R.id.btn_default_ip_local3:

                etIp.setText("http://192.168.1.109:8480/pnservice/");
                break;
        }
    }

    @Override
    public boolean isGestureLock() {
        return false;
    }
}
