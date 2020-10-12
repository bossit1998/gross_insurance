package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordModel {
    private String customer_account_number;
    private String customer_current_password;
    private String customer_new_password;
}
