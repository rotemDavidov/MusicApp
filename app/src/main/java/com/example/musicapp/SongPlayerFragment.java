package com.example.musicapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class SongPlayerFragment extends Fragment {

    Song songPlaying;
    ArrayList<Song> shownSongs;
    int position;
    MediaPlayer mp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song,container,false);
        Bundle b = this.getArguments();
        if(b != null){
            //position = b.getInt("SongPosition");
            //shownSongs = b.getParcelable("SongsList");
            songPlaying = (Song)b.getSerializable("song");
        }
        //songPlaying = shownSongs.get(position);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView songName = (TextView) view.findViewById(R.id.player_song_name);
        TextView artistName = (TextView) view.findViewById(R.id.player_artist_name);
        ImageView albumArt = (ImageView) view.findViewById(R.id.player_image);
//        BottomNavigationView bottomNavMenu = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
//        bottomNavMenu.setVisibility(View.GONE);
        songName.setText(songPlaying.name);
        artistName.setText(songPlaying.artist);
        if(mp != null)
            mp.stop();
        mp = MediaPlayer.create(view.getContext(),
                view.getResources().getIdentifier(songPlaying.track, "raw", view.getContext().getOpPackageName()));
        mp.start();
        albumArt.setImageResource(view.getResources().getIdentifier(
                songPlaying.art ,"drawable", view.getContext().getOpPackageName()
        ));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mp.stop();
        super.onDestroy();
    }
}
