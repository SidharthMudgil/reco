package com.sidharth.reco.recommender;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.sidharth.reco.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SongRecommender {
    private static JSONArray data;

    public static void initializeSongData(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Initializing Reco");
        dialog.setMessage("Please wait while reco is getting ready");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(() -> {
                    try {
                        data = loadJSONFromAssets(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .doWhenFinished(o -> {
                    if (data == null) {
                        throw new RuntimeException("Array List Empty");
                    } else {
                        dialog.dismiss();
                    }
                }).create().start();
    }

    private static JSONArray loadJSONFromAssets(Context context) {
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            String data = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(data);
            return jsonObject.getJSONArray("results");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void getResult() {

    }

    public static String getMoodSong(int mood) {
        getResult();
        return null;
    }

    public static String getNewSong() {
        getResult();
        return null;
    }

    public static ArrayList<String> getSimilarSongs() {
        getResult();
        return null;
    }
}
