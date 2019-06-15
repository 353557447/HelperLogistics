package com.shuiwangzhijia.wuliu.annotation;


import com.shuiwangzhijia.wuliu.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




/**
 * Created by Lijn on 2019/1/10.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BaseViewInject {
    //布局id
    int contentViewId() default -1;
    //标题栏返回键后面的内容
    String backTitle() default "";
    //标题栏的title
    String title() default "";
    //标题栏的背景色
    int barColor() default R.color.white;
    //标题栏右边文字
    String barRightTv() default "";
}
