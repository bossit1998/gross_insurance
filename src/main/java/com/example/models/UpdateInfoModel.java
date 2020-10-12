package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfoModel {
    private String customer_account_number;
    private String customer_email;
    private String customer_name;
    private String customer_surname;
}
