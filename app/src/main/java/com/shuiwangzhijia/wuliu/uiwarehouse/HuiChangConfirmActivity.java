package com.shuiwangzhijia.wuliu.uiwarehouse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapterwarehouse.HcActualAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.HcHuiTongAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.HcTuiShuiAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.ZiyingAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;

import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.beanwarehouse.WareHouseOperationBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseOutOrderBean;
import com.shuiwangzhijia.wuliu.dialogwarehouse.EditPurchaseAmountDialog;
import com.shuiwangzhijia.wuliu.dialogwarehouse.TuishuiBucketDialog;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.eventwarehouse.HuitongEvent;
import com.shuiwangzhijia.wuliu.eventwarehouse.TuishuiEvent;
import com.shuiwangzhijia.wuliu.eventwarehouse.ZiyingEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@BaseViewInject(contentViewId = R.layout.activity_hui_chang_confirm,title = "操作订单")
public class HuiChangConfirmActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.orderId)
    TextView mOrderId;
    @BindView(R.id.needTiHuo)
    TextView mNeedTiHuo;
    @BindView(R.id.numRecyclerView)
    RecyclerView mNumRecyclerView;
    @BindView(R.id.realTitle)
    TextView mRealTitle;
    @BindView(R.id.realRecyclerView)
    RecyclerView mRealRecyclerView;
    @BindView(R.id.editSelfBucket)
    TextView mEditSelfBucket;
    @BindView(R.id.selfBucketRecyclerView)
    RecyclerView mSelfBucketRecyclerView;
    @BindView(R.id.remove)
    RelativeLayout mRemove;
    @BindView(R.id.account)
    TextView mAccount;
    @BindView(R.id.add)
    RelativeLayout mAdd;
    @BindView(R.id.llReturnWareHouse)
    LinearLayout mLlReturnWareHouse;
    @BindView(R.id.sureBtn)
    TextView mSureBtn;
    @BindView(R.id.huiShouEdit)
    TextView mHuiShouEdit;
    @BindView(R.id.ziyingrv)
    RecyclerView mZiyingrv;
    @BindView(R.id.editHuishou)
    TextView mEditHuishou;
    @BindView(R.id.po_remove)
    RelativeLayout mPoRemove;
    @BindView(R.id.po_account)
    TextView mPoAccount;
    @BindView(R.id.po_add)
    RelativeLayout mPoAdd;
    @BindView(R.id.order_type)
    TextView mOrderType;
    private HcActualAdapter mActualAdapter;
    private HcHuiTongAdapter mAdapter;
    private ZiyingAdapter mZiyingAdapter;
    private WareHouseOperationBean mResult;
    private WarehouseOutOrderBean mOrderBean;
    private String outOrder;
    private ArrayList<WarehouseGoodsBean> mHuiTongData;
    private ArrayList<WarehouseGoodsBean> mTuiShuiData;
    private HcTuiShuiAdapter mHcTuiShuiAdapter;
    private ArrayList<WarehouseGoodsBean> mZybucket;
    private ArrayList<WarehouseGoodsBean> mZiyingData;
    private ArrayList<WarehouseGoodsBean> mData;

    public static void startAct(Context context) {
        Intent in = new Intent(context, HuiChangConfirmActivity.class);
        context.startActivity(in);
    }

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
        mOrderBean = (WarehouseOutOrderBean) getIntent().getSerializableExtra("orderData");
        outOrder = mOrderBean.getOut_order();
        getList();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void getList() {
        RetrofitUtils.getInstances().create().warehouseGetWareHouseOperation(outOrder).enqueue(new Callback<EntityObject<WareHouseOperationBean>>() {
            @Override
            public void onResponse(Call<EntityObject<WareHouseOperationBean>> call, Response<EntityObject<WareHouseOperationBean>> response) {
                EntityObject<WareHouseOperationBean> body = response.body();
                if (body.getCode() == 200) {
                    mResult = body.getResult();
                    int order_type = mResult.getOrder_type();
                    mName.setText("提货人：" + mResult.getUname());
                    mOrderId.setText("出货单号：" + mResult.getOut_order());
                    mNeedTiHuo.setText("需提货" + mResult.getSum() + "桶");
                    if (order_type == 0){
                        mOrderType.setText("配送订单");
                    }else if (order_type == 1){
                        mOrderType.setText("自提订单");
                    }
                    mAccount.setText(mResult.getMiscellaneous());
                    mPoAccount.setText(mResult.getPobucket() + "");
                    List<WarehouseGoodsBean> actual = mResult.getActual();        //实际
                    List<WarehouseGoodsBean> back = mResult.getBack();           //回桶
                    List<WarehouseGoodsBean> refund = mResult.getRefund();        //退水
                    //自营桶数据
                    mZybucket = mResult.getZybucket();
                    initActualRecycleView(actual);
                    initBackRecycleView(back);
                    initRefundRecycleView(refund);
                    initZiyingTongRv(mZybucket);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<WareHouseOperationBean>> call, Throwable t) {

            }
        });

        RetrofitUtils.getInstances().create().warehouseAddCouldOutGoodsList(outOrder).enqueue(new Callback<EntityObject<ArrayList<WarehouseGoodsBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<WarehouseGoodsBean>>> call, Response<EntityObject<ArrayList<WarehouseGoodsBean>>> response) {
                EntityObject<ArrayList<WarehouseGoodsBean>> body = response.body();
                if (body.getCode() == 200){
                    mData = body.getResult();
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<WarehouseGoodsBean>>> call, Throwable t) {

            }
        });
    }

    private void initZiyingTongRv(List<WarehouseGoodsBean> zybucket) {
        mZiyingrv.setLayoutManager(new LinearLayoutManager(this));
        mZiyingrv.setHasFixedSize(true);
        mZiyingAdapter = new ZiyingAdapter(this, zybucket);
        mZiyingAdapter.setOnViewItemClickListener(new ZiyingAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                WarehouseGoodsBean item = mZiyingAdapter.getItem(position);
                item.setNum(count);
                mZiyingAdapter.notifyDataSetChanged();
            }
        });
        mZiyingrv.setAdapter(mZiyingAdapter);
    }

    private void initRefundRecycleView(List<WarehouseGoodsBean> refund) {
        mSelfBucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSelfBucketRecyclerView.setHasFixedSize(true);
        mHcTuiShuiAdapter = new HcTuiShuiAdapter(this, refund);
        mHcTuiShuiAdapter.setOnViewItemClickListener(new HcTuiShuiAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                WarehouseGoodsBean item = mHcTuiShuiAdapter.getItem(position);
                item.setNum(count);
                mHcTuiShuiAdapter.notifyDataSetChanged();
            }
        });
        mSelfBucketRecyclerView.setAdapter(mHcTuiShuiAdapter);
    }

    private void initBackRecycleView(List<WarehouseGoodsBean> back) {
        mRealRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRealRecyclerView.setHasFixedSize(true);
        mAdapter = new HcHuiTongAdapter(this, back);
        mAdapter.setOnViewItemClickListener(new HcHuiTongAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                WarehouseGoodsBean item = mAdapter.getItem(position);
                item.setNum(count);
                mAdapter.notifyDataSetChanged();
            }
        });

        mRealRecyclerView.setAdapter(mAdapter);
    }

    private void initActualRecycleView(List<WarehouseGoodsBean> actual) {
        mNumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNumRecyclerView.setHasFixedSize(true);
        mActualAdapter = new HcActualAdapter(this, actual);
        mNumRecyclerView.setAdapter(mActualAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(HuitongEvent event) {
        mHuiTongData = event.getData();
        initBackRecycleView(mHuiTongData);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(TuishuiEvent event) {
        mTuiShuiData = event.getData();
        initRefundRecycleView(mTuiShuiData);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(ZiyingEvent event) {
        mZiyingData = event.getData();
        initZiyingTongRv(mZiyingData);
    }

    @OnClick({R.id.name, R.id.orderId, R.id.needTiHuo, R.id.realTitle, R.id.editSelfBucket, R.id.remove, R.id.account, R.id.add, R.id.llReturnWareHouse, R.id.sureBtn,
            R.id.huiShouEdit, R.id.editHuishou, R.id.po_add, R.id.po_account, R.id.po_remove})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.name:
                break;
            case R.id.orderId:
                break;
            case R.id.needTiHuo:
                break;
            case R.id.realTitle:
                break;
            case R.id.huiShouEdit:
//                EditHandleActivity.startAct(this, 0, outOrder,mHuiTongData);
                TuishuiBucketDialog dialog = new TuishuiBucketDialog(this, mData, new TuishuiBucketDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<WarehouseGoodsBean> data) {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setBname(data.get(i).getGname());
                            data.get(i).setBid(data.get(i).getGid()+"");
                        }
                        dialog.dismiss();
                        mAdapter.setData((ArrayList<WarehouseGoodsBean>) data);
                    }
                });
                dialog.show();
                break;
            case R.id.editSelfBucket:
//                EditHandleActivity.startAct(this, 1, outOrder,mTuiShuiData);
                TuishuiBucketDialog tuishuiDialog = new TuishuiBucketDialog(this, mData, new TuishuiBucketDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<WarehouseGoodsBean> data) {
                        dialog.dismiss();
                        mHcTuiShuiAdapter.setData(data);
                    }
                });
                tuishuiDialog.show();
                break;
            case R.id.editHuishou:
//                EditHandleActivity.startActb(this, 2, mZybucket, outOrder,mZiyingData);
                TuishuiBucketDialog huiShouDialog = new TuishuiBucketDialog(this, mData, new TuishuiBucketDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<WarehouseGoodsBean> data) {
                        dialog.dismiss();
                        mZiyingAdapter.setData(data);
                    }
                });
                huiShouDialog.show();
                break;
            case R.id.remove:
                int count = Integer.parseInt(mAccount.getText().toString());
                if (count <= 0) {
                    return;
                }
                count--;
                mAccount.setText(count + "");
                break;
            case R.id.account:
                int num = Integer.parseInt(mAccount.getText().toString());
                EditPurchaseAmountDialog zatongDialog = new EditPurchaseAmountDialog(this, num, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        mAccount.setText(count + "");
                    }
                });
                zatongDialog.show();
                break;
            case R.id.add:
                int countAdd = Integer.parseInt(mAccount.getText().toString());
                countAdd++;
                mAccount.setText(countAdd + "");
                break;
            case R.id.po_add:
                int poCountAdd = Integer.parseInt(mPoAccount.getText().toString());
                poCountAdd++;
                mPoAccount.setText(poCountAdd + "");
                break;
            case R.id.po_remove:
                int poAccount = Integer.parseInt(mPoAccount.getText().toString());
                if (poAccount <= 0) {
                    return;
                }
                poAccount--;
                mPoAccount.setText(poAccount + "");
                break;
            case R.id.po_account:
                int poNum = Integer.parseInt(mPoAccount.getText().toString());
                EditPurchaseAmountDialog poDialog = new EditPurchaseAmountDialog(this, poNum, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        mPoAccount.setText(count + "");
                    }
                });
                poDialog.show();
                break;
            case R.id.llReturnWareHouse:
                break;
            case R.id.sureBtn:
                post();
                break;
        }
    }

    private void post() {
        String count = mAccount.getText().toString().trim();
        String poCount = mPoAccount.getText().toString().trim();
        int num = Integer.parseInt(count);
        int poNum = Integer.parseInt(poCount);
        KLog.d("\n" + CommonUtils.getToken() +
                "\n" + outOrder +
                "\n" + packageData(mActualAdapter.getData()) +
                "\n" + packageOtherData(mAdapter.getData()) +
                "\n" + packageData(mHcTuiShuiAdapter.getData()) +
                "\n" + packageData(mZiyingAdapter.getData()) +
                "\n" + num);
        RetrofitUtils.getInstances().create().warehouseGetBackSure(CommonUtils.getToken(), outOrder, packageData(mActualAdapter.getData()), packageOtherData(mAdapter.getData())
                , packageData(mHcTuiShuiAdapter.getData()), packageData(mZiyingAdapter.getData()), poNum, num).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 200) {
                    ToastUitl.showToastCustom("回仓确认成功");
                    finish();
                    EventBus.getDefault().post(new MainEvent(3));
                } else {
                    ToastUitl.showToastCustom(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });
    }

    private String packageOtherData(List<WarehouseGoodsBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (WarehouseGoodsBean bean : data) {
            item = new JSONObject();
            try {
                item.put("bid", bean.getBid());
                item.put("num", bean.getNum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    private String packageData(List<WarehouseGoodsBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (WarehouseGoodsBean bean : data) {
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
}
