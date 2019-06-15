package com.shuiwangzhijia.wuliu.activitydriver;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.activity_driver_search_order, title = "搜索")
public class DriverSearchOrder extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView mRv;
    private String mSearchContent;

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        Bundle bundle=getIntent().getExtras();
        mSearchContent = bundle.getString("searchContent");
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
        search();
    }

    private void search() {
        RetrofitUtils.getInstances().create().driverSearchOrder(mSearchContent).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }





    @Override
    protected void initEvent() {

    }
}
