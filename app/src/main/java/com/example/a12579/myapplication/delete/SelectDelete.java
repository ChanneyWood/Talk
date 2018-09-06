package com.example.a12579.myapplication.delete;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class SelectDelete extends AppCompatActivity implements View.OnClickListener {

    private TextView s_title,s_name,s_text,s_agree,s_disagree,s_time,s_comment_num;
    ImageView iv_agree,iv_disagree,iv_comment;
    Boolean is_agree_select=false;
    Boolean is_disagree_select=false;
    //private ListView listView;
    private String comment_id;


    private  Boolean result=false;
    HashMap<String,Object> map;
    private ArrayList<Map<String,Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        map = (HashMap<String, Object>) intent.getSerializableExtra("data");

        setContentView(R.layout.select_delete_layout);
        s_title = findViewById(R.id.select_title);
        s_name = findViewById(R.id.select_name);
        s_text = findViewById(R.id.select_text);
        s_time = findViewById(R.id.select_time);
        s_agree = findViewById(R.id.select_agree);
        s_disagree = findViewById(R.id.select_disagree);
        s_comment_num = findViewById(R.id.select_comment);

        s_title.setText(map.get("title").toString());
        s_name.setText(map.get("name").toString());
        s_text.setText(map.get("text").toString());
        s_time.setText(map.get("time").toString());
        s_disagree.setText(map.get("disagree").toString());
        s_agree.setText(map.get("agree").toString());
        s_comment_num.setText(map.get("comment_num").toString());

        comment_id = map.get("comment").toString();



        iv_agree = findViewById(R.id.select_img_agree);
        iv_disagree = findViewById(R.id.select_img_disagree);
        iv_comment = findViewById(R.id.select_img_comment);
        iv_agree.setOnClickListener(this);
        iv_disagree.setOnClickListener(this);
        iv_comment.setOnClickListener(this);







//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String comment = et_add.getText().toString();
//                if (comment!=null){
//                    addComment(comment,comment_id);
//                    refresh();
//                }else {
//                    Toast.makeText(SelectDelete.this,"内容不能为空",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 0, 0, 240);
//        listView.setLayoutParams(lp);
    }

//    /**
//     * 动态设置ListView的高度
//     * @param listView
//     */
//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        if(listView == null) return;
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            System.out.println("no adapter");
//            return;
//        }
//
//        int totalHeight = 500;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            System.out.println(totalHeight += listItem.getMeasuredHeight());
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        System.out.println(totalHeight);
//        System.out.println(listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        System.out.println(params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() + 3)));
//        //params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.select_delete_btn_addcomment:
//                InputMethodManager imm =(InputMethodManager)getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(et_add.getWindowToken(), 0);
//                SharedPreferences preferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
//                String name = preferences.getString("name","");
//                Boolean b = preferences.getBoolean("isLogin",false);
//                if (b){
//                    String comment = et_add.getText().toString();
//                    if (comment==null||comment.equals("")){
//                        Toast.makeText(SelectDelete.this,"内容不能为空",Toast.LENGTH_SHORT).show();
//                    }else {
//                        addComment(comment,comment_id,name);
//                        toastResult();
//                        refresh();
//                    }
//                }else {
//                    Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
//                }

//                System.out.println(comment);
//                System.out.println(comment.equals(""));

//                break;
            case R.id.select_img_agree:
                if (is_agree_select==false&&is_disagree_select==false){
                    iv_agree.setImageResource(R.drawable.agree_selects);
                    addagree();
                    is_agree_select = true;
                    int agree = Integer.parseInt(map.get("agree").toString());
                    s_agree.setText(agree+1+"");
                }
                break;
            case R.id.select_img_disagree:
                if (is_disagree_select==false&&is_agree_select==false){
                    iv_disagree.setImageResource(R.drawable.disagree_select);
                    adddisagree();
                    is_disagree_select = true;
                    int disagree = Integer.parseInt(map.get("disagree").toString());
                    s_disagree.setText(disagree+1+"");
                }
                break;
            case R.id.select_img_comment:
                Intent i = new Intent(this,CommentList.class);
                i.putExtra("data",list);
                i.putExtra("comment_id",comment_id);
                startActivity(i);
                break;
        }
    }



    private void addagree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id",comment_id)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://47.94.157.71/emotion/dele/addagree")
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
    }

    private void adddisagree() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id",comment_id)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://47.94.157.71/emotion/dele/adddisagree")
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
    }

//    private void refresh() {
//        //刷新页面
//        finish();
//        Intent intent = new Intent(SelectDelete.this,SelectDelete.class);
//        intent.putExtra("data",map);
//        startActivity(intent);
//    }

//    private void addComment(final String comment, final String comment_id, final String name) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("id",comment_id)
//                            .add("text",comment)
//                            .add("name",name)
//                            .build();
//                    Request request = new Request.Builder()
//                            .url("http://47.94.157.71/emotion/dele/addcomment")
//                            .post(requestBody)
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    parseJSONWithJSONObject(responseData);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

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











}
