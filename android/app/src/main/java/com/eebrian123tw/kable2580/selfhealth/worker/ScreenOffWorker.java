package com.eebrian123tw.kable2580.selfhealth.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.config.Config;
import com.eebrian123tw.kable2580.selfhealth.service.PhoneStateLogic;

import org.threeten.bp.LocalDateTime;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ScreenOffWorker extends Worker {
  private static final String LOG_TAG = "ScreenOffWorker";

  public ScreenOffWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    Log.d(LOG_TAG, "ScreenOffWorker do work");
    PhoneStateLogic phoneStateLogic = new PhoneStateLogic(getApplicationContext());
    phoneStateLogic.handlePhoneState(Config.PhoneState.SCREEN_OFF, LocalDateTime.now());
    return Result.success();
  }
}
