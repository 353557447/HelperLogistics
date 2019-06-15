package com.shuiwangzhijia.wuliu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.StatisticsBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 店铺统计
 * created by wangsuli on 2018/8/21.
 */
public class ShopCountActivity extends BaseAct {
    @BindView(R.id.buyCount)
    TextView buyCount;
    @BindView(R.id.buyMoney)
    TextView buyMoney;
    @BindView(R.id.orderCount)
    TextView orderCount;
    @BindView(R.id.orderMoney)
    TextView orderMoney;
    @BindView(R.id.rlBuy)
    RelativeLayout rlBuy;
    @BindView(R.id.rlOrder)
    RelativeLayout rlOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_count);
        ButterKnife.bind(this);
        setTitle("店铺统计");
        setTextStyle(buyCount, "0" + "\n", "采购量");
        setTextStyle(buyMoney, "￥0" + "\n", "总金额");
        setTextStyle(orderCount, "0" + "\n", "订单总数");
        setTextStyle(orderMoney, "￥0" + "\n", "总金额");
        getData();

    }

    private void getData() {
        RetrofitUtils.getInstances().create().getShopStatistics().enqueue(new Callback<EntityObject<StatisticsBean>>() {
            @Override
            public void onResponse(Call<EntityObject<StatisticsBean>> call, Response<EntityObject<StatisticsBean>> response) {
                EntityObject<StatisticsBean> body = response.body();
                if (body.getCode() == 200) {
                    StatisticsBean result = body.getResult();
                    setTextStyle(buyCount, result.getPurchasing_num() + "\n", "采购量");
                    setTextStyle(buyMoney, "￥" + result.getTotal_price() + "\n", "总金额");
                    setTextStyle(orderCount, result.getOrder_num() + "\n", "订单总数");
                    setTextStyle(orderMoney, "￥" + result.getT_total() + "\n", "总金额");
                }
            }

            @Override
            public void onFailure(Call<EntityObject<StatisticsBean>> call, Throwable t) {

            }
        });
    }

    private void setTextStyle(TextView text, String content, String title) {
        SpannableString spanString = new SpannableString(content + title);
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_30adfd)), 0, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(20, true), 0, content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_999999)), content.length(), (content + title).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(14, true), content.length(), (content + title).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    @OnClick({R.id.rlBuy, R.id.rlOrder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBuy:
                CountActivity.startAct(ShopCountActivity.this, CountActivity.TYPE_BUY);
                break;
            case R.id.rlOrder:
                CountActivity.startAct(ShopCountActivity.this, CountActivity.TYPE_ORDER);
                break;
        }
    }
}
