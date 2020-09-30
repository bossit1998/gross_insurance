package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferModel {
    private String seller_account_number;
    private String seller_balance_number;
    private String buyer_account_number;
    private String buyer_balance_number;
    private String bond_series;
    private String bond_number;
    float money_amount;
    private String transfer_dat;
    boolean transfer_approved;
}
