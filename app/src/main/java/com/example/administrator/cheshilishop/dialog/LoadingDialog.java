package com.example.administrator.cheshilishop.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.administrator.cheshilishop.R;


public class LoadingDialog extends Dialog {



    public LoadingDialog(Context context) {
        this(context, R.style.loadingDialog);

    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
    }

}
