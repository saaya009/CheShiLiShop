package com.example.administrator.cheshilishop.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.BookingAllAdapter;
import com.example.administrator.cheshilishop.bean.BookingBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.DateUtil;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

/**
 * 作者：Ayase
 * 邮箱：ayase@ayase.cn
 */
public class OrderConfirmationActivity extends BaseActivity {


    @BindView(R.id.tv_tel)
    TextView mTvTel;
    @BindView(R.id.img_logo)
    ImageView mImgLogo;
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
    @BindView(R.id.tv_order)
    TextView mTvOrder;
    @BindView(R.id.tv_odertime)
    TextView mTvOdertime;
    @BindView(R.id.tv_bookingtime)
    TextView mTvBookingtime;
    @BindView(R.id.tv_Amount)
    TextView mTvAmount;
    @BindView(R.id.tv_Offer)
    TextView mTvOffer;
    @BindView(R.id.tv_preferential)
    TextView mTvPreferential;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private String id;

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

    }

    @Override
    protected void processLogic() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void onClickEvent(View paramView) {

    }

    /**
     * 获取用户数据
     */
    private void getData() {
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID",id);
        RestClient.post(UrlUtils.queryServiceAppointList(), params,this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)){
                        String data = jsonObject.getString("Data");
                        BookingBean bookingBean = JSON.parseObject(data,BookingBean.class);
                        mTvTel.setText(bookingBean.UserMobile);
                        if (!TextUtils.isEmpty(bookingBean.Imgs)){
                            Glide.with(context)
                                    .load(bookingBean.Imgs)
                                    .into(mImgLogo);
                        }
                        mTvShopname.setText(bookingBean.StoreName);
                        mTvMoney.setText(bookingBean.Price);
                        mTvStatus.setText(bookingBean.Descri);
                        mTvNumber.setText("x1");
                        mTvNumber2.setText("共1件商品");
                        mTvOrder.setText(bookingBean.OrderID);
                        mTvOdertime.setText(DateUtil.stampToDate(bookingBean.AddTime));
                        mTvBookingtime.setText(DateUtil.stampToDate3(bookingBean.AppointDate));
                        mTvAmount.setText(bookingBean.Price);
                        mTvOffer.setText(bookingBean.ServiceGapPrice);
                        mTvPreferential.setText(Double.parseDouble(bookingBean.Price)-Double.parseDouble(bookingBean.ServiceGapPrice)+"");
                    }else {
                        ToastUtils.show(OrderConfirmationActivity.this,jsonObject.getString("Data"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


            }
        });
    }

}
