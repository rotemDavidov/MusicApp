package com.example.musicapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SearchFragment extends Fragment {
    RecyclerView rvSongs;
    ArrayList<Song> shownSongsList;
    SearchAdapter adapter;
    SearchAdapter.SearchAdapterListener listener;
    ArrayList<Song> allSongsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        allSongsList = SongsCSVParser.parseSongs(inflater.getContext());
        shownSongsList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button allSongsBtn = (Button) getView().findViewById(R.id.all_songs_btn);
        Button searchSongsBtn = (Button) getView().findViewById(R.id.search_songs_btn);
        allSongsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filterSongs(v);
            }
        });
        searchSongsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filterSongs(v);
            }
        });
        rvSongs = view.findViewById(R.id.searchRv);
        SearchView searchView = (SearchView) getView().findViewById(R.id.searchView);
        searchView.setQueryHint("Enter song name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                shownSongsList.clear();
                if(newText != null && !newText.trim().isEmpty()) {
                    for (Song song:allSongsList) {
                        if(StringUtils.containsIgnoreCase(song.name,newText))
                            shownSongsList.add(song);
                    }
                }
                adapter.shownSongs = shownSongsList;
                rvSongs.setAdapter(adapter);
                rvSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
                return false;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
/*        if(context instanceof SearchAdapter.SearchAdapterListener) {
            listener = (SearchAdapter.SearchAdapterListener)context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement SearchAdapterListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //listener = null;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //if(shownSongsNames == null) {
            //shownSongsList = new ArrayList<Song>();
            adapter = new SearchAdapter(getActivity(),this.listener, this.allSongsList);
            rvSongs.setAdapter(adapter);
            rvSongs.setLayoutManager(new LinearLayoutManager(getActivity()));

        //}
    }

    public void filterSongs(View v) {
        Button button = (Button)v;
        SearchView searchView = (SearchView) getView().findViewById(R.id.searchView);
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setQueryHint("Enter song name");
        if(button.getText().toString().equals("All")){
            enableSearchView(getView().findViewById(R.id.searchView),false);
            adapter.shownSongs = allSongsList;
            rvSongs.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            enableSearchView(getView().findViewById(R.id.searchView),true);
            adapter.shownSongs = new ArrayList<Song>();
            rvSongs.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void enableSearchView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                enableSearchView(child, enabled);
            }
        }
    }


}