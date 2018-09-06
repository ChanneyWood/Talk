package com.example.a12579.myapplication.birth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a12579.myapplication.R;

/**
 * Created by 12579 on 2018/4/19.
 */

public class BirthHelpFragment extends Fragment{
    public static BirthHelpFragment newInstance() {
        BirthHelpFragment fragment = new BirthHelpFragment();
        return fragment;
    }

    public BirthHelpFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birth_help, container, false);
        ImageView imageView1 = view.findViewById(R.id.my_img1);
        Glide.with(this).load(R.drawable.readme1).into(imageView1);
        ImageView imageView2 = view.findViewById(R.id.my_img2);
        Glide.with(this).load(R.drawable.readme2).into(imageView2);
        return view;
    }
}
