package com.shuiwangzhijia.wuliu.uiwarehouse;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapterwarehouse.NumberAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.RealAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OutDetailsBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseOutOrderBean;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandleOrderActivityForSure extends BaseAct {

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
    @BindView(R.id.sureBtn)
    TextView mSureBtn;
    private WarehouseOutOrderBean mOrderBean;
    private String outOrder;
    private NumberAdapter mNumberAdapter;
    private List<OrderDetailBean.NeedBean> mGoods;
    private List<OrderDetailBean.ActualBean> mGoodsBeanXES;
    private RealAdapter mRealAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_order_for_sure);
        ButterKnife.bind(this);
        setTitle("操作订单");
        mOrderBean = (WarehouseOutOrderBean) getIntent().getSerializableExtra("orderData");
        outOrder = mOrderBean.getOut_order();
        getList();
    }

    private void getList() {
        RetrofitUtils.getInstances().create().warehouseGetOutDetailsInfo(outOrder).enqueue(new Callback<EntityObject<OutDetailsBean>>() {

            private List<OutDetailsBean.ActualBean.GoodsBeanX> mActualGoods;

            @Override
            public void onResponse(Call<EntityObject<OutDetailsBean>> call, Response<EntityObject<OutDetailsBean>> response) {
                EntityObject<OutDetailsBean> body = response.body();
                if (body.getCode() == 200) {
                    OutDetailsBean result = body.getResult();
                    mName.setText("提货人：" + result.getUname());
                    mOrderId.setText("出货单号：" + result.getOut_order());
                    mNeedTiHuo.setText("需提货" + result.getSum() + "桶");
                    int count = result.getActual().getCount();
                    mRealTitle.setText("实际出货" + "(" + count + ")");
                    mGoods = result.getNeed().getGoods();
                    mGoodsBeanXES = result.getActual().getGoods();
                    initNeedRecycle(mGoods);
                    initActualRecycle(mGoodsBeanXES);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<OutDetailsBean>> call, Throwable t) {

            }
        });
    }

    private void initActualRecycle(List<OrderDetailBean.ActualBean> actual) {
        mRealRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRealRecyclerView.setHasFixedSize(true);
        mRealAdapter = new RealAdapter(this, actual);
//        mRealAdapter.setOnViewItemClickListener(new RealAdapter.OnViewItemClickListener() {
//            @Override
//            public void doEditClick(int position, int count) {
//                OrderDetailBean.ActualBean item = mRealAdapter.getItem(position);
//                if (count <= 0){
//                    return;
//                }
//                item.setNum(count);
//                mRealAdapter.notifyDataSetChanged();
//            }
//        });
        mRealRecyclerView.setAdapter(mRealAdapter);
    }

    private void initNeedRecycle(List<OrderDetailBean.NeedBean> need) {
        mNumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNumRecyclerView.setHasFixedSize(true);
        mNumberAdapter = new NumberAdapter(this, need);
        mNumRecyclerView.setAdapter(mNumberAdapter);
    }

    @OnClick(R.id.sureBtn)
    public void onViewClicked() {
        post();
    }

    private void post() {
        RetrofitUtils.getInstances().create().warehouseConfirmShipment(CommonUtils.getToken(),outOrder,packageData(mRealAdapter.getData())).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 200){
                    ToastUitl.showToastCustom("出货成功");
                    finish();
                    EventBus.getDefault().post(new MainEvent(2));
                }else{
                    ToastUitl.showToastCustom(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });
    }

    private String packageData(List<OrderDetailBean.ActualBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderDetailBean.ActualBean bean : data) {
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
