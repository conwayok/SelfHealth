package com.eebrian123tw.kable2580.selfhealth.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
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
import com.eebrian123tw.kable2580.selfhealth.googleFit.GoogleFitOauth;
import com.eebrian123tw.kable2580.selfhealth.service.entity.SettingsModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch showNotificationSwitch;
    private Switch connectGoogleFitSwitch;

    private TextView dailyStepsGoal;
    private TextView dailySleepGoal;
    private TextView dailyPhoneUseGoal;
    private TextView dailyWaterGoal;

    private TextView heightTextView;
    private TextView weightTextView;

    private SettingsDao settingsDao;
    private SettingsModel settings;
    private LinearLayout stepsGoalLinearLayout;
    private LinearLayout sleepGoalLinearLayout;
    private LinearLayout waterGoalLinearLayout;
    private LinearLayout phoneUseGoalLinearLayout;
    private LinearLayout clearDataLinearLayout;

    private LinearLayout heightLinearLayout;
    private LinearLayout weightLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        showNotificationSwitch = findViewById(R.id.show_notifications_switch);
        connectGoogleFitSwitch = findViewById(R.id.connect_google_fit_switch);
        dailyStepsGoal = findViewById(R.id.daily_steps_goal_num);
        dailySleepGoal = findViewById(R.id.daily_sleep_goal_num);
        dailyPhoneUseGoal = findViewById(R.id.daily_phone_use_goal_num);
        dailyWaterGoal = findViewById(R.id.daily_water_goal_num);

        weightTextView = findViewById(R.id.weight_num);
        heightTextView = findViewById(R.id.height_num);

        clearDataLinearLayout = findViewById(R.id.clear_data_linear_layout);

        heightLinearLayout = findViewById(R.id.height_linear_layout);
        weightLinearLayout = findViewById(R.id.weight_linear_layout);

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
        heightLinearLayout.setOnClickListener(this);
        weightLinearLayout.setOnClickListener(this);
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

        connectGoogleFitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                try {
                    settings.setConnectedToGoogleFit(isChecked);
                    settingsDao.saveSettings(settings);
                    if(isChecked){
                        GoogleFitOauth.oauthRequest(SettingsActivity.this);
                    }else {
                        GoogleFitOauth.disconnectGoogleFit(SettingsActivity.this);
                    }
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
            case R.id.clear_data_linear_layout: {
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
            case R.id.steps_goal_linear_layout: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                edittext.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                | InputType.TYPE_NUMBER_FLAG_SIGNED);
                edittext.setText(Integer.toString(settings.getDailyStepsGoal()));
                edittext.setSelection(edittext.getText().toString().length());
                alert.setTitle(R.string.enter_goal_value);
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
            case R.id.sleep_goal_linear_layout: {
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    final EditText edittext = new EditText(this);
                    edittext.setInputType(
                            InputType.TYPE_CLASS_NUMBER
                                    | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                    | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    edittext.setText(Double.toString(settings.getDailySleepHoursGoal()));
                    edittext.setSelection(edittext.getText().toString().length());

                    alert.setTitle(R.string.enter_goal_value);
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
            case R.id.water_goal_linear_layout: {
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    final EditText edittext = new EditText(this);
                    edittext.setInputType(
                            InputType.TYPE_CLASS_NUMBER
                                    | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                    | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    edittext.setText(Integer.toString(settings.getDailyWaterGoal()));
                    edittext.setSelection(edittext.getText().toString().length());
                    alert.setTitle(R.string.enter_goal_value);
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
            case R.id.phone_use_goal_linear_layout: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                edittext.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                | InputType.TYPE_NUMBER_FLAG_SIGNED);
                edittext.setText(Double.toString(settings.getDailyPhoneUseHoursGoal()));
                edittext.setSelection(edittext.getText().toString().length());
                alert.setTitle(R.string.enter_limit_value);
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

            // <editor-fold defaultstate="collapsed" desc="height">
            case R.id.height_linear_layout: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                edittext.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                | InputType.TYPE_NUMBER_FLAG_SIGNED);
                edittext.setText(Double.toString(settings.getHeight()));
                edittext.setSelection(edittext.getText().toString().length());
                alert.setTitle(R.string.height);
                alert.setView(edittext);
                alert.setPositiveButton(
                        R.string.confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    settings.setHeight(
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

            // <editor-fold defaultstate="collapsed" desc="weight">
            case R.id.weight_linear_layout: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                edittext.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                | InputType.TYPE_NUMBER_FLAG_SIGNED);
                edittext.setText(Double.toString(settings.getWeight()));
                edittext.setSelection(edittext.getText().toString().length());
                alert.setTitle(R.string.weight);
                alert.setView(edittext);
                alert.setPositiveButton(
                        R.string.confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    settings.setWeight(
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


            connectGoogleFitSwitch.setChecked(settings.isConnectedToGoogleFit());
            connectGoogleFitSwitch.setText(connectGoogleFitSwitch.isChecked()?R.string.connected_google_fit:R.string.disconnected_google_fit);

            heightTextView.setText(Double.toString(settings.getHeight()));
            weightTextView.setText(Double.toString(settings.getWeight()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == GoogleFitOauth.REQUEST_GOOLE_FIT_OAUTH_REQUEST_CODE) {
            setSettingsState();
        }
    }
}
