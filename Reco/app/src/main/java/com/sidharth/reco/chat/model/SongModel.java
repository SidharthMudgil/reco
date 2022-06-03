package com.sidharth.reco.chat.model;

public class SongModel {
    final String imgURL;
    final String songName;
    final String songArtist;
    final String songURL;


    public SongModel(String imgURL, String songName, String songArtist, String songURL) {
        this.imgURL = imgURL;
        this.songName = songName;
        this.songArtist = songArtist;
        this.songURL = songURL;
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
