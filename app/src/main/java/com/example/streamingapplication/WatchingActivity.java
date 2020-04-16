package com.example.streamingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import static com.example.streamingapplication.MainPage.EXTRA_NUMBER;

public class WatchingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching);
        Intent intent = getIntent();
        int movie_id = intent.getIntExtra(EXTRA_NUMBER,0);

        Toast.makeText(this,Integer.toString(movie_id),Toast.LENGTH_LONG).show();


        VideoView videoView = findViewById(R.id.videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        Uri uri=Uri.parse("rtsp://192.168.1.34:1935/vod/"+movie_id+".mp4");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}
