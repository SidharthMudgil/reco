package com.sidharth.reco.recommender.callback;

import com.sidharth.reco.chat.model.ChatModel;

public interface APIResponseCallback {
    void onResponse(ChatModel chatModel);
}
