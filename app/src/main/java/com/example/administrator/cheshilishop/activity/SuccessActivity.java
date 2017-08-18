package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Ayase on 2017/8/16 14:58
 * 邮箱：ayase@ayase.cn
 */

public class SuccessActivity extends BaseActivity {


    @BindView(R.id.btn_home)
    Button mBtnHome;
    @BindView(R.id.btn_order)
    Button mBtnOrder;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_success);
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
        mBtnHome.setOnClickListener(this);
        mBtnOrder.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("确认成功");
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()){
            case R.id.btn_home://去首页
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_order://去订单
                intent = new Intent(this,BookingManagementActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
