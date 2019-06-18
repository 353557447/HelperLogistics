package com.shuiwangzhijia.wuliu.activitydriver;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.DSearchOrderAdapter;
import com.shuiwangzhijia.wuliu.adapter.DSearchOrderSonAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.DSearchResultBean;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.SpacesItemDecoration;
import com.shuiwangzhijia.wuliu.view.BaseBar;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.activity_dsearch_order, title = "订单搜索")
public class DSearchOrderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, DSearchOrderAdapter.OnViewItemClickListener, View.OnKeyListener {
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.search_content)
    EditText mSearchContent;
    @BindView(R.id.search)
    TextView mSearch;
    private LinearLayoutManager layoutManager;
    private DSearchOrderAdapter mOrderAdapter;
    private String mSearchContentStr;
    private List<DSearchResultBean.DataBean> mList;

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
        initRv();
    }

    private void initRv() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_30adfd));
        layoutManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(layoutManager);
        mRv.setHasFixedSize(true);
        mRv.addItemDecoration(new SpacesItemDecoration(this, 24));
        mOrderAdapter = new DSearchOrderAdapter(this, 0);
        mList = new ArrayList<>();
        mOrderAdapter.setData(mList);
        mOrderAdapter.setOnItemClickListener(this);
        mRv.setAdapter(mOrderAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mSearchContent.setOnKeyListener(this);
    }

    @Override
    public void onRefresh() {
        getSearchResultData();
    }

    @Override
    public void onCallClick(String phone) {

    }

    @Override
    public void onDetailClick(int position) {

    }

    @Override
    public void onHandleOrderClick(int position, DSearchOrderSonAdapter adapter, int order) {

    }

    @Override
    public void onHuichangOprea(int position) {

    }

    @OnClick(R.id.search)
    public void onViewClicked() {
        getSearchResultData();
    }

    private void getSearchResultData() {
        mSearchContentStr = mSearchContent.getText().toString().trim();
        if (TextUtils.isEmpty(mSearchContentStr)) {
            showToast("搜索内容不能为空~");
            return;
        }
        showLoadingDialog();
        RetrofitUtils.getInstances().create().driverSearchOrder(mSearchContentStr).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    String datas = mGson.toJson(response.body());
                    KLog.e(datas);
                    JSONObject object = new JSONObject(datas);
                    int code = object.getInt("code");
                    if (code == 200) {
                        DSearchResultBean dSearchResultBean = mGson.fromJson(datas, DSearchResultBean.class);
                        List<DSearchResultBean.DataBean> data = dSearchResultBean.getData();
                        mSwipeRefreshLayout.setRefreshing(false);
                        mList.clear();
                        mList.addAll(data);
                        mOrderAdapter.notifyDataSetChanged();
                    } else {

                    }
                } catch (Exception e) {
                    KLog.e(e.getMessage());
                } finally {
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                KLog.e(t.getMessage());
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
            getSearchResultData();
            return true;
        }
        return false;
    }
}
