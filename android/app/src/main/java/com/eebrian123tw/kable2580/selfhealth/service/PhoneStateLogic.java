package com.eebrian123tw.kable2580.selfhealth.service;

import android.content.Context;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.config.Config.PhoneState;
import com.eebrian123tw.kable2580.selfhealth.dao.HealthDataDao;
import com.eebrian123tw.kable2580.selfhealth.dao.IdleStateDao;
import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.io.IOException;

public class IdleLogic {
  private static final String LOG_TAG = "IdleLogic";
  private IdleStateDao idleStateDao;
  private HealthDataDao healthDataDao;

  public IdleLogic(Context context) {
    idleStateDao = new IdleStateDao(context);
    healthDataDao = new HealthDataDao(context);
  }

  // pass in the current time to determine if user was sleeping or using phone...etc
  public void handlePhoneState(PhoneState currentState, LocalDateTime newStartTime) {
    PhoneState prevState = idleStateDao.getState();

    if (prevState != currentState) {
      LocalDateTime prevStartTime = idleStateDao.getStartTime();
      idleStateDao.setState(currentState);
      idleStateDao.setStartTime(newStartTime);

      // detecting phone use
      if (prevState == PhoneState.ACTIVE && currentState == PhoneState.SCREEN_OFF){
        checkPhoneUse(prevStartTime, newStartTime);
      }

      else if (prevState == PhoneState.IDLE) {
        checkSleep(prevStartTime, newStartTime);
      }
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
