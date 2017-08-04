package com.example.administrator.cheshilishop.activity;

import android.os.Bundle;
import android.view.View;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;

/**
 * Created by Administrator on 2017/7/31.
 */

public class ScanActivity extends BaseActivity {
    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
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
