package com.shuiwangzhijia.wuliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提示框
 * created by wangsuli on 2018/8/20.
 */
public class NoticeDialog extends BottomDialog {

    @BindView(R.id.cancelBtn)
    TextView cancelBtn;
    @BindView(R.id.okBtn)
    TextView okBtn;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.address)
    TextView address;
    private OnConfirmClickListener listener;

    public NoticeDialog(Context context, String orderSn, String content, final OnConfirmClickListener listener) {
        super(context);
        this.listener = listener;
        setContentView(R.layout.dialog_notice);
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        orderId.setText("订单号:" + orderSn);
        address.setText("送货地址:" + content);
    }

    @OnClick({R.id.cancelBtn, R.id.okBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelBtn:
                dismiss();
                break;
            case R.id.okBtn:
                listener.onConfirm(this);
                break;
        }
    }

    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams attributesParams = window.getAttributes();
        attributesParams.gravity = Gravity.CENTER;
        attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributesParams.dimAmount = 0.4f;
        attributesParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        attributesParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributesParams.x = 0;
        window.setAttributes(attributesParams);
        this.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);
    }
}
