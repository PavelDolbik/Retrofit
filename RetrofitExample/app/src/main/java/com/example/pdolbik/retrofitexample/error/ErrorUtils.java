package com.example.pdolbik.retrofitexample.error;

import com.example.pdolbik.retrofitexample.Repo;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by p.dolbik on 11.12.2015.
 */
public class ErrorUtils {

    public static APIError parseError(Response<List<Repo>> response, Retrofit retrofit) {
        APIError error = null;

        try {
            Converter<ResponseBody, APIError> converter = retrofit.responseConverter(APIError.class, new Annotation[0]);
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return error;
    }
}
