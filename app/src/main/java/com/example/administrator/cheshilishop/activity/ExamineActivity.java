package com.example.administrator.cheshilishop.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Ayase on 2017/9/1 15:06
 * 邮箱：ayase@ayase.cn
 */

public class ExamineActivity extends BaseActivity {
    @BindView(R.id.btn_main)
    Button mBtnMain;
    @BindView(R.id.btn_date)
    Button mBtnDate;
    private String mId;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_examine);
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
        mBtnMain.setOnClickListener(this);
        mBtnDate.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("申请添加店面");
        mId = getIntent().getStringExtra("id");

    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()){
            case R.id.btn_main://去首页
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_date://查看资料
                intent = new Intent(this,StoreDataActivity.class);
                intent.putExtra("id",mId);
                Log.d("mId1", mId);
                startActivity(intent);
                break;
        }
    }

}
