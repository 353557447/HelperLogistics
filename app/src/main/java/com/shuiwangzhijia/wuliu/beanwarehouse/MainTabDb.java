package com.shuiwangzhijia.wuliu.beanwarehouse;


import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.fragmentwarehouse.DeliverOrderFragment;
import com.shuiwangzhijia.wuliu.fragmentwarehouse.MyFragment;

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
        String[] tabs = {"出货单", "统计"};
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
        Class[] cls = {DeliverOrderFragment.class, MyFragment.class};
        return cls;
    }

    /***
     * 获得所有点击前的图片
     */
    public static int[] getTabsImg() {
        int[] img = {R.drawable.tab0_0, R.drawable.tab1_0};
        return img;
    }

    /***
     * 获得所有点击后的图片
     */
    public static int[] getTabsImgLight() {
        int[] img = {R.drawable.tab0_1, R.drawable.tab1_1};
        return img;
    }
}
