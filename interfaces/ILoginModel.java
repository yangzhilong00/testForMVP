package com.example.m_evolution.interfaces;

public interface ILoginModel {

    interface ILoginModelAction{
        void sendCode(String userPhone, OnSendCodeListener listener);
        void login(String userPhone, String userCode, String androidId, OnLoginListener listener);
    }

    interface OnSendCodeListener{
        void onComplete();
        void onFail(String str);
    }

    interface OnLoginListener{
        void onComplete(String userId);
        void onFail(String str);
    }
}
