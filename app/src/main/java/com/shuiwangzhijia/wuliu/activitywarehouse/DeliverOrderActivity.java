package com.shuiwangzhijia.wuliu.activitywarehouse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BaseFragmentPagerAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.fragmentwarehouse.OrderFragment;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import java.util.ArrayList;

import butterknife.BindView;

//配送订单
@BaseViewInject(contentViewId = R.layout.activity_deliver_order, title = "配送订单")
public class DeliverOrderActivity extends BaseActivity {
    @BindView(R.id.tab)
    XTabLayout mTab;
    @BindView(R.id.vp)
    ViewPager mVp;
    private int mSelectedPosition;

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        mSelectedPosition = bundle.getInt("position");
        mBaseBar.setBarListener(new BaseBar.BarListener() {
            @Override
            public void onLeftClick() {
                closeActivity();
            }

            @Override
            public void onRightClick() {

            }
        });

        //初始化mTab
        initTab();
        //初始化mVp
        initVp();
        //关联mTab和mVp
        mTab.setupWithViewPager(mVp);
        if (bundle != null) {
            mVp.setCurrentItem(mSelectedPosition);
        }
    }


    private void initTab() {
        mTab.setxTabDisplayNum(3);
        mTab.addTab(mTab.newTab().setText("待提货"), true);
        mTab.addTab(mTab.newTab().setText("配送中"), false);
        mTab.addTab(mTab.newTab().setText("已完成"), false);
    }

    private void initVp() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(setFragment(0));
        fragmentList.add(setFragment(1));
        fragmentList.add(setFragment(2));
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("待提货");
        titleList.add("配送中");
        titleList.add("已完成");
        FragmentManager fm = getSupportFragmentManager();
        mVp.setAdapter(new BaseFragmentPagerAdapter(fm, fragmentList, titleList));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private Fragment setFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        OrderFragment orderBaseFragment = new OrderFragment();
        orderBaseFragment.setArguments(bundle);
        return orderBaseFragment;
    }
}
