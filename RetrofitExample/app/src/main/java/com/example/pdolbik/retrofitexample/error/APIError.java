package com.example.pdolbik.retrofitexample.error;

/**
 * Created by p.dolbik on 11.12.2015.
 */
public class APIError {

    private int    statusCode;
    private String message;

    public APIError() {}

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
}
