package com.ivanmagda.popularmovies.utilities.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    interface Parcelable<T> {
        T parse(JSONObject jsonObject) throws JSONException;
    }

    private JsonUtils() {
    }

    public static <T> List<T> parseJsonArray(JSONArray jsonArray, Parcelable<T> parcelable)
            throws JSONException {
        ArrayList<T> parsedArray = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T parsed = parcelable.parse(jsonObject);
            if (parsed != null) {
                parsedArray.add(parsed);
            }
        }

        return parsedArray;
    }

}
