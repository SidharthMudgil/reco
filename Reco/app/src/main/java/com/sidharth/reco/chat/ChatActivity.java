package com.sidharth.reco.chat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sidharth.reco.MainActivity;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.callback.OnChatOptionClickListener;
import com.sidharth.reco.chat.callback.OnSongLongClickedListener;
import com.sidharth.reco.chat.controller.ChatAdapter;
import com.sidharth.reco.chat.model.ChatModel;
import com.sidharth.reco.chat.model.ChatOptionModel;
import com.sidharth.reco.chat.model.SongModel;
import com.sidharth.reco.recommender.SongRecommender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity implements OnChatOptionClickListener, OnSongLongClickedListener {

    private Handler handler;
    private Runnable runnable;

    public static final int SENDER_BOT = 1;
    public static final int SENDER_USER = 2;
    public static final int SONG_VIEW = 3;
    private static final int TYPE_MOOD = 101;
    private static final int TYPE_TRY_NEW = 102;
    private static final int TYPE_FEEDBACK = 103;
    private static final int TYPE_SHOW_SIMILAR = 104;

    private static final ArrayList<String> MOODS = new ArrayList<>(Arrays.asList("happy", "sad", "angry", "joy"));
    private static final ArrayList<String> FEEDBACK = new ArrayList<>(Arrays.asList("Yes", "No"));

    private ArrayList<ChatModel> chats;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;

    private static final String BASE_URL = "https://spotify23.p.rapidapi.com/tracks/?ids=";
    private boolean songClicked = false;
    private SongModel songModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SongRecommender.initializeSongData(this);

        // intro chats
        String recoIntro = "Hi,\nI'm Reco, your personal bot\nLet's listen to some songs";
        String moodMessage = "How are your feeling today?";
        ChatOptionModel moodOptionModel = new ChatOptionModel(TYPE_MOOD, MOODS);
        chats = new ArrayList<>(
                Arrays.asList(
                        new ChatModel(SENDER_BOT, recoIntro),
                        new ChatModel(SENDER_BOT, moodMessage, moodOptionModel)
                ));

        // chat adapter
        chatAdapter = new ChatAdapter(this, chats, this);
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
                ChatModel chatModel = new ChatModel(SENDER_USER, message, null);
                addConversationToChats(chatModel);
                analyzeChat(message);
            }
            closeKeyboard();
        });

        handler = new Handler();
        runnable = () -> {
            removeOptions();
            ChatModel chatModel = new ChatModel(SENDER_BOT, getString(R.string.msg_no_response), null);
            addConversationToChats(chatModel);
            stopHandler();
        };
        startHandler();
    }

    private void removeOptions() {
        ChatModel chatModel = chats.get(chats.size() - 1);
        ChatModel newChatModel = new ChatModel(chatModel.getSender(), chatModel.getMessage());
        chats.remove(chats.size() - 1);
        chats.add(newChatModel);
        chatAdapter.notifyItemChanged(chats.size() - 1);
    }

    private void analyzeChat(String message) {
        String[] words = message.split(" ");
        Log.d(MainActivity.TAG, Arrays.toString(words) + "");
        handler.postDelayed(() -> replyToUser(2), 1000);
    }

    private void replyToUser(int replyWhat) {
        ChatModel chatModel = new ChatModel(SENDER_BOT, "Sorry can't understand");
        addConversationToChats(chatModel);
    }

    private void addConversationToChats(ChatModel chatModel) {
        if (chatModel != null) {
            chats.add(chatModel);
            playMessagingSound(chatModel.getSender());
        } else {
            ChatModel chat = new ChatModel(SENDER_BOT, getString(R.string.msg_try_new));
            addConversationToChats(chat);
            playMessagingSound(SENDER_BOT);
        }
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
                String songID = SongRecommender.getMoodSong(position);
                parseJSON("0kmOFBPszGiU5UiERMN9ph");
                break;
            }
            case TYPE_SHOW_SIMILAR: {
                if (position == 0) {
                    ArrayList<String> ids = SongRecommender.getSimilarSongs(songModel);
                    for (String songId : ids) {
                        handler.postDelayed((Runnable) () -> parseJSON(songId), 500);
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
                    String songID = SongRecommender.getNewSong();
                    parseJSON(songID);
                } else {
                    ChatModel chatModel = new ChatModel(SENDER_BOT, getString(R.string.msg_thanks));
                    addConversationToChats(chatModel);
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

    private void parseJSON(String id) {
        final String URL = BASE_URL + id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("tracks");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        // for spotify url
                        JSONObject urls = jsonObject.getJSONObject("external_urls");
                        String spotify_url = urls.getString("spotify");

                        // for artists
                        JSONArray artists = jsonObject.getJSONArray("artists");
                        StringBuilder artist = new StringBuilder();
                        for (int i = 0; i < artists.length(); i++) {
                            JSONObject object = artists.getJSONObject(i);
                            String name = object.getString("name");
                            artist.append(name);
                            if (i != artists.length() - 1) {
                                artist.append(", ");
                            }
                        }

                        // for image url
                        JSONObject album = jsonObject.getJSONObject("album");
                        JSONArray images = album.getJSONArray("images");
                        JSONObject image = images.getJSONObject(2);
                        String imgURL = image.getString("url");

                        String songName = jsonObject.getString("name");

                        SongModel songModel = new SongModel(id, imgURL, songName, String.valueOf(artist), spotify_url);

                        ChatModel chatModel = new ChatModel(SONG_VIEW, songModel);
                        addConversationToChats(chatModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.d(MainActivity.TAG, error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("X-RapidAPI-Host", getString(R.string.api_host));
                params.put("X-RapidAPI-Key", getString(R.string.api_key));
                return params;
            }
        };
        requestQueue.add(objectRequest);
    }

    public void stopHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    public void startHandler() {
        handler.postDelayed(runnable, 25 * 1000);
    }

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onOptionClicked(ChatOptionModel optionModel, int position) {
        String message = optionModel.getOptions().get(position);
        ChatModel chatModel = new ChatModel(SENDER_USER, message);
        addConversationToChats(chatModel);
        handler.postDelayed(() -> recommendSong(optionModel.getType(), position), 1000);
    }

    @Override
    public void askUserFeedback(SongModel songModel) {
        songClicked = true;
        this.songModel = songModel;
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
