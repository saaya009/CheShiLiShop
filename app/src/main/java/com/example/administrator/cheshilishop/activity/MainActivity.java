package com.example.administrator.cheshilishop.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.StoreBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yanzhenjie.permission.AndPermission;
import com.yzq.zxinglibrary.android.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.layout_scan)
    LinearLayout mLayoutScan;

    public static final int REQUEST_CODE_SCAN = 12;//二维码扫码成功
    @BindView(R.id.layout_userinfo)
    LinearLayout mLayoutUserinfo;
    @BindView(R.id.layout_order)
    LinearLayout mLayoutOrder;
    @BindView(R.id.layout_manager)
    LinearLayout mLayoutManager;
    @BindView(R.id.layout_booking)
    LinearLayout mLayoutBooking;
    @BindView(R.id.layout_commission)
    LinearLayout mLayoutCommission;
    @BindView(R.id.layout_change)
    LinearLayout mLayoutChange;
    @BindView(R.id.img_logo)
    CircleImageView mImgLogo;
    @BindView(R.id.tv_descri)
    TextView mTvDescri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /**
         * 动态权限申请
         */
        AndPermission.with(this)
                .requestCode(101)
                .permission(Manifest.permission.CAMERA)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
        getStore();
        initView();
    }

    /**
     * 获取默认店铺
     */
    private void getStore() {
        RequestParams params = new RequestParams();
        params.add("WToken", CheShiLiShopApplication.wtoken);
        RestClient.post(UrlUtils.queryDefaultStore(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("默认店铺", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)) {
                        String data = jsonObject.getString("Data");
                        CheShiLiShopApplication.store = JSON.parseObject(data, StoreBean.class);
                        CheShiLiShopApplication.storeID = CheShiLiShopApplication.store.ID;
                        Glide.with(MainActivity.this)
                                .load(CheShiLiShopApplication.store.Img)
                                .into(mImgLogo);
                        mTvDescri.setText(CheShiLiShopApplication.store.Name);
                    } else {
                        ToastUtils.show(MainActivity.this, jsonObject.getString("Data"));
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

    private void initView() {
        mLayoutScan.setOnClickListener(mOnClickListener);
        mLayoutBooking.setOnClickListener(mOnClickListener);
        mLayoutManager.setOnClickListener(mOnClickListener);
        mLayoutCommission.setOnClickListener(mOnClickListener);
        mLayoutUserinfo.setOnClickListener(mOnClickListener);
        mLayoutChange.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.layout_scan://扫一扫
                    intent = new Intent(MainActivity.this,
                            CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                    break;
                case R.id.layout_booking://预约管理
                    intent = new Intent(MainActivity.this,
                            BookingManagementActivity.class);
                    startActivity(intent);
                    break;
                case R.id.layout_manager://服务管理
                    intent = new Intent(MainActivity.this,
                            ProductActivity.class);
                    startActivity(intent);
                    break;
                case R.id.layout_userinfo://账户信息
                    intent = new Intent(MainActivity.this,
                            UserInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.layout_commission://佣金管理
                    intent = new Intent(MainActivity.this,
                            CommissionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.layout_change://更改店面
                    intent = new Intent(MainActivity.this,
                            SetStoreActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra("codedContent");
                Log.d("Qcode", content);
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getStore();
    }
}
