package com.shuiwangzhijia.wuliu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.LocationAddressAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.AddressBean;
import com.shuiwangzhijia.wuliu.event.AddressEvent;
import com.shuiwangzhijia.wuliu.utils.LocationUtils;
import com.shuiwangzhijia.wuliu.view.ClearEditText;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * created by wangsuli on 2018/8/31.
 */
public class SearchShopActivity extends BaseAct implements LocationAddressAdapter.OnRecyclerViewItemClickListener, TencentLocationListener {

    @BindView(R.id.showAddress)
    TextView showAddress;
    @BindView(R.id.searchEdit)
    ClearEditText searchEdit;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.receiveAddress)
    TextView receiveAddress;
    @BindView(R.id.llResult)
    LinearLayout llResult;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private LocationAddressAdapter adapter;
    private TencentLocationManager mLocationManager;
    private String mRequestParams;
    private TencentLocation mLocation;
    private AddressBean addressBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        ButterKnife.bind(this);
        setTitle("选择水店");
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = editable.toString();
                if (!TextUtils.isEmpty(search)) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    llResult.setVisibility(View.GONE);
                }
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(divider);
        adapter = new LocationAddressAdapter(this);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.location, R.id.receiveAddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.location:
                startLocation();
                break;
            case R.id.receiveAddress:
                startActivity(new Intent(SearchShopActivity.this, AddressManageActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        mRecyclerView.setVisibility(View.GONE);
        llResult.setVisibility(View.VISIBLE);
        showAddress.setText("空气水" + position + "分店");

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(AddressEvent event) {
        if (event.isDelete) {
            if (addressBean == null) {
                showAddress.setText("请选择收货人地址信息");
                return;
            }
            if (event.data.getId() == addressBean.getId()) {
                showAddress.setText("请选择收货人地址信息");
            }
        } else {
            if (event.data != null) {
                addressBean = event.data;
                showAddress.setText(addressBean.getDaddress());
            }
        }

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
//            ulnglat=location.getLongitude()+","+location.getLatitude();
            showAddress.setText(location.getProvince() + "-" + location.getCity() + "-" + location.getDistrict());
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
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();
    }


}
