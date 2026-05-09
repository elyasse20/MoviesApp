package com.example.moviesapp;

public class MyMovieData {

    private String movieName;
    private String movieDate;
    private String movieImage;
    private String movieDescription;
    private int movieId;

    public MyMovieData(int movieId, String movieName, String movieDate, String movieImage) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieDate = movieDate;
        this.movieImage = movieImage;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public void setMovieDate(String movieDate) {
        this.movieDate = movieDate;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }
}