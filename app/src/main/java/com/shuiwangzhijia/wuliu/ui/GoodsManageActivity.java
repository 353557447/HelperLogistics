package com.shuiwangzhijia.wuliu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.GoodsDetailAdapter;
import com.shuiwangzhijia.wuliu.adapter.GoodsTypeAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.GoodsManageBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商品管理
 * created by wangsuli on 2018/8/20.
 */
public class GoodsManageActivity extends BaseAct {
    @BindView(R.id.typeRecyclerView)
    RecyclerView typeRecyclerView;
    @BindView(R.id.detailRecyclerView)
    RecyclerView detailRecyclerView;
    @BindView(R.id.saleBtn)
    TextView saleBtn;
    @BindView(R.id.downBtn)
    TextView downBtn;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private GoodsTypeAdapter mGoodsTypeAdapter;
    private GoodsDetailAdapter mGoodsDetailAdapter;
    private boolean isSale = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_manage);
        ButterKnife.bind(this);
        setTitle("商品管理");
        initTypeRecyclerView();
        initDetailRecyclerView();
        changeState(isSale);
        getDataList();
    }

    private void getDataList() {
        RetrofitUtils.getInstances().create().getGoodsManageList(isSale ? 0 : 1).enqueue(new Callback<EntityObject<ArrayList<GoodsManageBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<GoodsManageBean>>> call, Response<EntityObject<ArrayList<GoodsManageBean>>> response) {
                EntityObject<ArrayList<GoodsManageBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<GoodsManageBean> result = body.getResult();
                    if (result == null) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        llContent.setVisibility(View.GONE);
                        return;
                    }
                    rlEmpty.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                    mGoodsTypeAdapter.setData(result);
                    mGoodsTypeAdapter.setSelectIndex(0);
                    mGoodsDetailAdapter.setData(result.get(0).getList());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<GoodsManageBean>>> call, Throwable t) {

            }
        });
    }

    private void initTypeRecyclerView() {
        typeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        typeRecyclerView.setHasFixedSize(true);
        mGoodsTypeAdapter = new GoodsTypeAdapter(this);
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
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailRecyclerView.setHasFixedSize(true);
        mGoodsDetailAdapter = new GoodsDetailAdapter(this, isSale, false);
        mGoodsDetailAdapter.setOnSaleClickListener(new GoodsDetailAdapter.OnSaleClickListener() {
            @Override
            public void onSelectClick(final int position) {
                GoodsBean item = mGoodsDetailAdapter.getItem(position);
                RetrofitUtils.getInstances().create().upDownGoods(item.getId()).enqueue(new Callback<EntityObject<String>>() {
                    @Override
                    public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                        EntityObject<String> body = response.body();
                        Toast.makeText(GoodsManageActivity.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                        if (body.getCode() == 200) {
                            mGoodsDetailAdapter.deleteItem(position);
                        }
                    }

                    @Override
                    public void onFailure(Call<EntityObject<String>> call, Throwable t) {

                    }
                });


            }
        });
        detailRecyclerView.setAdapter(mGoodsDetailAdapter);
    }


    @OnClick({R.id.saleBtn, R.id.downBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.saleBtn:
                changeState(true);
                getDataList();
                break;
            case R.id.downBtn:
                changeState(false);
                getDataList();
                break;
        }
    }

    private void changeState(boolean flag) {
        isSale = flag;
        if (flag) {
            saleBtn.setBackgroundColor(getResources().getColor(R.color.color_30adfd));
            downBtn.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
            saleBtn.setSelected(true);
            downBtn.setSelected(false);
        } else {
            downBtn.setBackgroundColor(getResources().getColor(R.color.color_30adfd));
            saleBtn.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
            downBtn.setSelected(true);
            saleBtn.setSelected(false);
        }
        mGoodsDetailAdapter.setSale(flag);
    }
}
