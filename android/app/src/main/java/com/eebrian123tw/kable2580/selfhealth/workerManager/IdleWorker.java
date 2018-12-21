//package com.eebrian123tw.kable2580.selfhealth.workerManager;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.eebrian123tw.kable2580.selfhealth.config.Config;
//import com.eebrian123tw.kable2580.selfhealth.dao.IdleStateDao;
//
//import org.threeten.bp.LocalDateTime;
//
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//public class IdleWorker extends Worker {
//
//  private static final String TAG = "IdleWorker";
//
//  public IdleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//    super(context, workerParams);
//  }
//
//  @NonNull
//  @Override
//  public Result doWork() {
//    // this code is run if device is idle
//    LocalDateTime now = LocalDateTime.now();
//    IdleStateDao idleStateDao = new IdleStateDao(getApplicationContext());
//
//    // if device becomes idle, set state to idle and start time to now
//    Log.i(TAG, "Setting device state to idle");
//    idleStateDao.setState(Config.PhoneState.IDLE);
//    idleStateDao.setStartTime(now);
//
//    Log.i(TAG, "Current time: " + now);
//    Log.d(TAG, "IdleWorker doing work");
//    return Worker.Result.success();
//  }
//}
