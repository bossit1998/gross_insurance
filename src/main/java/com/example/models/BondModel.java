package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BondModel {
    private String bondSeries;
    private String bondNumber;
    private Integer bondAbsoluteValue;
    private Integer bondPercent;
    private Integer bondLifeTime;
    private String bondStartDate;
    private String bondEndDate;
}
