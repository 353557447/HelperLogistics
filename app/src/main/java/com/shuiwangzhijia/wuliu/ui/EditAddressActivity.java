package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.AddressBean;
import com.shuiwangzhijia.wuliu.event.AddressEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.LocationUtils;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 编辑地址信息
 * created by wangsuli on 2018/8/21.
 */
public class EditAddressActivity extends BaseAct implements TencentLocationListener {
    public static final int TYPE_ADD = 0;
    public static final int TYPE_UPDATE = 1;
    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editPhone)
    EditText editPhone;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.llArea)
    LinearLayout llArea;
    @BindView(R.id.editDetailAddress)
    EditText editDetailAddress;
    @BindView(R.id.saveBtn)
    TextView saveBtn;
    private int sid, type;
    private String id;
    private AddressBean mAddressBean;
    private TencentLocationManager mLocationManager;
    private TencentLocation mLocation;
    private String mRequestParams;
    private String ulnglat;

    public static void startAct(Context context, int sid, int type, AddressBean bean) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("type", type);
        intent.putExtra("data", bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        sid = getIntent().getIntExtra("sid", -1);
        type = getIntent().getIntExtra("type", -1);
        if (type == TYPE_ADD) {
            setTitle("添加地址");
            startLocation();
            ulnglat = CommonUtils.getLongitude() + "," + CommonUtils.getLatitude();
        } else {
            setTitle("编辑地址");
            mAddressBean = (AddressBean) getIntent().getSerializableExtra("data");
            editName.setText(mAddressBean.getAname());
            editPhone.setText(mAddressBean.getSphone());
            area.setText(mAddressBean.getProvince() + "-" + mAddressBean.getCity() + "-" + mAddressBean.getDist());
            editDetailAddress.setText(mAddressBean.getDaddress());
            id = mAddressBean.getId() + "";
            ulnglat = mAddressBean.getUlnglat();
        }

        setEditTextInhibitInputSpace(editName);

    }

    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */

    public static void setEditTextInhibitInputSpace(EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }

            }
        };
        editText.setFilters(new InputFilter[]{filter});

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
            ulnglat = location.getLongitude() + "," + location.getLatitude();
            area.setText(location.getProvince() + "-" + location.getCity() + "-" + location.getDistrict());
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
        if (mLocationManager != null)
            mLocationManager.removeUpdates(this);
    }

    @OnClick({R.id.llArea, R.id.saveBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llArea:
                selectAddress();
                break;
            case R.id.saveBtn:
                if (TextUtils.isEmpty(ulnglat)) {
                    hint("请开启定位服务!");
                    return;
                }
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String address = editDetailAddress.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    hint("请输入收货人姓名!");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    hint("请输入联系方式!");
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    hint("请输入详细地址!");
                    return;
                }
                String areaStr = area.getText().toString();
                String[] split = areaStr.split("-");
                RetrofitUtils.getInstances().create().addAddress(name, phone.replace(" ",""), split[0], split[1], split[2], address, ulnglat, 1, id, sid).enqueue(new Callback<EntityObject<String>>() {
                    @Override
                    public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                        EntityObject<String> body = response.body();
                        if (body.getCode() == 200) {
                            EventBus.getDefault().post(new AddressEvent(null,false));
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<EntityObject<String>> call, Throwable t) {

                    }
                });

                break;
        }
    }

    private void selectAddress() {
        CityPicker cityPicker = new CityPicker.Builder(EditAddressActivity.this)
                .textSize(14)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#30ADFD")
                .cancelTextColor("#666666")
                .province("广东省")
                .city("深圳市")
                .district("福田区")
                .textColor(getResources().getColor(R.color.color_999999))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                //为TextView赋值
                area.setText(province.trim() + "-" + city.trim() + "-" + district.trim());
            }
        });
    }

    private void hint(String text) {
        Toast.makeText(EditAddressActivity.this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();
    }


}
