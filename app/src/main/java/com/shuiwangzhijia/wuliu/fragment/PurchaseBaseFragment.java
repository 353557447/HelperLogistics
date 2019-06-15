package com.shuiwangzhijia.wuliu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.PurchaseAdapter;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.event.GoodsEvent;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.DensityUtils;
import com.shuiwangzhijia.wuliu.utils.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * created by wangsuli on 2018/8/20.
 */
public class PurchaseBaseFragment extends BaseFragment implements PurchaseAdapter.OnSaleClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private Unbinder unbinder;
    private PurchaseAdapter mPurchaseAdapter;
    private String type;
    private int sid;//店id
    private GridLayoutManager layoutManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        completeSwipeRefresh();
                    }

                    break;
            }
        }
    };
    private boolean isRePayOrder;//是否是追加订单 追加订单 购买数量可以随意

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getString("type");
        sid = getArguments().getInt("sid");
        isRePayOrder = getArguments().getBoolean("isRePayOrder");
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_purchase_base, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initRecyclerView();
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            unbinder = ButterKnife.bind(this, mRootView);
        }
        return mRootView;
    }

    private void initRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.color_30adfd));
        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(DensityUtils.dp2px(10), mContext.getResources().getColor(R.color.color_f5f5f5)));
        mPurchaseAdapter = new PurchaseAdapter(getActivity(), true);
        mPurchaseAdapter.setOnSaleClickListener(this);
        mRecyclerView.setAdapter(mPurchaseAdapter);
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
                        showLoad();
                        new LoadDataThread().start();
                    }
                }
            }
        });
    }

    private void getGoodsList() {
        RetrofitUtils.getInstances().create().getGoodsList(type, sid, page, pageSize).enqueue(new Callback<EntityObject<ArrayList<GoodsBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<GoodsBean>>> call, Response<EntityObject<ArrayList<GoodsBean>>> response) {
                hintLoad();
                completeSwipeRefresh();
                EntityObject<ArrayList<GoodsBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<GoodsBean> result = body.getResult();
                    if (result == null) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        return;
                    } else {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        rlEmpty.setVisibility(View.GONE);
                        if (page == 0) {
                            mPurchaseAdapter.setData(result);
                        } else {
                            mPurchaseAdapter.addData(result);
                        }
                        if (result.size() < pageSize) {
                            isLoading = false;
                        } else {
                            isLoading = true;
                        }
                    }
                } else {
                    if (page == 0) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                    }
                    if (body.getScode() == -200) {
                        EventBus.getDefault().post(new LoginOutEvent());
                    }
                }

            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<GoodsBean>>> call, Throwable t) {
                rlEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                Log.i("retrofit json ", t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }


    @Override
    public void onSelectClick(int position) {
        Toast.makeText(getContext(), "加入购物车成功!", Toast.LENGTH_SHORT).show();
        List<GoodsBean> shopCart = CommonUtils.getShopCart();
        if (shopCart == null) {
            shopCart = new ArrayList<>();
        }
        GoodsBean item = mPurchaseAdapter.getItem(position);
        if (isRePayOrder) {
//            追加订单 购买数量可以随意
            item.setCount(1);
        } else {
            item.setCount(item.getLeast_p());
        }
        GoodsBean old = null;
        for (int i = 0; i < shopCart.size(); i++) {
            old = shopCart.get(i);
            if (old.getGid().equals(item.getGid())) {
                item.setCount(old.getCount() + item.getCount());
                shopCart.remove(old);
            }
        }
        shopCart.add(item);
        CommonUtils.saveShopCart(shopCart);
        EventBus.getDefault().post(new GoodsEvent(shopCart.size()));
    }


    private void completeSwipeRefresh() {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        page = -1;
        showLoad();
        new LoadDataThread().start();
    }


    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            page = page + 1;
            getGoodsList();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }

}
