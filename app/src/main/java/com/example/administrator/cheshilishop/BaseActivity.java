package com.example.administrator.cheshilishop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
    protected Context context;
    public static BaseActivity mCurrentAct;
    protected RelativeLayout mTopBar;
    protected ImageView topbar_iv_back;
    protected TextView topbar_tv_title;
    protected TextView topbar_iv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initView(savedInstanceState);

    }
    private void initView(Bundle savedInstanceState) {
        mCurrentAct = getAct();
        context = mCurrentAct;
        loadViewLayout(savedInstanceState);
        findTopView(mCurrentAct);// 查找并设置头栏控件
        showTopView(getTopViews());
        findViewById();
        setListener();
        processLogic();

    }


    /** 查找并设置头栏控件 */
    private void findTopView(final BaseActivity subAct) {
        mTopBar = (RelativeLayout) subAct.findViewById(R.id.top_view);
        if (null == mTopBar) {
            return;
        }
        topbar_iv_back = (ImageView) subAct.findViewById(R.id.topbar_iv_back);
        topbar_tv_title = (TextView) subAct.findViewById(R.id.topbar_tv_title);
        topbar_iv_right = (TextView) subAct.findViewById(R.id.topbar_iv_right);
        topbar_iv_back.setOnClickListener(subAct);
        topbar_iv_right.setOnClickListener(subAct);
    }

    /**
     * 显示topbar中所需要的view
     */
    protected void showTopView(TopView topView) {
        // 隐藏所有按钮
        if (null != topbar_iv_back) {
            topbar_iv_back.setVisibility(View.GONE);
        }
        if(null != topbar_iv_right){
            topbar_iv_right.setVisibility(View.GONE);
        }
        if(null != topbar_tv_title){
            topbar_tv_title.setVisibility(View.GONE);
        }


        if (null != topView && null != topView.topViews && topView.topViews.size() > 0) {
            // 显示指定按钮
            for (View view : topView.topViews) {
                if (null != view) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setTopTitle(String title) {
        if (null != topbar_tv_title && null != title) {
            topbar_tv_title.setText(title + "");
        }
    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_start_in_right_left, R.anim.anim_start_out_right_left);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_finish_in_left_right, R.anim.anim_finish_out_left_right);
    }
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        overridePendingTransition(R.anim.anim_start_in_right_left, R.anim.anim_start_out_right_left);
    }

    @Override
    public void onClick(View paramView) {
            if (null != topbar_iv_back && paramView.getId() == R.id.topbar_iv_back) {
                mCurrentAct.finish();
            }else{
                onClickEvent(paramView);
            }
    }


    protected abstract void loadViewLayout(Bundle savedInstanceState);
    // 需要在此返回当前的Activity
    protected abstract BaseActivity getAct();
    // 显示顶部导航栏
    protected abstract TopView getTopViews();

    protected abstract void findViewById();

    protected abstract void setListener();

    protected abstract void processLogic();

    protected abstract void onClickEvent(View paramView);

    @Override
    public void onLowMemory() {
        Runtime.getRuntime().gc();
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentAct = getAct();
        context = mCurrentAct;

    }

}
