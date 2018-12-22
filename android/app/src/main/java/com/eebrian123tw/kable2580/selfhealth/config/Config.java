package com.eebrian123tw.kable2580.selfhealth.config;

import java.io.Serializable;

public class Config {
  public static final String DAILY_DATA = "DailyData";
  public static final String SETTINGS = "Settings";
  public static final String IDLE_TIME = "IdleTime";
  public static final double MAX_SCREEN_OFF_TIME_BEFORE_SLEEP = 2;
  public static final long NOTIFICATIONS_AUTO_REFRESH_MILLI = 10000;

  public enum PhoneState implements Serializable {
    ACTIVE,
    SCREEN_OFF
  }
}
