package com.shuiwangzhijia.wuliu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.shuiwangzhijia.wuliu.R;


/**
 * 底部弹窗基类
 * Created by wangsuli on 2017/12/19.
 */

public class BottomDialog extends Dialog {
    public final Window window;
    public final int width;
    public Context context;

    public BottomDialog(Context context) {
        super(context, R.style.bottom_dialog);
        this.context = context;
        window = getWindow();
        width = window.getWindowManager().getDefaultDisplay().getWidth();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams attributesParams = window.getAttributes();
        attributesParams.gravity = Gravity.BOTTOM;
        attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributesParams.dimAmount = 0.4f;
        attributesParams.width = width;
        attributesParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributesParams.x = 0;
        window.setAttributes(attributesParams);

    }
}
