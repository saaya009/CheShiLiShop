package com.example.administrator.cheshilishop.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.dialog.LoadingDialog;
import com.example.administrator.cheshilishop.dialog.TwoButtonAndContentCustomDialog;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.hawk.Hawk;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yzq.zxinglibrary.android.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

/**
 * 登录
 * Created by Administrator on 2017/7/26.
 */

public class LoginActivity extends BaseActivity {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_getpwd;
    private Button btn_clear;
    private Button btn_clear2;

    private String mVal;
    private Intent mIntent;
    private LoadingDialog mDialog;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            mVal = data.getString("value");
            login();
        }
    };


    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        Hawk.init(LoginActivity.this).build();
        /**
         * 动态权限申请
         */
        AndPermission.with(LoginActivity.this)
                .requestCode(101)
                .permission(Manifest.permission.CAMERA
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .send();

    }

    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return new TopView(topbar_tv_title);
    }

    @Override
    protected void findViewById() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_getpwd = findViewById(R.id.tv_getpwd);
        btn_clear = findViewById(R.id.btn_clear);
        btn_clear2 = findViewById(R.id.btn_clear2);

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_username.getText().toString().trim().length() > 0) {
                    btn_clear.setVisibility(View.VISIBLE);
                } else {
                    btn_clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_password.getText().toString().trim().length() > 0) {
                    btn_clear2.setVisibility(View.VISIBLE);
                } else {
                    btn_clear2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void setListener() {
        btn_login.setOnClickListener(this);
        tv_getpwd.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {

        String wtoken = Hawk.get("wtoken", "");
        if (!"".equals(wtoken)) {
            CheShiLiShopApplication.wtoken = wtoken;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onClickEvent(View paramView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login://登录
                login();
                break;
            case R.id.img_back://返回
                setResult(9);
                finish();
                break;
            case R.id.tv_getpwd://找回密码
                mIntent = new Intent(LoginActivity.this, GetPwdActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_clear://清除手机号
                et_username.setText("");
                break;
            case R.id.btn_clear2://清除密码
                et_password.setText("");
                break;

        }

    }

    /**
     * 登录
     */
    private void login() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (username.length() != 11) {
            ToastUtils.show(LoginActivity.this, "请输入正确的手机号码");
            return;
        }
        if (password.length() == 0) {
            ToastUtils.show(LoginActivity.this, "密码不能为空");
            return;
        }
        mDialog = new LoadingDialog(this);
        mDialog.show();
        final RequestParams params = new RequestParams();
        params.add("Mobile", username);
        params.add("Password", password);
        params.add("Platform", "31");
        RestClient.post(UrlUtils.login(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    mDialog.dismiss();
                    String result = new String(responseBody);
                    JSONObject object = new JSONObject(result);
                    Log.d("登录", result);
                    int status = 0;
                    status = object.getInt("Status");
                    if (status == 0) {
                        ToastUtils.show(LoginActivity.this, "登录成功");
                        JSONObject data = object.getJSONObject("Data");
                        CheShiLiShopApplication.wtoken = data.getString("WToken");
                        Hawk.put("wtoken", CheShiLiShopApplication.wtoken);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(LoginActivity.this, "手机号或密码错误");
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
                RestClient.post(UrlUtils.insertErrLog(), errParams, LoginActivity.this, new AsyncHttpResponseHandler() {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TwoButtonAndContentCustomDialog dialog2 = new TwoButtonAndContentCustomDialog(
                    this, R.style.Translucent_NoTitle) {
                @Override
                public void doConfirm() {
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


}
