package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.OperationGoodsAdapterTwo;
import com.shuiwangzhijia.wuliu.adapter.OperationHuiShouAdapter;
import com.shuiwangzhijia.wuliu.adapter.OperationTuishuiAdapter;
import com.shuiwangzhijia.wuliu.adapter.OperationZiyingAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.OperationSureBean;
import com.shuiwangzhijia.wuliu.bean.OrderShowBean;
import com.shuiwangzhijia.wuliu.bean.PreparePayBean;
import com.shuiwangzhijia.wuliu.dialog.AddhuitongDialog;
import com.shuiwangzhijia.wuliu.dialog.EditPurchaseAmountDialog;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.shuiwangzhijia.wuliu.event.RecyclingEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.interfaces.ShopCartInterface;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.PreferenceUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 操作订单界面  create by xxc
 */
public class OperationOrderActivityNew extends BaseAct {
    private static final String TAG = "OperationOrderActivity";
    @BindView(R.id.shopName)
    TextView mShopName;
    @BindView(R.id.orderId)
    TextView mOrderId;
    //    @BindView(R.id.orderDate)
//    TextView mOrderDate;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.numRecyclerView)
    RecyclerView mNumRecyclerView;
    //    @BindView(R.id.addBucketBtn)
//    TextView mAddBucketBtn;
    @BindView(R.id.huiShouRecyclerView)
    RecyclerView mHuiShouRecyclerView;
    @BindView(R.id.huiShouBucketBtn)
    TextView mHuiShouBucketBtn;
    @BindView(R.id.tuiShuiRecyclerView)
    RecyclerView mTuiShuiRecyclerView;
    @BindView(R.id.tuiShuiBucketBtn)
    TextView mTuiShuiBucketBtn;
    //    @BindView(R.id.zaTongNumber)
//    TextView mZaTongNumber;
    @BindView(R.id.ziYingRecyclerView)
    RecyclerView mZiYingRecyclerView;
    //    @BindView(R.id.buTieWay)
//    TextView mBuTieWay;
//    @BindView(R.id.shuiPiaoName)
//    TextView mShuiPiaoName;
//    @BindView(R.id.shuiPiaoNumber)
//    TextView mShuiPiaoNumber;
//    @BindView(R.id.shuiPiaoStatus)
//    TextView mShuiPiaoStatus;
//    @BindView(R.id.zaTongBucketBtn)
//    TextView mZaTongBucketBtn;
    @BindView(R.id.unPayBtn)
    TextView mUnPayBtn;
    @BindView(R.id.sureBtn)
    TextView mSureBtn;
    @BindView(R.id.detailBtn)
    TextView mDetailBtn;
    @BindView(R.id.zatong_ll)
    LinearLayout mZatongLl;
    //    @BindView(R.id.delete_huitong)
//    ImageView mDeleteHuitong;
    @BindView(R.id.delete_linear)
    LinearLayout mDeleteLinear;
    @BindView(R.id.huitong_linear)
    LinearLayout mHuitongLinear;
    @BindView(R.id.remove)
    ImageView mRemove;
    @BindView(R.id.account)
    TextView mAccount;
    @BindView(R.id.add)
    ImageView mAdd;
    @BindView(R.id.addBucketBtn)
    TextView mAddBucketBtn;
    @BindView(R.id.numTv)
    EditText mNumTv;
    @BindView(R.id.gotopay)
    TextView mGotopay;
    @BindView(R.id.mSwitch)
    Switch mMSwitch;
    @BindView(R.id.payStatus)
    TextView mPayStatus;
    @BindView(R.id.zatong_lin)
    LinearLayout mZatongLin;
    @BindView(R.id.zatong_number)
    TextView mZatongNumber;
    @BindView(R.id.butie_money)
    TextView mButieMoney;
    @BindView(R.id.number)
    TextView mNumber;
    @BindView(R.id.zatong_bg)
    LinearLayout mZatongBg;
    @BindView(R.id.po_remove)
    ImageView mPoRemove;
    @BindView(R.id.po_account)
    TextView mPoAccount;
    @BindView(R.id.po_add)
    ImageView mPoAdd;
    @BindView(R.id.potong_lin)
    LinearLayout mPotongLin;
    @BindView(R.id.potong_number)
    TextView mPotongNumber;
    private String mOrderData;
    private Double total;
    private int num;
    private Double price;
    private Double tuishuiTotal;

    private List<OrderShowBean.GoodsBean> mGoods;
    private List<OrderShowBean.RecyclerBean> mRecycler;
    private List<OrderShowBean.GoodsBean> mRefund_water;
    private OperationGoodsAdapterTwo mPeisongAdapter;
    private OperationHuiShouAdapter mHuitongAdapter;
    private OperationTuishuiAdapter mTuishuiAdapter;
    private int mPay_style;
    private OrderShowBean mResult;
    private List<OrderShowBean.GoodsBean> mPeisongData;
    private List<OrderShowBean.RecyclerBean> mHuiTongData;
    private List<OrderShowBean.RecyclerBean> mTuiShuiData;
    private double mSub;
    private ArrayList<OrderShowBean.RecyclerBean> recyclerTongDialogData;
    private int mCount = 1;
    private int poCount = 0;
    private OperationZiyingAdapter mZiyingAdapter;
    private int mPay_status;
    private List<OrderShowBean.RecyclerBean> mGoods1;
    private int type;
    private boolean isInit;
    private int mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_order_new);
        ButterKnife.bind(this);
        setTitle("操作订单");
        mOrderData = getIntent().getStringExtra("orderData");
        mTag = getIntent().getIntExtra("type", 0);
        initListener();
        showLoad();
        getList();
        getAddSelfBucketList();
    }

    private void initListener() {
        mNumTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //正则，用来判断是否输入了小数点
                String regex = "^\\d+.$";
                Pattern r = Pattern.compile(regex);
                Matcher matcher = r.matcher(charSequence);
                if (matcher.matches()) {
                    mNumTv.setFilters(new InputFilter[]{
                            new InputFilter.LengthFilter(charSequence.length() + 2)
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (!text.equals("") && !text.equals("0")) {
                    mGotopay.setText("去支付");
                    mGotopay.setBackgroundResource(R.drawable.blue_rectangle);
                } else {
                    mGotopay.setText("确认");
                }
            }
        });
        mMSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    KLog.d("mMSwitch.isChecked");
                    type = 1;
                    mDeleteLinear.setVisibility(View.VISIBLE);
                    mZatongBg.setBackgroundResource(R.drawable.operation_bg_songda);
                } else {
                    KLog.d("mMSwitch.notChecked");
                    type = 0;
                    mDeleteLinear.setVisibility(View.GONE);
                    mZatongBg.setBackgroundResource(R.drawable.operation_bg_songda_new);
                    deleteZatong();
                }
            }
        });
        mUnPayBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (type == 1) {
                    hint("请先支付杂桶");
                    return;
                }
                post(2);
            }
        });
        mSureBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (type == 1) {
                    hint("请先支付杂桶");
                    return;
                }
                if (mPay_style == 0) {
                    //普通支付（在线支付）
                    post(1);
                } else {
                    showLoad();
                    RetrofitUtils.getInstances().create().sureOrder(CommonUtils.getToken(), 1, mOrderData,
                            packageGoodsData(mPeisongAdapter.getData()), packageHuitongData(mHuitongAdapter.getData()), packageLanGoodsData(mPeisongAdapter.getData()),
                            packageTuishuiData(mPeisongAdapter.getData())).enqueue(new Callback<EntityObject<OperationSureBean>>() {
                        @Override
                        public void onResponse(Call<EntityObject<OperationSureBean>> call, Response<EntityObject<OperationSureBean>> response) {
                            hintLoad();
                            EntityObject<OperationSureBean> body = response.body();
                            if (body.getCode() == 200) {
                                int jumpType = body.getResult().getJump_type();
                                if (jumpType == 0) {
                                    //跳完成
                                    EventBus.getDefault().post(new MainEvent(3));
                                    finish();
                                } else if (jumpType == 1) {
                                    //跳支付
                                    //货到付款
                                    String money = mMoney.getText().toString().trim();
                                    String actualMoney = money.substring(1, money.length());
                                    PreparePayBean bean = new PreparePayBean();
                                    bean.setSname(mResult.getSname());
                                    bean.setTamount(actualMoney);
                                    bean.setOrder_no(mResult.getOrder_sn());
                                    bean.setCreate_time(mResult.getCreate_time() + "");
//                                  bean.setCreate_time(DateUtils.getFormatDateStr(mResult.getTime() * 1000));
                                    //赊账中firstBtn 生成的二维码
                                    bean.setPayFrom(4);
                                    ReceivePayActivity.startAtc(OperationOrderActivityNew.this, bean, 4);
                                } else if (jumpType == 2) {
                                    //跳赊账
                                    EventBus.getDefault().post(new MainEvent(2));
                                    finish();
                                }
                            } else {
                                ToastUitl.showToastCustom(body.getMsg());
                            }
                        }

                        @Override
                        public void onFailure(Call<EntityObject<OperationSureBean>> call, Throwable t) {
                            hintLoad();
                        }
                    });
                }
            }
        });
    }

    private void deleteZatong() {
        RetrofitUtils.getInstances().create().clearChangeBucket(mOrderData).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 200) {
                    hint("删除成功");
                } else {
                    hint(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {
            }
        });
    }

    private void getAddSelfBucketList() {
        RetrofitUtils.getInstances().create().canHuiTonglist().enqueue(new Callback<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>> call, Response<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>> response) {
                EntityObject<ArrayList<OrderShowBean.RecyclerBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<OrderShowBean.RecyclerBean> result = body.getResult();
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    recyclerTongDialogData = result;
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>> call, Throwable t) {

            }
        });
    }

    private void getList() {
        //操作订单显示接口
        RetrofitUtils.getInstances().create().orderShowNew(mOrderData).enqueue(new Callback<EntityObject<OrderShowBean>>() {
            @Override
            public void onResponse(Call<EntityObject<OrderShowBean>> call, Response<EntityObject<OrderShowBean>> response) {
                hintLoad();
                EntityObject<OrderShowBean> body = response.body();
                if (body != null) {
                    if (body.getCode() == 200) {
                        mResult = body.getResult();
                        mShopName.setText(mResult.getSname());
                        mOrderId.setText("订单号：" + mResult.getOrder_sn());
                        //是在线支付还是货到付款
                        mPay_style = mResult.getPay_style();
                        if (mPay_style == 0) {
                            mUnPayBtn.setVisibility(View.GONE);
                            mSureBtn.setText("确认送达");
                        } else {
                            mUnPayBtn.setVisibility(View.VISIBLE);
                            mSureBtn.setText("立即收款");
                        }

                        //是否有换桶数据
                        OrderShowBean.ChangeBucketBean change_bucket = mResult.getChange_bucket();
                        if (change_bucket != null) {
                            mGotopay.setBackgroundResource(R.drawable.blue_rectangle);
                            mZatongBg.setBackgroundResource(R.drawable.operation_bg_songda);
                            mMSwitch.setChecked(true);
                            mPay_status = change_bucket.getPay_status();
                            if (mPay_status == 0) {
                                mPayStatus.setText("支付状态：未支付");
                                mMSwitch.setVisibility(View.VISIBLE);
                                mGotopay.setVisibility(View.VISIBLE);
                                mZatongLin.setVisibility(View.VISIBLE);
                                mPotongLin.setVisibility(View.VISIBLE);
                                mZatongNumber.setVisibility(View.GONE);
                                mPotongNumber.setVisibility(View.GONE);
                                mNumTv.setVisibility(View.VISIBLE);
                                mAddBucketBtn.setVisibility(View.VISIBLE);
                                type = 1;
                            } else {
                                KLog.d("已支付");
                                mPayStatus.setText("支付状态：已支付");
                                mMSwitch.setVisibility(View.GONE);
                                mGotopay.setVisibility(View.GONE);
                                mZatongLin.setVisibility(View.GONE);
                                mPotongLin.setVisibility(View.GONE);
                                mZatongNumber.setVisibility(View.VISIBLE);
                                mPotongNumber.setVisibility(View.VISIBLE);
                                mNumTv.setVisibility(View.GONE);
                                mAddBucketBtn.setVisibility(View.GONE);
                                mButieMoney.setText("￥" + change_bucket.getTotal_price());
                                mZatongNumber.setText("x" + change_bucket.getMix_num() + "");
                                mPotongNumber.setText("x"+change_bucket.getBrokenBucketNum());
                                type = 0;
                            }
                            mGoods1 = mResult.getChange_bucket().getGoods();
                        } else {
                            mDeleteLinear.setVisibility(View.GONE);
                            mZatongBg.setBackgroundResource(R.drawable.operation_bg_songda_new);
                            mMSwitch.setChecked(false);
                            mGotopay.setBackgroundResource(R.drawable.gray_rectangle);
                        }
                        if (mPay_status == 0) {
                            initZiyingRecycle(mGoods1, 1);
                        } else {
                            initZiyingRecycle(mGoods1, 2);
                        }
                        mGoods = mResult.getGoods();
                        mRecycler = mResult.getRecycler();
                        mRefund_water = mResult.getRefund_water();
                        price = 0.0;
                        for (OrderShowBean.GoodsBean bean : mGoods) {
                            price = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getSnum()), price);
                        }
                        //水票的价格
                        mSub = CalculateUtils.sub(price, Double.parseDouble(mResult.getTprice()));

                        if (mTag != 1) {
                            mMoney.setText("￥" + mResult.getTprice());
                            mNumber.setText(mResult.getSnum() + "");
                            initGoodsRecycle(mGoods);//已配送
                            initRecycler(mRecycler);//回桶自营桶
                        } else {
                            String price = PreferenceUtils.getString("price");
                            int number = PreferenceUtils.getInt("number", 0);
                            mMoney.setText(price);
                            mNumber.setText(number + "");
                            List<OrderShowBean.GoodsBean> peiSong = CommonUtils.getPeiSong();
                            List<OrderShowBean.RecyclerBean> huiTong = CommonUtils.getHuiTong();
                            initGoodsRecycle(peiSong);//已配送
                            initRecycler(huiTong);//回桶自营桶
                        }


//                        if (mTuiShuiData == null) {
//                            initRefundWater(mRefund_water);//退水数量
//                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<OrderShowBean>> call, Throwable t) {

            }
        });
    }

    private void initZiyingRecycle(List<OrderShowBean.RecyclerBean> goods, int type) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mZiYingRecyclerView.setLayoutManager(manager);
        mZiyingAdapter = new OperationZiyingAdapter(this, goods, type);
        mZiyingAdapter.setOnItemClickListener(new OperationZiyingAdapter.OnViewItemClickListener() {
            @Override
            public void doEditCount(int position, int count) {
                OrderShowBean.RecyclerBean item = mZiyingAdapter.getItem(position);
                if (count < 1) {
                    hint("购买数量不能低于最低购买量!");
                    return;
                }
                item.setNum(count);
                mZiyingAdapter.notifyDataSetChanged();
            }
        });
        mZiYingRecyclerView.setAdapter(mZiyingAdapter);
    }

    //退水
    private void initRefundWater(List<OrderShowBean.GoodsBean> refund_water) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mTuiShuiRecyclerView.setLayoutManager(manager);
        mTuishuiAdapter = new OperationTuishuiAdapter(this, refund_water);
        mTuishuiAdapter.setOnViewItemClickListener(new OperationTuishuiAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                OrderShowBean.RecyclerBean item = mHuitongAdapter.getItem(position);
                if (count < 1) {
                    hint("购买数量不能低于最低购买量!");
                    return;
                }
                item.setNum(count);
                mHuitongAdapter.notifyDataSetChanged();
            }
        });
        mTuiShuiRecyclerView.setAdapter(mTuishuiAdapter);
    }

    //回桶自营桶
    private void initRecycler(List<OrderShowBean.RecyclerBean> recycler) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mHuiShouRecyclerView.setLayoutManager(manager);
        mHuitongAdapter = new OperationHuiShouAdapter(this, recycler);
        mHuitongAdapter.setOnViewItemClickListener(new OperationHuiShouAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                OrderShowBean.RecyclerBean item = mHuitongAdapter.getItem(position);
//                if (count < 1) {
//                    hint("购买数量不能低于最低购买量!");
//                    return;
//                }
                item.setSnum(count);
                mHuitongAdapter.notifyDataSetChanged();
            }
        });
        mHuiShouRecyclerView.setAdapter(mHuitongAdapter);
    }

    //已配送数量
    private void initGoodsRecycle(List<OrderShowBean.GoodsBean> goods) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mNumRecyclerView.setLayoutManager(manager);
        mPeisongAdapter = new OperationGoodsAdapterTwo(this, goods);
        mPeisongAdapter.setOnItemClickListener(new OperationGoodsAdapterTwo.OnViewItemClickListener() {
            @Override
            public void doEditCount(int position, int count) {
                OrderShowBean.GoodsBean item = mPeisongAdapter.getItem(position);
//                if (count < 1) {
//                    hint("购买数量不能低于最低购买量!");
//                    return;
//                }
                if (count >= item.getNum() - item.getR_water()) {
                    item.setW_water(item.getNum() - item.getR_water());
                    item.setSnum(item.getNum() - item.getW_water() - item.getR_water());
                } else {
                    item.setW_water(count);
                    item.setSnum(item.getNum() - item.getW_water() - item.getR_water());
                }
                mPeisongAdapter.notifyDataSetChanged();
                CalculatePrice();
            }
        });
        mPeisongAdapter.setOnItemLanItenClickListener(new OperationGoodsAdapterTwo.OnViewLanItenClickListener() {
            @Override
            public void doEditCount(int positon, int count) {
                OrderShowBean.GoodsBean item = mPeisongAdapter.getItem(positon);
//                if (count < 1) {
//                    hint("购买数量不能低于最低购买量!");
//                    return;
//                }
                if (count > item.getNum() || count >= item.getNum() - item.getW_water()) {
                    item.setR_water(item.getNum() - item.getW_water());
                    item.setSnum(item.getNum() - item.getW_water() - item.getR_water());
                } else {
                    item.setR_water(count);
                    item.setSnum(item.getNum() - item.getW_water() - item.getR_water());
                }
                CalculatePrice();
                mPeisongAdapter.notifyDataSetChanged();
            }
        });
        mPeisongAdapter.setShopCartInterface(new ShopCartInterface() {
            @Override
            public void add(View view, int position) {
                CalculatePrice();
            }

            @Override
            public void remove(View view, int position) {
                CalculatePrice();
            }
        });
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg_new));
        mNumRecyclerView.addItemDecoration(divider);
        mNumRecyclerView.setAdapter(mPeisongAdapter);
    }

    @OnClick({R.id.detailBtn, R.id.huiShouBucketBtn, R.id.tuiShuiBucketBtn, R.id.remove, R.id.add, R.id.account, R.id.addBucketBtn, R.id.gotopay, R.id.po_add, R.id.po_account, R.id.po_remove})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gotopay:
                postGoPay();
                break;
            case R.id.addBucketBtn:
                AddhuitongDialog ziYingDialog = new AddhuitongDialog(this, recyclerTongDialogData, new AddhuitongDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<OrderShowBean.RecyclerBean> data) {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setNum(1);
                        }
                        mZiyingAdapter.setData(data);
//                        mGotopay.setBackgroundResource(R.drawable.blue_rectangle);
                    }
                });
                ziYingDialog.show();
                break;
            case R.id.remove:
                if (mCount <= 0) {
                    return;
                }
                mCount--;
                mAccount.setText(mCount + "");
                break;
            case R.id.add:
                mCount++;
                mAccount.setText(mCount + "");
                break;
            case R.id.account:
                EditPurchaseAmountDialog mEditPurchaseAmountDialog = new EditPurchaseAmountDialog(OperationOrderActivityNew.this, mCount, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        mCount = count;
                        mAccount.setText(count + "");
                    }
                });
                mEditPurchaseAmountDialog.show();
                break;
            case R.id.po_remove:
                if (poCount <= 0) {
                    return;
                }
                poCount--;
                mPoAccount.setText(poCount + "");
                break;
            case R.id.po_add:
                poCount++;
                mPoAccount.setText(poCount + "");
                break;
            case R.id.po_account:
                EditPurchaseAmountDialog mPurchaseAmountDialog = new EditPurchaseAmountDialog(OperationOrderActivityNew.this, poCount, new EditPurchaseAmountDialog.EditPurchaseAmountConfirmListener() {
                    @Override
                    public void onEditPurchaseAmountConfirm(int count) {
                        poCount = count;
                        mPoAccount.setText(count + "");
                    }
                });
                mPurchaseAmountDialog.show();
                break;
            case R.id.detailBtn:
                OrderDetailActivity.startAct(OperationOrderActivityNew.this, 1, mOrderData);
                break;
            case R.id.huiShouBucketBtn:
                if (mHuitongAdapter.getData() != null && recyclerTongDialogData != null) {
                    for (int i = 0; i < mHuitongAdapter.getData().size(); i++) {
                        for (int j = 0; j < recyclerTongDialogData.size(); j++) {
                            if (mHuitongAdapter.getData().get(i).getGid() == recyclerTongDialogData.get(j).getGid()) {
                                recyclerTongDialogData.get(j).setCheck(true);
                            }
                        }
                    }
                }
                AddhuitongDialog dialog = new AddhuitongDialog(this, recyclerTongDialogData, new AddhuitongDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<OrderShowBean.RecyclerBean> data) {
                        mHuitongAdapter.setData(data);
                    }
                });
                dialog.show();
                break;
        }
    }

    private void postGoPay() {
        //将设置的商品保存在本地
        CommonUtils.savePeisong(mPeisongAdapter.getData());
        CommonUtils.saveHuiTong(mHuitongAdapter.getData());
        PreferenceUtils.putInt("number", num);
        PreferenceUtils.putString("price", mMoney.getText().toString());
        String count = mAccount.getText().toString().trim();
        String poNum = mPoAccount.getText().toString().trim();
        String butiePrice = mNumTv.getText().toString().trim();
        if (butiePrice.equals("")) {
            ToastUitl.showToastCustom("价格不能为空");
            return;
        }
        KLog.d("\n" + CommonUtils.getToken() + "\n" + mResult.getOrder_sn() + "\n" + count + "\n" + packageData(mZiyingAdapter.getData()) + "\n" + butiePrice);
        RetrofitUtils.getInstances().create().goPay(CommonUtils.getToken(), mResult.getOrder_sn(), count, poNum, packageData(mZiyingAdapter.getData()), butiePrice).enqueue(new Callback<EntityObject<PreparePayBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PreparePayBean>> call, Response<EntityObject<PreparePayBean>> response) {
                EntityObject<PreparePayBean> body = response.body();
                if (body.getCode() == 200) {
                    PreparePayBean result = body.getResult();
                    //1代表桶的管理二维码
                    result.setPayFrom(1);
                    ReceivePayActivity.startAtc(OperationOrderActivityNew.this, result, 6);
                    finish();
                    //如果价格为0的时候code =800
                } else if (body.getCode() == 800) {
//                    finish();
                    //发消息刷新回收杂桶的数据
//                    EventBus.getDefault().post(new RecyclingEvent());
                    mTag = 1;
                    getList();
                } else {
                    hint(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PreparePayBean>> call, Throwable t) {

            }
        });
    }

    private String packageData(List<OrderShowBean.RecyclerBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderShowBean.RecyclerBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getNum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    //确认订单
    private void post(final int status) {
        KLog.d("\n" + CommonUtils.getToken() + "\n" + status + "\n" + mOrderData + "\n" + packageGoodsData(mPeisongAdapter.getData()) + "\n" + packageHuitongData(mHuitongAdapter.getData())
                + "\n" + packageTuishuiData(mPeisongAdapter.getData()) + "\n" + packageLanGoodsData(mPeisongAdapter.getData()));
        showLoad();
        RetrofitUtils.getInstances().create().sureOrder(CommonUtils.getToken(), status, mOrderData,
                packageGoodsData(mPeisongAdapter.getData()), packageHuitongData(mHuitongAdapter.getData()), packageLanGoodsData(mPeisongAdapter.getData()),
                packageTuishuiData(mPeisongAdapter.getData())).enqueue(new Callback<EntityObject<OperationSureBean>>() {
            @Override
            public void onResponse(Call<EntityObject<OperationSureBean>> call, Response<EntityObject<OperationSureBean>> response) {
                hintLoad();
                EntityObject<OperationSureBean> body = response.body();
                if (body.getCode() == 200) {
                    ToastUitl.showToastCustom("操作成功");
//                    if (status == 1) {
//                        EventBus.getDefault().post(new MainEvent(3));
//                    } else {
                    int jumpType = body.getResult().getJump_type();
                    if (jumpType == 0) {
                        EventBus.getDefault().post(new MainEvent(3));
                    } else {
                        EventBus.getDefault().post(new MainEvent(2));
                    }
//                    }
                    finish();
                } else {
                    ToastUitl.showToastCustom(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<OperationSureBean>> call, Throwable t) {
                hintLoad();
            }
        });
    }

    private String packageGoodsData(List<OrderShowBean.GoodsBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderShowBean.GoodsBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getSnum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    private String packageLanGoodsData(List<OrderShowBean.GoodsBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderShowBean.GoodsBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getR_water());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    private String packageHuitongData(List<OrderShowBean.RecyclerBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderShowBean.RecyclerBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getSnum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    private String packageTuishuiData(List<OrderShowBean.GoodsBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderShowBean.GoodsBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getW_water());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        finish();
    }

    public void CalculatePrice() {
        num = 0;
        total = 0.0;
        for (OrderShowBean.GoodsBean bean : mPeisongAdapter.getData()) {
            total = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getSnum()), total);
            num = CalculateUtils.add(bean.getSnum(), num);
        }
//        KLog.d(total + "\n" + mSub);
        double sub = CalculateUtils.sub(total, mSub);
        if (sub <= 0) {
            sub = 0.00;
        }
        mNumber.setText(num + "");
        mMoney.setText("￥" + sub);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(RecyclingEvent event) {
        showLoad();
        getList();
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }

    public abstract class NoDoubleClickListener implements View.OnClickListener {
        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View view) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(view);
            }
        }

        protected void onNoDoubleClick(View v) {

        }
    }
}
