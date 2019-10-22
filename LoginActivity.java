package com.example.m_evolution.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m_evolution.bean.HTTPBean.EasyHTTPBean;
import com.example.m_evolution.bean.HTTPBean.LoginHTTPResult;
import com.example.m_evolution.MyApp;
import com.example.m_evolution.R;
import com.example.m_evolution.controller.LoginController;
import com.example.m_evolution.interfaces.IBaseObservable;
import com.example.m_evolution.interfaces.ILoginContract;
import com.example.m_evolution.interfaces.ILoginObservable;
import com.example.m_evolution.interfaces.ILoginObserver;
import com.example.m_evolution.model.LoginModel;
import com.example.m_evolution.presenters.LoginPresenter;
import com.example.m_evolution.utils.SystemUtils;
import com.example.m_evolution.view.CloseFloatingRelativeLayout;

/**
 * A login screen that offers login via email/password.
 */

public class LoginActivity extends SwipeBackActivity implements View.OnClickListener,ILoginContract.View {
    private MyApp myApp;

    //View
    private EditText mEtUserPhone;
    private EditText mEtUserCode;
    private TextView mTvSendCode;

    //Presenter
    LoginPresenter mPresenter;

    //验证码等待时间
    private int mCodeWaitTime = 0;

    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        initData();
        setListener();
    }

    private void findView(){
        mEtUserPhone = findViewById(R.id.et_user_phone);
        mEtUserCode = findViewById(R.id.et_user_code);
        mTvSendCode = findViewById(R.id.button_send_code);
    }

    private void initData() {
        mPresenter = new LoginPresenter(this);
        myApp = (MyApp) getApplication();
        //设置“获取验证码”不可被编辑
        mTvSendCode.setFocusable(false);
        mTvSendCode.setFocusableInTouchMode(false);
    }

    private void setListener(){
        mTvSendCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(id == R.id.button_send_code){
            if(mCodeWaitTime>0) return ;
            String userPhone = mEtUserPhone.getText().toString();
            mPresenter.sendCode(userPhone);
        }
    }

    @Override
    public void sendCodeUpdate() {
        myApp.makeToast("已发送验证码");
        //设置灰色，并提示倒计时
        mCodeWaitTime = 60;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(mCodeWaitTime > 0){
                    mTvSendCode.setText("重新发送("+mCodeWaitTime+")");
                    mCodeWaitTime--;
                    mHandler.postDelayed(this, 1000);
                }
                else{
                    mTvSendCode.setText("发送验证码");
                }
            }
        };
        mHandler.post(runnable);
    }

    @Override
    public void showFailMessage(String str) {
        myApp.makeToast(str);
    }

}



