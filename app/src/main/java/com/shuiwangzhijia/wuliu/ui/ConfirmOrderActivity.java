package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.activitydriver.DriverNewOrderActivity;
import com.shuiwangzhijia.wuliu.adapter.OrderItemAdapterNew;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.AddressBean;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.bean.PreparePayBean;
import com.shuiwangzhijia.wuliu.dialog.ScheduleDialog;
import com.shuiwangzhijia.wuliu.event.AddressEvent;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.activity_confirm_order, title = "确认订单")
public class ConfirmOrderActivity extends BaseActivity {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.payBtn)
    TextView payBtn;
    @BindView(R.id.llAddress)
    RelativeLayout llAddress;
    @BindView(R.id.selectBucket)
    TextView selectBucket;
    @BindView(R.id.unSelectBucket)
    TextView unSelectBucket;
    @BindView(R.id.schedule)
    TextView schedule;
    @BindView(R.id.llSchedule)
    CardView llSchedule;
    @BindView(R.id.llMark)
    LinearLayout llMark;
    @BindView(R.id.payMoneyBtn)
    RadioButton mPayMoneyBtn;
    @BindView(R.id.ticketBtn)
    RadioButton mTicketBtn;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.back_return)
    RelativeLayout mBackReturn;
    private OrderItemAdapterNew mOrderAdapter;
    private List<GoodsBean> buyData = new ArrayList<>();
    private String totalMoney;
    private AddressBean addressBean;
    private int sid, payFrom;   //1代表1换1代付，2代表代下单代付   3追加订单代付
    private boolean bukFlag = true;
    private OrderBean oldOrder;//追加订单--下单 原订单
    private int type = -1;//支付方式

    public static void startAtc(Context context, List<GoodsBean> data, String total, int shopId, int payFrom, OrderBean oldOrder) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("money", total);
        intent.putExtra("sid", shopId);
        intent.putExtra("payFrom", payFrom);
        intent.putExtra("oldOrder", oldOrder);
        context.startActivity(intent);
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        setBaseBarHide();
        buyData = (List<GoodsBean>) getIntent().getSerializableExtra("data");
        oldOrder = (OrderBean) getIntent().getSerializableExtra("oldOrder");
        totalMoney = getIntent().getStringExtra("money");
        sid = getIntent().getIntExtra("sid", -1);
        payFrom = getIntent().getIntExtra("payFrom", -1);
        money.setText("￥" + totalMoney);
        if (oldOrder == null) {
            shopName.setText(buyData.get(0).getSname());
            getDefaultAddress();
        } else {
            shopName.setText(oldOrder.getSname());
            llAddress.setVisibility(View.GONE);
            llMark.setVisibility(View.GONE);
        }
        initRecyclerView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                count.setText(text.length() + "/200");
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.payMoneyBtn:
                        type = 1;
                        break;
                    case R.id.ticketBtn:
                        type = 2;
                        break;
                }
            }
        });
        setState(true);
    }


    private void getDefaultAddress() {
        RetrofitUtils.getInstances().create().getDefaultAddress(sid).enqueue(new Callback<EntityObject<ArrayList<AddressBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<AddressBean>>> call, Response<EntityObject<ArrayList<AddressBean>>> response) {
                EntityObject<ArrayList<AddressBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<AddressBean> result = body.getResult();
                    if (result == null || result.size() == 0) {
                        name.setText("请选择收货人地址信息");
                        phone.setText("");
                        address.setText("");
                    } else {
                        addressBean = result.get(0);
                        //name.setText("联系人:" + addressBean.getAname());
                        name.setText("" + addressBean.getAname());
                        phone.setText(addressBean.getSphone());
                        //address.setText("收货地址:" + addressBean.getDaddress());
                        address.setText("" + addressBean.getDaddress());
                    }

                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<AddressBean>>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(this.getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(divider);
        mOrderAdapter = new OrderItemAdapterNew(this, false);
        mOrderAdapter.setData(buyData);
        mRecyclerView.setAdapter(mOrderAdapter);
    }

    @OnClick({R.id.payBtn, R.id.llAddress, R.id.llSchedule, //
            R.id.selectBucket, R.id.unSelectBucket,//
            R.id.back_return
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llAddress:
                Intent intent = new Intent(ConfirmOrderActivity.this, AddressManageActivity.class);
                intent.putExtra("sid", sid);
                startActivity(intent);
                break;
            case R.id.payBtn:
                postpay();
                break;
            case R.id.selectBucket:
                setState(true);
                break;
            case R.id.unSelectBucket:
                setState(false);
                break;
            case R.id.llSchedule:
                ScheduleDialog scheduleDialog = new ScheduleDialog(this, new ScheduleDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, long date) {
                        dialog.dismiss();
                        schedule.setText("在" + date + "内送达");

                    }
                });
                scheduleDialog.show();
                break;
            case R.id.back_return:
                closeActivity();
                break;
        }
    }

    private void setState(boolean flag) {
        bukFlag = flag;
        selectBucket.setSelected(flag);
        unSelectBucket.setSelected(!flag);
    }

    private void postpay() {
        if (oldOrder == null) {
            createOrder();
        } else {
            appendOrder();
        }
    }

    private void createOrder() {
        if (addressBean == null) {
            hint("请选择收货人地址信息...");
            return;
        }
        if (type == -1) {
            hint("请选择支付方式");
            return;
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (GoodsBean bean : buyData) {
            item = new JSONObject();
            try {
                item.put("did", bean.getDid());
                item.put("gid", bean.getGid());
                item.put("num", bean.getCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }

        RetrofitUtils.getInstances().create().createOrder(CommonUtils.getToken(), sid, bukFlag ? 1 : 0, totalMoney, addressBean.getAname() + " " + addressBean.getSphone() + " " + addressBean.getDaddress(), array.toString(), edit.getText().toString().trim(), addressBean.getId(), type).enqueue(new Callback<EntityObject<PreparePayBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PreparePayBean>> call, Response<EntityObject<PreparePayBean>> response) {
                EntityObject<PreparePayBean> body = response.body();
                if (body.getCode() == 200) {
                    if (type == 1) {
                        PreparePayBean result = body.getResult();
                        result.setPayFrom(payFrom);
                        ReceivePayActivity.startAtc(ConfirmOrderActivity.this, result, 2);
                        finish();
                    } else if (type == 2) {
                        Bundle bundle=new Bundle();
                        bundle.putInt("selectPosition",2);
                        skipActivity(DriverNewOrderActivity.class,bundle);
                      //  startActivity(new Intent(ConfirmOrderActivity.this, MainActivity.class));
                     //   EventBus.getDefault().post(new CashDeliveryEvent());
                        finish();
                    }
                } else {
                    hint(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PreparePayBean>> call, Throwable t) {

            }
        });
    }

    private void appendOrder() {
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (GoodsBean bean : buyData) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        RetrofitUtils.getInstances().create().appendOrder(totalMoney, oldOrder.getOrder_sn(), array.toString()).enqueue(new Callback<EntityObject<PreparePayBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PreparePayBean>> call, Response<EntityObject<PreparePayBean>> response) {
                EntityObject<PreparePayBean> body = response.body();
                if (body.getCode() == 200) {
                    PreparePayBean result = body.getResult();
                    result.setSname(oldOrder.getSname());
                    result.setOrder_no(result.getOrder_sns());
                    result.setTamount(result.getSprice());
                    result.setCreate_time(DateUtils.getFormatDateStr(result.getTime() * 1000));
                    result.setPayFrom(payFrom);
                    ReceivePayActivity.startAtc(ConfirmOrderActivity.this, result, 3);
                    finish();
//                    hint("追加订单成功");
                } else {
                    hint(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PreparePayBean>> call, Throwable t) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(AddressEvent event) {
        if (event.isDelete) {
            AddressBean data = event.data;
            if (data.getId() == addressBean.getId()) {
                name.setText("请选择收货人地址信息");
                phone.setText("");
                address.setText("");
            }
            addressBean = null;
            if (data.getDefaultInt() != 0) {
                getDefaultAddress();
            } else {
                getListAddress();
            }

        } else {
            if (event.data != null) {
                addressBean = event.data;
                name.setText("联系人:" + addressBean.getAname());
                phone.setText(addressBean.getSphone());
                address.setText("收货地址:" + addressBean.getDaddress());
            }
        }

    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }

    private void getListAddress() {
        RetrofitUtils.getInstances().create().getAddressList(sid).enqueue(new Callback<EntityObject<ArrayList<AddressBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<AddressBean>>> call, Response<EntityObject<ArrayList<AddressBean>>> response) {
                EntityObject<ArrayList<AddressBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<AddressBean> result = body.getResult();
                    if (result == null || result.size() == 0) {
                        name.setText("请选择收货人地址信息");
                        phone.setText("");
                        address.setText("");
                    } else {
                        addressBean = result.get(0);
                        name.setText("联系人:" + addressBean.getAname());
                        phone.setText(addressBean.getSphone());
                        address.setText("收货地址:" + addressBean.getDaddress());
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<AddressBean>>> call, Throwable t) {

            }
        });
    }
}
