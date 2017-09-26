package com.example.tg.musicplayer.Http;



import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.tg.musicplayer.streamer.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultHandler extends Handler {
    private Context context;

    public ResultHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 0:
                String result =(String)msg.obj;
                ((MainActivity)context).initMainUI(dissolveJSONArray(result));

                break;
            case -1:
                break;
        }
    }
    private List<FileItem> dissolveJSONArray(String data){

        List<FileItem> list = new ArrayList<>();
        try {

            if(StaticVals.MovedDirectory.size()!=0){
                list.add(new FileItem("Go Back..", true, true));
            }

            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0 ; i<jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(new FileItem(jsonObject.getString("fileName"), jsonObject.getBoolean("file")));
                StaticVals.Directory.add(jsonObject.getString("fileName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
