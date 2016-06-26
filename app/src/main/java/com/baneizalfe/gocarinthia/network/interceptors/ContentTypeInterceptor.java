package com.baneizalfe.gocarinthia.network.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by baneizalfe on 5/17/16.
 */
public class ContentTypeInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(requestWithUserAgent);
    }
}
