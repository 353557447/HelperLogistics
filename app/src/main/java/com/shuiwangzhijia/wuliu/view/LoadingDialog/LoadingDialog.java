package com.shuiwangzhijia.wuliu.view.LoadingDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.shuiwangzhijia.wuliu.R;


public class LoadingDialog extends Dialog {
    public Context context;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog_loading);
        this.context = context;
        setCancelable(true);
        //setCanceledOnTouchOutside(false);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogWindowStyle);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_new);
    }
}