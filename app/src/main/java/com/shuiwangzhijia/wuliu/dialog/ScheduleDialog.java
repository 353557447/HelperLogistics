package com.shuiwangzhijia.wuliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.ScheduleAdapter;
import com.shuiwangzhijia.wuliu.utils.GridDividerItemDecoration;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预约时间弹窗
 * created by wangsuli on 2018/8/20.
 */
public class ScheduleDialog extends BottomDialog {
    private final ScheduleAdapter mScheduleAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.okBtn)
    TextView okBtn;
    @BindView(R.id.today)
    TextView today;
    @BindView(R.id.tomorrow)
    TextView tomorrow;
    private boolean isToday = true;
    private int index = -1;
    private OnConfirmClickListener listener;
    private int year;
    private int month;
    private int day;

    public ScheduleDialog(Context context, final OnConfirmClickListener listener) {
        super(context);
        setContentView(R.layout.dialog_schedule);
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        Calendar c = Calendar.getInstance();
        //年
        year = c.get(Calendar.YEAR);
        //月
        month = c.get(Calendar.MONTH) + 1;
        //天
        day = c.get(Calendar.DAY_OF_MONTH);

        today.setText("今天" + month + "-" + day);
        c.add(Calendar.DATE, 1);
        tomorrow.setText("明天" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH));
        this.listener = listener;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(20, context.getResources().getColor(R.color.color_ffffff)));
        mScheduleAdapter = new ScheduleAdapter(context);
        mScheduleAdapter.setOnItemClickListener(new ScheduleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                index = position;
            }
        });
        mRecyclerView.setAdapter(mScheduleAdapter);
    }


    @OnClick({R.id.okBtn, R.id.today, R.id.tomorrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.okBtn:
                String date = "";
                if (isToday) {
                    date = today.getText().toString().replace("今天", "");
                } else {
                    date = today.getText().toString().replace("明天", "");
                }
                listener.onConfirm(this, 1233012313);
                break;
            case R.id.today:
                isToday = true;
                break;
            case R.id.tomorrow:
                isToday = false;
                break;
        }
    }

    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog, long time);
    }
}
