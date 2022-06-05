package com.sidharth.reco.recommender.model;

import org.json.JSONException;
import org.json.JSONObject;

public class SongFeatureModel {
    private final int type;
    private final String id;
    private final String name;
    private final double acousticness;
    private final double danceability;
    private final double energy;
    private final double instrumentalness;
    private final double liveness;
    private final double loudness;
    private final double speechiness;
    private final double tempo;
    private final double valence;

    public SongFeatureModel(JSONObject jsonObject, int type) {
        this.type = type;
        String id1;
        String name1;
        double acousticness1;
        double danceability1;
        double energy1;
        double instrumentalness1;
        double liveness1;
        double loudness1;
        double speechiness1;
        double tempo1;
        double valence1;
        try {
            id1 = jsonObject.getString("id");
            name1 = jsonObject.getString("name");
            acousticness1 = jsonObject.getDouble("acousticness");
            danceability1 = jsonObject.getDouble("danceability");
            energy1 = jsonObject.getDouble("energy");
            instrumentalness1 = jsonObject.getDouble("instrumentalness");
            liveness1 = jsonObject.getDouble("liveness");
            loudness1 = jsonObject.getDouble("loudness");
            speechiness1 = jsonObject.getDouble("speechiness");
            tempo1 = jsonObject.getDouble("tempo");
            valence1 = jsonObject.getDouble("valence");
        } catch (JSONException e) {
            e.printStackTrace();
            id1 = "";
            name1 = "";
            acousticness1 = 0;
            danceability1 = 0;
            energy1 = 0;
            instrumentalness1 = 0;
            liveness1 = 0;
            loudness1 = 0;
            speechiness1 = 0;
            tempo1 = 0;
            valence1 = 0;
        }
        id = id1;
        name = name1;
        acousticness = acousticness1;
        danceability = danceability1;
        energy = energy1;
        instrumentalness = instrumentalness1;
        liveness = liveness1;
        loudness = loudness1;
        speechiness = speechiness1;
        tempo = tempo1;
        valence = valence1;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAcousticness() {
        return acousticness;
    }

    public double getDanceability() {
        return danceability;
    }

    public double getEnergy() {
        return energy;
    }

    public double getInstrumentalness() {
        return instrumentalness;
    }

    public double getLiveness() {
        return liveness;
    }

    public double getLoudness() {
        return loudness;
    }

    public double getSpeechiness() {
        return speechiness;
    }

    public double getTempo() {
        return tempo;
    }

    public double getValence() {
        return valence;
    }
}
