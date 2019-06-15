package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BaseFmAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.GoodsTypeBean;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.event.GoodsEvent;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.fragment.PurchaseBaseFragment;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 采购页面
 * created by wangsuli on 2018/8/17.
 */
public class PurchaseActivity extends BaseAct {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.rlNum)
    RelativeLayout rlNum;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    @BindView(R.id.shopping_cart)
    ImageView shoppingCart;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    private Unbinder unbinder;
    private List<String> titles = new ArrayList<>();
    private ArrayList<Object> pageList = new ArrayList<>();
    private BaseFmAdapter adapter;
    private int sid, payFrom;
    private OrderBean oldOrderBean;
    private boolean isRePayOrder = false;//是否是追加订单 追加订单 购买数量可以随意

    /**
     * @param context
     * @param sid
     * @param payFrom
     * @param oldOrder 追加订单时传 订单号
     */
    public static void startAtc(Context context, int sid, int payFrom, OrderBean oldOrder) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("payFrom", payFrom);
        intent.putExtra("oldOrder", oldOrder);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        ButterKnife.bind(this);
        CommonUtils.saveShopCart(null);
        sid = getIntent().getIntExtra("sid", -1);
        payFrom = getIntent().getIntExtra("payFrom", -1);
        oldOrderBean = (OrderBean) getIntent().getSerializableExtra("oldOrder");
        if (oldOrderBean != null) {
            isRePayOrder = true;
        }
        setTitle("采购商品");
        initView();
        getGoodsType();
    }


    private void initView() {
        adapter = new BaseFmAdapter(getSupportFragmentManager(), pageList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        List<GoodsBean> shopCart = CommonUtils.getShopCart();
        num.setText(shopCart == null ? "0" : shopCart.size() + "");
    }


    private void getGoodsType() {
        RetrofitUtils.getInstances().create().getGoodsType(sid).enqueue(new Callback<EntityObject<ArrayList<GoodsTypeBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<GoodsTypeBean>>> call, Response<EntityObject<ArrayList<GoodsTypeBean>>> response) {
                EntityObject<ArrayList<GoodsTypeBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<GoodsTypeBean> result = body.getResult();
                    if (result == null || result.size() == 0) {
                        llContent.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        llContent.setVisibility(View.VISIBLE);
                        rlEmpty.setVisibility(View.GONE);
                    }
                    for (GoodsTypeBean bean : result) {
                        titles.add(bean.getT_name());
                        pageList.add(setFragment(bean.getGtype()));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    if (body.getScode() == -200) {
                        EventBus.getDefault().post(new LoginOutEvent());
                    } else {
                        llContent.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<GoodsTypeBean>>> call, Throwable t) {
                llContent.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.VISIBLE);
            }
        });
    }


    private Fragment setFragment(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putInt("sid", sid);
        bundle.putBoolean("isRePayOrder", isRePayOrder);
        PurchaseBaseFragment orderBaseFragment = new PurchaseBaseFragment();
        orderBaseFragment.setArguments(bundle);
        return orderBaseFragment;
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(GoodsEvent event) {
        num.setText(event.count + "");
    }


    @OnClick(R.id.rlNum)
    public void onViewClicked() {
        ShoppingCartActivity.startAtc(this, sid, payFrom, oldOrderBean);
    }
}
