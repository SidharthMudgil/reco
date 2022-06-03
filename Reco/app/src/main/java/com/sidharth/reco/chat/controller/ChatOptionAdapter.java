package com.sidharth.reco.chat.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidharth.reco.R;
import com.sidharth.reco.chat.callback.OnChatOptionClickListener;
import com.sidharth.reco.chat.callback.OnActionPerformedListener;
import com.sidharth.reco.chat.model.ChatOptionModel;
import com.sidharth.reco.chat.view.ChatOptionHolder;

public class ChatOptionAdapter extends RecyclerView.Adapter<ChatOptionHolder> {
    private final Context context;
    private final ChatOptionModel optionModel;
    private final OnChatOptionClickListener onChatOptionClickListener;
    private final OnActionPerformedListener onActionPerformedListener;

    public ChatOptionAdapter(Context context, ChatOptionModel optionModel, OnChatOptionClickListener onChatOptionClickListener, OnActionPerformedListener onActionPerformedListener) {
        this.context = context;
        this.optionModel = optionModel;
        this.onChatOptionClickListener = onChatOptionClickListener;
        this.onActionPerformedListener = onActionPerformedListener;
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
        holder.itemView.setOnClickListener(view -> {
            onChatOptionClickListener.onOptionClicked(optionModel, position);
            onActionPerformedListener.removeOptions();
        });
    }

    @Override
    public int getItemCount() {
        if (optionModel != null)
            return optionModel.getOptions().size();
        else
            return 0;
    }
}
