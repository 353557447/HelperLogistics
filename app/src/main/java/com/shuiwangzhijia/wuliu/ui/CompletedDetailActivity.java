package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.CompletedDetailsAdapter;
import com.shuiwangzhijia.wuliu.adapter.CompletedDetailsSumAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.CompletedDetailsBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@BaseViewInject(contentViewId = R.layout.activity_completed_detail,title = "订单详情")
public class CompletedDetailActivity extends BaseActivity {
    @BindView(R.id.completed_detail_name)
    TextView mCompletedDetailName;
    @BindView(R.id.completed_detail_order_num)
    TextView mCompletedDetailOrderNum;
    @BindView(R.id.completed_detail_tuihuo)
    TextView mCompletedDetailTuihuo;
    @BindView(R.id.completed_detail_huicang)
    TextView mCompletedDetailHuicang;
    @BindView(R.id.completed_detail_fuze)
    TextView mCompletedDetailFuze;
    @BindView(R.id.completed_detail_need_rv)
    RecyclerView mCompletedDetailNeedRv;
    @BindView(R.id.completed_detail_actual)
    RecyclerView mCompletedDetailActual;
    @BindView(R.id.completed_detail_hui_rv)
    RecyclerView mCompletedDetailHuiRv;
    @BindView(R.id.completed_detail_tui_rv)
    RecyclerView mCompletedDetailTuiRv;
    @BindView(R.id.zatong_number)
    TextView mZatongNumber;
    @BindView(R.id.completed_detail_ziying_rv)
    RecyclerView mCompletedDetailZiyingRv;
    @BindView(R.id.potong_number)
    TextView mPotongNumber;
    private String mOrderNo;

    public static void startAct(Context context, String orderNo) {
        Intent in = new Intent(context, CompletedDetailActivity.class);
        in.putExtra("orderNo", orderNo);
        context.startActivity(in);
    }

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
        mOrderNo = getIntent().getStringExtra("orderNo");
        getList();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void getList() {
        RetrofitUtils.getInstances().create().getCompletedDetailInfo(mOrderNo).enqueue(new Callback<EntityObject<CompletedDetailsBean>>() {
            @Override
            public void onResponse(Call<EntityObject<CompletedDetailsBean>> call, Response<EntityObject<CompletedDetailsBean>> response) {
                EntityObject<CompletedDetailsBean> body = response.body();
                if (body.getCode() == 200) {
                    CompletedDetailsBean result = body.getResult();
                    mCompletedDetailName.setText("提货司机：" + result.getUname());
                    mCompletedDetailOrderNum.setText("提货单号：" + result.getOut_order());
                    mCompletedDetailHuicang.setText("回仓时间：" + DateUtils.getFormatDateStr(result.getUpdate_time() * 1000L));
                    mCompletedDetailTuihuo.setText("退货时间：" + DateUtils.getFormatDateStr(result.getCreate_time() * 1000L));
                    mCompletedDetailFuze.setText("仓管员：" + result.getCname());
                    mZatongNumber.setText("x" + result.getMiscellaneous());
                    mPotongNumber.setText("x"+result.getPobucket());
                    List<CompletedDetailsBean.TotalGoodsBean> totalGoods = result.getTotal_goods();
                    List<CompletedDetailsBean.SumGoodsBean> sumGoods = result.getSum_goods();
                    List<CompletedDetailsBean.TotalGoodsBean> hsum_goods = result.getHsum_goods();
                    List<CompletedDetailsBean.TotalGoodsBean> tsum_goods = result.getTsum_goods();
                    List<CompletedDetailsBean.TotalGoodsBean> self = result.getSelf();
                    initGoodsRV(totalGoods);
                    initSumGoods(sumGoods);
                    initHsumGoodsRv(hsum_goods);
                    initTsumGoodsRv(tsum_goods);
                    initZiyingRv(self);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<CompletedDetailsBean>> call, Throwable t) {

            }
        });
    }

    private void initZiyingRv(List<CompletedDetailsBean.TotalGoodsBean> self) {
        mCompletedDetailZiyingRv.setLayoutManager(new GridLayoutManager(this,2));
        mCompletedDetailZiyingRv.setHasFixedSize(true);
        CompletedDetailsAdapter mAdapter = new CompletedDetailsAdapter(this, 4);
        mAdapter.setData(self);
        mCompletedDetailZiyingRv.setAdapter(mAdapter);
    }

    private void initTsumGoodsRv(List<CompletedDetailsBean.TotalGoodsBean> tsum_goods) {
        mCompletedDetailTuiRv.setLayoutManager(new GridLayoutManager(this,2));
        mCompletedDetailTuiRv.setHasFixedSize(true);
        CompletedDetailsAdapter mAdapter = new CompletedDetailsAdapter(this, 3);
        mAdapter.setData(tsum_goods);
        mCompletedDetailTuiRv.setAdapter(mAdapter);
    }

    private void initHsumGoodsRv(List<CompletedDetailsBean.TotalGoodsBean> hsum_goods) {
        mCompletedDetailHuiRv.setLayoutManager(new GridLayoutManager(this,2));
        mCompletedDetailHuiRv.setHasFixedSize(true);
        CompletedDetailsAdapter mAdapter = new CompletedDetailsAdapter(this, 2);
        mAdapter.setData(hsum_goods);
        mCompletedDetailHuiRv.setAdapter(mAdapter);
    }

    private void initSumGoods(List<CompletedDetailsBean.SumGoodsBean> sumGoods) {
        mCompletedDetailActual.setLayoutManager(new GridLayoutManager(this,2));
        mCompletedDetailActual.setHasFixedSize(true);
        CompletedDetailsSumAdapter adapter = new CompletedDetailsSumAdapter(this);
        adapter.setData(sumGoods);
        mCompletedDetailActual.setAdapter(adapter);
    }

    private void initGoodsRV(List<CompletedDetailsBean.TotalGoodsBean> totalGoods) {
        mCompletedDetailNeedRv.setLayoutManager(new GridLayoutManager(this,2));
        mCompletedDetailNeedRv.setHasFixedSize(true);
        CompletedDetailsAdapter mAdapter = new CompletedDetailsAdapter(this, 1);
        mAdapter.setData(totalGoods);
        mCompletedDetailNeedRv.setAdapter(mAdapter);
    }
}
