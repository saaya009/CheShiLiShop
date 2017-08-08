package com.example.administrator.cheshilishop.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.BookingBean;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.DateUtil;
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
import cz.msebera.android.httpclient.util.TextUtils;

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
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if ("0".equals(Status)){
                        String data = jsonObject.getString("Data");

                    }else {
                        ToastUtils.show(MainActivity.this,jsonObject.getString("Data"));
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
}
