package com.shuiwangzhijia.wuliu.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.base.App;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.socks.library.KLog;

/**
 * Created by Lijn on 2018/12/25.
 */

public class FndHeader extends InternalAbstract implements RefreshHeader{

    private ImageView mRefreshIv;
    private TextView mRefreshState;

    public FndHeader(Context context) {
        this(context, null);
    }

    protected FndHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected FndHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final ViewGroup thisGroup = this;
        final DensityUtil density = new DensityUtil();
        RelativeLayout refreshLayout= (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.fnd_header, null);
        mRefreshIv = refreshLayout.findViewById(R.id.refresh_iv);
        mRefreshState = refreshLayout.findViewById(R.id.refresh_state);
        Glide.with(App.getContext()).load(R.drawable.fnd_refresh).into(mRefreshIv);
        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_HORIZONTAL);
        thisGroup.addView(refreshLayout,params);
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        super.onMoving(isDragging, percent, offset, height, maxDragHeight);
        KLog.e("isDragging:"+isDragging+" percent:"+percent+" offset:"+offset+" height:"+height+" maxDragHeight:"+maxDragHeight);
        if(offset<150){
            float i = offset / 150f;
            mRefreshIv.setScaleX(i);
            mRefreshIv.setScaleY(i);
        }else if(offset>150&&offset<200){
            float i = offset / 150f;
            mRefreshIv.setScaleX(i);
            mRefreshIv.setScaleY(i);
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        super.onReleased(refreshLayout, height, maxDragHeight);
        KLog.e("refreshLayout:"+refreshLayout+" height:"+height+" maxDragHeight:"+maxDragHeight);
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        super.onStateChanged(refreshLayout, oldState, newState);
        KLog.e("refreshLayout:"+refreshLayout+" oldState:"+oldState+" newState:"+newState);
        switch (newState){
            case PullDownToRefresh:
                mRefreshState.setText("下拉刷新");
                break;
            case ReleaseToRefresh:
                mRefreshState.setText("松手刷新");
                break;
            case  Refreshing:
                mRefreshState.setText("刷新中...");
                break;
          /*  case RefreshFinish:
                mRefreshState.setText("刷新成功");
                break;*/
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        KLog.e("refreshLayout:"+refreshLayout+" success:"+success+"");
        return super.onFinish(refreshLayout, success);
    }
}
