package com.example.musicapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    ArrayList<String> name_songs;
    ArrayList<Song> all_songs;
    RecyclerView rvFave;
    FavoritesAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainViewModel mv =  MainViewModel.getInstance(getActivity().getApplication());
        name_songs = mv.readFromFile("raw");
        all_songs = mv.getAllSongs().getValue();
        rvFave = view.findViewById(R.id.faveRv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new FavoritesAdapter(getActivity(),return_fave_list());
        rvFave.setAdapter(adapter);
        rvFave.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private ArrayList<Song> return_fave_list() {
        ArrayList<Song> fave_songs = new ArrayList<Song>();
        for(int i=0;i<all_songs.size();i++){
            if(name_songs.contains(all_songs.get(i).name))
                fave_songs.add(all_songs.get(i));

        }
        return fave_songs;
    }
}