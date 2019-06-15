package com.shuiwangzhijia.wuliu.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.bean.AlbumItem;
import com.shuiwangzhijia.wuliu.view.SquareImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商家评论-图片集适配器
 * created by wangsuli on 2018/8/17.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final Context mContext;
    private OnRecyclerViewItemClickListener itemClickListener;

    public void setAlbumList(List<AlbumItem> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }


    private List<AlbumItem> albumList;

    public void setCommentImageList(List<String> commentImageList) {
        this.commentImageList = commentImageList;
        notifyDataSetChanged();
    }

    private List<String> commentImageList;
    private boolean isComment;


    public ImageAdapter(Context context, boolean isComment) {
        mContext = context;
        this.isComment = isComment;
        if (isComment) {
            commentImageList = new ArrayList<>();
        } else {
            albumList = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object item = getItem(position);
        if (item == null) {
            holder.image.setImageResource(R.drawable.picture_null);
        } else if (item instanceof AlbumItem) {
            Uri uri = Uri.fromFile(new File(((AlbumItem) item).getFilePath()));
            Glide.with(mContext).load(uri).placeholder(R.color.color_eeeeee).into(holder.image);
        } else if (item instanceof String) {
            Glide.with(mContext).load(item).placeholder(R.color.color_eeeeee).into(holder.image);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick((int) view.getTag());
            }
        });
    }


    @Override
    public int getItemCount() {
        if (isComment) {
            return (commentImageList == null) ? 0 : commentImageList.size();
        } else {
            return (albumList == null) ? 0 : albumList.size();
        }
    }

    public Object getItem(int position) {
        if (isComment) {
            return commentImageList.get(position);
        } else {
            return albumList.get(position);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        itemClickListener = listener;
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SquareImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
