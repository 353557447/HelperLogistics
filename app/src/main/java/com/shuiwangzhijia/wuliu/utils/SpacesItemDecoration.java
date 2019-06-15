package com.shuiwangzhijia.wuliu.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private Context context;

    public SpacesItemDecoration(Context context,int space) {
        this.space = space;
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childPosition = parent.getChildLayoutPosition(view);
        if(childPosition==0){
            outRect.top = space;
        }else{
            outRect.top = space/2;
        }
        outRect.bottom = space/2;
        outRect.left = space;
        outRect.right = space;
    }
}