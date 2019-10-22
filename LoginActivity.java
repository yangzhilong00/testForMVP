package com.example.m_evolution.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import com.example.m_evolution.MyApp;
import com.example.m_evolution.R;
import com.example.m_evolution.interfaces.ILoginContract;
import com.example.m_evolution.presenters.LoginPresenter;
import com.example.m_evolution.utils.SystemUtils;
import com.example.m_evolution.view.CloseFloatingRelativeLayout;

import org.greenrobot.eventbus.EventBus;

//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
import nsu.edu.com.library.SwipeBackActivity;

import static com.example.m_evolution.activities.ShowTextActivity.gotoShowTextActivity;

//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
//import cn.smssdk.gui.RegisterPage;


/**
 * A login screen that offers login via email/password.
 */

public class LoginActivity extends SwipeBackActivity implements View.OnClickListener,ILoginContract.View {
    private MyApp myApp;

    //View
    private CloseFloatingRelativeLayout mRLayoutMain;
    private RelativeLayout mRLayoutUserPhone;
    private EditText mEtUserPhone;
    private EditText mEtUserCode;
    private TextView mTvSendCode;
    private Button mBtnLogin;
    private TextView mTvAgreement;
    private TextView mTvPolicy;
    private ImageView mIvLoginWechat;
    private ImageView mIvLoginQQ;

    //Presenter
    LoginPresenter mPresenter;

    private boolean mIsBusyingLogin;

    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBar();
        findView();
        initData();
        setListener();
        setLoginNotBusying();
    }

    private void setStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //设置状态栏
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));//设置状态栏颜色
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//实现状态栏不占空间
        }
    }

    private void findView(){
        mRLayoutMain = findViewById(R.id.layout_main);
        mRLayoutUserPhone = findViewById(R.id.layout_user_phone);
        mEtUserPhone = findViewById(R.id.et_user_phone);
        mEtUserCode = findViewById(R.id.et_user_code);
        mTvSendCode = findViewById(R.id.button_send_code);
        mBtnLogin = findViewById(R.id.button_login);
        mTvAgreement = findViewById(R.id.tv_agreement);
        mTvPolicy = findViewById(R.id.tv_policy);
        mIvLoginWechat = findViewById(R.id.iv_login_weixin);
        mIvLoginQQ = findViewById(R.id.iv_login_qq);
    }

    private void initData() {
        mPresenter = new LoginPresenter(getApplicationContext(),this);
        myApp = (MyApp) getApplication();
        //设置“获取验证码”不可被编辑
        mTvSendCode.setFocusable(false);
        mTvSendCode.setFocusableInTouchMode(false);
    }

    private void setListener(){
        mRLayoutMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                return false;
            }
        });
        mRLayoutUserPhone.setOnClickListener(this);
        mTvSendCode.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvAgreement.setOnClickListener(this);
        mTvPolicy.setOnClickListener(this);
        mIvLoginWechat.setOnClickListener(this);
        mIvLoginQQ.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(SystemUtils.isFastDoubleClick()){
            return;
        }
        if(mIsBusyingLogin) return;  //如果正在进行操作，则点击无反应；
        int id = view.getId();
        if(id == R.id.layout_user_phone){
            mEtUserPhone.requestFocus();
            mEtUserPhone.findFocus();
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEtUserPhone, InputMethodManager.SHOW_FORCED);// 显示输入法
        }
        else if(id == R.id.button_send_code){
            String userPhone = mEtUserPhone.getText().toString();
            mPresenter.sendCode(userPhone);
        }
        else if(id == R.id.button_login){
            if(mIsBusyingLogin){
                return;
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            String userPhone = mEtUserPhone.getText().toString();
            String userCode = mEtUserCode.getText().toString();
            mPresenter.login(userPhone, userCode, SystemUtils.getAndroidID(getApplicationContext()));
        }
        else if(id == R.id.tv_agreement){
            gotoShowTextActivity(LoginActivity.this, "agreement");
        }
        else if(id == R.id.tv_policy){
            gotoShowTextActivity(LoginActivity.this, "policy");
        }
        else if(id == R.id.iv_login_weixin){
            myApp.makeToast("暂未开发");
        }
        else if(id == R.id.iv_login_qq){
            myApp.makeToast("暂未开发");
        }
    }

    @Override
    public void setLoginBusying(){
        mIsBusyingLogin = true;
        mBtnLogin.setText("正在登录...");
    }

    @Override
    public void setLoginNotBusying(){
        mIsBusyingLogin = false;
        mBtnLogin.setText("登录");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        //隐藏悬浮窗
        myApp.getMusicFloatingView().detach(this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        myApp.getMusicFloatingView().attach(this);
        if(myApp.isShowMusicFloatingView()){
            if(myApp.isPlayingMainMusic()){
                myApp.getMusicFloatingView().setPlaying();
            }
            else{
                myApp.getMusicFloatingView().setPause();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        mPresenter = null;
    }

    @Override
    public void setTextSendCode(String str) {
        mTvSendCode.setText(str);
    }

    @Override
    public void loginSucceed() {
        //关闭悬浮播放窗口
        if(myApp.isShowMusicFloatingView()){
            if(myApp.isPlayingMainMusic()){
                EventBus.getDefault().post(MyApp.EVENTBUS_MAIN_MUSIC_STOP);
            }
            myApp.setIs_music_floating_show(false);
            myApp.updateFloatingView();
        }
        //跳转到MainActivity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showMessage(String str) {
        myApp.makeToast(str);
    }
}



