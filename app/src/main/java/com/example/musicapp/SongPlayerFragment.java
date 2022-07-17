package com.example.musicapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
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
    SeekBar songSeek;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song,container,false);
        Bundle b = this.getArguments();
        if(b != null)
            songPlaying = (Song)b.getSerializable("song");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView songName = (TextView) view.findViewById(R.id.player_song_name);
        TextView artistName = (TextView) view.findViewById(R.id.player_artist_name);
        ImageView albumArt = (ImageView) view.findViewById(R.id.player_image);
        songSeek = (SeekBar) view.findViewById(R.id.player_song_position);
        ImageButton playPauseBtn = (ImageButton) view.findViewById(R.id.player_button);
        playPauseBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if((mp != null) && mp.isPlaying()) {
                    mp.pause();
                    playPauseBtn.setImageResource(R.drawable.play_btn);
                }
                else {
                    mp.start();
                    playPauseBtn.setImageResource(R.drawable.pause_btn);
                }
            }
        });
        songName.setText(songPlaying.name);
        artistName.setText(songPlaying.artist);
        if(mp != null)
            mp.stop();
        mp = MediaPlayer.create(view.getContext(),
                view.getResources().getIdentifier(songPlaying.track, "raw", view.getContext().getOpPackageName()));
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                songSeek.setMax(mp.getDuration());
                mp.start();
                playPauseBtn.setImageResource(R.drawable.pause_btn);
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playPauseBtn.setImageResource(R.drawable.play_btn);
                songSeek.setProgress(0);
            }
        });
        albumArt.setImageResource(view.getResources().getIdentifier(
                songPlaying.art ,"drawable", view.getContext().getOpPackageName()
        ));
        songSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp != null) {
                    try{
                        if(mp.isPlaying()){
                            Message msg = new Message();
                            msg.what = mp.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(10);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        super.onViewCreated(view, savedInstanceState);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            songSeek.setProgress(msg.what);
        }
    };

    @Override
    public void onDestroy() {
        mp.stop();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mp.stop();
        super.onPause();
    }
}
