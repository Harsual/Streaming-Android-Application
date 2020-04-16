package com.example.streamingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import static com.example.streamingapplication.MainPage.EXTRA_NUMBER;

public class ViewingMovieInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        int movie_id = intent.getIntExtra(EXTRA_NUMBER,0);

        setContentView(R.layout.activity_viewing_movie_info);
        Button play_btn = findViewById(R.id.play_button);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewingMovieInfo.this,WatchingActivity.class);
                intent.putExtra(EXTRA_NUMBER,movie_id);
                startActivity(intent);
            }
        });


        getFromDB(movie_id);
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




    }
}
