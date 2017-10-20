package com.example.tg.musicplayer.Http;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.spdy.FrameReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public abstract class HttpSender {

    private static final String TAG = "HTTPSender";

    public static final String URL = "http://172.19.1.193:8080/";

    protected String apiName;
    protected RequestBody body;
    protected Handler handler;

    public HttpSender(Handler handler) {
        this.handler = handler;
    }
    public abstract void setBodyContents(Object... Params);


    public void send() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(10, TimeUnit.SECONDS);
                Request request = new Request.Builder().url(URL + apiName).post(body).build();
                Message msg = handler.obtainMessage();
                try {
                    Response response = client.newCall(request).execute();
                    msg.what = 0;
                    msg.obj = response.body().string();
                } catch (IOException e) {
                    Log.e(TAG, "Exception pccurred during HTTPSender send Method, Async Task.\nServer may not be reachable...");
                    msg.what = -1;
                }
                msg.sendToTarget();
                return null;
            }

        }.execute();
    }
}
