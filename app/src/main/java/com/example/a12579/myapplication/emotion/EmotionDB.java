package com.example.a12579.myapplication.emotion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 12579 on 2018/img3/25.
 */

public class EmotionDB extends SQLiteOpenHelper{


    public static final String TABLE_NAME = "emotions";
    public static final String CONTENT = "content";
    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String GRADE = "grade";
    private static EmotionDB mInstance = null;

    public EmotionDB(Context context) {
        super(context, "emotions", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_NAME +"(" + ID
                +" INTEGER PRIMARY KEY AUTOINCREMENT,"+ CONTENT
                +" TEXT NOT NULL," + GRADE
                +" TEXT NOT NULL," + TIME +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public synchronized static EmotionDB getInstance(Context context){
        if (mInstance == null){
            mInstance = new EmotionDB(context);
        }
        return mInstance;
    }
}
