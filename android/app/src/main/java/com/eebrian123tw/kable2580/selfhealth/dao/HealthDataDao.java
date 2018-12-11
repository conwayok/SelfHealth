package com.eebrian123tw.kable2580.selfhealth.dao;

import android.content.Context;
import android.content.SharedPreferences;

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

  public HealthDataDao(Context context) {
    objectMapper = new ObjectMapper();
    sharedPref = context.getSharedPreferences(DAILY_DATA, Context.MODE_PRIVATE);
  }

  public void saveDailyData(DailyDataModel dailyDataModel) throws JsonProcessingException {
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(dailyDataModel.getDataDate(), objectMapper.writeValueAsString(dailyDataModel));

    editor.apply();
  }

  public void saveDailyData(List<DailyDataModel> dailyDataModelList)
      throws JsonProcessingException {
    SharedPreferences.Editor editor = sharedPref.edit();

    for (DailyDataModel dailyDataModel : dailyDataModelList) {
      editor.putString(
          dailyDataModel.getDataDate(), objectMapper.writeValueAsString(dailyDataModel));
    }

    editor.apply();
  }

  public List<DailyDataModel> getDailyData(LocalDate startDate, LocalDate endDate)
      throws IOException {
    List<DailyDataModel> dailyDataModelList = new ArrayList<>();
    LocalDate localDate = startDate.plusDays(0);
    while (!localDate.equals(endDate.plusDays(1))) {
      String jsonString = sharedPref.getString(localDate.toString(), "");
      if (!jsonString.isEmpty())
        dailyDataModelList.add(objectMapper.readValue(jsonString, DailyDataModel.class));
      localDate = localDate.plusDays(1);
    }

    return dailyDataModelList;
  }

  public List<DailyDataModel> getDailyDataAll() {
    Map<String, ?> allEntries = sharedPref.getAll();
    List<DailyDataModel> allData = new ArrayList<>();
    for (Map.Entry<String, ?> entry : allEntries.entrySet())
      allData.add((DailyDataModel) entry.getValue());
    return allData;
  }

  public void deleteData(LocalDate startDate, LocalDate endDate) {
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
    SharedPreferences.Editor editor = sharedPref.edit();
    // delete ALL data
    editor.clear().apply();
  }
}
