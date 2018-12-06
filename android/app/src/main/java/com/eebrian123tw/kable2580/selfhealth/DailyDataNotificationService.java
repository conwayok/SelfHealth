package com.eebrian123tw.kable2580.selfhealth;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DailyDataNotificationService extends Service {
  private static final String TAG = "DailyDataNotService";

  @Override
  public void onCreate() {
    // initialize time zone information
    AndroidThreeTen.init(this);
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.i(TAG, "onStartCommand");
    Timer timer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          @Override
          public void run() {
            Log.i(TAG, System.currentTimeMillis() / 1000 + "");
            Random random = new Random();
            DailyDataModel dailyDataModel = new DailyDataModel();
            //                dailyDataModel.setDataDate(LocalDate.now());
            dailyDataModel.setUserId("1234567");
            dailyDataModel.setSteps(random.nextInt(1000) + 1000);
            dailyDataModel.setHoursOfSleep(2.6);
            dailyDataModel.setHoursPhoneUse(3.5);
            dailyDataModel.setWaterCC(random.nextInt(1000) + 1500);

            notificationDailyData(dailyDataModel);
          }
        };
    timer.schedule(timerTask, 0, 1000);

    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      stopForeground(true);
    }
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    System.out.println("service in onTaskRemoved");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

    } else {
      Intent restartService =
          new Intent(getApplicationContext(), DailyDataNotificationService.class);
      PendingIntent restartServicePI =
          PendingIntent.getService(getApplicationContext(), 0, restartService, 0);
      AlarmManager mgr =
          (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
      if (mgr != null) {
        mgr.set(AlarmManager.RTC_WAKEUP, 3000, restartServicePI);
      }
    }
    super.onTaskRemoved(rootIntent);
    Log.i(TAG, "onTaskRemoved");
  }

  private void notificationDailyData(DailyDataModel dailyDataModel) {

    // notification
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_daily_data);
    remoteViews.setTextColor(R.id.notication_title, Color.GREEN);

    remoteViews.setTextColor(R.id.notification_sleep_textview, Color.BLACK);
    remoteViews.setTextColor(R.id.notification_drink_textview, Color.BLACK);
    remoteViews.setTextColor(R.id.notification_use_phone_textview, Color.BLACK);
    remoteViews.setTextColor(R.id.notification_step_textview, Color.BLACK);
    remoteViews.setTextViewText(
        R.id.notification_step_textview,
        getString(R.string.today_step_string)
            + ": "
            + dailyDataModel.getSteps()
            + getString(R.string.step_string));
    remoteViews.setTextViewText(
        R.id.notification_sleep_textview,
        getString(R.string.yesterday_sleep_string)
            + ": "
            + dailyDataModel.getHoursOfSleep()
            + getString(R.string.hour_string));
    remoteViews.setTextViewText(
        R.id.notification_drink_textview,
        getString(R.string.today_drink_string)
            + ": "
            + dailyDataModel.getWaterCC()
            + getString(R.string.cc_string));
    remoteViews.setTextViewText(
        R.id.notification_use_phone_textview,
        getString(R.string.today_use_phone_string)
            + ": "
            + dailyDataModel.getHoursPhoneUse()
            + getString(R.string.hour_string));

    int notificationId = 11011;
    String channelId = "daily_data_channel_id";

    Intent notificationIntent = new Intent(this, MainActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

    stackBuilder.addParentStack(MainActivity.class);
    stackBuilder.addNextIntent(notificationIntent);
    PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(this, channelId);
    notificationBuilder
        .setAutoCancel(false)
        .setOnlyAlertOnce(true)
        .setCustomContentView(remoteViews)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentIntent(resultPendingIntent)
        .setCustomBigContentView(remoteViews)
        .setOngoing(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      int important = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel notificationChannel =
          new NotificationChannel(channelId, "daily_data_channel_name", important);
      notificationBuilder.setChannelId(channelId);
      if (notificationManager != null) {
        notificationManager.createNotificationChannel(notificationChannel);
      }
    }

    if (notificationManager != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForeground(notificationId, notificationBuilder.build());
        //                notificationManager.notify(notificationId, notificationBuilder.build());
      } else {
        notificationManager.notify(notificationId, notificationBuilder.build());
      }
    }
  }
}
