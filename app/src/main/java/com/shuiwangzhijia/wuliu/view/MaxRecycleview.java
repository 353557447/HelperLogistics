package com.shuiwangzhijia.wuliu.view;

import android.support.v7.widget.RecyclerView;

/**
 * Created by xxc on 2019/2/22.
 */

public class MaxRecycleview extends RecyclerView {
    public MaxRecycleview(android.content.Context context, android.util.AttributeSet attrs){
        super(context, attrs);
    }
    public MaxRecycleview(android.content.Context context){
        super(context);
    }
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
