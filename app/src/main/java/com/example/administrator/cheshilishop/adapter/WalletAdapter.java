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
import org.w3c.dom.Text;

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
            holder.tv_type2 = convertView.findViewById(R.id.tv_type2);
            holder.tv_amount = convertView.findViewById(R.id.tv_amount);
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            JSONObject data = new JSONObject(list.get(position).Data);
            switch (data.getString("AccountTransType")) {
                case "0":
                    holder.tv_type.setText("订单");
                    break;
                case "1":
                    holder.tv_type.setText("红包");
                    break;
                case "2":
                    holder.tv_type.setText("充值");
                    break;
                case "3":
                    holder.tv_type.setText("银行卡");
                    break;
                case "4":
                    holder.tv_type.setText("佣金");
                    break;
                case "5":
                    holder.tv_type.setText("服务收入");
                    break;
                case "6":
                    holder.tv_type.setText("优惠券");
                case "7":
                    holder.tv_type.setText("平均");
                    break;
            }
            holder.tv_type2.setText(data.getString("Text"));
            holder.tv_time.setText(DateUtil.stampToDate(list.get(position).AddTime));
            if (list.get(position).InOrOut.equals("1")) {
                holder.tv_amount.setText("+" + list.get(position).Amount);
            } else {
                holder.tv_amount.setText("-" + list.get(position).Amount);
            }
            if (position != 0 && Integer.parseInt(DateUtil.stampToDate4(list.get(position).AddTime)) != Integer.parseInt(DateUtil.stampToDate4(list.get(position - 1).AddTime))) {
                holder.tv_date.setVisibility(View.VISIBLE);
                holder.tv_date.setText(DateUtil.stampToDate5(list.get(position).AddTime));
            }else  if (position ==0){
                holder.tv_date.setVisibility(View.VISIBLE);
                holder.tv_date.setText(DateUtil.stampToDate5(list.get(position).AddTime));
            }else {
                holder.tv_date.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;


    }

    static class ViewHolder {
        TextView tv_type;
        TextView tv_type2;
        TextView tv_time;
        TextView tv_amount;
        TextView tv_date;
    }
}
