package com.eebrian123tw.kable2580.selfhealth.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.eebrian123tw.kable2580.selfhealth.R;
import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.dao.SettingsDao;
import com.eebrian123tw.kable2580.selfhealth.service.entity.SettingsModel;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

  private Switch showNotificationSwitch;
  private TextView dailyStepsGoal;
  private TextView dailySleepGoal;
  private TextView dailyPhoneUseGoal;
  private TextView dailyWaterGoal;
  private SettingsDao settingsDao;
  private SettingsModel settings;
  private LinearLayout stepsGoalLinearLayout;
  private LinearLayout sleepGoalLinearLayout;
  private LinearLayout waterGoalLinearLayout;
  private LinearLayout phoneUseGoalLinearLayout;
  private LinearLayout clearDataLinearLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    showNotificationSwitch = findViewById(R.id.show_notifications_switch);
    dailyStepsGoal = findViewById(R.id.daily_steps_goal_num);
    dailySleepGoal = findViewById(R.id.daily_sleep_goal_num);
    dailyPhoneUseGoal = findViewById(R.id.daily_phone_use_goal_num);
    dailyWaterGoal = findViewById(R.id.daily_water_goal_num);
    clearDataLinearLayout = findViewById(R.id.clear_data_linear_layout);

    stepsGoalLinearLayout = findViewById(R.id.steps_goal_linear_layout);
    sleepGoalLinearLayout = findViewById(R.id.sleep_goal_linear_layout);
    waterGoalLinearLayout = findViewById(R.id.water_goal_linear_layout);
    phoneUseGoalLinearLayout = findViewById(R.id.phone_use_goal_linear_layout);

    settingsDao = new SettingsDao(this);

    clearDataLinearLayout.setOnClickListener(this);
    stepsGoalLinearLayout.setOnClickListener(this);
    sleepGoalLinearLayout.setOnClickListener(this);
    waterGoalLinearLayout.setOnClickListener(this);
    phoneUseGoalLinearLayout.setOnClickListener(this);

    setSettingsState();

    showNotificationSwitch.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            try {
              settings.setShowNotification(isChecked);
              settingsDao.saveSettings(settings);
              setSettingsState();
            } catch (JsonProcessingException e) {
              e.printStackTrace();
            }
          }
        });
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
        // <editor-fold defaultstate="collapsed" desc="clear data">
      case R.id.clear_data_linear_layout:
        {
          AlertDialog.Builder alert = new AlertDialog.Builder(this);
          alert.setTitle("Clear Data");
          alert.setMessage("clear settings and info of health");
          alert.setNegativeButton(R.string.dialog_cancel, null);
          alert
              .setPositiveButton(
                  R.string.confirm,
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                      new HealthDataDao(SettingsActivity.this).deleteDataAll();
                      settingsDao.deleteAll();
                      Toast.makeText(SettingsActivity.this, "cleared all data", Toast.LENGTH_SHORT)
                          .show();
                      setSettingsState();
                    }
                  })
              .show();
        }

        break;
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="steps goal">
      case R.id.steps_goal_linear_layout:
        {
          AlertDialog.Builder alert = new AlertDialog.Builder(this);
          final EditText edittext = new EditText(this);
          edittext.setInputType(
              InputType.TYPE_CLASS_NUMBER
                  | InputType.TYPE_NUMBER_FLAG_DECIMAL
                  | InputType.TYPE_NUMBER_FLAG_SIGNED);
          edittext.setText(Integer.toString(settings.getDailyStepsGoal()));
          edittext.setSelection(edittext.getText().toString().length());
          alert.setTitle("Enter a goal value");
          alert.setView(edittext);
          alert.setPositiveButton(
              R.string.confirm,
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                  try {
                    settings.setDailyStepsGoal(Integer.parseInt(edittext.getText().toString()));
                    settingsDao.saveSettings(settings);
                    setSettingsState();
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              });
          alert.show();
        }
        break;
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="sleep goal">
      case R.id.sleep_goal_linear_layout:
        {
          {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            edittext.setInputType(
                InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_NUMBER_FLAG_DECIMAL
                    | InputType.TYPE_NUMBER_FLAG_SIGNED);
            edittext.setText(Double.toString(settings.getDailySleepHoursGoal()));
            edittext.setSelection(edittext.getText().toString().length());

            alert.setTitle("Enter a goal value");
            alert.setView(edittext);
            alert.setPositiveButton(
                R.string.confirm,
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                    try {
                      settings.setDailySleepHoursGoal(
                          Double.parseDouble(edittext.getText().toString()));
                      settingsDao.saveSettings(settings);
                      setSettingsState();
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  }
                });
            alert.show();
          }
        }
        break;
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="water goal">
      case R.id.water_goal_linear_layout:
        {
          {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            edittext.setInputType(
                InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_NUMBER_FLAG_DECIMAL
                    | InputType.TYPE_NUMBER_FLAG_SIGNED);
            edittext.setText(Integer.toString(settings.getDailyWaterGoal()));
            edittext.setSelection(edittext.getText().toString().length());
            alert.setTitle("Enter a goal value");
            alert.setView(edittext);
            alert.setPositiveButton(
                R.string.confirm,
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                    try {
                      settings.setDailyWaterGoal(Integer.parseInt(edittext.getText().toString()));
                      settingsDao.saveSettings(settings);
                      setSettingsState();
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  }
                });
            alert.show();
          }
        }
        break;
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="phone use goal">
      case R.id.phone_use_goal_linear_layout:
        {
          AlertDialog.Builder alert = new AlertDialog.Builder(this);
          final EditText edittext = new EditText(this);
          edittext.setInputType(
              InputType.TYPE_CLASS_NUMBER
                  | InputType.TYPE_NUMBER_FLAG_DECIMAL
                  | InputType.TYPE_NUMBER_FLAG_SIGNED);
          edittext.setText(Double.toString(settings.getDailyPhoneUseHoursGoal()));
          edittext.setSelection(edittext.getText().toString().length());
          alert.setTitle("Enter a goal value");
          alert.setView(edittext);
          alert.setPositiveButton(
              R.string.confirm,
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                  try {
                    settings.setDailyPhoneUseHoursGoal(
                        Double.parseDouble(edittext.getText().toString()));
                    settingsDao.saveSettings(settings);
                    setSettingsState();
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              });
          alert.show();
        }
        break;
        // </editor-fold>
    }
  }

  @SuppressLint("SetTextI18n")
  private void setSettingsState() {
    try {
      settings = settingsDao.getSettings();
      showNotificationSwitch.setChecked(settings.isShowNotification());
      showNotificationSwitch.setText(
          settings.isShowNotification() ? R.string.show_notification : R.string.hide_notification);
      dailyStepsGoal.setText(Integer.toString(settings.getDailyStepsGoal()));
      dailySleepGoal.setText(Double.toString(settings.getDailySleepHoursGoal()));
      dailyPhoneUseGoal.setText(Double.toString(settings.getDailyPhoneUseHoursGoal()));
      dailyWaterGoal.setText(Integer.toString(settings.getDailyWaterGoal()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
