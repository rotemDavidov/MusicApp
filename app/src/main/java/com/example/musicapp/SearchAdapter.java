package com.example.musicapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    ArrayList<Song> allSongs;
    ArrayList<Song> shownSongs;
    Context context;
    SearchAdapterListener listener;
    SongPlayerFragment songPlayerFragment;
    MediaPlayer mp;

    public SearchAdapter(Context context, SearchAdapterListener listener, ArrayList<Song> shownSongs) {
        this.context = context;
        this.listener = listener;
        this.allSongs = SongsCSVParser.parseSongs(context);
        this.shownSongs = shownSongs;
        this.songPlayerFragment = new SongPlayerFragment();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            albumArt.setImageResource(context.getResources().getIdentifier(
                    shownSongs.get(position).art,"drawable", context.getPackageName()
            ));
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClick(position);
                    return false;
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
        Song song = this.shownSongs.get(position);
        //TODO: add code for saving to favorites
        notifyDataSetChanged();
    }

    public void onItemClick(int position) {
        Song song = this.shownSongs.get(position);
/*        if(mp != null)
            mp.stop();
        mp = MediaPlayer.create(this.context,
                this.context.getResources().getIdentifier(song.track, "raw", this.context.getPackageName()));
        mp.start();*/
        Bundle b = new Bundle();
        //b.putInt("SongPosition", position);
        b.putSerializable("song", song);
        //b.putParcelable("SongsList", (Parcelable) shownSongs);
        songPlayerFragment.setArguments(b);
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, songPlayerFragment)
                .addToBackStack("SongPlayerFragment")
                .commit();
    }

    public interface SearchAdapterListener {
        public void updateShownSongsList(boolean toAdd, String name);
    }
}
