package com.example.administrator.cheshilishop;

import android.view.View;

import java.util.ArrayList;

public class TopView {

	public ArrayList<View> topViews = new ArrayList<View>();

	public TopView(View... views) {
		for (View view : views) {
			if (null != view) {
				topViews.add(view);
			}
		}
	}
}
