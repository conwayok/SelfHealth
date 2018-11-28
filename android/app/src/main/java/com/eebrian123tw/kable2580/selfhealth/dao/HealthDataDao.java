package com.eebrian123tw.kable2580.selfhealth.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.eebrian123tw.kable2580.selfhealth.entity.DailyDataModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HealthDataDao {
  private ObjectMapper objectMapper;
  private Context context;
  private static final String DAILY_DATA = "DailyData";

  public HealthDataDao(Context context) {
    objectMapper = new ObjectMapper();
    this.context = context;
  }

  public void saveDailyData(DailyDataModel dailyDataModel) {
    SharedPreferences sharedPref = context.getSharedPreferences(DAILY_DATA, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    try {
      editor.putString(
          dailyDataModel.getDataDate().toString(), objectMapper.writeValueAsString(dailyDataModel));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    editor.apply();
  }

  public List<DailyDataModel> getDailyData(LocalDate startDate, LocalDate endDate) {
    SharedPreferences sharedPref = context.getSharedPreferences(DAILY_DATA, Context.MODE_PRIVATE);

    while (!startDate.equals(endDate.plusDays(1))){

    }

    List<DailyDataModel> dailyDataModelList = new ArrayList<>();

    return dailyDataModelList;
  }
}
