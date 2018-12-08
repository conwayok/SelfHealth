package com.eebrian123tw.kable2580.selfhealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.dao.SettingsDao;
import com.eebrian123tw.kable2580.selfhealth.service.entity.SettingsModel;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

  private Switch showNotificationSwitch;
  private TextView dailyStepsGoal;
  private Button applyBtn;
  private Button clearDataBtn;
  // todo: add other options to set other goals

  private SettingsModel settings;
  private SettingsDao settingsDao;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    showNotificationSwitch = findViewById(R.id.show_notifications_switch);
    dailyStepsGoal = findViewById(R.id.daily_steps_goal_num);
    applyBtn = findViewById(R.id.settings_apply_btn);
    clearDataBtn = findViewById(R.id.clear_data_btn);

    settingsDao = new SettingsDao(this);

    try {
      settings = settingsDao.getSettings();
      dailyStepsGoal.setText(Integer.toString(settings.getDailyStepsGoal()));

    } catch (IOException e) {
      e.printStackTrace();
    }

    applyBtn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            SettingsModel settingsModel = new SettingsModel();
            settingsModel.setDailyStepsGoal(Integer.parseInt(dailyStepsGoal.getText().toString()));
            // todo: set the other values
            try {
              settingsDao.saveSettings(settingsModel);
            } catch (JsonProcessingException e) {
              e.printStackTrace();
            }
          }
        });

//    clearDataBtn.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        HealthDataDao healthDataDao = new HealthDataDao(SettingsActivity.super.getApplicationContext());
//
//      }
//    });
  }
}
