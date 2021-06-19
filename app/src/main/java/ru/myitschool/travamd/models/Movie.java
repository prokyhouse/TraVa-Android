package ru.myitschool.travamd.models;


public class Movie {

    private String cover_url, movieName, movieNameOriginal, movieYear, overview, backdropPath,
            vote_average, genres;
    private long movieId;

    public Movie(String cover_url, String movieName, String movieNameOriginal, String movieYear, long movieId, String overview) {
        this.cover_url = cover_url;

        this.movieName = movieName;
        this.movieNameOriginal = movieNameOriginal;
        this.movieId = movieId;
        this.movieYear = movieYear;
        this.overview = overview;

    }

    public Movie(String backdropPath, String vote_average, String genres) {
        this.backdropPath = backdropPath;
        this.vote_average = vote_average;
        this.genres = genres;
    }

    public String getCover_url() {
        return cover_url;
    }

    public String getMovieNameOriginal() {
        return movieNameOriginal;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public long getMovieId() {
        return movieId;
    }

    public String getMovieOverview() {
        return overview;
    }

    public String getMovieBackdropPath() {
        return backdropPath;
    }

    public String getMovieVoteAverage() {
        return vote_average;
    }

    public String getGenres() {
        return genres;
    }
}
