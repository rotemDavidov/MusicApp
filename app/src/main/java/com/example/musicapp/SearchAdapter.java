package com.example.musicapp;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    ArrayList<Song> allSongs;
    ArrayList<Song> shownSongs;
    Activity activity;
    SearchAdapterListener listener;
    MediaPlayer mp;

    public SearchAdapter(Activity activity, SearchAdapterListener listener, ArrayList<Song> shownSongs) {
        this.activity = activity;
        this.listener = listener;
        this.allSongs = MainViewModel.getInstance(activity.getApplication()).getAllSongs().getValue();
        this.shownSongs = shownSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.shownSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView songName;
        TextView artistName;
        TextView albumName;
        ImageView albumArt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setClickable(true);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            albumName = (TextView) itemView.findViewById(R.id.album_name);
            albumArt = (ImageView) itemView.findViewById(R.id.album_art);
        }

        public void bindData(final int position) {
            songName.setText(shownSongs.get(position).name);
            artistName.setText(shownSongs.get(position).artist);
            albumName.setText(shownSongs.get(position).album);
            albumArt.setImageResource(activity.getResources().getIdentifier(
                    shownSongs.get(position).art,"drawable", activity.getPackageName()
            ));
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClick(position);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(position);
                }
            });
        }
    }
    public void onItemLongClick(int position) {
        MainViewModel mv = MainViewModel.getInstance(activity.getApplication());
        // getting the name of the songs that been removed
        String s = mv.getAllSongs().getValue().get(position).name;
        // writing the songs to the ignored file
        mv.writeToFile(s);
        // removing the song from the list of all songs
        mv.getAllSongs().getValue().remove(position);
        // notify that the live data has been changed
        notifyItemRemoved(position);
    }

    public void onItemClick(int position) {
        Song song = this.shownSongs.get(position);
        if(mp != null)
            mp.stop();
        mp = MediaPlayer.create(this.activity,
                this.activity.getResources().getIdentifier(song.track, "raw", this.activity.getPackageName()));
        mp.start();
    }

    public interface SearchAdapterListener {
        public void updateShownSongsList(boolean toAdd, String name);
    }
}