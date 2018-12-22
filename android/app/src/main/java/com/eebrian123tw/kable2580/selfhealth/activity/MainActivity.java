package com.eebrian123tw.kable2580.selfhealth.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.dao.SettingsDao;
import com.eebrian123tw.kable2580.selfhealth.service.DailyDataNotificationService;
import com.eebrian123tw.kable2580.selfhealth.service.DetailDataUnit;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.eebrian123tw.kable2580.selfhealth.service.entity.SettingsModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.text.DecimalFormat;

import static com.eebrian123tw.kable2580.selfhealth.R.id;
import static com.eebrian123tw.kable2580.selfhealth.R.layout.activity_main;
import static com.eebrian123tw.kable2580.selfhealth.R.string.app_name;
import static com.eebrian123tw.kable2580.selfhealth.R.string.cc_string;
import static com.eebrian123tw.kable2580.selfhealth.R.string.hour_string;
import static com.eebrian123tw.kable2580.selfhealth.R.string.minute_string;
import static com.eebrian123tw.kable2580.selfhealth.R.string.my_today_status;
import static com.eebrian123tw.kable2580.selfhealth.R.string.share_string;
import static com.eebrian123tw.kable2580.selfhealth.R.string.step_string;
import static com.eebrian123tw.kable2580.selfhealth.R.string.today_drink_string;
import static com.eebrian123tw.kable2580.selfhealth.R.string.today_step_string;
import static com.eebrian123tw.kable2580.selfhealth.R.string.today_use_phone_string;
import static com.eebrian123tw.kable2580.selfhealth.R.string.yesterday_sleep_string;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

  private Button shareButton;
  private Button exportImportButton;
  private Button settingsButton;

  private Button stepButton;
  private Button sleepButton;
  private Button drinkButton;
  private Button usePhoneButton;

  private SwipeRefreshLayout swipeRefreshLayout;

  private Handler handler;
  private DailyDataModel dailyDataModel;
  private HealthDataDao healthDataDao;
  private SettingsDao settingsDao;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(activity_main);

    // initialize time zone information
    AndroidThreeTen.init(this);

    shareButton = findViewById(id.share_button);
    exportImportButton = findViewById(id.export_import_button);
    stepButton = findViewById(id.step_button);
    sleepButton = findViewById(id.sleep_button);
    drinkButton = findViewById(id.drink_button);
    usePhoneButton = findViewById(id.use_phone_button);
    swipeRefreshLayout = findViewById(id.swipe_refresh_layout);
    settingsButton = findViewById(id.settings_button);

    shareButton.setOnClickListener(this);
    exportImportButton.setOnClickListener(this);
    stepButton.setOnClickListener(this);
    sleepButton.setOnClickListener(this);
    drinkButton.setOnClickListener(this);
    usePhoneButton.setOnClickListener(this);
    settingsButton.setOnClickListener(this);
    swipeRefreshLayout.setOnRefreshListener(this);

    settingsDao = new SettingsDao(this);

    healthDataDao = new HealthDataDao(MainActivity.this);
//    DailyDataModel dailyDataModel = healthDataDao.getDailyData(LocalDate.now());
    dailyDataModel = healthDataDao.getDailyData(LocalDate.now());
    healthDataDao.saveDailyData(dailyDataModel);

    showDailyData();

    if (!isThisServiceRunning(DailyDataNotificationService.class)) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(new Intent(this, DailyDataNotificationService.class));
      } else {
        startService(new Intent(this, DailyDataNotificationService.class));
      }
    }

    handler = new Handler();
  }

  @Override
  protected void onResume() {
    super.onResume();
    onRefresh();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case id.step_button:
        Intent intent = new Intent(this, DetailDataActivity.class);
        intent.putExtra("type", DetailDataUnit.Type.STEPS);
        startActivity(intent);
        break;
      case id.sleep_button:
        intent = new Intent(this, DetailDataActivity.class);
        intent.putExtra("type", DetailDataUnit.Type.SLEEP);
        startActivity(intent);
        break;
      case id.drink_button:
        intent = new Intent(this, DetailDataActivity.class);
        intent.putExtra("type", DetailDataUnit.Type.DRINK);
        startActivity(intent);
        break;
      case id.use_phone_button:
        intent = new Intent(this, DetailDataActivity.class);
        intent.putExtra("type", DetailDataUnit.Type.PHONE_USE);
        startActivity(intent);
        break;
      case id.share_button:
        intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareSubject = getString(app_name);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
        String shareBody = parseDailyDataToString();
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, getString(share_string)));
        break;

      case id.export_import_button:
        startActivity(new Intent(this, ExportImportActivity.class));
        break;

      case id.settings_button:
        startActivity(new Intent(this, SettingsActivity.class));
        break;
    }
  }

  @SuppressLint("SetTextI18n")
  private void showDailyData() {
    DailyDataModel yesterday = healthDataDao.getDailyData(LocalDate.now().minusDays(1));
    try {
      SettingsModel settingsModel = settingsDao.getSettings();

      if (dailyDataModel.getSteps() < settingsModel.getDailyStepsGoal()) {
        stepButton.setTextColor(Color.RED);

      } else {
        stepButton.setTextColor(Color.GREEN);
      }
      if (dailyDataModel.getWaterCC() < settingsModel.getDailyWaterGoal()) {
        drinkButton.setTextColor(Color.RED);

      } else {
        drinkButton.setTextColor(Color.GREEN);
      }
      if (yesterday.getHoursOfSleep() < settingsModel.getDailySleepHoursGoal()) {
        sleepButton.setTextColor(Color.RED);
      } else {
        sleepButton.setTextColor(Color.GREEN);
      }
      if (dailyDataModel.getHoursPhoneUse() > settingsModel.getDailyPhoneUseHoursGoal()) {
        usePhoneButton.setTextColor(Color.RED);
      } else {
        usePhoneButton.setTextColor(Color.GREEN);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    DecimalFormat formatter = new DecimalFormat("#.#");


    double hoursOfSleep = 0;

    if (yesterday != null) {
      hoursOfSleep = yesterday.getHoursOfSleep();
    }
    if (hoursOfSleep < 1) {
      hoursOfSleep *= 60;
      sleepButton.setText(formatter.format(hoursOfSleep) + getString(minute_string));
    } else {
      sleepButton.setText(formatter.format(hoursOfSleep) + getString(hour_string));
    }

//    sleepButton.setText("0" + getString(hour_string));

    stepButton.setText(dailyDataModel.getSteps() + getString(step_string));

    drinkButton.setText(dailyDataModel.getWaterCC() + getString(cc_string));
    double hoursPhoneUse = dailyDataModel.getHoursPhoneUse();
    if (hoursPhoneUse < 1) {
      hoursPhoneUse *= 60;
      usePhoneButton.setText(formatter.format(hoursPhoneUse) + getString(minute_string));
    } else {
      usePhoneButton.setText(formatter.format(hoursPhoneUse) + getString(hour_string));
    }
  }

  private String parseDailyDataToString() {

    DecimalFormat formatter = new DecimalFormat("#.#");
    double hoursOfSleep = 0;
    String hourOFSleepString;
    DailyDataModel yesterday = healthDataDao.getDailyData(LocalDate.now().minusDays(1));
    if (yesterday != null) {
      hoursOfSleep = yesterday.getHoursOfSleep();
    }
    if (hoursOfSleep < 1) {
      hoursOfSleep *= 60;
      hourOFSleepString = formatter.format(hoursOfSleep) + getString(minute_string);
    } else {
      hourOFSleepString = formatter.format(hoursOfSleep) + getString(hour_string);
    }

    double hoursPhoneUse = dailyDataModel.getHoursPhoneUse();
    String hoursPhoneUseString;
    if (hoursPhoneUse < 1) {
      hoursPhoneUse *= 60;
      hoursPhoneUseString = formatter.format(hoursPhoneUse) + getString(minute_string);
    } else {
      hoursPhoneUseString = formatter.format(hoursPhoneUse) + getString(hour_string);
    }

    return getString(my_today_status)
        + '\n'
        + getString(today_step_string)
        + ": "
        + dailyDataModel.getSteps()
        + getString(step_string)
        + '\n'
        + getString(yesterday_sleep_string)
        + ": "
        + hourOFSleepString
        + '\n'
        + getString(today_drink_string)
        + ": "
        + dailyDataModel.getWaterCC()
        + getString(cc_string)
        + '\n'
        + getString(today_use_phone_string)
        + ": "
        + hoursPhoneUseString;
  }

  @Override
  public void onRefresh() {
    handler.postDelayed(
        new Runnable() {
          @Override
          public void run() {
            healthDataDao = new HealthDataDao(MainActivity.this);
            dailyDataModel = healthDataDao.getDailyData(LocalDate.now());
            showDailyData();
            swipeRefreshLayout.setRefreshing(false);
          }
        },
        1000);
  }

  private boolean isThisServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    if (manager != null) {
      for (ActivityManager.RunningServiceInfo service :
          manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName())) {
          return true;
        }
      }
    }
    return false;
  }
}
