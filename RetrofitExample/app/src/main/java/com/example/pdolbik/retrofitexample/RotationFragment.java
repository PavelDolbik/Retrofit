package com.example.pdolbik.retrofitexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.pdolbik.retrofitexample.error.APIError;
import com.example.pdolbik.retrofitexample.error.ErrorUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by p.dolbik on 14.12.2015.
 */
public class RotationFragment extends Fragment {

    private MainActivity activity;
    private APIService   service;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        prepareServiceAPI();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public void execute() {
        Call<List<Repo>> call = service.listRepos("pasha656");
        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Response<List<Repo>> response, Retrofit retrofit) {

                if (response.isSuccess()) {
                    if (!response.body().isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (Repo r : response.body()) {
                            sb.append(r.getId()).append(" ").append(r.getName()).append(" \n");
                        }
                        activity.setText(sb.toString());
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response, retrofit);
                    Log.d("Pasha", "No succsess message "+error.getMessage());
                }


                if (response.errorBody() != null) {
                    try {
                        Log.d("Pasha", "Error "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Pasha", "onFailure "+t.getMessage());
            }
        });
    }

    private void prepareServiceAPI() {
        //For logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new MyInterceptor());
        client.interceptors().add(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                //.addConverterFactory(new ToStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(APIService.class);
    }
}
