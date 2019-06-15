package com.shuiwangzhijia.wuliu.ui;

import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.view.BaseBar;

/**关于我们
 * created by wangsuli on 2018/8/23.
 */
@BaseViewInject(contentViewId = R.layout.activity_about,title = "关于我们")
public class AboutActivity extends BaseActivity {

    private TextView versionName;

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
        versionName =(TextView)findViewById(R.id.versionName);
        versionName.setText("V"+ CommonUtils.getAppVersionName(this));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
