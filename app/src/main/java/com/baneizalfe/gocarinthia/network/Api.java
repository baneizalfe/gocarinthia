package com.baneizalfe.gocarinthia.network;

import android.util.Log;

import com.baneizalfe.gocarinthia.BuildConfig;
import com.baneizalfe.gocarinthia.network.interceptors.ContentTypeInterceptor;
import com.baneizalfe.gocarinthia.payment.PaymentRequest;
import com.baneizalfe.gocarinthia.stations.StationsResponse;
import com.baneizalfe.gocarinthia.user.AuthToken;
import com.baneizalfe.gocarinthia.user.LoginRequest;
import com.baneizalfe.gocarinthia.user.RegisterRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * Created by baneizalfe on 6/25/16.
 */
public class Api {

    private ApiInterface apiService;
    private static Api instance;
    private static Retrofit retrofit;

    public static Api getInstance() {
        if (instance == null) instance = new Api();
        return instance;
    }

    public Api() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("HTTP", "" + message);
                }
            });
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addNetworkInterceptor(httpLoggingInterceptor);
        }

        httpClient.addInterceptor(new ContentTypeInterceptor());

        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        apiService = retrofit.create(ApiInterface.class);
    }

    public static Retrofit retrofit() {
        return retrofit;
    }

    public static ApiInterface getApiService() {
        return getInstance().apiService;
    }

    public interface ApiInterface {

        @PUT("user/")
        Call<AuthToken> registerUser(@Body RegisterRequest reqisterRequest);

        @POST("user/")
        Call<AuthToken> loginUser(@Body LoginRequest loginRequest);

        @GET
        Call<StationsResponse> getStations(@Url String url);

        @POST("users/payment/")
        Call<ResponseBody> addPayment(@Body PaymentRequest paymentRequest);
    }
}
