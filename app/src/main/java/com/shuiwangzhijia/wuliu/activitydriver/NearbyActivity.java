package com.shuiwangzhijia.wuliu.activitydriver;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.NearbyAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.ShopBean;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.ProcurementGoodsActivity;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.MeasureUtil;
import com.shuiwangzhijia.wuliu.utils.SpacesItemDecoration;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.activity_nearby, title = "代下单")
public class NearbyActivity extends BaseActivity implements NearbyAdapter.OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.searchEdit)
    EditText searchEdit;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.search)
    TextView mSearch;
    private NearbyAdapter adapter;
    private String lnglat;
    public int page = -1;
    public int pageSize = 10;
    public boolean isLoading;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                        completeSwipeRefresh();
                    }

                    break;
            }
        }
    };
    private String search = "";
    private LinearLayoutManager layoutManager;

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        lnglat = CommonUtils.getLongitude() + "," + CommonUtils.getLatitude();
        mBaseBar.setBarListener(new BaseBar.BarListener() {
            @Override
            public void onLeftClick() {
                closeActivity();
            }

            @Override
            public void onRightClick() {

            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search = editable.toString();
            }
        });

        searchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    //先隐藏软键盘
                    if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        // 先隐藏键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        onRefresh();
                    }
                }
                return false;
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_30adfd));
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(this, MeasureUtil.dip2px(this,12)));
        adapter = new NearbyAdapter(this, true);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (dy > 0 && lastVisibleItemPosition + 1 == layoutManager.getItemCount()) {
                    if (isLoading) {
                        isLoading = false;
                        showLoadingDialog();
                        new LoadDataThread().start();
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(CommonUtils.getLongitude())) {
            onRefresh();
        } else {
            ToastUitl.showToastCustom("请开启地位服务");
        }
    }

    private void search(String text) {
        RetrofitUtils.getInstances().create().getShopList(lnglat, text, page, pageSize).enqueue(new Callback<EntityObject<ArrayList<ShopBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<ShopBean>>> call, Response<EntityObject<ArrayList<ShopBean>>> response) {
                dismissLoadingDialog();
                completeSwipeRefresh();
                EntityObject<ArrayList<ShopBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<ShopBean> result = body.getResult();
                    //第一页数据为0才显示无数据布局
                    if (result == null || (page == 0 && result.size() == 0)) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        return;
                    } else {
                        rlEmpty.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        if (page == 0) {
                            adapter.setData(result);
                        } else {
                            adapter.addData(result);
                        }
                        if (result.size() < pageSize) {
                            isLoading = false;
                        } else {
                            isLoading = true;
                        }

                    }
                } else {
                    if (page == 0) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                    if (body.getScode() == -200) {
                        EventBus.getDefault().post(new LoginOutEvent());
                    }
                }

            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<ShopBean>>> call, Throwable t) {
                rlEmpty.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        lnglat = CommonUtils.getLongitude() + "," + CommonUtils.getLatitude();
    }

    @Override
    public void onRefresh() {
        page = -1;
        showLoadingDialog();
        new LoadDataThread().start();
    }

    @Override
    public void onItemClick(int position) {
        ShopBean item = adapter.getItem(position);
//        PurchaseActivity.startAtc(getContext(), item.getId(), 2, null);
        ProcurementGoodsActivity.startAtc(this, item.getId(), 2, null);
    }

    private void completeSwipeRefresh() {
        if (mSwipeRefreshLayout == null)
            return;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.search)
    public void onViewClicked() {
        onRefresh();
    }

    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            page = page + 1;
            search(search);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }
}
