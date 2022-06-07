package com.sidharth.reco.recommender;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.ChatActivity;
import com.sidharth.reco.chat.model.ChatModel;
import com.sidharth.reco.chat.model.ChatOptionModel;
import com.sidharth.reco.recommender.callback.APIResponseCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecoBrain {
    private static final List<String> GREETING = Arrays.asList(
            "hi", "hello", "hlo", "helo", "hii", "helloo",
            "hola", "nmste", "namaste",
            "nmshkaram", "namaskar");

    private static final ArrayList<String> OPTIONS = new ArrayList<>(Arrays.asList(
            "introduce yourself",
            "tell me the day today",
            "tell me a joke",
            "play a mood song"
    ));

    public static ArrayList<ChatModel> analyzeChat(String message) {
        String[] words = message.split(" ");
        for (String word : words) {
            if (GREETING.contains(word.toLowerCase())) {
                ChatOptionModel optionModel = new ChatOptionModel(ChatActivity.TYPE_OPTION_MENU, OPTIONS);
                ChatModel chatModel = new ChatModel(ChatActivity.SENDER_BOT, "What can I do for you?", optionModel);
                return new ArrayList<>(Collections.singletonList(chatModel));
            }
        }
        ChatModel chatModel1 = new ChatModel(ChatActivity.SENDER_BOT, "Sorry I can't understand you. I am learning");
        ChatModel chatModel2 = new ChatModel(ChatActivity.SENDER_BOT, "Tap on the option below to get started");
        ChatOptionModel optionModel = new ChatOptionModel(ChatActivity.TYPE_OPTION_MENU, OPTIONS);
        ChatModel chatModel3 = new ChatModel(ChatActivity.SENDER_BOT, "What can I do for you?", optionModel);

        return new ArrayList<>(Arrays.asList(chatModel1, chatModel2, chatModel3));
    }

    public static void getJoke(Context context, APIResponseCallback responseCallback) {
        final String URL = "https://v2.jokeapi.dev/joke/Any?blacklistFlags=nsfw,religious,political,racist,sexist,explicit";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        String type = response.getString("type");

                        if (type.equalsIgnoreCase("twopart")) {
                            String setup = response.getString("setup");
                            String delivery = response.getString("delivery");
                            ChatModel jokeSetup = new ChatModel(ChatActivity.SENDER_BOT, setup);
                            responseCallback.onResponse(jokeSetup);
                            new Handler().postDelayed(() -> {
                                ChatModel jokeDelivery = new ChatModel(ChatActivity.SENDER_BOT, delivery);
                                responseCallback.onResponse(jokeDelivery);
                            }, 500);
                        } else {
                            String joke = response.getString("joke");
                            ChatModel chatModel = new ChatModel(ChatActivity.SENDER_BOT, joke);
                            responseCallback.onResponse(chatModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-RapidAPI-Host", context.getString(R.string.SPOTIFY_API_HOST));
                params.put("X-RapidAPI-Key", context.getString(R.string.SPOTIFY_API_KEY));
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }
}
