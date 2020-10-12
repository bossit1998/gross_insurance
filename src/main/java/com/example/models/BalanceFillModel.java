package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceFillModel {
    private String customer_account_number;
    private String customer_phone_number;
    private String customer_card_number;
    private String customer_card_valid_date;
    private float money_amount;
    private String confirmation_code;
}