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
 * 作者：Ayase on 2017/8/28 16:46
 * 邮箱：ayase@ayase.cn
 */

public class AuthenticationActivity extends BaseActivity {


    @BindView(R.id.btn_next)
    Button mBtnNext;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_authentication);
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
        mBtnNext.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("申请添加店面");
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()){
            case R.id.btn_next:
                Intent intent = new Intent(this,WriteActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


}
