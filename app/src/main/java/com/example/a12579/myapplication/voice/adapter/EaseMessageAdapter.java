package com.example.a12579.myapplication.voice.adapter;


/**
 * Copyright (C) 2017  Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a12579.myapplication.R;
import com.example.a12579.myapplication.voice.model.MessageBean;
import com.example.a12579.myapplication.voice.utils.TimeUtils;
import com.ilike.voicerecorder.utils.CommonUtils;

import java.util.List;
import java.util.Map;


/**
 * desc:   消息适配器
 * author: wangshanhai
 * email: ilikeshatang@gmail.com
 * date: 2017/10/30 18:38
 */
public class EaseMessageAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,Object>> list;

    private onItemClickLister onItemClickLister;


    public EaseMessageAdapter(Context mContext, List<Map<String,Object>> list) {
        this.context = mContext;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.ease_row_sent_voice, parent, false);

            viewHolder.iv_voice = (ImageView) convertView.findViewById(R.id.iv_voice);
            viewHolder.tv_length = (TextView) convertView.findViewById(R.id.tv_length);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            viewHolder.bubble = (RelativeLayout) convertView.findViewById(R.id.bubble);
            viewHolder.bubble_text = (RelativeLayout) convertView.findViewById(R.id.bubble_text);
            viewHolder.tv_chatcontent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            viewHolder.tv_name = convertView.findViewById(R.id.voice_tv_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final Map<String,Object>map = list.get(position);

        viewHolder.bubble_text.setVisibility(View.GONE);
        viewHolder.bubble.setVisibility(View.VISIBLE);
        //更改并显示录音条长度
        RelativeLayout.LayoutParams ps = (RelativeLayout.LayoutParams) viewHolder.bubble.getLayoutParams();
        ps.width = CommonUtils.getVoiceLineWight2(context, Integer.parseInt((String) map.get("length")) );
        viewHolder.bubble.setLayoutParams(ps); //更改语音长条长度

        viewHolder.tv_length.setText(map.get("length")+ "");

        viewHolder.tv_name.setText(map.get("name")+"");

        viewHolder.timestamp.setText(TimeUtils.getTime(Long.parseLong((String) map.get("timestamp")) ));

        viewHolder.iv_voice.setImageResource(R.drawable.ease_chatto_voice_playing);

        viewHolder.bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLister != null) {
                    onItemClickLister.onItemClick(viewHolder.iv_voice, (String) map.get("url"), position);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_voice;
        TextView tv_length;
        TextView timestamp;
        RelativeLayout bubble;
        RelativeLayout bubble_text;
        TextView tv_chatcontent;
        TextView tv_name;
    }

    public interface onItemClickLister {
        void onItemClick(ImageView imageView, String path, int position);
    }

    public void setOnItemClickLister(EaseMessageAdapter.onItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }
}

