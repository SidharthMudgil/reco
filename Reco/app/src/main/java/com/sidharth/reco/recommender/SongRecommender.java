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
import com.sidharth.reco.recommender.callback.APIResponseCallback;
import com.sidharth.reco.recommender.model.SongFeatureModel;
import com.sidharth.reco.recommender.utils.SongAttrComparator;
import com.sidharth.reco.recommender.utils.SongAttrPair;

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
    private static final int TOTAL_NON_INDIAN_SONGS = 50014;
    private static final int TOTAL_INDIAN_SONGS = 1891;

    private static JSONArray allSongs;
    private static JSONArray hindiSongs;
    private static final int MOOD_HAPPY = 0;
    private static final int MOOD_CALM = 1;
    private static final int MOOD_ANXIOUS = 2;
    private static final int MOOD_ENERGETIC = 3;
    private static final int MOOD_INDIAN = 4;

    private static final int TYPE_NON_INDIAN = 1;
    private static final int TYPE_INDIAN = 0;

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
                        allSongs = loadJSONFromAssets(context, "data.json");
                        hindiSongs = loadJSONFromAssets(context, "hindi.json");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .doWhenFinished(o -> {
                    if (allSongs == null || hindiSongs == null) {
                        throw new RuntimeException("Array List Empty");
                    } else {
                        dialog.dismiss();
                    }
                }).create().start();
    }

    private static JSONArray loadJSONFromAssets(Context context, String file) {
        try {
            InputStream is = context.getAssets().open(file);
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
                        responseCallback.onResponse(chatModel);
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

    private static SongFeatureModel getNonIndianSongs(String attribute, int graph) {
        PriorityQueue<SongAttrPair> queue = new PriorityQueue<>(TOTAL_NON_INDIAN_SONGS, new SongAttrComparator());

        for (int i = 0; i < allSongs.length(); i++) {
            SongFeatureModel featureModel;
            try {
                featureModel = new SongFeatureModel(allSongs.getJSONObject(i), TYPE_NON_INDIAN);
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

    public static SongFeatureModel getMoodBasedSong(int mood) {
        switch (mood) {
            case MOOD_HAPPY:
                return getNonIndianSongs(ATTR_VALANCE, GRAPH_HIGH);
            case MOOD_CALM:
                return getNonIndianSongs(ATTR_TEMPO, GRAPH_LOW);
            case MOOD_ANXIOUS:
                return getNonIndianSongs(ATTR_VALANCE, GRAPH_LOW);
            case MOOD_ENERGETIC:
                return getNonIndianSongs(ATTR_ENERGY, GRAPH_HIGH);
            case MOOD_INDIAN:
                return getIndianSong();
            default:
                return getNewSong();
        }
    }

    public static SongFeatureModel getIndianSong() {
        try {
            JSONObject song = hindiSongs.getJSONObject(new Random().nextInt(TOTAL_INDIAN_SONGS));
            return new SongFeatureModel(song, TYPE_INDIAN);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SongFeatureModel getNewSong() {
        try {
            int choice = new Random().nextInt(2);
            JSONObject song;
            if (choice == TYPE_NON_INDIAN) {
                song = allSongs.getJSONObject(new Random().nextInt(TOTAL_NON_INDIAN_SONGS));
                return new SongFeatureModel(song, TYPE_NON_INDIAN);
            } else {
                song = hindiSongs.getJSONObject(new Random().nextInt(TOTAL_INDIAN_SONGS));
                return new SongFeatureModel(song, TYPE_INDIAN);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<SongFeatureModel> getSimilarSongs(SongModel songModel) {
        int type = songModel.getFeatureModel().getType();
        int total;
        PriorityQueue<SongAttrPair> queue;
        if (type == TYPE_INDIAN) {
            total = TOTAL_INDIAN_SONGS;
        } else {
            total = TOTAL_NON_INDIAN_SONGS;
        }
        queue = new PriorityQueue<>(total, new SongAttrComparator());

        for (int i = 0; i < total; i++) {
            SongFeatureModel featureModel;
            try {
                if (type == TYPE_NON_INDIAN) {
                    featureModel = new SongFeatureModel(allSongs.getJSONObject(i), TYPE_NON_INDIAN);
                } else {
                    featureModel = new SongFeatureModel(hindiSongs.getJSONObject(i), TYPE_INDIAN);
                }
                double distance = -1 * getDistance(songModel.getFeatureModel(), featureModel);
                queue.add(new SongAttrPair(distance, featureModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        int bound = (int) (0.05 * total);
        ArrayList<SongFeatureModel> similarSongs = new ArrayList<>();
        for (int i = 0; i < bound; i++) {
            SongAttrPair distanceSongPair = queue.poll();
            SongFeatureModel featureModel = Objects.requireNonNull(distanceSongPair).getFeatureModel();
            similarSongs.add(featureModel);
        }

        ArrayList<SongFeatureModel> songs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            songs.add(similarSongs.get(new Random().nextInt(bound)));
        }

        return songs;
    }
}
