package com.example.a12579.myapplication.birth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dk.view.patheffect.PathTextView;
import com.example.a12579.myapplication.R;
import com.example.a12579.myapplication.delete.DeleAddContent;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.FireworksPainter;

/**
 * Created by 12579 on 2018/img4/14.
 */

public class BirthAddFragment extends Fragment{
    //PathTextView pathTextView;
    private EditText et_text;
    private Button btn_show,btn_add;
    private Boolean result = false;
    SyncTextPathView syncPathView;
    public static BirthAddFragment newInstance() {
        BirthAddFragment fragment = new BirthAddFragment();
        return fragment;
    }

    public BirthAddFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birth_add, container, false);
//        pathTextView = view.findViewById(R.id.path);
////        app:stroke_width="img2"
////        app:text_color="#E91E63"
////        app:multiple="true"
//        pathTextView.setTextColor(android.support.v7.appcompat.R.color.material_blue_grey_900);
//        pathTextView.setPaintType(PathTextView.Type.SINGLE);
//        pathTextView.init("Hello");

        et_text = view.findViewById(R.id.et_birth_text);
        //et_name = view.findViewById(R.id.et_birth_name);
        btn_add = view.findViewById(R.id.birth_submit);
        btn_show = view.findViewById(R.id.birth_show);
        syncPathView = view.findViewById(R.id.birth_stpv_2017);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_text.getWindowToken(), 0);
                String text = et_text.getText().toString();
                syncPathView.setBackgroundColor(R.color.gray);
                syncPathView.setText(text);
                syncPathView.setShowPainter(true);
                FireworksPainter fireworksPainter = new FireworksPainter();
                syncPathView.setPathPainter(fireworksPainter);
                syncPathView.startAnimation(0,1);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_text.getWindowToken(), 0);
                SharedPreferences preferences = getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);
                String name = preferences.getString("name","");
                Boolean b = preferences.getBoolean("isLogin",false);
                if (b){
                    String text = et_text.getText().toString();
                    //String name = et_name.getText().toString();
                    if (text.equals("")){
                        Toast.makeText(getContext(),"祝福呢？",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        addBirth(text,name);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    toastResult();
                }else {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void addBirth(final String text, final String name) throws InterruptedException {

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name",name)
                            .add("text",text)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://47.94.157.71/emotion/dele/addbirth")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(500);
    }


    private void parseJSONWithJSONObject(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            result = jsonObject.getBoolean("result");
            System.out.println(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void toastResult(){
        System.out.println("result"+result);
        if (result==false){
            Toast.makeText(getContext(),"发送失败",Toast.LENGTH_SHORT).show();
        }else{
            et_text.setText("");
            Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
        }
    }
}
