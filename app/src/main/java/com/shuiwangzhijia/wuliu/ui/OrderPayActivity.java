package com.shuiwangzhijia.wuliu.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.pingplusplus.android.Pingpp;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


/**
 * 订单支付页面
 * created by wangsuli on 2018/8/22.
 */
public class OrderPayActivity extends BaseAct {
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.orderDate)
    TextView orderDate;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.onlineBtn)
    CheckBox onlineBtn;
    @BindView(R.id.weChatBtn)
    CheckBox weChatBtn;
    @BindView(R.id.aliPayBtn)
    CheckBox aliPayBtn;
    @BindView(R.id.llOnline)
    LinearLayout llOnline;
    @BindView(R.id.offlineBtn)
    CheckBox offlineBtn;
    @BindView(R.id.payBtn)
    TextView payBtn;
    private OrderBean data;
    private int pay_type = 1;//0表示货到付款 1微信 2 支付宝


    public static void startAtc(Context context, OrderBean data) {
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        ButterKnife.bind(this);
        setTitle("订单支付");
        data = (OrderBean) getIntent().getSerializableExtra("data");
//        orderId.setText("订单号:" + data.getOrder_no());
//        orderDate.setText("下单时间:" + data.getCreate_time());
//        money.setText("￥" + data.getTotal_price());
        onlineBtn.setOnClickListener(this);
        weChatBtn.setOnClickListener(this);
        aliPayBtn.setOnClickListener(this);
        offlineBtn.setOnClickListener(this);
        changeState(1);
    }

    private void getChannelInfo() {
       /* RetrofitUtils.getInstances().create().getPayChannelInfo(data.getOrder_no(), pay_type == 1 ? "wx" : "alipay").enqueue(new Callback<EntityObject<PayBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PayBean>> call, Response<EntityObject<PayBean>> response) {
                EntityObject<PayBean> body = response.body();
                hint(body.getMsg());
                Pingpp.createPayment(OrderPayActivity.this, new Gson().toJson(body.getResult()));
            }

            @Override
            public void onFailure(Call<EntityObject<PayBean>> call, Throwable t) {

            }
        });
*/

    }

    @OnClick(R.id.payBtn)
    public void onViewClicked() {
        if (pay_type == 0) {
            payOffLine();
        } else {
            getChannelInfo();
        }

    }

    private void payOffLine() {
       /* RetrofitUtils.getInstances().create().payOffLine(data.getOrder_no()).enqueue(new Callback<EntityObject<PayBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PayBean>> call, Response<EntityObject<PayBean>> response) {
                EntityObject<PayBean> body = response.body();
                hint(body.getMsg());
                if (body.getCode() == 200) {
                    List<GoodsBean> newShopCart = new ArrayList<>();
                    List<GoodsBean> shopCart = CommonUtils.getShopCart();
                    List<GoodsBean> buyGoodsList = CommonUtils.getBuyGoodsList();
                    for (GoodsBean bean : buyGoodsList) {
                        for (int i = 0; i < shopCart.size(); i++) {
                            GoodsBean goodsBean = shopCart.get(i);
                            if (goodsBean.getId() != bean.getId()) {
                                newShopCart.add(goodsBean);
                            }
                        }

                    }
                    CommonUtils.saveBuyGoodsList(null);
                    CommonUtils.saveShopCart(newShopCart);
                    EventBus.getDefault().post(new PayFinishEvent());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PayBean>> call, Throwable t) {

            }
        });*/
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.onlineBtn:
                changeState(1);
                break;
            case R.id.weChatBtn:
                weChatBtn.setChecked(weChatBtn.isChecked());
                aliPayBtn.setChecked(!weChatBtn.isChecked());
                pay_type = 1;
                break;
            case R.id.aliPayBtn:
                weChatBtn.setChecked(!aliPayBtn.isChecked());
                aliPayBtn.setChecked(aliPayBtn.isChecked());
                pay_type = 2;
                break;
            case R.id.offlineBtn:
                changeState(0);
                break;
        }
    }

    private void hint(String text) {
        Toast.makeText(OrderPayActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void changeState(int type) {
        pay_type = type;
        if (type == 0) {
            onlineBtn.setChecked(false);
            offlineBtn.setChecked(true);
            llOnline.setVisibility(View.GONE);
        } else {
            onlineBtn.setChecked(true);
            offlineBtn.setChecked(false);
            llOnline.setVisibility(View.VISIBLE);
            weChatBtn.setChecked(type == 1 ? true : false);
            aliPayBtn.setChecked(type == 2 ? true : false);
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
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                if (result.equals("success")) {
                    EventBus.getDefault().post(new PayFinishEvent());
                } else {
                    hint("请重新支付");
                }
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        finish();
    }
}
