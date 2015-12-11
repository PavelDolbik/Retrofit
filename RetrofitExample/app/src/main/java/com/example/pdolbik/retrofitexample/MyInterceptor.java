package com.example.pdolbik.retrofitexample;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by p.dolbik on 10.12.2015.
 */
public class MyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request  request  = chain.request();
        Response response = chain.proceed(request);

        Log.d("Pasha", "MyInterceptor");

        int tryCount = 0;
        while (!response.isSuccessful() && tryCount < 3) {
            tryCount++;
            Log.d("Pasha", "retry");
            // Do anything with response here
            // retry the request
            response = chain.proceed(request);
        }
        return response;
    }
}
