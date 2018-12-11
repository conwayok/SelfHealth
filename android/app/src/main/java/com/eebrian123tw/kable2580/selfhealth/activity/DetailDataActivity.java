package com.eebrian123tw.kable2580.selfhealth.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eebrian123tw.kable2580.selfhealth.R;
import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.service.DetailDataAdapter;
import com.eebrian123tw.kable2580.selfhealth.service.DetailDataUnit;
import com.eebrian123tw.kable2580.selfhealth.service.HealthDataCalculator;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DetailDataActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DetailDataActivity";
    private RecyclerView detailDataRecyclerView;
    private Button addButton;
    private DetailDataUnit.Type type;
    private TextView totalTextView;
    private TextView averageTextView;
    private LinearLayout summaryLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);

        addButton = findViewById(R.id.add_button);
        totalTextView = findViewById(R.id.total_textview);
        averageTextView = findViewById(R.id.average_textview);
        summaryLinearLayout = findViewById(R.id.summary_linearLayout);

        detailDataRecyclerView = findViewById(R.id.datail_data_recyclerview);
        detailDataRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        addButton.setOnClickListener(this);
        summaryLinearLayout.setOnClickListener(this);

        Intent intent = getIntent();
        type = (DetailDataUnit.Type) intent.getSerializableExtra("type");

        //    healthDataCalculator.get

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                addData();
                break;
            case R.id.summary_linearLayout:
                break;
        }
    }

    private void addData() {
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                final LocalDate localDate = LocalDate.of(year, month + 1, day);
                                final HealthDataDao healthDataDao = new HealthDataDao(DetailDataActivity.this);
                                DailyDataModel dailyDataModel;
                                try {
                                    List<DailyDataModel> dailyDataModelList =
                                            healthDataDao.getDailyData(localDate, localDate);
                                    if (dailyDataModelList.size() == 0) {
                                        dailyDataModel = new DailyDataModel();
                                    } else {
                                        dailyDataModel = dailyDataModelList.get(0);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    dailyDataModel = new DailyDataModel();
                                }

                                dailyDataModel.setDataDate(localDate.toString());

                                AlertDialog.Builder alert = new AlertDialog.Builder(DetailDataActivity.this);
                                final EditText edittext = new EditText(DetailDataActivity.this);
                                edittext.setInputType(
                                        InputType.TYPE_CLASS_NUMBER
                                                | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                                | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                switch (type) {
                                    case STEPS:
                                        alert.setMessage(getString(R.string.step));
                                        edittext.setText(dailyDataModel.getSteps() + "");
                                        break;
                                    case SLEEP:
                                        alert.setMessage(getString(R.string.sleep));
                                        edittext.setText(dailyDataModel.getHoursOfSleep() + "");
                                        break;
                                    case DRINK:
                                        alert.setMessage(getString(R.string.drink));
                                        edittext.setText(dailyDataModel.getWaterCC() + "");

                                        break;
                                    case PHONE_USE:
                                        alert.setMessage(getString(R.string.phone_use));
                                        edittext.setText(dailyDataModel.getHoursPhoneUse() + "");

                                        break;
                                }
                                edittext.setSelection(edittext.getText().toString().length());
                                alert.setTitle(localDate.toString());
                                alert.setView(edittext);
                                final DailyDataModel finalDailyDataModel = dailyDataModel;
                                alert.setPositiveButton(
                                        getText(R.string.confirm),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                try {

                                                    switch (type) {
                                                        case STEPS:
                                                            finalDailyDataModel.setSteps(
                                                                    Integer.parseInt(edittext.getText().toString()));
                                                            break;
                                                        case SLEEP:
                                                            finalDailyDataModel.setHoursOfSleep(
                                                                    Double.parseDouble(edittext.getText().toString()));
                                                            break;
                                                        case DRINK:
                                                            finalDailyDataModel.setWaterCC(
                                                                    Integer.parseInt(edittext.getText().toString()));
                                                            break;
                                                        case PHONE_USE:
                                                            finalDailyDataModel.setHoursPhoneUse(
                                                                    Double.parseDouble(edittext.getText().toString()));
                                                            break;
                                                    }
                                                    healthDataDao.saveDailyData(finalDailyDataModel);
                                                    loadData();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                alert.show();
                            }
                        },
                        LocalDate.now().getYear(),
                        LocalDate.now().getMonthValue() - 1,
                        LocalDate.now().getDayOfMonth());
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {

        LocalDate start = LocalDate.of(2018, 1, 1);
        LocalDate end = LocalDate.now();
        HealthDataDao healthDataDao = new HealthDataDao(this);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            List<DailyDataModel> dailyDataModelList = healthDataDao.getDailyData(start, end);
            Log.i(TAG, dailyDataModelList.size() + "");
            final List<DetailDataUnit> detailData = new ArrayList<>();
            Collections.reverse(dailyDataModelList);
            for (DailyDataModel dailyDataModel : dailyDataModelList) {
                double value = 0;
                switch (type) {
                    case STEPS:
                        value = dailyDataModel.getSteps();
                        break;
                    case SLEEP:
                        value = dailyDataModel.getHoursOfSleep();
                        break;
                    case DRINK:
                        value = dailyDataModel.getWaterCC();
                        break;
                    case PHONE_USE:
                        value = dailyDataModel.getHoursPhoneUse();
                        break;
                }
                LocalDate localDate = LocalDate.parse(dailyDataModel.getDataDate(), dateTimeFormatter);
                detailData.add(new DetailDataUnit(type, value, localDate));
            }

            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            detailDataRecyclerView.setAdapter(
                                    new DetailDataAdapter(DetailDataActivity.this, detailData));
                        }
                    });

            HealthDataCalculator healthDataCalculator = new HealthDataCalculator(this, start, end);

            switch (type) {
                case STEPS:
                    averageTextView.setText(getString(R.string.average)+": " + healthDataCalculator.getStepsAverage());
                    totalTextView.setText(getString(R.string.total)+": " + healthDataCalculator.getStepsTotal());
                    break;
                case SLEEP:
                    averageTextView.setText(getString(R.string.average)+": " + healthDataCalculator.getSleepAverage());
                    totalTextView.setText(getString(R.string.total) +": "+ healthDataCalculator.getSleepTotal());
                    break;
                case DRINK:
                    averageTextView.setText(getString(R.string.average)+": " + healthDataCalculator.getWaterAverage());
                    totalTextView.setText(getString(R.string.total) +": "+ healthDataCalculator.getWaterTotal());
                    break;
                case PHONE_USE:
                    averageTextView.setText(getString(R.string.average) +": "+ healthDataCalculator.getPhoneUseAverage());
                    totalTextView.setText(getString(R.string.total)+": " + healthDataCalculator.getPhoneUseTotal());
                    break;
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
