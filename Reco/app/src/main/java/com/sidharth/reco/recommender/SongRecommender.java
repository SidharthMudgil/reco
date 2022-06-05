package com.sidharth.reco.recommender;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arasthel.asyncjob.AsyncJob;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.ChatActivity;
import com.sidharth.reco.chat.model.ChatModel;
import com.sidharth.reco.chat.model.SongModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    private static final int GRAPH_LOW = 0;
    private static final int GRAPH_HIGH = 1;

    private static final String ATTR_ENERGY = "energy";
    private static final String ATTR_VALANCE = "valance";
    private static final String ATTR_TEMPO = "tempo";

    private static final String SPOTIFY_BASE_URL = "https://spotify23.p.rapidapi.com/tracks/?ids=";

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

    public static void getSongFromFeature(Context context, SongFeatureModel featureModel, APIResponseCallback responseCallback) {
        final String URL = SPOTIFY_BASE_URL + featureModel.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("tracks");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        // for spotify url
                        JSONObject urls = jsonObject.getJSONObject("external_urls");
                        String spotify_url = urls.getString("spotify");

                        // for artists
                        JSONArray artists = jsonObject.getJSONArray("artists");
                        StringBuilder artist = new StringBuilder();
                        for (int i = 0; i < artists.length(); i++) {
                            JSONObject object = artists.getJSONObject(i);
                            String name = object.getString("name");
                            artist.append(name);
                            if (i != artists.length() - 1) {
                                artist.append(", ");
                            }
                        }

                        // for image url
                        JSONObject album = jsonObject.getJSONObject("album");
                        JSONArray images = album.getJSONArray("images");
                        JSONObject image = images.getJSONObject(2);
                        String imgURL = image.getString("url");

                        String songName = jsonObject.getString("name");

                        SongModel songModel = new SongModel(imgURL, songName, String.valueOf(artist), spotify_url, featureModel);
                        ChatModel chatModel = new ChatModel(ChatActivity.SONG_VIEW, songModel);
                        responseCallback.onSongResponse(chatModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-RapidAPI-Host", context.getString(R.string.SPOTIFY_API_HOST));
                params.put("X-RapidAPI-Key", context.getString(R.string.SPOTIFY_API_KEY));
                return params;
            }
        };
        requestQueue.add(objectRequest);
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

    private static SongFeatureModel getSongForMood(String attribute, int graph) {
        PriorityQueue<SongAttrPair> queue = new PriorityQueue<>(totalSongs, new SongAttrComparator());

        for (int i = 0; i < songsData.length(); i++) {
            SongFeatureModel featureModel;
            try {
                featureModel = new SongFeatureModel(songsData.getJSONObject(i));
                switch (attribute) {
                    case ATTR_ENERGY: {
                        queue.add(new SongAttrPair(featureModel.getEnergy(), featureModel));
                        break;
                    }
                    case ATTR_VALANCE: {
                        if (graph == GRAPH_HIGH)
                            queue.add(new SongAttrPair(featureModel.getAcousticness(), featureModel));
                        else
                            queue.add(new SongAttrPair(-1 * featureModel.getAcousticness(), featureModel));
                        break;
                    }
                    case ATTR_TEMPO: {
                        queue.add(new SongAttrPair(-1 * featureModel.getTempo(), featureModel));
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayList<SongFeatureModel> top2000 = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            SongAttrPair songAttrPair = queue.poll();
            SongFeatureModel featureModel = Objects.requireNonNull(songAttrPair).getFeatureModel();
            top2000.add(featureModel);
        }

        return top2000.get(new Random().nextInt(2000));
    }

    public static SongFeatureModel getMoodSong(int mood) {
        switch (mood) {
            case MOOD_HAPPY:
                return getSongForMood(ATTR_VALANCE, GRAPH_HIGH);
            case MOOD_CALM:
                return getSongForMood(ATTR_TEMPO, GRAPH_LOW);
            case MOOD_ANXIOUS:
                return getSongForMood(ATTR_VALANCE, GRAPH_LOW);
            case MOOD_ENERGETIC:
                return getSongForMood(ATTR_ENERGY, GRAPH_HIGH);
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
        PriorityQueue<SongAttrPair> queue = new PriorityQueue<>(totalSongs, new SongAttrComparator());

        for (int i = 0; i < songsData.length(); i++) {
            SongFeatureModel featureModel;
            try {
                featureModel = new SongFeatureModel(songsData.getJSONObject(i));
                double distance = -1 * getDistance(songModel.getFeatureModel(), featureModel);
                queue.add(new SongAttrPair(distance, featureModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayList<SongFeatureModel> top1000 = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            SongAttrPair distanceSongPair = queue.poll();
            SongFeatureModel featureModel = Objects.requireNonNull(distanceSongPair).getFeatureModel();
            top1000.add(featureModel);
        }

        ArrayList<SongFeatureModel> songs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            songs.add(top1000.get(new Random().nextInt(1000)));
        }

        return songs;
    }
}
