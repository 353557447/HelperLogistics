package com.shuiwangzhijia.wuliu.activitydriver;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.UpdateInfo;
import com.shuiwangzhijia.wuliu.bean.UserBean;
import com.shuiwangzhijia.wuliu.dialog.NoticeDialog;
import com.shuiwangzhijia.wuliu.dialog.UpdatingDialog;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.LoginActivity;
import com.shuiwangzhijia.wuliu.ui.NetworkConnectChangedReceiver;
import com.shuiwangzhijia.wuliu.ui.RecordActivity;
import com.shuiwangzhijia.wuliu.ui.SetActivity;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.LocationUtils;
import com.shuiwangzhijia.wuliu.xinge.XGNotification;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.activity_driver_home_page)
public class DriverHomePageActivity extends BaseActivity implements TencentLocationListener {
    @BindView(R.id.driver_name)
    TextView mDriverName;
    @BindView(R.id.driver_phone_num)
    TextView mDriverPhoneNum;
    @BindView(R.id.warehouse_num)
    TextView mWarehouseNum;
    @BindView(R.id.driver_head)
    ImageView mDriverHead;
    @BindView(R.id.search_content)
    EditText mSearchContent;
    @BindView(R.id.search)
    TextView mSearch;
    @BindView(R.id.create_order)
    LinearLayout mCreateOrder;
    @BindView(R.id.wait_for_get)
    LinearLayout mWaitForGet;
    @BindView(R.id.distributioning)
    LinearLayout mDistributioning;
    @BindView(R.id.completed)
    LinearLayout mCompleted;
    @BindView(R.id.send_distributioning)
    LinearLayout mSendDistributioning;
    @BindView(R.id.send_completed)
    LinearLayout mSendCompleted;
    @BindView(R.id.in_the_credit)
    LinearLayout mInTheCredit;
    @BindView(R.id.driver_order_ll)
    RelativeLayout mDriverOrderLl;
    @BindView(R.id.setting)
    ImageView mSetting;
    @BindView(R.id.title_bar_rl)
    RelativeLayout mTitleBarRl;
    @BindView(R.id.total_send_counts)
    TextView mTotalSendCounts;
    @BindView(R.id.total_send_counts_ll)
    LinearLayout mTotalSendCountsLl;
    @BindView(R.id.return_bucket_counts)
    TextView mReturnBucketCounts;
    @BindView(R.id.return_bucket_counts_ll)
    LinearLayout mReturnBucketCountsLl;
    @BindView(R.id.today_gathering)
    TextView mTodayGathering;
    @BindView(R.id.today_gathering_ll)
    LinearLayout mTodayGatheringLl;
    private TencentLocationManager mLocationManager;
    private String mRequestParams;
    private TencentLocation mLocation;
    private MsgReceiver mMsgReceiver;
    private NetworkConnectChangedReceiver mReceiver;
    private UserBean userBean;

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
        startLocation();
        initXGPush();
        initNetStatus();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        showLoadingDialog();
        RetrofitUtils.getInstances().create().getUserInfo().enqueue(new Callback<EntityObject<UserBean>>() {
            @Override
            public void onResponse(Call<EntityObject<UserBean>> call, Response<EntityObject<UserBean>> response) {
                dismissLoadingDialog();
                EntityObject<UserBean> body = response.body();
                KLog.e(mGson.toJson(body));
                if (body.getCode() == 200) {
                    userBean = body.getResult();
                    mDriverName.setText(userBean.getUname());
                    mDriverPhoneNum.setText(userBean.getPhone());
                    mTotalSendCounts.setText("" + userBean.getToday_water());
                    mReturnBucketCounts.setText("" + userBean.getToday_buck());
                    mTodayGathering.setText("" + userBean.getToday_price());
                }
                if (body.getScode() == -200) {
                    skipActivity(LoginActivity.class);
                    closeActivity();
                }
            }

            @Override
            public void onFailure(Call<EntityObject<UserBean>> call, Throwable t) {

            }
        });
    }

    private void startLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        request.setInterval(5000);
        mLocationManager = TencentLocationManager.getInstance(this);
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mLocationManager.startIndoorLocation();
        int error = mLocationManager.requestLocationUpdates(request, this);
        Log.i("Tencent", "定位error：" + error);
        mRequestParams = request.toString() + ", 坐标系=" + LocationUtils.toString(mLocationManager.getCoordinateType());
    }

    private void initXGPush() {
        //开启信鸽的日志输出，线上版本不建议调用
        XGPushConfig.enableDebug(this, true);
        XGPushConfig.getToken(this);
        //注册数据更新监听器
        mMsgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.funengda.driver.Receiver");
        registerReceiver(mMsgReceiver, intentFilter);
        XGPushManager.bindAccount(DriverHomePageActivity.this, CommonUtils.getMobile());
    }

    private void initNetStatus() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetworkConnectChangedReceiver();
        registerReceiver(mReceiver, filter);
    }


    @Override
    protected void initEvent() {

    }

    private void checkVersion() {
        RetrofitUtils.getInstances().create().getAppVersionInfo(0, 2).enqueue(new Callback<EntityObject<UpdateInfo>>() {
            @Override
            public void onResponse(Call<EntityObject<UpdateInfo>> call, Response<EntityObject<UpdateInfo>> response) {
                EntityObject<UpdateInfo> body = response.body();
                if (body.getCode() == 200) {
                    UpdateInfo result = body.getResult();
                    if (result == null) {
                        return;
                    }
                    String versionName = CommonUtils.getAppVersionName(DriverHomePageActivity.this);
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
                        UpdatingDialog updatingDialog = new UpdatingDialog(DriverHomePageActivity.this, result, isUpdate, false, download_type);
                        updatingDialog.show();
                    }

                }

            }

            @Override
            public void onFailure(Call<EntityObject<UpdateInfo>> call, Throwable t) {

            }
        });

    }


    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        Log.i("Tencent onLocation", "  error：" + error + " reason: " + reason);
        if (error == TencentLocation.ERROR_OK) {
            mLocation = location;
            // 定位成功
            StringBuilder sb = new StringBuilder();
            sb.append("定位参数=").append(mRequestParams).append("\n");
            sb.append("(纬度=").append(location.getLatitude()).append(",经度=")
                    .append(location.getLongitude()).append(",精度=")
                    .append(location.getAccuracy()).append("), 来源=")
                    .append(location.getProvider()).append(", 地区=")
                    .append(location.getProvince() + "-" + location.getCity() + "-" + location.getDistrict()).append(", 地址=")
                    .append(location.getAddress());
            Log.i("Tencent", "定位成功：" + sb.toString());
            // 更新 status
            String ulnglat = location.getLongitude() + "," + location.getLatitude();
            CommonUtils.putLatitude("" + location.getLatitude());
            CommonUtils.putLongitude("" + location.getLongitude());
        }
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        // ignore
        Log.i("Tencent", "定位onStatusUpdate：" + name + "   status: " + status + "  " + desc);
    }

    private void stopLocation() {
        mLocationManager.removeUpdates(this);
    }


    @OnClick({R.id.search, R.id.create_order, R.id.wait_for_get, R.id.distributioning,//
            R.id.completed, R.id.send_distributioning, R.id.send_completed, //
            R.id.in_the_credit, R.id.driver_order_ll, R.id.setting,//
            R.id.total_send_counts_ll, R.id.return_bucket_counts_ll, //
            R.id.today_gathering_ll, R.id.search_content
    })
    public void onViewClicked(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.search:
                String searchContent = mSearchContent.getText().toString().trim();
                if (TextUtils.isEmpty(searchContent)) {
                    showToast("请填写搜索内容~");
                    return;
                }
                bundle = new Bundle();
                bundle.putString("searchContent", searchContent);
                skipActivity(DriverSearchOrder.class, bundle);
                break;
            case R.id.create_order:
                bundle = new Bundle();
                bundle.putInt("position", 0);
                skipActivity(DriverDeliverOrderActivity.class, bundle);
                break;
            case R.id.wait_for_get:
                bundle = new Bundle();
                bundle.putInt("position", 1);
                skipActivity(DriverDeliverOrderActivity.class, bundle);
                break;
            case R.id.distributioning:
                bundle = new Bundle();
                bundle.putInt("position", 2);
                skipActivity(DriverDeliverOrderActivity.class, bundle);
                break;
            case R.id.completed:
                bundle = new Bundle();
                bundle.putInt("position", 3);
                skipActivity(DriverDeliverOrderActivity.class, bundle);
                break;
            case R.id.send_distributioning:
                bundle = new Bundle();
                bundle.putInt("selectPosition", 0);
                skipActivity(DriverNewOrderActivity.class, bundle);
                break;
            case R.id.send_completed:
                bundle = new Bundle();
                bundle.putInt("selectPosition", 1);
                skipActivity(DriverNewOrderActivity.class, bundle);
                break;
            case R.id.in_the_credit:
                bundle = new Bundle();
                bundle.putInt("selectPosition", 2);
                skipActivity(DriverNewOrderActivity.class, bundle);
                break;
            case R.id.driver_order_ll:
                skipActivity(NearbyActivity.class);
                break;
            case R.id.setting:
                startActivity(new Intent(this, SetActivity.class));
                break;
            case R.id.total_send_counts_ll:
                RecordActivity.startAct(this, 1, null);
                break;
            case R.id.return_bucket_counts_ll:
                RecordActivity.startAct(this, 3, null);
                break;
            case R.id.today_gathering_ll:
                RecordActivity.startAct(this, 2, null);
                break;
            case R.id.search_content:
                skipActivity(DSearchOrderActivity.class);
                break;
        }
    }


    /**
     * 推送消息
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            XGNotification data = (XGNotification) intent.getSerializableExtra("data");
            try {
                JSONObject jsonObject = new JSONObject(data.getCustom_content());
                String orderSn = jsonObject.getString("order_sn");
                String address = jsonObject.getString("address");
                NoticeDialog noticeDialog = new NoticeDialog(DriverHomePageActivity.this, orderSn, address, new NoticeDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog) {
                        dialog.dismiss();
                        //新订单-->配送中页面
                        EventBus.getDefault().post(new MainEvent(1));
                    }
                });
                noticeDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMsgReceiver);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
