package com.eebrian123tw.kable2580.selfhealth.service;

import android.content.Context;

import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Data;

@Data
public class HealthDataCalculator {
  private Map<LocalDate, DailyDataModel> dailyDataModelMap;
  private List<DailyDataModel> dailyDataModelList;

  private LocalDate startDate;
  private LocalDate endDate;
  private long days;

  private HealthDataDao healthDataDao;

  public HealthDataCalculator(Context context, LocalDate startDate, LocalDate endDate) {
    healthDataDao = new HealthDataDao(context);
    this.startDate = startDate;
    this.endDate = endDate;
    days = ChronoUnit.DAYS.between(startDate, endDate);
    healthDataDao = new HealthDataDao(context);
    dailyDataModelMap = new HashMap<>();
    try {
      dailyDataModelList = healthDataDao.getDailyData(startDate, endDate);
      for (DailyDataModel model : dailyDataModelList)
        this.dailyDataModelMap.put(model.getDataDate(), model);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int getStepsForDay(LocalDate date) {
    return Objects.requireNonNull(dailyDataModelMap.get(date)).getSteps();
  }

  public double getStepsAverage() {
    int total = 0;
    for (DailyDataModel dailyDataModel : dailyDataModelList) total += dailyDataModel.getSteps();
    return total / days;
  }

  public double getSleepHoursDay(LocalDate date) {
    return Objects.requireNonNull(dailyDataModelMap.get(date)).getHoursOfSleep();
  }

  public double getSleepAverage() {
    int total = 0;
    for (DailyDataModel dailyDataModel : dailyDataModelList)
      total += dailyDataModel.getHoursOfSleep();
    return total / days;
  }

  public int getWaterDay(LocalDate date) {
    return Objects.requireNonNull(dailyDataModelMap.get(date)).getWaterCC();
  }

  public double getWaterAverage() {
    int total = 0;
    for (DailyDataModel dailyDataModel : dailyDataModelList) total += dailyDataModel.getWaterCC();
    return total / days;
  }

  public double getPhoneUseDay(LocalDate date) {
    return Objects.requireNonNull(dailyDataModelMap.get(date)).getHoursPhoneUse();
  }

  public double getPhoneUseAverage() {
    double total = 0;
    for (DailyDataModel dailyDataModel : dailyDataModelList)
      total += dailyDataModel.getHoursPhoneUse();
    return total / days;
  }
}
