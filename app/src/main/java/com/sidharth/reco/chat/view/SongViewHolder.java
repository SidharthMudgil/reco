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
        String name = songModel.getSongName();
        String artist = songModel.getSongArtist();

        if (name.length() > 25) {
            name = name.substring(0, 21) + " ...";
        }
        if (artist.length() > 50) {
            artist = artist.substring(0, 50) + " ...";
        }

        songName.setText(name);
        songArtists.setText(artist);
        Picasso.get().load(songModel.getImgURL()).into(songBanner);
    }
}
