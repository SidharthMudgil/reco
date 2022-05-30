package com.sidharth.reco.chat.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidharth.reco.R;
import com.sidharth.reco.chat.model.ChatModel;

public class BotChatHolder extends RecyclerView.ViewHolder {
    private final TextView botTV;

    public BotChatHolder(@NonNull View itemView) {
        super(itemView);

        botTV = itemView.findViewById(R.id.tv_Bot);
    }

    public void bind(ChatModel chatModel) {
        botTV.setText(chatModel.getMessage());
    }
}
