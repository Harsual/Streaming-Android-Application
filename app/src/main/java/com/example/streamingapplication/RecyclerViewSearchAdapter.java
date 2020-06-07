package com.example.streamingapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewSearchAdapter extends RecyclerView.Adapter<RecyclerViewSearchAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<HomeFragment.simple_movies> movies = new ArrayList<>();
    private OnNoteListener mNoteListener;
    private Context mContext;
    public ImageView poster;


    public RecyclerViewSearchAdapter(Context mContext,ArrayList<HomeFragment.simple_movies> mMovies, OnNoteListener onNoteListener)  {
        this.movies = mMovies;
        this.mContext = mContext;
        this.mNoteListener = onNoteListener;

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_movie_item,viewGroup,false);

        return new ViewHolder(view,mNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder2000: called.");


        holder.Movie_name.setText(movies.get(position).movieName);


        String pstr = "http://192.168.1.34:8080/movies/image/" + movies.get(position).id+".jpg";
        Picasso.get().load(pstr).placeholder(R.color.black_overlay)
                .error(R.color.black_overlay)
                .into(poster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

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

    /*@Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HomeFragment.simple_movies> filteredList = new ArrayList<>();


            /*if(constraint == null || constraint.length() == 0){

            }


            if(constraint != null){

                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for( HomeFragment.simple_movies movie : moviesFull ){
                        if(movie.getMovieName().toLowerCase().contains(filterPattern)){
                            filteredList.add(movie);


                        }
                    }
                }




            FilterResults results = new FilterResults();
            results.values = filteredList;

            return  results;


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(movies != null)
            movies.clear();


            movies.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };*/


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Movie_name;
       // RelativeLayout parentLayout;
        OnNoteListener onNoteListener;

        //ImageView imageview;


        public ViewHolder(View itemView,OnNoteListener onNoteListener){
            super(itemView);

            poster = itemView.findViewById(R.id.searchImage);
            Movie_name = itemView.findViewById(R.id.searchMovieName);

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
