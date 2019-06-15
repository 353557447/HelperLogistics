package com.shuiwangzhijia.wuliu.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 类    名:  NoScrollViewPager
 * 描    述：不可以滚动的ViewPager
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return super.onInterceptTouchEvent(ev);//true/false
        //return true;//事件就来到自己的onTouchEvent,孩子就无法接受到任何事件-->一定不行
        return false;//可行(排除onTouchEvent,return super.onTouchEvent(ev))-->事件给孩子,有可能来到onTouchEvent,但是来不来都可以
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //只要事件不传递给ViewPager,那么他就没法产生滚动效果
        //return super.onTouchEvent(ev);-->一定不可以
        //return true;//消费-->其实也不影响,因为它已经是最上层容器
        return false;//-->一定可以,不接受事件,不消费事件-->不影响事件的传递(继续把事件传递给父容器)
    }
}
