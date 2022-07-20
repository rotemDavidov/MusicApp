package com.example.musicapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private final SongPlayerFragment songPlayerFragment;
    private Activity activity;
    private ArrayList<Song> fave_songs;

    public FavoritesAdapter(Activity activity,ArrayList<Song> fave_songs) {
        this.activity = activity;
        this.fave_songs = fave_songs;
        this.songPlayerFragment = new SongPlayerFragment();

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(position);

    }

    @Override
    public int getItemCount() {
        return this.fave_songs.size();
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

        public void bindData(int position) {
            songName.setText(fave_songs.get(position).name);
            artistName.setText(fave_songs.get(position).artist);
            albumName.setText(fave_songs.get(position).album);
            albumArt.setImageResource(activity.getResources().getIdentifier(
                    fave_songs.get(position).art,"drawable", activity.getPackageName()
            ));
            ((ImageView) itemView.findViewById(R.id.favorite_image)).setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(position);
                }
            });
        }
    }
    public void onItemClick(int position) {
        Song song = this.fave_songs.get(position);
        Bundle b = new Bundle();
        //b.putInt("SongPosition", position);
        b.putSerializable("song", song);
        //b.putParcelable("SongsList", (Parcelable) shownSongs);
        songPlayerFragment.setArguments(b);
        ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, songPlayerFragment)
                .addToBackStack("SongPlayerFragment")
                .commit();
    }
}
