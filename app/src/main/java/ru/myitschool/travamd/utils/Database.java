package ru.myitschool.travamd.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Database extends SQLiteOpenHelper implements BaseColumns {
    private static Database mInstance = null;
    public static final String MOVIE_ID_COLUMN = "movieId";
    private static final String DATABASE_NAME = "favorite_movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "Movies";

    //Получение экзмепляра класса помощника.
    public static Database getInstance(Context context){
        if (mInstance == null) {
            mInstance = new Database(context.getApplicationContext());
        }
        return mInstance;
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DATABASE_TABLE + " (" +
                MOVIE_ID_COLUMN + " VARCHAR(20)); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Добавление фильма в список любимых.
    public static void addMovie(Context context, long movieId){
        Database db = getInstance(context);
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(MOVIE_ID_COLUMN, movieId);
        database.insert(DATABASE_TABLE, null, newValues);
        db.close();
        database.close();
        newValues.clear();
    }
    //Проверка на то, существует ли данный фильм в списке любимых.
    public static boolean isMovieExist(Context context, long movieId){
        Database db = getInstance(context);
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = database.query(DATABASE_TABLE,new String[]{MOVIE_ID_COLUMN},MOVIE_ID_COLUMN+" = ?",new String[]{movieId+""},null,null,null);
        if(cursor==null)return false;
        boolean isExist=cursor.getCount()>0;
        cursor.close();
        return isExist;
    }

    //Удаление фильма из списка любимых.
    public static void removeMovie(Context context, long movieId) {
        Database db = getInstance(context);
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(DATABASE_TABLE,MOVIE_ID_COLUMN+" = ?",new String[]{movieId+""});
        db.close();
        database.close();

    }
    public static long[] getAllMovie(Context context){
        Database db = getInstance(context);
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = database.query(DATABASE_TABLE,new String[]{MOVIE_ID_COLUMN},null,null,null,null,null);
        long[] movies = new long[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < movies.length; i++) {
            movies[i]=cursor.getLong(0);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        db.close();
        return movies;
    }
}