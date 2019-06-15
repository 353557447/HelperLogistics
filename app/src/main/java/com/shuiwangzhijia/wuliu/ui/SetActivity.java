package com.shuiwangzhijia.wuliu.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import butterknife.BindView;
import butterknife.OnClick;

@BaseViewInject(contentViewId = R.layout.activity_set,title = "设置")
public class SetActivity extends BaseActivity {
    @BindView(R.id.loginOutBtn)
    TextView loginOutBtn;

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        mBaseBar.setBarListener(new BaseBar.BarListener() {
            @Override
            public void onLeftClick() {
                closeActivity();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.aboutBtn, R.id.loginOutBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aboutBtn:
                startActivity(new Intent(SetActivity.this, AboutActivity.class));
                break;
            case R.id.loginOutBtn:
                CommonUtils.putToken("");
                startActivity(new Intent(SetActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}
