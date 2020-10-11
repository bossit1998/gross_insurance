package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuySellRequestModel {
    private String requester_account_number;
    private String bond_series;
    private String bond_number;
    private float money_amount;
    private int transfer_type;
    private boolean transfer_approved;
}