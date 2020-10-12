package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferApprovedModel {
    private String buyer_account_number;
    private String bond_series;
    private String bond_number;
}
