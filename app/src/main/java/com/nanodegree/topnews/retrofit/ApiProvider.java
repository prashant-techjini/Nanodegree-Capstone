package com.nanodegree.topnews.retrofit;

import android.content.Context;

import com.nanodegree.topnews.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Prashant Nayak
 */

public class ApiProvider {

    private static ApiInterface api = null;

    private ApiProvider() {
    }

    public static void createApiService(final Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        builder.interceptors().add(httpLoggingInterceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(ApiInterface.class);
    }

    public static ApiInterface getApiService(Context context) {
        if (api == null) {
            createApiService(context);
        }
        return api;
    }
}
