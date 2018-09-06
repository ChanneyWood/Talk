package com.example.a12579.myapplication.emotion;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.a12579.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by 12579 on 2018/5/23.
 */

public class EmotionChartFragment extends Fragment {

    private LineChartView lineChart;
//    String[] date = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};//X轴的标注
//    int[] score= {50,42,90,33,70,74,30,18,79,20};//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();
    private EmotionDB emotionDB;
    private SQLiteDatabase dbReader;
    private Cursor cursor;
    private SVProgressHUD svProgressHUD;
    FloatingActionButton add_btn;

    public static EmotionChartFragment newInstance() {
        EmotionChartFragment fragment = new EmotionChartFragment();
        return fragment;
    }

    public EmotionChartFragment(){

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        svProgressHUD = new SVProgressHUD(getContext());
        System.out.println("oncreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emotion_chart, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        System.out.println("create view");
//        System.out.println(date.length);
//        System.out.println(mPointValues.size());
        lineChart = view.findViewById(R.id.line_chart);


        add_btn = view.findViewById(R.id.chart_add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddContent.class);
                startActivity(i);
            }
        });


//        initData();
//
//        getAxisXLables();//获取x轴的标注
//        getAxisPoints();//获取坐标点
//        initLineChart();//初始化

        return view;
    }

//    /**
//     * 设置X 轴的显示
//     */
//    private void getAxisXLables(){
//        for (int i = 0; i < date.length; i++) {
//            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
//        }
//    }
//    /**
//     * 图表的每个点的显示
//     */
//    private void getAxisPoints() {
//        for (int i = 0; i < score.length; i++) {
//            mPointValues.add(new PointValue(i, score[i]));
//        }
//    }

    public void initData(){
        mAxisXValues.clear();
        mPointValues.clear();
        //初始化数据
        emotionDB = new EmotionDB(getActivity());
        dbReader = emotionDB.getReadableDatabase();
        cursor = dbReader.query(EmotionDB.TABLE_NAME,null,null,null,null,null,null);
        for (int i=0;i<cursor.getCount();i++)
        {
            cursor.moveToPosition(i);
            String string = cursor.getString(cursor.getColumnIndex(EmotionDB.TIME));
            int gggg = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EmotionDB.GRADE)));
            mAxisXValues.add(new AxisValue(i).setLabel(string.substring(string.length()-14)));
            mPointValues.add(new PointValue(i,gggg));
        }
    }

    private void initLineChart(){
        //Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        System.out.println("inintchart");
        Line line = new Line(mPointValues).setColor(Color.WHITE).setCubic(false);
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
 //       line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        System.out.println(line);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(7);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        //axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setMaxLabelChars(6);//max label length, for example 60
        List<AxisValue> values = new ArrayList<>();
        for(int i = 0; i < 10; i+= 1){
            AxisValue value = new AxisValue(i);
            String label = "";
            value.setLabel(label+i);
            values.add(value);
        }
        axisY.setValues(values);
        axisY.setTextSize(7);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                cursor.moveToPosition(i1);
                Intent intent = new Intent(getActivity(),SelectAct.class);
                intent.putExtra(EmotionDB.ID,cursor.getInt(cursor.getColumnIndex(EmotionDB.ID)));
                intent.putExtra(EmotionDB.CONTENT,cursor.getString(
                        cursor.getColumnIndex(EmotionDB.CONTENT)));
                intent.putExtra(EmotionDB.TIME,
                        cursor.getString(cursor.getColumnIndex(EmotionDB.TIME)));
                intent.putExtra(EmotionDB.GRADE,
                        cursor.getString(cursor.getColumnIndex(EmotionDB.GRADE)));
                startActivity(intent);
            }

            @Override
            public void onValueDeselected() {

            }
        });
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }




    @Override
    public void onResume() {
        svProgressHUD.show();
        super.onResume();
        initData();
        initLineChart();
        svProgressHUD.dismiss();
    }
}
