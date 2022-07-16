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


//creating a class that inherit from android view model
public class MainViewModel extends AndroidViewModel {

    private static MainViewModel instance;
    public Context context;

    //this values have the charters of live data
    private MutableLiveData<String> inputLiveData;


    public MainViewModel(@NonNull Application application) {
        super(application);
        this.context = getApplication().getApplicationContext();
        initlaizeValues();
    }

    public void initlaizeValues(){
        // in this phase we have not said who is the observable data we just set an object of type live data
        inputLiveData = new MutableLiveData<>();
    }

    // Pay attention that MainViewModel is singleton it helps
    public static MainViewModel getInstance(Application application, Context context, Activity activity){
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

}

