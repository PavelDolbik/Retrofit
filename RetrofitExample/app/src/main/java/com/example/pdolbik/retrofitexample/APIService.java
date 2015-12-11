package com.example.pdolbik.retrofitexample;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by p.dolbik on 10.12.2015.
 */
public interface APIService {

    @GET("/users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

    @GET("/users/{user}/repos")
    Call<String> listReposStr(@Path("user") String user);
}
