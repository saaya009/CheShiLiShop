package com.example.administrator.cheshilishop.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.tree.Node;

import java.util.List;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

    private int bigger;
    private boolean changed = false;
    private List<T> datas;

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
                             int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.datas = datas;
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null || changed) {
            convertView = mInflater.inflate(R.layout.list_item1_on, parent, false);
            changed = false;
            viewHolder = new ViewHolder();
            viewHolder.icon = convertView.findViewById(R.id.id_treenode_icon);
            viewHolder.label = convertView.findViewById(R.id.id_treenode_label);
            viewHolder.down = convertView.findViewById(R.id.down);
            viewHolder.up = convertView.findViewById(R.id.up);
            viewHolder.left = convertView.findViewById(R.id.left);
            viewHolder.layout_bg = convertView.findViewById(R.id.layout_bg);
            viewHolder.layout_line = convertView.findViewById(R.id.layout_line);
            viewHolder.layout_line2 = convertView.findViewById(R.id.layout_line2);
            viewHolder.layout_line3 = convertView.findViewById(R.id.layout_line3);
            viewHolder.layout_shop = convertView.findViewById(R.id.layout_shop);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
            if (node.isLeaf()) {
                switch (node.getLevel()) {
                    case 0://
                        viewHolder.layout_bg.setBackgroundResource(R.color.colorPrimaryDark);
                        Resources resource = mContext.getResources();
                        ColorStateList csl = resource.getColorStateList(R.color.white);
                        viewHolder.label.setTextColor(csl);
                        break;
                    case 1://
                        viewHolder.layout_bg.setBackgroundResource(R.drawable.bg_order_style);
                        resource = mContext.getResources();
                        csl = resource.getColorStateList(R.color.colorPrimaryDark);
                        viewHolder.label.setTextColor(csl);
                        break;
                }
            }
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
            if (node.isExpand()) {
                switch (node.getLevel()) {
                    case 0://根节点
                        viewHolder.layout_bg.setBackgroundResource(R.color.colorPrimaryDark);
                        Resources resource = mContext.getResources();
                        ColorStateList csl = resource.getColorStateList(R.color.white);
                        viewHolder.icon.setBackgroundResource(R.mipmap.tree_ex);
                        viewHolder.label.setTextColor(csl);
                        break;
                    case 1://中间节点
                        viewHolder.layout_bg.setBackgroundResource(R.drawable.bg_order_style);
                        resource = mContext.getResources();
                        csl = resource.getColorStateList(R.color.colorPrimaryDark);
                        viewHolder.icon.setBackgroundResource(R.mipmap.tree_ex2);
                        viewHolder.label.setTextColor(csl);
                        break;
                    case 2:
                        viewHolder.layout_bg.setBackgroundResource(R.color.white);
                        resource = mContext.getResources();
                        csl = resource.getColorStateList(R.color.colorPrimaryDark);
                        viewHolder.icon.setVisibility(View.INVISIBLE);
                        viewHolder.label.setTextColor(csl);
                        break;
                }
            } else {
                switch (node.getLevel()) {
                    case 0://根节点
                        viewHolder.layout_bg.setBackgroundResource(R.color.colorPrimarybg);
                        Resources resource = mContext.getResources();
                        ColorStateList csl = resource.getColorStateList(R.color.white);
                        viewHolder.icon.setBackgroundResource(R.mipmap.tree_ec);
                        viewHolder.label.setTextColor(csl);
                        break;
                    case 1://中间节点
                        viewHolder.layout_bg.setBackgroundResource(R.drawable.bg_order_style2);
                        resource = mContext.getResources();
                        csl = resource.getColorStateList(R.color.colorPrimarybg);
                        viewHolder.icon.setBackgroundResource(R.mipmap.tree_ec2);
                        viewHolder.label.setTextColor(csl);
                        break;
                    case 2:
                        viewHolder.layout_bg.setBackgroundResource(R.color.white);
                        resource = mContext.getResources();
                        csl = resource.getColorStateList(R.color.colorPrimaryDark);
                        viewHolder.icon.setVisibility(View.INVISIBLE);
                        viewHolder.label.setTextColor(csl);
                        break;
                }
            }

        }
        switch (node.getLevel()) {
            case 2:
                viewHolder.layout_bg.setBackgroundResource(R.color.white);
                Resources resource = mContext.getResources();
                ColorStateList csl = resource.getColorStateList(R.color.colorPrimaryDark);
                viewHolder.icon.setVisibility(View.INVISIBLE);
                viewHolder.label.setTextColor(csl);
                viewHolder.layout_line2.setVisibility(View.VISIBLE);
                viewHolder.layout_line.setVisibility(View.VISIBLE);
                break;
            case 1:
                viewHolder.layout_line2.setVisibility(View.VISIBLE);
                ;
                viewHolder.layout_line.setVisibility(View.GONE);
                break;
            case 0:
                viewHolder.layout_line.setVisibility(View.GONE);
                viewHolder.layout_line2.setVisibility(View.GONE);
                break;
        }

        if (node.getId() == 1) {
            viewHolder.up.setVisibility(View.INVISIBLE);
            viewHolder.layout_shop.setVisibility(View.VISIBLE);
            viewHolder.left.setVisibility(View.VISIBLE);
        } else if (node.getId() == datas.size()) {
            viewHolder.down.setVisibility(View.INVISIBLE);
            viewHolder.layout_shop.setVisibility(View.INVISIBLE);
            viewHolder.left.setVisibility(View.INVISIBLE);
        } else if (!node.isRoot() && node.getParent().getChildren().get(node.getParent().getChildren().size() - 1).getId() == node.getId()) {
            viewHolder.down.setVisibility(View.INVISIBLE);
            viewHolder.layout_shop.setVisibility(View.INVISIBLE);
            viewHolder.left.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.down.setVisibility(View.VISIBLE);
        }


        viewHolder.label.setText(node.getName());


        return convertView;
    }

    private final class ViewHolder {
        ImageView icon;
        TextView up;
        TextView down;
        TextView left;
        TextView label;
        RelativeLayout layout_bg;
        LinearLayout layout_line;
        LinearLayout layout_line2;
        LinearLayout layout_line3;
        LinearLayout layout_shop;
    }

    public void setBigger(int bigger) {
        this.bigger = bigger;
        changed = true;
    }
}
