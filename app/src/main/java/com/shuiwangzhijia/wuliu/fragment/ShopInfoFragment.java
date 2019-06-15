package com.shuiwangzhijia.wuliu.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.utils.LocationUtils;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 商家信息
 * created by wangsuli on 2018/8/20.
 */
public class ShopInfoFragment extends BaseFragment implements TencentLocationListener {
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.callBtn)
    TextView callBtn;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.mapView)
    MapView mapView;
    private Unbinder unbinder;
    private TencentMap mTencentMap;
    private TencentLocationManager mLocationManager;
    private TencentLocation mLocation;
    private Marker mLocationMarker;
    private String mRequestParams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_shop_info, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initMap();
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

    private void initMap() {
        mTencentMap = mapView.getMap();
        mTencentMap.setZoom(13);
        mLocationManager = TencentLocationManager.getInstance(getContext());
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error,
                                  String reason) {
        if (error == TencentLocation.ERROR_OK) {
            mLocation = location;
            // 定位成功
            StringBuilder sb = new StringBuilder();
            sb.append("定位参数=").append(mRequestParams).append("\n");
            sb.append("(纬度=").append(location.getLatitude()).append(",经度=")
                    .append(location.getLongitude()).append(",精度=")
                    .append(location.getAccuracy()).append("), 来源=")
                    .append(location.getProvider()).append(", 地址=")
                    .append(location.getAddress());
            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // 更新 status
            address.setText(location.getAddress());

            // 更新 location Marker
            if (mLocationMarker == null) {
                mLocationMarker = mTencentMap.addMarker(new MarkerOptions().
                        position(latLngLocation).title(location.getAddress()).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.dianpudingwei)));
            } else {
                mLocationMarker.setPosition(latLngLocation);
            }
            mTencentMap.animateTo(latLngLocation);
            mTencentMap.setCenter(latLngLocation);
        }
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        // ignore
    }

    // ====== location callback

    private void startLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME);
        request.setInterval(5000);
        mLocationManager.requestLocationUpdates(request, this);
        mRequestParams = request.toString() + ", 坐标系=" + LocationUtils.toString(mLocationManager.getCoordinateType());
    }

    private void stopLocation() {
        mLocationManager.removeUpdates(this);
    }

    @OnClick(R.id.callBtn)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + "13766665222");
        intent.setData(data);
        startActivity(intent);
    }
}
