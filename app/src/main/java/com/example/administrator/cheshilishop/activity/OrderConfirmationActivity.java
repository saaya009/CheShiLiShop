package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.bean.BookingBean;
import com.example.administrator.cheshilishop.bean.ServiceBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.DateUtil;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.roundedimageview.RoundedImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

/**
 * 作者：Ayase
 * 邮箱：ayase@ayase.cn
 */
public class OrderConfirmationActivity extends BaseActivity {


    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.image_id)
    ImageView mImageId;
    @BindView(R.id.tv_ordernum)
    TextView mTvOrdernum;
    @BindView(R.id.img_logo)
    RoundedImageView mImgLogo;
    @BindView(R.id.tv_shopname)
    TextView mTvShopname;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.tv_number)
    TextView mTvNumber;
    @BindView(R.id.tv_number2)
    TextView mTvNumber2;
    @BindView(R.id.tv_service)
    TextView mTvService;
    @BindView(R.id.tv_ordertime)
    TextView mTvOrdertime;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_bookingtime)
    TextView mTvBookingtime;
    @BindView(R.id.tv_Amount)
    TextView mTvAmount;
    @BindView(R.id.tv_Offer)
    TextView mTvOffer;
    @BindView(R.id.tv_preferential)
    TextView mTvPreferential;
    @BindView(R.id.image_ad)
    ImageView mImageAd;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.layout_confirm)
    RelativeLayout mLayoutConfirm;
    @BindView(R.id.btn_home)
    Button mBtnHome;
    @BindView(R.id.btn_list)
    Button mBtnList;
    @BindView(R.id.layout_suc)
    RelativeLayout mLayoutSuc;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.tv_times)
    TextView mTvTimes;
    @BindView(R.id.btn_cancel2)
    Button mBtnCancel2;
    @BindView(R.id.layout_confirm2)
    RelativeLayout mLayoutConfirm2;

    private String appointID;
    private String serviceID;
    private String confirmCode;
    private String type;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_orderconfirmation);
        ButterKnife.bind(this);
    }

    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return new TopView(topbar_iv_back, topbar_tv_title);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {
        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mBtnList.setOnClickListener(this);
        mBtnHome.setOnClickListener(this);
        mBtnCancel2.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("预约详情");
        appointID = getIntent().getStringExtra("AppointID");
        serviceID = getIntent().getStringExtra("ServiceID");
        confirmCode = getIntent().getStringExtra("ConfirmCode");
        type = getIntent().getStringExtra("type");

        switch (type) {
            case "0"://
                if ("0".equals(appointID)) {
                    getServiceData();
                    mTvType.setText("未预约");
                    mBtnCancel.setVisibility(View.GONE);
                } else {
                    mTvType.setText("已预约");
                    getAppointData();
                }
                mLayoutConfirm2.setVisibility(View.VISIBLE);
                mLayoutConfirm.setVisibility(View.GONE);
                mLayoutSuc.setVisibility(View.GONE);
                break;
            case "1"://列表
                getAppointData();
                mTvType.setText("用户取消");
                mLayoutSuc.setVisibility(View.GONE);
                mLayoutConfirm2.setVisibility(View.GONE);
                mLayoutConfirm.setVisibility(View.INVISIBLE);
                break;
            case "2"://列表
                getAppointData();
                mTvType.setText("商家取消");
                mLayoutSuc.setVisibility(View.GONE);
                mLayoutConfirm2.setVisibility(View.GONE);
                mLayoutConfirm.setVisibility(View.INVISIBLE);
                break;
            case "4"://已履约
                getAppointData();
                mTvType.setText("已验证");
                mLayoutConfirm2.setVisibility(View.GONE);
                mLayoutSuc.setVisibility(View.GONE);
                mLayoutConfirm.setVisibility(View.INVISIBLE);
                break;
            case "5"://查看订单
                getServiceData();
                mTvType.setText("已验证");
                mLayoutConfirm2.setVisibility(View.GONE);
                mLayoutSuc.setVisibility(View.VISIBLE);
                mLayoutConfirm.setVisibility(View.INVISIBLE);
                break;
            case "3"://
                mTvType.setText("未履约");
                getAppointData();
                mLayoutConfirm2.setVisibility(View.GONE);
                mBtnConfirm.setVisibility(View.GONE);
                mLayoutConfirm.setVisibility(View.VISIBLE);
                mLayoutSuc.setVisibility(View.GONE);
                break;
            case "9"://
                mTvType.setText("已服务");
                getServiceData();
                mLayoutConfirm2.setVisibility(View.GONE);
                mLayoutSuc.setVisibility(View.GONE);
                mLayoutConfirm.setVisibility(View.INVISIBLE);
                break;
            case "99"://扫码
                if ("0".equals(appointID)) {
                    getServiceData();
                    mTvType.setText("未预约");
                    mBtnCancel.setVisibility(View.GONE);
                } else {
                    mTvType.setText("已预约");
                    getAppointData();
                }
                mLayoutConfirm2.setVisibility(View.GONE);
                mLayoutConfirm.setVisibility(View.VISIBLE);
                mLayoutSuc.setVisibility(View.GONE);
                break;
            default:
                getServiceData();
                mLayoutConfirm2.setVisibility(View.GONE);
                mLayoutSuc.setVisibility(View.GONE);
                mLayoutConfirm.setVisibility(View.INVISIBLE);
                break;
        }

    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_cancel://取消预约
                cancelAppoint();
                break;
            case R.id.btn_confirm://确认预约
                confirmService();
                break;
            case R.id.btn_list://去列表
                Intent intent = new Intent(this, BookingManagementActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_home://去首页
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_cancel2://取消预约
                cancelAppoint();
                break;

        }
    }

    /**
     * 取消预约
     */
    private void cancelAppoint() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", appointID);
        RestClient.post(UrlUtils.cancelAppoint(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    Log.d("取消预约", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("Status");
                    if ("0".equals(status)) {
                        ToastUtils.show(OrderConfirmationActivity.this, "取消订单");
                        finish();
                    } else if ("-1".equals(status)) {
                        Intent intent = new Intent(OrderConfirmationActivity.this, SuccessActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(OrderConfirmationActivity.this, "取消失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                RequestParams errParams = new RequestParams();
                errParams.add("LogCont", new String(responseBody));
                errParams.add("Url", UrlUtils.cancelAppoint());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, OrderConfirmationActivity.this, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
    }

    /**
     * 确认预约
     */
    private void confirmService() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("AppointID", appointID);
        params.add("ServiceID", serviceID);
        params.add("ConfirmCode", confirmCode);
        RestClient.post(UrlUtils.confirmService(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    Log.d("确认预约", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("Status");
                    if ("0".equals(status)) {
                        Intent intent = new Intent(OrderConfirmationActivity.this, SuccessActivity.class);
                        intent.putExtra("AppointID", appointID);
                        intent.putExtra("ServiceID", serviceID);
                        startActivity(intent);
                        finish();
                    } else if ("-1".equals(status)) {
                        Intent intent = new Intent(OrderConfirmationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                errParams.add("LogCont", new String(responseBody));
                errParams.add("Url", UrlUtils.confirmService());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, OrderConfirmationActivity.this, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        RequestParams errParams = new RequestParams();
                        errParams.add("LogCont", new String(responseBody));
                        errParams.add("Url", UrlUtils.queryServiceAppointDetail());
                        errParams.add("PostData", params.toString());
                        errParams.add("WToken", CheShiLiShopApplication.wtoken);
                        RestClient.post(UrlUtils.insertErrLog(), errParams, OrderConfirmationActivity.this, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 根据预约ID获取数据
     */
    private void getAppointData() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", appointID);
        RestClient.post(UrlUtils.queryServiceAppointDetail(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("预约详情", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        String data = jsonObject.getString("Data");
                        BookingBean bookingBean = JSON.parseObject(data, BookingBean.class);
                        if (!bookingBean.StoreID.equals(CheShiLiShopApplication.storeID) && Status.equals("1")) {
                            Toast.makeText(OrderConfirmationActivity.this, "预约并不是当前店面，是你名下的店面", Toast.LENGTH_LONG);
                        }
                        JSONObject dataObject = new JSONObject(bookingBean.Data);
//                        DataBean dataBean = JSON.parseObject(bookingBean.Data, DataBean.class);
                        if (!TextUtils.isEmpty(dataObject.getString("ProductImg"))) {
                            Glide.with(context)
                                    .load(UrlUtils.BASE_URL + "/Img/" + dataObject.getString("ProductImg"))
                                    .into(mImgLogo);
                        }
                        mTvShopname.setText(dataObject.getString("ProductName"));
                        mTvMoney.setText("¥ " + dataObject.getString("AllMoney"));
//                         ToastUtils.show(OrderConfirmationActivity.this, dataObject.getString("ProductDescri"));
                        if (!"null".equals(dataObject.getString("ProductDescri"))) {
                            JSONObject descri = new JSONObject(dataObject.getString("ProductDescri"));
                            mTvStatus.setText(descri.getString("title"));
                        } else {
                            mTvStatus.setText("");
                        }
//                        if (!android.text.TextUtils.isEmpty(dataObject.getString("ProductDescri"))) {
//                            JSONObject descri = new JSONObject(dataObject.getString("ProductDescri"));
//                            mTvStatus.setText(descri.getString("title"));
//                        } else {
//                            mTvStatus.setText("");
//                        }
                        mTvOrdernum.setText("订单编号: " + dataObject.getString("OrderID"));
                        mTvMobile.setText(dataObject.getString("UserMobile"));
                        if (bookingBean.Status.equals("4")) {
                            mTvTimes.setText("已验证第 " + dataObject.getString("ServiceNum") + " 次服务");
                        } else {
                            mTvTimes.setText("预约第 " + dataObject.getString("ServiceNum") + " 次服务");
                        }
                        mTvName.setText(dataObject.getString("UserRealName"));
                        mTvNumber.setText("x1");
                        mTvNumber2.setText("共1件商品");
                        mTvOrdertime.setText(DateUtil.stampToDate(bookingBean.AddTime));
                        mTvBookingtime.setText(DateUtil.stampToDate3(bookingBean.AppointTimeS));
                        mTvAmount.setText("¥ " + dataObject.getString("AllMoney"));
                        mTvService.setText(bookingBean.ID);
                        mTvOffer.setText("¥ " + (Float.parseFloat(dataObject.getString("AllMoney")) - Float.parseFloat(dataObject.getString("OrderOutPocket"))));
                        mTvPreferential.setText("¥ " + dataObject.getString("OrderOutPocket"));

                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(OrderConfirmationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if ("94".equals(Status)) {
                        ToastUtils.show(OrderConfirmationActivity.this, "二维码已过期！");
                        finish();
                    } else if ("98".equals(Status)) {
                        ToastUtils.show(OrderConfirmationActivity.this, "这不是你预约的店铺！");
                        finish();
                    } else {
                        ToastUtils.show(OrderConfirmationActivity.this, "这不是你预约的店铺！");
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                errParams.add("LogCont", new String(responseBody));
                errParams.add("Url", UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, OrderConfirmationActivity.this, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        RequestParams errParams = new RequestParams();
                        errParams.add("LogCont", new String(responseBody));
                        errParams.add("Url", UrlUtils.queryServiceAppointDetail());
                        errParams.add("PostData", params.toString());
                        errParams.add("WToken", CheShiLiShopApplication.wtoken);
                        RestClient.post(UrlUtils.insertErrLog(), errParams, OrderConfirmationActivity.this, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 根据服务ID获取数据
     */
    public void getServiceData() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", serviceID);
        RestClient.post(UrlUtils.queryUserServiceDetail(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("服务详情", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        String data = jsonObject.getString("Data");
                        ServiceBean service = JSON.parseObject(data, ServiceBean.class);
                        mTvMobile.setText(service.UserMobile);
                        if (!TextUtils.isEmpty(service.ProductImg)) {
                            Glide.with(context)
                                    .load(UrlUtils.BASE_URL + "/Img/" + service.ProductImg)
                                    .into(mImgLogo);
                        }
                        mTvShopname.setText(service.ProductName);
                        mTvMoney.setText("¥ " + service.AllMoney);
                        if (!"null".equals(service.ProductDescri)) {
                            JSONObject descri = new JSONObject(service.ProductDescri);
                            mTvStatus.setText(descri.getString("title"));
                        } else {
                            mTvStatus.setText("");
                        }
                        mTvNumber.setText("x1");
                        mTvNumber2.setText("共1件商品");
                        mTvMobile.setText(service.UserMobile);
                        mTvOrdernum.setText("订单编号：" + service.OrderID);
                        mTvService.setText(service.ID);
                        mTvOrdertime.setText(DateUtil.stampToDate(service.AddTime));
                        mTvBookingtime.setVisibility(View.GONE);
                        mTvAmount.setText("¥ " + service.AllMoney);
                        mTvName.setText(service.UserRealName);
                        mTvTimes.setText("剩余服务次数:" + service.ServiceNum);
                        mTvOffer.setText("¥ " + (Float.parseFloat(service.AllMoney) - Float.parseFloat(service.OrderOutPocket)));
                        mTvPreferential.setText("¥ " + service.OrderOutPocket);
                    } else {
                        ToastUtils.show(OrderConfirmationActivity.this, "辨认失败");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                RequestParams errParams = new RequestParams();
                errParams.add("LogCont", new String(responseBody));
                errParams.add("Url", UrlUtils.queryUserServiceDetail());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, OrderConfirmationActivity.this, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
