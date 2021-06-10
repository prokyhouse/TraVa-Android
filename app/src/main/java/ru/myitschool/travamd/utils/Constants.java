package ru.myitschool.travamd.utils;

/**
 * Created by kirillprokofev on 03.06.17.
 */

public class Constants {

    public static String API_KEY = "2e774b038b2dc15a1db7397f1b6b63a7";

    //URL для осуществления запросов.
    public static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    public static String TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated";
    public static String UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming";
    public static String ACTOR_URL = "https://api.themoviedb.org/3/person/";
    public static String COVER_W780_URL = "https://image.tmdb.org/t/p/w780";
    public static String POPULAR_SERIES_URL = "https://api.themoviedb.org/3/tv/popular";
    public static String IN_THEATRE_URL = "https://api.themoviedb.org/3/movie/now_playing";

    //Строки ошибок.
    public static String NOT_FOUND = "Отсутствует";
    public static String NOT_RATED = "Нет оценки";
    public static String ERROR = "Ошибка. Попробуйте еще раз.";
}
