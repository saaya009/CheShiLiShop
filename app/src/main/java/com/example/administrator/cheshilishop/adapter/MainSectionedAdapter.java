package com.example.administrator.cheshilishop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.activity.ChangeActivity;
import com.example.administrator.cheshilishop.bean.ProductBean;

import java.util.List;

/**
 * 作者：Ayase on 2017/8/7 14:48
 * 邮箱：ayase@ayase.cn
 */

public class MainSectionedAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private String[] leftStr;
    private List<List<ProductBean>> rlist;

    public MainSectionedAdapter(Context context, String[] leftStr, List<List<ProductBean>> rlist) {
        this.mContext = context;
        this.leftStr = leftStr;
        this.rlist = rlist;
    }

    @Override
    public Object getItem(int section, int position) {
        return rlist.get(section).get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr.length;
    }

    @Override
    public int getCountForSection(int section) {
        return rlist.get(section).size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.right_list_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.tv_name)).setText(rlist.get(section).get(position).ProductName);
        ((TextView) layout.findViewById(R.id.tv_context)).setText(rlist.get(section).get(position).Descri);
        ((TextView) layout.findViewById(R.id.tv_money)).setText("¥"+rlist.get(section).get(position).GapPrice);
        (layout.findViewById(R.id.btn_change)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChangeActivity.class);
                intent.putExtra("ID",rlist.get(section).get(position).ID);
                intent.putExtra("productID",rlist.get(section).get(position).ProductID);
                mContext.startActivity(intent);
            }
        });
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        return layout;
    }



}
