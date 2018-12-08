package com.eebrian123tw.kable2580.selfhealth;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Button shareButton;
    private Button exportImportButton;
    private Button settingsButton;

    private Button stepButton;
    private Button sleepButton;
    private Button drinkButton;
    private Button usePhoneButton;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler handler;
    private DailyDataModel dailyDataModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize time zone information
        AndroidThreeTen.init(this);

        shareButton = findViewById(R.id.share_button);
        exportImportButton = findViewById(R.id.export_import_button);
        stepButton = findViewById(R.id.step_button);
        sleepButton = findViewById(R.id.sleep_button);
        drinkButton = findViewById(R.id.drink_button);
        usePhoneButton = findViewById(R.id.use_phone_button);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        settingsButton = findViewById(R.id.settings_button);

        shareButton.setOnClickListener(this);
        exportImportButton.setOnClickListener(this);
        stepButton.setOnClickListener(this);
        sleepButton.setOnClickListener(this);
        drinkButton.setOnClickListener(this);
        usePhoneButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        dailyDataModel = new DailyDataModel();
        dailyDataModel.setDataDate(LocalDate.now());
        dailyDataModel.setUserId("1234567");
        dailyDataModel.setSteps(1234);
        dailyDataModel.setHoursOfSleep(2.6);
        dailyDataModel.setHoursPhoneUse(3.5);
        dailyDataModel.setWaterCC(3000);

        showDailyData();

        if (!isThisServiceRunning(DailyDataNotificationService.class)) {
            Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //                startService(new Intent(this, DailyDataNotificationService.class));
                startForegroundService(new Intent(this, DailyDataNotificationService.class));
            } else {
                startService(new Intent(this, DailyDataNotificationService.class));
            }
        }

        //        notificationDailyData(dailyDataModel);

        //        ComponentName componentName = new ComponentName(this,
        // DailyDataNotificationJobService.class.getName());
        //        JobInfo jobInfo = new JobInfo.Builder(1234547, componentName)
        //
        //                .setPeriodic(15*60*1000)
        //                .build();
        //        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        //        int returnCode = jobScheduler.schedule(jobInfo);

        handler = new Handler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_button:
                Intent intentStep = new Intent(this, DetailDataActivity.class);
                intentStep.putExtra("type", DetailDataUnit.Type.STEPS);
                startActivity(intentStep);

                break;
            case R.id.sleep_button:
                Intent intentSleep = new Intent(this, DetailDataActivity.class);
                intentSleep.putExtra("type", DetailDataUnit.Type.SLEEP);
                startActivity(intentSleep);

            case R.id.drink_button:
                Intent intentDrink = new Intent(this, DetailDataActivity.class);
                intentDrink.putExtra("type", DetailDataUnit.Type.DRINK);
                startActivity(intentDrink);
                break;
            case R.id.use_phone_button:
                Intent intentPhoneUse = new Intent(this, DetailDataActivity.class);
                intentPhoneUse.putExtra("type", DetailDataUnit.Type.PHONE_USE);
                startActivity(intentPhoneUse);
                break;
            case R.id.share_button:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareSubject = getString(R.string.app_name);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                String shareBody = parseDailyDataToString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_string)));
                break;

            case R.id.export_import_button:
                startActivity(new Intent(this, ExportImportActivity.class));
                break;

            case R.id.settings_button:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showDailyData() {
        stepButton.setText(dailyDataModel.getSteps() + getString(R.string.step_string));
        sleepButton.setText(dailyDataModel.getHoursOfSleep() + getString(R.string.hour_string));
        drinkButton.setText(dailyDataModel.getWaterCC() + getString(R.string.cc_string));
        usePhoneButton.setText(dailyDataModel.getHoursPhoneUse() + getString(R.string.hour_string));
    }

    private String parseDailyDataToString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getString(R.string.my_today_status));
        stringBuilder.append('\n');

        stringBuilder.append(getString(R.string.today_step_string));
        stringBuilder.append(": ");
        stringBuilder.append(dailyDataModel.getSteps()).append(getString(R.string.step_string));
        stringBuilder.append('\n');

        stringBuilder.append(getString(R.string.yesterday_sleep_string));
        stringBuilder.append(": ");
        stringBuilder.append(dailyDataModel.getHoursOfSleep()).append(getString(R.string.hour_string));
        stringBuilder.append('\n');

        stringBuilder.append(getString(R.string.today_drink_string));
        stringBuilder.append(": ");
        stringBuilder.append(dailyDataModel.getWaterCC()).append(getString(R.string.cc_string));
        stringBuilder.append('\n');

        stringBuilder.append(getString(R.string.today_use_phone_string));
        stringBuilder.append(": ");
        stringBuilder.append(dailyDataModel.getHoursPhoneUse()).append(getString(R.string.hour_string));

        return stringBuilder.toString();
    }

    private void notificationDailyData(DailyDataModel dailyDataModel) {

        // notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_daily_data);
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
                .setAutoCancel(true)
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
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
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }

    @Override
    public void onRefresh() {
        // simulate data update
        dailyDataModel.setSteps(12345);
        dailyDataModel.setHoursPhoneUse(2.8);
        dailyDataModel.setHoursOfSleep(8.5);
        dailyDataModel.setWaterCC(4000);

        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        showDailyData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                1000);
    }

    private boolean isThisServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
