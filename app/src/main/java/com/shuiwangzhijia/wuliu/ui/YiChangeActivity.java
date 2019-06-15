package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BucketHandleOrderAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.BucketBean;
import com.shuiwangzhijia.wuliu.bean.BucketOperationBean;
import com.shuiwangzhijia.wuliu.bean.BucketRecordBean;
import com.shuiwangzhijia.wuliu.bean.PolicyBean;
import com.shuiwangzhijia.wuliu.bean.PreparePayBean;
import com.shuiwangzhijia.wuliu.bean.ShopBean;
import com.shuiwangzhijia.wuliu.bean.TuiTongBean;
import com.shuiwangzhijia.wuliu.dialog.AddZiyingTongDialog;
import com.shuiwangzhijia.wuliu.dialog.EditPurchaseAmountDialog;
import com.shuiwangzhijia.wuliu.dialog.HintDialog;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 换桶 退桶 压桶 页面
 * created by wangsuli on 2018/9/5.
 */
public class YiChangeActivity extends BaseAct {

    @BindView(R.id.selectShop)
    TextView selectShop;
    @BindView(R.id.rlSelectShop)
    RelativeLayout rlSelectShop;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.remove)
    ImageView remove;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.moneyTv)
    TextView moneyTv;
    @BindView(R.id.moneyEdit)
    EditText moneyEdit;
    @BindView(R.id.bucketRecyclerView)
    RecyclerView bucketRecyclerView;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    @BindView(R.id.addBucketBtn)
    TextView addBucketBtn;
    @BindView(R.id.numTv)
    EditText numTv;
    @BindView(R.id.llPayType)
    LinearLayout llPayType;
    @BindView(R.id.bucketRecordTitleTv)
    TextView bucketRecordTitleTv;
    @BindView(R.id.bucketRecordTv)
    TextView bucketRecordTv;
    @BindView(R.id.rlBucketRecord)
    RelativeLayout rlBucketRecord;
    @BindView(R.id.llSaveBucketRecord)
    LinearLayout llSaveBucketRecord;
    @BindView(R.id.payMoneyBtn)
    TextView payMoneyBtn;
    @BindView(R.id.ticketBtn)
    TextView ticketBtn;
    @BindView(R.id.llOtherBucket)
    LinearLayout llOtherBucket;
    @BindView(R.id.moneyTitle)
    TextView moneyTitle;
    @BindView(R.id.llMoney)
    LinearLayout llMoney;
    @BindView(R.id.unit)
    TextView mUnit;
    private BucketHandleOrderAdapter bucketHandleOrderAdapter;
    private ArrayList<BucketBean> recycler;
    private int count = 1;
    private ShopBean mShopBean;
    private int type;
    private double price = 10;
    private int payType = 0;
    private TuiTongBean.ListBean quitBucket;
    private BucketRecordBean mBucketRecordBean;
    private ArrayList<BucketBean> recyclerTongDialogData;
    private String mBucket_order_sn;

    public static void startAct(Context context, int type, TuiTongBean.ListBean total) {
        Intent intent = new Intent(context, YiChangeActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("quitBucket", total);
        context.startActivity(intent);
    }

    public static void startAct(Context context, int type, TuiTongBean.ListBean total, int count) {
        Intent intent = new Intent(context, YiChangeActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("quitBucket", total);
        intent.putExtra("total", count);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_change);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        switch (type) {
            case 1:
                setTitle("换桶");
                llOtherBucket.setVisibility(View.VISIBLE);
                llPayType.setVisibility(View.VISIBLE);
                llMoney.setVisibility(View.GONE);
                setState(0);
                getData();
                changeNum();
                break;
            case 2:
                setTitle("押桶");
                moneyTv.setVisibility(View.GONE);
                moneyEdit.setVisibility(View.VISIBLE);
                getData();
                changeNum();
                break;
            case 3:
                quitBucket = (TuiTongBean.ListBean) getIntent().getSerializableExtra("quitBucket");
                int total = getIntent().getIntExtra("total", -1);
                setTitle("退桶操作");
                rlSelectShop.setClickable(false);
                selectShop.setCompoundDrawables(null, null, null, null);
                llSaveBucketRecord.setVisibility(View.VISIBLE);
                addBucketBtn.setVisibility(View.GONE);
                moneyTitle.setText("应退金额");
                selectShop.setText(quitBucket.getSname());
                moneyTv.setText("0");
                bucketRecordTitleTv.setText("压桶记录（还有" + quitBucket.getNum() + "个压桶记录）");
                bucketRecyclerView.setVisibility(View.GONE);
                getList();
                break;
        }
        initBucketRecyclerView();

    }

    private void getList() {
        showLoad();
        RetrofitUtils.getInstances().create().getBucketRecord(quitBucket.getSid(), "").enqueue(new Callback<EntityObject<BucketOperationBean>>() {
            @Override
            public void onResponse(Call<EntityObject<BucketOperationBean>> call, Response<EntityObject<BucketOperationBean>> response) {
                hintLoad();
                EntityObject<BucketOperationBean> body = response.body();
                if (body.getCode() == 200) {
                    BucketOperationBean result = body.getResult();
                    KLog.d(result.getTotal_price());
                    moneyTv.setText(result.getTotal_price());
                    mBucket_order_sn = result.getBucket_order_sn();
                    List<BucketBean> goods = result.getGoods();
                    bucketHandleOrderAdapter.setData(goods);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<BucketOperationBean>> call, Throwable t) {

            }
        });
    }


    private void getData() {
        moneyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //正则，用来判断是否输入了小数点
                String regex = "^\\d+.$";
                Pattern r = Pattern.compile(regex);
                Matcher matcher = r.matcher(charSequence);
                if (matcher.matches()){
                    moneyEdit.setFilters(new InputFilter[]{
                            new InputFilter.LengthFilter(charSequence.length()+2)
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        RetrofitUtils.getInstances().create().getBucketData().enqueue(new Callback<EntityObject<ArrayList<BucketBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<BucketBean>>> call, Response<EntityObject<ArrayList<BucketBean>>> response) {
                EntityObject<ArrayList<BucketBean>> body = response.body();
                if (body.getCode() == 200) {
                    recycler = body.getResult();
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<BucketBean>>> call, Throwable t) {

            }
        });

        //可换取的自营桶列表接口
        RetrofitUtils.getInstances().create().canChangeBucketList().enqueue(new Callback<EntityObject<ArrayList<BucketBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<BucketBean>>> call, Response<EntityObject<ArrayList<BucketBean>>> response) {
                EntityObject<ArrayList<BucketBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<BucketBean> result = body.getResult();
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    recyclerTongDialogData = result;
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
        bucketHandleOrderAdapter.setOnItemClickListener(new BucketHandleOrderAdapter.OnViewItemClickListener() {
            @Override
            public void doEditCount(int position, int count) {
                BucketBean item = bucketHandleOrderAdapter.getItem(position);
                if (count < 1) {
                    ToastUitl.showToastCustom("购买数量不能低于最低购买量!");
                    return;
                }
                item.setCount(count);
                bucketHandleOrderAdapter.notifyDataSetChanged();
            }
        });
        bucketRecyclerView.setAdapter(bucketHandleOrderAdapter);
    }


    @OnClick({R.id.rlSelectShop, R.id.remove, R.id.add, R.id.sureBtn, R.id.addBucketBtn, R.id.rlBucketRecord, R.id.payMoneyBtn, R.id.ticketBtn, R.id.account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBucketRecord:
                BucketRecordActivity.startAct(this, quitBucket.getSid());
                break;
            case R.id.rlSelectShop:
                startActivity(new Intent(this, SelectShopActivity.class));
                break;
            case R.id.remove:
                if (count <= 0)
                    return;
                count--;
                changeNum();
                break;
            case R.id.add:
                count++;
                changeNum();
                break;
            case R.id.addBucketBtn:
                if (recyclerTongDialogData == null) {
                    ToastUitl.showToastCustom("正加载数据...");
                    return;
                }
                int itemCount = bucketHandleOrderAdapter.getItemCount();
                if (itemCount != 0) {
                    BucketBean old, item = null;
                    for (int i = 0; i < recyclerTongDialogData.size(); i++) {
                        old = recyclerTongDialogData.get(i);
                        boolean check = false;
                        for (int j = 0; j < itemCount; j++) {
                            item = bucketHandleOrderAdapter.getItem(j);
                            if (old.getGid() == item.getGid()) {
                                check = true;
                                old.setCount(item.getCount());
                            }

                        }
                        old.setCheck(check);
                    }
                }
                AddZiyingTongDialog dialog = new AddZiyingTongDialog(this, recyclerTongDialogData, new AddZiyingTongDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<BucketBean> data) {
                        dialog.dismiss();
                        bucketHandleOrderAdapter.setData(data);
                    }
                });
                dialog.show();
                break;
            case R.id.sureBtn:
                switch (type) {
                    case 1:
                        postChangeBucket();
                        break;
                    case 2:
                        HintDialog hintDialog = new HintDialog(YiChangeActivity.this, "请仔细确认水店名称,\n金额及桶类型和数量", new HintDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog) {
                                dialog.dismiss();
                                postPressBucket();
                            }
                        });
                        if (!hintDialog.isShowing())
                            hintDialog.show();
                        break;
                    case 3:
                        postQuitBucket();
                        break;
                }
                break;
            case R.id.payMoneyBtn:
                setState(0);
                break;
            case R.id.ticketBtn:
                setState(1);
                break;
            case R.id.account:
                EditPurchaseAmountDialog mEditPurchaseAmountDialog = new EditPurchaseAmountDialog(YiChangeActivity.this, count, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        YiChangeActivity.this.count = count;
                        changeNum();
                    }
                });
                mEditPurchaseAmountDialog.show();
                break;
        }
    }

    private void setState(int flag) {
        payType = flag;
        numTv.setText("");
        numTv.setKeyListener(new DigitsKeyListener(false, flag == 0));
        payMoneyBtn.setSelected(flag == 0);
        ticketBtn.setSelected(flag == 1);
    }

    /**
     * 计算金额
     */
    private void changeNum() {
        account.setText(count + "");
        moneyTv.setText("" + CalculateUtils.mul(price, count));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(ShopBean bean) {
        mShopBean = bean;
        selectShop.setText(mShopBean.getSname());
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(BucketRecordBean bean) {
        bucketRecyclerView.setVisibility(View.VISIBLE);
        mBucketRecordBean = bean;
        bucketRecordTv.setText("压桶" + mBucketRecordBean.getSum() + "个 (" + DateUtils.getYMDTime(bean.getOrder_time() * 1000) + ")");
        moneyTv.setText(mBucketRecordBean.getTotal_price());
        bucketHandleOrderAdapter.setData(bean.getData());
        bucketHandleOrderAdapter.setQuitBucket(true);
    }

    private int bucketCount = 0;

    private String packageData(List<BucketBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        bucketCount = 0;
        for (BucketBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getNum());
                bucketCount = bucketCount + bean.getCount();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        return array.toString();
    }

    private void postChangeBucket() {
        if (mShopBean == null) {
            ToastUitl.showToastCustom("请选择水店");
            return;
        }

        if (count == 0) {
            ToastUitl.showToastCustom("请输入杂桶数量");
            return;
        }
        String num = numTv.getText().toString();
        if (TextUtils.isEmpty(num)) {
            ToastUitl.showToastCustom(payType == 0 ? "请输入支付金额！" : "请输入选水票张数！");
        }
        String data = packageData(bucketHandleOrderAdapter.getData());
        if (bucketCount != count) {
            ToastUitl.showToastCustom("杂桶数量应与自营桶数量一样");
            return;
        }
        String price = "", ticketNum = "";
        if (payType == 0) {
            price = num;
        } else {
            ticketNum = num;
        }

        RetrofitUtils.getInstances().create().postChangeBucket(count, mShopBean.getId(), payType + "", price, ticketNum, data).enqueue(new Callback<EntityObject<PolicyBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PolicyBean>> call, Response<EntityObject<PolicyBean>> response) {
                EntityObject<PolicyBean> body = response.body();
                if (body.getCode() == 200) {
                    PolicyBean result = body.getResult();
                    if (payType == 1) {
                        ToastUitl.showToastCustom(body.getMsg());
                        finish();
                    } else {
                        toPay(result);
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PolicyBean>> call, Throwable t) {

            }
        });
    }

    private void toPay(PolicyBean result) {
        PreparePayBean data = new PreparePayBean();
        data.setOrder_no(result.getOrder_no());
        data.setTamount(result.getPrice());
        data.setPayFrom(1);
        data.setSname(result.getSname());
        data.setDname(result.getDname());
        data.setPolicy(result.getPolicy());
        ReceivePayActivity.startAtc(YiChangeActivity.this, data, 5);
        finish();
    }

    private void postPressBucket() {
        if (mShopBean == null) {
            ToastUitl.showToastCustom("请选择水店");
            return;
        }
        String price = moneyEdit.getText().toString();
        if (TextUtils.isEmpty(price)) {
            ToastUitl.showToastCustom("请输入需支付的金额");
            return;
        }
        String data = packageData(bucketHandleOrderAdapter.getData());
        RetrofitUtils.getInstances().create().postPressBucket(mShopBean.getId(), price, data).enqueue(new Callback<EntityObject<PolicyBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PolicyBean>> call, Response<EntityObject<PolicyBean>> response) {
                EntityObject<PolicyBean> body = response.body();
                if (body.getCode() == 200) {
                    PolicyBean result = body.getResult();
                    toPay(result);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PolicyBean>> call, Throwable t) {

            }
        });
    }

    private void postQuitBucket() {
        if (mBucketRecordBean == null) {
            ToastUitl.showToastCustom("请选择压桶记录");
            return;
        }
        RetrofitUtils.getInstances().create().postQuitBucket(mBucket_order_sn).enqueue(new Callback<EntityObject<PolicyBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PolicyBean>> call, Response<EntityObject<PolicyBean>> response) {
                EntityObject<PolicyBean> body = response.body();
                if (body.getCode() == 200) {
//                    PolicyBean result = body.getResult();
//                    toPay(result);
                    ToastUitl.showToastCustom("退水成功");
                    finish();
                }else {
                    ToastUitl.showToastCustom(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PolicyBean>> call, Throwable t) {

            }
        });
    }
}
