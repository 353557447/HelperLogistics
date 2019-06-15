package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BucketHandleOrderAdapter;
import com.shuiwangzhijia.wuliu.adapter.OtherBucketShuipiaoAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.BucketBean;
import com.shuiwangzhijia.wuliu.bean.BucketShowBean;
import com.shuiwangzhijia.wuliu.bean.CanShuipiaoBean;
import com.shuiwangzhijia.wuliu.dialog.AddZatongDialog;
import com.shuiwangzhijia.wuliu.dialog.AddZiyingTongDialog;
import com.shuiwangzhijia.wuliu.event.RecyclingEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
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
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 杂桶回收确认操作页面
 * created by wangsuli on 2018/9/5.
 */
public class OtherBucketHandleActivity extends BaseAct {
    private static final String TAG = "OtherBucketHandleActivi";
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.remove)
    ImageView remove;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.bucketRecyclerView)
    RecyclerView bucketRecyclerView;
    @BindView(R.id.addBucketBtn)
    TextView addBucketBtn;
    @BindView(R.id.payMoneyBtn)
    RadioButton payMoneyBtn;
//    @BindView(R.id.ticketBtn)
//    RadioButton ticketBtn;
    @BindView(R.id.numTv)
    EditText numTv;
    @BindView(R.id.payStatus)
    TextView payStatus;
    @BindView(R.id.ticketRecyclerView)
    RecyclerView ticketRecyclerView;
    @BindView(R.id.addTicketBtn)
    TextView addTicketBtn;
    @BindView(R.id.llTicket)
    LinearLayout llTicket;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.select_zhifu)
    LinearLayout mSelectZhifu;
    private int count = 1;
    private String mOrder_sn;
    private ArrayList<BucketBean> recyclerTongDialogData;
    private ArrayList<CanShuipiaoBean> recyclerShuipiaoData;
    private BucketHandleOrderAdapter bucketHandleOrderAdapter;
    private OtherBucketShuipiaoAdapter mShuipiaoAdapter;
    private int snum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bucket_handle);
        ButterKnife.bind(this);
        setTitle("杂桶回收确认操作");
        Intent intent = getIntent();
        mOrder_sn = intent.getStringExtra("order_sn");
        getList();
        //添加自营桶上的列表
        initBucketRecyclerView();
        //添加水票上的列表
        initShuiPiaoRecyclerView();
        initLisener();
    }

    private void initShuiPiaoRecyclerView() {
        ticketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ticketRecyclerView.setHasFixedSize(true);
        mShuipiaoAdapter = new OtherBucketShuipiaoAdapter(this);
        ticketRecyclerView.setAdapter(mShuipiaoAdapter);
    }

    private void initBucketRecyclerView() {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        bucketHandleOrderAdapter = new BucketHandleOrderAdapter(this, true);
        bucketRecyclerView.setAdapter(bucketHandleOrderAdapter);
    }

    private void initLisener() {
        numTv.addTextChangedListener(new TextWatcher() {
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
                    numTv.setFilters(new InputFilter[]{
                            new InputFilter.LengthFilter(charSequence.length()+2)
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.payMoneyBtn:
                        llTicket.setVisibility(View.GONE);
                        mSelectZhifu.setVisibility(View.VISIBLE);
                        sureBtn.setText("去支付");
                        break;
//                    case R.id.ticketBtn:
//                        mSelectZhifu.setVisibility(View.GONE);
//                        llTicket.setVisibility(View.VISIBLE);
//                        sureBtn.setText("确认");
//                        break;
                }
            }
        });
    }

    private void getList() {
        RetrofitUtils.getInstances().create().showBucketDetail(mOrder_sn).enqueue(new Callback<EntityObject<BucketShowBean>>() {
            @Override
            public void onResponse(Call<EntityObject<BucketShowBean>> call, Response<EntityObject<BucketShowBean>> response) {
                EntityObject<BucketShowBean> body = response.body();
                if (body.getCode() == 200){
                    BucketShowBean result = body.getResult();
                    account.setText(result.getZ_num()+"");

                    //支付状态
                    int pay_status = result.getPay_status();
                    if (pay_status == 0){
                        payStatus.setText("支付状态：未支付");
                    }else {
                        payStatus.setText("支付状态：已支付");
                    }

                    //补贴方式
//                    int barrel_way = result.getBarrel_way();
//                    if (barrel_way == 0){
//                        payMoneyBtn.setChecked(true);
//                    }else {
//                        ticketBtn.setChecked(true);
//                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<BucketShowBean>> call, Throwable t) {

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

        //显示可换取的水票列表
        RetrofitUtils.getInstances().create().canChangePicket().enqueue(new Callback<EntityObject<ArrayList<CanShuipiaoBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<CanShuipiaoBean>>> call, Response<EntityObject<ArrayList<CanShuipiaoBean>>> response) {
                EntityObject<ArrayList<CanShuipiaoBean>> body = response.body();
                if (body.getCode() == 200){
                    ArrayList<CanShuipiaoBean> result = body.getResult();
                    if (result == null || result.size() == 0){
                        return;
                    }
                    recyclerShuipiaoData = result;
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<CanShuipiaoBean>>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.remove, R.id.add, R.id.sureBtn, R.id.addBucketBtn, R.id.account , R.id.addTicketBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.remove:
                if (count <= 0)
                    return;
                count--;
                account.setText(count+"");
                break;
            case R.id.add:
                count++;
                account.setText(count+"");
                break;
            case R.id.addBucketBtn:
                //添加自营桶点击
                if (recyclerTongDialogData == null || recyclerTongDialogData.size() == 0){
                    ToastUitl.showToastCustom("自营桶无数据");
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
                //添加水票点击
            case R.id.addTicketBtn:
                AddZatongDialog addZatongDialog = new AddZatongDialog(this, recyclerShuipiaoData, new AddZatongDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<CanShuipiaoBean> data) {
                        dialog.dismiss();
                        mShuipiaoAdapter.setData(data);
                    }
                });
                addZatongDialog.show();
                break;
            case R.id.sureBtn:
                //去支付点击
                String status = sureBtn.getText().toString().trim();
                if (status.equals("去支付")){
                    postGoPay();
                }else if (status.equals("确认")){
                    postChangeBucket();
                }
                break;
            case R.id.account:
               /* EditPurchaseAmountDialog mEditPurchaseAmountDialog = new EditPurchaseAmountDialog(OtherBucketHandleActivity.this, count, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        OtherBucketHandleActivity.this.count = count;
                    }
                });
                mEditPurchaseAmountDialog.show();*/
                break;
        }
    }

    private void postChangeBucket() {
            snum = 0;
            String count = account.getText().toString().trim();
            List<CanShuipiaoBean> data = mShuipiaoAdapter.getData();
            if (data != null){
                for (CanShuipiaoBean bean : data){
                    if (bean.getCount() > 0){
                        snum += bean.getCount();
                    }
                }
            }
            KLog.d("\n"+CommonUtils.getToken()+"\n"+mOrder_sn+"\n"+count+"\n"+packageData(bucketHandleOrderAdapter.getData())+"\n"+snum+"\n"+packageShuiPiaoData(mShuipiaoAdapter.getData()));
            RetrofitUtils.getInstances().create().changeBucket(CommonUtils.getToken(),mOrder_sn,count,packageData(bucketHandleOrderAdapter.getData()),snum,packageShuiPiaoData(mShuipiaoAdapter.getData())).enqueue(new Callback<EntityObject<String>>() {
                @Override
                public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                    EntityObject<String> body = response.body();
                    if (body.getCode() == 200){
                        finish();
                        //发消息刷新回收杂桶的数据
                        EventBus.getDefault().post(new RecyclingEvent());
                    }else{
                        ToastUitl.showToastCustom(response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<EntityObject<String>> call, Throwable t) {

                }
            });
    }

    private void postGoPay() {
        String count = account.getText().toString().trim();
        String butiePrice = numTv.getText().toString().trim();
        if (butiePrice.equals("")){
            ToastUitl.showToastCustom("价格不能为空");
            return;
        }
        KLog.d("\n"+CommonUtils.getToken()+"\n"+mOrder_sn+"\n"+count+"\n"+packageData(bucketHandleOrderAdapter.getData())+"\n"+butiePrice);
//        RetrofitUtils.getInstances().create().goPay(CommonUtils.getToken(),mOrder_sn,count,packageData(bucketHandleOrderAdapter.getData()),butiePrice).enqueue(new Callback<EntityObject<PreparePayBean>>() {
//            @Override
//            public void onResponse(Call<EntityObject<PreparePayBean>> call, Response<EntityObject<PreparePayBean>> response) {
//                EntityObject<PreparePayBean> body = response.body();
//                if (body.getCode() == 200){
//                    PreparePayBean result = body.getResult();
//                    //1代表桶的管理二维码
//                    result.setPayFrom(1);
//                    ReceivePayActivity.startAtc(OtherBucketHandleActivity.this, result, 6);
//                    finish();
//                    //如果价格为0的时候code =800
//                }else if (body.getCode() == 800){
//                    finish();
//                    //发消息刷新回收杂桶的数据
//                    EventBus.getDefault().post(new RecyclingEvent());
//                }else {
//                    hint(body.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EntityObject<PreparePayBean>> call, Throwable t) {
//
//            }
//        });
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }


    private String packageData(List<BucketBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (BucketBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getNum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    private String packageShuiPiaoData(List<CanShuipiaoBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (CanShuipiaoBean bean : data) {
            item = new JSONObject();
            try {
                item.put("s_gid", bean.getS_gid());
                item.put("num", bean.getNum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }


}
