package com.sidharth.reco.chat.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sidharth.reco.R;
import com.sidharth.reco.chat.ChatActivity;
import com.sidharth.reco.chat.callback.OnActionPerformedListener;
import com.sidharth.reco.chat.callback.OnChatOptionClickListener;
import com.sidharth.reco.chat.callback.OnSongLongClickedListener;
import com.sidharth.reco.chat.model.ChatModel;
import com.sidharth.reco.chat.view.BotChatHolder;
import com.sidharth.reco.chat.view.SongViewHolder;
import com.sidharth.reco.chat.view.UserChatHolder;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter implements OnActionPerformedListener {
    private static final int VIEW_TYPE_MESSAGE_BOT = 1;
    private static final int VIEW_TYPE_MESSAGE_USER = 2;
    private static final int VIEW_TYPE_SONG = 3;

    private final Context context;
    private final ArrayList<ChatModel> chats;
    private final OnSongLongClickedListener songLongClickedListener;

    private RecyclerView recyclerView;


    public ChatAdapter(Context context, ArrayList<ChatModel> chats, OnSongLongClickedListener songLongClickedListener) {
        this.context = context;
        this.chats = chats;
        this.songLongClickedListener = songLongClickedListener;
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chat = chats.get(position);
        if (chat.getSender() == ChatActivity.SENDER_BOT) {
            return VIEW_TYPE_MESSAGE_BOT;
        } else if (chat.getSender() == ChatActivity.SENDER_USER) {
            return VIEW_TYPE_MESSAGE_USER;
        } else {
            return VIEW_TYPE_SONG;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_BOT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_bot, parent, false);
            return new BotChatHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_USER) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_user, parent, false);
            return new UserChatHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_song, parent, false);
            return new SongViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel message = chats.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_BOT:
                ((BotChatHolder) holder).bind(message);
                recyclerView = ((BotChatHolder) holder).getRv();
                ChatOptionAdapter adapter = new ChatOptionAdapter(context, chats.get(position).getOptionModel(), (OnChatOptionClickListener) context, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
                break;
            case VIEW_TYPE_MESSAGE_USER:
                ((UserChatHolder) holder).bind(message);
                break;
            case VIEW_TYPE_SONG:
                ((SongViewHolder) holder).bind(chats.get(position).getSongModel());
                holder.itemView.setOnLongClickListener(view -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(chats.get(position).getSongModel().getSongURL()));
                    context.startActivity(browserIntent);
                    songLongClickedListener.askUserFeedback();
                    return true;
                });
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public void removeOptions() {
        recyclerView.setAdapter(null);
    }
}
