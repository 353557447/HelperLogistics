package com.shuiwangzhijia.wuliu.http;


import com.shuiwangzhijia.wuliu.bean.AddressBean;
import com.shuiwangzhijia.wuliu.bean.AppendOrderBean;
import com.shuiwangzhijia.wuliu.bean.BannerBean;
import com.shuiwangzhijia.wuliu.bean.BucketBean;
import com.shuiwangzhijia.wuliu.bean.BucketOperationBean;
import com.shuiwangzhijia.wuliu.bean.BucketRecordBean;
import com.shuiwangzhijia.wuliu.bean.BucketShowBean;
import com.shuiwangzhijia.wuliu.bean.CanShuipiaoBean;
import com.shuiwangzhijia.wuliu.bean.CashBean;
import com.shuiwangzhijia.wuliu.bean.CommentBean;
import com.shuiwangzhijia.wuliu.bean.CompletedDetailsBean;
import com.shuiwangzhijia.wuliu.bean.ConsumerDetailBean;
import com.shuiwangzhijia.wuliu.bean.CountBean;
import com.shuiwangzhijia.wuliu.bean.CustomerBean;
import com.shuiwangzhijia.wuliu.bean.DefaultStoreBean;
import com.shuiwangzhijia.wuliu.bean.DriverBean;
import com.shuiwangzhijia.wuliu.bean.EditDeliveryOrderBean;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.GoodsManageBean;
import com.shuiwangzhijia.wuliu.bean.GoodsTypeBean;
import com.shuiwangzhijia.wuliu.bean.LoginBean;
import com.shuiwangzhijia.wuliu.bean.OperationSureBean;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.bean.OrderShowBean;
import com.shuiwangzhijia.wuliu.bean.OutOrderBean;
import com.shuiwangzhijia.wuliu.bean.OutOrderDetailBean;
import com.shuiwangzhijia.wuliu.bean.PayBean;
import com.shuiwangzhijia.wuliu.bean.PayOrderDetailBean;
import com.shuiwangzhijia.wuliu.bean.PolicyBean;
import com.shuiwangzhijia.wuliu.bean.PreparePayBean;
import com.shuiwangzhijia.wuliu.bean.RecordBean;
import com.shuiwangzhijia.wuliu.bean.SendOrderBean;
import com.shuiwangzhijia.wuliu.bean.ShopBean;
import com.shuiwangzhijia.wuliu.bean.StatisticsBean;
import com.shuiwangzhijia.wuliu.bean.StoreListBean;
import com.shuiwangzhijia.wuliu.bean.TuiTongBean;
import com.shuiwangzhijia.wuliu.bean.UpdateInfo;
import com.shuiwangzhijia.wuliu.bean.UserBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OutDetailsBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.StatisticalInfoBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WareHouseOperationBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseDriverBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseOutOrderBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseUserBean;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface AppService {

    /**
     * 登录
     *
     * @param phone
     * @param pswd
     * @return
     */
    @POST("logistics/v1.Login/tologin")
  //  @FormUrlEncoded
    Call<EntityObject<LoginBean>> getLogin(@Query("phone") String phone, @Query("psword") String pswd,@Query("serial") String serial);

    /**
     * 登出原因
     *
     * @param phone
     * @return
     */
    @POST("logistics/v1.Login/outlogin")
    Call<EntityObject<String>> loginOutInfo(@Query("phone") String phone);

    /**
     * 版本信息
     *
     * @param type        0 Android  1 ios
     * @param client_type 0用户端 1 水店 2 司机端
     * @return
     */
    @GET("shop/v1.Versions/GetVersionInfo")
    Call<EntityObject<UpdateInfo>> getAppVersionInfo(@Query("type") int type, @Query("client_type") int client_type);

    @Streaming //大文件时要加不然会OOM
    @GET
    Call<ResponseBody> downloadFile(@Url String url);


    /**
     * 新订单 列表
     *
     * @param status
     * @return
     */
    @GET("logistics/v1.Order/newOrder_v1")
    Call<EntityObject<ArrayList<OrderBean>>> getOrderList(@Query("status") int status, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 操作订单
     *
     * @param order_sn
     * @return
     */
    @GET("logistics/v1.Order/orderShowNew_v1")
    Call<EntityObject<OrderBean>> HandleOrder(@Query("order_sn") String order_sn);


    /**
     * 操作订单-回收桶弹窗
     *
     * @return
     */
    @GET("logistics/v1.Order/ShowBackBucket")
    Call<EntityObject<ArrayList<BucketBean>>> showDialogBucket();

    /**
     * 订单详情
     *
     * @param order_sn
     * @return
     */
    @GET("logistics/v1.Order/showOrderDetail_v1")
    Call<EntityObject<OrderBean>> getOrderDetail(@Query("order_sn") String order_sn);

    /**
     * 追加订单详情
     *
     * @param order_sns
     * @return
     */
    @GET("logistics/v1.Order/appendOrderDetail_v1")
    Call<EntityObject<AppendOrderBean>> appendOrderDetail(@Query("order_sns") String order_sns);

    /**
     * 订单详情
     *
     * @param order_sn
     * @return
     */
    @GET("logistics/v1.Order/orderDetail_v1")
    Call<EntityObject<PayOrderDetailBean>> getPayOrderDetail(@Query("order_no") String order_sn);

    /**
     * 转单司机列表
     *
     * @return
     */
    @GET("logistics/v1.Order/showDrivers")
    Call<EntityObject<ArrayList<DriverBean>>> getDriverList();

    /**
     * 转单司机
     *
     * @return
     */
    @GET("logistics/v1.Order/slip_v1")
    Call<EntityObject<Object>> turnDriver(@Query("order_sn") String order_sn, @Query("id") int id);

    /**
     * 广告
     *
     * @return
     */
    @GET("logistics/v1.Index/Banner")
    Call<EntityObject<ArrayList<BannerBean>>> getBannerList();

    /**
     * 代下单 -查找店铺
     *
     * @return
     */
    @GET("logistics/v1.Goods/GetShops")
    Call<EntityObject<ArrayList<ShopBean>>> getShopList(@Query("lnglat") String lnglat, @Query("content") String content, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 管理-店铺营业状态
     *
     * @return
     */
    @GET("logistics/v1.Shop/business")
    Call<EntityObject<Object>> getShopState();

    /**
     * 管理-店铺信息
     *
     * @return
     */
    @GET("logistics/v1.Spread/showShop")
    Call<EntityObject<ShopBean>> getShopInfo();

    /**
     * 商品类型
     *
     * @return
     */
    @GET("logistics/v1.Goods/showGoods_header")
    Call<EntityObject<ArrayList<GoodsTypeBean>>> getGoodsType(@Query("id") int id);

    /**
     * 获取商品列表
     *
     * @return
     */
    @GET("logistics/v1.Goods/showGoods")
    Call<EntityObject<ArrayList<GoodsBean>>> getGoodsList(@Query("gtype") String gtype, @Query("id") int id, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 地址
     *
     * @return
     */
    @GET("logistics/v1.Address/ListAddress")
    Call<EntityObject<ArrayList<AddressBean>>> getAddressList(@Query("sid") int id);

    /**
     * 设置默认地址
     *
     * @return
     */
    @GET("logistics/v1.Address/DefaultAddress")
    Call<EntityObject<String>> setDefaultAddress(@Query("id") int id, @Query("sid") int sid);

    /**
     * 获取默认地址
     *
     * @return
     */
    @GET("logistics/v1.Address/defaultAddr")
    Call<EntityObject<ArrayList<AddressBean>>> getDefaultAddress(@Query("sid") int id);

    /**
     * 删除地址
     *
     * @return
     */
    @GET("logistics/v1.Address/DeleteAddress")
    Call<EntityObject<String>> deleteAddress(@Query("id") int id, @Query("sid") int sid);

    /**
     * 新增和修改地址
     *
     * @param aname
     * @param sphone
     * @param province
     * @param city
     * @param dist
     * @param daddress
     * @param ulnglat
     * @param type     1发货，2自提
     * @param id       用于修改地址
     * @return
     */
    @GET("logistics/v1.Address/AddAddress")
    Call<EntityObject<String>> addAddress(@Query("aname") String aname, @Query("sphone") String sphone,
                                          @Query("province") String province, @Query("city") String city,
                                          @Query("dist") String dist, @Query("daddress") String daddress,
                                          @Query("ulnglat") String ulnglat, @Query("type") int type, @Query("id") String id, @Query("sid") int sid);

    /**
     * 商品管理列表
     *
     * @param status 1上架，0下架
     * @return
     */
    @GET("logistics/v1.Goods/goodsManagement")
    Call<EntityObject<ArrayList<GoodsManageBean>>> getGoodsManageList(@Query("status") int status);

    /**
     * 商品管理 上架下架处理
     *
     * @param id 商品id
     * @return
     */
    @GET("logistics/v1.Goods/upGoods")
    Call<EntityObject<String>> upDownGoods(@Query("id") int id);

    /**
     * 评论列表
     *
     * @return
     */
    @GET("logistics/v1.Spread/showComments")
    Call<EntityObject<ArrayList<CommentBean>>> getCommentList();

    /**
     * 店铺统计
     *
     * @return
     */
    @GET("logistics/v1.Statistics/shop_statistics")
    Call<EntityObject<StatisticsBean>> getShopStatistics();

    /**
     * 司机用户信息
     *
     * @return
     */
    @GET("logistics/v1.Personal/showPersonal")
    Call<EntityObject<UserBean>> getUserInfo();

    /**
     * 今日送水记录
     *
     * @return
     */
    @GET("logistics/v1.Personal/deliver_water")
    Call<EntityObject<ArrayList<RecordBean>>> getWaterList(@Query("time") long time, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 今日收款记录
     *
     * @return
     */
    @GET("logistics/v1.Personal/deliver_price")
    Call<EntityObject<ArrayList<RecordBean>>> getMoneyList(@Query("time") long time, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 今日回桶记录
     *
     * @return
     */
    @GET("logistics/v1.Personal/deliver_buck")
    Call<EntityObject<ArrayList<RecordBean>>> getBucketList(@Query("time") long time, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 自营桶记录
     *
     * @return
     */
    @GET("logistics/v1.Personal/singlebuck_v1")
    Call<EntityObject<ArrayList<RecordBean>>> getSelfBucketList(@Query("sid") int sid, @Query("time") long time);

    /**
     * 杂桶记录
     *
     * @return
     */
    @GET("logistics/v1.Personal/zbuck_v1")
    Call<EntityObject<ArrayList<RecordBean>>> getOtherBucketList(@Query("sid") int sid, @Query("time") long time);

    /**
     * 添加自营桶
     *
     * @return
     */
    @GET("logistics/v1.Barrel/getbarreldata")
    Call<EntityObject<ArrayList<BucketBean>>> getBucketData();

    /**
     * 提交换桶
     *
     * @param znum
     * @param sid
     * @param payWay
     * @param price
     * @param s_num
     * @param data
     * @return
     */
    @GET("logistics/v1.Barrel/changebarrel")
    Call<EntityObject<PolicyBean>> postChangeBucket(@Query("z_num") int znum, @Query("sid") int sid, @Query("barrel_way") String payWay, @Query("price") String price, @Query("s_num") String s_num, @Query("data") String data);

    /**
     * 压桶
     *
     * @param sid
     * @param price
     * @param data
     * @return
     */
    @GET("logistics/v1.Bucket/pressbarrel")
    Call<EntityObject<PolicyBean>> postPressBucket(@Query("sid") int sid, @Query("price") String price, @Query("data") String data);

    /**
     * 退桶列表
     *
     * @return
     */
    @GET("logistics/v1.Bucket/barrelrecordList")
    Call<EntityObject<ArrayList<BucketRecordBean>>> getBucketRecordList(@Query("sid") int sid, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 退桶操作页面
     *
     * @return
     */
    @GET("logistics/v1.Bucket/barrelrecord")
    Call<EntityObject<BucketOperationBean>> getBucketRecord(@Query("sid") int sid, @Query("bucket_order_sn") String orderSn);

    /**
     * 退桶
     *
     * @param id
     * @return
     */
    @GET("logistics/v1.Bucket/quitbarrel")
    Call<EntityObject<PolicyBean>> postQuitBucket(@Query("bucket_order_sn") String id);

    /**
     * 退桶列表
     *
     * @return
     */
//    @GET("api/WtapiDriver.Barrel/refundbarrel")
//    Call<EntityObject<ArrayList<BucketBean>>> getQuitBucketList(@Query("content") String content, @Query("start") int page, @Query("limit") int pageSize);
    @GET("logistics/v1.Bucket/shopbarrel")
    Call<EntityObject<TuiTongBean>> getQuitBucketList(@Query("content") String content, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 空桶管理——选择水店
     *
     * @return
     */
    @GET("logistics/v1.Barrel/getshopdata")
    Call<EntityObject<ArrayList<ShopBean>>> getWaterShopList(@Query("lnglat") String lnglat, @Query("content") String content, @Query("start") int page, @Query("limit") int pageSize);


    /**
     * 客户管理
     *
     * @return
     */
    @GET("logistics/v1.Statistics/customer")
    Call<EntityObject<ArrayList<CustomerBean>>> getCustomerList(@Query("start") int page, @Query("limit") int pageSize);

    /**
     * 消费明细
     *
     * @return
     */
    @GET("logistics/v1.Statistics/detailed")
    Call<EntityObject<ArrayList<ConsumerDetailBean>>> getConsumerDetailList(@Query("start") int page, @Query("limit") int pageSize);


    /***
     * 采购统计列表
     * @param start_time
     * @param end_time
     * @return
     */
    @GET("logistics/v1.Statistics/purchase_count")
    Call<EntityObject<ArrayList<CountBean>>> getPurchaseCountList(@Query("start_time") long start_time, @Query("end_time") long end_time);

    /***
     * 订单统计列表
     * @param start_time
     * @param end_time
     * @return
     */
    @GET("logistics/v1.Statistics/order_count")
    Call<EntityObject<ArrayList<CountBean>>> getOrderCountList(@Query("start_time") long start_time, @Query("end_time") long end_time);

    /**
     * 店铺管理
     *
     * @param busi_time
     * @param is_free
     * @param effic
     * @param amount
     * @param full_free
     * @return
     */
    @GET("logistics/v1.Shop/AddShopInfo")
    Call<EntityObject<Object>> addShopInfo(@Query("busi_time") String busi_time, @Query("is_free") int is_free,
                                           @Query("effic") String effic, @Query("amount") String amount, @Query("full_free") String full_free);


    /**
     * 生成订单
     *
     * @param sprice
     * @param addr
     * @param data
     * @param remark
     * @return
     */
    @POST("logistics/v1.Order/generateOrderShop_v1")
    @FormUrlEncoded
    Call<EntityObject<PreparePayBean>> createOrder(@Field("token") String token, @Field("id") int sid, @Field("buk") int buk, @Field("sprice") String sprice, @Field("addr") String addr,
                                                   @Field("data") String data, @Field("remark") String remark, @Field("address_id") int id, @Field("type") int type);

    /**
     * 追加订单
     *
     * @param sprice
     * @param order_sn
     * @param data
     * @return
     */
    @GET("logistics/v1.Order/appendOrderShop_v1")
    Call<EntityObject<PreparePayBean>> appendOrder(@Query("sprice") String sprice, @Query("order_sn") String order_sn, @Query("data") String data);

    /**
     * 货到付款
     *
     * @return
     */
    @GET("logistics/v1.Order/preGenerateOrderShop")
    Call<EntityObject<PayBean>> payOffLine(@Query("order_no") String order_no);

    /**
     * 生成支付二维码
     *
     * @return
     */
    @GET("logistics/v1.Pingpp/qrcode")
    Call<EntityObject<String>> getPayQRcode(@Query("order_no") String order_no, @Query("type") int type);

    /**
     * 生成支付二维码
     *
     * @return
     */
    @GET("logistics/v1.Pingpp/another_pays")
    Call<EntityObject<PayBean>> payByDriver(@Query("order_no") String order_no, @Query("type") int type);

    /**
     * 在线支付
     *
     * @return
     */
    @GET("logistics/v1.Pingpp/pay")
    Call<EntityObject<PayBean>> getPayChannelInfo(@Query("order_no") String order_no, @Query("channel") String channel);

    /**
     * 接单
     *
     * @param order_no
     * @return
     */
    @GET("logistics/v1.Order/reseiveOrder")
    Call<EntityObject<Object>> receiptOrder(@Query("order_no") String order_no);

    /**
     * 取消订单
     *
     * @param order_no
     * @return
     */
    @GET("logistics/v1.Order/exitOrder")
    Call<EntityObject<Object>> cancelOrder(@Query("order_no") String order_no);

    /**
     * 接单
     *
     * @param order_no
     * @return
     */
    @GET("logistics/v1.Order/sendOrder")
    Call<EntityObject<Object>> sendOrder(@Query("order_no") String order_no, @Query("status") int status);

    /**
     * 提现
     *
     * @return
     */
    @GET("logistics/v1.Statistics/reflect")
    Call<EntityObject<CashBean>> getCashInfo();
    /**
     * 提交提现
     *
     * @return
     */
    /**
     * @param amount
     * @param remark
     * @param account
     * @param bank
     * @param card_no
     * @param id      驳回提现再次提现传id  其他不用传
     * @return
     */
    @GET("logistics/v1.Statistics/addreflect")
    Call<EntityObject<CashBean>> postCash(@Query("amount") String amount, @Query("remark") String remark, @Query("account") String account,
                                          @Query("bank") String bank, @Query("card_no") String card_no, @Query("id") String id);

    /**
     * 获取已经完成的出货单列表
     */
    @GET("logistics/v1.Outorder/outOrderList")
    Call<EntityObject<ArrayList<OutOrderBean>>> getOutOrderList(@Query("start") int page, @Query("limit") int pageSize);

    /**
     * 已完成出货单详情
     */
    @GET("logistics/v1.Outorder/finishOutOrderDetail")
    Call<EntityObject<CompletedDetailsBean>> getCompletedDetailInfo(@Query("out_order") String outOrder);

    /**
     * 待配送订单
     */
    @GET("logistics/v1.Outorder/sendOrderList")
    Call<EntityObject<ArrayList<SendOrderBean>>> getSendOrderList(@Query("start") int page, @Query("limit") int pageSize);

    /**
     * 判断是否有正在处理的订单
     */
    @GET("logistics/v1.Outorder/isOut")
    Call<EntityObject<String>> getOrderIsBegin();

    /**
     * 4、返回出货订单详情
     */
    @GET("logistics/v1.Outorder/outOrderDetail")
    Call<EntityObject<OutOrderDetailBean>> getOutOrderDetail(@Query("status") int status);

    /**
     * 删除出货单
     */
    @GET("logistics/v1.Outorder/deleteOutOrder")
    Call<EntityObject<String>> deleteOutOrder(@Query("out_order") String outOrder);

    /**
     * 生成出货单
     */
    @GET("logistics/v1.Outorder/makeOutOrder")
    Call<EntityObject<String>> makeOutOrder(@Query("order_sns") String outOrder, @Query("id") String id, @Query("cid") String cid);

    /**
     * 生成出货单
     */
    @GET("logistics/v1.Outorder/getStoreList")
    Call<EntityObject<ArrayList<StoreListBean>>> getStoreList();

    /**
     * 编辑订单--详情
     */
    @GET("logistics/v1.Outorder/editOutOrderDetail")
    Call<EntityObject<ArrayList<SendOrderBean>>> editOutOrderDetail(@Query("out_order") String outOrder);

    /**
     * 操作订单显示接口
     */
    @GET("logistics/v1.Order/orderShowNew_v3")
    Call<EntityObject<OrderShowBean>> orderShowNew(@Query("order_sn") String orderSn);

    /**
     * 确认订单
     *
     * @param status        status（1正常单，2赊账）
     * @param order_sn
     * @param data_order
     * @param data_recycler
     * @return
     */
    @POST("logistics/v1.Order/sureOrderNew_v2")
    @FormUrlEncoded
    Call<EntityObject<OperationSureBean>> sureOrder(@Field("token") String token, @Field("status") int status,
                                                    @Field("order_sn") String order_sn,
                                                    @Field("data_order") String data_order,
                                                    @Field("data_recycler") String data_recycler,
                                                    @Field("data_refundwt") String data_refundwt,//烂水
                                                    @Field("withdrawal_Water") String withdrawal_Water);//退水

    @POST("logistics/v1.Order/sureOrderNew")
    @FormUrlEncoded
    Call<EntityObject<String>> sureOrderNoHuitong(@Field("token") String token, @Field("status") int status,
                                                  @Field("order_sn") String order_sn,
                                                  @Field("data_order") String data_order,
                                                  @Field("data_refundwt") String data_refundwt);

    /**
     * 显示杂桶确认界面
     */
    @GET("logistics/v1.Order/ShowBucketDetail")
    Call<EntityObject<BucketShowBean>> showBucketDetail(@Query("order_sn") String orderSn);

    /**
     * 可换取的自营桶列表
     */
    @GET("logistics/v1.Bucket/canChangeBucketList")
    Call<EntityObject<ArrayList<BucketBean>>> canChangeBucketList();

    /**
     * 可换取的自营桶列表
     */
    @GET("logistics/v1.Bucket/canChangeBucketList")
    Call<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>> canHuiTonglist();

    /**
     * 显示可换取的水票列表
     */
    @GET("logistics/v1.Order/canChangePicket")
    Call<EntityObject<ArrayList<CanShuipiaoBean>>> canChangePicket();

    /**
     * 杂桶回收--去支付
     */
    @POST("logistics/v1.Bucket/goPay")
    @FormUrlEncoded
    Call<EntityObject<PreparePayBean>> goPay(@Field("token") String token,
                                             @Field("order_sn") String order_sn,
                                             @Field("z_num") String num,
                                             @Field("po_num") String po_num,
                                             @Field("data_bucket") String data_bucket,
                                             @Field("price") String price);

    /**
     * 杂桶回收--换水票
     */
    @POST("logistics/v1.Bucket/changeBucket")
    @FormUrlEncoded
    Call<EntityObject<String>> changeBucket(@Field("token") String token,
                                            @Field("order_sn") String order_sn,
                                            @Field("z_num") String num,
                                            @Field("data_bucket") String data_bucket,
                                            @Field("s_num") int snum,
                                            @Field("ticket") String ticket);

    /**
     * 首页商品展示
     */
    @GET("logistics/v1.Goods/showGoodsList")
    Call<EntityObject<ArrayList<GoodsBean>>> showGoodsList(@Query("sid") int sid, @Query("start") int start, @Query("limit") int limit);

    /**
     * 获取扫码支付的状态
     */
    @GET("logistics/v1.Pingpp/getPayStatus")
    Call<EntityObject<String>> getPayStatus(@Query("order_no") String order_no, @Query("type") int type);


    /**
     * 删除换桶
     *
     * @return
     */
    @GET("logistics/v1.Bucket/clearChangeBucket")
    Call<EntityObject<String>> clearChangeBucket(@Query("order_sn") String orderNo);

    /**
     * 出货订单详情接口--待出货
     *
     * @return
     */
    @GET("logistics/v1.Outorder/preOutOrderDetail")
    Call<EntityObject<EditDeliveryOrderBean>> preOutOrderDetail();

    /**
     * 确定出货单接口
     */
    @POST("logistics/v1.Outorder/additionalGoods")
    @FormUrlEncoded
    Call<EntityObject<String>> additionalGoods(@Field("token") String token, @Field("out_order") String order,
                                               @Field("data") String data);

    /**
     * 添加商品
     *
     * @return
     */
    @GET("logistics/v1.Outorder/additionalGoodsList")
    Call<EntityObject<ArrayList<GoodsBean>>> additionalGoodsList();

    /**
     * 判断是否有默认仓库接口
     *
     * @return
     */
    @GET("logistics/v1.Outorder/isDefaultStore")
    Call<EntityObject<DefaultStoreBean>> isDefaultStore();


   //------------------------------------------------仓库---------------------------------------------------//


/*    *//**
     * 登出原因
     *
     * @param phone
     * @return
     *//*
    @POST("/logistics/v1.Login/outlogin")
    Call<EntityObject<String>> loginOutInfo(@Query("phone") String phone);*/



    /**
     * 出货单列表
     *
     * @param status
     * @return
     */
    @GET("logistics/v1.Personals/getPersonalInfo")
    Call<EntityObject<ArrayList<OrderBean>>> warehouseGetOrderList(@Query("status") int status);

    /**
     * 操作订单
     *
     * @param order_sn
     * @return
     */
    @GET("logistics/v1.Order/orderShowNew")
    Call<EntityObject<OrderBean>> warehouseHandleOrder(@Query("order_sn") String order_sn);


    /**
     * 操作订单-回收桶弹窗
     *
     * @return
     */
    @GET("logistics/v1.Order/ShowBackBucket")
    Call<EntityObject<ArrayList<BucketBean>>> warehouseShowDialogBucket();

    /**
     * 订单详情
     *
     * @param order_sn
     * @return
     */
    @GET("logistics/v1.Order/showOrderDetail")
    Call<EntityObject<OrderBean>> warehouseGetOrderDetail(@Query("order_sn") String order_sn);


    /**
     * 订单详情
     *
     * @param order_sn
     * @return
     */
    @GET("logistics/v1.Order/orderDetail")
    Call<EntityObject<PayOrderDetailBean>> warehouseGetPayOrderDetail(@Query("order_no") String order_sn);

    /**
     * 转单司机列表
     *
     * @return
     */
    @GET("logistics/v1.Order/showDrivers")
    Call<EntityObject<ArrayList<WarehouseDriverBean>>> warehouseGetDriverList();

    /**
     * 转单司机
     *
     * @return
     */
    @GET("logistics/v1.Order/slip")
    Call<EntityObject<Object>> warehouseTurnDriver(@Query("order_sn") String order_sn, @Query("id") int id);

    /**
     * 确认订单
     *
     * @param status        status（1正常单，2赊账）
     * @param order_sn
     * @param data_order
     * @param data_recycler
     * @return
     */
    @POST("logistics/v1.Order/sureOrderNew")
    @FormUrlEncoded
    Call<EntityObject<String>> warehouseSureOrder(@Field("token") String token, @Field("status") int status, @Field("order_sn") String order_sn, @Field("data_order") String data_order, @Field("data_recycler") String data_recycler);

    /**
     * 广告
     *
     * @return
     */
    @GET("logistics/v1.Index/Banner")
    Call<EntityObject<ArrayList<BannerBean>>> warehouseGetBannerList();

    /**
     * 代下单 -查找店铺
     *
     * @return
     */
    @GET("logistics/v1.Goods/GetShops")
    Call<EntityObject<ArrayList<ShopBean>>> warehouseGetShopList(@Query("lnglat") String lnglat, @Query("content") String content, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 管理-店铺营业状态
     *
     * @return
     */
    @GET("logistics/v1.Shop/business")
    Call<EntityObject<Object>> warehouseGetShopState();

    /**
     * 管理-店铺信息
     *
     * @return
     */
    @GET("logistics/v1.Spread/showShop")
    Call<EntityObject<ShopBean>> warehouseGetShopInfo();


    /**
     * 获取商品列表
     *
     * @return
     */
    @GET("logistics/v1.Goods/showGoods")
    Call<EntityObject<ArrayList<GoodsBean>>> warehouseGetGoodsList(@Query("gtype") String gtype, @Query("id") int id, @Query("start") int page, @Query("limit") int pageSize);


    /**
     * 个人信息
     *
     * @return
     */
    @GET("logistics/v1.Personals/getPersonalInfo")
    Call<EntityObject<WarehouseUserBean>> warehouseGetUserInfo();

    /*
         * 今日送水记录
         *
         * @return
         */
    @GET("logistics/v1.Personals/deliver_water")
    Call<EntityObject<ArrayList<RecordBean>>> warehouseGetWaterList(@Query("time") long time, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 今日收款记录
     *
     * @return
     */
    @GET("logistics/v1.Personals/deliver_price")
    Call<EntityObject<ArrayList<RecordBean>>> warehouseGetMoneyList(@Query("time") long time, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 今日回桶记录
     *
     * @return
     */
    @GET("logistics/v1.Personals/deliver_buck")
    Call<EntityObject<ArrayList<RecordBean>>> warehouseGetBucketList(@Query("time") long time, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 自营桶记录
     *
     * @return
     */
    @GET("logistics/v1.Personals/singlebuck")
    Call<EntityObject<ArrayList<RecordBean>>> warehouseGetSelfBucketList(@Query("sid") int sid, @Query("time") long time, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 杂桶记录
     *
     * @return
     */
    @GET("logistics/v1.Personals/zbuck")
    Call<EntityObject<ArrayList<RecordBean>>> warehouseGetOtherBucketList(@Query("sid") int sid, @Query("time") long time, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 添加自营桶
     *
     * @return
     */
    @GET("logistics/v1.Barrel/getbarreldata")
    Call<EntityObject<ArrayList<BucketBean>>> warehouseGetBucketData();


    /**
     * 退桶列表
     *
     * @return
     */
    @GET("logistics/v1.Barrel/barrelrecord")
    Call<EntityObject<ArrayList<BucketRecordBean>>> warehouseGetBucketRecordList(@Query("sid") int sid, @Query("start") int page, @Query("limit") int pageSize);


    /**
     * 退桶列表
     *
     * @return
     */
    @GET("logistics/v1.Barrel/refundbarrel")
    Call<EntityObject<ArrayList<BucketBean>>> warehouseGetQuitBucketList(@Query("content") String content, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 空桶管理——选择水店
     *
     * @return
     */
    @GET("logistics/v1.Barrel/getshopdata")
    Call<EntityObject<ArrayList<ShopBean>>> warehouseGetWaterShopList(@Query("lnglat") String lnglat, @Query("content") String content, @Query("start") int page, @Query("limit") int pageSize);


    /**
     * 店铺管理
     *
     * @param busi_time
     * @param is_free
     * @param effic
     * @param amount
     * @param full_free
     * @return
     */
    @GET("logistics/v1.Shop/AddShopInfo")
    Call<EntityObject<Object>> warehouseAddShopInfo(@Query("busi_time") String busi_time, @Query("is_free") int is_free,
                                           @Query("effic") String effic, @Query("amount") String amount, @Query("full_free") String full_free);


    /**
     * 生成订单
     *
     * @param sprice
     * @param addr
     * @param data
     * @param remark
     * @return
     */
    @POST("logistics/v1.Order/generateOrderShop")
    @FormUrlEncoded
    Call<EntityObject<PreparePayBean>> warehouseCreateOrder(@Field("token") String token, @Field("id") int sid, @Field("buk") int buk, @Field("sprice") String sprice, @Field("addr") String addr,
                                                   @Field("data") String data, @Field("remark") String remark);

    /**
     * 追加订单
     *
     * @param sprice
     * @param order_sn
     * @param data
     * @return
     */
    @GET("logistics/v1.Order/appendOrderShop")
    Call<EntityObject<PreparePayBean>> warehouseAppendOrder(@Query("sprice") String sprice, @Query("order_sn") String order_sn, @Query("data") String data);


    /**
     * 生成支付二维码
     *
     * @return
     */
    @GET("logistics/v1.Pingpp/qrcode")
    Call<EntityObject<String>> warehouseGetPayQRcode(@Query("order_no") String order_no, @Query("type") int type);


    /**
     * 接单
     *
     * @param order_no
     * @return
     */
    @GET("logistics/v1.Order/reseiveOrder")
    Call<EntityObject<Object>> warehouseReceiptOrder(@Query("order_no") String order_no);

    /**
     * 取消订单
     *
     * @param order_no
     * @return
     */
    @GET("logistics/v1.Order/exitOrder")
    Call<EntityObject<Object>> warehouseCancelOrder(@Query("order_no") String order_no);

    /**
     * 接单
     *
     * @param order_no
     * @return
     */
    @GET("logistics/v1.Order/sendOrder")
    Call<EntityObject<Object>> warehouseSendOrder(@Query("order_no") String order_no, @Query("status") int status);

    /**
     * 提现
     *
     * @return
     */
    @GET("logistics/v1.Statistics/reflect")
    Call<EntityObject<CashBean>> warehouseGetCashInfo();
    /**
     * 提交提现
     *
     * @return
     */
    /**
     * @param amount
     * @param remark
     * @param account
     * @param bank
     * @param card_no
     * @param id      驳回提现再次提现传id  其他不用传
     * @return
     */
    @GET("logistics/v1.Statistics/addreflect")
    Call<EntityObject<CashBean>> warehousePostCash(@Query("amount") String amount, @Query("remark") String remark, @Query("account") String account,
                                          @Query("bank") String bank, @Query("card_no") String card_no, @Query("id") String id);

    /**
     * 统计出货信息
     */
    @GET("logistics/v1.Statisticss/getOutOrderDetail")
//    Call<EntityObject<ResultListData<ArrayList<GoodsBean>>>> getStatisticalInfo(@Query("status") int status ,  @Query("start") int page, @Query("limit") int pageSize);
    Call<EntityObject<StatisticalInfoBean>> warehouseGetStatisticalInfo(@Query("status") int status, @Query("start") int page, @Query("limit") int pageSize);

    /**
     * 出货单列表接口
     */
    @GET("logistics/v1.Outorders/OutOrderList")
    Call<EntityObject<ArrayList<WarehouseOutOrderBean>>> warehouseGetOutOrderList(@Query("status") int status, @Query("start") int page, @Query("limit") int pageSize, @Query("order_type") int orderType);

    /**
     * 出货查看详情接口：
     */
    @GET("logistics/v1.Outorders/OutOrderDetail")
    Call<EntityObject<OutDetailsBean>> warehouseGetOutDetailsInfo(@Query("out_order") String order);

    /**
     * 确认提货接口:
     */
    @POST("logistics/v1.Outorders/OutOrderOperation")
    @FormUrlEncoded
    Call<EntityObject<String>> warehouseConfirmShipment(@Field("token") String token, @Field("out_order") String order, @Field("data") String data);

    /**
     * 回仓操作显示接口：
     */
    @GET("logistics/v1.Outorders/backStoreDetail")
    Call<EntityObject<WareHouseOperationBean>> warehouseGetWareHouseOperation(@Query("out_order") String order);

    /**
     * 添加 回自营桶/退水 列表接口：
     */
    @GET("logistics/v1.Outorders/addBucketList")
    Call<EntityObject<ArrayList<BucketBean>>> warehouseGetBucketList(@Query("out_order") String order, @Query("status") int status);

    @GET("logistics/v1.Outorders/addBucketList")
    Call<EntityObject<ArrayList<WarehouseGoodsBean>>> warehouseGetBucketListOther(@Query("out_order") String order, @Query("status") int status);

    /**
     * 回仓确认接口
     */
    @POST("logistics/v1.Outorders/backSure")
    @FormUrlEncoded
    Call<EntityObject<String>> warehouseGetBackSure(@Field("token") String token, @Field("out_order") String order, @Field("data_order") String dataOeder
            , @Field("data_back") String dataBack, @Field("refund") String dataRefund, @Field("zybucket") String zyBucket, @Field("pobucket") int poBucket, @Field("miscellaneous") int num);

    /**
     * 订单详情列表接口  出货中  已完成
     */
    @GET("logistics/v1.Outorders/finishOrderDetail")
    Call<EntityObject<OrderDetailBean>> warehouseGetFinishOrderDetail(@Query("out_order") String order);

    /**
     * 订单详情列表接口  出货中  已完成
     */
    @GET("logistics/v1.Outorders/addCouldOutGoodsList")
    Call<EntityObject<ArrayList<WarehouseGoodsBean>>> warehouseAddCouldOutGoodsList(@Query("out_order") String order);

    /**
     * 司机端搜索订单
     */
    @GET("logistics/v1.Order/searchOrder")
    Call<Object> driverSearchOrder(@Query("search") String search);

    /**
     * 仓库端搜索订单
     */
    @GET("logistics/v1.Outorders/searchOrder")
    Call<Object> warehouseSearchOrder(@Query("search") String search);
}
