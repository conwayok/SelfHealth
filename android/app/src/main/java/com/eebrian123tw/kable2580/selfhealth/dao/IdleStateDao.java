package com.eebrian123tw.kable2580.selfhealth.dao;

import android.content.Context;
import android.content.SharedPreferences;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Objects;

import static com.eebrian123tw.kable2580.selfhealth.config.Config.IDLE_TIME;

public class IdleStateDao {
  private SharedPreferences sharedPref;

  public IdleStateDao(Context context) {
    sharedPref = context.getSharedPreferences(IDLE_TIME, Context.MODE_PRIVATE);
  }

  public void setState(String state) {
    sharedPref.edit().putString("state", state).apply();
  }

  public String getState() {
    return sharedPref.getString("state", "notIdle");
  }

  public void setStartTime(LocalDateTime startTime) {
    sharedPref.edit().putString("startTime", startTime.toString()).apply();
  }

  public LocalDateTime getStartTime() {
    String startTimeString = sharedPref.getString("startTime", "");
    if (!Objects.requireNonNull(startTimeString).isEmpty()) {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd Hh-mm:ss");
      return LocalDateTime.parse(startTimeString, dateTimeFormatter);
    } else {
      return LocalDateTime.now();
    }
  }
}
