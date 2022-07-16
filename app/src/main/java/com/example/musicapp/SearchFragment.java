package com.example.musicapp;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;


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
        allSongsList = MainViewModel.getInstance(getActivity().getApplication()).getAllSongs().getValue();
        shownSongsList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
            adapter = new SearchAdapter(getActivity(),this.listener, this.shownSongsList);
            rvSongs.setAdapter(adapter);
            rvSongs.setLayoutManager(new LinearLayoutManager(getActivity()));

        //}
    }
}