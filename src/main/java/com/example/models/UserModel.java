package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private String customer_name;
    private String customer_username;
    private String customer_password;
    private String customer_account_number;
    private String customer_balance_number;
    private Integer customer_balance;
    private String customer_phone_number;
    private String customer_mail;
    private Integer customer_privilege;
    private String customer_register_date;
}
