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
import com.example.administrator.cheshilishop.utils.PreferenceUtil;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.example.administrator.cheshilishop.widget.Countdown;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 注册
 * Created by Administrator on 2017/7/26.
 */

public class RegActivity extends BaseActivity {

    private ImageView img_back;
    private EditText et_username;
    private EditText et_password;
    private EditText et_yzm;
    private Button btn_login;
    private TextView tv_reg;
    private TextView tv_getpwd;
    private Button btn_clear;
    private Button btn_clear2;
    private Button btn_yzm;
    private EditText et_invitation;

    private String page;
    private String mVal;

    private Countdown mCountdown;



    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reg);
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
        et_yzm = findViewById(R.id.et_yzm);
        btn_yzm = findViewById(R.id.btn_yzm);
        et_invitation = findViewById(R.id.et_invitation);

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
        btn_clear.setOnClickListener(this);
        btn_clear2.setOnClickListener(this);
        btn_yzm.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        page = getIntent().getStringExtra("page");
    }

    @Override
    protected void onClickEvent(View paramView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login://注册
                reg();
                break;
            case R.id.btn_yzm://获取验证码
                String username = et_username.getText().toString().trim();
                if (username.length() != 11) {
                    ToastUtils.show(RegActivity.this, "请输入正确的手机号码");
                    return;
                }
                getCode();
                break;
            case R.id.img_back://返回
                finish();
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
     * 注册
     */
    private void reg() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String code = et_yzm.getText().toString().trim();
        String invitation = et_invitation.getText().toString().trim();
        if (username.length() != 11) {
            ToastUtils.show(RegActivity.this, "请输入正确的手机号码");
            return;
        }
        if (password.length() == 0) {
            ToastUtils.show(RegActivity.this, "密码不能为空");
            return;
        }
        if (code.length() == 0) {
            ToastUtils.show(RegActivity.this, "验证码不能为空");
            return;
        }
        RequestParams params = new RequestParams();
        params.add("Mobile", username);
        params.add("Password", password);
        params.add("Platform", "31");
        params.add("VC", code);
        if (invitation.length() > 0) {
            params.add("RegisterFrom", "1");
            params.add("RegisterCont",invitation);
        }else {
            params.add("RegisterFrom", "0");
        }
        RestClient.post(UrlUtils.registerUser(), params, RegActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("注册", result);
                    JSONObject object = new JSONObject(result);
                    int status = 0;
                    status = object.getInt("Status");
                    if (status == 0) {
                        ToastUtils.show(RegActivity.this, "注册成功");
                        JSONObject data = object.getJSONObject("Data");
                        CheShiLiShopApplication.wtoken = data.getString("WToken");
                        PreferenceUtil.putString("wtoken",CheShiLiShopApplication.wtoken);
                        Hawk.put("wtoken", CheShiLiShopApplication.wtoken);
                        Intent intent = new Intent(RegActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else if (status == -3){
                        ToastUtils.show(RegActivity.this, "验证码错误");
                    } else if (status == 99){
                        ToastUtils.show(RegActivity.this, "该手机号已注册");
                    } else if (status == -11){
                        ToastUtils.show(RegActivity.this, "验证码不可用");
                    }
                    else {
                        ToastUtils.show(RegActivity.this, "手机号或密码错误或者验证码错误");
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

    /**
     * 验证码
     */
    private void getCode(){
        String username = et_username.getText().toString().trim();
        if (username.length() != 11) {
            ToastUtils.show(RegActivity.this, "请输入正确的手机号码");
            return;
        }
        RequestParams params = new RequestParams();

        params.add("Mobile",username);
        params.add("Type","0");
        params.add("PType","1");
        RestClient.post(UrlUtils.getCode(), params, RegActivity.this, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                    Log.d("验证码", result);
                    int status = 0;
                    status = object.getInt("Status");
                    if (status == 0) {
                        mCountdown = new Countdown(btn_yzm, et_username, 60000, 1000);
                        mCountdown.start();
                        ToastUtils.show(RegActivity.this, "发送成功");
                    }else {
                        ToastUtils.show(RegActivity.this, "发送失败");
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
