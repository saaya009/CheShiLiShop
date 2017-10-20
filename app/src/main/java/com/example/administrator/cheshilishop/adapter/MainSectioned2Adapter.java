package com.example.administrator.cheshilishop.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.cheshilishop.CheShiLiShopApplication;
import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.activity.ChangeActivity;
import com.example.administrator.cheshilishop.bean.ProductBean;
import com.example.administrator.cheshilishop.bean.tree.Node;
import com.example.administrator.cheshilishop.widget.PinnedHeaderListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：Ayase on 2017/8/7 14:48
 * 邮箱：ayase@ayase.cn
 */

public class MainSectioned2Adapter extends SectionedBaseAdapter {

    private Context mContext;
    private String[] leftStr;
    private List<List<ProductBean>> rlist;
    private LayoutInflater inflater;

    CheckBox checkBox;
    TextView tv_money;

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public MainSectioned2Adapter(Context context, String[] leftStr, List<List<ProductBean>> rlist, List<List<Boolean>> DeviceStatus) {
        this.mContext = context;
        this.leftStr = leftStr;
        this.rlist = rlist;
        CheShiLiShopApplication.status = DeviceStatus;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 点击的回调接口
     */
    private MSClickListener mListener;

    public interface MSClickListener {
        void onClick(Boolean flag, int section, int position);
    }

    public void setMSClickListener(MSClickListener mListener) {
        this.mListener = mListener;
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
//        final ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.right_list2_item, null);
//            holder.cb = convertView.findViewById(R.id.cb);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        if (lmap.get(position) == null) {
            convertView = inflater.inflate(R.layout.right_list2_item, parent, false);
            checkBox = convertView.findViewById(R.id.cb);
            tv_money = convertView.findViewById(R.id.tv_money);
            convertView.setTag(checkBox);
        } else {
            checkBox = (CheckBox) convertView.getTag();
        }
        checkBox.setText(rlist.get(section).get(position).Name);

        checkBox.setId(Integer.parseInt(rlist.get(section).get(position).ID));
        tv_money.setText(rlist.get(section).get(position).Price);
        checkBox.setChecked( CheShiLiShopApplication.status.get(section).get(position));
        Log.d("选择", "section:" + section + "==position:" + position);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CheShiLiShopApplication.status.get(section).set(position, b);
                notifyDataSetChanged();
            }
        });
        return convertView;
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


    private final class ViewHolder {
        CheckBox cb;
        TextView tv_money;
    }


}
