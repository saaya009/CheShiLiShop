package com.example.administrator.cheshilishop.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.StoreBean;
import com.example.administrator.cheshilishop.bean.UserInfoBean;
import com.example.administrator.cheshilishop.dialog.TwoButtonAndContentCustomDialog;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yzq.zxinglibrary.android.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;

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
                                .load(UrlUtils.BASE_URL + "/Img/" + CheShiLiShopApplication.store.Img)
                                .into(mImgLogo);
                        mTvDescri.setText(CheShiLiShopApplication.store.Name);
                    } else if ("-1".equals(Status)) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
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
                String content = URLDecoder.decode(data.getStringExtra("codedContent"));
                Log.d("Qcode", content);
                int index1 = content.indexOf("AppointID");
                int index2 = content.indexOf("ServiceID");
                int index3 = content.indexOf("ConfirmCode");
                int index4 = content.indexOf("&Type");
                String AppointID = content.substring(index1 + 10);
                String ConfirmCode = content.substring(index3 + 12, index4);
                String ServiceID = content.substring(index2 + 10, index1 - 1);
                Intent intent = new Intent(MainActivity.this, OrderConfirmationActivity.class);
                intent.putExtra("AppointID", AppointID);
                intent.putExtra("ConfirmCode", ConfirmCode);
                intent.putExtra("ServiceID", ServiceID);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        }
    }

    /**
     * 更改预约状态
     */
    private void confirmService(String content) {
        int index1 = content.indexOf("AppointID");
        int index2 = content.indexOf("ServiceID");
        int index3 = content.indexOf("ConfirmCode");
        int index4 = content.indexOf("&Type");
        String AppointID = content.substring(index1 + 10);
        String ConfirmCode = content.substring(index3 + 12, index4);
        String ServiceID = content.substring(index2 + 10, index1 - 1);
        if (TextUtils.isEmpty(AppointID)) {
            RequestParams params = new RequestParams();
            params.add("ID", AppointID);
            params.add("WToken", CheShiLiShopApplication.wtoken);
            RestClient.post(UrlUtils.queryServiceAppointDetail(), params, MainActivity.this, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String Status = jsonObject.getString("Status");
                        if ("0".equals(Status)) {

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
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getStore();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TwoButtonAndContentCustomDialog dialog2 = new TwoButtonAndContentCustomDialog(
                    this, R.style.Translucent_NoTitle) {
                @Override
                public void doConfirm() {
                    // TODO Auto-generated method stub
                    super.doConfirm();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            };
            dialog2.show();
            dialog2.setContent("确定退出？");
            dialog2.setTitle("退出");
            dialog2.setCancel("取消");
            dialog2.setConfirm("确认");
        }
        return true;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        // 只需要调用这一句，剩下的AndPermission自动完成。
//        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(101)
    private void getLocationYes() {
        // 申请权限成功，可以去做点什么了。
        Intent intent = new Intent(MainActivity.this,
                CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(101)
    private void getLocationNo() {
        // 申请权限失败，可以提醒一下用户。
        Toast.makeText(this, "获取相机权限失败，无法使用扫一扫", Toast.LENGTH_SHORT).show();
        finish();
    }
}
