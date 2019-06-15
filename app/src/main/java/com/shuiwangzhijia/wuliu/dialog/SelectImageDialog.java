package com.shuiwangzhijia.wuliu.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;


/**
 * SelectImageDialog
 * 0表示拍照 1表示相册
 * Created by wangsuli on 2017/5/5
 */
public class SelectImageDialog extends BottomDialog {
	private final Window window;
	private final int width;
	private TextView camera;
	private TextView photo;
	private LinearLayout llClose;
	private OnItemClickListener clickListener;
	public static final int TYPE_CAMERA=0;
	public static final int TYPE_PHOTO=1;


	public interface OnItemClickListener {
		/**
		 * @param index 0表示拍照 1表示相册
		 */
		void onClick(SelectImageDialog dialog, int index);
	}

	public SelectImageDialog(Context context, OnItemClickListener clickListener) {
		super(context);
		window = getWindow();
		width = window.getWindowManager().getDefaultDisplay().getWidth();
		setCanceledOnTouchOutside(true);
		this.clickListener = clickListener;
		setContentView(R.layout.dialog_switch_image);
		bindViews();
	}

	private void bindViews() {
		camera = (TextView) findViewById(R.id.camera);
		photo = (TextView) findViewById(R.id.photo);
		llClose = (LinearLayout) findViewById(R.id.llClose);
		llClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}

		});
		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				clickListener.onClick(SelectImageDialog.this,TYPE_CAMERA);
			}

		});
		photo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				clickListener.onClick(SelectImageDialog.this,TYPE_PHOTO);
			}

		});
	}
	@Override
	public void show() {
		super.show();
		WindowManager.LayoutParams attributesParams = window.getAttributes();
		attributesParams.gravity  = Gravity.BOTTOM;
		attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		attributesParams.dimAmount = 0.4f;
		attributesParams.width = width;
		attributesParams.x = 0;
		window.setAttributes(attributesParams);

	}
}
