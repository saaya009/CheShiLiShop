package com.example.administrator.cheshilishop.utils;

import android.net.Uri;

/**
 * Created by Administrator on 2017/7/26.
 * 请求地址拼接
 */

public class UrlUtils {
    public static String BASE_URL = "http://api.cheshili.com.cn:8000";

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
        Uri.Builder uribBuilder = Uri.parse(BASE_URL + "/League/Login/ResetPwdByPhone").buildUpon();
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
}
