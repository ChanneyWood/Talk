package com.example.a12579.myapplication.emotion;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a12579.myapplication.R;

/**
 * Created by 12579 on 2018/img3/25.
 */

public class EmotionAdapter extends BaseAdapter{

    private Context context;
    private Cursor cursor;
    private LinearLayout linearLayout;

    public EmotionAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(context);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.cell_emotion,null);
        TextView contenttv = linearLayout.findViewById(R.id.tv_content);
        TextView timetv = linearLayout.findViewById(R.id.tv_time);

//        //设置字体
//        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "textstyle/huawenxinwei.TTF");
//        contenttv.setTypeface(typeface);
//        timetv.setTypeface(typeface);

        cursor.moveToPosition(i);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        contenttv.setText(content);
        timetv.setText(time);

        return linearLayout;
    }

}
