package com.shuiwangzhijia.wuliu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.activitydriver.DriverHomePageActivity;
import com.shuiwangzhijia.wuliu.activitywarehouse.WareHouseHomePageActivity;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;


public class SplashActivity extends BaseAct {
    private static final int sleepTime = 2000;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setNoTitleBar();
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
                if (!TextUtils.isEmpty(CommonUtils.getToken())) {
                    toMain();
                } else {
                    toLogin();
                }

            }


        }).start();

    }

    private void toMain() {
        if(CommonUtils.getRoleId()==4)
            startActivity(new Intent(this, DriverHomePageActivity.class));
        else
            startActivity(new Intent(this, WareHouseHomePageActivity.class));
        finish();
    }

    private void toLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}
