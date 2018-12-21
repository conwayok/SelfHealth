package com.eebrian123tw.kable2580.selfhealth.service;

import android.content.Context;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.config.Config.PhoneState;
import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.dao.PhoneStateDao;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.io.IOException;

public class PhoneStateLogic {
  private static final String LOG_TAG = "PhoneStateLogic";
  private PhoneStateDao phoneStateDao;
  private HealthDataDao healthDataDao;

  public PhoneStateLogic(Context context) {
    phoneStateDao = new PhoneStateDao(context);
    healthDataDao = new HealthDataDao(context);
  }

  // pass in the current time to determine if user was sleeping or using phone...etc
  public void handlePhoneState(PhoneState currentState, LocalDateTime newStartTime) {
    PhoneState prevState = phoneStateDao.getState();

    // only set changes when current state is different
    if (prevState != currentState) {
      LocalDateTime prevStartTime = phoneStateDao.getStartTime();
      phoneStateDao.setState(currentState);
      phoneStateDao.setStartTime(newStartTime);

      // if user turns off screen after using phone
      if (prevState == PhoneState.ACTIVE && currentState == PhoneState.SCREEN_OFF)
        checkPhoneUse(prevStartTime, newStartTime);

      // if phone screen was off but user is now using phone
      else if (prevState == PhoneState.SCREEN_OFF && currentState == PhoneState.ACTIVE)
        checkSleep(prevStartTime, newStartTime);
    }
  }

  private void checkSleep(LocalDateTime prev, LocalDateTime current) {
    Log.d(LOG_TAG, "checkSleep");

    double idleHours = calcHoursInBetween(prev, current);

    // if phone is idle for 2 hours, assume user was sleeping
    if (idleHours > 2) {
      try {
        // add sleep time to today's sleep time
        DailyDataModel today = healthDataDao.getDailyDataSingle(prev.toLocalDate());
        double todaySleepHours = today.getHoursOfSleep();
        Log.d(LOG_TAG, "User was sleeping for " + idleHours);
        today.setHoursOfSleep(todaySleepHours + idleHours);
        healthDataDao.saveDailyData(today);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void checkPhoneUse(LocalDateTime prev, LocalDateTime current) {
    Log.d(LOG_TAG, "checkPhoneUse");

    try {
      DailyDataModel today = healthDataDao.getDailyDataSingle(prev.toLocalDate());
      double todayPhoneUse = today.getHoursPhoneUse();
      double phoneUseHours = calcHoursInBetween(prev, current);
      today.setHoursPhoneUse(todayPhoneUse + phoneUseHours);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private double calcHoursInBetween(LocalDateTime prev, LocalDateTime current) {
    double secondsOneHour = 60 * 60;
    long seconds =
        current.atZone(ZoneId.systemDefault()).toEpochSecond()
            - prev.atZone(ZoneId.systemDefault()).toEpochSecond();
    return ((double) seconds) / secondsOneHour;
  }
}
