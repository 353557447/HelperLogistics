package com.shuiwangzhijia.wuliu.fragmentwarehouse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseUserBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.SetActivity;
import com.shuiwangzhijia.wuliu.uiwarehouse.MsgActivity;
import com.shuiwangzhijia.wuliu.uiwarehouse.RecordActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的页面
 * created by wangsuli on 2018/8/28.
 */
public class MyFragment extends BaseFragment {

    private static final String TAG = "MyFragment";
    @BindView(R.id.msgBtn)
    ImageView msgBtn;
    @BindView(R.id.setBtn)
    ImageView setBtn;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.unOrder)
    TextView unOrder;
    @BindView(R.id.doOrder)
    TextView doOrder;
    @BindView(R.id.historyOrder)
    TextView historyOrder;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_warehouse_my, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            unbinder = ButterKnife.bind(this, mRootView);
        }
        getUserInfo();
        return mRootView;
    }

    private void setTextStyle(TextView text, String content, String title) {
        SpannableString spanString = new SpannableString(content + title);
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_333333)), 0, content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(16, true), 0, content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_DF3A44)), content.length(), (content + title).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(16, true), content.length(), (content + title).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }
    private void getUserInfo() {
        RetrofitUtils.getInstances().create().warehouseGetUserInfo().enqueue(new Callback<EntityObject<WarehouseUserBean>>() {
            @Override
            public void onResponse(Call<EntityObject<WarehouseUserBean>> call, Response<EntityObject<WarehouseUserBean>> response) {
                hintLoad();
                EntityObject<WarehouseUserBean> body = response.body();
                if (body.getCode() == 200){
                    WarehouseUserBean userBean = body.getResult();
                    if (userBean.getPersonalInfo() != null){
                        String phoneNumber = userBean.getPersonalInfo().getPhone();
                        phone.setText(phoneNumber);
                        name.setText(userBean.getPersonalInfo().getUname());
                    }
                   if (userBean.getOutOrderInfo() != null){
                       WarehouseUserBean.OutOrderInfoBean orderInfo = userBean.getOutOrderInfo();
                       setTextStyle(unOrder, "今日待出货" + "\n", orderInfo.getUn_today());
                       setTextStyle(doOrder, "今日已出货" + "\n", orderInfo.getToday());
                       setTextStyle(historyOrder, "历史出货" + "\n", orderInfo.getPre_day());
                   }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<WarehouseUserBean>> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.msgBtn, R.id.setBtn, R.id.unOrder, R.id.doOrder, R.id.historyOrder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.msgBtn:
                startActivity(new Intent(getActivity(), MsgActivity.class));
                break;
            case R.id.setBtn:
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            case R.id.unOrder:
                RecordActivity.startAct(getContext(),0);
                break;
            case R.id.doOrder:
                RecordActivity.startAct(getContext(),1);
                break;
            case R.id.historyOrder:
                RecordActivity.startAct(getContext(),2);
//                HuiChangConfirmActivity.startAct(getContext());
                break;
        }
    }
}
