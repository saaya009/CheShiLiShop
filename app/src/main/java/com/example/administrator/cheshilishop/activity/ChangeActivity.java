package com.example.administrator.cheshilishop.activity;

import android.os.Bundle;
import android.view.View;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;

/**
 * 编辑服务
 * 作者：Ayase on 2017/9/8 15:03
 * 邮箱：ayase@ayase.cn
 */

public class ChangeActivity extends BaseActivity {


    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change);
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

    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void onClickEvent(View paramView) {

    }
}
