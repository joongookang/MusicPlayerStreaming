package com.example.tg.musicplayer.streamer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.tg.musicplayer.Http.FileItem;
import com.example.tg.musicplayer.Http.GetListSender;
import com.example.tg.musicplayer.Http.HttpSender;
import com.example.tg.musicplayer.Http.MainListAdapter;
import com.example.tg.musicplayer.Http.ResultHandler;
import com.example.tg.musicplayer.Http.SimpleDividerItemDecoration;
import com.example.tg.musicplayer.R;
import com.example.tg.musicplayer.Http.StaticVals;

import java.net.URLEncoder;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mainContext;
    private RecyclerView listView;
    private MediaPlayer mediaPlayer;
    private TextView text;
    private final static int LOADER_ID = 0x001;
    public String requsturl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;
        initFindViewByld();
        HttpSender sender = new GetListSender(new ResultHandler(mainContext));
        sender.setBodyContents();
        sender. send();



    }

    private void initFindViewByld(){
        listView = (RecyclerView) findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(mainContext));
        listView.addItemDecoration(new SimpleDividerItemDecoration(mainContext));

    }

    public void initMainUI(List<FileItem> data) {
        RecyclerView.Adapter adapter = new MainListAdapter(data, mainContext, new MainListListener());
        listView.setAdapter(adapter);
    }
    private class MainListListener implements MainListAdapter.MainItemClickListener {

        @Override
        public void onClick(FileItem item, int position) {

            if (item.isGoBack) {
                //pressed go back button...
                Log.e("MAIN_CLICK", "GO BACK");
                int index = StaticVals.MovedDirectory.size() - 1;
                StaticVals.MovedDirectory.remove(index);
                doMoveDirectory();
            } else {
                if (!item.isFile) {
                    //If the selection is a directory, move.
                    Log.e("MAIN_CLICK", "DIRECTORY");
                    StaticVals.MovedDirectory.add(item.fileName);
                    doMoveDirectory();
                } else {
                    //if it's a file, play it.
                    try{
                        String url = HttpSender.URL + "/download?name=";
                        String contents= "";
                        for(String dir : StaticVals.MovedDirectory){
                            contents +=dir +"\\";
                        }

                        url = url+ URLEncoder.encode(contents + item.fileName,"UTF-8");
                        requsturl=url;
                        StartMusic(url,position);
                    }catch (Exception e){
                        Log.e("MUSIC",e.getMessage());

                    }
                }
            }
        }
    }
    private void doMoveDirectory(){
        HttpSender sender = new GetListSender(new ResultHandler(mainContext));
        String requestDirName = "";
        for(String dir : StaticVals.MovedDirectory){
            requestDirName+=dir+"\\";
        }
        if(!requestDirName.equals(""))
            sender.setBodyContents(requestDirName);
        else
            sender.setBodyContents();

        Log.e("REQDIRNAME", requestDirName);
        sender.send();
    }
    public void StartMusic(String url, final int po) {
        try {

            if(mediaPlayer!=null){
                mediaPlayer.stop();
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    Log.e("PREPARED", "STARTING Music");
//                    mp.start();
                    StaticVals.Index=po;
                    Intent intent = new Intent(MainActivity.this, PlayerContlloer.class);
                    intent.putExtra("url",requsturl);
                    startActivity(intent);


                }
            });

        } catch (Exception e) {
            Log.e("MusicPlayer", e.getMessage());
        }
    }


}
