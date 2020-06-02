package com.revolve44.windturbine1.ui.home;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revolve44.windturbine1.MainActivity;
import com.revolve44.windturbine1.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import im.dacer.androidcharts.LineView;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ImageView fan;
    ImageView arrow;
    LineView lineView;
    LineChartView lineChart;
//    String[] date2 = {"5-23","5-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","1-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","4-22","9-22","10-22","11-22","zxc"};//X轴的标注
//    Integer[] score2= {74,22,18,79,20,74,20,74,42,90,74,42,90,50,42,90,33,10,74,22,18,79,20,74,22,18,79,20};//图表的数据

    private String[] date2;
    private Integer[] score2;
//    private String[] date2 = {"lol", "POL","lodl", "POwL"};
//    private Integer[] score2= {74,22,18,79};
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    int q=0;
    TextView CurrentPower;
    int CurrentPowerInt = 0 ;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mCatTextView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("Lifecycle Launch ->>>"," Home Fragment ");

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        fan = (ImageView) root.findViewById(R.id.fan);
        arrow =  (ImageView) root.findViewById(R.id.arrow);

        lineChart = (LineChartView)root.findViewById(R.id.line_chart);
        CurrentPower = root.findViewById(R.id.CurrentPower);


        mSwipeRefreshLayout = root.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);

        mSwipeRefreshLayout.getNestedScrollAxes();

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_red_light);

        // get seekbar from view


        Log.d("Recycler view ->Linked", "HashMap");
//        LinkedList<String> Legend = new LinkedList<>();
//        Legend.add("Mango");
//        Legend.add("Papayia");
//        Legend.add("Apple");
//        Legend.add("Watermelon");
//        Legend.add("Mango");
//        Legend.add("Papayia");
//        Legend.add("Apple");
//        Legend.add("Watermelon");
//
//
//        LinkedList<Integer> Value = new LinkedList<>();
//        Value.add(123);
//        Value.add(124);
//        Value.add(125);
//        Value.add(127);
//        Value.add(123);
//        Value.add(124);
//        Value.add(125);
//        Value.add(127);
//        recyclerPostagem = findViewById(R.id.recyclerView);
//
//        /** 3) Set a layout for the recycler view **/
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Layout
//        recyclerPostagem.setLayoutManager( layoutManager);


        /** 4) Create and set an adapter ( I created the class "Adapter" ) **/
//        getAxisPoints();// be first we must initialization variables and legend
//        getAxisXLables();
//        initLineChart();



        rotateFan();
        rotateArrow();

        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRefresh() {
        ((MainActivity) requireActivity()).runforecast();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MasterSave",MODE_PRIVATE);
        CurrentPowerInt = sharedPreferences.getInt("CurPow",CurrentPowerInt);
        makeArray();
        Log.d("Datamap 5 >>>", " score "+ Arrays.toString(score2)+ " Legend "+  Arrays.toString(date2));



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                mSwipeRefreshLayout.setRefreshing(false);// stop refresh


                rotateFan();
                rotateArrow();

                //editor.putInt("CurPow",CurrentPowerInt);
                //solarhoursString = sharedPreferences.getString("solarhours","7");// here was bug

                CurrentPower.setText(""+CurrentPowerInt);

            }
        }, 4000);

    }

    public void makeArray(){
        try{
            Log.d("Lifecycle -> method "," build Graph ");
            //////////////////////////////////////////////////////////////////////////
            //                       GET FROM SHARED PREFERENCES                    //
            //////////////////////////////////////////////////////////////////////////
            LinkedList<String> Legend = new LinkedList<>();
            LinkedList<Integer> Value = new LinkedList<>();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MasterSave", MODE_PRIVATE);


            //>>>>
            Gson gson = new Gson();
            String json = sharedPreferences.getString("legend", null);
            String json2 = sharedPreferences.getString("value", null);
            Type type = new TypeToken<LinkedList<String>>() {}.getType();
            Type type2 = new TypeToken<LinkedList<Integer>>() {}.getType();
            Legend = gson.fromJson(json, type);
            Value = gson.fromJson(json2, type2);

            Log.d("Datamap 3>>>>>", ""+Legend + " and Value >>>" +Value);


            //////////////////////////////////////////////////////////
            //                  initialization                      //
            //////////////////////////////////////////////////////////
            if (Legend.size()>1& Legend.size()<=21 & q<=1) {
                date2 = Legend.toArray(new String[Legend.size()]);
                score2 = Value.toArray(new Integer[Value.size()]);
                //Value.toArray(score2);
                //score2 = Value.toArray();

                getAxisXLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                initLineChart();//初始化
                q++;
                Log.d("Datamap 4>>>>>", ""+date2);
            }


        }catch (Exception e){
            Toast.makeText(getActivity(), "No Internet. Check Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Lifecycle resume ->>>"," Home Fragment ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Lifecycle pause ->>>"," Home Fragment ");
    }

    public void rotateFan() {

        RotateAnimation rotate = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.625f);
        rotate.setDuration(500);
        //from 0,61 to 0,64
        // Bofort scale
//        if (wind == 0){
//            rotate.setDuration(9000);
//        }else if (wind>=1 && wind <= 3) {
//            rotate.setDuration(5000);
//        }else if (wind>=2 && wind <=5){
//            rotate.setDuration(3000);
//        }else if (wind>=6 && wind <= 10){
//            rotate.setDuration(1500);
//        }else if (wind>=11){
//            rotate.setDuration(900);
//        }
//        else if (){
//
//        }
//        rotate.setDuration(1500);

        rotate.setRepeatCount(Animation.INFINITE);

        rotate.setInterpolator(new LinearInterpolator());
        fan.startAnimation(rotate);

    }
    public void rotateArrow() {

        RotateAnimation rotate = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(10000);

        // Bofort scale
//        if (wind == 0){
//            rotate.setDuration(9000);
//        }else if (wind>=1 && wind <= 3) {
//            rotate.setDuration(5000);
//        }else if (wind>=2 && wind <=5){
//            rotate.setDuration(3000);
//        }else if (wind>=6 && wind <= 10){
//            rotate.setDuration(1500);
//        }else if (wind>=11){
//            rotate.setDuration(900);
//        }
//        else if (){
//
//        }
//        rotate.setDuration(1500);
        rotate.setRepeatCount(Animation.INFINITE);

        rotate.setInterpolator(new LinearInterpolator());
        arrow.startAnimation(rotate);
    }


    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#727EC1"));  //ArrayList
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//灰色

//	    axisX.setName("未来几天的天气");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(1); //1<- more fit  // less fit -> 7
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        axisX.setHasTiltedLabels(false);
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setName("power output [watts]");// Y axis name
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 1);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 14;
        lineChart.setCurrentViewport(v);

    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < date2.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date2[i]));
        }

    }
    /**
     * Y
     */
    private void getAxisPoints(){
        for (int i = 0; i < score2.length; i++) {
            mPointValues.add(new PointValue(i, score2[i]));
        }

    }


}
