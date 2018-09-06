package com.example.a12579.myapplication.my;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a12579.myapplication.R;
import com.example.a12579.myapplication.delete.SelectDelete;

/**
 * Created by 12579 on 2018/img3/21.
 */

public class MyFragment extends Fragment{
    private FloatingActionButton btn_out;
    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        Bundle bundle = getArguments();
        String name = bundle.getString("name");
        TextView tvName = view.findViewById(R.id.my_name);
        tvName.setText(name);
        btn_out = view.findViewById(R.id.my_btn_out);
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("userdata", Context.MODE_PRIVATE).edit();
                editor.putBoolean("isLogin",false);
                editor.apply();
                FragmentTransaction fm = getActivity().getFragmentManager().beginTransaction();

                LoginFragment loginFragment = LoginFragment.newInstance("我");
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                fm.replace(R.id.tbb, loginFragment);
                fm.addToBackStack(null);

                // 执行事务
                fm.commit();
            }
        });
        return view;
    }
}
