package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.dialog.LoadingDialog;
import com.example.administrator.cheshilishop.dialog.TwoButtonAndContentCustomDialog;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.HttpUtils;
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
 * 修改密码
 * Created by Administrator on 2017/7/26.
 */
public class GetPwdActivity extends BaseActivity {

    private ImageView img_back;
    private EditText et_username;
    private EditText et_password;
    private EditText et_yzm;
    private Button btn_login;
    private Button btn_clear;
    private Button btn_code;

    private String mVal;

    private LoadingDialog mDialog;
    private Countdown mCountdown;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int num = msg.what;
            switch (num){
                case 1:
                    mVal = data.getString("value");
                    mDialog = new LoadingDialog(GetPwdActivity.this);
                    mDialog.show();
                    change();
                    break;
                case 2:
                    mVal = data.getString("value");
                    getCode();
                    break;
            }


        }
    };


    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_getpwd);
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

        btn_clear = findViewById(R.id.btn_clear);
        et_yzm = findViewById(R.id.et_yzm);
        btn_code = findViewById(R.id.btn_code);

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


    }

    @Override
    protected void setListener() {
        btn_login.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_code.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
    }

    @Override
    protected void onClickEvent(View paramView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login://修改密码
                new Thread(networkTask).start();
                break;
            case R.id.btn_code://获取验证码
                String username = et_username.getText().toString().trim();
                if (username.length() != 11) {
                    ToastUtils.show(GetPwdActivity.this, "请输入正确的手机号码");
                    return;
                }
                new Thread(getCode).start();
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
     * 修改密码
     */
    private void change() {
        try {
            JSONObject object = new JSONObject(mVal);
            Log.d("注册", mVal);
            int status = 0;
            status = object.getInt("Status");
            if (status == 0) {
                ToastUtils.show(GetPwdActivity.this, "密码修改成功");
                login();
            } else {
                ToastUtils.show(GetPwdActivity.this, "手机号或密码错误或者验证码错误");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *修改密码
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            String username = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            String code = et_yzm.getText().toString().trim();
            if (username.length() != 11) {
                ToastUtils.show(GetPwdActivity.this, "请输入正确的手机号码");
                return;
            }
            if (password.length() == 0) {
                ToastUtils.show(GetPwdActivity.this, "密码不能为空");
                return;
            }
            if (code.length() == 0) {
                ToastUtils.show(GetPwdActivity.this, "验证码不能为空");
                return;
            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("Mobile", username);
            map.put("NewPwd", password);
            map.put("VC", code);
            Log.d("返回", username+"=="+password+"==="+code);
            String json = JSON.toJSONString(map);
            String result = HttpUtils.loginByPost(UrlUtils.getPwd(), json);
            Log.d("返回", result);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", result);
            msg.setData(data);
            msg.what  = 1;
            handler.sendMessage(msg);
        }
    };

    /**
     * 获取验证码
     */
    Runnable getCode = new Runnable() {
        @Override
        public void run() {

            String username = et_username.getText().toString().trim();
            if (username.length() != 11) {
                ToastUtils.show(GetPwdActivity.this, "请输入正确的手机号码");
                return;
            }
            Map<String,String> map = new HashMap<String, String>();
            map.put("Mobile",username);
            map.put("Type","1");
            map.put("PType","0");
            String json = JSON.toJSONString(map);
            String result = HttpUtils.loginByPost(UrlUtils.getCode(), json);
            Log.d("返回", result);
            Message msg = new Message();
            msg.what  = 2;
            Bundle data = new Bundle();
            data.putString("value", result);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    /**
     * 验证码
     */
    private void getCode(){
        JSONObject object = null;
        try {
            object = new JSONObject(mVal);
            Log.d("验证码", mVal);
            int status = 0;
            status = object.getInt("Status");
            if (status == 0) {
                mCountdown = new Countdown(btn_code, et_username, 60000, 1000);
                mCountdown.start();
                ToastUtils.show(GetPwdActivity.this, "发送成功");
            }else {
                ToastUtils.show(GetPwdActivity.this, "发送失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 登录
     */
    private void login() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (username.length() != 11) {
            ToastUtils.show(GetPwdActivity.this, "请输入正确的手机号码");
            return;
        }
        if (password.length() == 0) {
            ToastUtils.show(GetPwdActivity.this, "密码不能为空");
            return;
        }

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
                        ToastUtils.show(GetPwdActivity.this, "登录成功");
                        JSONObject data = object.getJSONObject("Data");
                        CheShiLiShopApplication.wtoken = data.getString("WToken");
                        Hawk.put("wtoken", CheShiLiShopApplication.wtoken);
                        Intent intent = new Intent(GetPwdActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.show(GetPwdActivity.this, "手机号或密码错误");
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
                RestClient.post(UrlUtils.insertErrLog(), errParams, GetPwdActivity.this, new AsyncHttpResponseHandler() {
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
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
