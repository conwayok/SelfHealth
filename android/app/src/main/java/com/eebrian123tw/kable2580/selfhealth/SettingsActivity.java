package com.eebrian123tw.kable2580.selfhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.dao.SettingsDao;
import com.eebrian123tw.kable2580.selfhealth.service.entity.SettingsModel;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private Switch showNotificationSwitch;
    private EditText dailyStepsGoal;
    private EditText dailySleepGoal;
    private EditText dailyPhoneUseGoal;
    private EditText dailyWaterGoal;
    private Button applyBtn;
    private Button clearDataBtn;
    private SettingsDao settingsDao;
    private SettingsModel settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        showNotificationSwitch = findViewById(R.id.show_notifications_switch);
        dailyStepsGoal = findViewById(R.id.daily_steps_goal_num);
        dailySleepGoal = findViewById(R.id.daily_sleep_goal_num);
        dailyPhoneUseGoal = findViewById(R.id.daily_phone_use_goal_num);
        dailyWaterGoal = findViewById(R.id.daily_water_goal_num);
        applyBtn = findViewById(R.id.settings_apply_btn);
        clearDataBtn = findViewById(R.id.clear_data_btn);
        settingsDao = new SettingsDao(this);

        try {
            settings = settingsDao.getSettings();
            showNotificationSwitch.setChecked(settings.isShowNotification());
            dailyStepsGoal.setText(Integer.toString(settings.getDailyStepsGoal()));
            dailySleepGoal.setText(Double.toString(settings.getDailySleepHoursGoal()));
            dailyPhoneUseGoal.setText(Double.toString(settings.getDailyPhoneUseHoursGoal()));
            dailyWaterGoal.setText(Integer.toString(settings.getDailyWaterGoal()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        showNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    settings.setShowNotification(isChecked);
                    settingsDao.saveSettings(settings);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });

        applyBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean showNotification = showNotificationSwitch.isChecked();
                        String dailyStepsGoalStr = dailyStepsGoal.getText().toString();
                        String dailySleepGoalStr = dailySleepGoal.getText().toString();
                        String dailyPhoneUseGoalStr = dailyPhoneUseGoal.getText().toString();
                        String dailyWaterGoalStr = dailyWaterGoal.getText().toString();

                        settings = new SettingsModel();
                        settings.setShowNotification(showNotification);
                        settings.setDailyStepsGoal(Integer.parseInt(dailyStepsGoalStr));
                        settings.setDailySleepHoursGoal(Double.parseDouble(dailySleepGoalStr));
                        settings.setDailyPhoneUseHoursGoal(Double.parseDouble(dailyPhoneUseGoalStr));
                        settings.setDailyWaterGoal(Integer.parseInt(dailyWaterGoalStr));

                        Toast.makeText(
                                SettingsActivity.this,
                                "Goals: \nsteps "
                                        + dailyStepsGoalStr
                                        + "\nsleep "
                                        + dailySleepGoalStr
                                        + "\nphoneUse "
                                        + dailyPhoneUseGoalStr
                                        + "\nwater "
                                        + dailyWaterGoalStr,
                                Toast.LENGTH_SHORT)
                                .show();

                        try {
                            settingsDao.saveSettings(settings);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                });

        clearDataBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new HealthDataDao(SettingsActivity.this).deleteDataAll();
                        new SettingsDao(SettingsActivity.this).deleteAll();
                        Toast.makeText(SettingsActivity.this, "cleared all data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
