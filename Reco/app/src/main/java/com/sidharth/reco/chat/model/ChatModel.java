package com.sidharth.reco.chat.model;

public class ChatModel {
    private final int id;
    private final int sender;
    private final String message;

    public ChatModel(int id, String message, int sender) {
        this.id = id;
        this.message = message;
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public int getSender() {
        return sender;
    }
}
