package com.duriana.cloudbox;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by tonyhaddad on 14/07/2016.
 */
public class ServiceGenerator {
    private static ServiceGenerator instance = null;
    private static OkHttpClient.Builder httpClient;
    private static Retrofit.Builder builder;

    protected ServiceGenerator() {
    }

    public static ServiceGenerator getInstance(String API_BASE_URL) {
        if (instance == null) {
            instance = new ServiceGenerator();
            httpClient = new OkHttpClient.Builder();
            builder =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(JacksonConverterFactory.create());
        }
        return instance;
    }

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

}
