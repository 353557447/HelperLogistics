package com.shuiwangzhijia.wuliu.utils;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;


public class ToastUtils {
    //简单的toast,使用前在application里面初始化
    private static Toast toast;
    //定制维护其他布局类型的toast,根据项目需求来
    private static Toast customToast;
    private static TextView toastView;

    private ToastUtils() {
    }

    public static void init(Context context){
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toastView = (TextView) LayoutInflater.from(context).inflate(R.layout.view_toast, null);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
    }
    public static void show(CharSequence text){
        if(toast!=null){
            toastView.setText(text);
            toast.show();
        }
    }
}
