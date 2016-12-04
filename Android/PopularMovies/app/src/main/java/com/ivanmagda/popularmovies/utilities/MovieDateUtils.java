package com.ivanmagda.popularmovies.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class MovieDateUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private MovieDateUtils() {
    }

    public static Date fromString(String string) {
        try {
            return DATE_FORMAT.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
