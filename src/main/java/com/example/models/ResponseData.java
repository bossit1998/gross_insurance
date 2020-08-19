package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;

@Data
@AllArgsConstructor
public class ResponseData {
    private int status;
    private String message;
    private Object data;

    public ResponseData(Object data) {
        this.data = data;
        this.status = 0;
        this.message = "SUCCESS";
    }

    public ResponseData() {
        this.data = Collections.EMPTY_MAP;
        this.status = 1;
        this.message = "ERROR";
    }
}
