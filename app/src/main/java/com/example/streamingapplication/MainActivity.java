package com.example.streamingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainPage();
            }
        });


        ImageView poster = findViewById(R.id.imageView);
        //String pstr = "https://image.tmdb.org/t/p/w500/stmYfCUGd8Iy6kAMBr6AmWqx8Bq.jpg";
        //String pstr = "http://192.168.1.33:8080/sid";


        /*URI uri = null;
        try {
            uri = new URI(pstr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/

        /*Glide.with(this)
                .load(pstr) // Remote URL of image.
                .into(poster);*/ //ImageView to set.
        /*Picasso.get().load(Uri.parse(pstr)).placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(poster, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        System.out.println("yesss");
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        System.out.println("NOOOOO");
                    }
                });*/

       /* Picasso.Builder builder = new Picasso.Builder(getApplicationContext());

        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            });*/


        /*VideoView videoView = findViewById(R.id.imageView2);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri uri=Uri.parse("rtsp://192.168.1.33:1935/vod/454626.mp4");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();*/


    }

    private void openMainPage() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);

    }

}
