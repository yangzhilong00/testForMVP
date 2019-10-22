package com.example.m_evolution.interfaces;


//包含LoginActivity中的View、Presenter的所有函数
public interface ILoginContract {

    interface View {
        void setTextSendCode(String str);  //设置“发送验证码”的文字
        void loginSucceed();  //成功登陆后进行的操作
        void showMessage(String str);
        void setLoginBusying();
        void setLoginNotBusying();
    }

    interface Presenter{
        void sendCode(String userPhone);
        void login(String userPhone, String userCode, String androidId);
        void destroy();
    }

}
