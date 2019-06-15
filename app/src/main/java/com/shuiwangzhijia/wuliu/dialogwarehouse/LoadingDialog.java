package com.shuiwangzhijia.wuliu.dialogwarehouse;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 加载中弹窗
 * created by wangsuli on 2018/8/20.
 */
public class LoadingDialog extends BottomDialog {


    @BindView(R.id.ivGif)
    ImageView ivGif;

    public LoadingDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        Glide.with(context).load(R.drawable.load).into(ivGif);
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
