package com.shuiwangzhijia.wuliu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;


public class BaseBar extends RelativeLayout {

    private RelativeLayout mBarRootRl;
    private LinearLayout mBack;
    private ImageView mBackIcon;
    private TextView mBackTitle;
    private TextView mTitle;
    private LinearLayout mRightBtn;
    private TextView mRightTv;
    private ImageView mRightIv;

    private BarListener mBarListener;

    public interface BarListener {
        void onLeftClick();

        void onRightClick();
    }

    public void setBarListener(BarListener mBarListener) {
        this.mBarListener = mBarListener;
    }

    public BaseBar(Context context) {
        this(context, null);
    }

    public BaseBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBaseBar(context, attrs);
    }

    private void initBaseBar(Context context, AttributeSet attrs) {
        View barView = LayoutInflater.from(context).inflate(R.layout.view_bar_base, this, true);
        mBarRootRl = barView.findViewById(R.id.bar_root_rl);
        mBack = barView.findViewById(R.id.back);
        mBackIcon = barView.findViewById(R.id.back_icon);
        mBackTitle = barView.findViewById(R.id.back_title);
        mTitle = barView.findViewById(R.id.title);
        mRightBtn = barView.findViewById(R.id.right_btn);
        mRightTv = barView.findViewById(R.id.right_tv);
        mRightIv = barView.findViewById(R.id.right_iv);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BaseBar);
        int barColor = typedArray.getInt(R.styleable.BaseBar_bar_color, R.color.white);
        int backIconRes = typedArray.getResourceId(R.styleable.BaseBar_back_icon, R.drawable.black_back);
        String backTitleText = typedArray.getString(R.styleable.BaseBar_back_title);
        int backTitleColorRes = typedArray.getInt(R.styleable.BaseBar_back_title_color, R.color.black);
        String titleText = typedArray.getString(R.styleable.BaseBar_title);
        int titleColorRes = typedArray.getInt(R.styleable.BaseBar_title_color, R.color.black);
        String rightTvText = typedArray.getString(R.styleable.BaseBar_right_tv);
        int rightTvColorRes = typedArray.getInt(R.styleable.BaseBar_right_tv_color, R.color.black);
        int rightIconRes = typedArray.getResourceId(R.styleable.BaseBar_right_icon, R.drawable.black_search_icon);
        typedArray.recycle();

        mBarRootRl.setBackgroundColor(barColor);
        mBackIcon.setImageResource(backIconRes);
        mBackTitle.setText(backTitleText);
        mBackTitle.setTextColor(getResources().getColor(backTitleColorRes));
        mTitle.setText(titleText);
        mTitle.setTextColor(getResources().getColor(titleColorRes));
        mRightTv.setText(rightTvText);
        mRightTv.setTextColor(getResources().getColor(rightTvColorRes));
        mRightIv.setImageResource(rightIconRes);

        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBarListener != null)
                    mBarListener.onLeftClick();
            }
        });
        mRightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBarListener != null)
                    mBarListener.onRightClick();
            }
        });
    }

    public void setBarColor(String color) {
        mBarRootRl.setBackgroundColor(Color.parseColor(color));
    }

    public void setBarColorRes(int barColorRes) {
        mBarRootRl.setBackgroundColor(getResources().getColor(barColorRes));
    }

    public void setbackIconRes(int backIconRes) {
        mBackIcon.setImageResource(backIconRes);
    }

    public void setBackTitle(String backTitle) {
        mBackTitle.setText(backTitle);
    }

    public void setBackTitleColor(String color) {
        mBackTitle.setTextColor(Color.parseColor(color));
    }

    public void setBackTitleColorRes(int colorRes) {
        mBackTitle.setTextColor(getResources().getColor(colorRes));
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setTitleColor(String color) {
        mTitle.setTextColor(Color.parseColor(color));
    }

    public void setTitleColorRes(int colorRes) {
        mTitle.setTextColor(getResources().getColor(colorRes));
    }

    public void setRightTv(String rightString) {
        mRightTv.setText(rightString);
    }

    public void setRightTvColor(String color) {
        mRightTv.setTextColor(Color.parseColor(color));
    }

    public void setRightTvColorRes(int colorRes) {
        mRightTv.setTextColor(getResources().getColor(colorRes));
    }

    public void setRightIv(int rightIvRes) {
        mRightIv.setImageResource(rightIvRes);
        mRightIv.setVisibility(VISIBLE);
        mRightTv.setVisibility(GONE);
    }

    public ImageView getRightIv() {
        return mRightIv;
    }
}
