package com.jsz.peini.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jsz.peini.PeiNiApp;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by 15089 on 2017/2/15.
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private WXPayEntryActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
//        String data = getIntent().getStringExtra("_wxapi_payresp_extdata");

        IWXAPI wxapi = WXAPIFactory.createWXAPI(mActivity, PeiNiApp.WXAPIPAY);
        wxapi.handleIntent(getIntent(), mActivity);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtil.d("支付回调" + baseResp.errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent(Conversion.WX_RESULT);
            intent.putExtra(Conversion.WX_RESULT_CODE_FLAG, baseResp.errCode);
            intent.putExtra(Conversion.WX_RESULT_EXT_DATA_FLAG, ((PayResp)baseResp).extData);
            sendBroadcast(intent);
        }
        finish();
    }
}
