package com.eebrian123tw.kable2580.selfhealth.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.config.Config;
import com.eebrian123tw.kable2580.selfhealth.service.PhoneStateLogic;

import org.threeten.bp.LocalDateTime;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UserPresentWorker extends Worker {
  private static final String TAG = "UserPresentWorker";

  public UserPresentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    Log.d(TAG, "UserPresentWorker doWork");
    LocalDateTime now = LocalDateTime.now();
    PhoneStateLogic phoneStateLogic = new PhoneStateLogic(getApplicationContext());
    phoneStateLogic.handlePhoneState(Config.PhoneState.ACTIVE, now);
    Log.i(TAG, "Setting phone state to " + Config.PhoneState.ACTIVE.name());
    Log.i(TAG, "Current time: " + now);
    return Worker.Result.success();
  }
}
