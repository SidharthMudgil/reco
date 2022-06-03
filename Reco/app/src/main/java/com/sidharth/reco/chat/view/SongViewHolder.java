package com.sidharth.reco.chat.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidharth.reco.R;
import com.sidharth.reco.chat.model.SongModel;
import com.squareup.picasso.Picasso;

public class SongViewHolder extends RecyclerView.ViewHolder {
    private final ImageView songBanner;
    private final TextView songName;
    private final TextView songArtists;

    public SongViewHolder(@NonNull View itemView) {
        super(itemView);

        songBanner = itemView.findViewById(R.id.song_banner);
        songName = itemView.findViewById(R.id.song_name);
        songArtists = itemView.findViewById(R.id.song_artist);
    }

    public void bind(SongModel songModel) {
        songName.setText(songModel.getSongName());
        songArtists.setText(songModel.getSongArtist());
        Picasso.get().load(songModel.getImgURL()).into(songBanner);
    }
}
