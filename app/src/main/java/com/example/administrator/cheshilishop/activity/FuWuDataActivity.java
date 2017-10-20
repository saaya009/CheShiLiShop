package com.example.administrator.cheshilishop.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

/**
 * 作者：Ayase
 * 邮箱：ayase@ayase.cn
 */
public class FuWuDataActivity extends BaseActivity {


    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.image_id)
    ImageView mImageId;
    @BindView(R.id.tv_ordernum)
    TextView mTvOrdernum;
    @BindView(R.id.tv_times)
    TextView mTvTimes;
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
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.tv_bookingtime)
    TextView mTvBookingtime;
    @BindView(R.id.tv_Amount)
    TextView mTvAmount;
    @BindView(R.id.tv_Offer)
    TextView mTvOffer;
    @BindView(R.id.tv_preferential)
    TextView mTvPreferential;
    private String appointID;
    private String serviceID;
    private String confirmCode;
    private String type;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fuwudata);
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

    }

    @Override
    protected void processLogic() {
        setTopTitle("服务详情");
        serviceID = getIntent().getStringExtra("ServiceID");
        confirmCode = getIntent().getStringExtra("ConfirmCode");
        getServiceData();
    }

    @Override
    protected void onClickEvent(View paramView) {

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
                        mTvMoney.setText("¥ "+service.AllMoney);
                        if (!"null".equals(service.ProductDescri)) {
                            JSONObject descri = new JSONObject(service.ProductDescri);
                            mTvStatus.setText(descri.getString("title"));
                        } else {
                            mTvStatus.setText("");
                        }
                        mTvNumber.setText("x1");
                        mTvNumber2.setText("共1件商品");
                        mTvMobile.setText(service.UserMobile);
                        mTvOrdernum.setText("订单编号："+service.OrderID);
                        mTvService.setText(service.ID);
                        mTvOrdertime.setText(DateUtil.stampToDate(service.AddTime));
                        mTvBookingtime.setVisibility(View.GONE);
                        mTvAmount.setText(service.AllMoney);
                        mTvName.setText(service.UserRealName);
                        mTvTimes.setText("已完成第"+service.ServiceNum+"次服务");
                        mTvOffer.setText("¥ "+(Float.parseFloat(service.AllMoney) - Float.parseFloat(service.OrderOutPocket)));
                        mTvPreferential.setText("¥ "+service.OrderOutPocket);
                    } else {
                        ToastUtils.show(FuWuDataActivity.this, "辨认失败");
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
                RestClient.post(UrlUtils.insertErrLog(), errParams, FuWuDataActivity.this, new AsyncHttpResponseHandler() {
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
}
