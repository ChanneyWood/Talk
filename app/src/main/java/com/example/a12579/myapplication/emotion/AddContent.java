package com.example.a12579.myapplication.emotion;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a12579.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends AppCompatActivity implements View.OnClickListener {

    private Button savebtn,cancelbtn;
    private EditText ettext;
    private RadioGroup emotionGroup;
    private RadioButton emotion1,emotion2,emotion3,emotion4,emotion5,emotion6;
    EmotionDB emotionDB;
    private SQLiteDatabase dbWriter;
    private int grade;
    private boolean ischecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_add_content);

        savebtn = findViewById(R.id.save_btn);
        cancelbtn = findViewById(R.id.cancel_btn);
        ettext = findViewById(R.id.ettext);

        savebtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);

        emotionGroup = findViewById(R.id.emotion_group);
        emotion1 = findViewById(R.id.emotion1);
        emotion2 = findViewById(R.id.emotion2);
        emotion3 = findViewById(R.id.emotion3);
        emotion4 = findViewById(R.id.emotion4);
        emotion5 = findViewById(R.id.emotion5);
        emotion6 = findViewById(R.id.emotion6);
        emotionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (emotion1.isChecked()){
                    grade = 1;
                    ischecked = true;
                }else if (emotion2.isChecked()){
                    grade = 2;
                    ischecked = true;
                }else if (emotion3.isChecked()){
                    grade = 3;
                    ischecked = true;
                }else if (emotion4.isChecked()){
                    grade = 4;
                    ischecked = true;
                }else if (emotion5.isChecked()){
                    grade = 5;
                    ischecked = true;
                }else if (emotion6.isChecked()){
                    grade = 6;
                    ischecked = true;
                }else {
                    ischecked = false;
                }
            }
        });

        emotionDB = EmotionDB.getInstance(this);
        dbWriter = emotionDB.getWritableDatabase();
        requestWriterPermission();
    }



    private void requestWriterPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_btn:
                if (ischecked == false){
                    Toast.makeText(this,"请选择心情",Toast.LENGTH_SHORT).show();
                    return;
                }
                addDB();
                finish();
                break;
            case R.id.cancel_btn:
                finish();
                break;
        }
    }

    private void addDB() {

        ContentValues cv = new ContentValues();
        cv.put(EmotionDB.CONTENT,ettext.getText().toString());
        cv.put(EmotionDB.TIME,getTime());
        cv.put(EmotionDB.GRADE,grade);
        dbWriter.insert(EmotionDB.TABLE_NAME,null,cv);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    public Activity getActivity() {
        return this;
    }
}
