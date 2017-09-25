package com.example.administrator.cheshilishop.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.activity.UserInfoActivity;
import com.example.administrator.cheshilishop.bean.StoreBean;
import com.example.administrator.cheshilishop.utils.UrlUtils;

import java.util.List;

public class SetStoreAdapter extends BaseAdapter {
    private Context context;
    private List<StoreBean> list;

    public SetStoreAdapter(Context context, List<StoreBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size() == 0 ? 0 : list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_store, null);
            holder.img_store = convertView.findViewById(R.id.img_store);
            holder.img_right = convertView.findViewById(R.id.img_right);
            holder.tv_store = convertView.findViewById(R.id.tv_store);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.img_shenhe = convertView.findViewById(R.id.img_shenhe);
            holder.tv_shenhe = convertView.findViewById(R.id.tv_shenhe);
            holder.tv_Address = convertView.findViewById(R.id.tv_Address);
            holder.layout_store = convertView.findViewById(R.id.layout_store);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if ("0".equals(list.get(position).Enable)) {

        } else {
            holder.tv_shenhe.setVisibility(View.GONE);
            holder.img_shenhe.setVisibility(View.GONE);
        }

        switch (list.get(position).Approved){
            case "0"://审核不通过
                holder.img_shenhe.setVisibility(View.VISIBLE);
                holder.img_right.setVisibility(View.GONE);
                holder.img_shenhe.setImageResource(R.mipmap.icon_close);
                holder.tv_shenhe.setText("审核不通过");
                holder.tv_shenhe.setVisibility(View.VISIBLE);
                Resources resource = context.getResources();
                ColorStateList csl = resource.getColorStateList(R.color.pickerview_wheelview_textcolor_out);
                holder.tv_shenhe.setTextColor(csl);
                holder.layout_store.setEnabled(false);
                break;
            case "1"://审核通过
                break;
            case "2"://审核zhong
                holder.img_shenhe.setVisibility(View.VISIBLE);
                holder.img_right.setVisibility(View.GONE);
                holder.img_shenhe.setImageResource(R.mipmap.icon_time2);
                holder.tv_shenhe.setText("审核中");
                holder.tv_shenhe.setVisibility(View.VISIBLE);
                resource = context.getResources();
                csl = resource.getColorStateList(R.color.textOrange);
                holder.tv_shenhe.setTextColor(csl);
                holder.layout_store.setEnabled(false);
                break;
        }

        if (list.get(position).ID.equals(CheShiLiShopApplication.storeID)) {
            holder.layout_store.setBackgroundResource(R.drawable.bg_store_style2);
            holder.img_right.setVisibility(View.VISIBLE);
        } else {
            holder.layout_store.setBackgroundResource(R.drawable.bg_store_style);
            holder.img_right.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(list.get(position).Img)) {
            Glide.with(context)
                    .load(UrlUtils.BASE_URL + "/Img/" + list.get(position).Img)
                    .into(holder.img_store);
        }
        holder.tv_store.setText(list.get(position).Name);
        holder.tv_Address.setText(list.get(position).Address);
        switch (list.get(position).Type) {
            case "0":
                holder.tv_type.setText("旗舰店");
                break;
            case "1":
                holder.tv_type.setText("综合店");
                break;
            case "2":
                holder.tv_type.setText("专营店");
                break;
        }

        return convertView;
    }

    public static class ViewHolder {
        private ImageView img_store;
        private ImageView img_right;
        private ImageView img_shenhe;
        private TextView tv_store;
        private TextView tv_Address;
        private TextView tv_type;
        private TextView tv_shenhe;
        private RelativeLayout layout_store;
    }
}
