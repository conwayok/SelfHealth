package com.eebrian123tw.kable2580.selfhealth;

import lombok.Data;

@Data
public class DetailDataUnit {
    private String type;
    private double value;
    private String dataTime;

    public DetailDataUnit(String type, double value, String dataTime) {
        this.type = type;
        this.value = value;
        this.dataTime = dataTime;
    }
}
