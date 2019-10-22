package com.example.m_evolution.model;

import android.util.Log;

import com.example.m_evolution.bean.HTTPBean.EasyHTTPBean;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.m_evolution.MyApp.httpAPI;

public class LoginModel{
    private String TAG = "LoginModel";

    //进行网络请求：请求发送验证码
    public void sendCode(String userPhone, final OnSendCodeListener listener){
        Log.d(TAG, "sendCode: ");
        if(userPhone.length() == 0){
            if(listener != null){
                listener.onFail("手机号未填写");
            }
            return;
        }
        else if(userPhone.length()<11){
            if(listener != null){
                listener.onFail("手机号不正确");
            }
            return;
        }
        //通过HTTP发送请求
        httpAPI.verify(userPhone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EasyHTTPBean>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.toString());
                        if(listener != null){
                            listener.onFail("网络出错");
                        }
                    }

                    @Override
                    public void onNext(EasyHTTPBean easyHTTPBean) {
                        if(easyHTTPBean.getCode()==0 && easyHTTPBean.getIsOk().equals("1")){
                            if(listener != null){
                                listener.onComplete();
                            }
                        }
                        else{
                            if(listener != null){
                                listener.onFail("操作失败: "+easyHTTPBean.getMsg());
                            }
                        }
                    }
                });
    }

    public interface OnSendCodeListener{
        void onComplete();
        void onFail(String str);
    }
}
