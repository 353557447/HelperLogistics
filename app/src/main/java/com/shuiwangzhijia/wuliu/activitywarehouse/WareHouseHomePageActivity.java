package com.shuiwangzhijia.wuliu.activitywarehouse;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.UpdateInfo;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseUserBean;
import com.shuiwangzhijia.wuliu.dialog.UpdatingDialog;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.LoginActivity;
import com.shuiwangzhijia.wuliu.ui.SetActivity;
import com.shuiwangzhijia.wuliu.uiwarehouse.RecordActivity;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@BaseViewInject(contentViewId = R.layout.activity_ware_house_home_page)
public class WareHouseHomePageActivity extends BaseActivity {
    @BindView(R.id.driver_name)
    TextView mDriverName;
    @BindView(R.id.total_balance)
    TextView mTotalBalance;
    @BindView(R.id.total_balance_ll)
    LinearLayout mTotalBalanceLl;
    @BindView(R.id.total_return_money)
    TextView mTotalReturnMoney;
    @BindView(R.id.total_return_money_ll)
    LinearLayout mTotalReturnMoneyLl;
    @BindView(R.id.discount_coupon_counts)
    TextView mDiscountCouponCounts;
    @BindView(R.id.discount_coupon_counts_ll)
    LinearLayout mDiscountCouponCountsLl;
    @BindView(R.id.already_return_bucket)
    LinearLayout mAlreadyReturnBucket;
    @BindView(R.id.wait_for_get)
    LinearLayout mWaitForGet;
    @BindView(R.id.completed)
    LinearLayout mCompleted;
    @BindView(R.id.send_wait_for_get)
    LinearLayout mSendWaitForGet;
    @BindView(R.id.send_distributioning)
    LinearLayout mSendDistributioning;
    @BindView(R.id.send_completed)
    LinearLayout mSendCompleted;
    @BindView(R.id.setting)
    ImageView mSetting;
    @BindView(R.id.title_bar_rl)
    RelativeLayout mTitleBarRl;
    @BindView(R.id.driver_phone)
    TextView mDriverPhone;

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        setBaseBarHide();
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            for (int i = 0; i < permissions.length; i++) {
                if (checkSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, i);
                }
            }
        }
        checkVersion();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void checkVersion() {
        RetrofitUtils.getInstances().create().getAppVersionInfo( 0, 2).enqueue(new Callback<EntityObject<UpdateInfo>>() {
            @Override
            public void onResponse(Call<EntityObject<UpdateInfo>> call, Response<EntityObject<UpdateInfo>> response) {
                EntityObject<UpdateInfo> body = response.body();
                if (body.getCode() == 200) {
                    UpdateInfo result = body.getResult();
                    if (result == null) {
                        return;
                    }
                    String versionName = CommonUtils.getAppVersionName(WareHouseHomePageActivity.this);
                    int download_type = result.getDownload_type();
                    String version = result.getVersion();
                    int isUpdate = -1;
                    if (CommonUtils.isUpdate(version, versionName)) {
                        //最新版本比当前高 建议更新
                        isUpdate = 1;
                    }
                    if (CommonUtils.isUpdate(version, versionName)) {
                        //支持的最低版本比当前高 强制更新
                        isUpdate = 2;
                    }
                    if (isUpdate != -1) {
                        UpdatingDialog updatingDialog = new UpdatingDialog(WareHouseHomePageActivity.this, result, isUpdate, false,download_type);
                        updatingDialog.show();
                    }

                }

            }

            @Override
            public void onFailure(Call<EntityObject<UpdateInfo>> call, Throwable t) {

            }
        });

    }


    private void getUserInfo() {
        showLoadingDialog();
        RetrofitUtils.getInstances().create().warehouseGetUserInfo().enqueue(new Callback<EntityObject<WarehouseUserBean>>() {
            @Override
            public void onResponse(Call<EntityObject<WarehouseUserBean>> call, Response<EntityObject<WarehouseUserBean>> response) {
                dismissLoadingDialog();
                EntityObject<WarehouseUserBean> body = response.body();
                if (body.getCode() == 200) {
                    WarehouseUserBean userBean = body.getResult();
                    if (userBean.getPersonalInfo() != null) {
                        String phoneNumber = userBean.getPersonalInfo().getPhone();
                        mDriverPhone.setText(phoneNumber);
                        mDriverName.setText(userBean.getPersonalInfo().getUname());
                    }
                    if (userBean.getOutOrderInfo() != null) {
                        WarehouseUserBean.OutOrderInfoBean orderInfo = userBean.getOutOrderInfo();
                        mDiscountCouponCounts.setText(orderInfo.getUn_today());
                        mTotalReturnMoney.setText(orderInfo.getToday());
                        mTotalBalance.setText(orderInfo.getPre_day());
                    }
                }
                if(body.getScode()==-200){
                    skipActivity(LoginActivity.class);
                    closeActivity();
                }
            }

            @Override
            public void onFailure(Call<EntityObject<WarehouseUserBean>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.total_balance_ll, R.id.total_return_money_ll, R.id.discount_coupon_counts_ll, R.id.already_return_bucket, R.id.wait_for_get, R.id.completed, R.id.send_wait_for_get, R.id.send_distributioning, R.id.send_completed, R.id.setting})
    public void onViewClicked(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.total_balance_ll:
                RecordActivity.startAct(this,2);
                break;
            case R.id.total_return_money_ll:
                RecordActivity.startAct(this,1);
                break;
            case R.id.discount_coupon_counts_ll:
                RecordActivity.startAct(this,0);
                break;
            case R.id.already_return_bucket:
                bundle = new Bundle();
                bundle.putInt("position", 0);
                skipActivity(SelfDeliverOrderActivity.class, bundle);
                break;
            case R.id.wait_for_get:
                bundle = new Bundle();
                bundle.putInt("position", 1);
                skipActivity(SelfDeliverOrderActivity.class, bundle);
                break;
            case R.id.completed:
                bundle = new Bundle();
                bundle.putInt("position", 2);
                skipActivity(SelfDeliverOrderActivity.class, bundle);
                break;
            case R.id.send_wait_for_get:
                bundle = new Bundle();
                bundle.putInt("position", 0);
                skipActivity(DeliverOrderActivity.class, bundle);
                break;
            case R.id.send_distributioning:
                bundle = new Bundle();
                bundle.putInt("position", 1);
                skipActivity(DeliverOrderActivity.class, bundle);
                break;
            case R.id.send_completed:
                bundle = new Bundle();
                bundle.putInt("position", 2);
                skipActivity(DeliverOrderActivity.class, bundle);
                break;
            case R.id.setting:
                skipActivity(SetActivity.class);
                break;
        }
    }
}
