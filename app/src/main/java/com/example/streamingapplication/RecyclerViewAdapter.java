package com.example.streamingapplication;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;






public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<MainPage.Movie_List> movie_lists ;
    private ArrayList<MainPage.simple_movies> movies;
    private Context mContext;
    public ImageView poster;
    private OnNoteListener mNoteListener;


    public RecyclerViewAdapter(Context mContext, ArrayList<MainPage.simple_movies> mMovies,OnNoteListener onNoteListener)  {
        this.movies = mMovies;
        this.mContext = mContext;
        this.mNoteListener = onNoteListener;

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_image,viewGroup,false);
        ViewHolder holder = new ViewHolder(view,mNoteListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder2: called.");

        String pstr = "http://192.168.1.34:8080/movies/image/"+movies.get(position).id+".jpg";



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



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //TextView listname;
       // RelativeLayout parentLayout;
        OnNoteListener onNoteListener;

        //ImageView imageview;


        public ViewHolder(View itemView,OnNoteListener onNoteListener){
            super(itemView);

            poster = itemView.findViewById(R.id.imageView2);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            onNoteListener.onNoteClick(movies.get(getAdapterPosition()).id);


        }


    }

    public interface OnNoteListener{
        void onNoteClick(int movie_id);
    }

}
