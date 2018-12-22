package com.eebrian123tw.kable2580.selfhealth.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.threeten.bp.LocalDate;

import lombok.Data;

@Data
public class DailyDataModel {

  public DailyDataModel() {
    this.userId = "id";
    this.dataDate = LocalDate.MIN.toString();
    this.steps = 0;
    this.hoursOfSleep = 0;
    this.waterCC = 0;
    this.hoursPhoneUse = 0;
  }

  @JsonProperty("userId")
  private String userId;

  @JsonProperty("dataDate")
  private String dataDate;

  @JsonProperty("steps")
  private int steps;

  @JsonProperty("hoursOfSleep")
  private double hoursOfSleep;

  @JsonProperty("waterCC")
  private int waterCC;

  @JsonProperty("hoursPhoneUse")
  private double hoursPhoneUse;
}
