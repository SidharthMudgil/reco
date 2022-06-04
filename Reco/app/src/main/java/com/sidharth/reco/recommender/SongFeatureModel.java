package com.sidharth.reco.recommender;

import org.json.JSONObject;

public class SongFeatureModel {
    private double acousticness;
    private String artists;
    private double danceability;
    private double duration_ms;
    private double energy;
    private int explicit;
    private String id;
    private double instrumentalness;
    private int key;
    private double liveness;
    private double loudness;
    private int mode;
    private String name;
    private int popularity;
    private String releaseDate;
    private double speechiness;
    private double tempo;
    private double valence;
    private int year;

    public SongFeatureModel(JSONObject jsonObject) {
        setAttributes(jsonObject);
    }

    private void setAttributes(JSONObject jsonObject) {

    }

    public double getAcousticness() {
        return acousticness;
    }

    public String getArtists() {
        return artists;
    }

    public double getDanceability() {
        return danceability;
    }

    public double getDuration_ms() {
        return duration_ms;
    }

    public double getEnergy() {
        return energy;
    }

    public int getExplicit() {
        return explicit;
    }

    public String getId() {
        return id;
    }

    public double getInstrumentalness() {
        return instrumentalness;
    }

    public int getKey() {
        return key;
    }

    public double getLiveness() {
        return liveness;
    }

    public double getLoudness() {
        return loudness;
    }

    public int getMode() {
        return mode;
    }

    public String getName() {
        return name;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
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

    public int getYear() {
        return year;
    }
}
