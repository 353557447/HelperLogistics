package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.NearbyAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.ShopBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * created by wangsuli on 2018/8/28.
 */
public class SelectShopActivity extends BaseAct implements NearbyAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.searchEdit)
    EditText searchEdit;
    private NearbyAdapter adapter;
    private String lnglat;
    private String mS;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);
        ButterKnife.bind(this);
        mContext = this;
        setTitle("选择水店");
        lnglat = CommonUtils.getLongitude() + "," + CommonUtils.getLatitude();
        initRecyclerView();
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mS = editable.toString();
            }
        });
        searchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    //先隐藏软键盘
                    if (i == KeyEvent.KEYCODE_ENTER  && keyEvent.getAction() == KeyEvent.ACTION_DOWN ) {
                        // 先隐藏键盘
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        search(mS);
                    }
                }
                return false;
            }
        });
        search("");
    }

    private void search(String text) {
        showLoad();
        RetrofitUtils.getInstances().create().getWaterShopList(lnglat, text, 0, 10).enqueue(new Callback<EntityObject<ArrayList<ShopBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<ShopBean>>> call, Response<EntityObject<ArrayList<ShopBean>>> response) {
                hintLoad();
                EntityObject<ArrayList<ShopBean>> body = response.body();
                if (body.getCode() == 200) {
                    adapter.setData(body.getResult());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<ShopBean>>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg));
        mRecyclerView.addItemDecoration(divider);
        adapter = new NearbyAdapter(this, false);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        ShopBean item = adapter.getItem(position);
        EventBus.getDefault().post(item);
        finish();
    }

}
