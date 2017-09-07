package com.example.administrator.cheshilishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.adapter.BookingAdapter;
import com.example.administrator.cheshilishop.fragment.BookingAllFragment;
import com.example.administrator.cheshilishop.fragment.BookingEndFragment;
import com.example.administrator.cheshilishop.fragment.BookingVerifiedFragment;
import com.example.administrator.cheshilishop.fragment.OrderOneFragment;
import com.example.administrator.cheshilishop.fragment.OrderThreeFragment;
import com.example.administrator.cheshilishop.fragment.OrderTwoFragment;
import com.example.administrator.cheshilishop.widget.TopbarIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 三级分销
 * Created by Administrator on 2017/8/23.
 */
public class OrderManagementActivity extends BaseActivity {

    private TopbarIndicator ti_indicator;
    private ViewPager vp_financial;
    private List<String> type = Arrays.asList("一级推广", "二级推广", "三级推广");
    private BookingAdapter adapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private OrderOneFragment orderOneFragment;
    private OrderTwoFragment mOrderTwoFragment;
    private OrderThreeFragment mOrderThreeFragment;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_servicemanagement);
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
        ti_indicator = (TopbarIndicator) findViewById(R.id.ti_indicator);
        vp_financial = (ViewPager) findViewById(R.id.vp_financial);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        topbar_tv_title.setText("推广清单");
        topbar_iv_right.setText("推广树形图");

        orderOneFragment = new OrderOneFragment();
        mOrderTwoFragment = new OrderTwoFragment();
        mOrderThreeFragment = new OrderThreeFragment();
        mFragmentList.add(orderOneFragment);
        mFragmentList.add(mOrderTwoFragment);
        mFragmentList.add(mOrderThreeFragment);

        adapter = new BookingAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp_financial.setAdapter(adapter);
        vp_financial.setCurrentItem(0);

        ti_indicator.setVisibleTabCount(3);
        ti_indicator.setTitles(type);
        ti_indicator.setViewpager(vp_financial);
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.topbar_iv_right://树形图
                Intent intent = new Intent(this,OrderTreeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
