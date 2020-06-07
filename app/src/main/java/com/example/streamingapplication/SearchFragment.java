package com.example.streamingapplication;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

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

import static android.content.Context.SEARCH_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.streamingapplication.HomeFragment.EXTRA_NUMBER;

public class SearchFragment extends Fragment implements RecyclerViewSearchAdapter.OnNoteListener {
    static ArrayList<HomeFragment.simple_movies> searchList =new ArrayList<>();
    View rootView;
    private RecyclerViewSearchAdapter adapter;
    private Handler mainHandler = new Handler();
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);




        return rootView;
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

    public void setUpRecyclerView(View rootView, ArrayList<HomeFragment.simple_movies> List) {
        recyclerView = rootView.findViewById(R.id.searchRecylerView);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new RecyclerViewSearchAdapter(getActivity(), List,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


    }



    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        SearchView searchView = view.findViewById(R.id.searchView);
        AppBarLayout appBarLayout = view.findViewById(R.id.appbar2);
        appBarLayout.setExpanded(true);

        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        setUpRecyclerView(view, searchList);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList.clear();
                getFromDB(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList.clear();
                getFromDB(newText);
                return false;
            }
        });








    }

    private void getFromDB(String query) {

        getSearchListRunnable runnable = new getSearchListRunnable(query);
        Thread tr = new Thread(runnable);
        tr.start();
    }

    @Override
    public void onNoteClick(int movie_id) {
        //Toast.makeText(MainPage.this,Integer.toString(movie_id),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(),ViewingMovieInfo.class);
        intent.putExtra(EXTRA_NUMBER,movie_id);
        startActivity(intent);
    }

    class getSearchListRunnable implements  Runnable {
        String query;

        public getSearchListRunnable(String text) {
            this.query = text;
        }

        @Override
        public void run() {

            InputStream inputStream = null;
            try {

                URL url = new URL("http://192.168.1.34:8080/movies/search/"+ query);
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


                {
                    for(int i=0 ; i<JA.length();i++){
                        JSONObject JO = JA.getJSONObject(i);
                        searchList.add(new HomeFragment.simple_movies(JO.getInt("id"),JO.getString("name")));

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
                        setUpRecyclerView(rootView,searchList);



                    }
                });

            }


        }


    }
}
