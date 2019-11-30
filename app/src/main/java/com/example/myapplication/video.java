package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class video extends AppCompatActivity {

    //private static final String VIDEO_SAMPLE ="http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
    private  String VIDEO_SAMPLE ="";
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.driverVideo);

        Intent intent = getIntent();
        VIDEO_SAMPLE = intent.getStringExtra("CONTENT");
        Log.d("VIDEO--->", VIDEO_SAMPLE);
        playVideo();

    }

    private void playVideo() {

        videoView.setVideoURI(Uri.parse(VIDEO_SAMPLE));
        videoView.requestFocus();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController mediaController = new MediaController(video.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });
    }

}
