package com.sidharth.reco.recommender;

import com.sidharth.reco.chat.model.ChatModel;

public interface APIResponseCallback {
    void onResponse(ChatModel chatModel);
}
