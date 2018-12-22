package com.eebrian123tw.kable2580.selfhealth.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.eebrian123tw.kable2580.selfhealth.config.Config.DAILY_DATA;

public class HealthDataDao {
  private ObjectMapper objectMapper;
  private SharedPreferences sharedPref;
  private static final String LOG_TAG = "HealthDataDao";

  public HealthDataDao(Context context) {
    objectMapper = new ObjectMapper();
    sharedPref = context.getSharedPreferences(DAILY_DATA, Context.MODE_PRIVATE);
  }

  public void saveDailyData(DailyDataModel dailyDataModel) {
    Log.d(LOG_TAG, "saveDailyData " + dailyDataModel.getDataDate());
    SharedPreferences.Editor editor = sharedPref.edit();
    try {
      editor.putString(
          dailyDataModel.getDataDate(), objectMapper.writeValueAsString(dailyDataModel));
      editor.apply();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void saveDailyData(List<DailyDataModel> dailyDataModelList) {
    Log.d(LOG_TAG, "saveDailyData List size: " + dailyDataModelList.size());

    SharedPreferences.Editor editor = sharedPref.edit();
    for (DailyDataModel dailyDataModel : dailyDataModelList) {
      try {
        editor.putString(
            dailyDataModel.getDataDate(), objectMapper.writeValueAsString(dailyDataModel));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }

    editor.apply();
  }

  public List<DailyDataModel> getDailyData(LocalDate startDate, LocalDate endDate) {
    Log.d(
        LOG_TAG,
        "getDailyData from startDate "
            + startDate.toString()
            + " to endDate "
            + endDate.toString());
    List<DailyDataModel> dailyDataModelList = new ArrayList<>();
    LocalDate localDate = startDate.plusDays(0);
    while (!localDate.equals(endDate.plusDays(1))) {
      String jsonString = sharedPref.getString(localDate.toString(), "");
      if (!jsonString.isEmpty()) {
        try {
          dailyDataModelList.add(objectMapper.readValue(jsonString, DailyDataModel.class));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      localDate = localDate.plusDays(1);
    }

    return dailyDataModelList;
  }

  public List<DailyDataModel> getDailyDataAll() {
    Map<String, ?> allEntries = sharedPref.getAll();
    List<DailyDataModel> allData = new ArrayList<>();
    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
      try {
        allData.add((objectMapper.readValue((String) entry.getValue(), DailyDataModel.class)));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return allData;
  }

  public DailyDataModel getDailyData(LocalDate date) {
    String dateString = date.toString();
    Log.d(LOG_TAG, "getDailyData " + dateString);
    String dataString = sharedPref.getString(dateString, "");
    if (!dataString.isEmpty()) {
      try {
        return objectMapper.readValue(dataString, DailyDataModel.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Log.d(LOG_TAG, "data for date " + date + " is null");
    Log.d(LOG_TAG, "Creating empty DailyDataModel");
    DailyDataModel dailyDataModel = new DailyDataModel();
    dailyDataModel.setDataDate(date.toString());
    saveDailyData(dailyDataModel);
    return dailyDataModel;
  }

  public void deleteData(LocalDate startDate, LocalDate endDate) {
    Log.d(
        LOG_TAG,
        "deleteData from start date "
            + startDate.toString()
            + " to end date "
            + endDate.toString());
    SharedPreferences.Editor editor = sharedPref.edit();
    if (startDate.equals(LocalDate.MIN) && endDate.equals(LocalDate.MAX)) {
      // delete ALL data
      editor.clear();
    } else {
      // delete some data
      while (!startDate.equals(endDate.plusDays(1))) {
        editor.remove(startDate.toString());
        startDate = startDate.plusDays(1);
      }
    }
    editor.apply();
  }

  public void deleteDataAll() {
    Log.d(LOG_TAG, "deleteDataAll");
    SharedPreferences.Editor editor = sharedPref.edit();
    // delete ALL data
    editor.clear().apply();
  }
}
