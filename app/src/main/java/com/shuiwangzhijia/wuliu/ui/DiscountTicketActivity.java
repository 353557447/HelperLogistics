package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.TicketAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.TicketBean;
import com.shuiwangzhijia.wuliu.bean.UseTicketBean;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 优惠水票 选择水票页面
 * created by wangsuli on 2018/9/26.
 */
public class DiscountTicketActivity extends BaseAct implements SwipeRefreshLayout.OnRefreshListener, TicketAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    private LinearLayoutManager layoutManager;
    private TicketAdapter mTicketAdapter;
    private int type;
    private UseTicketBean data;
    private ArrayList<TicketBean> dataList=new ArrayList<>();
    private int count;
    private double amount;
    private List<GoodsBean> goodsList;
    private Map<String, Integer> ticketData = new HashMap<>();
    private String gid;

    /**
     * @param context
     * @param type    -1 优惠水票 -2 选择水票
     * @param data    选择水票 需传的参数
     */
    public static void startAtc(Context context, int type, UseTicketBean data) {
        Intent intent = new Intent(context, DiscountTicketActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_ticket);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        initRecyclerView();
        if (type == -1) {
            setTitle("优惠水票");
            onRefresh();
        } else if(type==-2){
            setTitle("选择水票");
            /*data = (UseTicketBean) getIntent().getSerializableExtra("data");
            goodsList = data.getGoodsList();
            for (GoodsBean goodsBean : goodsList) {
                ticketData.put(goodsBean.getGid(), goodsBean.getCount());
            }
            dataList = data.getList();*/
            sureBtn.setVisibility(View.VISIBLE);
            mTicketAdapter.setData(dataList);
        }
    }

    private void initRecyclerView() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg));
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setHasFixedSize(true);
        mTicketAdapter = new TicketAdapter(this, type);
        mTicketAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mTicketAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (type == -1 && dy > 0 && lastVisibleItemPosition + 1 == layoutManager.getItemCount()) {
                    if (loading) {
                        loading = false;
                        new LoadDataThread().start();
                    }
                }
            }
        });


    }


    @Override
    public void onRefresh() {
        if (type == -1) {
            page = -1;
            showLoad();
            new LoadDataThread().start();
        } else {
            completeSwipeRefresh();
        }
    }

    private void completeSwipeRefresh() {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onSelectClick(int position) {
        TicketBean item = mTicketAdapter.getItem(position);
        item.setCheck(!item.isCheck());
        mTicketAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPayClick(int position) {
        final TicketBean data = mTicketAdapter.getItem(position);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("s_gid", data.getS_gid());
            jsonObject.put("num", data.getSnum());
            array.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* RetrofitUtils.getInstances().create().createTicketOrder(CommonUtils.getToken(), "" + data.getSprice(), array.toString()).enqueue(new Callback<EntityObject<TicketOrderBean>>() {
            @Override
            public void onResponse(Call<EntityObject<TicketOrderBean>> call, Response<EntityObject<TicketOrderBean>> response) {
                EntityObject<TicketOrderBean> body = response.body();
                if (body.getCode() == 200) {
//                    ToastUitl.showToastCustom("提交成功!");
                    TicketOrderBean result = body.getResult();
                    result.setData(data);
                    TicketPayActivity.startAtc(DiscountTicketActivity.this, result);
                } else {
                    ToastUitl.showToastCustom("提交失败!");
                }
            }

            @Override
            public void onFailure(Call<EntityObject<TicketOrderBean>> call, Throwable t) {

            }
        });*/

    }

    @Override
    public void onUpDownClick(int position) {
        //无需操作
    }

    @OnClick(R.id.sureBtn)
    public void onViewClicked() {
        List<TicketBean> selectData = new ArrayList<>();
        for (int i = 0; i < mTicketAdapter.getItemCount(); i++) {
            TicketBean item = mTicketAdapter.getItem(i);
            if (item.isCheck()) {
                gid = item.getGid();
                //与商品的gid 一致后 看选择的商品数量和当前水票的可使用张数作对比
                if (ticketData.containsKey(gid)) {
                    int count = ticketData.get(gid);
                    if (count > 0) {
                        //当水票的张数高于商品的数量 直接使用当前的水票
                        // 否则 不够的水票数量 用下一个水票数量
                        if (CalculateUtils.sub(item.getSum(), count) > 0) {
                            item.setTotal(count + "");
                            ticketData.put(gid, 0);
                        } else {
                            item.setTotal(item.getSum() + "");
                            ticketData.put(gid, (int) CalculateUtils.sub(count, item.getSum()));
                        }
                        selectData.add(item);
                    }

                }

            }
        }
        String dataInfo = packageTicketData(selectData);
        Log.i("tag", "水票数据:" + dataInfo);
//        EventBus.getDefault().post(new TicketEvent(count, amount, dataInfo));
        finish();

    }

    private String packageTicketData(List<TicketBean> dataList) {
        count = 0;
        amount = 0;
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (TicketBean bean : dataList) {
            int total = Integer.valueOf(bean.getTotal());
            count = count + total;
            amount = CalculateUtils.add(amount, CalculateUtils.mul(total, bean.getPprice()));
            item = new JSONObject();
            try {
                item.put("s_gid", bean.getS_gid());
                item.put("num", bean.getTotal());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        return array.toString();
    }

    private void getList() {
       /* RetrofitUtils.getInstances().create().getDiscountTicketList(page, PageSize).enqueue(new Callback<EntityObject<ArrayList<TicketBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<TicketBean>>> call, Response<EntityObject<ArrayList<TicketBean>>> response) {
                completeSwipeRefresh();
                hintLoad();
                EntityObject<ArrayList<TicketBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<TicketBean> result = body.getResult();
                    if (result == null) {
                        loading = false;
                        rlEmpty.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        return;
                    } else {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        rlEmpty.setVisibility(View.GONE);
                        if (result.size() < PageSize) {
                            loading = false;
                        } else {
                            loading = true;
                        }
                        if (page == 0) {
                            mTicketAdapter.setData(result);
                        } else {
                            mTicketAdapter.addData(result);
                        }

                    }

                } else {
                    if (page == 0) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                    }
                    loading = false;
                }

            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<TicketBean>>> call, Throwable t) {
                rlEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                Log.i("json", t.getMessage());
            }
        });*/
    }

    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            page++;
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

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        finish();
    }
}
