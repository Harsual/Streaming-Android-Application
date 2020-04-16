package com.example.streamingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class MainPage extends AppCompatActivity implements RecyclerViewAdapter.OnNoteListener {
    public static final String EXTRA_NUMBER = "com.example.streamingapplication.EXTRA_NUMBER";
    static ArrayList<Movie_List> arrayList=new ArrayList<Movie_List>();
    static ArrayList<simple_movies> action =new ArrayList<>();
    static ArrayList<simple_movies> comedy =new ArrayList<>();
    static ArrayList<simple_movies> family =new ArrayList<>();
    private static final String TAG = "MainPageActivity";
    RecyclerViewAdapter adapter;
    static int []id_list= new int[20];
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);




        /*for(int i=0;i<3;i++)
        {
            action.add(new simple_movies(454626,"lol"));
            thriller.add(new simple_movies(454626,"lol2"));
            drama.add(new simple_movies(454626,"lol3"));
        }*/
        initMainRecyclerView(action, R.id.movies_recyclerview);
        initMainRecyclerView(comedy, R.id.movies_recyclerview2);
        initMainRecyclerView(family, R.id.movies_recyclerview3);

        int id = 454626;
        ImageView img = setUpImage(id);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this,Integer.toString(id),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainPage.this,ViewingMovieInfo.class);
                intent.putExtra(EXTRA_NUMBER,id);
                startActivity(intent);

            }
        });

        getLists();



        /*for(int i=0;i<3;i++)
        {
            arrayList.add(new Movie_List(i,"list"+ i));
        }*/







        //getLists();
    }

    private ImageView setUpImage(int id) {
        String pstr = "http://192.168.1.34:8080/movies/image/"+id+".jpg";
        ImageView poster = findViewById(R.id.imageView);


        Picasso.get().load(pstr).placeholder(R.drawable.ic_launcher_background)
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
                });

        return poster;




    }

    private void getLists() {
        getListsRunnable runnable = new getListsRunnable();
        Thread tr = new Thread(runnable);
        tr.start();

    }

    private void initMainRecyclerView(ArrayList<simple_movies> arr,int id){
        Log.d(TAG, "initRecyclerView Main: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView recyclerView = findViewById(id);
        adapter = new RecyclerViewAdapter(this, arr,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onNoteClick(int movie_id) {
        Toast.makeText(MainPage.this,Integer.toString(movie_id),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainPage.this,ViewingMovieInfo.class);
        intent.putExtra(EXTRA_NUMBER,movie_id);
        startActivity(intent);
    }

    /*private String getInfo(int position) {


    }*/

    class getListsRunnable implements  Runnable {



        @Override
        public void run() {

            InputStream inputStream = null;
            try {

                URL url = new URL("http://192.168.1.34:8080/movies/genres");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //bufferedReader.skip(20);
                String line ="";
                String data = line;




                while(line!=null) {
                        line = bufferedReader.readLine();
                        data = data + line;


                }

                JSONArray JA = new JSONArray(data);

                int counter = 0;




                    for(int j=0; j<JA.length();j++) {

                        JSONObject JO = JA.getJSONObject(j);

                        switch (JO.getInt("genre")) {
                                case 28:
                                    action.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                                    break;
                                case 35:
                                    comedy.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                                    break;
                                case 10751:
                                    family.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                                    break;
                                default:

                            }






                    }






            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

            }finally {
                try { if (inputStream != null) inputStream.close(); } catch(IOException e) {}
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        initMainRecyclerView(action, R.id.movies_recyclerview);
                        initMainRecyclerView(comedy, R.id.movies_recyclerview2);
                        initMainRecyclerView(family, R.id.movies_recyclerview3);
                    }
                });

            }






        }
    }












    class Movie_List
    {

        int id;
        String listName;
        int [] movie_ids = new int[20];

        Movie_List(int id,String listname){
            this.id = id;
            this.listName = listname;
        }

    }

    class simple_movies
    {

        int id;
        String movieName;

        simple_movies(int id,String MovieName){
            this.id = id;
            this.movieName = MovieName;
        }



    }





}
