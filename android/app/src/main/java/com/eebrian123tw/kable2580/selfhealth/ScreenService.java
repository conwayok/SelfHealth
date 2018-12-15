package com.eebrian123tw.kable2580.selfhealth;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ScreenService extends Service {
  @Override
  public void onCreate() {
    super.onCreate();
    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    BroadcastReceiver mReceiver = new ScreenReceiver();
    registerReceiver(mReceiver, filter);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
