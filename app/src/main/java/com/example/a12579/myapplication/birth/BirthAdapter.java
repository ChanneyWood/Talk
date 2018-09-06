package com.example.a12579.myapplication.birth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a12579.myapplication.R;

import java.util.ArrayList;
import java.util.Map;

import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.FireworksPainter;

/**
 * Created by 12579 on 2018/img4/14.
 */

public class BirthAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Map<String,Object>> list;// = new ArrayList<Map<String, Object>>();
    private LinearLayout linearLayout;

    public BirthAdapter(Context context,ArrayList list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return (list==null)?0:list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.cell_birth,null);
        TextView tvname = linearLayout.findViewById(R.id.cell_birth_name);
        TextView tvtime = linearLayout.findViewById(R.id.cell_birth_time);
        SyncTextPathView syncTextPathView = linearLayout.findViewById(R.id.birth_cell_stpv_2017);

        Map<String,Object> map = list.get(i);


        tvname.setText(map.get("name").toString());
        tvtime.setText(map.get("time").toString());
        syncTextPathView.setText(map.get("text").toString());
        syncTextPathView.setBackgroundColor(Color.parseColor("#b3e5fc") );
        syncTextPathView.setShowPainter(true);
        FireworksPainter fireworksPainter = new FireworksPainter();
        syncTextPathView.setPathPainter(fireworksPainter);
        syncTextPathView.startAnimation(0,1);

        return  linearLayout;
    }
}
