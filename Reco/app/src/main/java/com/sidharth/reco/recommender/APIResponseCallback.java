package com.sidharth.reco.recommender;

import com.sidharth.reco.chat.model.ChatModel;

public interface APIResponseCallback {
    void onSongResponse(ChatModel chatModel);
    void onJokeResponse(String joke);
}
