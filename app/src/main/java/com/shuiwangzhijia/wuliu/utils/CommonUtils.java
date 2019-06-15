package com.shuiwangzhijia.wuliu.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;


import com.shuiwangzhijia.wuliu.base.Constant;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.OrderShowBean;
import com.shuiwangzhijia.wuliu.bean.SendOrderBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangsuli on 2018/4/19.
 */

public class CommonUtils {

    /**
     * 判断手机号码是否正确
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(147)|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static void putMobile(String Mobile) {
        PreferenceUtils.putString(SettingItems.Mobile, Mobile);
    }

    /**
     * @return
     */
    public static String getMobile() {
        return PreferenceUtils.getString(SettingItems.Mobile);

    }
    /**
     * 保存Token
     *
     * @param token
     */
    public static void putToken(String token) {
        PreferenceUtils.putString(SettingItems.Token, token);
    }

    /**
     * 保存是哪个端
     *
     * @param token
     */
    public static void putRoleId(int roleId) {
        PreferenceUtils.putInt(SettingItems.ROLEID, roleId);
    }

    public static int getRoleId() {
       return PreferenceUtils.getInt(SettingItems.ROLEID);
    }


    /**
     * 保存userid
     *
     * @param token
     */
    public static void putUserId(String roleId) {
        PreferenceUtils.putString(SettingItems.USERID, roleId);
    }

    public static void getUserId() {
        PreferenceUtils.getString(SettingItems.USERID);
    }

    /**
     * 是否已登录
     *
     * @param data
     */
    public static void putLogin(boolean isLogin) {
        PreferenceUtils.putBoolean(SettingItems.ISLOGIN, isLogin);
    }

    public static boolean isLogin() {
        return PreferenceUtils.getBoolean(SettingItems.ISLOGIN);
    }


    /**
     * 将商品保存到本地
     *
     * @param data
     */
    public static void saveShopCart(List<GoodsBean> data) {
        PreferenceUtils.saveObject(SettingItems.ShopCart, data);
    }

    public static List<GoodsBean> getShopCart() {
        return (List<GoodsBean>) PreferenceUtils.getObject(SettingItems.ShopCart);
    }

    /*
    * 将确认送达的商品保存在本地
    * */

    public static void savePeisong(List<OrderShowBean.GoodsBean> data) {
        PreferenceUtils.saveObject(SettingItems.PeiSong, data);
    }

    public static List<OrderShowBean.GoodsBean> getPeiSong() {
        return (List<OrderShowBean.GoodsBean>) PreferenceUtils.getObject(SettingItems.PeiSong);
    }

    /*
    * 将回桶的商品保存在本地
    * */

    public static void saveHuiTong(List<OrderShowBean.RecyclerBean> data) {
        PreferenceUtils.saveObject(SettingItems.HuiTong, data);
    }

    public static List<OrderShowBean.RecyclerBean> getHuiTong() {
        return (List<OrderShowBean.RecyclerBean>) PreferenceUtils.getObject(SettingItems.HuiTong);
    }

    /**
     *  将待配送订单保存在本地
     * */
    public static void savePendingOrder(List<SendOrderBean> data){
        PreferenceUtils.saveObject(SettingItems.PendingOrder,data);
    }

    public static List<SendOrderBean> getPendingOrder(){
        return (List<SendOrderBean>) PreferenceUtils.getObject(SettingItems.PendingOrder);
    }

    /**
     * 将购买的商品保存到本地
     *
     * @param data
     */
    public static void saveBuyGoodsList(List<GoodsBean> data) {
        PreferenceUtils.saveObject(SettingItems.BuyGoodsList, data);
    }

    public static List<GoodsBean> getBuyGoodsList() {
        return (List<GoodsBean>) PreferenceUtils.getObject(SettingItems.BuyGoodsList);
    }
    public static void putLongitude(String lng) {
        PreferenceUtils.putString(SettingItems.Longitude, lng);
    }

    public static void putLatitude(String lat) {
        PreferenceUtils.putString(SettingItems.Latitude, lat);
    }

    /**
     * 获取Latitude
     *
     * @return
     */
    public static String getLatitude() {
        return PreferenceUtils.getString(SettingItems.Latitude);

    }

    /**
     * 获取Longitude
     *
     * @return
     */
    public static String getLongitude() {
        return PreferenceUtils.getString(SettingItems.Longitude);

    }

    /**
     * 获取Token
     *
     * @return
     */
    public static String getToken() {
        String token = PreferenceUtils.getString(SettingItems.Token);
        if (!TextUtils.isEmpty(token)) {
            return token;
        }
        return "";
    }

    public static String getUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * 更新对比大小
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean isUpdate(String first, String second) {
        int firstNum = Integer.parseInt(first.replace(".", ""));
        int secondNum = Integer.parseInt(second.replace(".", ""));
        return firstNum - secondNum > 0;
    }

    /**
     * 便于删除旧app文件
     * 版本更新文件名
     *
     * @return
     */
    public static String getAppFile() {
        return PreferenceUtils.getString(SettingItems.AppFile);
    }

    public static void setAppFile(String filePath) {
        PreferenceUtils.putString(SettingItems.AppFile, filePath);
    }

    /**
     * 返回当前程序版本名
     */
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
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 删除旧的app文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteOldUpdateFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(Constant.UPDATE_FILE_DIR, filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File saveFile(File file, Bitmap photo) {
        FileOutputStream fOut = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fOut = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        photo.compress(Bitmap.CompressFormat.PNG, 100, fOut);// 把Bitmap对象解析成流
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
    public static class SettingItems {
        // 保存在静态变量中
        public static final String User = "user";
        public static final String Mobile = "mobile";
        public static final String Token = "token";
        public static final String ShopCart = "ShopCart";
        public static final String PeiSong = "PeiSong";
        public static final String HuiTong = "HuiTong";
        public static final String PendingOrder = "pendingOrder";
        public static final String BuyGoodsList = "BuyGoodsList";
        public static final String EmployeeId = "employeeId";
        public static final String JPushAlias = "alias";
        public static final String UserHost = "userHost";
        public static final String UploadLocationTime = "uploadLocationTime";
        public static final String AppFile = "AppFile";
        public static final String Longitude = "longitude";
        public static final String Latitude = "latitude";
        public static final String Distance = "distance";
        public static final String ISLOGIN = "islogin";
        public static final String ROLEID = "roleid";
        public static final String USERID = "userid";
    }
}
