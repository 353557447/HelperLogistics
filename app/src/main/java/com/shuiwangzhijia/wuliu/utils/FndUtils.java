package com.shuiwangzhijia.wuliu.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by cw5242 on 2017/11/6.
 */

public class FndUtils {
    //----------------------------判断OS的常量start---------------------------------------------------------------
    public static final String SYS_EMUI = "HUAWEI";
    public static final String SYS_MIUI = "XIAOMI";
    public static final String SYS_FLYME = "MEIZU";
    public static final String SYS_OTHER = "OTHER";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";
    //----------------------------判断OS的常量end---------------------------------------------------------------

    /**
     * 解决InputMethodManager内存泄露现象
     *
     * @param destContext 上下文
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get
                            .getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        /*if (QLog.isColorLevel()) {
                            QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
                        }*/
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    /**
     * 判断手机号是否符合规范
     *
     * @param phoneNo 输入的手机号
     * @return
     */
    public static boolean isPhoneNumber(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return false;
        }
        if (phoneNo.length() == 11) {
            for (int i = 0; i < 11; i++) {
                if (!PhoneNumberUtils.isISODigit(phoneNo.charAt(i))) {
                    return false;
                }
            }
            //            Pattern p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
            //                    "|(14[5,7])" +
            //                    "|(15[^4,\\D])" +
            //                    "|(17[3,0-8])" +
            //                    "|(18[0-9]))\\d{8}$");
            //            Pattern p = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
            Pattern p = Pattern.compile("^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$");
            Matcher m = p.matcher(phoneNo);
            return m.matches();
        }
        return false;
    }


    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }


    /**
     * 查看密码显示隐藏
     *
     * @return
     */
    public static void showPwd(EditText editText, boolean show) {
        editText.setTypeface(Typeface.DEFAULT);
        editText.setTransformationMethod(show ? HideReturnsTransformationMethod.getInstance()
                : PasswordTransformationMethod.getInstance());
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        editText.setSelection(start, end);
    }

    /**
     * 查看手机系统
     *
     * @return
     */

    public static String getSystem() {
        String SYS = null;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
                SYS = SYS_MIUI;//小米
            } else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    || prop.getProperty(KEY_EMUI_VERSION, null) != null
                    || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
                SYS = SYS_EMUI;//华为
            } else if (getMeizuFlymeOSFlag().toLowerCase().contains("flyme")) {
                SYS = SYS_FLYME;//魅族
            } else {
                SYS = SYS_OTHER;//其它
            }
        } catch (IOException e) {
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }


    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 关闭软键盘
     *
     * @param context
     * @param editText
     */
    public static void closeSoftInput(Context context, EditText editText) {
        //        editText.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    /**
     * 打开软键盘
     *
     * @param context
     * @param editText
     */
    public static void openSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }


    /**
     * 判断软键盘是否弹出
     */
    public static boolean isSHowKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(v.getWindowToken(), 0)) {
            imm.showSoftInput(v, 0);
            return true;
            //软键盘已弹出
        } else {
            return false;
            //软键盘未弹出
        }
    }

    /**
     * 判断当前软键盘是否打开
     *
     * @param activity
     * @return
     */
    public static boolean isSoftInputShow(Activity activity) {

        // 虚拟键盘隐藏 判断view是否为空
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            //       inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);

            return inputmanger.isActive() && activity.getWindow().getCurrentFocus() != null;
        }
        return false;
    }

    /**
     * 判断是否平板设备
     * @param context
     * @return true:平板,false:手机
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * 重置用户信息
     */
    public static void clearUserInfo() {
        //用户设备信息
     /*   ShareUtils.putString(ShareConstants.DEVICE_ID, "");
        ShareUtils.putString(ShareConstants.SIM_SERIAL_NUMBER, "");
        ShareUtils.putString(ShareConstants.ANDROID_ID, "");
        ShareUtils.putString(ShareConstants.DEVICE_UUID, "");
        ShareUtils.putString(ShareConstants.PHONE_VERSION, "");
        ShareUtils.putString(ShareConstants.PHONE_MODEL, "");
        ShareUtils.putString(ShareConstants.APP_VERSION_NAME, "");
        ShareUtils.putString(ShareConstants.PHONE_NAME, "");
        ShareUtils.putString(ShareConstants.PHONE_SYSTEM, "");
        //推送的一些信息
        ShareUtils.putString(ShareConstants.XIAOMI_PUSH_TOKEN, "");
        ShareUtils.putString(ShareConstants.HUAWEI_PUSH_TOKEN, "");
        ShareUtils.putString(ShareConstants.UMENG_PUSH_TOKEN, "");
        ShareUtils.putString(ShareConstants.TYPE, "");
        ShareUtils.putString(ShareConstants.RUN_ID, "");
        ShareUtils.putString(ShareConstants.TASK_ID, "");
        ShareUtils.putString(ShareConstants.PROJECT_ID, "");
        ShareUtils.putString(ShareConstants.PAKGE_NAME, "");
        ShareUtils.putString(ShareConstants.ACTIVITY_ID, "");
        ShareUtils.putString(ShareConstants.DETAIL_NAME, "");
        //用户登录信息
       // ShareUtils.putBoolean(ShareConstants.IS_LOGIN, false);
        //ShareUtils.putString(ShareConstants.USER_NAME, "");//帐号
        //ShareUtils.putString(ShareConstants.PASS_WORD, "");//密码
        ShareUtils.putString(ShareConstants.APP_FACTORY, "");//工厂权限
        ShareUtils.putString(ShareConstants.CARD_SERIAL, "");//二维码
        ShareUtils.putString(ShareConstants.COMPANY_ANNOUNCEMENT, "");
        ShareUtils.putString(ShareConstants.CONTACTS, "");//通讯录权限
        ShareUtils.putString(ShareConstants.LOGIN_NAME, "");//登录名
        ShareUtils.putString(ShareConstants.MOBILE, "");//手机
        ShareUtils.putString(ShareConstants.PROCESS_APPROVAL, "");//流程审批权限
        ShareUtils.putString(ShareConstants.REAL_NAME, "");//真实姓名
        ShareUtils.putString(ShareConstants.SIGNATURE, "");//签名
        ShareUtils.putString(ShareConstants.TOKEN, "");//用户token
        ShareUtils.putString(ShareConstants.POSITITLE, "");//用户token
        ShareUtils.putString(ShareConstants.IMGFILE, "");//头像
        ShareUtils.putString("manage_view", "");
        ShareUtils.putString("mes_production", "");
        ShareUtils.putString("orgTitle", "");
        ShareUtils.putString("project_manager", "");
        ShareUtils.putString("purchasing_application", "");
        ShareUtils.putString("purchasing_info", "");
        ShareUtils.putString("purchasing_query", "");
        ShareUtils.putString("tasktools_send", "");
        ShareUtils.putString("tasktools_user", "");
        ShareUtils.putString("user", "");
        ShareUtils.putString("userId", "");
        //业务信息
        ShareUtils.putInt(ShareConstants.NOTICE_NO_READ_COUNTS, 0);//未读消息数量
        ShareUtils.putString(ShareConstants.NOTICE_TITTLE, "");//最新公告信息标题
        ShareUtils.putString(ShareConstants.URL_MAIN, "");//HOST*/
    }


    /**
     * 格式化银行卡 加*
     * 3749 **** **** 330
     *
     * @param cardNo 银行卡
     * @return 3749 **** **** 330
     */
    public static String formatCard(String cardNo) {
        if (cardNo.length() < 8) {
            return "银行卡号有误";
        }
        String card = "";
        card = cardNo.substring(0, 4) + " **** **** ";
        card += cardNo.substring(cardNo.length() - 4);
        return card;
    }

    /**
     * 银行卡后四位
     *
     * @param cardNo
     * @return
     */
    public static String formatCardEnd4(String cardNo) {
        if (cardNo.length() < 8) {
            return "银行卡号有误";
        }
        String card = "";
        card += cardNo.substring(cardNo.length() - 4);
        return card;
    }
    public static String getUUID() {
        return java.util.UUID.randomUUID().toString();
    }
}
