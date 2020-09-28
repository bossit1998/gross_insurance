package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpModel extends SignInModel{
    private String name;
    private String surName;
    private String email;
    private String userName;
    private String password;
    private String phoneNumber;
}
