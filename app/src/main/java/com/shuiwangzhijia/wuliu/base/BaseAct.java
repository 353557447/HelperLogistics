package com.shuiwangzhijia.wuliu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.tencent.android.tpush.XGPushManager;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.dialog.LoadingDialog;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.ui.LoginActivity;
import com.shuiwangzhijia.wuliu.ui.SplashActivity;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * created by wangsuli on 2018/8/17.
 */
public class BaseAct extends AppCompatActivity implements View.OnClickListener {
    ImageView backIv;
    ImageView rightIv;
    TextView title;
    TextView rightTv;
    LinearLayout titleBar;
    FrameLayout frameLayout;
    public static final int PageSize = 10;
    public boolean loading;
    public int page = 0;
    private LoadingDialog mLoadingDialog;
    private View line;
    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        initBar();
        EventBus.getDefault().register(this);
        if (this instanceof LoginActivity//
                || this instanceof SplashActivity
                ) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar
                    .statusBarColor(R.color.transparent)
                    .statusBarDarkFont(true)
                    .init();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(Object messageEvent) {
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(LoginOutEvent event) {
        //解绑
        XGPushManager.delAccount(this, CommonUtils.getMobile());
        //反注册，建议在不需要接收推送的时候调用
        XGPushManager.unregisterPush(this);
        LoginActivity.startActLoginOut(this);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initBar() {
        titleBar = (LinearLayout) findViewById(R.id.titleBar);
        title = (TextView) findViewById(R.id.title);
        rightTv = (TextView) findViewById(R.id.rightTv);
        backIv = (ImageView) findViewById(R.id.backIv);
        rightIv = (ImageView) findViewById(R.id.rightIv);
        line = (View) findViewById(R.id.line);
        backIv.setOnClickListener(this);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
    }

    protected void setNoTitleBar() {
        titleBar.setVisibility(View.GONE);
    }

    protected void setTitleBarBgColor(int id) {
        titleBar.setBackgroundColor(getResources().getColor(id));
        line.setBackgroundColor(getResources().getColor(id));
    }

    protected void setNobackIv() {
        backIv.setVisibility(View.GONE);
    }

    protected void setTitle(String titleStr) {
        title.setText(titleStr);
    }

    protected void setRightTitle(String titleStr) {
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText(titleStr);
    }

    protected void setRightTextColor(int id) {
        rightTv.setTextColor(getResources().getColor(id));
    }

    protected void setRightClickListener(View.OnClickListener listener) {
        rightTv.setOnClickListener(listener);
    }

    protected void setRightIvClickListener(int id, View.OnClickListener listener) {
        rightIv.setVisibility(View.VISIBLE);
        rightIv.setImageResource(id);
        rightIv.setOnClickListener(listener);
    }

    protected void setLeftImageResource(int id) {
        backIv.setImageResource(id);
    }

    @Override
    public void setContentView(int layoutResID) {
        View content = getLayoutInflater().inflate(layoutResID, frameLayout, false);
        frameLayout.removeAllViews();
        frameLayout.addView(content);
    }

    @Override
    public void setContentView(View view) {
        frameLayout.removeAllViews();
        frameLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        frameLayout.removeAllViews();
        frameLayout.addView(view, params);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backIv:
                finish();
                break;
        }
    }

    public void showLoad() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void hintLoad() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
