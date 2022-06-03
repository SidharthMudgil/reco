package com.sidharth.reco.chat.model;

public class ChatModel {
    private final int sender;
    private final String message;
    private final ChatOptionModel optionModel;

    public ChatModel(String message, int sender, ChatOptionModel optionModel) {
        this.message = message;
        this.sender = sender;
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
}
