package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.PhotoViewAdapter;
import com.shuiwangzhijia.wuliu.bean.AlbumItem;
import com.shuiwangzhijia.wuliu.event.PictureEvent;
import com.shuiwangzhijia.wuliu.view.PhotoViewPager;
import com.shuiwangzhijia.wuliu.base.BaseAct;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 图片展示
 * Created by wangsuli on 2018/9/3.
 */

public class PhotoViewActivity extends BaseAct {

    private PhotoViewPager mViewPager;
    private int currentPosition;
    private PhotoViewAdapter adapter;
    private List<AlbumItem> data;

    public static void statAct(Context context, int position, ArrayList<AlbumItem> data) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putInt("currentPosition", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        initView();
        initData();
    }

    private void initView() {
        setTitleBarBgColor(R.color.color_black);
        setRightIvClickListener(R.drawable.delete_iv, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new PictureEvent(currentPosition));
                finish();
            }
        });
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager_photo);
    }

    private void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        data = (List<AlbumItem>) intent.getSerializableExtra("data");
        setTitle("图片(" + currentPosition + "/" + data.size() + ")");
        adapter = new PhotoViewAdapter(data, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                setTitle("图片(" + (currentPosition + 1) + "/" + data.size() + ")");
            }
        });
    }

}
