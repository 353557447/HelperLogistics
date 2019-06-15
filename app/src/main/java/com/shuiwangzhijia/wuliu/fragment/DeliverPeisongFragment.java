package com.shuiwangzhijia.wuliu.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.DeliverOrderAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseLazyFragment;
import com.shuiwangzhijia.wuliu.bean.OutOrderDetailBean;
import com.shuiwangzhijia.wuliu.event.EditOrderEvent;
import com.shuiwangzhijia.wuliu.event.StatusBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.EditShippingOrderActivity;
import com.shuiwangzhijia.wuliu.ui.PendingOrderActivity;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
import com.shuiwangzhijia.wuliu.utils.PreferenceUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.fragment_deliver_peisong)
public class DeliverPeisongFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "DeliverOrderBaseFragmen";
    @BindView(R.id.deliver_consignee)
    TextView mDeliverConsignee; //提货人
    @BindView(R.id.callBtn)
    TextView mCallBtn;
    @BindView(R.id.deliver_order_number)
    TextView mDeliverOrderNumber;   //出货单号
    @BindView(R.id.deliver_time)
    TextView mDeliverTime;  //下单时间
    @BindView(R.id.deliver_remark)
    TextView mDeliverRemark;    //备注
    @BindView(R.id.deliver_total_number)
    TextView mDeliverTotalNumber;   //共xx桶
    @BindView(R.id.deliver_status)
    TextView mDeliverStatus;    //待发货
    @BindView(R.id.deliver_rv)
    RecyclerView mDeliverRv;
    @BindView(R.id.deliver_delete_order)
    TextView mDeliverDeleteOrder;
    @BindView(R.id.deliver_edit_order)
    TextView mDeliverEditOrder;
    @BindView(R.id.deliver_one)
    LinearLayout mDeliverOne;
    @BindView(R.id.deliver_two)
    LinearLayout mDeliverTwo;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.rlEmpty)
    RelativeLayout mRlEmpty;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.deliver_no_distribution)
    TextView mDeliverNoDistribution;
    @BindView(R.id.no_distribution_rv)
    RecyclerView mNoDistributionRv;
    private String mOut_order;
    private int mStatus_type;

    @Override
    protected void initView(View view) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.color_30adfd));
        mNoDistributionRv.setLayoutManager(new GridLayoutManager(mContext,2));
        mDeliverRv.setLayoutManager(new GridLayoutManager(mContext,2));
    }

    @Override
    protected void lazyLoadData() {
        getData();
    }

    @Override
    protected void initEvent() {

    }

    private void getData() {
        showLoadingDialog();
        RetrofitUtils.getInstances().create().getOutOrderDetail(1).enqueue(new Callback<EntityObject<OutOrderDetailBean>>() {

            @Override
            public void onResponse(Call<EntityObject<OutOrderDetailBean>> call, Response<EntityObject<OutOrderDetailBean>> response) {
                try {
                    dismissLoadingDialog();
                    if (mSwipeRefreshLayout != null)
                        mSwipeRefreshLayout.setRefreshing(false);
                    EntityObject<OutOrderDetailBean> body = response.body();
                    if (body != null) {
                        if (body.getCode() == 200) {
                            mDeliverOne.setVisibility(View.VISIBLE);
                            mDeliverTwo.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            mRlEmpty.setVisibility(View.GONE);
                            OutOrderDetailBean result = body.getResult();
                            mDeliverConsignee.setText("提货人：" + result.getUname());
                            mDeliverOrderNumber.setText("" + result.getOut_order());
                            mDeliverTime.setText("" + DateUtils.getFormatDateStr(result.getCreate_time() * 1000L));
                            mDeliverRemark.setText("备注：" + result.getRemark());
                            PreferenceUtils.putString("deliver_order", result.getId() + "");
                            mDeliverNoDistribution.setText("未配送"+result.getGoods_additional_num()+"桶");

                            mOut_order = result.getOut_order();
                            String status = result.getStatus();
                            mStatus_type = result.getStatus_type();
                            mDeliverStatus.setText(status);
                            //0待出货  1.2是已出货
                            if (mStatus_type == 0) {
                                mDeliverDeleteOrder.setVisibility(View.VISIBLE);
                                mDeliverNoDistribution.setVisibility(View.GONE);
                                mNoDistributionRv.setVisibility(View.GONE);
                                mDeliverTotalNumber.setText("共"+result.getSum()+"桶");
                            } else {
                                mDeliverDeleteOrder.setVisibility(View.GONE);
                                mDeliverNoDistribution.setVisibility(View.VISIBLE);
                                mNoDistributionRv.setVisibility(View.VISIBLE);
                                mDeliverTotalNumber.setText("已提货"+result.getSum()+"桶");
                            }
//                        if (status.contains("已出货")){
//                            mDeliverDeleteOrder.setVisibility(View.GONE);
//
//                        }else {
//                            mDeliverDeleteOrder.setVisibility(View.VISIBLE);
//                            mDeliverStatus.setText(status);
//                        }

                            initRecycleView(result.getGoods());
                            initNoDistributionRv(result.getGoods_additional());
                        } else {
                            mDeliverOne.setVisibility(View.GONE);
                            mDeliverTwo.setVisibility(View.GONE);
                            mRlEmpty.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    KLog.d(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<OutOrderDetailBean>> call, Throwable t) {
                dismissLoadingDialog();
            }
        });
    }

    private void initNoDistributionRv(List<OutOrderDetailBean.GoodsBean> goods_additional) {
        DeliverOrderAdapter adapter = new DeliverOrderAdapter(getContext(), goods_additional);
        mNoDistributionRv.setAdapter(adapter);
    }

    private void initRecycleView(List<OutOrderDetailBean.GoodsBean> goods) {
        DeliverOrderAdapter adapter = new DeliverOrderAdapter(getContext(), goods);
        mDeliverRv.setAdapter(adapter);
    }


    private void setTextStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ff3939)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(24, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }


    @OnClick({R.id.deliver_delete_order, R.id.deliver_edit_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.deliver_delete_order:
                deleteOrder();
                break;
            case R.id.deliver_edit_order:
                if (mStatus_type == 0) {
                    Intent intent = new Intent(getActivity(), EditShippingOrderActivity.class);
                    startActivity(intent);
                } else {
                    //fromType 2 表示是从出货单的编辑订单页面跳转进去的。
                    PendingOrderActivity.startAct(getContext(), 2, mOut_order);
                }
                break;
        }
    }

    private void deleteOrder() {
        RetrofitUtils.getInstances().create().deleteOutOrder(mOut_order).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 200) {
                    ToastUitl.showToastCustom("删除成功");
                    mDeliverOne.setVisibility(View.GONE);
                    mDeliverTwo.setVisibility(View.GONE);
                    mRlEmpty.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    EventBus.getDefault().post(new StatusBean());
                } else if (body.getCode() == 800) {
                    ToastUitl.showToastCustom("已出货，无法删除");
                    initData();
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(EditOrderEvent event) {
        setUserVisibleHint(true);
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
