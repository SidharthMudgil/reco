package com.sidharth.reco.chat.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidharth.reco.R;

public class ChatOptionHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    public ChatOptionHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.tv_option);
    }

    public void bind(String option) {
        textView.setText(option);
    }
}
