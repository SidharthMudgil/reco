package com.sidharth.reco.chat;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.controller.ChatAdapter;
import com.sidharth.reco.chat.model.ChatModel;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    public static final int SENDER_BOT = 1;
    public static final int SENDER_USER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ArrayList<ChatModel> chats = new ArrayList<>(
                Arrays.asList(new ChatModel(1, "hello dipankar", SENDER_BOT),
                        new ChatModel(2, "ha bol", SENDER_USER),
                        new ChatModel(3, "nora fatehi set krwani h kya", SENDER_BOT),
                        new ChatModel(4, "ha krwade", SENDER_USER),
                        new ChatModel(5, "jada tadap lgi h", SENDER_BOT),
                        new ChatModel(6, "hmm kb tk haath hi chalaunga", SENDER_USER),
                        new ChatModel(7, "shi bola", SENDER_BOT),
                        new ChatModel(8, "muddu ki gand dilade", SENDER_USER),
                        new ChatModel(9, "ya tu hi dede", SENDER_USER),
                        new ChatModel(10, "bhak", SENDER_BOT),
                        new ChatModel(11, "batata hu nidhi ko bhejta hu", SENDER_BOT),
                        new ChatModel(12, "dabio phie uske tt", SENDER_BOT))
        );

        ChatAdapter chatAdapter = new ChatAdapter(this, chats);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(view -> {
            EditText inET = findViewById(R.id.in_message);
            String message = String.valueOf(inET.getText());
            ChatModel chatModel = new ChatModel(chats.size(), message, SENDER_USER);
            chats.add(chatModel);
            chatAdapter.notifyItemInserted(chats.size());
        });
    }
}