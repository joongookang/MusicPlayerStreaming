package com.example.tg.musicplayer.streamer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tg.musicplayer.Http.FileItem;
import com.example.tg.musicplayer.Http.HttpSender;
import com.example.tg.musicplayer.Http.StaticVals;
import com.example.tg.musicplayer.R;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;


public class PlayerContlloer extends AppCompatActivity {
    private Button b1, b2, b3, b4, b5;
    private ImageView iv;
    private SeekBar seekbar;
    private TextView tx1, tx2, tx3;
    private MediaPlayer mediaPlayer;
    private int backwardTime = 5000;
    private double startTime = 0;
    private double finalTime = 0;
    private int forwardTime = 5000;
    private Handler myHandler = new Handler();
    public static int oneTimeOnly = 0;
    private int currentPosition = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contlloer);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        iv = (ImageView) findViewById(R.id.imageView);
        tx1 = (TextView) findViewById(R.id.textView2);
        tx2 = (TextView) findViewById(R.id.textView3);
        tx3 = (TextView) findViewById(R.id.textView4);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        int i;
        Intent intent = getIntent();
        String url = intent.getExtras().getString("url");
        currentPosition = StaticVals.Index;
        StartMusic(url);
//        b3.setEnabled(false);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }
                tx2.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );

                tx1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );

                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
//                b2.setEnabled(true);
//                b3.setEnabled(false);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
//                b2.setEnabled(false);
//                b3.setEnabled(true);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if(fromUser)
                    mediaPlayer.seekTo(progress);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (++currentPosition >= StaticVals.Directory.size()) {
                    // 마지막 곡이 끝나면, 재생할 곡을 초기화합니다.
                    currentPosition = 0;
                    StartMusic(HttpSender.URL + "/download?name=" + StaticVals.Directory.get(currentPosition));
                } else {
                    // 다음 곡을 재생합니다.
                    Toast.makeText(getApplicationContext(), "다음 곡을 재생합니다.", Toast.LENGTH_SHORT).show();
                    StartMusic(HttpSender.URL + "/download?name=" + StaticVals.Directory.get(currentPosition));
                }
            }
        });

    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",

                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public void StartMusic(String url) {
        try {

            if (mediaPlayer != null) {
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
                    mp.start();
                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }
                    tx2.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                    );

                    tx1.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    seekbar.setProgress((int) startTime);
                    tx3.setText(StaticVals.Directory.get(currentPosition));
                    myHandler.postDelayed(UpdateSongTime, 100);
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer arg0) {
                            nextSong();
                        }
                    });
                }
            });

        } catch (Exception e) {
            Log.e("MusicPlayer", e.getMessage());
        }
    }

    private void nextSong() {
        if (++currentPosition >= StaticVals.Directory.size()) {
            // 마지막 곡이 끝나면, 재생할 곡을 초기화합니다.
            currentPosition = 0;
        } else {
            // 다음 곡을 재생합니다.
            Toast.makeText(getApplicationContext(), "다음 곡을 재생합니다.", Toast.LENGTH_SHORT).show();
            StartMusic(HttpSender.URL + "/download?name=" + StaticVals.Directory.get(currentPosition));
        }
    }
}





