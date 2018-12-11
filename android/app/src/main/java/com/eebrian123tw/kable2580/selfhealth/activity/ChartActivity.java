package com.eebrian123tw.kable2580.selfhealth.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eebrian123tw.kable2580.selfhealth.R;
import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.service.DetailDataUnit;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    public DetailDataUnit.Type type;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        type = (DetailDataUnit.Type) getIntent().getSerializableExtra("type");
        lineChart = findViewById(R.id.detail_line_chart);
        lineChart.setDragEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setScaleXEnabled(true);
        lineChart.setScaleYEnabled(false);

        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setAxisMinValue(0.0f);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setLabelCount(8);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setNoDataText("No Data");
        lineChart.setNoDataTextColor(Color.BLACK);
        lineChart.animateX(1000);



        HealthDataDao healthDataDao = new HealthDataDao(this);
        List<DailyDataModel> dailyDataModelList;
        try {
            dailyDataModelList = healthDataDao.getDailyData(LocalDate.of(2018, 1, 1), LocalDate.now());

            List<Entry> poitList = new ArrayList<>();
            for (int i = 0; i < dailyDataModelList.size(); i++) {
                switch (type){
                    case STEPS:
                        poitList.add(new Entry(i, dailyDataModelList.get(i).getSteps()));
                        break;
                    case DRINK:
                        poitList.add(new Entry(i, dailyDataModelList.get(i).getWaterCC()));
                        break;
                    case PHONE_USE:
                        poitList.add(new Entry(i, (float) dailyDataModelList.get(i).getHoursPhoneUse()));
                        break;
                    case SLEEP:
                        poitList.add(new Entry(i, (float) dailyDataModelList.get(i).getHoursOfSleep()));
                        break;

                }

            }

            LineDataSet dataSet = new LineDataSet(poitList, "data 1");
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet.setHighLightColor(Color.rgb(255,99,71)); // 设置点击某个点时，横竖两条线的颜色
            dataSet.setHighlightLineWidth(3f);

            dataSet.setLineWidth(5f);
            dataSet.setColor(Color.rgb(255,165,0));

            dataSet.setCircleColor(Color.rgb(255,140,0));
            dataSet.setCircleRadius(7f);


            LineData data = new LineData(dataSet);

            lineChart.setMaxVisibleValueCount(10);

            lineChart.setData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
