package com.eebrian123tw.kable2580.selfhealth.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DailyDataModel {
  private String userId;
  private LocalDate dataDate;
  private int steps;
  private double hoursOfSleep;
  private int waterCC;
  private double hoursPhoneUse;
}
