package com.shuiwangzhijia.wuliu.fragment;

import android.app.Dialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.CreateOrderAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseLazyFragment;
import com.shuiwangzhijia.wuliu.bean.DefaultStoreBean;
import com.shuiwangzhijia.wuliu.bean.SendOrderBean;
import com.shuiwangzhijia.wuliu.bean.StoreListBean;
import com.shuiwangzhijia.wuliu.dialog.CreateOrder;
import com.shuiwangzhijia.wuliu.event.DeliverOrderEvent;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.event.StatusBean;
import com.shuiwangzhijia.wuliu.event.pendingEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.PendingOrderActivity;
import com.shuiwangzhijia.wuliu.utils.MeasureUtil;
import com.shuiwangzhijia.wuliu.utils.SpacesItemDecoration;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

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

@BaseViewInject(contentViewId = R.layout.fragment_create_order)
public class CreateOrderFragment extends BaseLazyFragment {
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.checkBtn)
    TextView checkBtn;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    @BindView(R.id.create_order_rv)
    RecyclerView mCreateOrderRv;
    @BindView(R.id.addOrder)
    TextView mAddOrder;
    @BindView(R.id.shengChengOrder)
    TextView mShengChengOrder;
    @BindView(R.id.create_order_lin)
    LinearLayout mCreateOrderLin;
    @BindView(R.id.create_order_ns)
    NestedScrollView mCreateOrderNs;
    private boolean hasOrder = true;
    private EntityObject<String> mBody;
    private ArrayList<StoreListBean> recycler;
    private ArrayList<String> saveOrder = new ArrayList<>();
    private String mStoreId;
    private String mOrderSn;
    private List<SendOrderBean> mData;
    private int mCid;

    @Override
    protected void initView(View view) {
        recycler = new ArrayList<>();
        initRv();
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mCreateOrderRv.setLayoutManager(manager);
      //  mCreateOrderRv.setHasFixedSize(true);
        mCreateOrderRv.addItemDecoration(new SpacesItemDecoration(mContext, MeasureUtil.dip2px(mContext,12)));
    }

    @Override
    protected void lazyLoadData() {
        initView();
        getStoreList();
    }

    @Override
    protected void initEvent() {

    }

    private void getStoreList() {
        RetrofitUtils.getInstances().create().getStoreList().enqueue(new Callback<EntityObject<ArrayList<StoreListBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<StoreListBean>>> call, Response<EntityObject<ArrayList<StoreListBean>>> response) {
                EntityObject<ArrayList<StoreListBean>> body = response.body();
                if (body != null) {
                    if (body.getCode() == 200) {
                        ArrayList<StoreListBean> result = body.getResult();
                        recycler.clear();
                        recycler.addAll(result);
                    } else {
                        if (body.getScode() == -200) {
                            EventBus.getDefault().post(new LoginOutEvent());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<StoreListBean>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        showLoadingDialog();
        RetrofitUtils.getInstances().create().getOrderIsBegin().enqueue(new Callback<EntityObject<String>>() {

            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                dismissLoadingDialog();
                mBody = response.body();
                //判断mCreateOrderNs不为null 出货单和我的快速切换的时候其没有初始化完成
                if (mBody != null && mCreateOrderNs != null) {
                    if (mBody.getCode() == 200) {
                        mCreateOrderLin.setVisibility(View.GONE);
                        mCreateOrderNs.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                        hint.setText("您有一个订单正在进行，\n" + "无法创建出货订单");
                        checkBtn.setText("查看订单");
                    } else if (mBody.getCode() == 300) {
                        mCreateOrderNs.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                        hint.setText("您还没有出货订单~");
                        checkBtn.setText("添加订单");
                        initCreateData();
                    } else {
                        mCreateOrderNs.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                        hint.setText("您还没有出货订单~");
                        checkBtn.setText("添加订单");
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });

        RetrofitUtils.getInstances().create().isDefaultStore().enqueue(new Callback<EntityObject<DefaultStoreBean>>() {
            @Override
            public void onResponse(Call<EntityObject<DefaultStoreBean>> call, Response<EntityObject<DefaultStoreBean>> response) {
                EntityObject<DefaultStoreBean> body = response.body();
                if (body.getCode() == 200) {
                    DefaultStoreBean result = body.getResult();
                    mCid = result.getId();
                }
            }

            @Override
            public void onFailure(Call<EntityObject<DefaultStoreBean>> call, Throwable t) {

            }
        });
    }

    //添加订单页面传回来的
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(pendingEvent event) {
        StringBuilder builder = new StringBuilder();
        mData = event.getData();
        initCreateData();
        for (int i = 0; i < mData.size(); i++) {
            builder.append(mData.get(i).getOrder_sn()).append(",");
        }
        mOrderSn = builder.deleteCharAt(builder.length() - 1).toString();
    }

    public void initCreateData() {
        if (mData != null && mData.size() != 0) {
            mCreateOrderNs.setVisibility(View.VISIBLE);
            rlEmpty.setVisibility(View.GONE);
            initData(mData);
        } else {
            mCreateOrderNs.setVisibility(View.GONE);
            rlEmpty.setVisibility(View.VISIBLE);
            hint.setText("您还没有出货订单哦~");
            checkBtn.setText("添加订单");
        }
    }

    ;

    private void initData(List<SendOrderBean> data) {
        if (data != null) {
            rlEmpty.setVisibility(View.GONE);
            mCreateOrderRv.setVisibility(View.VISIBLE);
            mCreateOrderLin.setVisibility(View.VISIBLE);
        }
        CreateOrderAdapter adapter = new CreateOrderAdapter(getContext(), data);
        mCreateOrderRv.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(StatusBean event) {
        mCreateOrderNs.setVisibility(View.GONE);
        rlEmpty.setVisibility(View.VISIBLE);
        hint.setText("您还没有出货订单哦~");
        checkBtn.setText("添加订单");
    }

    private void getShipmentRequest(String cid) {
        RetrofitUtils.getInstances().create().makeOutOrder(mOrderSn, "", cid).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 700) {
                    ToastUitl.showToastCustom(body.getMsg());
                    return;
                }
                if (body.getCode() == 200) {
                    mData = null;
                    ToastUitl.showToastCustom("创建出货单成功");
                    EventBus.getDefault().post(new DeliverOrderEvent(1));
                    mCreateOrderNs.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                    hint.setText("您有一个订单正在进行，\n" + "无法创建出货订单");
                    checkBtn.setText("查看订单");
                } else {
                    ToastUitl.showToastCustom(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.addOrder, R.id.shengChengOrder, R.id.checkBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addOrder:
                PendingOrderActivity.startActb(getContext(), 1, "", mData);
                break;
            case R.id.shengChengOrder:
                //有默认仓库不需要选择仓库端，直接生成出货单
                if (mCid != 0) {
                    getShipmentRequest(mCid + "");
                } else {
                    CreateOrder dialog = new CreateOrder(getContext(), recycler, new CreateOrder.OnConfirmClickListener() {
                        @Override
                        public void onConfirm(Dialog dialog, int cid) {
                            dialog.dismiss();
                            getShipmentRequest(cid + "");
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.checkBtn:
                if (checkBtn.getText().toString().equals("查看订单")) {
                    EventBus.getDefault().post(new DeliverOrderEvent(1));
                } else {
                    PendingOrderActivity.startAct(getContext(), 1, "");
                }
                break;
        }
    }
}
