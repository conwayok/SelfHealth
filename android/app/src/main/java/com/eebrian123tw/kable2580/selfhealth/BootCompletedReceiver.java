package com.eebrian123tw.kable2580.selfhealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.eebrian123tw.kable2580.selfhealth.service.DailyDataNotificationService;

public class BootCompletedReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
      Toast.makeText(context, "start service after boot", Toast.LENGTH_SHORT).show();
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        context.startForegroundService(new Intent(context, DailyDataNotificationService.class));
      else context.startService(new Intent(context, DailyDataNotificationService.class));
    }
  }
}
