package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BucketHandleOrderAdapter;
import com.shuiwangzhijia.wuliu.adapter.HandleOrderAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.BucketBean;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.dialog.AddSelfBucketDialog;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 操作订单页面
 * created by wangsuli on 2018/9/5.
 */
public class HandleOrderActivity extends BaseAct {
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
    @BindView(R.id.numRecyclerView)
    RecyclerView numRecyclerView;
    @BindView(R.id.bucketRecyclerView)
    RecyclerView bucketRecyclerView;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    @BindView(R.id.unPayBtn)
    TextView unPayBtn;
    @BindView(R.id.addBucketBtn)
    TextView addBucketBtn;
    private HandleOrderAdapter numHandleOrderAdapter;
    private BucketHandleOrderAdapter bucketHandleOrderAdapter;
    private String orderNo;
    private OrderBean mOrderBean;
    private List<BucketBean> recycler;//显示弹窗数据
    private OrderBean resultOrderBean;
    private int payStyle;
    private int leastNum;
    private int goodCount;
    private ArrayList<BucketBean> recyclerDialogData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_order);
        ButterKnife.bind(this);
        mOrderBean = (OrderBean) getIntent().getSerializableExtra("orderData");
        orderNo = mOrderBean.getOrder_sn();
        setTitle("订单操作");
        initNumRecyclerView();
        initBucketRecyclerView();
        shopName.setText(mOrderBean.getSname());
        orderId.setText("订单号:" + mOrderBean.getOrder_sn());
        orderDate.setText("下单时间:" + DateUtils.getFormatDateStr(mOrderBean.getTime() * 1000));
        money.setText("￥" + mOrderBean.getTprice());
        numHandleOrderAdapter.setData(mOrderBean.getGoods());
        getList();
    }

    private void getList() {
        RetrofitUtils.getInstances().create().HandleOrder(orderNo).enqueue(new Callback<EntityObject<OrderBean>>() {
            @Override
            public void onResponse(Call<EntityObject<OrderBean>> call, Response<EntityObject<OrderBean>> response) {
                EntityObject<OrderBean> body = response.body();
                if (body.getCode() == 200) {
                    resultOrderBean = body.getResult();
                    recycler = resultOrderBean.getRecycler();
                    leastNum = resultOrderBean.getLeast_num();
                    payStyle = resultOrderBean.getPay_style();
                    if (payStyle == 1) {
                        sureBtn.setText("去支付");
                        unPayBtn.setVisibility(View.VISIBLE);
                    } else {
                        sureBtn.setText("确认送达");
                        unPayBtn.setVisibility(View.GONE);
                    }
                    numHandleOrderAdapter.setPayStyle(payStyle);
//                    for (BucketBean bean : recycler) {
//                        bean.setCount(bean.getNum());
//                    }
                    bucketHandleOrderAdapter.setData(recycler);
                }

            }

            @Override
            public void onFailure(Call<EntityObject<OrderBean>> call, Throwable t) {

            }
        });
        RetrofitUtils.getInstances().create().showDialogBucket().enqueue(new Callback<EntityObject<ArrayList<BucketBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<BucketBean>>> call, Response<EntityObject<ArrayList<BucketBean>>> response) {
                EntityObject<ArrayList<BucketBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<BucketBean> result = body.getResult();
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    recyclerDialogData = result;
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<BucketBean>>> call, Throwable t) {
            }
        });
    }

    private void initBucketRecyclerView() {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        bucketHandleOrderAdapter = new BucketHandleOrderAdapter(this, true);
        bucketRecyclerView.setAdapter(bucketHandleOrderAdapter);
    }

    private void initNumRecyclerView() {
        numRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        numRecyclerView.setHasFixedSize(true);
        numHandleOrderAdapter = new HandleOrderAdapter(this, false);
        numHandleOrderAdapter.setShopCartInterface(new ShopCartInterface() {
            @Override
            public void add(View view, int position) {
                showTotalPrice();
            }

            @Override
            public void remove(View view, int position) {
                showTotalPrice();

            }
        });

        numRecyclerView.setAdapter(numHandleOrderAdapter);

    }

    private void showTotalPrice() {
        List<GoodsBean> data = numHandleOrderAdapter.getData();
        Double total = 0.0;
        goodCount = 0;
        for (GoodsBean bean : data) {
            total = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getCount()), total);
            goodCount = goodCount + bean.getCount();
        }
        numHandleOrderAdapter.setLimitCount(goodCount);
        money.setText("￥" + total);
    }

    @OnClick({R.id.detailBtn, R.id.addBucketBtn, R.id.unPayBtn, R.id.sureBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detailBtn:
                OrderDetailActivity.startAct(HandleOrderActivity.this, 1, mOrderBean.getOrder_sn());
                break;
                //添加自营桶按钮
            case R.id.addBucketBtn:
                if (recyclerDialogData == null || recyclerDialogData.size() == 0) {
                    return;
                }
                int itemCount = bucketHandleOrderAdapter.getItemCount();
                if (itemCount != 0) {
                    BucketBean old, item = null;
                    for (int i = 0; i < recyclerDialogData.size(); i++) {
                        old = recyclerDialogData.get(i);
                        boolean check = false;
                        for (int j = 0; j < itemCount; j++) {
                            item = bucketHandleOrderAdapter.getItem(j);
                            if (old.getBid() == item.getBid()) {
                                check = true;
                                old.setCount(item.getCount());
                            }

                        }
                        old.setCheck(check);
                    }
                }
                AddSelfBucketDialog dialog = new AddSelfBucketDialog(HandleOrderActivity.this, recyclerDialogData, new AddSelfBucketDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<BucketBean> data) {
                        dialog.dismiss();
                        bucketHandleOrderAdapter.setData(data);
                    }
                });
                dialog.show();
                break;
            case R.id.unPayBtn:
//                post(2, false);
                break;
            case R.id.sureBtn:
                //pay_style ==1 货到付款  先 调sureOrder 再去支付页面 调支付接口
                //pay_style ==0 调sureOrder确认送达接口
                if (payStyle == 1) {
                    //货到付款 去支付
//                    post(1, true);
                } else {
                    //确认到达
//                    post(1, false);
                }
                break;
        }
    }

//    private void post(final int status, final boolean toPayBtn) {
//        if (CalculateUtils.sub(leastNum, goodCount) > 0) {
//            ToastUitl.showToastCustom("商品数量的总量不能低于水厂起送量");
//            return;
//        }
//        RetrofitUtils.getInstances().create().sureOrder(CommonUtils.getToken(), status, orderNo, packageData(numHandleOrderAdapter.getData()), packageBucketData(bucketHandleOrderAdapter.getData())).enqueue(new Callback<EntityObject<String>>() {
//            @Override
//            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
//                EntityObject<String> body = response.body();
//                if (body.getCode() == 200) {
//                    if (toPayBtn) {
//                        PreparePayBean data = new PreparePayBean();
//                        data.setSname(mOrderBean.getSname());
//                        data.setOrder_no(mOrderBean.getOrder_sn());
//                        data.setCreate_time(DateUtils.getFormatDateStr(mOrderBean.getTime() * 1000));
//                        data.setTamount(mOrderBean.getTprice());
//                        data.setPayFrom(4);
//                        ReceivePayActivity.startAtc(HandleOrderActivity.this, data, 1);
//                        finish();
//                    } else {
//                        ToastUitl.showToastCustom("提交成功");
//                        EventBus.getDefault().post(new MainEvent(status == 2 ? 2 : 3));
//                        finish();
//                    }
//
//                } else if(body.getCode()==600){
//                    ToastUitl.showToastCustom(body.getMsg());
//                    EventBus.getDefault().post(new MainEvent( 3));
//                    finish();
//                }else {
//                    ToastUitl.showToastCustom(body.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EntityObject<String>> call, Throwable t) {
//
//            }
//        });
//    }

    private String packageData(List<GoodsBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (GoodsBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    private String packageBucketData(List<BucketBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (BucketBean bean : data) {
            item = new JSONObject();
            try {
                item.put("bid", bean.getBid());
                item.put("num", bean.getCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

}
