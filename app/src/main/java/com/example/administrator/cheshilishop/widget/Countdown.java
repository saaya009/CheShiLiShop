package com.example.administrator.cheshilishop.widget;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.cheshilishop.R;


/**
 * Created by alva on 2016/8/8 0008.
 */
public class Countdown extends CountDownTimer {
    private Button mButton;
    private EditText et;

    public Countdown(Button button, EditText editText, long millisInFuture,
                     long countDownInterval) {
        super(millisInFuture, countDownInterval);
        et = editText;
        mButton = button;
    }

    @Override
    public void onFinish() {

        // 获取 验证码按钮可点击
        setButtonEnable();

    }

    @Override
    public void onTick(long millisUntilFinished) {
        // 获取验证码按钮不可点击,倒计时开始
        setButtonUnEnable(millisUntilFinished);

    }


    @SuppressWarnings("deprecation")
    private void setButtonEnable() {
        if(et.getText().toString().trim().length()==11){
            mButton.setEnabled(true);
            mButton.setText("获取验证码");
            mButton.setBackgroundResource(R.drawable.bg_button_style);
        }else{
            mButton.setEnabled(false);
            mButton.setText("获取验证码");
            mButton.setBackgroundResource(R.drawable.bg_button_style2);
        }
    }

    @SuppressWarnings("deprecation")
    public void setButtonUnEnable(long millisUntilFinished) {
        mButton.setEnabled(false);
        mButton.setText(millisUntilFinished / 1000 + "s后重发");
        mButton.setBackgroundResource(R.drawable.bg_button_style2);
    }
}

