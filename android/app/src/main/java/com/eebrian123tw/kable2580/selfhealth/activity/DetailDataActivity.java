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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailDataActivity extends AppCompatActivity
    implements View.OnClickListener, DetailDataAdapter.CallBack {

  private static final String TAG = "DetailDataActivity";
  @BindView(R.id.datail_data_recyclerview) RecyclerView detailDataRecyclerView;
  @BindView(R.id.add_button) ImageButton addButton;
  @BindView(R.id.total_textView) TextView totalTextView;
  @BindView(R.id.average_textView) TextView averageTextView;
  @BindView(R.id.summary_linearLayout) LinearLayout summaryLinearLayout;
  private static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");
  private DetailDataUnit.Type type;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_data);
    ButterKnife.bind(this);

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
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("type", type);
        //                ActivityOptionsCompat
        // activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(this,summaryLinearLayout,"summary_linearLayout");
        //                startActivity(intent,activityOptionsCompat.toBundle());
        startActivity(intent);
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
                DailyDataModel dailyDataModel = healthDataDao.getDailyData(localDate);
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
  public void loadData() {
    switch (type) {
      case DRINK:
        break;
      case SLEEP:
        break;
      case STEPS:
        addButton.setVisibility(View.INVISIBLE);
        break;
      case PHONE_USE:
        break;
    }

    final LocalDate start = LocalDate.now().withMonth(1).withDayOfMonth(1);
    final LocalDate end = LocalDate.now();
    final HealthDataCalculator healthDataCalculator = new HealthDataCalculator(this, start, end);

    List<DailyDataModel> dailyDataModelList = healthDataCalculator.getDailyDataModelList();
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
          @SuppressLint("DefaultLocale")
          @Override
          public void run() {
            detailDataRecyclerView.setAdapter(
                new DetailDataAdapter(
                    DetailDataActivity.this, detailData, DetailDataActivity.this));

            switch (type) {
              case STEPS:
                averageTextView.setText(
                    getString(R.string.average)
                        + ": "
                        + decimalFormat.format(healthDataCalculator.getStepsAverage()));
                totalTextView.setText(
                    getString(R.string.total) + ": " + healthDataCalculator.getStepsTotal());
                break;
              case SLEEP:
                averageTextView.setText(
                    getString(R.string.average)
                        + ": "
                        + decimalFormat.format(healthDataCalculator.getSleepAverage()));
                totalTextView.setText(
                    getString(R.string.total)
                        + ": "
                        + decimalFormat.format(healthDataCalculator.getSleepTotal()));
                break;
              case DRINK:
                averageTextView.setText(
                    getString(R.string.average)
                        + ": "
                        + decimalFormat.format(healthDataCalculator.getWaterAverage()));
                totalTextView.setText(
                    getString(R.string.total) + ": " + healthDataCalculator.getWaterTotal());
                break;
              case PHONE_USE:
                averageTextView.setText(
                    getString(R.string.average)
                        + ": "
                        + decimalFormat.format(healthDataCalculator.getPhoneUseAverage()));
                totalTextView.setText(
                    getString(R.string.total)
                        + ": "
                        + decimalFormat.format(healthDataCalculator.getPhoneUseTotal()));
                break;
            }
          }
        });
  }
}
