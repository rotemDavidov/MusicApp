package com.example.musicapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


//creating a class that inherit from android view model
public class MainViewModel extends AndroidViewModel {

    private static MainViewModel instance;

    public Context context;
    public Activity activity;

    //  The MutableLiveData class has the setValue(T) and postValue(T)
    //  methods publicly and you must use these if you need to edit the value stored in a LiveData object.
    //  Usually, MutableLiveData is used in the ViewModel and then the ViewModel only exposes


    //this values have the charters of live data
    private MutableLiveData<String> inputLiveData;

    public MainViewModel(@NonNull Application application, Context context, Activity activity) {
        super(application);
        // call your Rest API in init method
        this.activity = activity;
        this.context = context;
        initlaizeValues(application);
    }

    // This use the setValue
    public void initlaizeValues(Application application){
        // in this phase we have not said who is the observable data we just set an object of type live data
        inputLiveData = new MutableLiveData<>();
        //now the edit text field is observable
    }

    // Pay attention that MainViewModel is singleton it helps
    public static MainViewModel getInstance(Application application, Context context, Activity activity){
        if(instance ==null){
            instance = new MainViewModel(application, context, activity);
        }
        return instance;
    }

    //getters and setters
    public MutableLiveData<String> getInputLiveData() {
        return inputLiveData;
    }
    public void setInputLiveData(String val){ inputLiveData.setValue(val);}

}

