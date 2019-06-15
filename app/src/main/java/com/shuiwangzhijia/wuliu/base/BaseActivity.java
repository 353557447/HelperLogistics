package com.shuiwangzhijia.wuliu.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.activitydriver.DriverHomePageActivity;
import com.shuiwangzhijia.wuliu.activitywarehouse.WareHouseHomePageActivity;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.event.RefreshDataEvent;
import com.shuiwangzhijia.wuliu.ui.ConfirmOrderActivity;
import com.shuiwangzhijia.wuliu.uiwarehouse.RecordActivity;
import com.shuiwangzhijia.wuliu.utils.DoubleUtils;
import com.shuiwangzhijia.wuliu.utils.MeasureUtil;
import com.shuiwangzhijia.wuliu.view.BaseBar;
import com.shuiwangzhijia.wuliu.view.LoadingDialog.LoadingDialog;

import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public abstract class BaseActivity extends AppCompatActivity {
    private RelativeLayout mBaseContainer;
    private LayoutInflater mLayoutInflater;
    private InputMethodManager mIms;
    protected BaseBar mBaseBar;
    private Unbinder mUnbinder;
    private LoadingDialog mLoadingDialog;
    private View mContentView;
    protected Gson mGson;
    protected Handler mHandler;
    private Dialog mBottomDialog;
    private ImmersionBar mImmersionBar;
    public static final int PageSize = 10;
    public boolean loading;
    public int page = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //竖屏展示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        beforeSetContentView();
        setContentView(R.layout.activity_base_new);
        // overridePendingTransition(R.anim.anim_activity_open, R.anim.anim_activity_close);
        overridePendingTransition(R.anim.anim_activity_open, R.anim.anim_activity_close);
        EventBus.getDefault().register(this);
        if (this instanceof DriverHomePageActivity//
               ||this instanceof WareHouseHomePageActivity//
               ||this instanceof ConfirmOrderActivity//
                ||this instanceof RecordActivity
                ) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar
                    .statusBarColor(R.color.transparent)
                    .statusBarDarkFont(true)
                    .init();
        } else {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar
                    .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true)
                    .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                    .init();
        }
        intParentView();
        commonDataCofig();
        initView();
        initData();
        initEvent();
    }

    protected abstract void beforeSetContentView();

    private void intParentView() {
        mBaseContainer = findViewById(R.id.base_container);
        mBaseBar = findViewById(R.id.base_bar);
        mBaseBar.layout(0,MeasureUtil.dip2px(this, MeasureUtil.getStatusBarHeight(this)),0,0);
        mLayoutInflater = LayoutInflater.from(this);
        //获取子activity是否存在注解
        if (getClass().isAnnotationPresent(BaseViewInject.class)) {
            BaseViewInject annotation = getClass().getAnnotation(BaseViewInject.class);
            int contentViewId = annotation.contentViewId();
            String backTitle = annotation.backTitle();
            String title = annotation.title();
            int barColor = annotation.barColor();
            String barRightTv = annotation.barRightTv();
            mBaseBar.setBackTitle(backTitle);
            mBaseBar.setTitle(title);
            mBaseBar.setBarColorRes(barColor);
            mBaseBar.setRightTv(barRightTv);
            mContentView = mLayoutInflater.inflate(contentViewId, mBaseContainer, false);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.base_bar);
            mBaseContainer.addView(mContentView, layoutParams);
            mUnbinder = ButterKnife.bind(this);
        } else {
            throw new RuntimeException("请设置activity的注解");
        }
    }

    protected void setBaseBarHide() {
        mBaseBar.setVisibility(View.GONE);
    }

    //可单独定义界面切入切出动画
    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    private void commonDataCofig() {
        mGson = new Gson();
        //主线程handler
        mHandler = new Handler();
        // initCancelSureRxdialog();
    }

    protected void skipActivity(Class clz) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent(this, clz);
            startActivity(intent);
        }
    }

    protected void skipActivity(Class clz, Bundle bundle) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent(this, clz);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    protected void skipActivityForResult(Class clz, int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent(this, clz);
            startActivityForResult(intent, requestCode);
        }
    }

    protected void skipActivityForResult(Class clz, Bundle bundle, int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent(this, clz);
            intent.putExtras(bundle);
            startActivityForResult(intent, requestCode);
        }
    }

    protected void closeActivity() {
        finish();
        hideSoftKeyBoard();
        // overridePendingTransition(0, R.anim.anim_activity_close);
    }

    protected void showToast(String text) {
        //ToastUtils.show(text);
    }

    protected void showLoadingDialog() {
        if (!isFinishing()) {
            dismissLoadingDialog();
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.show();
        }
    }

    protected void dismissLoadingDialog() {
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.mIms == null) {
            this.mIms = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.mIms != null)) {
            this.mIms.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEvent(Object messageEvent) {
    }

    private LinearLayout mCenterView;
    private ImageView mCenterViewIv;
    private TextView mCenterViewTv;

    protected void setCentreViewShow(int imageResource, String hint) {
        if (mCenterView == null) {
            mCenterView = (LinearLayout) mLayoutInflater.inflate(R.layout.view_center, mBaseContainer, false);
            mCenterViewIv = mCenterView.findViewById(R.id.center_view_iv);
            mCenterViewTv = mCenterView.findViewById(R.id.center_view_tv);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    , LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            if (mBaseContainer != null) {
                mBaseContainer.addView(mCenterView, layoutParams);
            }
        }
        mCenterViewIv.setImageResource(imageResource);
        mCenterViewTv.setText(hint);
        mCenterView.setVisibility(View.VISIBLE);
    }

    protected void setCentreViewDismiss() {
        if (mCenterView != null) {
            mCenterView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void refreshDataEvent(RefreshDataEvent event) {
        if (this.getClass().getSimpleName().equals(event.getAim())) {
            String methodName = event.getMethodName();
            try {
                Method method = this.getClass().getDeclaredMethod(methodName);
                method.setAccessible(true);
                method.invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*protected RxDialogSureCancel mRxDialogSureCancel;

    protected void initCancelSureRxdialog() {
        if (mRxDialogSureCancel == null) {
            mRxDialogSureCancel = new RxDialogSureCancel(this);
            mRxDialogSureCancel.getLogoView().setVisibility(View.GONE);
            mRxDialogSureCancel.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRxDialogSureCancel.dismiss();
                }
            });
        }
    }*/

    //底部弹出窗口
    protected void showBottomDialog(View mBottomDialogView) {
        if (mBottomDialog == null)
            mBottomDialog = new Dialog(this, R.style.BottomDialog);
        // mBottomDialogView = LayoutInflater.from(this).inflate(layout, null);
        mBottomDialog.setContentView(mBottomDialogView);
        ViewGroup.LayoutParams layoutParams = mBottomDialogView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        mBottomDialogView.setLayoutParams(layoutParams);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //设置外部可以点击
        mBottomDialog.setCanceledOnTouchOutside(true);
        mBottomDialog.show();
    }

    protected void setGone(boolean visible, View... views) {
        if (visible) {
            for (int i = 0; i < views.length; i++) {
                views[i].setVisibility(View.VISIBLE);
            }
        } else {
            for (int i = 0; i < views.length; i++) {
                views[i].setVisibility(View.GONE);
            }
        }
    }


    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        int scrollState = recyclerView.getScrollState();
        if (scrollState == 0) return false;
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    public boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        } else {
            return false;
        }
    }
}
