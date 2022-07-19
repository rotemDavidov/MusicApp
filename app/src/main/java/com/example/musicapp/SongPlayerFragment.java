package com.example.musicapp;

import android.content.Context;
import android.media.AudioManager;
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
    TextView elapsedTime;
    TextView remainingTime;
    int songDuration;
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;

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
        initControls();
        elapsedTime = (TextView) view.findViewById(R.id.time_counter_up);
        remainingTime = (TextView) view.findViewById(R.id.time_counter_down);
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
                            progressHandler.sendMessage(msg);
                            Thread.sleep(10);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                songDuration = mp.getDuration();
                int seconds = 0;
                int minutes = 0;
                String stringToSend = new String();
                while(mp != null) {
                    try{
                        if(mp.isPlaying()){
                            Message msgElapsed = new Message();
                            Message msgRemaining = new Message();
                            msgElapsed.obj = getTimeString(mp.getCurrentPosition());
                            elapsedTimeHandler.sendMessage(msgElapsed);
                            msgRemaining.obj = getTimeString(mp.getDuration() - mp.getCurrentPosition());
                            remainingTimeHandler.sendMessage(msgRemaining);
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        super.onViewCreated(view, savedInstanceState);
    }

    private Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            songSeek.setProgress(msg.what);
        }
    };

    private Handler elapsedTimeHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String time = (String)msg.obj;
            elapsedTime.setText(time);
        }
    };

    private Handler remainingTimeHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String time = (String)msg.obj;
            remainingTime.setText(time);
        }
    };

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        long minutes = ( millis % (1000*60*60) ) / (1000*60);
        long seconds = ( ( millis % (1000*60*60) ) % (1000*60) ) / 1000;

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

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

    private void initControls()
    {
        try
        {
            volumeSeekbar = (SeekBar) getView().findViewById(R.id.player_volume);
            audioManager = (AudioManager) getView().getContext().getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
