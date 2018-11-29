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

public class HealthDataDao {
  private ObjectMapper objectMapper;
  private static final String DAILY_DATA = "DailyData";
  private SharedPreferences sharedPref;

  public HealthDataDao(Context context) {
    objectMapper = new ObjectMapper();
    sharedPref = context.getSharedPreferences(DAILY_DATA, Context.MODE_PRIVATE);
  }

  public void saveDailyData(DailyDataModel dailyDataModel) throws JsonProcessingException {
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(
        dailyDataModel.getDataDate().toString(), objectMapper.writeValueAsString(dailyDataModel));

    editor.apply();
  }

  public void saveDailyData(List<DailyDataModel> dailyDataModelList)
      throws JsonProcessingException {
    SharedPreferences.Editor editor = sharedPref.edit();

    for (DailyDataModel dailyDataModel : dailyDataModelList) {
      editor.putString(
          dailyDataModel.getDataDate().toString(), objectMapper.writeValueAsString(dailyDataModel));
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
}
