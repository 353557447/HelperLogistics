package com.shuiwangzhijia.wuliu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.bean.UserBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.BucketManageActivity;
import com.shuiwangzhijia.wuliu.ui.MsgActivity;
import com.shuiwangzhijia.wuliu.ui.RecordActivity;
import com.shuiwangzhijia.wuliu.ui.SetActivity;

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


    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.totalSend)
    TextView totalSend;
    @BindView(R.id.unSend)
    TextView unSend;
    @BindView(R.id.finish)
    TextView finish;
    @BindView(R.id.llSend)
    LinearLayout llSend;
    @BindView(R.id.totalMoney)
    TextView totalMoney;
    @BindView(R.id.online)
    TextView online;
    @BindView(R.id.cash)
    TextView cash;
    @BindView(R.id.llMoney)
    LinearLayout llMoney;
    @BindView(R.id.totalBucket)
    TextView totalBucket;
    @BindView(R.id.selfBucket)
    TextView selfBucket;
    @BindView(R.id.otherBucket)
    TextView otherBucket;
    @BindView(R.id.llBucket)
    LinearLayout llBucket;
    @BindView(R.id.changeBtn)
    TextView changeBtn;
    @BindView(R.id.rlSelfBucket)
    RelativeLayout rlSelfBucket;
    @BindView(R.id.rlOtherBucket)
    RelativeLayout rlOtherBucket;
    @BindView(R.id.msgBtn)
    ImageView msgBtn;
    @BindView(R.id.setBtn)
    ImageView setBtn;
    private Unbinder unbinder;
    private UserBean userBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_my, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            unbinder = ButterKnife.bind(this, mRootView);
        }


        return mRootView;
    }

    private void getUserInfo() {
        RetrofitUtils.getInstances().create().getUserInfo().enqueue(new Callback<EntityObject<UserBean>>() {
            @Override
            public void onResponse(Call<EntityObject<UserBean>> call, Response<EntityObject<UserBean>> response) {
                hintLoad();
                EntityObject<UserBean> body = response.body();
                if (body.getCode() == 200) {
                    userBean = body.getResult();
                    phone.setText(userBean.getPhone());
                    name.setText(userBean.getUname());
                    totalSend.setText("" + userBean.getToday_water());
                    unSend.setText("" + userBean.getStay_water());
                    finish.setText("" + userBean.getAlready_water());
                    totalMoney.setText("" + userBean.getToday_price());
                    online.setText("" + userBean.getPayment());
                    cash.setText("" + userBean.getDriver_payment());

                    totalBucket.setText("" + userBean.getToday_buck());
                    selfBucket.setText("" + userBean.getSelf_buck());
                    otherBucket.setText("" + userBean.getZnum_buck());
//                    changeBtn.setVisibility(userBean.getIs_policy() == 1 ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<UserBean>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @OnClick({R.id.msgBtn, R.id.setBtn, R.id.changeBtn, R.id.rlSelfBucket, R.id.rlOtherBucket, R.id.totalSend, R.id.llMoney, R.id.llBucket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changeBtn:
                startActivity(new Intent(getActivity(), BucketManageActivity.class));
                break;
            case R.id.totalSend:
                RecordActivity.startAct(getContext(), 1, null);
                break;
            case R.id.llMoney:
                RecordActivity.startAct(getContext(), 2, null);
                break;
            case R.id.llBucket:
                RecordActivity.startAct(getContext(), 3, null);
                break;
          /*  case R.id.rlSelfBucket:
                RecordActivity.startAct(getContext(), 4);
                break;
            case R.id.rlOtherBucket:
                RecordActivity.startAct(getContext(), 5);
                break;*/
            case R.id.msgBtn:
                startActivity(new Intent(getActivity(), MsgActivity.class));
                break;
            case R.id.setBtn:
                startActivity(new Intent(getActivity(), SetActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoad();
        getUserInfo();
    }
}
