package com.example.m_evolution.interfaces;


//包含LoginActivity中的View、Presenter的所有函数
public interface ILoginContract {

    interface View {
        void sendCodeUpdate();
        void showFailMessage(String str);
    }

    interface Presenter{
        void sendCode(String userPhone);
    }

}
