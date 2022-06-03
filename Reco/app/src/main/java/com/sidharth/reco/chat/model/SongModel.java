package com.sidharth.reco.chat.model;

public class SongModel {
    final String imgURL;
    final String songName;
    final String songArtist;


    public SongModel(String imgURL, String songName, String songArtist) {
        this.imgURL = imgURL;
        this.songName = songName;
        this.songArtist = songArtist;
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
}
