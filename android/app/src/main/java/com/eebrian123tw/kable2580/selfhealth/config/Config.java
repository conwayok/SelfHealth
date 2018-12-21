package com.eebrian123tw.kable2580.selfhealth.config;

import java.io.Serializable;

public class Config {
  public static final String DAILY_DATA = "DailyData";
  public static final String SETTINGS = "Settings";
  public static final String IDLE_TIME = "IdleTime";

  public enum PhoneState implements Serializable {
    ACTIVE,
    SCREEN_OFF
  }
}
