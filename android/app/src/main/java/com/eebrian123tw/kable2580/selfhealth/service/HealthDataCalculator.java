package com.eebrian123tw.kable2580.selfhealth.service;

import android.content.Context;

import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;

import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Data;

@Data
public class HealthDataCalculator {
  private Map<String, DailyDataModel> dailyDataModelMap;
  private List<DailyDataModel> dailyDataModelList;

  private LocalDate startDate;
  private LocalDate endDate;
  private long days;

  private HealthDataDao healthDataDao;

  public HealthDataCalculator(Context context, LocalDate startDate, LocalDate endDate) {
    healthDataDao = new HealthDataDao(context);
    this.startDate = startDate;
    this.endDate = endDate;
    healthDataDao = new HealthDataDao(context);
    dailyDataModelMap = new HashMap<>();
    dailyDataModelList = healthDataDao.getDailyData(startDate, endDate);
    for (DailyDataModel model : dailyDataModelList)
      this.dailyDataModelMap.put(model.getDataDate(), model);
    days = dailyDataModelMap.size();
  }

  public int getStepsForDay(LocalDate date) {
    return Objects.requireNonNull(dailyDataModelMap.get(date.toString())).getSteps();
  }

  public int getStepsTotal() {
    int total = 0;
    for (DailyDataModel dailyDataModel : dailyDataModelList) total += dailyDataModel.getSteps();
    return total;
  }

  public double getStepsAverage() {
    return getStepsTotal() / days;
  }

  public double getSleepHoursDay(LocalDate date) {
    return Objects.requireNonNull(dailyDataModelMap.get(date.toString())).getHoursOfSleep();
  }

  public double getSleepTotal() {
    double total = 0;
    for (DailyDataModel dailyDataModel : dailyDataModelList)
      total += dailyDataModel.getHoursOfSleep();
    return total;
  }

  public double getSleepAverage() {
    return getSleepTotal() / days;
  }

  public int getWaterDay(LocalDate date) {
    return Objects.requireNonNull(dailyDataModelMap.get(date.toString())).getWaterCC();
  }

  public int getWaterTotal() {
    int total = 0;
    for (DailyDataModel dailyDataModel : dailyDataModelList) total += dailyDataModel.getWaterCC();
    return total;
  }

  public double getWaterAverage() {
    return getWaterTotal() / days;
  }

  public double getPhoneUseDay(LocalDate date) {
    return Objects.requireNonNull(dailyDataModelMap.get(date.toString())).getHoursPhoneUse();
  }

  public double getPhoneUseTotal() {
    double total = 0;
    for (DailyDataModel dailyDataModel : dailyDataModelList)
      total += dailyDataModel.getHoursPhoneUse();
    return total;
  }

  public double getPhoneUseAverage() {
    return getPhoneUseTotal() / days;
  }
}
