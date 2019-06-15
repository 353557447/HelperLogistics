package com.shuiwangzhijia.wuliu.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.GoodsDetailAdapter;
import com.shuiwangzhijia.wuliu.adapter.GoodsTypeAdapter;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.GoodsManageBean;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;
import com.shuiwangzhijia.wuliu.view.RxFakeAddImageView;
import com.shuiwangzhijia.wuliu.view.RxPointFTypeEvaluator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品管理
 * created by wangsuli on 2018/8/20.
 */
public class ShopGoodsFragment extends BaseFragment implements ShopCartInterface {
    @BindView(R.id.typeRecyclerView)
    RecyclerView typeRecyclerView;
    @BindView(R.id.detailRecyclerView)
    RecyclerView detailRecyclerView;
    @BindView(R.id.shopping_cart_total_tv)
    TextView shoppingCartTotalTv;
    @BindView(R.id.payBtn)
    TextView payBtn;
    @BindView(R.id.shopping_cart_bottom)
    LinearLayout shoppingCartBottom;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.rlNum)
    RelativeLayout rlNum;
    @BindView(R.id.shopping_cart)
    ImageView shoppingCart;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    private GoodsTypeAdapter mGoodsTypeAdapter;
    private GoodsDetailAdapter mGoodsDetailAdapter;
    private boolean isSale = true;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_shop_goods, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initTypeRecyclerView();
            initDetailRecyclerView();
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

    @Override
    public void onResume() {
        super.onResume();
//        getDataList();

    }

    private void getDataList() {
        RetrofitUtils.getInstances().create().getGoodsManageList(isSale ? 0 : 1).enqueue(new Callback<EntityObject<ArrayList<GoodsManageBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<GoodsManageBean>>> call, Response<EntityObject<ArrayList<GoodsManageBean>>> response) {
                EntityObject<ArrayList<GoodsManageBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<GoodsManageBean> result = body.getResult();
                    if (result == null) {
                        return;
                    }
                    mGoodsTypeAdapter.setData(result);
                    mGoodsTypeAdapter.setSelectIndex(0);
                    mGoodsDetailAdapter.setData(result.get(0).getList());
                } else {
                    if (body.getScode() == -200) {
                        EventBus.getDefault().post(new LoginOutEvent());
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<GoodsManageBean>>> call, Throwable t) {

            }
        });
    }

    private void initTypeRecyclerView() {
        typeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        typeRecyclerView.setHasFixedSize(true);
        mGoodsTypeAdapter = new GoodsTypeAdapter(getContext());
        mGoodsTypeAdapter.setOnItemClickListener(new GoodsTypeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GoodsManageBean item = mGoodsTypeAdapter.getItem(position);
                mGoodsDetailAdapter.setData(item.getList());
                mGoodsTypeAdapter.setSelectIndex(position);
            }
        });
        typeRecyclerView.setAdapter(mGoodsTypeAdapter);

    }

    private void initDetailRecyclerView() {
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        detailRecyclerView.setHasFixedSize(true);
        mGoodsDetailAdapter = new GoodsDetailAdapter(getContext(), isSale, true);
        mGoodsDetailAdapter.setOnSaleClickListener(new GoodsDetailAdapter.OnSaleClickListener() {
            @Override
            public void onSelectClick(int position) {

            }
        });
        mGoodsDetailAdapter.setShopCartInterface(this);
        List<GoodsBean> data = new ArrayList<>();
        GoodsBean goodsBean = null;
        for (int i = 0; i < 5; i++) {
            goodsBean = new GoodsBean();
            goodsBean.setGname("空气水" + i);
            goodsBean.setPprice(120 + i + "");
            goodsBean.setCount(1);
            data.add(goodsBean);
        }
        mGoodsDetailAdapter.setData(data);
        detailRecyclerView.setAdapter(mGoodsDetailAdapter);
    }


    @Override
    public void add(View view, int position) {
        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        shoppingCart.getLocationInWindow(cartLocation);
        detailRecyclerView.getLocationInWindow(recycleLocation);

        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();

        startP.x = addLocation[0];
        startP.y = addLocation[1] - recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1] - recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;

        final RxFakeAddImageView rxFakeAddImageView = new RxFakeAddImageView(getContext());
        mainLayout.addView(rxFakeAddImageView);
        rxFakeAddImageView.setImageResource(R.drawable.jia);
        rxFakeAddImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.dp_25);
        rxFakeAddImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.dp_25);
        rxFakeAddImageView.setVisibility(View.VISIBLE);
        ObjectAnimator addAnimator = ObjectAnimator.ofObject(rxFakeAddImageView, "mPointF",
                new RxPointFTypeEvaluator(controlP), startP, endP);
        addAnimator.setInterpolator(new AccelerateInterpolator());
        addAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                rxFakeAddImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rxFakeAddImageView.setVisibility(View.GONE);
                mainLayout.removeView(rxFakeAddImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(shoppingCart, "scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(shoppingCart, "scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();

        showTotalPrice();
    }

    @Override
    public void remove(View view, int position) {
        showTotalPrice();
    }

    private void showTotalPrice() {
        List<GoodsBean> data = mGoodsDetailAdapter.getData();
        Double total = 0.0;
        int numStr = 0;
        for (GoodsBean bean : data) {
            if (bean.isCheck()) {
                total = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getCount()), total);
                numStr = numStr + bean.getCount();
            }
        }
        if (numStr > 0) {
            shoppingCartTotalTv.setVisibility(View.VISIBLE);
            shoppingCartTotalTv.setText("¥ " + total);
            num.setVisibility(View.VISIBLE);
            num.setText("" + numStr);
        } else {
            shoppingCartTotalTv.setVisibility(View.INVISIBLE);
            num.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.payBtn)
    public void onViewClicked() {
    }
}
