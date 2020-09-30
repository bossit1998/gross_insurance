package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpModel{
    private String name;
    private String surName;
    private String email;
    private String userName;
    private String password;
    private String phoneNumber;
}
