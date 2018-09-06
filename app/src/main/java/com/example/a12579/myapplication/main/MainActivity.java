package com.example.a12579.myapplication.main;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.a12579.myapplication.R;
import com.example.a12579.myapplication.birth.BirthFragment;
import com.example.a12579.myapplication.emotion.EmotionChartFragment;
import com.example.a12579.myapplication.my.LoginFragment;
import com.example.a12579.myapplication.my.MyFragment;
import com.example.a12579.myapplication.delete.DeleFragment;
import com.example.a12579.myapplication.emotion.EmotionFragment;
import com.example.a12579.myapplication.voice.ui.VoiceFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private String TAG = MainActivity.class.getSimpleName();
    private EmotionChartFragment mEmotionFragment;
    private DeleFragment mDeleFragment;
    //private BirthFragment mBirthFragment;
    private VoiceFragment voiceFragment;
    private MyFragment mMyFragment;
    private LoginFragment loginFragment;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermissions();

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.dele, "删除").setActiveColor(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.heart, "心情").setActiveColor(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.birth, "听").setActiveColor(R.color.gray))
                .addItem(new BottomNavigationItem(R.drawable.my, "我").setActiveColor(R.color.gray))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mDeleFragment = DeleFragment.newInstance();
        transaction.replace(R.id.tbb, mDeleFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mDeleFragment == null) {
                    mDeleFragment = DeleFragment.newInstance();
                }
                transaction.replace(R.id.tbb, mDeleFragment);
                break;
            case 1:
                if (mEmotionFragment == null) {
                    mEmotionFragment = EmotionChartFragment.newInstance();
                }
                transaction.replace(R.id.tbb, mEmotionFragment);
                break;
            case 2:
//                if (mBirthFragment == null) {
//                    mBirthFragment = BirthFragment.newInstance();
//                }
//                transaction.replace(R.id.tbb, mBirthFragment);
                if (voiceFragment == null) {
                    voiceFragment = VoiceFragment.newInstance();
                }
                transaction.replace(R.id.tbb, voiceFragment);
                break;
            case 3:
                SharedPreferences preferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                String name = preferences.getString("name","");
                Boolean b = preferences.getBoolean("isLogin",false);
                if (b){
                    if (mMyFragment == null) {
                        mMyFragment = MyFragment.newInstance(name);
                    }
                    transaction.replace(R.id.tbb, mMyFragment);
                }else {
                    if (loginFragment == null) {
                        loginFragment = LoginFragment.newInstance("我");
                    }
                    transaction.replace(R.id.tbb, loginFragment);
                }
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTabUnselected(int position) {
        Log.d(TAG, "onTabUnselected() called with: " + "position = [" + position + "]");
    }

    @Override
    public void onTabReselected(int position) {

    }

    //运行授权，6.0以上系统需要
    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean value) {
                        if (value) {
                            Toast.makeText(MainActivity.this, "同意权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "拒绝权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

}
