package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.ShoppingCartAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.dialog.HintDialog;
import com.shuiwangzhijia.wuliu.event.GoodsEvent;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 购物车页面
 * created by wangsuli on 2018/8/17.
 */
public class ShoppingCartActivity extends BaseAct implements ShoppingCartAdapter.OnViewItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.allBtn)
    CheckBox allBtn;
    @BindView(R.id.hintBtn)
    TextView hintBtn;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.payBtn)
    TextView payBtn;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private ShoppingCartAdapter mShoppingCartAdapter;
    private List<GoodsBean> buyData = new ArrayList<>();
    private boolean isPay = false;
    private Double total;
    private int payFrom, sid;//店id
    private boolean isDelete = false;

    public static void startAtc(Context context, int sid, int payFrom, OrderBean oldOrder) {
        Intent intent = new Intent(context, ShoppingCartActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("payFrom", payFrom);
        intent.putExtra("oldOrder", oldOrder);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_cart);
        ButterKnife.bind(this);
        sid = getIntent().getIntExtra("sid", -1);
        payFrom = getIntent().getIntExtra("payFrom", -1);
        setTitle("购物车");
        setRightTitle("编辑");
        setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDelete = !isDelete;
                if (isDelete) {
                    setRightTitle("完成");
                    setRightTextColor(R.color.color_ff8331);
                    payBtn.setText("删除所选");
                    money.setVisibility(View.INVISIBLE);
                } else {
                    setRightTitle("编辑");
                    setRightTextColor(R.color.color_ffffff);
                    payBtn.setText("立即结算");
                    money.setVisibility(View.VISIBLE);
                    money.setText("￥" + 0);
                    calculate();
                }
            }
        });
        initRecyclerView();

    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg));
        mRecyclerView.addItemDecoration(divider);
        mShoppingCartAdapter = new ShoppingCartAdapter(this);
        mShoppingCartAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mShoppingCartAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        List<GoodsBean> shopCart = CommonUtils.getShopCart();
        if (shopCart == null || shopCart.size() == 0) {
            rlEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            shopCart = new ArrayList<>();
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            rlEmpty.setVisibility(View.GONE);
        }
        mShoppingCartAdapter.setData(shopCart);
        calculate();

    }

    @OnClick({R.id.allBtn, R.id.payBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.allBtn:
                List<GoodsBean> data = mShoppingCartAdapter.getData();
                for (GoodsBean bean : data) {
                    bean.setCheck(allBtn.isChecked());
                }
                mShoppingCartAdapter.setData(data);
                calculate();
                break;
            case R.id.payBtn:
                if (isDelete) {
                    List<GoodsBean> newList = new ArrayList<>();
                    List<GoodsBean> list = mShoppingCartAdapter.getData();
                    for (GoodsBean bean : list) {
                        if (!bean.isCheck()) {
                            newList.add(bean);
                        }
                    }
                    allBtn.setChecked(false);
                    mShoppingCartAdapter.setData(newList);
                    CommonUtils.saveShopCart(newList);
                } else {
                    if (isPay) {
                        CommonUtils.saveBuyGoodsList(buyData);
                        ConfirmOrderActivity.startAtc(this, buyData, total + "", sid, payFrom, (OrderBean) getIntent().getSerializableExtra("oldOrder"));
                    } else {
                        hint("请选择购买的商品!");
                    }
                }

                break;
        }
    }

    @Override
    public void onSelectClick(int position) {
        GoodsBean item = mShoppingCartAdapter.getItem(position);
        boolean check = item.isCheck();
        item.setCheck(!check);
        mShoppingCartAdapter.notifyDataSetChanged();
        if (!isDelete) {
            calculate();
        }
    }

    private void calculate() {
        buyData.clear();
        List<GoodsBean> data = mShoppingCartAdapter.getData();
        total = 0.0;
        boolean isAll = true;
        for (GoodsBean bean : data) {
            if (bean.isCheck()) {
                isPay = true;
                total = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getCount()), total);
                buyData.add(bean);
            } else {
                isAll = false;
            }
        }
        allBtn.setChecked(isAll);
        money.setText("￥" + total);
        CommonUtils.saveShopCart(data);
        EventBus.getDefault().post(new GoodsEvent(data.size()));
    }

    @Override
    public void onCutClick(final int position) {
        GoodsBean item = mShoppingCartAdapter.getItem(position);
        int count = item.getCount() - 1;
        if (count < 1) {
//            hint("购买数量不能低于最低购买量");
            HintDialog hintDialog = new HintDialog(this, "确定删除商品？", new HintDialog.OnConfirmClickListener() {
                @Override
                public void onConfirm(Dialog dialog) {
                    dialog.dismiss();
                    mShoppingCartAdapter.deleteItem(position);
                }
            });
            hintDialog.show();

        } else {
            item.setCount(count);
            mShoppingCartAdapter.notifyDataSetChanged();
        }
        calculate();
    }

    @Override
    public void onAddClick(int position) {
        GoodsBean item = mShoppingCartAdapter.getItem(position);
        item.setCount(item.getCount() + 1);
        mShoppingCartAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void onDeleteClick(final int position) {
        HintDialog hintDialog = new HintDialog(this, "确定删除商品？", new HintDialog.OnConfirmClickListener() {
            @Override
            public void onConfirm(Dialog dialog) {
                dialog.dismiss();
                mShoppingCartAdapter.deleteItem(position);
                calculate();
            }
        });
        hintDialog.show();
    }

    @Override
    public void doEditCount(int position, int count) {
        GoodsBean item = mShoppingCartAdapter.getItem(position);
        if (count < 1) {
            hint("购买数量不能低于最低购买量!");
            return;
        }
        item.setCount(count);
        mShoppingCartAdapter.notifyDataSetChanged();
        calculate();
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        List<GoodsBean> shopCart = CommonUtils.getShopCart();
        if (shopCart == null) {
            shopCart = new ArrayList<>();
        }
        mShoppingCartAdapter.setData(shopCart);
        calculate();
    }

}
