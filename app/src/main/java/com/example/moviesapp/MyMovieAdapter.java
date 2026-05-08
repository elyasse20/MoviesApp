package com.example.moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyMovieAdapter extends RecyclerView.Adapter<MyMovieAdapter.ViewHolder> implements Filterable {

    MyMoviesData[] myMovieData;
    MyMoviesData[] myMovieDataFull; // For filtering
    Context context;

    public MyMovieAdapter(MyMoviesData[] myMovieData, Context context) {
        this.myMovieData = myMovieData;
        this.myMovieDataFull = myMovieData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyMoviesData movie = myMovieData[position];
        holder.textMovie.setText(movie.getName());
        holder.DateMovie.setText(movie.getDate());

        // Use Glide to load the image from URL
        String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getImage();
        Glide.with(context)
                .load(imageUrl)
                .into(holder.movieImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, movie.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myMovieData.length;
    }

    @Override
    public Filter getFilter() {
        return movieFilter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        TextView textMovie;
        TextView DateMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.img_png);
            textMovie = itemView.findViewById(R.id.textViewName);
            DateMovie = itemView.findViewById(R.id.textViewDate);
        }
    }

    private Filter movieFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MyMoviesData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(Arrays.asList(myMovieDataFull));
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyMoviesData movie : myMovieDataFull) {
                    if (movie.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(movie);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<?> resultList = (List<?>) results.values;
            if (resultList != null) {
                myMovieData = resultList.toArray(new MyMoviesData[0]);
                notifyDataSetChanged();
            }
        }
    };
}
