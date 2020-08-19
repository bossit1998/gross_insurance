package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    private Integer productCode;
    private String productName;
    private Integer volume;
    private Integer price;
    private String assortment;
    private String productType;
    private Integer numberInTheBox;
    private String storeTemperature;
    private String goodThrough;
    private String description;
}
