package com.example.streamingapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements  RecyclerViewAdapter.OnNoteListener {
    public static final String EXTRA_NUMBER = "com.example.streamingapplication.EXTRA_NUMBER";
    static ArrayList<simple_movies> arrayList=new ArrayList<>();
    static ArrayList<simple_movies> adventure =new ArrayList<>();
    static ArrayList<simple_movies> action =new ArrayList<>();
    static ArrayList<simple_movies> comedy =new ArrayList<>();
    static ArrayList<simple_movies> family =new ArrayList<>();
    static ArrayList<simple_movies> fantasy =new ArrayList<>();
    static ArrayList<simple_movies> animation =new ArrayList<>();
    static ArrayList<simple_movies> drama =new ArrayList<>();
    static ArrayList<simple_movies> horror =new ArrayList<>();
    static ArrayList<simple_movies> history =new ArrayList<>();
    static ArrayList<simple_movies> western =new ArrayList<>();
    static ArrayList<simple_movies> thriller =new ArrayList<>();
    static ArrayList<simple_movies> crime = new ArrayList<>();
    static ArrayList<simple_movies> documentary =new ArrayList<>();
    static ArrayList<simple_movies> science_fiction =new ArrayList<>();
    static ArrayList<simple_movies> mystery =new ArrayList<>();
    static ArrayList<simple_movies> music =new ArrayList<>();
    static ArrayList<simple_movies> romance =new ArrayList<>();
    static ArrayList<simple_movies> war =new ArrayList<>();
    static ArrayList<simple_movies> tv_movie =new ArrayList<>();



    private static final String TAG = "MainPageActivity";
    RecyclerViewAdapter adapter;
    static int []id_list= new int[20];
    private Handler mainHandler = new Handler();

    private Toolbar toolbarTop;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private NestedScrollView nestedScrollView;
    private View rootView;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nestedScrollView = rootView.findViewById(R.id.scroll_view);

        toolbarTop = rootView.findViewById(R.id.toolbar);

        toolbarTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Clicked");
                nestedScrollView.fullScroll(ScrollView.FOCUS_UP);

            }
        });

        loadData();

        initMainRecyclerView(arrayList,R.id.movies_recyclerview_mylist);
        initMainRecyclerView(action, R.id.movies_recyclerview);
        initMainRecyclerView(comedy, R.id.movies_recyclerview2);
        initMainRecyclerView(family, R.id.movies_recyclerview3);

        final int id = 454626;
        ImageView img = setUpImage(id);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainPage.this,Integer.toString(id),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(),ViewingMovieInfo.class);
                intent.putExtra(EXTRA_NUMBER,id);
                startActivity(intent);

            }
        });

        getLists();

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home,container,false);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initMainRecyclerView(arrayList,R.id.movies_recyclerview_mylist);
    }

    private ImageView setUpImage(int id) {
        String pstr = "http://192.168.1.34:8080/movies/image/"+id+".jpg";
        ImageView poster = rootView.findViewById(R.id.imageView);


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG,"Back Button Pressed");
        if (item.getItemId() == android.R.id.home) {
            Log.i(TAG, "home on backpressed");
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLists() {
        getListsRunnable runnable = new getListsRunnable();
        Thread tr = new Thread(runnable);
        tr.start();
    }

    public void scrollToTop(){
        nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    private void initMainRecyclerView(ArrayList<simple_movies> arr,int id){
        Log.d(TAG, "initRecyclerView Main: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView recyclerView = rootView.findViewById(id);
        adapter = new RecyclerViewAdapter(getActivity(), arr,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }




    @Override
    public void onNoteClick(int movie_id) {
        //Toast.makeText(MainPage.this,Integer.toString(movie_id),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(),ViewingMovieInfo.class);
        intent.putExtra(EXTRA_NUMBER,movie_id);
        startActivity(intent);
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences",MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list",null);
        Type type = new TypeToken<ArrayList<simple_movies>>() {}.getType();
        arrayList = gson.fromJson(json, type);

        if(arrayList == null){
            arrayList = new ArrayList<>();
        }

    }




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
                String line = "";
                String data = line;


                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;


                }

                JSONArray JA = new JSONArray(data);

                int counter = 0;


                for (int j = 0; j < JA.length(); j++) {

                    JSONObject JO = JA.getJSONObject(j);

                    switch (JO.getInt("genre")) {
                        case 12:
                            adventure.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 28:
                            action.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 35:
                            comedy.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 10751:
                            family.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 14:
                            fantasy.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 16:
                            animation.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 18:
                            drama.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 27:
                            horror.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 36:
                            history.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 37:
                            western.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 53:
                            thriller.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 80:
                            crime.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 99:
                            documentary.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 878:
                            science_fiction.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 9648:
                            mystery.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 10402:
                            music.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 10749:
                            romance.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 10752:
                            war.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;
                        case 10770:
                            tv_movie.add(new simple_movies(JO.getInt("id"), JO.getString("movie_name")));
                            break;


                        default:

                    }


                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                } catch (IOException ignored) {
                }
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



    static class Movie_List
    {

        int id;
        String listName;
        int [] movie_ids = new int[20];

        Movie_List(int id,String listname){
            this.id = id;
            this.listName = listname;
        }

    }


    static class simple_movies
    {

        int id;
        String movieName;

        simple_movies(int id,String MovieName){
            this.id = id;
            this.movieName = MovieName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }
    }

}
