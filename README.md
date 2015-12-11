## Retrofit 2.0

Examples of using Retrofit 2.0

#### See this good article - http://inthecheesefactory.com/blog/retrofit-2.0/en

#### Add dependencies
```java
//Retrofit
compile 'com.squareup.retrofit:retrofit:2.0.0-beta2'
compile 'com.squareup.retrofit:converter-gson:2.0.0-beta2'

//RxJava
compile 'com.squareup.retrofit:adapter-rxjava:2.0.0-beta2'
compile 'io.reactivex:rxandroid:1.0.1'

//logging
compile 'com.squareup.okhttp:logging-interceptor:2.6.0'
```

#### Add logging
See this article - https://futurestud.io/blog/retrofit-2-log-requests-and-responses
```java
HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
logging.setLevel(HttpLoggingInterceptor.Level.BODY);
```

#### Add retry policy
Need create interceptor
```java
public class MyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request  request  = chain.request();
        Response response = chain.proceed(request);

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
```

#### Create OkHttpClient
With logging and retry policy
```java
OkHttpClient client = new OkHttpClient();
client.interceptors().add(new MyInterceptor());
client.interceptors().add(logging);
```

#### String convert factory
If you wont you can convert responce to string
```java
public class ToStringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        if (String.class.equals(type)) {
            return new Converter<ResponseBody, String>() {
                @Override public String convert(ResponseBody value) throws IOException {
                    return value.string();
                }
            };
        }
        return null;
    }

    @Override public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        if (String.class.equals(type)) {
            return new Converter<String, RequestBody>() {
                @Override public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value);
                }
            };
        }
        return null;
    }
}
```

#### Create service API
Receive string or other entity
```java
public interface APIService {

	@GET("/users/{user}/repos")
    Call<String> listReposStr(@Path("user") String user);
	
    @GET("/users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);
}
```

#### Simple entity
```java
public class Repo {

    @SerializedName("id")
    private String idRep;

    private String name;

    @SerializedName("full_name")
    private String fullName;

    public Repo() {}

    public String getId()       { return idRep; }
    public String getName()     { return name; }
    public String getFullName() { return fullName; }
}
```

#### Build Retrofit
```java
HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
logging.setLevel(HttpLoggingInterceptor.Level.BODY);

OkHttpClient client = new OkHttpClient();
client.interceptors().add(new MyInterceptor());
client.interceptors().add(logging);

Retrofit retrofit = new Retrofit.Builder()
.baseUrl("https://api.github.com")
.addConverterFactory(GsonConverterFactory.create()) //for entity
//.addConverterFactory(new ToStringConverterFactory()) //for string
.client(client)
.build();

APIService service = retrofit.create(APIService.class);
```

#### Error handler
See this article - https://futurestud.io/blog/retrofit-2-simple-error-handling
```java
public class APIError {

    private int    statusCode;
    private String message;

    public APIError() {}

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
}


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
```

#### Execute request
```java
HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
logging.setLevel(HttpLoggingInterceptor.Level.BODY);

OkHttpClient client = new OkHttpClient();
client.interceptors().add(new MyInterceptor());
client.interceptors().add(logging);

Retrofit retrofit = new Retrofit.Builder()
.baseUrl("https://api.github.com")
.addConverterFactory(GsonConverterFactory.create())
.client(client)
.build();

APIService service = retrofit.create(APIService.class);

Call<List<Repo>> call = service.listRepos("pasha656");
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
                APIError error = ErrorUtils.parseError(response, retrofit);
                Log.d("Pasha", "No succsess message "+error.getMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Pasha", "onFailure "+t.getMessage());
            }
});
```

