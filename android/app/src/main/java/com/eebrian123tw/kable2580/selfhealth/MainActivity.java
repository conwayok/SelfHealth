package com.eebrian123tw.kable2580.selfhealth;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.eebrian123tw.kable2580.selfhealth.service.entity.DailyDataModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button shareButton;
    private Button exportImportButton;

    private Button stepButton;
    private Button sleepButton;
    private Button drinkButton;
    private Button usePhoneButton;
    private DailyDataModel dailyDataModel;


    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int notificationId;
    private RemoteViews remoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        shareButton.setOnClickListener(this);
        exportImportButton.setOnClickListener(this);
        stepButton.setOnClickListener(this);
        sleepButton.setOnClickListener(this);
        drinkButton.setOnClickListener(this);
        usePhoneButton.setOnClickListener(this);


        dailyDataModel = new DailyDataModel();
        dailyDataModel.setDataDate(LocalDate.now());
        dailyDataModel.setUserId("1234567");
        dailyDataModel.setSteps(1234);
        dailyDataModel.setHoursOfSleep(2.6);
        dailyDataModel.setHoursPhoneUse(3.5);
        dailyDataModel.setWaterCC(3000);

        stepButton.setText(dailyDataModel.getSteps() + getString(R.string.step_string));
        sleepButton.setText(dailyDataModel.getHoursOfSleep() + getString(R.string.hour_string));
        drinkButton.setText(dailyDataModel.getWaterCC() + getString(R.string.cc_string));
        usePhoneButton.setText(dailyDataModel.getHoursPhoneUse() + getString(R.string.hour_string));
        //notification
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_daily_data);
//        remoteViews.setTextViewText(R.id.notification_step_textview, dailyDataModel.getSteps() + getString(R.string.step_string));
//        remoteViews.setTextViewText(R.id.notification_sleep_textview, dailyDataModel.getHoursOfSleep() + getString(R.string.hour_string));
//        remoteViews.setTextViewText(R.id.notification_drink_textview, dailyDataModel.getWaterCC() + getString(R.string.cc_string));
//        remoteViews.setTextViewText(R.id.notification_use_phone_textview, dailyDataModel.getHoursPhoneUse() + getString(R.string.hour_string));
//
//        notificationId = (int) System.currentTimeMillis();
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//
//        stackBuilder.addParentStack(MainActivity.class);
//// Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(notificationIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        notificationBuilder = new NotificationCompat.Builder(this);
//        notificationBuilder.setAutoCancel(true).setContentTitle("My notification")
//                .setContentText("Hello World!").setCustomBigContentView(remoteViews).setSmallIcon(R.mipmap.ic_launcher).setOngoing(true);
//        notificationManager.notify(notificationId, notificationBuilder.build());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1001, mBuilder.build());
        //


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_button:
                startActivity(new Intent(this, DetailDataActivity.class));
                break;
            case R.id.sleep_button:
                startActivity(new Intent(this, DetailDataActivity.class));
                break;
            case R.id.drink_button:
                startActivity(new Intent(this, DetailDataActivity.class));
                break;
            case R.id.use_phone_button:
                startActivity(new Intent(this, DetailDataActivity.class));
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
        }
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
}
