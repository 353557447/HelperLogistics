package com.shuiwangzhijia.wuliu.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.ImageAdapter;
import com.shuiwangzhijia.wuliu.base.App;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.base.Constant;
import com.shuiwangzhijia.wuliu.bean.AlbumItem;
import com.shuiwangzhijia.wuliu.dialog.SelectImageDialog;
import com.shuiwangzhijia.wuliu.event.PictureEvent;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.GridDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 评论页面
 * created by wangsuli on 2018/8/31.
 */
public class OrderCommentActivity extends BaseAct implements ImageAdapter.OnRecyclerViewItemClickListener {
    private static final int LIMIT_SIZE = 4;//最多图片张数
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.createTime)
    TextView createTime;
    @BindView(R.id.commentEdit)
    EditText commentEdit;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.goodsStartNum)
    RatingBar goodsStartNum;
    @BindView(R.id.serviceStartNum)
    RatingBar serviceStartNum;
    @BindView(R.id.speedStartNum)
    RatingBar speedStartNum;
    @BindView(R.id.postBtn)
    TextView postBtn;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private String photoPath;
    private File photoFile;
    private ArrayList<AlbumItem> selectedImages = new ArrayList<>();
    private Uri imageUri;
    public static final int REQUEST_CAMERA = 106; // 拍照
    private static final int CODE_FOR_WRITE_PERMISSION = 666;
    private boolean hasPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comment);
        ButterKnife.bind(this);
        int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_FOR_WRITE_PERMISSION);
        } else {
            hasPermissions = true;
        }
        setTitle("评论");
        setTextStyle(orderId, "订单号码：", "20187642881256523");
        setTextStyle(createTime, "下单时间：", "2018-12-12 12:36:23");
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(10, getResources().getColor(R.color.color_ffffff)));
        selectedImages.add(null);
        mImageAdapter = new ImageAdapter(this, false);
        mImageAdapter.setAlbumList(selectedImages);
        mImageAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mImageAdapter);

    }

    private void setTextStyle(TextView text, String first, String content) {
        SpannableString spanString = new SpannableString(first + content);
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_666666)), 0, first.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_333333)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        text.setText(spanString);
    }

    @OnClick(R.id.postBtn)
    public void onViewClicked() {

    }

    private void takeAPicture() {
        photoPath = Constant.CACHE_DIR_IMAGE + "/" + CommonUtils.getUUID();
        photoFile = new File(photoPath);
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(photoFile));
        startActivityForResult(intentCamera, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == SelectImageActivity.REQUEST_COMMMENT_IMAGE) {
            //从相册选照片
            ArrayList<AlbumItem> list = (ArrayList<AlbumItem>) data.getSerializableExtra("images");
            if (list == null)
                return;
            for (int i = 0; i < list.size(); i++) {
                sort(list.get(i));
            }
//            AlbumItem albumItem = selectedImages.get(0);
//            imageUri = getUriForFile(new File(albumItem.getFilePath()));
//            BitmapUtils.createPhotoCrop(OrderCommentActivity.this, imageUri);
        } else if (requestCode == REQUEST_CAMERA) {
            //拍照照片
            imageUri = getUriForFile(photoFile);
            AlbumItem newItem = new AlbumItem();
            newItem.setFilePath(photoFile.getAbsolutePath());
            sort(newItem);
//            BitmapUtils.createPhotoCrop(OrderCommentActivity.this, imageUri);
        } else if (requestCode == Constant.IMAGE_CROP) {
            //经过裁剪后的图片
            if (data != null) {
                //TODO
//                postAvatar();
            }
        }
    }

    private void sort(AlbumItem newItem) {
        int size = selectedImages.size();
        AlbumItem albumItem = selectedImages.get(size - 1);
        if (albumItem == null) {
            selectedImages.remove(size - 1);
        }
        selectedImages.add(newItem);
        if (selectedImages.size() < LIMIT_SIZE) {
            selectedImages.add(null);
        }
        mImageAdapter.setAlbumList(selectedImages);
    }

    private static Uri getUriForFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(App.getInstance(), "com.funengda.water.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    public void onItemClick(int position) {
        Object item = mImageAdapter.getItem(position);
        if (item == null) {
            if (!hasPermissions) {
                Toast.makeText(OrderCommentActivity.this, "该相册需要赋予访问存储的权限，不开启将无法正常工作！", Toast.LENGTH_SHORT).show();
                return;
            }
            SelectImageDialog dialog = new SelectImageDialog(OrderCommentActivity.this, new SelectImageDialog.OnItemClickListener() {
                @Override
                public void onClick(SelectImageDialog dialog, int index) {
                    dialog.dismiss();
                    switch (index) {
                        case SelectImageDialog.TYPE_CAMERA:
                            takeAPicture();
                            break;

                        case SelectImageDialog.TYPE_PHOTO:
                            Intent intent = new Intent(OrderCommentActivity.this, SelectImageActivity.class);
                            int limit = LIMIT_SIZE - selectedImages.size();
                            if (selectedImages.get(selectedImages.size() - 1) == null) {
                                limit = limit + 1;
                            }
                            //控制选择的图片数量
                            intent.putExtra(SelectImageActivity.LIMIT, limit);
                            startActivityForResult(intent, SelectImageActivity.REQUEST_COMMMENT_IMAGE);
                            break;
                    }

                }
            });
            dialog.show();
        } else {
            ArrayList<AlbumItem> data = new ArrayList<>();
            for (AlbumItem image : selectedImages) {
                if (image != null) {
                    data.add(image);
                }
            }
            PhotoViewActivity.statAct(OrderCommentActivity.this, 1 + position, data);
        }

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PictureEvent event) {
        selectedImages.remove(event.deletePosition - 1);
        mImageAdapter.notifyDataSetChanged();
    }
}
