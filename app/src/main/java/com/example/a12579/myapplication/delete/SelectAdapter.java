package com.example.a12579.myapplication.delete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a12579.myapplication.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 12579 on 2018/img4/13.
 */

public class SelectAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Map<String,Object>> list;
    private LinearLayout linearLayout;


    public SelectAdapter(Context context,ArrayList list){
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.cell_delect_comment,null);
        TextView tvtext = linearLayout.findViewById(R.id.select_cell_comment);
        TextView tvtime = linearLayout.findViewById(R.id.select_cell_time);
        TextView tvname = linearLayout.findViewById(R.id.select_cell_name);
        Map<String,Object> map = list.get(i);

        tvtext.setText(map.get("text").toString());
        tvtime.setText(map.get("time").toString());
        tvname.setText(map.get("name").toString());
        return linearLayout;
    }
}
