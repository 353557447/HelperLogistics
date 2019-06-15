package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.ProcuementGoodsAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.dialog.HintDialog;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.MeasureUtil;
import com.shuiwangzhijia.wuliu.utils.SpacesItemDecoration;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@BaseViewInject(contentViewId = R.layout.activity_procurement_goods,title = "采购商品")
public class ProcurementGoodsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ProcuementGoodsAdapter.OnViewItemClickListener {
    private static final String TAG = "ProcurementGoodsActivit";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.rlEmpty)
    RelativeLayout mRlEmpty;
    @BindView(R.id.allBtn)
    TextView mAllBtn;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.payBtn)
    TextView mPayBtn;
    @BindView(R.id.bottom)
    LinearLayout mBottom;
    private int payFrom,sid;    //payFrom 为2
    private LinearLayoutManager mManager;
    private ProcuementGoodsAdapter mAdapter;
    public boolean isLoading;
    private List<GoodsBean> buyData = new ArrayList<>();
    private Double total;
    private int number;
    private boolean isPay = false;
    public static final int PageSize = 10;
    public boolean loading;
    public int page = 0;

    /**
     * @param context
     * @param sid
     * @param payFrom
     * @param oldOrder 追加订单时传 订单号
     */
    public static void startAtc(Context context, int sid, int payFrom, OrderBean oldOrder) {
        Intent intent = new Intent(context, ProcurementGoodsActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("payFrom", payFrom);
        intent.putExtra("oldOrder", oldOrder);
        context.startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                        completeSwipeRefresh();
                    }

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
        sid = getIntent().getIntExtra("sid", -1);
        payFrom = getIntent().getIntExtra("payFrom", -1);
        getGoodsType();
        initRecycleView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void initRecycleView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(R.color.color_30adfd));
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(this, MeasureUtil.dip2px(this,12)));
        mAdapter = new ProcuementGoodsAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getGoodsType() {
        RetrofitUtils.getInstances().create().showGoodsList(sid, page, PageSize).enqueue(new Callback<EntityObject<ArrayList<GoodsBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<GoodsBean>>> call, Response<EntityObject<ArrayList<GoodsBean>>> response) {
               dismissLoadingDialog();
                completeSwipeRefresh();
                EntityObject<ArrayList<GoodsBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<GoodsBean> result = body.getResult();
                    if (result == null || result.size() == 0) {
                        mRlEmpty.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        mBottom.setVisibility(View.GONE);
                        return;
                    } else {
                        mRlEmpty.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        mBottom.setVisibility(View.VISIBLE);
                        if (page == 0) {
                            mAdapter.setData(result);
                        } else {
                            mAdapter.addData(result);
                        }
                        if (result.size() < PageSize) {
                            isLoading = false;
                        } else {
                            isLoading = true;
                        }
                    }
                } else {
                    if (page == 0) {
                        mRlEmpty.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        mBottom.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<GoodsBean>>> call, Throwable t) {
                mRlEmpty.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                mBottom.setVisibility(View.GONE);
            }
        });
    }

    private void completeSwipeRefresh() {
        if (mSwipeRefreshLayout == null)
            return;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        finish();
    }

    @Override
    public void onRefresh() {
        page = -1;
        showLoadingDialog();
        new LoadDataThread().start();
    }

    @Override
    public void onAddClick(int position) {
        GoodsBean item = mAdapter.getItem(position);
        item.setCount(item.getCount() + 1);
        mAdapter.notifyDataSetChanged();
        calculate();
    }

    private void calculate() {
        buyData.clear();
        List<GoodsBean> data = mAdapter.getData();
        total = 0.0;
        number = 0;
        for (GoodsBean bean : data) {
            if (bean.getCount() > 0) {
                isPay = true;
                total = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getCount()), total);
                number += bean.getCount();
                buyData.add(bean);
            }
        }
        mAllBtn.setText("共"+number+"件");
        mMoney.setText("￥" + total);
        CommonUtils.saveShopCart(data);
    }

    @Override
    public void onCutClick(final int position) {
        GoodsBean item = mAdapter.getItem(position);
        int count = item.getCount() - 1;
        if (count < 0) {
            //            hint("购买数量不能低于最低购买量");
            HintDialog hintDialog = new HintDialog(this, "确定删除商品？", new HintDialog.OnConfirmClickListener() {
                @Override
                public void onConfirm(Dialog dialog) {
                    dialog.dismiss();
                    mAdapter.deleteItem(position);
                }
            });
            hintDialog.show();
        } else {
            item.setCount(count);
            mAdapter.notifyDataSetChanged();
        }
        calculate();
    }

    @Override
    public void doEditCount(int position, int count) {
        GoodsBean item = mAdapter.getItem(position);
        if (count < 1) {
            hint("购买数量不能低于最低购买量!");
            return;
        }
        item.setCount(count);
        mAdapter.notifyDataSetChanged();
        calculate();
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }

    @OnClick(R.id.payBtn)
    public void onViewClicked() {
        if (isPay) {
            CommonUtils.saveBuyGoodsList(buyData);
            ConfirmOrderActivity.startAtc(this, buyData, total + "", sid, payFrom, (OrderBean) getIntent().getSerializableExtra("oldOrder"));
            finish();
        } else {
            hint("请选择购买的商品!");
        }
    }

    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            page = page + 1;
            getGoodsType();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }
}
