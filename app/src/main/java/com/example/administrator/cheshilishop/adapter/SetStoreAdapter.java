package com.example.administrator.cheshilishop.adapter;

import android.content.Context;
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

import java.util.List;

public class SetStoreAdapter extends BaseAdapter {
	private Context context;
	private List<StoreBean> list ;

	public SetStoreAdapter(Context context, List<StoreBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size()==0?0:list.size();
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
			holder.img_store= convertView.findViewById(R.id.img_store);
			holder.img_right= convertView.findViewById(R.id.img_right);
			holder.tv_store=convertView.findViewById(R.id.tv_store);
			holder.tv_Address=convertView.findViewById(R.id.tv_Address);
			holder.layout_store = convertView.findViewById(R.id.layout_store);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).ID.equals(CheShiLiShopApplication.storeID)){
			holder.layout_store.setBackgroundResource(R.drawable.bg_store_style2);
			holder.img_right.setVisibility(View.VISIBLE);
		}else {
			holder.layout_store.setBackgroundResource(R.drawable.bg_store_style);
			holder.img_right.setVisibility(View.GONE);
		}
		Glide.with(context)
				.load(list.get(position).Img)
				.into(holder.img_store);
		holder.tv_store.setText(list.get(position).Name);
		holder.tv_Address.setText(list.get(position).Address);
		return convertView;
	}

	public static class ViewHolder {
		private ImageView img_store;
		private ImageView img_right;
		private TextView tv_store;
		private TextView tv_Address;
		private RelativeLayout layout_store;
	}
}
