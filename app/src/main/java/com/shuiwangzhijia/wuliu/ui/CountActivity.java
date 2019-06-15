package com.shuiwangzhijia.wuliu.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.CountAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.CountBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 采购统计和订单统计页面
 * created by wangsuli on 2018/8/22.
 */
public class CountActivity extends BaseAct implements Callback<EntityObject<ArrayList<CountBean>>> {
    public static final int TYPE_BUY = 0;
    public static final int TYPE_ORDER = 1;
    @BindView(R.id.llBuy)
    LinearLayout llBuy;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.startTime)
    TextView startTime;
    @BindView(R.id.endTime)
    TextView endTime;
    @BindView(R.id.rlEnd)
    RelativeLayout rlEnd;
    @BindView(R.id.rlStart)
    LinearLayout rlStart;

    private int type;
    private CountAdapter countAdapter;
    private Long startTimeStr, endTimeStr;
    private int year;
    private int month;
    private int day;

    public static void startAct(Context context, int type) {
        Intent intent = new Intent(context, CountActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        initRecyclerView();
        date();
        if (type == TYPE_BUY) {
            setTitle("采购统计");
            llBuy.setVisibility(View.VISIBLE);
            llOrder.setVisibility(View.GONE);
        } else {
            setTitle("订单统计");
            llBuy.setVisibility(View.GONE);
            llOrder.setVisibility(View.VISIBLE);
        }
        checkData();
    }

    //获取当前系统时间
    private void date() {
        Calendar c = Calendar.getInstance();
        //年
        year = c.get(Calendar.YEAR);
        //月
        month = c.get(Calendar.MONTH) + 1;
        //日
        day = c.get(Calendar.DAY_OF_MONTH);
        String date = String.format("%d-%d-%d", year, month, day);
        startTime.setText(date);
        endTime.setText(date);
        startTimeStr = DateUtils.getTime(date);
        endTimeStr = startTimeStr;
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(divider);
        countAdapter = new CountAdapter(this, type == TYPE_BUY ? false : true);
        mRecyclerView.setAdapter(countAdapter);
    }

    @Override
    public void onResponse(Call<EntityObject<ArrayList<CountBean>>> call, Response<EntityObject<ArrayList<CountBean>>> response) {
        EntityObject<ArrayList<CountBean>> body = response.body();

        if (body.getCode() == 200) {
            ArrayList<CountBean> result = body.getResult();
            if (result == null) {
                return;
            }
            countAdapter.setData(result);
        }
    }

    @Override
    public void onFailure(Call<EntityObject<ArrayList<CountBean>>> call, Throwable t) {

    }

    @OnClick({R.id.rlEnd, R.id.rlStart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlEnd:
                endDatePickerDialog();
                break;
            case R.id.rlStart:
                startDatePickerDialog();
                break;
        }
    }

    //起始时间的选取
    private void startDatePickerDialog() {
        new DatePickerDialog(CountActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth);
                startTime.setText(date);
                startTimeStr = DateUtils.getTime(date);
                checkData();
            }
        }, year, month-1, day).show();
    }

    private void checkData() {
        if (endTimeStr - startTimeStr < 0) {
            Toast.makeText(CountActivity.this, "结束时间比开始时间晚", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type == TYPE_BUY) {
            RetrofitUtils.getInstances().create().getPurchaseCountList(startTimeStr / 1000, endTimeStr / 1000).enqueue(this);

        } else {
            RetrofitUtils.getInstances().create().getOrderCountList(startTimeStr / 1000, endTimeStr / 1000).enqueue(this);
        }
    }

    //终止时间的选取
    private void endDatePickerDialog() {
        new DatePickerDialog(CountActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth);
                endTime.setText(date);
                endTimeStr = DateUtils.getTime(date);
                checkData();
            }
        }, year, month-1, day).show();
    }


}
