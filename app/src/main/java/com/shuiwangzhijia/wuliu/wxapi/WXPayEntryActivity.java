package com.shuiwangzhijia.wuliu.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.shuiwangzhijia.wuliu.activitydriver.DriverNewOrderActivity;
import com.shuiwangzhijia.wuliu.base.App;
import com.shuiwangzhijia.wuliu.event.WechatPayResultEvent;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import de.greenrobot.event.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = App.getIwxapi();
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        KLog.e(baseReq.getType());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            KLog.e("baseResp.errCode:"+baseResp.errCode);
            if(baseResp.errCode==0){
                //支付成功
                EventBus.getDefault().post(new WechatPayResultEvent(1));

                startActivity(new Intent(this, DriverNewOrderActivity.class));
              //  EventBus.getDefault().post(new CashDeliveryEvent());
             /*   Intent intent = new Intent(WXPayEntryActivity.this, PaySuccessActivity.class);
                startActivity(intent);*/
//                MyOrderActivity.statAct(this, 2);
            }else if (baseResp.errCode == -1){
                //支付失败
                EventBus.getDefault().post(new WechatPayResultEvent(0));
                ToastUitl.showToastCustom("支付失败");
            }else if (baseResp.errCode == -2){
                //支付取消
                EventBus.getDefault().post(new WechatPayResultEvent(2));
               // MyOrderActivity.statAct(this, 1);
                ToastUitl.showToastCustom("您已取消支付");
            }
        }
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
