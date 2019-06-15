package com.shuiwangzhijia.wuliu.utils;

import java.math.BigDecimal;

/**计算工具类
 * Created by wangsuli on 2017/8/19.
 */

public class CalculateUtils {
    /**
     * 加法
     *
     * @param var1
     * @param var2
     * @return
     */
    public static double add(double var1, double var2) {
        BigDecimal b1 = new BigDecimal(Double.toString(var1));
        BigDecimal b2 = new BigDecimal(Double.toString(var2));
        BigDecimal add = b1.add(b2).setScale(2,BigDecimal.ROUND_HALF_UP);
        return add.doubleValue();
    }

    public static int add(int var1,int var2){
        BigDecimal b1 = new BigDecimal(Integer.toString(var1));
        BigDecimal b2 = new BigDecimal(Integer.toString(var2));
        BigDecimal add = b1.add(b2).setScale(2,BigDecimal.ROUND_HALF_UP);
        return add.intValue();
    }

    /**
     * 减法
     *
     * @param var1
     * @param var2
     * @return
     */

    public static double sub(double var1, double var2) {
        BigDecimal b1 = new BigDecimal(Double.toString(var1));
        BigDecimal b2 = new BigDecimal(Double.toString(var2));
        BigDecimal subtract = b1.subtract(b2).setScale(2,BigDecimal.ROUND_HALF_UP);
        return subtract.doubleValue();
    }

    /**
     * 乘法
     *
     * @param var1
     * @param var2
     * @return
     */
    public static double mul(double var1, double var2) {
        BigDecimal b1 = new BigDecimal(Double.toString(var1));
        BigDecimal b2 = new BigDecimal(Double.toString(var2));
        BigDecimal mul = b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP);
        return mul.doubleValue();
    }

    /**
     * 保留2位小数
     * @param d
     * @return
     */
    public static String formatDouble(double d) {
        return String.format("%.2f", d);
    }

}
