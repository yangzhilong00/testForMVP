package com.example.m_evolution.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import com.example.m_evolution.R;
import com.example.m_evolution.interfaces.ILoginContract;
import com.example.m_evolution.interfaces.ILoginModel;
import com.example.m_evolution.model.LoginModel;

import static android.content.Context.MODE_PRIVATE;


public class LoginPresenter implements ILoginContract.Presenter {
    private ILoginContract.View mView;
    private LoginModel mModel;
    private int mCodeWaitTime = 0;
    private Handler mHandler = new Handler();

    //数据
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private String TAG = "LoginPresenter";

    public LoginPresenter(Context context, ILoginContract.View view){
        mView = view;
        mModel = new LoginModel();
        mSharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public void sendCode(String userPhone) {
        if(mCodeWaitTime>0) return ;
        mModel.sendCode(userPhone, new ILoginModel.OnSendCodeListener() {
            @Override
            public void onComplete() {
                mView.showMessage("已发送验证码");
                //设置灰色，并提示倒计时
                mCodeWaitTime = 60;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(mCodeWaitTime > 0){
                            mView.setTextSendCode("重新发送("+mCodeWaitTime+")");
                            mCodeWaitTime--;
                            mHandler.postDelayed(this, 1000);
                        }
                        else{
                            mView.setTextSendCode("发送验证码");
                        }
                    }
                };
                mHandler.post(runnable);
            }

            @Override
            public void onFail(String str) {
                mView.setTextSendCode(str);
            }
        });
    }

    @Override
    public void login(String userPhone, String userCode, String androidId){
        if(userPhone.length()!=11){
            mView.showMessage("手机号未填写");
        }
        else if(userCode.length() == 0){
            mView.showMessage("验证码未填写");
        }
        else{
            mView.setLoginBusying();
            mModel.login(userPhone, userCode, androidId, new ILoginModel.OnLoginListener() {
                @Override
                public void onComplete(String userId) {
                    mView.setLoginNotBusying();
                    mView.showMessage("验证码正确!");
                    mEditor.putString("user_id", userId);
                    mEditor.commit();
                    mView.loginSucceed();
                }

                @Override
                public void onFail(String str) {
                    mView.setLoginNotBusying();
                    mView.showMessage(str);
                }
            });
        }
    }

    @Override
    public void destroy() {
        mView = null;
        mModel = null;
    }
}
