package com.example.musicapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongsCSVParser {

    private static InputStream openSongsFile(Context context){
        AssetManager assetManager = context.getAssets();
        InputStream in =null;
        try {
            in = assetManager.open("songs.csv");
        } catch (IOException e) {e.printStackTrace();}
        return in;
    }

    private static List<String[]> readSongsCSV(Context context) throws IOException {
        List<String> songLine = new ArrayList<>();
        List<String[]> songs = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(assetManager.open("songs.csv"));
        } catch (IOException e) { }
        BufferedReader reader = new BufferedReader(is);
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            songLine.add(line);
        }
        for (String song: songLine) {
            songs.add(song.split(","));
        }
        return songs;
    }

    //Read all songs from CSV and create an arraylist from them
    public static ArrayList<Song> parseSongs(Context context) {
        List<String[]> songsFromFile = new ArrayList<>();
        try {
            songsFromFile = readSongsCSV(context);
        }
        catch (IOException e){}
        ArrayList<Song> songsList = new ArrayList<>();
        for(String[] singleSong: songsFromFile) {
            songsList.add(new Song(singleSong[0],singleSong[1],singleSong[2],
                    singleSong[3],singleSong[4]));
        }
        return songsList;
    }

}
