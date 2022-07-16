package com.example.musicapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


//creating a class that inherit from android view model
public class MainViewModel extends AndroidViewModel {

    private static MainViewModel instance;
    public Context context;

    //this values have the charters of live data
    private MutableLiveData<String> inputLiveData;
    private MutableLiveData<ArrayList<Song>> allSongs = null;
    private ArrayList<String> ignoredSongs;



    public MainViewModel(@NonNull Application application) {
        super(application);
        this.context = getApplication().getApplicationContext();
        initlaizeValues();
    }

    public void initlaizeValues(){
        // in this phase we have not said who is the observable data we just set an object of type live data
        inputLiveData = new MutableLiveData<>();
        ignoredSongs = this.readFromFile();

    }

    // Pay attention that MainViewModel is singleton it helps
    public static MainViewModel getInstance(Application application){
        if(instance ==null){
            instance = new MainViewModel(application);
        }
        return instance;
    }

    //getters and setters
    public MutableLiveData<String> getInputLiveData() {
        return inputLiveData;
    }
    public void setInputLiveData(String val){ inputLiveData.setValue(val);}

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

    public ArrayList<String> readFromFile() {
        ArrayList<String> ignoredSongs = new ArrayList<String>();
        SharedPreferences sharedPref = context.getSharedPreferences("ignoredList", Context.MODE_PRIVATE);
        Set<String> s = (Set<String>)sharedPref.getStringSet("ignore", Collections.singleton(""));
        ignoredSongs.addAll(s);
        return ignoredSongs;
    }

    //creating NEW sp that save the ignore songs
    public void writeToFile(String data) {
        SharedPreferences sharedPref = context.getSharedPreferences("ignoredList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> set = (HashSet<String>) sharedPref.getStringSet("ignore",new HashSet<String>());
        Set<String> setCopy = new HashSet(set);
        setCopy.add(data);
        editor.putStringSet("ignore",setCopy);
        editor.apply();
    }

}

