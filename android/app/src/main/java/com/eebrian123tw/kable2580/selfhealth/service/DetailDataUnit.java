package com.eebrian123tw.kable2580.selfhealth.service;

import org.threeten.bp.LocalDate;

import java.io.Serializable;

import lombok.Data;

@Data
public class DetailDataUnit {
  private Type type;
  private double value;
  private LocalDate dataTime;

  public DetailDataUnit(Type type, double value, LocalDate dataTime) {
    this.type = type;
    this.value = value;
    this.dataTime = dataTime;
  }

  public enum Type implements Serializable {
    SLEEP,
    STEPS,
    PHONE_USE,
    DRINK
  }
}
