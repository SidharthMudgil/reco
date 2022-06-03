package com.sidharth.reco.chat.model;

import java.util.ArrayList;

public class ChatOptionModel {
    private final int type;
    private final ArrayList<String> options;

    public ChatOptionModel(int type, ArrayList<String> options) {
        this.type = type;
        this.options = options;
    }

    public int getType() {
        return type;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
