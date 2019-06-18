package com.shuiwangzhijia.wuliu.activitydriver;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BaseFmAdapter;
import com.shuiwangzhijia.wuliu.adapter.BaseFragmentPagerAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.event.OrderListFlashEvent;
import com.shuiwangzhijia.wuliu.fragment.CreateOrderFragment;
import com.shuiwangzhijia.wuliu.fragment.DeliverPeisongFragment;
import com.shuiwangzhijia.wuliu.fragment.DeliverTihuoFragment;
import com.shuiwangzhijia.wuliu.fragment.FinishOrderFragment;
import com.shuiwangzhijia.wuliu.fragment.OrderFragment;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

@BaseViewInject(contentViewId = R.layout.activity_driver_new_order,title = "配送订单")
public class DriverNewOrderActivity extends BaseActivity {
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
        Bundle bundle=getIntent().getExtras();
        mSelectedPosition = bundle.getInt("selectPosition");
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
        mTab.addTab(mTab.newTab().setText("配送中"), true);
        mTab.addTab(mTab.newTab().setText("已完成"), false);
        mTab.addTab(mTab.newTab().setText("赊账中"), false);
    }

    private void initVp() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(setFragment(1));
        fragmentList.add(setFragment(3));
        fragmentList.add(setFragment(2));
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("配送中");
        titleList.add("已完成");
        titleList.add("赊账中");
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

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(MainEvent event) {
        mVp.setCurrentItem(event.index-1);
        EventBus.getDefault().post(new OrderListFlashEvent(false, event.index));
    }
}
