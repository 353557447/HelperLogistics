package com.shuiwangzhijia.wuliu.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.SelectImageAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.AlbumItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by wangsuli on 2017/6/2.
 */
public class SelectImageActivity extends BaseAct implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static  final String LIMIT="limit";//可选几张图片
    public static final int REQUEST_AVATAR = 110;  // 头像请求
    public static final int REQUEST_COMMMENT_IMAGE = 111;//评论图片
    private GridView gridView;
    private List<AlbumItem> albumList = new ArrayList<AlbumItem>();
    private Map<String, String> thumbnailMap = new TreeMap<String, String>();  // 系统缩略图
    private SelectImageAdapter adapter;

    private int limitCount=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        limitCount=getIntent().getIntExtra(LIMIT,1);
        setTitle("图片");
        if(limitCount!=1) {
            setRightTitle("确定");
            setRightClickListener(this);
        }
        initView();
        scanImages();
    }

    private void initView() {
        gridView= (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);
        adapter = new SelectImageAdapter(this,limitCount);
        gridView.setAdapter(adapter);
    }
    /**
     * 扫描手机里面的图片
     */
    public void scanImages() {
        if (android.os.Build.VERSION.SDK_INT > 18) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
        getThumbnail();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.MediaColumns.MIME_TYPE + "=? or " + MediaStore.MediaColumns.MIME_TYPE + "=? or " + MediaStore.MediaColumns.MIME_TYPE + "=?";
        String[] selectionArgs = {"image/webp", "image/jpeg", "image/png"};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, MediaStore.MediaColumns.DATE_ADDED + " desc");  // 按时间排序
        if (cursor != null) {
            while (cursor.moveToNext()) {
                AlbumItem item = new AlbumItem();
                File file = new File(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
                item.setImageId(cursor.getString(cursor.getColumnIndex("_id")));
                item.setThumbnail(thumbnailMap.get(item.getImageId()));
                // 如果无缩略图  赋予原图路径
                if (TextUtils.isEmpty(item.getThumbnail())) {
                    item.setThumbnail(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
                }
                item.setFilePath(file.getAbsolutePath());
                item.setTime(cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED)));
                albumList.add(item);

            }
            cursor.close();
        }

        adapter.setData(albumList);

    }


    private void getThumbnail() {
        Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
//				File file = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.THUMB_DATA)));
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                thumbnailMap.put(id, data);
            }
        }
        cursor.close();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        AlbumItem item = adapter.getItem(position);
        ArrayList<AlbumItem> selectedImages =adapter.getSelectedImages();
        if (selectedImages.size() >= limitCount) {
            Toast.makeText(SelectImageActivity.this, "最多只能选" + limitCount + "张图片", Toast.LENGTH_SHORT).show();
        }else if(!selectedImages.contains(item)){
            adapter.addSelectedImages(item);
        }else if(selectedImages.contains(item)){
            adapter.removeSelectedImages(item);
        }
        if(limitCount==1){
            Intent intent = new Intent();
            intent.putExtra("images", adapter.getSelectedImages());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.rightTv:
                Intent intent = new Intent();
                intent.putExtra("images", adapter.getSelectedImages());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}

