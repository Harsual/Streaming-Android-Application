package com.example.streamingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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


import static com.example.streamingapplication.HomeFragment.EXTRA_NUMBER;



public class ViewingMovieInfo extends AppCompatActivity {


    ImageButton addButton;
    ImageButton likeButton;
    boolean isAdded;
    boolean isLiked;
    int addPosition;
    int likePosition = 0;
    HomeFragment.simple_movies movie2;
    ArrayList<HomeFragment.simple_movies> likedList;
    ArrayList<HomeFragment.simple_movies> similarMovies = new ArrayList<>();
    ImageView similarImage1;
    ImageView similarImage2;
    ImageView similarImage3;
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        final int movie_id = intent.getIntExtra(EXTRA_NUMBER,0);




        setContentView(R.layout.activity_viewing_movie_info);
        Button play_btn = findViewById(R.id.play_button);
        ConstraintLayout CLout= findViewById(R.id.Lout);
        addButton = findViewById(R.id.addtolist);
        likeButton = findViewById(R.id.like);
        similarImage1 = findViewById(R.id.additional_movie1);
        similarImage2 = findViewById(R.id.additional_movie2);
        similarImage3 = findViewById(R.id.additional_movie3);

        addButton.setClickable(false);
        likeButton.setClickable(false);
        similarImage1.setClickable(false);
        similarImage2.setClickable(false);
        similarImage3.setClickable(false);

        similarImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSameActivity(0);

            }
        });

        similarImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSameActivity(1);

            }
        });


        similarImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSameActivity(2);
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isLiked)
                {
                    likedList.remove(likePosition);

                    likeButton.setColorFilter(Color.argb(255,255,255,255));
                    isLiked = false;
                    removeLikeFromDB();
                }
                else
                {
                    likedList.add(movie2);
                    likeButton.setColorFilter(Color.argb(255,24,65,100));
                    isLiked = true;
                    addLikeInDB();
                }


            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if(isAdded)
                {

                    HomeFragment.arrayList.remove(addPosition);
                    addButton.setColorFilter(Color.argb(255,255,255,255));
                    isAdded = false;

                }
                else {
                    HomeFragment.arrayList.add(movie2);
                    addButton.setColorFilter(Color.argb(255,24,65,100));
                    isAdded = true;

                }

            }
        });


        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewingMovieInfo.this,WatchingActivity.class);
                intent.putExtra(EXTRA_NUMBER,movie_id);
                startActivity(intent);
            }
        });

        //getMoreLikeThis();


        loadData();
        checkForButton(movie_id);
        getFromDB(movie_id);
    }

    private void removeLikeFromDB() {
        removeLikeRunnable runnable = new removeLikeRunnable();
        Thread tr = new Thread(runnable);
        //likeButton.setClickable(false);
        tr.start();
    }

    private void addLikeInDB() {
        addLikeRunnable runnable = new addLikeRunnable();
        Thread tr = new Thread(runnable);
        //likeButton.setClickable(false);
        tr.start();
    }

    private void openSameActivity(int i) {
        Intent intent = new Intent(this,ViewingMovieInfo.class);
        intent.putExtra(EXTRA_NUMBER,similarMovies.get(i).id);
        startActivity(intent);
    }

    /*private void getMoreLikeThis() {
        getMoreRunnable runnable = new getMoreRunnable();
        Thread tr = new Thread(runnable);
        tr.start();
    }*/

    private void saveList() {
        SharedPreferences sharedPreferences= getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(HomeFragment.arrayList);
        String json2 = gson.toJson(likedList);
        editor.putString("task list",json);
        editor.putString("task list2",json2);
        editor.apply();

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list2",null);
        Type type = new TypeToken<ArrayList<HomeFragment.simple_movies>>() {}.getType();
        likedList = gson.fromJson(json, type);

        if(likedList == null){
            likedList = new ArrayList<>();
        }

    }

    @Override
    protected void onStop() {
        saveList();
        super.onStop();
    }

    @Override
    protected void onPause() {
        saveList();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        saveList();
        super.onBackPressed();
    }



    private void checkForButton(int movie_id) {

        for (int i = 0; i < HomeFragment.arrayList.size(); i++) {
            if (HomeFragment.arrayList.get(i).id == movie_id) {
                isAdded = true;
                addPosition = i;
                break;
            }
        }

        for (int i = 0; i<likedList.size();i++){
            if(likedList.get(i).id == movie_id){
                isLiked = true;
                likePosition = i;
                break;
            }

        }
    }


    @Override
    protected void onStart() {
        //loadData();
        super.onStart();
    }

    private void getFromDB(int movie_id) {

        if(movie_id == 0)
        {
            Toast.makeText(this,"not available",Toast.LENGTH_LONG).show();
        }

        else {
            getInfoAsyncTask asyncTask = new getInfoAsyncTask(movie_id);
            asyncTask.execute();
        }

    }

    private String formatDataAsJSON() {
        final JSONObject root = new JSONObject();
        try {


            root.put("id", movie2.id);



        } catch (JSONException e) {
            Log.d("JWP", "Can't format JSON");
        }
        return root.toString();
    }

    //removing likes from database
    class removeLikeRunnable implements  Runnable {


        @Override
        public void run() {

            String urlString ="http://192.168.1.34:8080/movies/removeLike/"+movie2.id; // URL to call
            String data = formatDataAsJSON(); //data to post
            OutputStreamWriter out = null;




            try {

                URL url = new URL(urlString);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setRequestMethod("PUT");
                out = new OutputStreamWriter(
                        httpCon.getOutputStream());
                out.write("Resource content");
                out.close();
                httpCon.getInputStream();



            } catch (IOException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("like removed");



                }
            });




        }


    }

    //adding likes in database
    class addLikeRunnable implements  Runnable {


        @Override
        public void run() {

            String urlString ="http://192.168.1.34:8080/movies/addLike/" + movie2.id; // URL to call
            //String data = formatDataAsJSON(); //data to post
            OutputStreamWriter out = null;




            try {

                URL url = new URL(urlString);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setRequestMethod("PUT");
                 out = new OutputStreamWriter(
                        httpCon.getOutputStream());
                out.write("Resource content");
                out.close();
                httpCon.getInputStream();



            } catch (IOException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("like sent");


                }
            });




        }


    }

    // another thread to get the movie's information
    private class getInfoAsyncTask extends AsyncTask<Void, Void, Void> {
        String data;
        int id;
        Movie movie = new Movie();


        getInfoAsyncTask(int id){
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {


            InputStream inputStream = null;
            try {

                URL url = new URL("http://192.168.1.34:8080/movies/" + id);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //bufferedReader.skip(20);
                String line ="";
                data = line;




                while(line!=null) {
                    if (isCancelled()) {
                        break;
                    } else {
                        line = bufferedReader.readLine();
                        data = data + line;
                    }

                }

                JSONObject JO = new JSONObject(data);
                movie.id = JO.getInt("id");
                movie.name = JO.getString("name");
                movie.releaseDate = JO.getString("date");
                movie.original_language = JO.getString("og_lang");
                movie.vote_average = JO.getString("rating");
                movie.genres = JO.getString("genres");
                movie.overview = JO.getString("overview");
                movie.runtime = JO.getString("runtime");
                movie.age_rating=JO.getString("age_rating");
                movie.director=JO.getString("director");
                movie.notable_cast = JO.getString("notable_cast");





                url = new URL("http://192.168.1.34:8080/movies/similar/" + movie.id);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //bufferedReader.skip(20);
                line ="";
                data = line;




                while(line!=null) {
                    if (isCancelled()) {
                        break;
                    } else {
                        line = bufferedReader.readLine();
                        data = data + line;
                    }

                }

                JSONArray JA = new JSONArray(data);

                System.out.println(JA.length());

                for(int i=0;i<3 && i<JA.length();i++)
                {
                    JSONObject JO2= JA.getJSONObject(i);
                    HomeFragment.simple_movies similar = new HomeFragment.simple_movies(JO2.getInt("id"),JO2.getString("name"));
                    System.out.println(JO2.getInt("id"));
                    similarMovies.add(similar);

                }



            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(SearchingMovieActivity.this,"FUCK",Toast.LENGTH_LONG).show();
            }finally {
                try { if (inputStream != null) inputStream.close(); } catch(IOException e) {}


            }




            return null;
        }





        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayInfo(movie);
            //adapter.notifyDataSetChanged();
            // stop animating Shimmer and hide the layout
            // mShimmerViewContainer.stopShimmer();
            //mShimmerViewContainer.setVisibility(View.GONE);
        }


    }


    //responsible for attaching the info to the UI. must be called after getListInfo function.
    private void displayInfo(Movie movie) {

        TextView txtview = findViewById(R.id.textView_year);
        TextView txtview2 = findViewById(R.id.age_rating);
        TextView txtview3 = findViewById(R.id.textView_runtime);
        TextView txtview4 = findViewById(R.id.textView_overview);
        TextView txtview5 = findViewById(R.id.textView_details);
        ImageView imgview = findViewById(R.id.Poster);



        txtview.setText(movie.releaseDate.substring(0,4));
        txtview2.setText(movie.age_rating);
        txtview3.setText(movie.runtime);
        txtview4.setText(movie.overview);
        txtview5.setText("Director: "+movie.director+'\n' +"Cast: "+ movie.notable_cast);
        movie2 = new HomeFragment.simple_movies(movie.id,movie.name);

        if(isAdded)
        {
            addButton.setColorFilter(Color.argb(255,24,65,100));
        }

        if(isLiked)
        {
            likeButton.setColorFilter(Color.argb(255,24,65,100));
        }

        String pstr = "http://192.168.1.34:8080/movies/image/"+movie.id+".jpg";;


        Picasso.get().load(pstr).placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgview, new com.squareup.picasso.Callback() {

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


        //Loading additional movie images.
        pstr = "http://192.168.1.34:8080/movies/image/"+similarMovies.get(0).id+".jpg";
        Picasso.get().load(pstr).placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(similarImage1, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        System.out.println("yesss");
                        similarImage1.setClickable(true);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        System.out.println("NOOOOO");
                    }
                });


        /*Intent i = new Intent(OldActivity.this, NewActivity.class);
// set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);*/

        if(similarMovies.size() >1) {
            pstr = "http://192.168.1.34:8080/movies/image/" + similarMovies.get(1).id + ".jpg";

            Picasso.get().load(pstr).placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(similarImage2, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            System.out.println("yesss");
                            similarImage2.setClickable(true);
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            System.out.println("NOOOOO");
                        }
                    });

        }else {
            similarImage2.setVisibility(View.INVISIBLE);
        }


        if(similarMovies.size() > 2) {
            pstr = "http://192.168.1.34:8080/movies/image/" + similarMovies.get(2).id + ".jpg";

            Picasso.get().load(pstr).placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(similarImage3, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            System.out.println("yesss");
                            similarImage3.setClickable(true);
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            System.out.println("NOOOOO");
                            similarImage3.setClickable(false);
                        }
                    });

        } else {
            similarImage3.setVisibility(View.INVISIBLE);
        }





        likeButton.setClickable(true);
        addButton.setClickable(true);


    }
}
