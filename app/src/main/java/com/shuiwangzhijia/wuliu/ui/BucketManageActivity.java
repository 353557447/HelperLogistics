package com.shuiwangzhijia.wuliu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.BaseAct;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 空桶管理
 * created by wangsuli on 2018/10/17.
 */
public class BucketManageActivity extends BaseAct {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_manage);
        ButterKnife.bind(this);
        setTitle("空桶管理");
    }


    @OnClick({R.id.changeBtn, R.id.saveBtn, R.id.returnBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changeBtn:
                YiChangeActivity.startAct(this, 1,null);
                break;
            case R.id.saveBtn:
                YiChangeActivity.startAct(this, 2,null);
                break;
            case R.id.returnBtn:
                startActivity(new Intent(this, ReturnBucketActivity.class));
                break;
        }
    }
}
