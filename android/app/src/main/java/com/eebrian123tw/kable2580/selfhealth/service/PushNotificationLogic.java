package com.eebrian123tw.kable2580.selfhealth.service;

import android.content.Context;

import org.threeten.bp.LocalDate;

public class PushNotificationLogic {

  private Context appContext;
  private HealthDataCalculator healthDataCalculator;

  public PushNotificationLogic(Context context) {
    this.appContext = context;

    // health data from past week
    healthDataCalculator =
        new HealthDataCalculator(context, LocalDate.now(), LocalDate.now().minusDays(7));
  }

  public void checkPushNotification() {
    /*
     * if any health data is worse than average
     * if any health data average is not healthy
     *
     * todo: add more rules
     * */

    LocalDate today = LocalDate.now();

    if (healthDataCalculator.getStepsForDay(today) < healthDataCalculator.getStepsAverage()) {
      // steps not enough notification
    }
    if (healthDataCalculator.getSleepHoursDay(today) < healthDataCalculator.getSleepAverage()) {
      // sleep not enough notification
    }
    if (healthDataCalculator.getPhoneUseDay(today) > healthDataCalculator.getPhoneUseAverage()) {
      // use to much phone notification
    }
    if (healthDataCalculator.getWaterDay(today) < healthDataCalculator.getWaterAverage()) {
      // drink not enough water notification
    }
  }
}
