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
 * Created by 12579 on 2018/img4/img6.
 */

public class DeleteAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Map<String,Object>> list;// = new ArrayList<Map<String, Object>>();
    private LinearLayout linearLayout;

    public DeleteAdapter(Context context,ArrayList list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return (list==null||list.size()==0)?0:list.size();
    }

    @Override
    public Object getItem(int i) {
        if (list!=null&&list.size()>0)
            return list.get(i);
        else
            return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        ViewHolder viewHolder = null;
//        if (view == null){
//            viewHolder = new ViewHolder();
//
//
//        }
        LayoutInflater inflater = LayoutInflater.from(context);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.cell_delect,null);
        TextView tvname = linearLayout.findViewById(R.id.tv_name);
        TextView tvtime = linearLayout.findViewById(R.id.tv_time);
        TextView tvtitle = linearLayout.findViewById(R.id.tv_title);
        TextView tvtext = linearLayout.findViewById(R.id.tv_text);
        TextView tvagree = linearLayout.findViewById(R.id.tv_agree);
        TextView tvdisagree = linearLayout.findViewById(R.id.tv_disagree);
        TextView tvcomment = linearLayout.findViewById(R.id.tv_comment);

        Map<String,Object> map = list.get(i);


        tvname.setText(map.get("name").toString()+"发表于");
        tvtime.setText(map.get("time").toString());
        tvtitle.setText(map.get("title").toString());
        tvtext.setText(map.get("text").toString());
        tvagree.setText(map.get("agree").toString()+"赞同");
        tvdisagree.setText(map.get("disagree").toString()+"反对");
        tvcomment.setText(map.get("comment_num").toString()+"评论");

        return  linearLayout;
    }



//    final static class ViewHolder {
//        TextView name;
//        TextView time;
//        TextView title;
//        TextView text;
//        TextView agree;
//        TextView disagree;
//        TextView comment;
//    }
}
