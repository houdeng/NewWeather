package com.example.newweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil{
    /**
     * 实现与服务器交互
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
