package com.example.a12579.myapplication.delete;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.a12579.myapplication.R;
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
 * Created by 12579 on 2018/img3/21.
 */

public class DeleFragment extends Fragment{
    private FloatingActionButton add_btn;
    private SVProgressHUD svProgressHUD;
    private LoadMoreListView listView;
    private CircleRefreshLayout circleRefreshLayout;
    private ArrayList<Map<String,Object>> list = new ArrayList<Map<String, Object>>();//从网络中获得的所有数据
    private ArrayList<Map<String,Object>> data = new ArrayList<Map<String, Object>>();//加载入listview的数据
    private int index;//已加载数据
    private int maxNum = 8;//每次加载的最大数据
    private int length;//总数据长度
    private DeleteAdapter adapter;
    public static DeleFragment newInstance() {
        DeleFragment fragment = new DeleFragment();
        return fragment;
    }

    public DeleFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = 0;
        length = 0;
        svProgressHUD = new SVProgressHUD(getContext());

    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        circleRefreshLayout = view.findViewById(R.id.circlefreshlayout);
        circleRefreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {
                Toast.makeText(getActivity(), "refresh has complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void refreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        circleRefreshLayout.finishRefreshing();
                    }
                },2000);
            }
        });

        listView = view.findViewById(R.id.dele_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击列表后
                if (list.size()>0&&list!=null)
                {
                    HashMap<String,Object> map = (HashMap<String, Object>) list.get(i);
                    Intent intent = new Intent(getActivity(), SelectDelete.class);
                    intent.putExtra("data", map);//只能传序列化的数据
                    startActivity(intent);
                }

            }
        });

        //设置底部菜单不遮挡listview
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 0, 0, 100);
//        listView.setLayoutParams(lp);

        LinearLayout linearLayout = view.findViewById(R.id.dele_linear_btn);
        linearLayout.bringToFront();

        add_btn = view.findViewById(R.id.dele_cell_add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //添加数据
                SharedPreferences preferences = getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE);
                String name = preferences.getString("name","");
                Boolean b = preferences.getBoolean("isLogin",false);
                if (b){
                    Intent i = new Intent(getActivity(), DeleAddContent.class);
                    i.putExtra("name",name);
                    startActivity(i);
                }else {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        svProgressHUD.show();
        getData();
        svProgressHUD.dismiss();
    }



    private void getData() {
        list.clear();
        data.clear();
        if (adapter!=null)
            adapter.notifyDataSetChanged();
        //从网页获取评论信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient=new OkHttpClient();
                //服务器返回的地址  
                Request request=new Request.Builder()
                        .url("http://47.94.157.71/emotion/dele/text").build();
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


                    String name = jsonObject.getString("username");
                    String title = jsonObject.getString("title");
                    String text = jsonObject.getString("text");
                    String agree = String.valueOf(jsonObject.getInt("agree"));
                    String disagree = String.valueOf(jsonObject.getInt("disagree"));
                    String time = jsonObject.getString("time");
                    String comment_num = String.valueOf(jsonObject.getInt("comment_num"));
                    String comment = String.valueOf(jsonObject.getInt("comment"));

                    map.put("name",name);
                    map.put("title",title);
                    map.put("text",text);
                    map.put("agree",agree);
                    map.put("disagree",disagree);
                    map.put("time",time);
                    map.put("comment_num",comment_num);
                    map.put("comment",comment);

                    list.add(map);
                }
                //System.out.println(list.toString());
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
                    index = 0;
                    length = list.size();
                    //System.out.println("list_size =  "+length);
                    Collections.reverse(list);
                    //复制一部分
                    for (int i = 0;i<maxNum&&i<length;i++,index++)
                    {
                        if (list!=null&&list.size()>0)
                            data.add(list.get(i));
                       // System.out.println("index1="+i);
                    }
                    adapter = new DeleteAdapter(getActivity(),data);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
                        @Override
                        public void onloadMore() {
//                            if (index==length)
//                            {
//                                Toast.makeText(getActivity(),"到底了",Toast.LENGTH_SHORT).show();
//                                listView.setLoadCompleted();
//                                return;
//                            }
                            loadMore();
                        }
                    });
                    break;
            }
        }
    };

    private void loadMore() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //data.clear();
                int i=0;
                for (;i<maxNum&&index+i<length;i++)
                {
                    if (list!=null&&list.size()>0)
                        data.add(list.get(i+index));
                    //System.out.println("index2="+i);
                }
                index += i;

                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        listView.setLoadCompleted();

                    }
                });
            }
        }.start();
    }
}
