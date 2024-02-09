package com.sidharth.reco.chat.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidharth.reco.R;
import com.sidharth.reco.chat.model.ChatModel;

public class UserChatHolder extends RecyclerView.ViewHolder {
    private final TextView userTV;

    public UserChatHolder(@NonNull View itemView) {
        super(itemView);

        userTV = itemView.findViewById(R.id.tv_user);
    }

    public void bind(ChatModel chatModel) {
        userTV.setText(chatModel.getMessage());
    }
}
