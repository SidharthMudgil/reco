package com.sidharth.reco.chat.callback;

import com.sidharth.reco.chat.model.SongModel;

public interface OnSongLongClickedListener {
    void askUserFeedback(SongModel songModel);
}
