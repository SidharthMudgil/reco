package com.sidharth.reco.chat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.controller.ChatAdapter;
import com.sidharth.reco.chat.model.ChatModel;
import com.sidharth.reco.chat.model.ChatOptionModel;
import com.sidharth.reco.chat.model.SongModel;
import com.sidharth.reco.recommender.RecoBrain;
import com.sidharth.reco.recommender.SongRecommender;
import com.sidharth.reco.recommender.model.SongFeatureModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


public class ChatActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    public static final int SENDER_BOT = 1;
    public static final int SENDER_USER = 2;
    public static final int SONG_VIEW = 3;

    public static final int TYPE_MOOD = 101;
    public static final int TYPE_TRY_NEW = 102;
    public static final int TYPE_FEEDBACK = 103;
    public static final int TYPE_SHOW_SIMILAR = 104;
    public static final int TYPE_OPTION_MENU = 105;

    private static final ArrayList<String> MOODS = new ArrayList<>(Arrays.asList("happy", "calm", "anxious", "energetic", "indian"));
    private static final ArrayList<String> FEEDBACK = new ArrayList<>(Arrays.asList("Yes", "No"));

    private static ArrayList<ChatModel> chats;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;

    private boolean songClicked = false;
    private SongModel songModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (savedInstanceState == null) {
            SongRecommender.initializeSongData(this);
        }

        // intro chats
        ChatOptionModel moodOptionModel = new ChatOptionModel(TYPE_MOOD, MOODS);
        chats = new ArrayList<>(
                Arrays.asList(
                        new ChatModel(SENDER_BOT, getString(R.string.msg_intro)),
                        new ChatModel(SENDER_BOT, getString(R.string.msg_mood), moodOptionModel)
                ));

        // chat adapter
        chatAdapter = new ChatAdapter(this, chats, model -> {
            songClicked = true;
            songModel = model;
        }, (optionModel, position) -> {
            removeOptions();
            String message = optionModel.getOptions().get(position);
            ChatModel chatModel = new ChatModel(SENDER_USER, message);
            addConversationToChats(chatModel);
            handler.postDelayed(() -> recommendSong(optionModel.getType(), position), 1000);
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // send button
        FloatingActionButton sendBtn = findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(view -> {
            EditText inET = findViewById(R.id.in_message);
            String message = String.valueOf(inET.getText());
            inET.setText("");
            if (!TextUtils.isEmpty(message)) {
                stopHandler();
                removeOptions();
                ChatModel chatModel = new ChatModel(SENDER_USER, message);
                addConversationToChats(chatModel);
                ChatModel answer = RecoBrain.analyzeChat(message);
                handler.postDelayed(() -> replyToUser(answer), 1000);
            }
            closeKeyboard();
        });

        handler = new Handler();
        runnable = () -> {
            removeOptions();
            ChatModel chatModel = new ChatModel(SENDER_BOT, getString(R.string.msg_no_response));
            addConversationToChats(chatModel);
            stopHandler();
        };
        startHandler();
    }

    private void removeOptions() {
        if (chats.get(chats.size() - 1).getSender() == SENDER_BOT) {
            ChatModel chatModel = chats.get(chats.size() - 1);
            ChatModel newChatModel = new ChatModel(chatModel.getSender(), chatModel.getMessage());
            chats.remove(chats.size() - 1);
            chats.add(newChatModel);
            chatAdapter.notifyItemChanged(chats.size() - 1);
        }
    }

    private void replyToUser(ChatModel chatModel) {
        addConversationToChats(chatModel);
    }

    private void addConversationToChats(ChatModel chatModel) {
        chats.add(chatModel);
        if (chatModel.getOptionModel() != null) {
            startHandler();
        }
        playMessagingSound(chatModel.getSender());
        recyclerView.smoothScrollToPosition(chats.size() - 1);
        chatAdapter.notifyItemInserted(chats.size());
    }

    private void playMessagingSound(int sender) {
        MediaPlayer player;
        if (sender == SENDER_USER) {
            player = MediaPlayer.create(this, R.raw.sent);
        } else {
            player = MediaPlayer.create(this, R.raw.recieved);
        }
        if (player.isPlaying()) {
            player.stop();
        }
        player.start();
        player.setOnCompletionListener(mediaPlayer -> player.release());
    }

    private void recommendSong(int type, int position) {
        switch (type) {
            case TYPE_MOOD: {
                SongFeatureModel song = SongRecommender.getMoodBasedSong(position);
                getShowSongView(song);
                break;
            }
            case TYPE_SHOW_SIMILAR: {
                if (position == 0) {
                    ArrayList<SongFeatureModel> songs = SongRecommender.getSimilarSongs(songModel);
                    for (SongFeatureModel song : songs) {
                        handler.postDelayed((Runnable) () -> getShowSongView(song), 500);
                    }
                } else {
                    tryNewSong();
                }
                break;
            }
            case TYPE_FEEDBACK: {
                if (position == 0) {
                    wantSimilarSong();
                } else {
                    tryNewSong();
                }
                break;
            }
            case TYPE_TRY_NEW: {
                if (position == 0) {
                    SongFeatureModel song = SongRecommender.getNewSong();
                    assert song != null;
                    getShowSongView(song);
                }
                break;
            }
            case TYPE_OPTION_MENU: {
                switch (position) {
                    case 0: {
                        ChatModel chatModel = new ChatModel(SENDER_BOT, getString(R.string.msg_recoIntro));
                        addConversationToChats(chatModel);
                        break;
                    }
                    case 1: {
                        String day = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            day = LocalDate.now().getDayOfWeek().name();
                        }
                        ChatModel chatModel = new ChatModel(SENDER_BOT, day);
                        addConversationToChats(chatModel);
                        break;
                    }
                    case 2: {
                        RecoBrain.getJoke(this, this::addConversationToChats);
                        break;
                    }
                    case 3: {
                        ChatOptionModel optionModel = new ChatOptionModel(TYPE_MOOD, MOODS);
                        ChatModel chatModel = new ChatModel(SENDER_BOT, getString(R.string.msg_mood), optionModel);
                        addConversationToChats(chatModel);
                        break;
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    private void likedTheSong() {
        ChatOptionModel optionModel = new ChatOptionModel(TYPE_FEEDBACK, FEEDBACK);
        ChatModel chatModel = new ChatModel(SENDER_BOT, getString(R.string.msg_liked_the_song), optionModel);
        addConversationToChats(chatModel);
        startHandler();
    }

    private void wantSimilarSong() {
        ChatOptionModel optionModel = new ChatOptionModel(TYPE_SHOW_SIMILAR, FEEDBACK);
        ChatModel chatModel = new ChatModel(SENDER_BOT, getString(R.string.msg_want_similar), optionModel);
        addConversationToChats(chatModel);
        startHandler();
    }

    private void tryNewSong() {
        ChatOptionModel optionModel = new ChatOptionModel(TYPE_TRY_NEW, FEEDBACK);
        ChatModel chatModel = new ChatModel(SENDER_BOT, getString(R.string.msg_try_new1), optionModel);
        addConversationToChats(chatModel);
        startHandler();
    }

    private void getShowSongView(SongFeatureModel featureModel) {
        SongRecommender.getSongFromFeature(this, featureModel, this::addConversationToChats);
    }

    public void stopHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    public void startHandler() {
        handler.postDelayed(runnable, 10 * 1000);
    }

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeOptions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (songClicked) {
            handler.postDelayed(this::likedTheSong, 1000);
            songClicked = false;
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        stopHandler();
    }
}
