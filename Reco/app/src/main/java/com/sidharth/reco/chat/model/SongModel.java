package com.sidharth.reco.chat.model;

public class SongModel {
    private final String imgID;
    private final String imgURL;
    private final String songName;
    private final String songArtist;
    private final String songURL;

    public SongModel(String imgID, String imgURL, String songName, String songArtist, String songURL) {
        this.imgID = imgID;
        this.imgURL = imgURL;
        this.songName = songName;
        this.songArtist = songArtist;
        this.songURL = songURL;
    }

    public String getImgID() {
        return imgID;
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
}
