package com.example.streamingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
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
import java.util.Objects;

public class MainPage extends AppCompatActivity{
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment(),"MainTag").commit();

        bottomNav = findViewById(R.id.bottom_navigation);


        bottomNav.setOnNavigationItemSelectedListener(navListener);

        bottomNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.nav_home)
                {
                    NestedScrollView nestedScrollView = findViewById(R.id.scroll_view);
                    nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                }

                if(menuItem.getItemId() == R.id.nav_search)
                {
                    RecyclerView recyclerView = findViewById(R.id.searchRecylerView);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if(layoutManager.findFirstVisibleItemPosition() != 0 && recyclerView.getAdapter().getItemCount() != 0)
                    {
                        recyclerView.smoothScrollToPosition(0);
                    }

                    else {
                        SearchView searchView = findViewById(R.id.searchView);

                        searchView.setIconifiedByDefault(false);
                        searchView.setFocusable(true);
                        searchView.setIconified(false);
                        searchView.requestFocusFromTouch();
                    }

                }

            }
        });




    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(getSupportFragmentManager().findFragmentByTag("MainTag")!= null){
            //getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag("MainTag")).commit();
            bottomNav.setSelectedItemId(R.id.nav_home);

        }

        /*if(fragmentManager.findFragmentByTag("SearchTag")!= null)
            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("SearchTag"))).commit();

        if(fragmentManager.findFragmentByTag("SettingsTag")!= null)
            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("SettingsTag"))).commit();*/

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    FragmentManager fragmentManager = getSupportFragmentManager();

                    String MainTag = "MainTag";
                    String SearchTag = "SearchTag";
                    String SettingsTag = "SettingsTag";

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            if(fragmentManager.findFragmentByTag(MainTag)!= null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(MainTag)).commit();
                            }else {
                                fragmentManager.beginTransaction().add(R.id.fragment_container,selectedFragment,"MainTag").commit();
                            }

                            if(fragmentManager.findFragmentByTag(SearchTag)!= null)
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("SearchTag"))).commit();

                            if(fragmentManager.findFragmentByTag(SettingsTag)!= null)
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("SettingsTag"))).commit();

                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            if(fragmentManager.findFragmentByTag(SearchTag)!= null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(SearchTag)).commit();
                            }else {
                                fragmentManager.beginTransaction().add(R.id.fragment_container,selectedFragment,"SearchTag").commit();
                            }

                            if(fragmentManager.findFragmentByTag(MainTag)!= null)
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("MainTag"))).commit();

                            if(fragmentManager.findFragmentByTag(SettingsTag)!= null)
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("SettingsTag"))).commit();





                            break;
                        case R.id.nav_list:
                            selectedFragment = new SettingsFragment();
                            if(fragmentManager.findFragmentByTag(SettingsTag)!= null){
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(SettingsTag)).commit();
                            }else {
                                fragmentManager.beginTransaction().add(R.id.fragment_container,selectedFragment,"SettingsTag").commit();
                            }

                            if(fragmentManager.findFragmentByTag(SearchTag)!= null)
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("SearchTag"))).commit();

                            if(fragmentManager.findFragmentByTag(MainTag)!= null)
                            fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("MainTag"))).commit();



                            break;



                    }

                return true;

                }


            };




}
