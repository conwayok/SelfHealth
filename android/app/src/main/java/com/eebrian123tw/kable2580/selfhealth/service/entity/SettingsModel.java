package com.eebrian123tw.kable2580.selfhealth.service.entity;

import lombok.Data;

@Data
public class SettingsModel {
  private boolean showNotification;
  private int dailyStepsGoal;
  private double dailySleepHoursGoal;
  private double dailyPhoneUseHoursGoal;
  private int dailyWaterIntakeGoal;
}
