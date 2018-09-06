package com.example.a12579.myapplication.birth;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.a12579.myapplication.R;
import com.example.a12579.myapplication.delete.DeleteAdapter;
import com.example.a12579.myapplication.my.MyFragment;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 12579 on 2018/img4/14.
 */

public class BirthListFragment extends Fragment{
    private ListView listView;
    private SVProgressHUD svProgressHUD;
    private BirthAdapter birthAdapter;
    private ArrayList<Map<String,Object>> list = new ArrayList<>();
    public static BirthListFragment newInstance() {
        BirthListFragment fragment = new BirthListFragment();
        return fragment;
    }

    public BirthListFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        svProgressHUD = new SVProgressHUD(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birth_list, container, false);
        listView = view.findViewById(R.id.birth_list);
        return view;
    }

    @Override
    public void onResume() {
        svProgressHUD.show();
        super.onResume();
        getData();
        svProgressHUD.dismiss();
    }

    private void getData() {
        list.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址
                Request request=new Request.Builder()
                        .url("http://47.94.157.71/emotion/dele/birth").build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //获取到数据
                    String data=response.body().string();
                    //把数据传入解析josn数据方法
                    jsonJX(data);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void jsonJX(String data) {
        JSONObject jsonObject;
        if (data!=null){
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    Map<String,Object> map = new HashMap();


                    String name = jsonObject.getString("name");
                    String text = jsonObject.getString("text");
                    String time = jsonObject.getString("time");

                    map.put("name",name);
                    map.put("text",text);
                    map.put("time",time);

                    list.add(map);
                }
                System.out.println(list.toString());
                Message message = new Message();
                message.what = 1;

                handler.sendMessage(message);


            }catch (Exception e){
                e.printStackTrace();
            }
        }





    }


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Collections.reverse(list);
                    birthAdapter = new BirthAdapter(getActivity(),list);
                    listView.setAdapter(birthAdapter);
                    birthAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


}
