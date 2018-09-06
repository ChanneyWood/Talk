package com.example.a12579.myapplication.birth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.a12579.myapplication.R;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;


/**
 * Created by 12579 on 2018/img3/21.
 */

public class BirthFragment extends Fragment implements View.OnClickListener {
    FilterMenuLayout layout;
    private BirthAddFragment birthAddFragment;
    private BirthListFragment birthListFragment;
    private BirthHelpFragment birthHelpFragment;

    public static BirthFragment newInstance() {
        BirthFragment fragment = new BirthFragment();
        return fragment;
    }

    public BirthFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birth, container, false);
        layout = view.findViewById(R.id.filter_menu);

        ImageView gezi = view.findViewById(R.id.birth_img_gezi);
        int[] location = new int[2];
        gezi.getLocationInWindow(location);//获取Imageview在屏幕中的位置
        Animation geziTranslate = new TranslateAnimation(location[0]-300f,location[0],location[0]-800f,location[0]);//移动动画
        gezi.setAnimation(geziTranslate);
        geziTranslate.setDuration(3000);
        //hero.setImageDrawable(getResources().getDrawable(R.mipmap.hero_bg));
        geziTranslate.start();



        FilterMenu menu = new FilterMenu.Builder(getContext())
                .addItem(R.drawable.addbirth)
                .addItem(R.drawable.birthlist)
                .addItem(R.drawable.readme)
                .attach(layout)
                .withListener(new FilterMenu.OnMenuChangeListener() {
                    @Override
                    public void onMenuItemClick(View view, int position) {
                        FragmentManager fm = getChildFragmentManager();
                        //开启事务
                        FragmentTransaction transaction = fm.beginTransaction();
                        switch (position){
                            case 0:
                                if (birthAddFragment == null){
                                    birthAddFragment = BirthAddFragment.newInstance();
                                }
                                //Toast.makeText(getActivity(),"birth1",Toast.LENGTH_SHORT).show();
                                transaction.replace(R.id.tmp_layout, birthAddFragment).commit();
                                break;
                            case 1:
                                if (birthListFragment == null){
                                    birthListFragment = BirthListFragment.newInstance();
                                }
                                //Toast.makeText(getActivity(),"birth1",Toast.LENGTH_SHORT).show();
                                transaction.replace(R.id.tmp_layout, birthListFragment).commit();
                                //Toast.makeText(getActivity(),"birth2",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                if (birthHelpFragment == null){
                                    birthHelpFragment = BirthHelpFragment.newInstance();
                                }
                                //Toast.makeText(getActivity(),"birth1",Toast.LENGTH_SHORT).show();
                                transaction.replace(R.id.tmp_layout, birthHelpFragment).commit();
                                break;
                        }
                    }

                    @Override
                    public void onMenuCollapse() {

                    }

                    @Override
                    public void onMenuExpand() {

                    }
                }).build();
        return view;
    }


    @Override
    public void onClick(View view) {

    }
}

