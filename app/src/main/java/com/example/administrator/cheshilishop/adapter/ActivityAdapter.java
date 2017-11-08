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
import com.example.administrator.cheshilishop.bean.ActivityBean;
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

public class ActivityAdapter extends BaseAdapter {

    private List<ActivityBean> list;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public ActivityAdapter(Context context, List<ActivityBean> list) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(position).ID)) {
            holder.tv_name.setText(list.get(position).Name);
            switch (list.get(position).AppTypeID) {
                case "1":
                    holder.tv_type.setText("微信");
                    break;
                default:
                    holder.tv_type.setText("全部");
                    break;
            }
            holder.tv_time.setText(DateUtil.stampToDate3(list.get(position).StartTime) + "至" + DateUtil.stampToDate3(list.get(position).EndTime));
        }
        return convertView;


    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_type;
        TextView tv_time;

    }
}
