package com.shuiwangzhijia.wuliu.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.bean.AlbumItem;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by wangsuli on 2018/9/3.
 */
public class PhotoViewAdapter extends PagerAdapter {
    public static final String TAG = PhotoViewAdapter.class.getSimpleName();
    private List<AlbumItem> imageUrls;
    private Activity activity;

    public PhotoViewAdapter(List<AlbumItem> imageUrls, Activity activity) {
        this.imageUrls = imageUrls;
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AlbumItem item = imageUrls.get(position);
        Uri uri = Uri.fromFile(new File(item.getFilePath()));
        PhotoView photoView = new PhotoView(activity);
        Glide.with(activity)
                .load(uri)
                .into(photoView);
        container.addView(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        return photoView;
    }

    @Override
    public int getCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
