package com.sidharth.reco.chat.callback;

import com.sidharth.reco.chat.model.ChatOptionModel;

public interface OnOptionClickListener {
    void onClick(ChatOptionModel optionModel, int position);
}
