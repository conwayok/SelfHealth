package com.eebrian123tw.kable2580.selfhealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

  private Switch showNotificationSwitch;
  private Switch showStepsSwitch;
  private Switch showSleepDataSwitch;
  private Switch showWaterAmountSwitch;
  private Switch showPhoneUsageSwtich;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    showNotificationSwitch = findViewById(R.id.show_notifications_switch);
    showStepsSwitch = findViewById(R.id.show_steps_switch);
    showSleepDataSwitch = findViewById(R.id.show_sleep_data_switch);
    showWaterAmountSwitch = findViewById(R.id.show_water_amount_switch);
    showPhoneUsageSwtich = findViewById(R.id.show_phone_usage_switch);


  }
}
