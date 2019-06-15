package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BaseFmAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.ShopBean;
import com.shuiwangzhijia.wuliu.event.CommentEvent;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.fragment.ShopCommentFragment;
import com.shuiwangzhijia.wuliu.fragment.ShopGoodsFragment;
import com.shuiwangzhijia.wuliu.fragment.ShopInfoFragment;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 店铺推广
 * created by wangsuli on 2018/8/21.
 */
public class ShopSpreadActivity extends BaseAct {

    public static final int FROM_NEARBY = 1;
    public static final int FROM_OTHER = 2;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.shopType)
    TextView shopType;
    @BindView(R.id.startNum)
    RatingBar startNum;
    @BindView(R.id.orderCount)
    TextView orderCount;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.deliverFee)
    TextView deliverFee;
    @BindView(R.id.freeHint)
    TextView freeHint;
    @BindView(R.id.sendTime)
    TextView sendTime;
    private List<String> titles;
    private ArrayList<Object> pageList;
    private BaseFmAdapter adapter;
    private int isFrom;

    public static void startAtc(Context context, int isFrom) {
        Intent intent = new Intent(context, ShopSpreadActivity.class);
        intent.putExtra("isFrom", isFrom);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_spread);
        ButterKnife.bind(this);

        initView();
//        initData();
    }

    private void initData() {
        RetrofitUtils.getInstances().create().getShopInfo().enqueue(new Callback<EntityObject<ShopBean>>() {
            @Override
            public void onResponse(Call<EntityObject<ShopBean>> call, Response<EntityObject<ShopBean>> response) {
                EntityObject<ShopBean> body = response.body();
                if (body.getCode() == 200) {
                    ShopBean shopBean = body.getResult();
                    shopName.setText(shopBean.getSname());
                    orderCount.setText("销量：" + shopBean.getSname());
//                    deliverFee.setText("配送费:" + shopBean.getSailsum());
//                    freeHint.setText("满" + shopBean.getFull_free() + "元免配送费");
//                    String effic = shopBean.get();
//                    if(TextUtils.isEmpty(effic)){
//                        sendTime.setVisibility(View.GONE);
//                    }else{
//                        sendTime.setText("1小时内:" + effic + "千米");
//                    }
//                    startNum.setRating(shopBean.getScore());
//                    Glide.with(ShopSpreadActivity.this).load(shopBean.getPicturl()).placeholder(R.color.color_eeeeee).into(image);
                } else {
                    if (body.getScode() == -200) {
                        EventBus.getDefault().post(new LoginOutEvent());
                    }
                    hint(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ShopBean>> call, Throwable t) {
            }
        });
    }

    private void hint(String text) {
        Toast.makeText(ShopSpreadActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void initView() {
        isFrom = getIntent().getIntExtra("isFrom", -1);
        pageList = new ArrayList<>();
        if (FROM_NEARBY == isFrom) {
            setTitle("店铺推广");
            titles = Arrays.asList(new String[]{"商家商品", "商家信息", "评论"});
            pageList.add(new ShopGoodsFragment());
        } else if (FROM_OTHER == isFrom) {
            setTitle("店铺");
            titles = Arrays.asList(new String[]{"购买详情", "商家信息", "评论"});
            pageList.add(new ShopCommentFragment());
        }
        pageList.add(new ShopInfoFragment());
        pageList.add(new ShopCommentFragment());
        adapter = new BaseFmAdapter(getSupportFragmentManager(), pageList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(CommentEvent event) {
        titles.set(2, "评论（" + event.size + "）");
        adapter.setTitles(titles);
    }
}
