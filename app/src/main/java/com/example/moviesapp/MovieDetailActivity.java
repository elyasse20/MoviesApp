package com.example.moviesapp;

import static android.content.ContentValues.TAG;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private TextView descriptionTextView;
    private TextView Name;
    private ImageView img;
    private String trailerKey;
    private RequestQueue requestQueue;
    private Button playButton;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private GoogleMap mMap;
    private List<LatLng> cinemaLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // 1. Les IDs t9addou bach y-matchiw m3a activity_movie_detail.xml jdid
        descriptionTextView = findViewById(R.id.Details);
        img = findViewById(R.id.imageview);
        Name = findViewById(R.id.textName);
        playButton = findViewById(R.id.playButton);

        requestQueue = Volley.newRequestQueue(this);

        // Retrieve movie ID from Intent extras
        int movieId = getIntent().getIntExtra("movieId", -1);
        if (movieId != -1) {
            fetchMovieDetails(movieId);
        } else {
            descriptionTextView.setText("No movie ID provided");
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTrailer();
            }
        });

        cinemaLocations.add(new LatLng(33.596460, -7.615480)); // Example cinema location

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void fetchMovieDetails(int movieId) {
        // 2. Dernalha la clé dyalek bach tbda tjib les données d'sah
        String TMDB_API_KEY = "5cb8043a3b7e0c1210dacce4482f075e";
        String movieDetailsUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + TMDB_API_KEY;
        String movieVideosUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + TMDB_API_KEY;

        JsonObjectRequest movieDetailsRequest = new JsonObjectRequest(Request.Method.GET, movieDetailsUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "Movie Details Response: " + response.toString());

                    String movieName = response.getString("title");
                    String movieDescription = response.getString("overview");
                    String imageUrl = "https://image.tmdb.org/t/p/w500" + response.getString("poster_path");

                    Name.setText(movieName);
                    descriptionTextView.setText(movieDescription);

                    Glide.with(MovieDetailActivity.this).load(imageUrl).into(img);
                } catch (JSONException e) {
                    if (e.getMessage().contains("title")) {
                        Log.e(TAG, "Error: Missing 'title' key in response");
                    } else if (e.getMessage().contains("overview")) {
                        Log.e(TAG, "Error: Missing 'overview' key in response");
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error fetching movie details: " + error.getMessage());
                descriptionTextView.setText("Failed to fetch movie details");
            }
        });

        JsonObjectRequest movieVideosRequest = new JsonObjectRequest(Request.Method.GET, movieVideosUrl, null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("results")) {
                                JSONArray results = response.getJSONArray("results");
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject video = results.getJSONObject(i);
                                    if (video.getString("type").equals("Trailer")) {
                                        trailerKey = video.getString("key");
                                        Log.d(TAG, "Trailer Key: " + trailerKey);
                                        break; // Assuming you only need first trailer key
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error fetching movie videos: " + error.getMessage());
                Toast.makeText(MovieDetailActivity.this, "Trailer not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Add both requests to the RequestQueue
        requestQueue.add(movieDetailsRequest);
        requestQueue.add(movieVideosRequest);
    }

    private void playTrailer() {
        if (trailerKey != null && !trailerKey.isEmpty()) {
            // L'Intent lwel kay7awel y7el l'application YouTube directe
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));

            // L'Intent tani kiykhdem ka plan B, kay7el l'vidéo f Google Chrome ila makhdmatch l'appli
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));

            try {
                startActivity(appIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        } else {
            Toast.makeText(this, "Trailer not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            LatLng cinemaLocation = new LatLng(33.596460, -7.615480);
            addCinemaMarker(cinemaLocation);
            moveToCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void addCinemaMarker(LatLng cinemaLocation) {
        mMap.addMarker(new MarkerOptions().position(cinemaLocation).title("Cinema").snippet("Location of the cinema"));
    }

    private void moveToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = null;
            try {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                return;
            }

            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            } else {
                Toast.makeText(this, "Last known location not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                moveToCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}