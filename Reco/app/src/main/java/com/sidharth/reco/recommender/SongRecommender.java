package com.sidharth.reco.recommender;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.sidharth.reco.MainActivity;
import com.sidharth.reco.chat.model.SongModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;

public class SongRecommender {
    private static final int totalSongs = 50014;

    private static JSONArray songsData;
    private static final int MOOD_HAPPY = 0;
    private static final int MOOD_CALM = 1;
    private static final int MOOD_ANXIOUS = 2;
    private static final int MOOD_ENERGETIC = 3;

    public static void initializeSongData(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Initializing Reco");
        dialog.setMessage("Please wait while reco is getting ready");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(() -> {
                    try {
                        songsData = loadJSONFromAssets(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .doWhenFinished(o -> {
                    if (songsData == null) {
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

    private static double getDistance(SongFeatureModel playedSong, SongFeatureModel currSong) {
        double acousticness = Math.pow(playedSong.getAcousticness() - currSong.getAcousticness(), 2);
        double danceability = Math.pow(playedSong.getDanceability() - currSong.getDanceability(), 2);
        double energy = Math.pow(playedSong.getEnergy() - currSong.getEnergy(), 2);
        double instrumentalness = Math.pow(playedSong.getInstrumentalness() - currSong.getInstrumentalness(), 2);
        double liveness = Math.pow(playedSong.getLiveness() - currSong.getLiveness(), 2);
        double loudness = Math.pow(playedSong.getLoudness() - currSong.getLoudness(), 2);
        double speechiness = Math.pow(playedSong.getSpeechiness() - currSong.getSpeechiness(), 2);
        double tempo = Math.pow(playedSong.getTempo() - currSong.getTempo(), 2);
        double valence = Math.pow(playedSong.getValence() - currSong.getValence(), 2);
        return Math.sqrt(acousticness + danceability + energy + instrumentalness + liveness + loudness + speechiness + tempo + valence);
    }

    public static SongFeatureModel getMoodSong(int mood) {
        switch (mood) {
            case MOOD_HAPPY:

            case MOOD_CALM:

            case MOOD_ANXIOUS:

            case MOOD_ENERGETIC:

            default:
                return getNewSong();
        }
    }

    public static SongFeatureModel getNewSong() {
        try {
            JSONObject song = songsData.getJSONObject(new Random().nextInt(totalSongs));
            return new SongFeatureModel(song);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<SongFeatureModel> getSimilarSongs(SongModel songModel) {
        PriorityQueue<DistanceSongPair> queue = new PriorityQueue<>(totalSongs, new StudentComparator());

        for (int i = 0; i < songsData.length(); i++) {
            SongFeatureModel featureModel;
            try {
                featureModel = new SongFeatureModel(songsData.getJSONObject(i));
                double distance = -1 * getDistance(songModel.getFeatureModel(), featureModel);
                queue.add(new DistanceSongPair(distance, featureModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayList<SongFeatureModel> top1000 = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            DistanceSongPair distanceSongPair = queue.poll();
            SongFeatureModel featureModel = Objects.requireNonNull(distanceSongPair).getFeatureModel();
            Log.d(MainActivity.TAG, distanceSongPair.getDistance() + "");
            top1000.add(featureModel);
        }

        ArrayList<SongFeatureModel> songs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            songs.add(top1000.get(new Random().nextInt(1000)));
        }

        return songs;
    }
}
