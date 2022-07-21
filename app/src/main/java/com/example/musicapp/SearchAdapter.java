package com.example.musicapp;

import android.app.Activity;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    ArrayList<Song> allSongs;
    ArrayList<Song> shownSongs;
    Activity activity;
    SearchAdapterListener listener;
    SongPlayerFragment songPlayerFragment;
    MediaPlayer mp;
    MainViewModel mainViewModel;

    public SearchAdapter(Activity activity, SearchAdapterListener listener, ArrayList<Song> shownSongs) {
        this.activity = activity;
        this.listener = listener;
        mainViewModel =  MainViewModel.getInstance(activity.getApplication());
        this.allSongs = mainViewModel.getAllSongs().getValue();
        this.shownSongs = shownSongs;
        this.songPlayerFragment = new SongPlayerFragment();
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
        ImageView favorit;

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
            //------------- bind the hearts according to thayer existing in the raw file------------
            String song_name = mainViewModel.getAllSongs().getValue().get(position).name;
            // if the name is in the file then we marked it as favorite and we need to bind the full heart
            // we have to do this check because the constructor in song class put false as default
            if(mainViewModel.songInFavoritFile(song_name)) {
                ((ImageView) itemView.findViewById(R.id.favorite_image)).setImageResource(R.drawable.heart);
                mainViewModel.getAllSongs().getValue().get(position).fave=true;
            }

            else //we bind the hollow heart
                ((ImageView)itemView.findViewById(R.id.favorite_image)).setImageResource(R.drawable.empty_heart);

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

            //setting Favorits LISTNER
            favorit = (ImageView) itemView.findViewById(R.id.favorite_image);
            favorit.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    boolean ans_fave = mainViewModel.getAllSongs().getValue().get(position).fave;
                    mainViewModel.getAllSongs().getValue().get(position).fave = !ans_fave;
                    if(mainViewModel.getAllSongs().getValue().get(position).fave) // save to favorites
                        save2favorites(view,position);
                    else // delete from favorites
                    {
                        try {
                            unsave2favorites(view,position);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    public void onItemLongClick(int position) {
        // getting the name of the songs that been removed
        String s =mainViewModel.getAllSongs().getValue().get(position).name;
        // writing the songs to the ignored file
        mainViewModel.writeToFile(s,"sp");
        // removing the song from the list of all songs
        mainViewModel.getAllSongs().getValue().remove(position);
        // notify that the live data has been changed
//        notifyItemRemoved(position);
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
        ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, songPlayerFragment)
                .addToBackStack("SongPlayerFragment")
                .commit();
    }

    public interface SearchAdapterListener {
        public void updateShownSongsList(boolean toAdd, String name);
    }

    public void unsave2favorites(View view, int position) throws IOException {
        ((ImageView)view.findViewById(R.id.favorite_image)).setImageResource(R.drawable.empty_heart);
        String song_name = mainViewModel.getAllSongs().getValue().get(position).name;
        mainViewModel.removeSongFromRaw(song_name);

    }

    public void save2favorites(View view, int position) {
        ((ImageView)view.findViewById(R.id.favorite_image)).setImageResource(R.drawable.heart);
        String song_name = mainViewModel.getAllSongs().getValue().get(position).name;
        mainViewModel.writeToFile(song_name,"row");
    }


}
