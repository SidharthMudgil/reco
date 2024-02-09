package com.sidharth.reco.chat.model;

import com.sidharth.reco.recommender.model.SongFeatureModel;

public class SongModel {
    private final String imgURL;
    private final String songName;
    private final String songArtist;
    private final String songURL;
    private final SongFeatureModel featureModel;

    public SongModel(String imgURL, String songName, String songArtist, String songURL, SongFeatureModel featureModel) {
        this.imgURL = imgURL;
        this.songName = songName;
        this.songArtist = songArtist;
        this.songURL = songURL;
        this.featureModel = featureModel;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongURL() {
        return songURL;
    }

    public SongFeatureModel getFeatureModel() {
        return featureModel;
    }
}
