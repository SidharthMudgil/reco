package com.sidharth.reco.recommender;

import com.sidharth.reco.chat.ChatActivity;
import com.sidharth.reco.chat.model.ChatModel;
import com.sidharth.reco.chat.model.ChatOptionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecoBrain {
    private static final List<String> GREETING = Arrays.asList("hi", "hello", "hlo", "helo");

    public static ChatModel analyzeChat(String message) {
        String[] words = message.split(" ");
        for (String word : words) {
            if (GREETING.contains(word)) {
                ArrayList<String> options = new ArrayList<>(Arrays.asList(
                        "introduce yourself",
                        "whats the day today",
                        "tell me a joke",
                        "mood based song"
                ));
                ChatOptionModel optionModel = new ChatOptionModel(ChatActivity.TYPE_OPTION_MENU, options);
                return new ChatModel(ChatActivity.SENDER_BOT, "Tap on one of the options", optionModel);
            }
        }
        String reply = "Sorry I can't understand you. I am learning, Say 'hi' to get options";
        return new ChatModel(ChatActivity.SENDER_BOT, reply);
    }
}
