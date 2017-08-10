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
import com.example.administrator.cheshilishop.bean.CommissionBean;
import com.example.administrator.cheshilishop.utils.DateUtil;

import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by Administrator on 2017/8/2.
 */

public class CommissionAdapter extends BaseAdapter {

    private List<CommissionBean> list;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public CommissionAdapter(Context context, List<CommissionBean> list, int type) {
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
            convertView = inflater.inflate(R.layout.item_commission, null);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_money = convertView.findViewById(R.id.tv_money);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(position).ID)) {
            holder.tv_name.setText(list.get(position).ProductName);
            holder.tv_money.setText("+¥"+list.get(position).Amount);
            holder.tv_time.setText(DateUtil.stampToDate(list.get(position).AddTime));
            holder.tv_type.setText("");
        }
        return convertView;


    }
    static class ViewHolder {
        TextView tv_name;
        TextView tv_money;
        TextView tv_time;
        TextView tv_type;
    }
}
