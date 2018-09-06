package com.example.a12579.myapplication.voice.ui;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a12579.myapplication.R;
import com.example.a12579.myapplication.voice.adapter.EaseMessageAdapter;
import com.example.a12579.myapplication.voice.model.MessageBean;
import com.example.a12579.myapplication.voice.service.PlayService;
import com.example.a12579.myapplication.voice.utils.AppCache;
import com.example.a12579.myapplication.voice.utils.TimeUtils;
import com.ilike.voicerecorder.widget.VoiceRecorderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 12579 on 2018/6/4.
 */

public class VoiceFragment extends Fragment{

    protected VoiceRecorderView voiceRecorderView;
    protected ListView message_list;
    protected TextView tvRecorder;

    private List<Map<String,Object>> list = new LinkedList<>();
    private Timer timer;
    private boolean islogin;
    private String name;
    private int index;
    EaseMessageAdapter adapter;
    PlayServiceConnection mPlayServiceConnection;

    public static VoiceFragment newInstance(){
        VoiceFragment fragment = new VoiceFragment();
        return fragment;
    }

    public VoiceFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Intent intent = new Intent();
        intent.setClass(getContext(), PlayService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        getActivity().bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);

        message_list = view.findViewById(R.id.message_list);
        voiceRecorderView = view.findViewById(R.id.voice_recorder);

        voiceRecorderView.setShowMoveUpToCancelHint("松开手指，取消发送");
        voiceRecorderView.setShowReleaseToCancelHint("手指上滑，取消发送");
        tvRecorder = view.findViewById(R.id.tv_touch_recorder);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userdata",Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        islogin = sharedPreferences.getBoolean("isLogin",false);
        list.clear();
        index = 0;
        tvRecorder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (islogin==false){
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (AppCache.getPlayService().isPlaying) {
                        AppCache.getPlayService().stopPlayVoiceAnimation2();
                    }
                }

                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new VoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        Log.e("voiceFilePath=", voiceFilePath + "  time = " + voiceTimeLength);
                        //   sendVoiceMessage(voiceFilePath, voiceTimeLength);
                        MessageBean bean = new MessageBean();
                        bean.path = voiceFilePath;
                        bean.msg = "image";
                        bean.second = voiceTimeLength;
                        bean.time = TimeUtils.getCurrentTimeInLong();
                        try {
                            httpPost(voiceFilePath,bean);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        voices.add(bean);
//                        adapter.notifyDataSetChanged();
                        try {
                            httpGet();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        //adapter = new EaseMessageAdapter(getContext(),voices);
        adapter = new EaseMessageAdapter(getContext(),list);
        message_list.setAdapter(adapter);
        adapter.setOnItemClickLister(new EaseMessageAdapter.onItemClickLister() {
            @Override
            public void onItemClick(ImageView imageView, String path, int position) {
                if (AppCache.getPlayService() != null) {
                    AppCache.getPlayService().setImageView(imageView);
                    AppCache.getPlayService().stopPlayVoiceAnimation();
                    //  AppCache.getPlayService().play("http://img.layuva.com//b96c4bde124a328d9c6edb5b7d51afb2.amr");
                    AppCache.getPlayService().play(path);

                }
            }
        });

        try {
            httpGet();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void httpGet() throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("index", String.valueOf(index))
                .build();
        Request request = new Request.Builder()
                .url("http://47.94.157.71/emotion/dele/getvoice")
                .post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        String data = response.body().string();
        JSONObject jsonObject;
        if (data!=null&&!data.equals("")){
            JSONArray jsonArray = new JSONArray(data);
            for (int i =0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                Map<String,Object>map = new HashMap<>();
                map.put("name",jsonObject.getString("name"));
                map.put("url",jsonObject.getString("url"));
                map.put("timestamp",jsonObject.getString("timestamp"));
                map.put("length",jsonObject.getString("time_length"));
                list.add(map);
                adapter.notifyDataSetChanged();
            }
            index += jsonArray.length();
        }
    }

    private void httpPost(String voiceFilePath, MessageBean bean) throws IOException {
        OkHttpClient client = new OkHttpClient();
        File file = new File(voiceFilePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file","abc.amr",fileBody)
                .addFormDataPart("name",name)
                .addFormDataPart("second", String.valueOf(bean.second))
                .addFormDataPart("timestamp", String.valueOf(bean.time))
                .build();
        Request request = new Request.Builder()
                .url("http://47.94.157.71/emotion/dele/addvoice")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        parseJSONWithJSONObject(responseData);
    }

    private void parseJSONWithJSONObject(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            if (jsonObject.getBoolean("result")==false){
                Toast.makeText(getActivity(),"发送失败",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final PlayService playService = ((PlayService.PlayBinder) service).getService();
            Log.e("onServiceConnected----", "onServiceConnected");
            AppCache.setPlayService(playService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);;
            }
        },500,4000);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                try {
                    httpGet();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
