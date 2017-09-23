package com.example.tg.musicplayer.Http;


import android.os.Handler;

import com.squareup.okhttp.FormEncodingBuilder;

public class GetListSender extends HttpSender{

    public GetListSender(Handler handler) {
        super(handler);
        apiName = "listfile";
    }

    @Override
    public void setBodyContents(Object... Params) {
        if(Params.length != 0){
            body = new FormEncodingBuilder().add("name",(String) Params[0]).build();
        }else{
            body = new FormEncodingBuilder().add("name", "null").build();
        }

    }
}
