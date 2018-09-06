package com.example.a12579.myapplication.emotion;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.a12579.myapplication.R;

/**
 * Created by 12579 on 2018/img3/25.
 */

public class SelectAct extends Activity implements View.OnClickListener{

    private Button s_delete,s_back;
    private TextView s_tv;
    private RadioButton emotion1,emotion2,emotion3,emotion4,emotion5,emotion6;
    private EmotionDB emotionDB;
    private SQLiteDatabase dbWriter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_emotion_layout);
        s_delete = (Button) findViewById(R.id.s_delete);
        s_back = (Button) findViewById(R.id.s_back);

        emotion1 = findViewById(R.id.select_emotion1);
        emotion2 = findViewById(R.id.select_emotion2);
        emotion3 = findViewById(R.id.select_emotion3);
        emotion4 = findViewById(R.id.select_emotion4);
        emotion5 = findViewById(R.id.select_emotion5);
        emotion6 = findViewById(R.id.select_emotion6);

        switch (getIntent().getStringExtra(EmotionDB.GRADE)){
            case "1":
                emotion1.setVisibility(View.VISIBLE);
                break;
            case "2":
                emotion2.setVisibility(View.VISIBLE);
                break;
            case "3":
                emotion3.setVisibility(View.VISIBLE);
                break;
            case "4":
                emotion4.setVisibility(View.VISIBLE);
                break;
            case "5":
                emotion5.setVisibility(View.VISIBLE);
                break;
            case "6":
                emotion6.setVisibility(View.VISIBLE);
                break;
        }
        s_tv = (TextView) findViewById(R.id.s_tv);
        emotionDB = EmotionDB.getInstance(this);
        dbWriter = emotionDB.getWritableDatabase();
        s_back.setOnClickListener(this);
        s_delete.setOnClickListener(this);
        s_tv.setText(getIntent().getStringExtra(EmotionDB.CONTENT));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.s_delete:
                deleteDate();
                finish();
                break;

            case R.id.s_back:
                finish();
                break;
        }
    }

    private void deleteDate() {
        dbWriter.delete(EmotionDB.TABLE_NAME,
                "_id=" + getIntent().getIntExtra(EmotionDB.ID, 0), null);
    }
}
