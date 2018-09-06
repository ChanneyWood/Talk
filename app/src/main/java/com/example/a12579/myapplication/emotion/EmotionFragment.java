package com.example.a12579.myapplication.emotion;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.a12579.myapplication.R;

/**
 * Created by 12579 on 2018/img3/21.
 */

public class EmotionFragment extends Fragment implements View.OnClickListener {
    //private Button add_btn;
    private FloatingActionButton add_btn;
    private ListView listView;
    private EmotionDB emotionDB;
    private SQLiteDatabase dbReader;
    private Cursor cursor;
    private SVProgressHUD svProgressHUD;
    public static EmotionFragment newInstance() {
        EmotionFragment fragment = new EmotionFragment();
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_emotion, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        listView = view.findViewById(R.id.list);
        //设置底部菜单不遮挡listview
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 0, 0, 0);
//        listView.setLayoutParams(lp);

        LinearLayout linearLayout = view.findViewById(R.id.linear_btn);
        linearLayout.bringToFront();

        add_btn = view.findViewById(R.id.cell_add);
        add_btn.setOnClickListener(this);
        emotionDB = new EmotionDB(getActivity());
        dbReader = emotionDB.getReadableDatabase();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positon, long l) {
                cursor.moveToPosition(positon);
                Intent i = new Intent(getActivity(),SelectAct.class);
                i.putExtra(EmotionDB.ID,cursor.getInt(cursor.getColumnIndex(EmotionDB.ID)));
                i.putExtra(EmotionDB.CONTENT,cursor.getString(
                        cursor.getColumnIndex(EmotionDB.CONTENT)));
                i.putExtra(EmotionDB.TIME,
                        cursor.getString(cursor.getColumnIndex(EmotionDB.TIME)));
                startActivity(i);
            }
        });
        return view;
    }



    @Override
    public void onClick(View view) {
        Intent i = new Intent(getActivity(), AddContent.class);
        switch (view.getId()){
            case R.id.cell_add:
                startActivity(i);
                break;
        }
    }

    @Override
    public void onResume() {
        svProgressHUD.show();
        super.onResume();
        selectDB();
        svProgressHUD.dismiss();
    }

    private void selectDB() {
        cursor = dbReader.query(EmotionDB.TABLE_NAME,null,null,null,null,null,null);
        EmotionAdapter adapter = new EmotionAdapter(getActivity(),cursor);
        //Context context = getContext();
        listView.setAdapter(adapter);
    }
}
