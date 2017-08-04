package com.example.administrator.cheshilishop.net;

import android.content.Context;
import android.widget.Toast;

import com.example.administrator.cheshilishop.utils.Log;
import com.example.administrator.cheshilishop.utils.NetworkUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author txiuqi
 */
public class RestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void get(String url, Context context,
                           AsyncHttpResponseHandler responseHandler) {
        if (NetworkUtil.isNetworkValidate(context)) {
            try {
                client.get(url, null, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("ok", "getRequest" + url);
        } else {
            Toast.makeText(context, "网络不给力！", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public static void post(String url, RequestParams params, Context context,
                            AsyncHttpResponseHandler responseHandler) {
        if (NetworkUtil.isNetworkValidate(context)) {
            try {
                Log.e("requestUrl", url);
                Log.e("requestParams", params.toString());
                client.post(context, url, params, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "网络不给力！", Toast.LENGTH_SHORT) .show();
        }
    }


}