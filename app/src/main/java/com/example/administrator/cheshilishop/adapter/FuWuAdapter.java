package com.example.administrator.cheshilishop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.BookingBean;
import com.example.administrator.cheshilishop.utils.DateUtil;
import com.example.administrator.cheshilishop.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by Administrator on 2017/8/2.
 */

public class FuWuAdapter extends BaseAdapter {

    private List<BookingBean> list;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public FuWuAdapter(Context context, List<BookingBean> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_booking, null);
            holder.mTvTel = convertView.findViewById(R.id.tv_tel);
            holder.mTvStatus = convertView.findViewById(R.id.tv_status);
            holder.mTvEndtime = convertView.findViewById(R.id.tv_endtime);
            holder.mTvOrdernumber = convertView.findViewById(R.id.tv_ordernumber);
            holder.mTvShopname = convertView.findViewById(R.id.tv_shopname);
            holder.mTvMoney = convertView.findViewById(R.id.tv_money);
            holder.mTvBookingtime = convertView.findViewById(R.id.tv_bookingtime);
            holder.mTvService = convertView.findViewById(R.id.tv_service);
            holder.mImgLogo = convertView.findViewById(R.id.img_logo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(position).ID)) {
            holder.mTvTel.setText("订单编号"+list.get(position).OrderID);
            holder.mTvStatus.setText("已服务");
            holder.mTvOrdernumber.setText(list.get(position).ServiceID);
            holder.mTvShopname.setText(list.get(position).ProductName);
            holder.mTvMoney.setText("¥ " + list.get(position).AllMoney);
            if (!TextUtils.isEmpty(list.get(position).ProductDescri)){
            JSONObject descri = null;
            try {
                descri = new JSONObject(list.get(position).ProductDescri);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
            holder.mTvService.setText(list.get(position).StoreName);
            if (!TextUtils.isEmpty(list.get(position).Imgs)) {
                Glide.with(context)
                        .load(UrlUtils.BASE_URL + "/Img/" + list.get(position).ProductImg)
                        .into(holder.mImgLogo);
            }
        }
        return convertView;


    }

    static class ViewHolder {
        TextView mTvTel;
        TextView mTvStatus;
        TextView mTvEndtime;
        TextView mTvOrdernumber;
        ImageView mImgLogo;
        TextView mTvShopname;
        TextView mTvMoney;
        TextView mTvBookingtime;
        TextView mTvService;

    }
}
