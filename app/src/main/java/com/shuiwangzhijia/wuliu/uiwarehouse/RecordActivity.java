package com.shuiwangzhijia.wuliu.uiwarehouse;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapterwarehouse.BucketAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.beanwarehouse.StatisticalInfoBean;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.activity_warehouse_record)
public class RecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, Callback<EntityObject<StatisticalInfoBean>> {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.date)
    TextView dateTv;
    /*  @BindView(R.id.llTop)
      LinearLayout llTop;*/
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.numTv)
    TextView numTv;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.back_return)
    RelativeLayout mBackReturn;
    @BindView(R.id.record_title)
    TextView mRecordTitle;
    private int type;
    private BucketAdapter mRecordAdapter;
    private int year, month;
    private Long monthTime;
    private LinearLayoutManager layoutManager;

    public static void startAct(Context context, int type) {
        Intent intent = new Intent(context, RecordActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        setBaseBarHide();
        type = getIntent().getIntExtra("type", -1);
        switch (type) {
            case 0:
                mRecordTitle.setText("待提货详情");
                titleTv.setText("待提货数量");
                break;
            case 1:
                mRecordTitle.setText("回桶详情");
                titleTv.setText("回桶数量");
                break;
            case 2:
                mRecordTitle.setText("提货详情");
                titleTv.setText("提货数量");
                break;
        }
        initRecyclerView();
        onRefresh();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void getList() {
        RetrofitUtils.getInstances().create().warehouseGetStatisticalInfo(type, page, PageSize).enqueue(this);
    }

    //获取当前系统时间
    private void date() {
        Calendar c = Calendar.getInstance();
        //年
        year = c.get(Calendar.YEAR);
        //月
        month = c.get(Calendar.MONTH) + 1;
        //日
        String date = String.format("%d年%d月", year, month);
        dateTv.setText(date);
        monthTime = DateUtils.getMonthTime(year + "-" + month + "-01 00:00:00");
    }

    private void initRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_30adfd));
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(divider);
        mRecordAdapter = new BucketAdapter(this);
        mRecyclerView.setAdapter(mRecordAdapter);
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
                    if (loading) {
                        loading = false;
                        showLoadingDialog();
                        new LoadDataThread().start();
                    }
                }
            }
        });
    }

    private void completeSwipeRefresh() {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        page = -1;
        showLoadingDialog();
        new LoadDataThread().start();
    }

    @Override
    public void onResponse(Call<EntityObject<StatisticalInfoBean>> call, Response<EntityObject<StatisticalInfoBean>> response) {
        dismissLoadingDialog();
        completeSwipeRefresh();
        EntityObject<StatisticalInfoBean> body = response.body();
        if (body == null)
            return;
        if (body.getCode() == 200) {
            StatisticalInfoBean result = body.getResult();
            if (result == null) {
                rlEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                return;
            } else {
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                rlEmpty.setVisibility(View.GONE);
                List<StatisticalInfoBean.GoodsBean> goods = result.getGoods();
                int count = result.getCount();
                numTv.setText(count + "");
                if (page == 0) {
                    mRecordAdapter.setData(goods);
                } else {
                    mRecordAdapter.addData(goods);
                }
                if (goods.size() < PageSize) {
                    loading = false;
                } else {
                    loading = true;
                }
            }
        } else {
            if (page == 0) {
                rlEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
            if (body.getScode() == -200) {
                EventBus.getDefault().post(new LoginOutEvent());
            }
        }
    }

    @Override
    public void onFailure(Call<EntityObject<StatisticalInfoBean>> call, Throwable t) {
        rlEmpty.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.back_return)
    public void onViewClicked() {
        closeActivity();
    }

    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            page = page + 1;
            getList();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        completeSwipeRefresh();
                    }

                    break;
            }
        }
    };

}
