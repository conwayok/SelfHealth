package com.eebrian123tw.kable2580.selfhealth.workerManager;

import android.content.Context;
import android.os.PowerManager;
import android.support.annotation.NonNull;

import com.eebrian123tw.kable2580.selfhealth.config.Config;
import com.eebrian123tw.kable2580.selfhealth.service.PhoneStateLogic;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CheckScreenOffWorker extends Worker {
  public CheckScreenOffWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {

    Context appContext = getApplicationContext();
    AndroidThreeTen.init(appContext);

    PhoneStateLogic phoneStateLogic = new PhoneStateLogic(appContext);
    PowerManager pm = (PowerManager) appContext.getSystemService(Context.POWER_SERVICE);

    boolean isScreenOn = false;
    if (pm != null) {
      isScreenOn = pm.isInteractive();
    }

    // if detects screen has been turned off
    if (!isScreenOn) {
      phoneStateLogic.handlePhoneState(Config.PhoneState.SCREEN_OFF, LocalDateTime.now());
    }

    return Result.success();
  }
}
