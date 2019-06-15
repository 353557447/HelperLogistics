package com.shuiwangzhijia.wuliu.fragmentwarehouse;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BaseFmAdapter;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.eventwarehouse.OrderListFlashEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 新订单页面
 * created by wangsuli on 2018/8/17.
 */
public class DeliverOrderFragment extends BaseFragment {
    private static final String TAG = "DeliverOrderFragment";
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private Unbinder unbinder;
    private List<String> titles = new ArrayList<>();
    private ArrayList<Object> pageList = new ArrayList<>();
    private BaseFmAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_warehouse_new_order, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initView();
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            unbinder = ButterKnife.bind(this, mRootView);
        }
        return mRootView;
    }

    private void initView() {
        titles = Arrays.asList(new String[]{"待提货", "待回桶", "已完成"});
        pageList.add(setFragment(0));
        pageList.add(setFragment(1));
        pageList.add(setFragment(2));
        adapter = new BaseFmAdapter(getChildFragmentManager(), pageList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private Fragment setFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        OrderFragment orderBaseFragment = new OrderFragment();
        orderBaseFragment.setArguments(bundle);
        return orderBaseFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(MainEvent event) {
        viewPager.setCurrentItem(event.index-1);
        EventBus.getDefault().post(new OrderListFlashEvent(false, event.index));
    }

}
