package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpModel{
    private String customer_name;
    private String customer_surname;
    private String customer_email;
//    private String customer_username;
    private String customer_password;
//    private String customer_phone_number;
}
