package com.example.pdolbik.retrofitexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pdolbik.retrofitexample.error.APIError;
import com.example.pdolbik.retrofitexample.error.ErrorUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.security.Provider;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private Button   button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new MyInterceptor());
        client.interceptors().add(logging);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                //.addConverterFactory(new ToStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        final APIService service = retrofit.create(APIService.class);


        textView = (TextView) findViewById(R.id.textView);
        button   = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Call<List<Repo>> call = service.listRepos("pasha656");
                call.enqueue(new Callback<List<Repo>>() {
                    @Override
                    public void onResponse(Response<List<Repo>> response, Retrofit retrofit) {

                        if (response.isSuccess()) {
                            if (!response.body().isEmpty()) {
                                for (Repo r : response.body()) {
                                    textView.setText(textView.getText()+" "+r.getId()+" "+r.getName()+" \n");
                                }
                            }
                        } else {
                            Log.d("Pasha", "No succsess");
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


                /*final Call<String> callStr = service.listReposStr("pasha656");
                callStr.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {

                        if (response.isSuccess()) {
                            if (!response.body().isEmpty()) {
                                textView.setText(textView.getText()+" "+response.body());
                            }
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
                });*/
            }
        });
    }
}
