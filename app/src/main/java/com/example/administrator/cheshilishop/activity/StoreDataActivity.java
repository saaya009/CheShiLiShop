package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.bean.StoreBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.DateUtil;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 作者：Ayase on 2017/9/1 14:10
 * 邮箱：ayase@ayase.cn
 */

public class StoreDataActivity extends BaseActivity {
    @BindView(R.id.tv_shopname)
    TextView mTvShopname;
    @BindView(R.id.et_shopname)
    TextView mEtShopname;
    @BindView(R.id.et_shortName)
    TextView mEtShortName;
    @BindView(R.id.tv_people)
    TextView mTvPeople;
    @BindView(R.id.et_chargeMan)
    TextView mEtChargeMan;
    @BindView(R.id.et_chargeTitle)
    TextView mEtChargeTitle;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.et_mobile)
    TextView mEtMobile;
    @BindView(R.id.et_tel)
    TextView mEtTel;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_expired)
    TextView mTvExpired;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.et_type)
    TextView mEtType;
    @BindView(R.id.tv_WDeviceType)
    TextView mTvWDeviceType;
    @BindView(R.id.et_wdeviceType)
    TextView mEtWdeviceType;
    @BindView(R.id.et_wdeviceNum)
    TextView mEtWdeviceNum;
    @BindView(R.id.et_wpostions)
    TextView mEtWpostions;
    @BindView(R.id.tv_city1)
    TextView mTvCity1;
    @BindView(R.id.tv_city)
    TextView mTvCity;
    @BindView(R.id.layout_city)
    RelativeLayout mLayoutCity;
    @BindView(R.id.et_adress)
    TextView mEtAdress;
    @BindView(R.id.et_Longitude)
    TextView mEtLongitude;
    @BindView(R.id.et_Latitude)
    TextView mEtLatitude;
    @BindView(R.id.et_descri)
    TextView mEtDescri;
    @BindView(R.id.img1)
    ImageView mImg1;
    @BindView(R.id.img_close1)
    ImageView mImgClose1;
    @BindView(R.id.layout_img1)
    RelativeLayout mLayoutImg1;
    @BindView(R.id.img2)
    ImageView mImg2;
    @BindView(R.id.img_close2)
    ImageView mImgClose2;
    @BindView(R.id.layout_img2)
    RelativeLayout mLayoutImg2;
    @BindView(R.id.layout_main)
    LinearLayout mLayoutMain;
    @BindView(R.id.tv_bankname)
    TextView mTvBankname;
    @BindView(R.id.tv_banknum)
    TextView mTvBanknum;
    @BindView(R.id.tv_bank)
    TextView mTvBank;
    private StoreBean mStore;
    private String mId;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_storedata);
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
        setTopTitle("查看店面信息");
        mId = getIntent().getStringExtra("id");
        Log.d("mId2", mId);
        getStore();
    }

    @Override
    protected void onClickEvent(View paramView) {

    }


    /**
     * 获取店铺信息
     */
    private void getStore() {
        final RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        params.add("ID", mId);
        RestClient.post(UrlUtils.queryStoreDetail(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("获取店铺", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        String data = jsonObject.getString("Data");
                        mStore = JSON.parseObject(data, StoreBean.class);
                        loadData();
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(StoreDataActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(StoreDataActivity.this, jsonObject.getString("Data"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                RequestParams errParams = new RequestParams();
                try {
                    errParams.add("LogCont", URLEncoder.encode(new String(responseBody), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                errParams.add("Url", UrlUtils.queryServiceAppointDetail());
                errParams.add("PostData", params.toString());
                errParams.add("WToken", CheShiLiShopApplication.wtoken);
                RestClient.post(UrlUtils.insertErrLog(), errParams, StoreDataActivity.this, new AsyncHttpResponseHandler() {
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
     * 加载数据
     */
    private void loadData() {
        mEtShopname.setText(mStore.Name);
        mEtShortName.setText(mStore.SortName);
        mEtChargeTitle.setText(mStore.ChargeTitle);
        mEtChargeMan.setText(mStore.ChargeMan);
        mEtMobile.setText(mStore.Phone);
        mEtTel.setText(mStore.Tel);
        mTvExpired.setText(DateUtil.stampToDate2(mStore.Expired));
        switch (mStore.Type) {
            case "0":
                mEtType.setText("一站式");
                break;
            case "1":
                mEtType.setText("汽修厂");
                break;
            case "2":
                mEtType.setText("美容店");
                break;
            case "3":
                mEtType.setText("轮胎");
                break;
        }
        switch (mStore.WDeviceType) {
            case "0":
                mEtWdeviceType.setText("手工");
                break;
            case "1":
                mEtWdeviceType.setText("机器");
                break;
        }
        mEtWdeviceNum.setText(mStore.WDeviceNum);
        mEtWpostions.setText(mStore.WPostions);
        mEtAdress.setText(mStore.Address);
        if (Double.parseDouble(mStore.Latitude) != 0){
            mEtLatitude.setText(mStore.Latitude);
        }
        if (Double.parseDouble(mStore.Longitude) != 0){
            mEtLongitude.setText(mStore.Longitude);
        }
        if (!mStore.Descri.equals("[]")) {
            try {
                JSONObject descri = new JSONObject(mStore.Descri);
                mEtDescri.setText(descri.getString("text1"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Glide.with(this).load(mStore.Img).into(mImg1);
        Glide.with(this).load(mStore.CerImg).into(mImg1);
        mTvBankname.setText(mStore.BankName);
        mTvBanknum.setText(mStore.BankNo);
        mTvBank.setText(mStore.BankCompany);
        mTvCity.setText(mStore.AddressNames);
    }
}
