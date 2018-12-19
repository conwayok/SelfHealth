package com.eebrian123tw.kable2580.selfhealth.service;

import android.content.Context;

import com.eebrian123tw.kable2580.selfhealth.dao.IdleStateDao;

import org.threeten.bp.LocalDateTime;

public class IdleLogic {
  private IdleStateDao idleStateDao;

  public IdleLogic(Context context) {
    idleStateDao = new IdleStateDao(context);
  }

  // pass in the current time to determine if user was sleeping or using phone...etc
  public void handlePhoneState(String state, LocalDateTime localDateTime) {
    String prevState = idleStateDao.getState();
    LocalDateTime prevStartTime = idleStateDao.getStartTime();
    idleStateDao.setState(state);
    idleStateDao.setStartTime(localDateTime);
    // todo: add logic
    // todo: if phone is idle for 2 hours, assume user is sleeping
  }
}
