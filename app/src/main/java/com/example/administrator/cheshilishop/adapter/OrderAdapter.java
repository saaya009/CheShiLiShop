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
import com.example.administrator.cheshilishop.bean.UserRegisterBean;
import com.example.administrator.cheshilishop.utils.DateUtil;
import com.example.administrator.cheshilishop.utils.InviteUtils;
import com.example.administrator.cheshilishop.utils.UrlUtils;

import java.util.List;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by Administrator on 2017/8/23.
 */

public class OrderAdapter extends BaseAdapter {

    private List<UserRegisterBean> list;
    private LayoutInflater inflater;
    private Context context;

    public OrderAdapter(Context context, List<UserRegisterBean> list) {
        this.context = context;
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_order, null);
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_nickname = convertView.findViewById(R.id.tv_nickname);
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_InviteCode = convertView.findViewById(R.id.tv_InviteCode);
            holder.tv_InviteId = convertView.findViewById(R.id.tv_InviteId);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(position).UserID)) {
            holder.tv_username.setText(list.get(position).Mobile.substring(0,3)+"****"+list.get(position).Mobile.substring(7,list.get(position).Mobile.length()));
            holder.tv_nickname.setText(list.get(position).NickName);
            holder.tv_date.setText(DateUtil.stampToDate3(list.get(position).AddTime));
            holder.tv_time.setText(DateUtil.stampToDate2(list.get(position).AddTime));
            holder.tv_InviteCode.setText(InviteUtils.getInviteCode(list.get(position).RegisterCont).trim());
            holder.tv_InviteId.setText(list.get(position).InviteCode);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_username;
        TextView tv_nickname;
        TextView tv_date;
        TextView tv_time;
        TextView tv_InviteCode;
        TextView tv_InviteId;

    }
}
