package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BondInfoModel {
    private String customer_account_number;
    private String bond_series;
    private String bond_number;
}
