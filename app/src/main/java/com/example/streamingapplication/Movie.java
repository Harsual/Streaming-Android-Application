package com.example.streamingapplication;

import java.util.Calendar;
import java.util.Comparator;

public class Movie {

        String name;
        int id;
        Integer budget;
        String runtime;
        float Rating;
        Calendar dateAdded;
        String releaseDate;
        String Poster;
        String vKey;
        String vote_average;
        String overview;
        String age_rating;
        String director;
        String notable_cast;
        String tagline;
        String moreInfo;
        String genres;
        String original_language;
        String imdb_id;
        boolean isAdded = false;

        // Constructor
        public Movie(){
            this.name = "";
            this.id =0;
            this.Rating = 0;
            this.tagline="";
            this.moreInfo = "";
            this.vote_average="";
            this.runtime = "";
            this.original_language="en";
            this.budget = 0;
            this.overview ="";
            this.releaseDate ="";
            this.vKey = "";
            this.Poster = "";
            this.imdb_id="";
            this.genres="";
            this.age_rating="";
            this.director="";
            this.notable_cast="";



        }
        public Movie(String name, int r)
        {
            this.name = name;
            this.Rating = r;
            this.dateAdded = Calendar.getInstance();
            this.Poster = "";

        }

        public Movie(String name, String relDate ,String image,int id,String vote)
        {
            this.name = name;
            this.releaseDate = relDate;
            this.dateAdded = Calendar.getInstance();
            this.Poster = image;
            this.id = id;
            this.vote_average = vote;

        }

        // Used to print movie details in main()
        public String toString()
        {
            return this.name + " " + this.Rating;
        }

    /*class SortbyRating implements Comparator<Movie>
    {
        // Used for sorting in descending order of
        // roll number
        public int compare(Movie a, Movie b)
        {
            return Float.compare(b.Rating2,a.Rating2);
        }
    }*/


}




