package com.example.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyMovieAdapter extends RecyclerView.Adapter<MyMovieAdapter.ViewHolder> implements Filterable {

    private MyMovieData[] originalMovieData; // L'array l'asli
    private List<MyMovieData> filteredMovieData; // La liste li kante7kmo fiha b'la recherche
    private Context context;

    public MyMovieAdapter(MyMovieData[] myMovieData, Context context) {
        this.originalMovieData = myMovieData;
        // Kanjm3o les données f la liste bach t-filtrer b'shoula
        this.filteredMovieData = new ArrayList<>(Arrays.asList(myMovieData));
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_movie_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Kan-jbdou l'film mn la liste filtrée
        final MyMovieData movieData = filteredMovieData.get(position);

        holder.textMovie.setText(movieData.getMovieName());
        holder.DateMovie.setText(movieData.getMovieDate());

        // Glide bach njibou l'image
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movieData.getMovieImage())
                .into(holder.movieImage);

        // Mnin tkliqi 3la l'film ydik l'MovieDetailActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movieId", movieData.getMovieId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredMovieData.size(); // Taille dyal la liste filtrée
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
            // Had les IDs t9addou m3a l'XML jdid
            movieImage = itemView.findViewById(R.id.imageview);
            textMovie = itemView.findViewById(R.id.textName);
            DateMovie = itemView.findViewById(R.id.textdate);
        }
    }

    private Filter movieFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MyMovieData> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(Arrays.asList(originalMovieData));
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyMovieData movie : originalMovieData) {
                    if (movie.getMovieName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(movie);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredMovieData.clear();
            filteredMovieData.addAll((List<MyMovieData>) results.values);
            notifyDataSetChanged();
        }
    };
}