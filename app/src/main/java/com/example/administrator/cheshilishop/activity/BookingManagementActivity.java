package com.example.administrator.cheshilishop.activity;

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
import com.example.administrator.cheshilishop.widget.TopbarIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 预约管理
 * Created by Administrator on 2017/8/2.
 */
public class BookingManagementActivity extends BaseActivity{

    private TopbarIndicator ti_indicator;
    private ViewPager vp_financial;
    private List<String> type = Arrays.asList("全部","已预约","已验证");
    private BookingAdapter adapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private BookingAllFragment aFragment;
    private BookingEndFragment eFragment;
    private BookingVerifiedFragment vFragment;

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
       return new TopView(topbar_iv_back, topbar_tv_title);
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
        topbar_tv_title.setText("预约管理");

        aFragment = new BookingAllFragment();
        eFragment = new BookingEndFragment();
        vFragment = new BookingVerifiedFragment();
        mFragmentList.add(aFragment);
        mFragmentList.add(eFragment);
        mFragmentList.add(vFragment);

        adapter = new BookingAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp_financial.setAdapter(adapter);
        vp_financial.setCurrentItem(0);

        ti_indicator.setVisibleTabCount(3);
        ti_indicator.setTitles(type);
        ti_indicator.setViewpager(vp_financial);
    }

    @Override
    protected void onClickEvent(View paramView) {

    }
}
