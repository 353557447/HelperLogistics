package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.RecordAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.RecordBean;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecordActivity extends BaseAct implements RecordAdapter.OnRecyclerViewItemClickListener, Callback<EntityObject<ArrayList<RecordBean>>>, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.date)
    TextView dateTv;
    @BindView(R.id.llTop)
    LinearLayout llTop;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private int type;
    private RecordAdapter mRecordAdapter;
    private int year, month;
    private Long monthTime;
    private LinearLayoutManager layoutManager;
    private int sid;
    private  RecordBean data;

    public static void startAct(Context context, int type, RecordBean data) {
        Intent intent = new Intent(context, RecordActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        switch (type) {
            case 1:
                date();
                setTitle("送水记录");
                break;
            case 2:
                date();
                setTitle("收款记录");
                break;
            case 3:
                date();
                setTitle("回桶记录");
                break;
            case 4:
                setTitle("自营桶记录");
                llTop.setVisibility(View.GONE);
                data = (RecordBean) getIntent().getSerializableExtra("data");
                sid=data.getSid();
                monthTime=data.getUpdate_time();
                break;
            case 5:
                setTitle("杂桶记录");
                llTop.setVisibility(View.GONE);
                data = (RecordBean) getIntent().getSerializableExtra("data");
                sid=data.getSid();
                monthTime=data.getUpdate_time();
                break;
        }

        initRecyclerView();
        onRefresh();
    }

    private void getList() {
        switch (type) {
            case 1:
                RetrofitUtils.getInstances().create().getWaterList(monthTime / 1000, page, PageSize).enqueue(this);
                break;
            case 2:
                RetrofitUtils.getInstances().create().getMoneyList(monthTime / 1000, page, PageSize).enqueue(this);
                break;
            case 3:
                RetrofitUtils.getInstances().create().getBucketList(monthTime / 1000, page, PageSize).enqueue(this);
                break;
            case 4:
                RetrofitUtils.getInstances().create().getSelfBucketList(sid,monthTime).enqueue(this);
                break;
            case 5:
                RetrofitUtils.getInstances().create().getOtherBucketList(sid,monthTime).enqueue(this);
                break;
        }

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
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg));
        mRecyclerView.addItemDecoration(divider);
        mRecordAdapter = new RecordAdapter(this, type);
        mRecordAdapter.setOnItemClickListener(this);
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
                    if (type == 4 || type == 5){
                        loading = false;
                    }
                    if (loading) {
                        loading = false;
                        showLoad();
                        new LoadDataThread().start();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onSelfBucketClick(int position) {
        RecordBean item = mRecordAdapter.getItem(position);
        RecordActivity.startAct(this, 4, item);
    }

    @Override
    public void onOtherBucketClick(int position) {
        RecordBean item = mRecordAdapter.getItem(position);
        RecordActivity.startAct(this, 5, item);
    }

    @OnClick(R.id.date)
    public void onViewClicked() {
        initTimePickView();
       /* DatePickerDialog datePicker = new DatePickerDialog(RecordActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                dateTv.setText(year + "年" + (monthOfYear + 1) + "月");
                monthTime = DateUtils.getMonthTime("" + year + "-" + (monthOfYear + 1) + "-01 00:00:00");
                onRefresh();
            }
        }, year, month - 1, 1);*/
       /* DatePicker dp = findDatePicker((ViewGroup) datePicker.getWindow().getDecorView());
        if (dp!=null){
            ((ViewGroup)((ViewGroup)dp.getChildAt(2)).getChildAt(2))
                    .getChildAt(2).setVisibility(View.GONE);
        }
        datePicker.show();*/
    }

    private void initTimePickView() {
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String format = sdf.format(date);
                monthTime = DateUtils.getMonthTime("" + format.split("-")[0] + "-" + format.split("-")[1] + "-01 00:00:00");
                dateTv.setText(format.split("-")[0] + "年" + format.split("-")[1] + "月");
                onRefresh();
            }
        }).setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setLabel("年     ","月     ","","","","")
                .setCancelColor(Color.parseColor("#FE0233"))
                .setSubmitColor(Color.parseColor("#FE0233"))
                .setSubmitText("完成")
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    @Override
    public void onResponse(Call<EntityObject<ArrayList<RecordBean>>> call, Response<EntityObject<ArrayList<RecordBean>>> response) {
        hintLoad();
        completeSwipeRefresh();
        EntityObject<ArrayList<RecordBean>> body = response.body();
        if (body.getCode() == 200) {
            ArrayList<RecordBean> result = body.getResult();
            if (result == null) {
                rlEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                return;
            } else {
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                rlEmpty.setVisibility(View.GONE);
                if (page == 0) {
                    mRecordAdapter.setData(result);
                } else {
                    mRecordAdapter.addData(result);
                }
                if (result.size() < PageSize) {
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

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    @Override
    public void onFailure(Call<EntityObject<ArrayList<RecordBean>>> call, Throwable t) {
        rlEmpty.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }


    private void completeSwipeRefresh() {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        page = -1;
        showLoad();
        new LoadDataThread().start();
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
