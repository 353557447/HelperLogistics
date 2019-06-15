package com.shuiwangzhijia.wuliu.activitydriver;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BaseFmAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.event.OrderListFlashEvent;
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
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private List<String> titles = new ArrayList<>();
    private ArrayList<Object> pageList = new ArrayList<>();
    private BaseFmAdapter adapter;
    private int mSelectPosition;

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        Bundle bundle=getIntent().getExtras();
        mSelectPosition = bundle.getInt("selectPosition");
        mBaseBar.setBarListener(new BaseBar.BarListener() {
            @Override
            public void onLeftClick() {
                closeActivity();
            }

            @Override
            public void onRightClick() {

            }
        });
        titles = Arrays.asList(new String[]{"配送中", "已完成", "赊账中"});
        pageList.add(setFragment(1));
        pageList.add(setFragment(3));
        pageList.add(setFragment(2));
        adapter = new BaseFmAdapter(getSupportFragmentManager(), pageList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(mSelectPosition);
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
        viewPager.setCurrentItem(event.index-1);
        EventBus.getDefault().post(new OrderListFlashEvent(false, event.index));
    }
}
