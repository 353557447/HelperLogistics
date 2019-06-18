package com.shuiwangzhijia.wuliu.activitywarehouse;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import butterknife.BindView;
import butterknife.ButterKnife;

@BaseViewInject(contentViewId = R.layout.activity_wsearch_order, title = "订单搜索")
public class WSearchOrderActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

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
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
