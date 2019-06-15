package com.shuiwangzhijia.wuliu.bean;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.fragment.DeliverOrderFragment;
import com.shuiwangzhijia.wuliu.fragment.MyFragment;
import com.shuiwangzhijia.wuliu.fragment.NearByFragment;
import com.shuiwangzhijia.wuliu.fragment.NewOrderFragment;

import java.util.Arrays;
import java.util.List;
/**
 * created by wangsuli on 2018/8/17.
 */
public class MainTabDb {
    /***
     * 获得底部所有项
     */
    public static String[] getTabsTxt() {
        String[] tabs = {"新订单", "代下单","出货单","我的"};
        return tabs;
    }

    public static int getTabsIndex(String txt) {
        List<String> tabs = Arrays.asList(getTabsTxt());
        return tabs.indexOf(txt);
    }

    /***
     * 获得所有碎片
     */
    public static Class[] getFramgent() {
        Class[] cls = {NewOrderFragment.class, NearByFragment.class,DeliverOrderFragment.class,MyFragment.class};
        return cls;
    }

    /***
     * 获得所有点击前的图片
     */
    public static int[] getTabsImg() {
        int[] img = {R.drawable.xindingdan_2, R.drawable.daixiadan_2, R.drawable.chuhuodan_2, R.drawable.wode_2};
        return img;
    }

    /***
     * 获得所有点击后的图片
     */
    public static int[] getTabsImgLight() {
        int[] img = {R.drawable.xindingdan_1, R.drawable.daixiadan_1,R.drawable.chuhuodan_1, R.drawable.wode_1};
        return img;
    }
}
