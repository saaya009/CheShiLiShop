package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.net.RestClient;
import com.example.administrator.cheshilishop.utils.ToastUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 修改姓名
 * 作者：Ayase on 2017/10/27 17:23
 * 邮箱：ayase@ayase.cn
 */

public class SetNameActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText mEtName;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setname);
        ButterKnife.bind(this);
    }

    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return new TopView(topbar_iv_back, topbar_tv_title, topbar_iv_right);
    }

    @Override
    protected void findViewById() {
        topbar_iv_right.setOnClickListener(this);
        topbar_iv_right.setVisibility(View.VISIBLE);
        Resources resource = this.getResources();
        ColorStateList csl = resource.getColorStateList(R.color.blue);
        topbar_iv_right.setTextColor(csl);
        topbar_iv_right.setText("确认");
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTopTitle("修改姓名");
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()){
            case R.id.topbar_iv_right:
                String name = mEtName.getText().toString().trim();
                if (name.length()==0){
                    ToastUtils.show(this,"姓名不能为空");
                    return;
                }
                RequestParams params = new RequestParams();
                params.add("WToken", CheShiLiShopApplication.wtoken);
                params.add("RealName",name);
                RestClient.post(UrlUtils.updateNickName(), params, context, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String result = new String(responseBody);
                        try {
                            Log.d("更改姓名", result);
                            JSONObject json = new JSONObject(result);
                            String Status = json.getString("Status");
                            if ("0".equals(Status)) {
                                ToastUtils.show(SetNameActivity.this, "修改成功");
                                finish();
                            } else if ("-1".equals(Status)) {
                                Intent intent = new Intent(SetNameActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.show(SetNameActivity.this, json.getString("Data"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                break;
        }
    }


}
