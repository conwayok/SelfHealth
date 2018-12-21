package com.eebrian123tw.kable2580.selfhealth.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.eebrian123tw.kable2580.selfhealth.config.Config;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Objects;

import static com.eebrian123tw.kable2580.selfhealth.config.Config.IDLE_TIME;

import static com.eebrian123tw.kable2580.selfhealth.config.Config.PhoneState;

public class IdleStateDao {
  private SharedPreferences sharedPref;

  public IdleStateDao(Context context) {
    sharedPref = context.getSharedPreferences(IDLE_TIME, Context.MODE_PRIVATE);
  }

  public void setState(PhoneState state) {
    sharedPref.edit().putString("state", state.name()).apply();
  }

  public PhoneState getState() {
    return sharedPref.getString("state", PhoneState.ACTIVE.name()).equals(PhoneState.ACTIVE.name())
        ? PhoneState.ACTIVE
        : PhoneState.SCREEN_OFF;
  }

  public void setStartTime(LocalDateTime startTime) {
    sharedPref.edit().putString("startTime", startTime.toString()).apply();
  }

  public LocalDateTime getStartTime() {
    String startTimeString = sharedPref.getString("startTime", "");
    if (!Objects.requireNonNull(startTimeString).isEmpty()) {
      return LocalDateTime.parse(startTimeString);
    } else {
      return LocalDateTime.now();
    }
  }
}
