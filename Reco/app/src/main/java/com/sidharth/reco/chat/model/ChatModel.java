package com.sidharth.reco.chat.model;

public class ChatModel {
    private int sender;
    private String message;
    private ChatOptionModel optionModel;
    private SongModel songModel;

    public ChatModel(int sender, SongModel songModel) {
        this.sender = sender;
        this.songModel = songModel;
    }

    public ChatModel(int sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public ChatModel(int sender, String message, ChatOptionModel optionModel) {
        this.sender = sender;
        this.message = message;
        this.optionModel = optionModel;
    }

    public String getMessage() {
        return message;
    }

    public int getSender() {
        return sender;
    }

    public ChatOptionModel getOptionModel() {
        return optionModel;
    }

    public SongModel getSongModel() {
        return songModel;
    }
}
