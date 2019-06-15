package com.shuiwangzhijia.wuliu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BaseFmAdapter;
import com.shuiwangzhijia.wuliu.adapter.PurchaseAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.event.OrderListFlashEvent;
import com.shuiwangzhijia.wuliu.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 订单管理
 * created by wangsuli on 2018/8/21.
 */
public class OrderManageActivity extends BaseAct implements ViewPager.OnPageChangeListener {
    @BindView(R.id.saleBtn)
    TextView saleBtn;
    @BindView(R.id.buyBtn)
    TextView buyBtn;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private List<String> titles = new ArrayList<>();
    private ArrayList<Object> pageList = new ArrayList<>();
    private BaseFmAdapter adapter;
    private boolean isBuy = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manage);
        ButterKnife.bind(this);
        setTitle("订单管理");
        initView();
        initData(isBuy);
    }

    public void initView() {
        adapter = new BaseFmAdapter(getSupportFragmentManager(), pageList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * @param flag true 购买订单 否 卖水订单列表
     */
    private void initData(boolean flag) {
        pageList = new ArrayList<>();
        pageList.add(setFragment(PurchaseAdapter.TYPE_ALL, flag));
        pageList.add(setFragment(PurchaseAdapter.TYPE_ALL_1, flag));
        pageList.add(setFragment(PurchaseAdapter.TYPE_ALL_2, flag));
        pageList.add(setFragment(PurchaseAdapter.TYPE_ALL_3, flag));
        titles = Arrays.asList(new String[]{"新订单", "待配送", "配送中", "已完成"});
        if (flag) {
            titles.set(0, "待支付");
            titles.set(1, "已支付");
            saleBtn.setSelected(false);
            buyBtn.setSelected(true);
        } else {
            titles.set(0, "新订单");
            titles.set(1, "待配送");
            saleBtn.setSelected(true);
            buyBtn.setSelected(false);
        }
        adapter.setList(pageList, titles);
    }

    private Fragment setFragment(int type, boolean flag) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putBoolean("isBuy", flag);
        OrderFragment orderBaseFragment = new OrderFragment();
        orderBaseFragment.setArguments(bundle);
        return orderBaseFragment;
    }

    @OnClick({R.id.saleBtn, R.id.buyBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.saleBtn:
                isBuy = false;
                initData(isBuy);
                viewPager.setCurrentItem(0);
                EventBus.getDefault().post(new OrderListFlashEvent(isBuy,0));
                break;
            case R.id.buyBtn:
                isBuy = true;
                initData(isBuy);
                viewPager.setCurrentItem(0);
                EventBus.getDefault().post(new OrderListFlashEvent(isBuy,0));
                break;
        }
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        EventBus.getDefault().post(new OrderListFlashEvent(isBuy,position));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
