package com.sidharth.reco.chat.callback;

import com.sidharth.reco.chat.model.ChatOptionModel;

public interface OnChatOptionClickListener {
    void onOptionClicked(ChatOptionModel optionModel, int position);
}
