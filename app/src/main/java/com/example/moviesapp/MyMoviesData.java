package com.example.moviesapp;

public class MyMoviesData {

    private int id;
    private String Name;
    private String Date;
    private String Image;
    private String description;

    public MyMoviesData(int id, String name, String date, String image) {
        this.id = id;
        this.Name = name;
        this.Date = date;
        this.Image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
