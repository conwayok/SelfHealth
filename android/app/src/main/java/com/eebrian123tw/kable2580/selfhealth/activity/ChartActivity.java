package com.eebrian123tw.kable2580.selfhealth.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.eebrian123tw.kable2580.selfhealth.R;
import com.eebrian123tw.kable2580.selfhealth.dao.SettingsDao;
import com.eebrian123tw.kable2580.selfhealth.service.DetailDataUnit;
import com.eebrian123tw.kable2580.selfhealth.service.HealthDataCalculator;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.eebrian123tw.kable2580.selfhealth.service.entity.SettingsModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends AppCompatActivity {
    public DetailDataUnit.Type type;
    @BindView(R.id.detail_line_chart) LineChart lineChart;
    @BindView(R.id.total_textView)  TextView totalTextView;
    @BindView(R.id.average_textView) TextView averageTextView;
    @BindView(R.id.goal_textView) TextView goalTextView;
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
        type = (DetailDataUnit.Type) getIntent().getSerializableExtra("type");


        lineChart.setDragEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);

        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setAxisMinValue(0.0f);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setNoDataText("No Data");
        lineChart.setNoDataTextColor(Color.BLACK);
        lineChart.animateX(300);


        final HealthDataCalculator healthDataCalculator = new HealthDataCalculator(this, LocalDate.of(2018, 1, 1), LocalDate.now());

        final List<DailyDataModel> dailyDataModelList;
        dailyDataModelList = healthDataCalculator.getDailyDataModelList();

        List<Entry> poitList = new ArrayList<>();
        for (int i = 0; i < dailyDataModelList.size(); i++) {
            switch (type) {
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

        LineDataSet dataSet = new LineDataSet(poitList, "");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(10f);

        dataSet.setHighLightColor(Color.rgb(255, 99, 71));
        dataSet.setHighlightLineWidth(3f);
        dataSet.setHighlightEnabled(false);

        dataSet.setLineWidth(5f);
        dataSet.setColor(Color.rgb(255, 165, 0));

        dataSet.setCircleColor(Color.rgb(255, 140, 0));
        dataSet.setCircleRadius(7f);



        LineData data = new LineData(dataSet);
        lineChart.setMaxVisibleValueCount(10);
        if (dailyDataModelList.size() >= 10) {
            lineChart.getViewPortHandler().getMatrixTouch().postScale(5f, 1f);
        }
        lineChart.setData(data);
        lineChart.moveViewToX(dailyDataModelList.size());


        float average = 0;
        switch (type) {
            case STEPS:
                average = (float) healthDataCalculator.getStepsAverage();
                break;
            case DRINK:
                average = (float) healthDataCalculator.getWaterAverage();
                break;
            case PHONE_USE:
                average = (float) healthDataCalculator.getPhoneUseAverage();
                break;
            case SLEEP:
                average = (float) healthDataCalculator.getSleepAverage();
                break;

        }
        LimitLine yLimitLine = new LimitLine(average, getString(R.string.average) + ": " + average);
        yLimitLine.enableDashedLine(10f,10f,0f);
        yLimitLine.setLineColor(Color.RED);
        yLimitLine.setTextColor(Color.BLACK);
        yLimitLine.setLineWidth(2f);
        yLimitLine.setTextSize(10f);
        lineChart.getAxisLeft().addLimitLine(yLimitLine);

        lineChart.getXAxis().setGranularity(1);
        lineChart.getXAxis().setLabelCount(dailyDataModelList.size()>9?9:dailyDataModelList.size());
        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
               @Override
               public String getFormattedValue(float value, AxisBase axis) {

                   int index = Math.round(value);

                   if (index < 0 || index >= dailyDataModelList.size() )
                       return "";

                   String date = dailyDataModelList.get(index).getDataDate();
                   LocalDate localDate = LocalDate.parse(date);
                   return localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
               }
           }


        );

        SettingsModel settings = null;

        try {
            settings = new SettingsDao(this).getSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }


        switch (type) {
            case STEPS:
                if (settings != null) {
                    goalTextView.setText(getText(R.string.goal) + ": " + settings.getDailyStepsGoal());
                }
                averageTextView.setText(getString(R.string.average) + ": " + decimalFormat.format(healthDataCalculator.getStepsAverage()));
                totalTextView.setText(getString(R.string.total) + ": " + healthDataCalculator.getStepsTotal());
                break;
            case SLEEP:
                if (settings != null) {
                    goalTextView.setText(getText(R.string.goal) + ": " + settings.getDailySleepHoursGoal());
                }
                averageTextView.setText(getString(R.string.average) + ": " + decimalFormat.format(healthDataCalculator.getSleepAverage()));
                totalTextView.setText(getString(R.string.total) + ": " + decimalFormat.format(healthDataCalculator.getSleepTotal()));
                break;
            case DRINK:
                if (settings != null) {
                    goalTextView.setText(getText(R.string.goal) + ": " + settings.getDailyWaterGoal());
                }
                averageTextView.setText(getString(R.string.average) + ": " + decimalFormat.format(healthDataCalculator.getWaterAverage()));
                totalTextView.setText(getString(R.string.total) + ": " + healthDataCalculator.getWaterTotal());
                break;
            case PHONE_USE:
                if (settings != null) {
                    goalTextView.setText(getText(R.string.limit) + ": " + settings.getDailyPhoneUseHoursGoal());
                }
                averageTextView.setText(getString(R.string.average) + ": " + decimalFormat.format(healthDataCalculator.getPhoneUseAverage()));
                totalTextView.setText(getString(R.string.total) + ": " + decimalFormat.format(healthDataCalculator.getPhoneUseTotal()));
                break;
        }


    }
}
