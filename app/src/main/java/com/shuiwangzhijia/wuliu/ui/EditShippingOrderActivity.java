package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.EditAdditionalOrderAdapter;
import com.shuiwangzhijia.wuliu.adapter.EditShippingOrderAdapter;
import com.shuiwangzhijia.wuliu.adapter.OperationHuitongAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.CalculateBean;
import com.shuiwangzhijia.wuliu.bean.EditDeliveryOrderBean;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.dialog.AddShoppDialog;
import com.shuiwangzhijia.wuliu.event.EditOrderEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
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
@BaseViewInject(contentViewId = R.layout.activity_edit_shipping_order,title = "编辑提货单")
public class EditShippingOrderActivity extends BaseActivity {

    @BindView(R.id.order_id)
    TextView mOrderId;
    @BindView(R.id.data)
    TextView mData;
    @BindView(R.id.actual_rv)
    RecyclerView mActualRv;
    @BindView(R.id.actual_number)
    TextView mActualNumber;
    @BindView(R.id.edit_number)
    TextView mEditNumber;
    @BindView(R.id.additional_rv)
    RecyclerView mAdditionalRv;
    @BindView(R.id.additional_number)
    TextView mAdditionalNumber;
    @BindView(R.id.add_additional)
    TextView mAddAdditional;
    @BindView(R.id.rv)
    ScrollView mRv;
    @BindView(R.id.pending_order_total)
    TextView mPendingOrderTotal;
    @BindView(R.id.pending_order_surnbtn)
    TextView mPendingOrderSurnbtn;
    @BindView(R.id.pendding_order_bottom)
    RelativeLayout mPenddingOrderBottom;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.rlEmpty)
    RelativeLayout mRlEmpty;
    private String mOut_order;
    private ArrayList<GoodsBean> mAddShopData;
    private EditAdditionalOrderAdapter mAdapter;
    private int mSum;
    private String mGoods_num;

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
        addShop();
        getList();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void getList() {
        showLoadingDialog();
        RetrofitUtils.getInstances().create().preOutOrderDetail().enqueue(new Callback<EntityObject<EditDeliveryOrderBean>>() {
            @Override
            public void onResponse(Call<EntityObject<EditDeliveryOrderBean>> call, Response<EntityObject<EditDeliveryOrderBean>> response) {
                dismissLoadingDialog();
                EntityObject<EditDeliveryOrderBean> body = response.body();
                if (body.getCode() == 200){
                    EditDeliveryOrderBean result = body.getResult();
                    int status_type = result.getStatus_type();
                    if (status_type == 0){
                        mAddAdditional.setVisibility(View.VISIBLE);
                    }else {
                        mAddAdditional.setVisibility(View.GONE);
                    }
                    mOut_order = result.getOut_order();
                    mSum = result.getSum();
                    mGoods_num = result.getGoods_num();
                    mOrderId.setText("出货单号："+ mOut_order);
                    mData.setText("下单时间："+ DateUtils.getFormatDateStr(result.getCreate_time()*1000L));
                    List<GoodsBean> goods = result.getGoods();
                    List<GoodsBean> goods_additional = result.getGoods_additional();
                    initGoodRv(goods);
                    initAdditionalRv(goods_additional);
                    mActualNumber.setText("合计："+mGoods_num+"(桶)");
                  //  setTextStyle(mActualNumber,"合计：",mGoods_num,"(桶)");
                    mAddAdditional.setText("合计："+result.getGoods_additional_num()+"(桶)");
                   // setTextStyle(mAdditionalNumber,"合计：",result.getGoods_additional_num(),"(桶)");
                    mPendingOrderTotal.setText("共"+mSum+"(桶/箱)");
                    //setTextStyle(mPendingOrderTotal,"共",mSum+"","(桶/箱)");
                }
            }

            @Override
            public void onFailure(Call<EntityObject<EditDeliveryOrderBean>> call, Throwable t) {

            }
        });
    }

    private void setTextStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_ff3939)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(24, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    private void initAdditionalRv(List<GoodsBean> goods_additional) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdditionalRv.setLayoutManager(manager);
        mAdapter = new EditAdditionalOrderAdapter(this, goods_additional, 2);
        mAdapter.setOnViewItemClickListener(new OperationHuitongAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                GoodsBean item = mAdapter.getItem(position);
//                if (count < 1){
//                    hint("购买数量不能低于最低购买量!");
//                    return;
//                }
                item.setAnum(count);
                mAdapter.notifyDataSetChanged();
                calculateTotal();
            }
        });
        mAdditionalRv.setAdapter(mAdapter);
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }

    private void initGoodRv(List<GoodsBean> goods) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mActualRv.setLayoutManager(manager);
        EditShippingOrderAdapter adapter = new EditShippingOrderAdapter(this, goods, 1);
        mActualRv.setAdapter(adapter);
    }

    @OnClick({R.id.edit_number, R.id.add_additional, R.id.pending_order_surnbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_number:
                PendingOrderActivity.startAct(this, 2, mOut_order);
                break;
            case R.id.add_additional:
                AddShoppDialog dialog = new AddShoppDialog(this,mAddShopData , new AddShoppDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<GoodsBean> data) {
                        dialog.dismiss();
                        mAdapter.setData(data);
                        calculateTotal();
                    }
                });
                dialog.show();
                break;
            case R.id.pending_order_surnbtn:
                sureShipment();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(CalculateBean event) {
        calculateTotal();
    }

    private void calculateTotal() {
        int total = 0;
        List<GoodsBean> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            total += data.get(i).getAnum();
        }
        int add = CalculateUtils.add(Integer.parseInt(mGoods_num), total);
        mAdditionalNumber.setText("合计："+total+"(桶)");
       // setTextStyle(mAdditionalNumber,"合计：",total+"","(桶)");
        mPendingOrderTotal.setText("合计："+add+"(桶/箱)");
       // setTextStyle(mPendingOrderTotal,"共",add+"","(桶/箱)");
    }

    private void sureShipment() {
        KLog.d("\n"+CommonUtils.getToken()+"\n"+mOut_order+"\n"+packageAdditionalData(mAdapter.getData()));
        RetrofitUtils.getInstances().create().additionalGoods(CommonUtils.getToken(),mOut_order,packageAdditionalData(mAdapter.getData())).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 200){
                    hint("添加成功");
                    finish();
                    EventBus.getDefault().post(new EditOrderEvent());
                }else if (body.getCode() == 700){
                    hint(body.getMsg());
                    getList();
                }else {
                    hint(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });
    }

    private String packageAdditionalData(List<GoodsBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (GoodsBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getAnum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    private void addShop() {
        RetrofitUtils.getInstances().create().additionalGoodsList().enqueue(new Callback<EntityObject<ArrayList<GoodsBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<GoodsBean>>> call, Response<EntityObject<ArrayList<GoodsBean>>> response) {
                EntityObject<ArrayList<GoodsBean>> body = response.body();
                if (body.getCode() == 200){
                    mAddShopData = body.getResult();
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<GoodsBean>>> call, Throwable t) {

            }
        });
    }
}
