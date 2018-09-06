package com.example.a12579.myapplication.my;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a12579.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 12579 on 2018/4/29.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener{

    private EditText et_name,et_password;
    private Button btn_register;
    private Boolean result_register = false;



    public RegisterFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_register, container, false);
        et_name = view.findViewById(R.id.my_register_name);
        et_password = view.findViewById(R.id.my_register_password);
        btn_register = view.findViewById(R.id.my_register_btn_register);
        btn_register.setOnClickListener(this);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        String name = et_name.getText().toString();
        String password = et_password.getText().toString();
        if (name.equals("")){
            Toast.makeText(getContext(),"请输入用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("")){
            Toast.makeText(getContext(),"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }

        switch (view.getId()){
            case R.id.my_register_btn_register:
                try {
                    register(name,password);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (result_register){
                    Toast.makeText(getContext(),"注册成功，已登录",Toast.LENGTH_SHORT).show();
                    saveUser(name);
                }else {
                    Toast.makeText(getContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void register(final String name, final String password) throws InterruptedException {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name",name)
                            .add("password",password)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://47.94.157.71/emotion/dele/register")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject_register(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(500);
    }

    private void parseJSONWithJSONObject_register(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            result_register = jsonObject.getBoolean("result");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveUser(String name) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE).edit();
        editor.putString("name",name);
        editor.putBoolean("isLogin",true);
        editor.apply();
        FragmentTransaction fm = getActivity().getFragmentManager().beginTransaction();

        MyFragment myFragment = MyFragment.newInstance(name);
        // 将 fragment_container View 中的内容替换为此 Fragment ，
        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
        fm.replace(R.id.tbb, myFragment);
        fm.addToBackStack(null);

        // 执行事务
        fm.commit();
    }


    public static RegisterFragment newInstance() {
        RegisterFragment registerFragment = new RegisterFragment();
        return registerFragment;
    }
}
