package com.hua.musice;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.URLEncoder;

public class MediaPlayByUrlActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play_by_url);
        btnPlay = findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                play();
                break;
        }
    }

    private void play(){
        // String url = "http://39.107.90.144:8081/guidersrv/SystekGuiderData/ScenicSpotData/audio/A=%E5%A4%A7%E5%8E%85%E7%AE%80%E4%BB%8B.m4a";
        String url = "http://39.107.90.144:8081/guidersrv/SystekGuiderData/ScenicSpotData/audio/";
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            String fileName = URLEncoder.encode("B0=前言.m4a", "utf-8");
            mediaPlayer.setDataSource(url + fileName);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }
}
