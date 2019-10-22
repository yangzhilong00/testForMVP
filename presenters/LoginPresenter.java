package com.example.m_evolution.presenters;

import android.app.Activity;

import com.example.m_evolution.BasePresenter;
import com.example.m_evolution.activities.LoginActivity;
import com.example.m_evolution.interfaces.ILoginContract;
import com.example.m_evolution.model.LoginModel;

public class LoginPresenter implements ILoginContract.Presenter {
    private ILoginContract.View mView;
    private LoginModel mModel;

    public LoginPresenter(ILoginContract.View view){
        mView = view;
        mModel = new LoginModel();
    }

    @Override
    public void sendCode(String userPhone) {
        mModel.sendCode(userPhone, new LoginModel.OnSendCodeListener() {
            @Override
            public void onComplete() {
                mView.sendCodeUpdate();
            }

            @Override
            public void onFail(String str) {
                mView.showFailMessage(str);
            }
        });
    }
}
