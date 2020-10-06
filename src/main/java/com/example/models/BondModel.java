package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BondModel {
    private String bond_series;
    private String bond_number;
    private Integer bond_absolute_value;
    private Integer bond_percent;
    private Integer bond_life_time;
    private String bond_start_date;
    private String bond_end_date;
}
