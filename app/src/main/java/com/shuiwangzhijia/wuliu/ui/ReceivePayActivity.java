package com.shuiwangzhijia.wuliu.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.App;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.base.Constant;
import com.shuiwangzhijia.wuliu.bean.AppendOrderBean;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.bean.PayBean;
import com.shuiwangzhijia.wuliu.bean.PayOrderDetailBean;
import com.shuiwangzhijia.wuliu.bean.PreparePayBean;
import com.shuiwangzhijia.wuliu.event.CashDeliveryEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.shuiwangzhijia.wuliu.view.BaseBar;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.activity_receive_pay,title = "收款")
public class ReceivePayActivity extends BaseActivity {
    private static final String TAG = "ReceivePayActivity";
    private static final int TIME_TASK = 0x01;
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.orderDate)
    TextView orderDate;
    @BindView(R.id.detailBtn)
    TextView detailBtn;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.qRCode)
    ImageView qRCode;
    @BindView(R.id.payBySelf)
    TextView payBySelf;
    private PreparePayBean data;
    private int typePay;
    private int fromType;       //2 代表代下单的二维码
    private Timer mTimer;
    private TimerTask mTimerTask;
    private PayBean mSubmitOrderWxData;

    /**
     * @param context
     * @param data
     * @param fromType //1 赊账中收款 2.代下单--生成订单  3.追加订单  5.桶的管理 6.杂桶去支付
     */
    public static void startAtc(Context context, PreparePayBean data, int fromType) {
        Intent intent = new Intent(context, ReceivePayActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("fromType", fromType);
        context.startActivity(intent);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_TASK: {
                    getPayResult();
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        mBaseBar.setBarListener(new BaseBar.BarListener() {
            @Override
            public void onLeftClick() {
                closeActivity();
            }

            @Override
            public void onRightClick() {

            }
        });
        data = (PreparePayBean) getIntent().getSerializableExtra("data");
        fromType = getIntent().getIntExtra("fromType", -1);
        typePay = data.getPayFrom();
        detailBtn.setVisibility(fromType > 3 ? View.GONE : View.VISIBLE);
        switch (fromType) {
            case 1:
                //从新订单-->赊账中 firstBtn跳转
                getDetailInfo();
                break;
            case 2:
                //确认订单  创建订单
                getPayDetailInfo();
                break;
            case 3:
                //确认订单  追加订单
                appendOrderDetail();
                break;
            case 4:
                shopName.setText(data.getSname());
                orderId.setText("订单号:" + data.getOrder_no());
                orderDate.setText("下单时间:" + DateUtils.getFormatDateStr(Long.parseLong(data.getCreate_time())*1000L));
                money.setText("￥" + data.getTamount());
                break;
            case 5:
                //换桶 压桶 退桶 页面跳转
                shopName.setText("收款方：" + data.getDname());
                orderId.setText("付款方:" + data.getSname());
                orderDate.setText("支付说明:" + data.getPolicy());
                money.setText("￥" + data.getTamount());
                break;
            case 6:
                //杂桶回收确认操作页面
                shopName.setText("收款方： " + data.getPay());
                orderId.setText("付款方： " + data.getSail());
                orderDate.setText("支付说明: " + data.getPay_type());
                money.setText("￥" + data.getPrice());
                break;
        }
    }

    @Override
    protected void initData() {
        getData();
        updateShopCartInfo();
        getPayStatus();
    }

    @Override
    protected void initEvent() {

    }

    private void getPayResult() {
        RetrofitUtils.getInstances().create().getPayStatus(data.getOrder_no(), typePay).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                KLog.e(mGson.toJson(body));
                if (body != null){
                    if (body.getCode() == 200){
                        mTimer.cancel();
                        hint("支付成功");
                        if (fromType == 6){
                            //从支付杂桶跳转过来的
                            Intent intent = new Intent(ReceivePayActivity.this,OperationOrderActivityNew.class);
                            intent.putExtra("type",1);
                            intent.putExtra("orderData",data.getOrder_sn());
                            startActivity(intent);
                        }else {
//                            EventBus.getDefault().post(new PayFinishEvent());
//                            EventBus.getDefault().post(new MainEvent(3));
                            startActivity(new Intent(ReceivePayActivity.this, MainActivity.class));
                            EventBus.getDefault().post(new CashDeliveryEvent());
                        }
                        finish();
                    }else {
//                        hint("请重新支付");
                        if(payBySelf==null)
                            return;
                        payBySelf.setClickable(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });
    }

    private void getPayStatus() {
        mTimer = new Timer();
        mTimer.schedule(new Test1(), 0, 1000);
    }

    class Test1 extends TimerTask{
        int count = 120;
        @Override
        public void run() {
            if (count > 0){
                Message message = new Message();
                message.what = TIME_TASK;
                handler.sendEmptyMessage(TIME_TASK);
                count --;
            }else {
                this.cancel();
            }
        }
    }

    private void updateShopCartInfo() {
        List<GoodsBean> shopCart = CommonUtils.getShopCart();
        List<GoodsBean> newShopCart = new ArrayList<>();
        List<GoodsBean> buyGoodsList = CommonUtils.getBuyGoodsList();
        if (shopCart != null && buyGoodsList != null) {
            GoodsBean buy, item = null;
            for (int j = 0; j < shopCart.size(); j++) {
                buy = shopCart.get(j);
                boolean remove = false;
                for (int i = 0; i < buyGoodsList.size(); i++) {
                    item = buyGoodsList.get(i);
                    if (buy.getGid().equals(item.getGid())) {
                        remove = true;
                    }
                }
                if (!remove) {
                    newShopCart.add(buy);
                }

            }
            CommonUtils.saveBuyGoodsList(null);
            CommonUtils.saveShopCart(newShopCart);
        }

    }

    private void appendOrderDetail() {
        RetrofitUtils.getInstances().create().appendOrderDetail(data.getOrder_no()).enqueue(new Callback<EntityObject<AppendOrderBean>>() {
            @Override
            public void onResponse(Call<EntityObject<AppendOrderBean>> call, Response<EntityObject<AppendOrderBean>> response) {
                EntityObject<AppendOrderBean> body = response.body();
                if (body.getCode() == 200) {
                    AppendOrderBean orderBean = body.getResult();
                    if (orderBean == null) {
                        return;
                    }
                    shopName.setText(orderBean.getSname());
                    orderId.setText("订单号:" + orderBean.getOrder_sns());
                    orderDate.setText("下单时间:" + DateUtils.getFormatDateStr(orderBean.getUpdate_time() * 1000));
                    money.setText("￥" + orderBean.getTotal_price());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<AppendOrderBean>> call, Throwable t) {

            }
        });
    }

    private void getDetailInfo() {
        RetrofitUtils.getInstances().create().getOrderDetail(data.getOrder_no()).enqueue(new Callback<EntityObject<OrderBean>>() {
            @Override
            public void onResponse(Call<EntityObject<OrderBean>> call, Response<EntityObject<OrderBean>> response) {
                EntityObject<OrderBean> body = response.body();
                if (body.getCode() == 200) {
                    OrderBean orderBean = body.getResult();
                    if (orderBean == null) {
                        return;
                    }
                    shopName.setText(orderBean.getSname());
                    orderId.setText("订单号:" + orderBean.getOrder_sn());
                    orderDate.setText("下单时间:" + DateUtils.getFormatDateStr(orderBean.getCreate_time() * 1000));
                    money.setText("￥" + orderBean.getTotal_price());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<OrderBean>> call, Throwable t) {

            }
        });
    }

    private void getPayDetailInfo() {
        RetrofitUtils.getInstances().create().getPayOrderDetail(data.getOrder_no()).enqueue(new Callback<EntityObject<PayOrderDetailBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PayOrderDetailBean>> call, Response<EntityObject<PayOrderDetailBean>> response) {
                EntityObject<PayOrderDetailBean> body = response.body();
                if (body.getCode() == 200) {
                    PayOrderDetailBean orderBean = body.getResult();
                    if (orderBean == null) {
                        return;
                    }
                    shopName.setText(orderBean.getName());
                    orderId.setText("订单号:" + orderBean.getOrder_no());
                    orderDate.setText("下单时间:" + DateUtils.getFormatDateStr(orderBean.getCreate_time() * 1000));
                    money.setText("￥" + orderBean.getAmount());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PayOrderDetailBean>> call, Throwable t) {

            }
        });
    }

    private void getData() {
        showLoadingDialog();
        RetrofitUtils.getInstances().create().getPayQRcode(data.getOrder_no(), typePay).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                dismissLoadingDialog();
                EntityObject<String> body = response.body();
                if (body.getCode() == 200) {
                  //  payBySelf.setBackgroundResource(R.drawable.cff4d14_solid_big_radius_bg);
                    payBySelf.setClickable(true);
                    Glide.with(ReceivePayActivity.this).load(Constant.url + body.getResult()).into(qRCode);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.detailBtn, R.id.payBySelf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detailBtn:
                OrderDetailActivity.startAct(ReceivePayActivity.this, fromType, data.getOrder_no());
                break;
            case R.id.payBySelf:
                payBySelf.setClickable(false);
                RetrofitUtils.getInstances().create().payByDriver(data.getOrder_no(), typePay).enqueue(new Callback<EntityObject<PayBean>>() {
                    @Override
                    public void onResponse(Call<EntityObject<PayBean>> call, Response<EntityObject<PayBean>> response) {
                        EntityObject<PayBean> body = response.body();
                        if (body.getCode() == 200) {
                            hint(body.getMsg());
                            KLog.d("\n"+body.getMsg()+"\n"+new Gson().toJson(body.getResult()));
                           // Pingpp.createPayment(ReceivePayActivity.this, new Gson().toJson(body.getResult()));
                            mSubmitOrderWxData=  body.getResult();
                            toWXPay();
                        }
                    }

                    @Override
                    public void onFailure(Call<EntityObject<PayBean>> call, Throwable t) {

                    }
                });
                break;
        }
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                KLog.d(result);
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                if (result.equals("success")) {
                    //出货单已完成界面刷新  orderFragment刷新  确定订单页面关闭
//                    EventBus.getDefault().post(new PayFinishEvent());
                    //显示到已完成界面
//                    EventBus.getDefault().post(new MainEvent(3));

                    startActivity(new Intent(ReceivePayActivity.this, MainActivity.class));
                    EventBus.getDefault().post(new CashDeliveryEvent());
                    finish();
                } else {
                    hint("请重新支付");
                    payBySelf.setClickable(true);

                }
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            }
        }
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer!=null){
            mTimer.purge();
            mTimer.cancel();
            mTimer=null;
        }
    }

    /**
     * 调起微信支付的方法
     **/
    private void toWXPay() {
        IWXAPI wxapi = App.getIwxapi(); //应用ID 即微信开放平台审核通过的应用APPID
        PayReq request = new PayReq(); //调起微信APP的对象
        //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
        request.appId = mSubmitOrderWxData.getAppid();
        request.partnerId = mSubmitOrderWxData.getPartnerid();
        request.prepayId = mSubmitOrderWxData.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = mSubmitOrderWxData.getNoncestr();
        request.timeStamp = mSubmitOrderWxData.getTimestamp() + "";
        request.sign = mSubmitOrderWxData.getSign();
        //   EventBus.getDefault().postSticky(new WeChatPaySuccessEvent(mOrderId, mOrderCode, "buy"));
        wxapi.sendReq(request);//发送调起微信的请求
    }
}
