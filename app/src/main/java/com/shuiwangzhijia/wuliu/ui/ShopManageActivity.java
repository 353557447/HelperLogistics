package com.shuiwangzhijia.wuliu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
import com.shuiwangzhijia.wuliu.view.CustomDatePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 店铺管理
 * created by wangsuli on 2018/8/20.
 */
public class ShopManageActivity extends BaseAct {
    @BindView(R.id.editStartTime)
    TextView editStartTime;
    @BindView(R.id.editEndTime)
    TextView editEndTime;
    @BindView(R.id.freeBtn)
    TextView freeBtn;
    @BindView(R.id.payBtn)
    TextView payBtn;
    @BindView(R.id.editPayMoney)
    EditText editPayMoney;
    @BindView(R.id.editFreeMoney)
    EditText editFreeMoney;
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.llPayInfo)
    LinearLayout llPayInfo;
    @BindView(R.id.editSpeed)
    EditText editSpeed;
    @BindView(R.id.saveBtn)
    TextView saveBtn;
    private CustomDatePicker customDatePicker;
    private boolean isStart, isFree;
    private String currentDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_manage);
        ButterKnife.bind(this);
        setTitle("店铺管理");
        initDate();
        initState(true);
        editFreeMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.length() > 0) {
                    setTextStyle(hint, s);
                }
            }
        });
    }

    private void setTextStyle(TextView text, String content) {
        SpannableString spanString = new SpannableString("满" + content + "元免配送费");
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_30adfd)), 1, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        text.setText(spanString);
    }

    @OnClick({R.id.editStartTime, R.id.editEndTime, R.id.freeBtn, R.id.payBtn, R.id.saveBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editStartTime:
                isStart = true;
                customDatePicker.show(currentDate);
                break;
            case R.id.editEndTime:
                isStart = false;
                customDatePicker.show(currentDate);
                break;
            case R.id.freeBtn:
                initState(true);
                break;
            case R.id.payBtn:
                initState(false);
                break;
            case R.id.saveBtn:
                String start = editStartTime.getText().toString();
                String end = editEndTime.getText().toString();
                String speed = "", payMoney = "", freeMoney = "";
                speed = editSpeed.getText().toString();
                if (TextUtils.isEmpty(start)) {
                    hint("请选择营业开始时间");
                    return;
                }
                if (TextUtils.isEmpty(start)) {
                    hint("请选择营业结束时间");
                    return;
                }
                if (!isFree) {
                    payMoney = editPayMoney.getText().toString();
                    freeMoney = editFreeMoney.getText().toString();
                    if (TextUtils.isEmpty(speed)) {
                        hint("请输入配送费金额");
                        return;
                    }
                    if (TextUtils.isEmpty(speed)) {
                        hint("请输入满免配送费金额");
                        return;
                    }

                }
                if (TextUtils.isEmpty(speed)) {
                    hint("请输入配送效率");
                    return;
                }
                RetrofitUtils.getInstances().create().addShopInfo(start + "-" + end
                        , isFree ? 0 : 1, speed, payMoney, freeMoney).enqueue(new Callback<EntityObject<Object>>() {
                    @Override
                    public void onResponse(Call<EntityObject<Object>> call, Response<EntityObject<Object>> response) {
                        Toast.makeText(ShopManageActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<EntityObject<Object>> call, Throwable t) {

                    }
                });
                break;
        }
    }

    private void hint(String text) {
        Toast.makeText(ShopManageActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void initState(boolean isFree) {
        this.isFree = isFree;
        freeBtn.setSelected(isFree ? true : false);
        payBtn.setSelected(!isFree ? true : false);
        llPayInfo.setVisibility(isFree ? View.GONE : View.VISIBLE);
    }

    private void initDate() {
        currentDate = DateUtils.getFormatDateStr(System.currentTimeMillis());
        editStartTime.setText(currentDate.split(" ")[1]);
        editEndTime.setText(currentDate.split(" ")[1]);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (isStart) {
                    editStartTime.setText(time.split(" ")[1]);
                } else {
                    editEndTime.setText(time.split(" ")[1]);
                }
            }
        }, "2010-01-01 00:00", DateUtils.getCurrentDate()); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 不显示时和分
        customDatePicker.setIsLoop(true); // 不允许循环滚动
    }


}
