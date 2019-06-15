package com.shuiwangzhijia.wuliu.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.CompletedAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseLazyFragment;
import com.shuiwangzhijia.wuliu.bean.OutOrderBean;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.event.OrderListFlashEvent;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.CompletedDetailActivity;
import com.shuiwangzhijia.wuliu.utils.MeasureUtil;
import com.shuiwangzhijia.wuliu.utils.SpacesItemDecoration;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import java.util.ArrayList;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@BaseViewInject(contentViewId = R.layout.fragment_finish_order)
public class FinishOrderFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener,CompletedAdapter.OnViewItemClickListener{
    private static final String TAG = "FinishOrderFragment";
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private int type;
    private LinearLayoutManager layoutManager;
    private CompletedAdapter mCompletedAdapter;


    @Override
    protected void initView(View view) {
        initRecyclerView();
    }


    @Override
    protected void lazyLoadData() {
        page = 0;
        showLoadingDialog();
        getList();
    }

    @Override
    protected void initEvent() {

    }


    private void getList() {
        RetrofitUtils.getInstances().create().getOutOrderList(page,pageSize).enqueue(new Callback<EntityObject<ArrayList<OutOrderBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<OutOrderBean>>> call, Response<EntityObject<ArrayList<OutOrderBean>>> response) {
                dismissLoadingDialog();
                EntityObject<ArrayList<OutOrderBean>> body = response.body();
                completeSwipeRefresh();
                if (body != null){
                    if (body.getCode() == 200){
                        ArrayList<OutOrderBean> result = body.getResult();
                        if (result == null || result.size() == 0){
                            rlEmpty.setVisibility(View.VISIBLE);
                            return;
                        }else {
                          //  rlEmpty.setVisibility(View.GONE);
//                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            if (page == 0){
                                mCompletedAdapter.setData(result);
                            }else {
                            mCompletedAdapter.addData(result);
                            }
                            if (result.size()<pageSize){
                                isLoading = false;
                            }else{
                                isLoading = true;
                            }
                        }
                    }else {
                        if (page == 0){
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                        if (body.getScode() == -200){
                            EventBus.getDefault().post(new LoginOutEvent());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<OutOrderBean>>> call, Throwable t) {
                rlEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.color_30adfd));
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(mContext, MeasureUtil.dip2px(mContext,12)));
        mCompletedAdapter = new CompletedAdapter(getContext());
        mCompletedAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mCompletedAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (dy > 0 && lastVisibleItemPosition + 1 == layoutManager.getItemCount()) {
                    if (isLoading) {
                        isLoading = false;
                        showLoadingDialog();
                        page = page + 1;
                        getList();
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        Log.d(TAG,"onRefresh");
        page = 0;
        showLoadingDialog();
        getList();
    }

    private void completeSwipeRefresh() {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDetailClick(int position) {
        CompletedDetailActivity.startAct(getContext(),mCompletedAdapter.getItem(position).getOut_order());
    }

    @Override
    public void onLookDetails(int position) {
        CompletedDetailActivity.startAct(getContext(),mCompletedAdapter.getItem(position).getOut_order());
    }



    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(OrderListFlashEvent event) {
        if (type == event.type) {
            type = event.type;
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        onRefresh();
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }
}
