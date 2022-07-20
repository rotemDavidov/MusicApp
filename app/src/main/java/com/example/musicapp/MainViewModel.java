package com.example.musicapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Switch;
import java.io.FileOutputStream;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


//creating a class that inherit from android view model
public class MainViewModel extends AndroidViewModel {

    private static MainViewModel instance;
    public Context context;

    //this values have the charters of live data
    private MutableLiveData<String> nameLiveData;
    private MutableLiveData<String> phoneLiveData;
    private MutableLiveData<ArrayList<Song>> allSongs = null;
    private ArrayList<String> ignoredSongs;



    public MainViewModel(@NonNull Application application) {
        super(application);
        this.context = getApplication().getApplicationContext();
        initlaizeValues();
    }

    public void initlaizeValues(){
        // in this phase we have not said who is the observable data we just set an object of type live data
        phoneLiveData = new MutableLiveData<>();
        nameLiveData = new MutableLiveData<>();
        ignoredSongs = this.readFromFile("sp");

    }

    // Pay attention that MainViewModel is singleton it helps
    public static MainViewModel getInstance(Application application){
        if(instance ==null){
            instance = new MainViewModel(application);
        }
        return instance;
    }

    //getters and setters
    public MutableLiveData<String> getNameLiveData() {
        return nameLiveData;
    }
    public void setNameLiveData(String val){ nameLiveData.setValue(val);}

    public MutableLiveData<String> getPhoneLiveData() {
        return phoneLiveData;
    }
    public void setPhoneLiveData(String val){ phoneLiveData.setValue(val);}

    public MutableLiveData<ArrayList<Song>> getAllSongs(){
        ArrayList<Song> listOfSongs;
        boolean switch_ans = context.getSharedPreferences(ProfileFragment.SHARED_PREFS, Context.MODE_PRIVATE).getBoolean(ProfileFragment.ANS_SWITCH,false);
        if(allSongs == null){
            allSongs = new MutableLiveData<ArrayList<Song>>();
            listOfSongs = SongsCSVParser.parseSongs(context);
            listOfSongs.removeIf(song -> (ignoredSongs.contains(song.name) && switch_ans));
            allSongs.setValue(listOfSongs);
        }
        return allSongs;
    }

    public ArrayList<String> readFromFile(String output_format) {
        ArrayList<String> list_ = new ArrayList<String>();

        if(output_format.equals("sp")) {
            list_ = new ArrayList<String>();
            SharedPreferences sharedPref = context.getSharedPreferences("ignoredList", Context.MODE_PRIVATE);
            Set<String> s = (Set<String>) sharedPref.getStringSet("ignore", Collections.singleton(""));
            list_.addAll(s);
        }
        else{
            try {
                InputStream inputStream = context.openFileInput("favorites_songs.txt");
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    while ((receiveString = bufferedReader.readLine()) != null) {
                        list_.add(receiveString);
                    }
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }
        return list_;

    }

    //creating NEW sp that save the ignore songs
    public void writeToFile(String data,String output_format) {
        if(output_format.equals("sp")) {
            SharedPreferences sharedPref = context.getSharedPreferences("ignoredList", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Set<String> set = (HashSet<String>) sharedPref.getStringSet("ignore", new HashSet<String>());
            Set<String> setCopy = new HashSet(set);
            setCopy.add(data);
            editor.putStringSet("ignore", setCopy);
            editor.apply();
        }
        else{
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.context.openFileOutput("favorites_songs.txt", Context.MODE_APPEND));
                outputStreamWriter.write(data + '\n');
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

        }
    }
    public void removeSongFromRaw(String delete_song) throws IOException {
        //reading the file to get all the name in the file
        ArrayList<String> all_songs = readFromFile("row");
        int index=-1;
        for (int i=0;i<all_songs.size();i++)
            if(all_songs.get(i).equals(delete_song)) {
                index = i;
                break;
            }
        //removing the song from the list
        all_songs.remove(index);
        // clear the existing file
        OutputStream out = this.context.openFileOutput("favorites_songs.txt", Context.MODE_PRIVATE);
        out.flush();
        out.write("".getBytes());
        out.close();
        //write the songs that left
        for (int i=0;i<all_songs.size();i++)
            writeToFile(all_songs.get(i),"row");


    }
    public boolean songInFavoritFile(String name){
        ArrayList<String> all_songs = readFromFile("row");
        for (int i=0;i<all_songs.size();i++)
            if(all_songs.get(i).equals(name)) {
                return true;
            }
        return false;
    }

}

