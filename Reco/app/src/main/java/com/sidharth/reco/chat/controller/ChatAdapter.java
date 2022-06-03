package com.sidharth.reco.chat.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sidharth.reco.R;
import com.sidharth.reco.chat.ChatActivity;
import com.sidharth.reco.chat.callback.OnChatOptionClickListener;
import com.sidharth.reco.chat.model.ChatModel;
import com.sidharth.reco.chat.view.BotChatHolder;
import com.sidharth.reco.chat.view.UserChatHolder;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_BOT = 1;
    private static final int VIEW_TYPE_MESSAGE_USER = 2;

    private final Context context;
    private final ArrayList<ChatModel> chats;

    public ChatAdapter(Context context, ArrayList<ChatModel> chats) {
        this.context = context;
        this.chats = chats;
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chat = chats.get(position);
        if (chat.getSender() == ChatActivity.SENDER_BOT) {
            return VIEW_TYPE_MESSAGE_BOT;
        } else {
            return VIEW_TYPE_MESSAGE_USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_BOT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_bot, parent, false);
            return new BotChatHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_user, parent, false);
            return new UserChatHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel message = chats.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_BOT:
                ((BotChatHolder) holder).bind(message);
                RecyclerView recyclerView = ((BotChatHolder) holder).getRv();
                ChatOptionAdapter adapter = new ChatOptionAdapter(context, chats.get(position).getOptionModel(), (OnChatOptionClickListener) context);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
                break;
            case VIEW_TYPE_MESSAGE_USER:
                ((UserChatHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
