package com.shuiwangzhijia.wuliu.ui;

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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.MainTabDb;
import com.shuiwangzhijia.wuliu.bean.UpdateInfo;
import com.shuiwangzhijia.wuliu.dialog.NoticeDialog;
import com.shuiwangzhijia.wuliu.dialog.UpdatingDialog;
import com.shuiwangzhijia.wuliu.event.CashDeliveryEvent;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.LocationUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.shuiwangzhijia.wuliu.view.FragmentTabHost;
import com.shuiwangzhijia.wuliu.xinge.XGNotification;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * created by wangsuli on 2018/8/17.
 */
public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener, TencentLocationListener {
    private FragmentTabHost mainTabHost;
    private int index = 0;
    private TencentLocationManager mLocationManager;
    private String mRequestParams;
    private TencentLocation mLocation;
    private MsgReceiver mMsgReceiver;
    //语音播报
    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private NetworkConnectChangedReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initHost();
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

    private void initNetStatus() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetworkConnectChangedReceiver();
        registerReceiver(mReceiver, filter);
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
        XGPushManager.bindAccount(MainActivity.this, CommonUtils.getMobile());
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
                     String versionName = CommonUtils.getAppVersionName(MainActivity.this);
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
                        UpdatingDialog updatingDialog = new UpdatingDialog(MainActivity.this, result, isUpdate, false,download_type);
                        updatingDialog.show();
                    }

                }

            }

            @Override
            public void onFailure(Call<EntityObject<UpdateInfo>> call, Throwable t) {

            }
        });

    }

    /***
     * 初始化底部导航栏
     */
    private void initHost() {
        mainTabHost = (FragmentTabHost) findViewById(R.id.mainTab);
        //调用setup方法 设置view
        mainTabHost.setup(this, getSupportFragmentManager(), R.id.mainView);
        //去除分割线
        mainTabHost.getTabWidget().setDividerDrawable(null);
        //监听事件
        mainTabHost.setOnTabChangedListener(this);
        initTab();
        //默认选中
        mainTabHost.setCurrentTab(index);
    }

    /**
     * 创建子tab
     */
    private void initTab() {
        String[] tabs = MainTabDb.getTabsTxt();
        for (int i = 0; i < tabs.length; i++) {
            //新建TabSpec
            TabHost.TabSpec tabSpec = mainTabHost.newTabSpec(tabs[i]);
            View view = LayoutInflater.from(this).inflate(R.layout.main_tab, null);
            ((TextView) view.findViewById(R.id.foot_tv)).setText(tabs[i]);
            ((ImageView) view.findViewById(R.id.foot_iv)).setImageResource(MainTabDb.getTabsImg()[i]);
            tabSpec.setIndicator(view);
            //加入TabSpec
            mainTabHost.addTab(tabSpec, MainTabDb.getFramgent()[i], null);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        int indexCurrentTab = MainTabDb.getTabsIndex(tabId);
        //从分割线中获得多少个切换界面
        TabWidget tabw = mainTabHost.getTabWidget();
        for (int i = 0; i < tabw.getChildCount(); i++) {
            View v = tabw.getChildAt(i);
            TextView tv = (TextView) v.findViewById(R.id.foot_tv);
            ImageView iv = (ImageView) v.findViewById(R.id.foot_iv);
            //修改当前的界面按钮颜色图片
            if (i == indexCurrentTab) {
                tv.setTextColor(getResources().getColor(R.color.color_30adfd));
                iv.setImageResource(MainTabDb.getTabsImgLight()[i]);
            } else {
                tv.setTextColor(getResources().getColor(R.color.color_999999));
                iv.setImageResource(MainTabDb.getTabsImg()[i]);
            }
        }

    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    //如果两次按键时间间隔大于2秒，则不退出
                    ToastUitl.showToastCustom("再按一次退出水的快递-司机端");
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {
                    moveTaskToBack(false);
                    return super.onKeyDown(keyCode, event);

                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error,
                                  String reason) {
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

    // ====== location callback

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

    private void stopLocation() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
        unregisterReceiver(mMsgReceiver);
        if (mReceiver != null){
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(Object messageEvent) {
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(LoginOutEvent event) {
        LoginActivity.startActLoginOut(this);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(CashDeliveryEvent event) {
        mainTabHost.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainTabHost.setCurrentTab(index);
            }
        },60);
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
                NoticeDialog noticeDialog = new NoticeDialog(MainActivity.this, orderSn, address, new NoticeDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog) {
                        dialog.dismiss();
                        //新订单-->配送中页面
                        EventBus.getDefault().post(new MainEvent(1));
                        mainTabHost.setCurrentTab(0);
                    }
                });
                noticeDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
