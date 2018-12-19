package com.eebrian123tw.kable2580.selfhealth.workerManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eebrian123tw.kable2580.selfhealth.dao.IdleStateDao;

import org.threeten.bp.LocalDateTime;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class IdleWorker extends Worker {

  private static final String TAG = "IdleWorker";

  public IdleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    // this code is run if device is idle
    IdleStateDao idleStateDao = new IdleStateDao(getApplicationContext());
    idleStateDao.setState("idle");
    idleStateDao.setStartTime(LocalDateTime.now());
    Log.i(TAG, LocalDateTime.now().toString());
    Log.d(TAG, "IdleWorker doing work");
    return Worker.Result.success();
  }
}
