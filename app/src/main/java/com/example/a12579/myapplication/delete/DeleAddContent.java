package com.example.a12579.myapplication.delete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class DeleAddContent extends AppCompatActivity implements View.OnClickListener {


    private Button savebtn,cancelbtn;
    private EditText ettext,etname,ettitle;
    private Boolean result=false;
    String name,text,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dele_add_content);

        savebtn = findViewById(R.id.dele_add_save_btn);
        cancelbtn = findViewById(R.id.dele_add_cancel_btn);
        //etname = findViewById(R.id.dele_add_etname);

        ettext = findViewById(R.id.dele_add_ettext);
        ettitle = findViewById(R.id.dele_add_ettitle);

        savebtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dele_add_save_btn:
                //name = etname.getText().toString();
                name = getIntent().getStringExtra("name");
                text = ettext.getText().toString();
                title = ettitle.getText().toString();
                if (name.equals("")){
                    Toast.makeText(this,"留下大名",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (text.equals("")){
                    Toast.makeText(this,"非测试请添加标题",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (title.equals("")){
                    Toast.makeText(this,"非测试请添加内容",Toast.LENGTH_SHORT).show();
                    break;
                }
                try {
                    addConten();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toastResult();
                finish();
                break;
            case R.id.dele_add_cancel_btn:
                finish();
                break;
        }
    }

    private void addConten() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name",name)
                            .add("title",title)
                            .add("text",text)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://47.94.157.71/emotion/dele/add")
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
//            if (result==false){
//                Toast.makeText(this,"发送失败",Toast.LENGTH_SHORT).show();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void toastResult(){
        System.out.println("result"+result);
        if (result==false){
            Toast.makeText(this,"发送失败",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"发送成功",Toast.LENGTH_SHORT).show();
        }
    }
}
