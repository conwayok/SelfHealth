package com.eebrian123tw.kable2580.selfhealth.workerManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.dao.IdleStateDao;

import org.threeten.bp.LocalDateTime;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ScreenOffWorker extends Worker {

  private static final String TAG = "ScreenOffWorker";


  public ScreenOffWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    IdleStateDao idleStateDao = new IdleStateDao(getApplicationContext());
    idleStateDao.setState("idle");
    idleStateDao.setStartTime(LocalDateTime.now());

    Log.i(TAG, LocalDateTime.now().toString());

    return Worker.Result.success();
  }
}
