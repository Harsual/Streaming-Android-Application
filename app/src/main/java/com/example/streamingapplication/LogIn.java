package com.example.streamingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LogIn extends AppCompatActivity {
    EditText username;
    EditText password;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    boolean auth = false;
    private Handler mainHandler = new Handler();
    TextView textView;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);

        progressBar = findViewById(R.id.pBar);

        username = findViewById(R.id.Username);
        password = findViewById(R.id.password);
        textView = findViewById(R.id.warning);
        relativeLayout = findViewById(R.id.innerRL);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoginCred();
            }
        });




    }

    private void checkLoginCred() {
        btn.setText("");
        btn.setClickable(false);
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);


        checkCredRunnable runnable = new checkCredRunnable();
        Thread tr = new Thread(runnable);
        tr.start();


    }
    private String formatDataAsJSON(){
        final JSONObject root = new JSONObject();
        try{
            EditText editText1 = findViewById(R.id.Username);
            EditText editText2 = findViewById(R.id.password);


            root.put("username",editText1.getText());
            root.put("password",editText2.getText());

            System.out.print(root.toString());

            textView.setText(root.toString());

            return root.toString();
        }catch (JSONException e){
            Log.d("JWP","Can't format JSON");
        }

        return null;


    }
    class checkCredRunnable implements  Runnable {


        @Override
        public void run() {


            String urlString ="http://192.168.1.34:8080/secure/Auth"; // URL to call
            String data = formatDataAsJSON(); //data to post
            OutputStream out = null;
            InputStream in = null;




            try {

                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/JSON");
                urlConnection.setDoOutput(true);

                out = new BufferedOutputStream(urlConnection.getOutputStream());

                OutputStreamWriter wr = new OutputStreamWriter(out);
                wr.write(data);
                wr.flush();

                System.out.println(urlConnection.getResponseCode());

                //if()
                /*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                out.close();*/

                //urlConnection.setRequestMethod("GET");
                in = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

                String line = "";
                data = line;


                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;


                }

                JSONObject JO = new JSONObject(data);

                auth = JO.getBoolean("response");





            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(auth){
                        openMainPage();
                    }

                    else {
                        textView.setVisibility(View.VISIBLE);
                        btn.bringToFront();
                        btn.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                        btn.setText("Log In");
                    }


                }
            });





        }


    }



    private void openMainPage() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);

    }

}
