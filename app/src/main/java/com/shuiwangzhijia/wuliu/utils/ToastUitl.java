package com.shuiwangzhijia.wuliu.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.App;


/**
 * 吐司弹窗
 * created by wangsuli on 2018/8/30.
 */
public class ToastUitl {


    private static Toast toast;

    /**
     * 初始化Toast(消息，时间)
     */
    private static Toast initToast(CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(), message, duration);
        } else {
            //设置文字
            toast.setText(message);
            //设置存续期间
            toast.setDuration(duration);
        }
        return toast;
    }

    /**
     * 短时间显示Toast(消息 String等)
     */
    public static void showShort(CharSequence message) {
        initToast(message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 短时间显示Toast（资源id)
     */
    public static void showShort(int strResId) {
        initToast(App.getInstance().getResources().getText(strResId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast(消息 String等)
     */
    public static void showLong(CharSequence message) {
        initToast(message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast（资源id)
     */
    public static void showLong(int strResId) {
        initToast(App.getInstance().getResources().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间(消息 String等，时间)
     */
    public static void show(CharSequence message, int duration) {
        initToast(message, duration).show();
    }

    /**
     * 自定义显示Toast时间(消息 资源id，时间)
     */
    public static void show(int strResId, int duration) {
        initToast(App.getInstance().getResources().getText(strResId), duration).show();
    }

    /**
     * 显示有image的toast 这是个view
     */
    public static Toast showToastCustom(final String tvStr) {
        if (toast == null) {
            toast = new Toast(App.getInstance());
        }
        View view = LayoutInflater.from(App.getInstance()).inflate(R.layout.toast_custom, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_custom_tv);
        tv.setText(TextUtils.isEmpty(tvStr) ? "" : tvStr);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        return toast;

    }
}
