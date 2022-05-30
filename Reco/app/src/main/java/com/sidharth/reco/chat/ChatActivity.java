package com.sidharth.reco.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.controller.ChatAdapter;
import com.sidharth.reco.chat.model.ChatModel;

import java.util.ArrayList;
import java.util.Collections;

public class ChatActivity extends AppCompatActivity {

    public static final int SENDER_BOT = 1;
    public static final int SENDER_USER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String recoIntro = "Hi,\nI'm Reco, your personal bot\nLet's listen to some songs";
        ArrayList<ChatModel> chats = new ArrayList<>(
                Collections.singletonList(new ChatModel(0, recoIntro, SENDER_BOT))
        );

        ChatAdapter chatAdapter = new ChatAdapter(this, chats);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(view -> {
            EditText inET = findViewById(R.id.in_message);
            String message = String.valueOf(inET.getText());
            inET.setText("");
            if (!TextUtils.isEmpty(message)) {
                ChatModel chatModel = new ChatModel(chats.size(), message, SENDER_USER);
                chats.add(chatModel);
                chatAdapter.notifyItemInserted(chats.size());
            }
            closeKeyboard();
        });
    }

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}