package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.activitydriver.DriverHomePageActivity;
import com.shuiwangzhijia.wuliu.activitywarehouse.WareHouseHomePageActivity;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.base.Constant;
import com.shuiwangzhijia.wuliu.bean.LoginBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.AsteriskPasswordTransformationMethod;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginActivity extends BaseAct {
    @BindView(R.id.editPhone)
    EditText editPhone;
    @BindView(R.id.editPsw)
    EditText editPsw;
    @BindView(R.id.loginBtn)
    TextView loginBtn;
    private String phone;
    private String psw;
    private boolean loginOut;

    public static void startActLoginOut(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("loginOut", true);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setNoTitleBar();
        ButterKnife.bind(this);
        editPsw.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        loginOut = getIntent().getBooleanExtra("loginOut", false);
        if(loginOut){
            RetrofitUtils.getInstances().create().loginOutInfo(CommonUtils.getMobile()).enqueue(new Callback<EntityObject<String>>() {
                @Override
                public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                    if (response.body() != null)
                    ToastUitl.showToastCustom(response.body().getMsg());
                }
                @Override
                public void onFailure(Call<EntityObject<String>> call, Throwable t) {

                }
            });
        }
    }

    private void hint(String text) {
        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.loginBtn)
    public void onViewClicked() {
        phone = editPhone.getText().toString();
        psw = editPsw.getText().toString();
//        if (!CommonUtils.isMobileNO(phone)) {
//            hint("手机号码格式有误!");
//            return;
//        }
        if (phone.length() != 11){
            hint("手机号码格式有误!");
            return;
        }
        if (psw.length() < 6 || psw.length() > 18) {
            hint("密码长度有误，应6-18位之间!");
            return;
        }
        showLoad();
        KLog.e(phone+"psw:"+psw);
        RetrofitUtils.getInstances().create().getLogin(phone, psw, Constant.WATER_FACTORY_ID).enqueue(new Callback<EntityObject<LoginBean>>() {
            @Override
            public void onResponse(Call<EntityObject<LoginBean>> call, Response<EntityObject<LoginBean>> response) {
                KLog.e("我执行了么");
                hintLoad();
                EntityObject<LoginBean> object = response.body();
                if (object.getCode() == 200) {
                    LoginBean result = object.getResult();
                    KLog.e(new Gson().toJson(result));
                    CommonUtils.putToken(result.getToken());
                    CommonUtils.putMobile(phone);
                    CommonUtils.putUserId(result.getUser_id());
                    int roleId = result.getRole_id();
                    CommonUtils.putRoleId(roleId);
                    CommonUtils.putLogin(true);
                    if (roleId == 4) {
                        startActivity(new Intent(LoginActivity.this,DriverHomePageActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, WareHouseHomePageActivity.class));
                    }
                    finish();
                } else {
                    hint(object.getMsg());
                }

            }

            @Override
            public void onFailure(Call<EntityObject<LoginBean>> call, Throwable t) {
                KLog.e("onFailure"+t.getMessage()+t.getCause());
                KLog.e("我执行了么onFailure");
            }
        });

    }
}
