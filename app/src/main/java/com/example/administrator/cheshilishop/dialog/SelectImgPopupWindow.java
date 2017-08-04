package com.example.administrator.cheshilishop.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.administrator.cheshilishop.R;


/**
 * 图片选择dialog
 */
public class SelectImgPopupWindow extends PopupWindow {

    private Button btn_photograph;

    private Button btn_pickphoto;

    private Button btn_cancel;

    private View mView;

    public SelectImgPopupWindow(Context context, View.OnClickListener mOnClickListener){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_selectimg,null);
        btn_photograph = (Button) mView.findViewById(R.id.btn_photograph);
        btn_pickphoto = (Button) mView.findViewById(R.id.btn_pickphoto);
        btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);

        btn_photograph.setOnClickListener(mOnClickListener);
        btn_pickphoto.setOnClickListener(mOnClickListener);
        btn_cancel.setOnClickListener(mOnClickListener);

        // 设置SelectPicPopupWindow的View
        this.setContentView(mView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
