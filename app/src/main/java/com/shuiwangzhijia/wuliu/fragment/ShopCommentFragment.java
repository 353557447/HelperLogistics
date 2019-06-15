package com.shuiwangzhijia.wuliu.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.ShopCommentAdapter;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.bean.CommentBean;
import com.shuiwangzhijia.wuliu.event.CommentEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商家评论
 * created by wangsuli on 2018/8/20.
 */
public class ShopCommentFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;
    private ShopCommentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_shop_comment, container, false);
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

    @Override
    public void onResume() {
        super.onResume();
        getCommentData();

    }

    private void getCommentData() {
        RetrofitUtils.getInstances().create().getCommentList().enqueue(new Callback<EntityObject<ArrayList<CommentBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<CommentBean>>> call, Response<EntityObject<ArrayList<CommentBean>>> response) {
                EntityObject<ArrayList<CommentBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<CommentBean> result = body.getResult();
                    adapter.setData(result);
                    EventBus.getDefault().post(new CommentEvent(result.size()));
                }

            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<CommentBean>>> call, Throwable t) {

            }
        });
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg));
        mRecyclerView.addItemDecoration(divider);
        adapter = new ShopCommentAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
    }


}
