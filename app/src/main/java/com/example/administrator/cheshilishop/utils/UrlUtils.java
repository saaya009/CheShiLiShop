package com.example.administrator.cheshilishop.utils;

import android.net.Uri;

/**
 * Created by Administrator on 2017/7/26.
 * 请求地址拼接
 */

public class UrlUtils {
    public static String BASE_URL = "http://api.cheshili.com.cn";

    /**
     * 登录
     */
    public static String login() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Login/LG").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }


    /**
     * 获取验证码
     */
    public static String getCode() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/Product/Info/SendVerifiedCode").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 找回密码
     */
    public static String getPwd() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/CSL/Login/ResetPwdByPhone").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 上传图片
     */
    public static String UploaImg() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/LeagueInfo/UpdateImg").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取OrderString
     */
    public static String orderString(String json) {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/CSL/A_Pay/GetOrderString").buildUpon()
                .appendEncodedPath(json);
        url = uribBuilder.build().toString();
        return url;
    }


    /**
     * 查询服务预约List
     */
    public static String queryServiceAppointList() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/QueryServiceAppointList").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }


    /**
     * 获取用户数据
     */
    public static String getInfoByToken() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/LeagueInfo/GetInfoByToken").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取Service列表
     */
    public static String queryServiceList() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/QueryServiceJson").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取默认Store
     */
    public static String queryDefaultStore() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/LeagueInfo/QueryDefaultStore").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取Store列表
     */
    public static String queryStoreList() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Store/QueryStoreList").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取Store列表
     */
    public static String updateDefaultStore() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/LeagueInfo/UpdateDefaultStore").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 查询商店的佣金记录
     */
    public static String queryCmnList() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Cmn/QueryCmnList").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取更新
     */
    public static String queryAppNewVersion() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/Product/App/QueryAppNewVersion?AppTypeID=5").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 查询服务预约详情
     */
    public static String queryServiceAppointDetail() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/QueryServiceAppointDetail").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 查询用户服务详情
     */
    public static String queryUserServiceDetail() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/QueryUserServiceDetail").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 查询用户服务详情
     */
    public static String confirmService() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/ConfirmService").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 取消预约
     */
    public static String cancelAppoint() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/CancelAppoint").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取三级分销列表
     */
    public static String queryUserRegisterJson() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Store/QueryUserRegisterJson").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 添加ErrLog
     */
    public static String insertErrLog() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/InsertErrLog").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 添加ErrLog
     */
    public static String queryCmnCount() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Cmn/QueryCmnCount").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 增加商家
     */
    public static String addStore() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Store/AddStoreAll").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 更新商家
     */
    public static String updateStore() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Store/UpdateStore").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 增加商家图片(虚拟文件夹
     */
    public static String addStoreImage() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Store/AddStoreImage").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }


    /**
     * 获取Store的Detail
     */
    public static String queryStoreDetail() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Store/QueryStoreDetail").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }


    /**
     * 修改商家服务
     */
    public static String updateService() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/UpdateService").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }


    /**
     * 查询可用标准服务json
     */
    public static String queryProductServiceJson() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/Product/StoreService/QueryProductServiceJson").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 批量添加服务
     */
    public static String addServices() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/AddServices").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 删除商品
     */
    public static String delCategory() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/DelService").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 查询服务列表
     */
    public static String queryService() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/QueryServiceList").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 注册
     */
    public static String registerUser() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Login/RegisterUser").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 修改Service是否可用
     */
    public static String NAService() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/NAService").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取我的余额
     */
    public static String queryAccountCount() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Account/QueryAccountCount").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取我的账单列表
     */
    public static String queryAccountList() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Account/QueryAccountList").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 清除缓存
     */
    public static String flushRedisByLeagueID() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/LeagueInfo/FlushRedisByLeagueID").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 清除缓存
     */
    public static String updateNickName() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/LeagueInfo/UpdateNickName").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取活动列表1
     */
    public static String queryCampaignList() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/Product/Campaign/QueryCampaignList").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 获取活动列表1
     */
    public static String queryMyLeagueList() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/StoreService/QueryMyLeagueList").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 更新店铺活动协议说明
     */
    public static String updateStoreCampaignAgreement() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Campaign/UpdateStoreCampaignAgreement").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

    /**
     * 图片修改
     */
    public static String updateStoreImg() {
        String url = null;
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Store/UpdateStoreImg").buildUpon();
        url = uribBuilder.build().toString();
        return url;
    }

}
