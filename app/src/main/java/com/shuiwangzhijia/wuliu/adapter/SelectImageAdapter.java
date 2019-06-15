package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.AlbumItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangsuli on 2017/4/27.
 */
public class SelectImageAdapter extends BaseAdapter {
    private final Context mContext;
    private List<AlbumItem> albumList;
    private ArrayList<AlbumItem> selectedImages;//标记选中的图片
    private int max;//最多可选张数


    public SelectImageAdapter(Context context, int limitCount) {
        this.mContext = context;
        max = limitCount;
        albumList = new ArrayList<>();
        selectedImages = new ArrayList<>();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void addSelectedImages(AlbumItem item) {
        selectedImages.add(item);
        notifyDataSetChanged();
    }
    public void removeSelectedImages(AlbumItem item) {
        selectedImages.remove(item);
        notifyDataSetChanged();
    }
    public ArrayList<AlbumItem> getSelectedImages() {
        return selectedImages;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public AlbumItem getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (null == convertView) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_select_image, null);
            mViewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            mViewHolder.check = (ImageView) convertView.findViewById(R.id.check);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        AlbumItem item = getItem(position);
        if (item.getThumbnail().isEmpty()) {
            Glide.with(mContext).load( item.getThumbnail()).into(mViewHolder.image);
        } else {
            Glide.with(mContext).load(item.getFilePath()).into(mViewHolder.image);
        }
        mViewHolder.check.setVisibility(getMax()==1? View.GONE: View.VISIBLE);
        mViewHolder.check.setImageResource(selectedImages.contains(item) ? R.drawable.xuanzhong : R.drawable.weigouxuan);
        return convertView;
    }

    public void setData(List<AlbumItem> albumList) {
        if (albumList == null || albumList.size() == 0) {
            return;
        }
        this.albumList = albumList;
        notifyDataSetChanged();
    }


    class ViewHolder {
        private ImageView image;
        private ImageView check;
    }
}
