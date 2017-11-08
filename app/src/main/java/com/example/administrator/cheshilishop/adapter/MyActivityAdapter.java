package com.example.administrator.cheshilishop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.ActivityBean;
import com.example.administrator.cheshilishop.bean.tree.MyActivityBean;
import com.example.administrator.cheshilishop.utils.DateUtil;

import java.util.List;
import java.util.concurrent.TimeoutException;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by Administrator on 2017/8/2.
 */

public class MyActivityAdapter extends BaseAdapter {

    private List<MyActivityBean> list;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public MyActivityAdapter(Context context, List<MyActivityBean> list) {
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
            convertView = inflater.inflate(R.layout.item_activity, null);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_addtime = convertView.findViewById(R.id.tv_addtime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(list.get(position).CampaignName);
        switch (list.get(position).AppTypeID) {
            case "1":
                holder.tv_type.setText("微信");
                break;
            default:
                holder.tv_type.setText("全部");
                break;
        }
        holder.tv_time.setText(DateUtil.stampToDate6(list.get(position).StartTime) + "至" + DateUtil.stampToDate6(list.get(position).EndTime));
        holder.tv_addtime.setText(DateUtil.stampToDate6(list.get(position).AddTime));
        return convertView;


    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_type;
        TextView tv_time;
        TextView tv_addtime;
    }
}
