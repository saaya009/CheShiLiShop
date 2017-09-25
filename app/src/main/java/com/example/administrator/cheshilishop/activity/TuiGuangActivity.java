package com.example.administrator.cheshilishop.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.cheshilishop.BaseActivity;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.TopView;
import com.example.administrator.cheshilishop.utils.FileUtils;
import com.example.administrator.cheshilishop.utils.InviteUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;
import com.mob.MobSDK;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
    private String mPath;

    @Override
    protected void loadViewLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tuiguang);
        ButterKnife.bind(this);
    }

    @Override
    protected BaseActivity getAct() {
        return this;
    }

    @Override
    protected TopView getTopViews() {
        return new TopView(topbar_tv_title,topbar_iv_back);
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void setListener() {
        mImageBtn.setOnClickListener(this);
        mImageBtn2.setOnClickListener(this);
        mImageCode.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        setTopTitle("推广有奖");
        mPath = UrlUtils.BASE_URL
                + "/wechat/www/web/main/index.html?R=home/wechatLogin.html%3FRegisterFrom%3D4%26RegisterCont%3D"
                + InviteUtils.getInviteCode(CheShiLiShopApplication.storeID);
        Bitmap mBitmap = CodeUtils.createImage(mPath, 400, 400, null);
        mImageCode.setImageBitmap(mBitmap);
    }

    @Override
    protected void onClickEvent(View paramView) {
        switch (paramView.getId()) {
            case R.id.image_btn://
                MobSDK.init(this,"20f356f80a794","a4aecdd80a872f2d6e0d68a8711df702");
                share();
                break;
            case R.id.image_btn2://
                share();
                break;
            case R.id.image_code://
                share();
                break;
        }
    }

    private void share() {
        final OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("车势力推荐注册有礼");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(mPath);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("扫码注册车势力，钜惠豪礼抢不停");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        Resources res = TuiGuangActivity.this.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_sj3);
        oks.setImagePath(FileUtils.saveBitmap(TuiGuangActivity.this, bmp));
        // 确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mPath);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
//        // site是分享此内容的网站名称，仅在QQ空间使用
       oks.setSite("车势力推荐注册有礼");
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
       oks.setSiteUrl(mPath);
        // 启动分享GUI
        oks.show(TuiGuangActivity.this);

    }

}
