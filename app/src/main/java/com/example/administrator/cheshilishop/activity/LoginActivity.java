package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.HttpUtils;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 登录
 * Created by Administrator on 2017/7/26.
 */

public class LoginActivity extends BaseActivity {

    private ImageView img_back;
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_getpwd;
    private Button btn_clear;
    private Button btn_clear2;

    private String mVal;
    private Intent mIntent;

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
    }

    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return null;
    }

    @Override
    protected void findViewById() {
        img_back = findViewById(R.id.img_back);
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
        img_back.setOnClickListener(this);
        tv_getpwd.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("登录");
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
                mIntent = new Intent(LoginActivity.this,GetPwdActivity.class);
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
        RequestParams params = new RequestParams();
        params.add("Mobile", username);
        params.add("Password", password);
        params.add("Platform", "31");
        RestClient.post(UrlUtils.login(), params, this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject object = new JSONObject(result);
                    Log.d("登录", result);
                    int status = 0;
                    status = object.getInt("Status");
                    if (status == 0) {
                        ToastUtils.show(LoginActivity.this, "登录成功");
                        JSONObject data = object.getJSONObject("Data");
                        CheShiLiShopApplication.wtoken = data.getString("WToken");
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtils.show(LoginActivity.this, "手机号或密码错误");
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
