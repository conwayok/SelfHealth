package com.eebrian123tw.kable2580.selfhealth.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.eebrian123tw.kable2580.selfhealth.service.entity.SettingsModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

import static com.eebrian123tw.kable2580.selfhealth.config.Config.SETTINGS;

public class SettingsDao {
  private ObjectMapper objectMapper;
  private SharedPreferences sharedPref;

  public SettingsDao(Context context) {
    objectMapper = new ObjectMapper();
    sharedPref = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
  }

  public void saveSettings(SettingsModel settingsModel) throws JsonProcessingException {
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(SETTINGS, objectMapper.writeValueAsString(settingsModel));
    editor.apply();
  }

  public SettingsModel getSettings() throws IOException {
    String jsonString = sharedPref.getString(SETTINGS, "");
    if (!Objects.requireNonNull(jsonString).isEmpty())
      return objectMapper.readValue(jsonString, SettingsModel.class);
    else {
      SettingsModel settingsModel = new SettingsModel();
      settingsModel.setDailyStepsGoal(10000);
      settingsModel.setDailySleepHoursGoal(8);
      settingsModel.setDailyPhoneUseHoursGoal(4);
      settingsModel.setDailyWaterGoal(3000);
      settingsModel.setConnectedToGoogleFit(false);
      settingsModel.setShowNotification(true);
      saveSettings(settingsModel);
      return settingsModel;
    }
  }

  public void deleteAll() {
    sharedPref.edit().clear().apply();
  }
}
