package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;

@Data
@AllArgsConstructor
public class ResponseData {
    private int status;
    private String error;
    private Object data;

    // only data constructor
    public ResponseData(Object data) {
        this.data = data;
        this.status = 0;
        this.error = "undefined";
    }

    // default constructor
    public ResponseData() {
//        this.data = Collections.EMPTY_MAP;
        this.data = "undefined";
        this.status = 1;
        this.error = "error";
    }
}
