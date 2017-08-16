package com.example.administrator.cheshilishop.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.cheshilishop.R;
import com.example.administrator.cheshilishop.widget.refresh.WdigetUtil;


public class TwoButtonAndContentCustomDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private LayoutInflater factory;
	private TextView dialog_title;
	private TextView dialog_content;
	private LinearLayout dialog_cancel_ll;
	private LinearLayout dialog_confirm_ll;
	private TextView dialog_cancel;
	private TextView dialog_confirm;

	public TwoButtonAndContentCustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TwoButtonAndContentCustomDialog(Context context, int theme) {
		// TODO Auto-generated constructor stub
		super(context, theme);
		this.context = context;
		factory = LayoutInflater.from(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		this.setContentView(factory.inflate(R.layout.dialog_custom_two_button_and_content,
				null));
		WindowManager m = ((Activity) context).getWindowManager();
		Display d = m.getDefaultDisplay();
		Window window = this.getWindow();
		LayoutParams dialogParams = window.getAttributes();
		// dialogParams.width = (int)(d.getWidth()*0.90);
		// dialogParams.height = (int)(d.getHeight() * 0.3);
		dialogParams.y = -WdigetUtil.dip2px(context, 20);
		window.setAttributes(dialogParams);
		window.setGravity(Gravity.CENTER);
		// window.setWindowAnimations(R.style.mydialogstyle);
		dialog_title = (TextView) this.findViewById(R.id.dialog_title);
		dialog_content = (TextView) this.findViewById(R.id.dialog_content);
		dialog_cancel_ll = (LinearLayout) this
				.findViewById(R.id.dialog_cancel_ll);
		dialog_confirm_ll = (LinearLayout) this
				.findViewById(R.id.dialog_confirm_ll);
		dialog_cancel = (TextView) this.findViewById(R.id.dialog_cancel);
		dialog_confirm = (TextView) this.findViewById(R.id.dialog_confirm);
		dialog_cancel_ll.setOnClickListener(this);
		dialog_confirm_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_cancel_ll:
			doCancel();
			break;
		case R.id.dialog_confirm_ll:
			doConfirm();
			break;
		default:
			break;
		}
	}

	public void doCancel() {
		dismiss();
	}

	public void doConfirm() {
		dismiss();
	}

	public void setTitle(String title) {
		dialog_title.setText(title);
	}

	public void setCancel(String cancel) {
		dialog_cancel.setText(cancel);
	}

	public void setConfirm(String confirm) {
		dialog_confirm.setText(confirm);
	}

	public void setContent(String content) {
		dialog_content.setText(content);
	}
}
