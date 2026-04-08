package com.example.movieticketapp;

public class Movie {
    private String title;
    private String genre;
    private String imageUrl;

    public Movie() {} // Cần thiết cho Firebase

    public Movie(String title, String genre, String imageUrl) {
        this.title = title;
        this.genre = genre;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}