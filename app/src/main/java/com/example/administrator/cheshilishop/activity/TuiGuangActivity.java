package com.example.administrator.cheshilishop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.utils.InviteUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 推广页面
 * 作者：Ayase on 2017/9/5 13:58
 * 邮箱：ayase@ayase.cn
 */

public class TuiGuangActivity extends BaseActivity {
    @BindView(R.id.image_btn)
    ImageView mImageBtn;
    @BindView(R.id.image_btn2)
    ImageView mImageBtn2;
    @BindView(R.id.image_code)
    ImageView mImageCode;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tuiguang);
        ButterKnife.bind(this);
    }

    @Override
    protected BaseActivity getAct() {
        return null;
    }

    @Override
    protected TopView getTopViews() {
        return null;
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        setTopTitle("推广有奖");
        String path = UrlUtils.BASE_URL
                +"/wechat/www/web/main/index.html?R=home/wechatLogin.html%3FRegisterFrom%3D4%26RegisterCont%3D"
                + InviteUtils.getInviteCode(CheShiLiShopApplication.storeID);
    }

    @Override
    protected void onClickEvent(View paramView) {

    }

}
