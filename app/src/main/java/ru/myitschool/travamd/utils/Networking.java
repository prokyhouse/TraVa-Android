package ru.myitschool.travamd.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Networking {

    public static URL buildUrl(String queryUrl) {
        return buildUrl(queryUrl, null);
    }

    public static URL buildUrl(String queryUrl, String page) {
        Uri.Builder builder = Uri.parse(queryUrl).buildUpon()
                .appendQueryParameter("api_key", Constants.API_KEY)
                .appendQueryParameter("language", "ru-RU");
        if (!TextUtils.isEmpty(page)) {
            builder.appendQueryParameter("page", page);
        }
        builder.appendQueryParameter("region", "US");
        Uri uri = builder.build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = connection.getInputStream();

            Scanner scanner = new Scanner(inputStream);

            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            connection.disconnect();
            connection.connect();
        }

    }

}
