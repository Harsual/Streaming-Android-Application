package com.example.streamingapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;


import static com.example.streamingapplication.HomeFragment.EXTRA_NUMBER;


public class WatchingActivity extends AppCompatActivity {

    VideoView videoView;
    View decorView;
    MediaController mediaController;
    ProgressBar pBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_watching);
        Intent intent = getIntent();
        int movie_id = intent.getIntExtra(EXTRA_NUMBER,0);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                {
                    delayedHide(4000);
                }
            }
        });

        pBar = findViewById(R.id.pBar);

        pBar.setVisibility(View.VISIBLE);



        /*dialog = new ProgressDialog(this);
        dialog.setMessage("loading info");
        dialog.show();*/





        videoView = findViewById(R.id.videoView);
        mediaController=new MediaController(this);
        mediaController.setAnchorView(videoView);




        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!= null)
        {getSupportActionBar().hide();}

        //Objects.requireNonNull(getActionBar()).hide();

        String str = "rtsp://192.168.1.34:1935/vod/"+movie_id+".mp4";

        //Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        //Uri uri=Uri.parse("rtsp://192.168.1.34:1935/vod/"+movie_id+".mp4");



        videoView.setMediaController(mediaController);
        Uri uri=Uri.parse(str);
        videoView.setVideoURI(uri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //Toast.makeText(WatchingActivity.this,"cool",Toast.LENGTH_LONG).show();
                pBar.setVisibility(View.GONE);

            }
        });

        videoView.start();




        /*final GestureDetector clickDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                boolean visible = (decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

                if (visible) {
                    hideSystemUI();
                } else {
                    //showSystemUI();
                }


                System.out.println("PRINTED");
                return true;
            }
            });*/



    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide(1000);
    }

    Handler mHideSystemUiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            hideSystemUI();
        }
    };

    private void delayedHide(int delayMillis) {
        mHideSystemUiHandler.removeMessages(0);
        mHideSystemUiHandler.sendEmptyMessageDelayed(0,delayMillis);

    }

    /*@Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }*/



    /*@Override
    public void onBackPressed() {
        Toast.makeText(this,"IT IS PRESSED",Toast.LENGTH_LONG).show();
        finish();

    }*/


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            delayedHide(3000);
        }

        else {
            mHideSystemUiHandler.removeMessages(0);
        }


    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        decorView.setSystemUiVisibility(
                //View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /*private void showSystemUI() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }*/
}
