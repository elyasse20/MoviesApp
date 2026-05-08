package com.example.moviesapp;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TMDB_API_KEY = "5cb8043a3b7e0c1210dacce4482f075e";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private MyMovieAdapter myMovieAdapter;
    private EditText searchEditText;
    // Make sure these are declared at the top of your MainActivity class:
    // private EditText searchEditText;
    // private MyMovieAdapter myMovieAdapter;
    // private static final String BASE_URL = "...";
    // private static final String TMDB_API_KEY = "...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Your modern Edge-to-Edge UI setup
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Initialize Views
        searchEditText = findViewById(R.id.editTextSearch);
        RecyclerView recyclerView = findViewById(R.id.recycler_view); // Kept your ID
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Fetch data from API (Replaces your hardcoded dummy data)
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BASE_URL + "?api_key=" + TMDB_API_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            // Note: Make sure your data class is named MyMovieData to match this
                            MyMoviesData[] movies = new MyMoviesData[results.length()];

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject movieObject = results.getJSONObject(i);
                                int id = movieObject.getInt("id");
                                String title = movieObject.getString("title");
                                String releaseDate = movieObject.getString("release_date");
                                String imageUrl = movieObject.getString("poster_path");

                                movies[i] = new MyMoviesData(id, title, releaseDate, imageUrl);
                            }

                            myMovieAdapter = new MyMovieAdapter(movies, MainActivity.this);
                            recyclerView.setAdapter(myMovieAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", "Error occurred: " + error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);

        // 4. Search Filter Logic
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter movie list based on search input
                if (myMovieAdapter != null) {
                    myMovieAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }
}