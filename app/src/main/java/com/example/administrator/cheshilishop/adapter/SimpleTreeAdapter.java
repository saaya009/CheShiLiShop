package com.example.administrator.cheshilishop.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.bean.tree.Node;
import com.example.administrator.cheshilishop.bean.tree.TreeHelper;

import java.util.List;

public class SimpleTreeAdapter<T> extends BaseAdapter {

    protected Context mContext;
    /**
     * 存储所有可见的Node
     */
    protected List<Node> mNodes;
    protected LayoutInflater mInflater;
    /**
     * 存储所有的Node
     */
    protected List<Node> mAllNodes;

    ListView mTree;
    ViewHolder viewHolder = null;

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
                             int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        mContext = context;
        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        mInflater = LayoutInflater.from(context);
        this.mTree = mTree;


    }

    public View getConvertView(final Node node, int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView
                    .findViewById(R.id.id_treenode_icon);
            viewHolder.label = (TextView) convertView
                    .findViewById(R.id.id_treenode_label);
            viewHolder.layout_bg = convertView.findViewById(R.id.layout_bg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
            switch (node.getLevel()) {
                case 0:
                    viewHolder.layout_bg.setBackgroundResource(R.color.colorPrimaryDark);
                    break;
                case 1:
                    viewHolder.layout_bg.setBackgroundResource(R.drawable.bg_order_style);
                    Resources resource = mContext.getResources();
                    ColorStateList csl = resource.getColorStateList(R.color.colorPrimaryDark);
                    viewHolder.label.setTextColor(csl);
                    break;
            }
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
            switch (node.getLevel()) {
                case 0:
                    viewHolder.layout_bg.setBackgroundResource(R.color.colorPrimarybg);
                    break;
                case 1:
                    viewHolder.layout_bg.setBackgroundResource(R.drawable.bg_order_style2);
                    Resources resource = mContext.getResources();
                    ColorStateList csl = resource.getColorStateList(R.color.colorPrimarybg);
                    viewHolder.label.setTextColor(csl);
                    break;
            }
        }


        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Node n = mNodes.get(position);
                if (n != null)// 排除传入参数错误异常
                {
                    if (!n.isLeaf()) {
                        mTree.setSelection(R.color.colorPrimaryDark);
                        n.setExpand(!n.isExpand());
                        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
                        notifyDataSetChanged();// 刷新视图
                    }
                }

            }
        });

        viewHolder.label.setText(node.getName());


        return convertView;
    }

    private final class ViewHolder {
        ImageView icon;
        TextView label;
        RelativeLayout layout_bg;
    }


    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position
     */
    public void expandOrCollapse(int position) {
        Node n = mNodes.get(position);

        if (n != null)// 排除传入参数错误异常
        {
            if (!n.isLeaf()) {
                n.setExpand(!n.isExpand());
                mNodes = TreeHelper.filterVisibleNode(mAllNodes);

                notifyDataSetChanged();// 刷新视图
            }
        }
    }

    @Override
    public int getCount() {
        return mNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        // 设置内边距
        convertView.setPadding(node.getLevel() * 230, 3, 3, 3);
        return convertView;
    }


}
