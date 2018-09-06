package com.example.a12579.myapplication.delete;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a12579.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 12579 on 2018/4/25.
 */

public class CommentList extends AppCompatActivity{
    private ListView listView;
    private Button btn_add;
    private EditText et_add;
    private  Boolean result=false;
    private ArrayList<Map<String,Object>> list = new ArrayList<>();
    private String comment_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_comment_layout);

        listView = findViewById(R.id.select_comment_list);
        Intent intent = getIntent();
        //list = (ArrayList<Map<String, Object>>) intent.getSerializableExtra("data");
//        Collections.reverse(list);
//        SelectAdapter adapter = new SelectAdapter(this, list);
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        comment_id = intent.getStringExtra("comment_id");

        getComment(comment_id);

        //设置底部菜单不遮挡listview
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 200);
        listView.setLayoutParams(lp);


        et_add = findViewById(R.id.select_delete_et_comment);
        btn_add = findViewById(R.id.select_delete_btn_addcomment);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_add.getWindowToken(), 0);
                SharedPreferences preferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
                String name = preferences.getString("name", "");
                Boolean b = preferences.getBoolean("isLogin", false);
                if (b) {
                    String comment = et_add.getText().toString();
                    if (comment == null || comment.equals("")) {
                        Toast.makeText(CommentList.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        addComment(comment, comment_id, name);
                        toastResult();
                        refresh();
                    }
                } else {
                    Toast.makeText(CommentList.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refresh() {
        //刷新页面
        finish();
        Intent intent = new Intent(CommentList.this,CommentList.class);
        //intent.putExtra("data",list);
        intent.putExtra("comment_id",comment_id);
        startActivity(intent);
    }

    private void addComment(final String comment, final String comment_id, final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id",comment_id)
                            .add("text",comment)
                            .add("name",name)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://47.94.157.71/emotion/dele/addcomment")
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
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void parseJSONWithJSONObject(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            result = jsonObject.getBoolean("result");
//            if (result==false){
//                Toast.makeText(this,"发送失败",Toast.LENGTH_SHORT).show();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getComment(final String comment_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://47.94.157.71/emotion/dele/comment?id="+comment_id).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String data = response.body().string();
                    jsonJX(data);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void jsonJX(String data) throws JSONException {
        JSONObject jsonObject;
        if (data!=null){
            System.out.println(true);
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                Map<String,Object> map = new HashMap<>();
                System.out.println(jsonArray);

                map.put("text",jsonObject.getString("text"));
                map.put("time",jsonObject.getString("time"));
                map.put("name",jsonObject.getString("name"));
                list.add(map);
            }
            Message message = new Message();
            message.what = 1;

            handler.sendMessage(message);
        }else{
            System.out.println(false);
        }
    }


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Collections.reverse(list);
                    SelectAdapter adapter = new SelectAdapter(CommentList.this,list);
                    listView.setAdapter(adapter);
                    //setListViewHeightBasedOnChildren(listView);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    public void toastResult(){
        System.out.println("result"+result);
        if (result == false){
            Toast.makeText(this,"发送失败",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"发送成功",Toast.LENGTH_SHORT).show();
        }
    }

}
