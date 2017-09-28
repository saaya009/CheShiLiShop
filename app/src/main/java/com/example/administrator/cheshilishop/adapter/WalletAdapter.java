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
import com.example.administrator.cheshilishop.bean.WalletBean;
import com.example.administrator.cheshilishop.utils.DateUtil;
import com.example.administrator.cheshilishop.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by Administrator on 2017/8/2.
 */

public class WalletAdapter extends BaseAdapter {

    private List<WalletBean> list;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public WalletAdapter(Context context, List<WalletBean> list, int type) {
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
            convertView = inflater.inflate(R.layout.item_wallet, null);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_amount = convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (list.get(position).Type){
            case "0":
                holder.tv_type.setText("充值");
                break;
            case "1":
                holder.tv_type.setText("提现");
                break;
            case "2":
                holder.tv_type.setText("消费");
                break;
            case "3":
                holder.tv_type.setText("转账");
                break;
            case "4":
                holder.tv_type.setText("活动");
                break;
            case "5":
                holder.tv_type.setText("红包");
                break;
            case "6":
                holder.tv_type.setText("服务收入");
                break;
        }
        holder.tv_time.setText(DateUtil.stampToDate2(list.get(position).AddTime));
        if (Double.parseDouble(list.get(position).Amount)>0){
            holder.tv_amount.setText("+"+list.get(position).Amount);
        }


        return convertView;


    }

    static class ViewHolder {
        TextView tv_type;
        TextView tv_time;
        TextView tv_amount;
    }
}
