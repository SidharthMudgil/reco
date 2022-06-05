package com.sidharth.reco.chat.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidharth.reco.R;
import com.sidharth.reco.chat.callback.OnOptionClickListener;
import com.sidharth.reco.chat.model.ChatOptionModel;
import com.sidharth.reco.chat.view.ChatOptionHolder;

public class ChatOptionAdapter extends RecyclerView.Adapter<ChatOptionHolder> {
    private final Context context;
    private final ChatOptionModel optionModel;
    private final OnOptionClickListener onChatOptionClickListener;

    public ChatOptionAdapter(Context context, ChatOptionModel optionModel, OnOptionClickListener onChatOptionClickListener) {
        this.context = context;
        this.optionModel = optionModel;
        this.onChatOptionClickListener = onChatOptionClickListener;
    }

    @NonNull
    @Override
    public ChatOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.option_item, parent, false);
        return new ChatOptionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatOptionHolder holder, int position) {
        holder.bind(optionModel.getOptions().get(position));
        holder.itemView.setOnClickListener(view -> onChatOptionClickListener.onClick(optionModel, position));
    }

    @Override
    public int getItemCount() {
        if (optionModel != null)
            return optionModel.getOptions().size();
        else
            return 0;
    }
}
