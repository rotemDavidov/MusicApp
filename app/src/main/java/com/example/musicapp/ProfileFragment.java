package com.example.musicapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.example.musicapp.R;
import android.content.SharedPreferences;


public class ProfileFragment extends Fragment {

    public static final String SHARED_PREFS="sharedPre";
    public static final String TEXT_NAME ="text_name";
    public static final String TEXT_PHONE ="text_phone";
    public static final String ANS_SWITCH="switch";
    private static String dialog_name;

    private ImageView add_name_btn=null;
    private ImageView add_phone_btn = null;
    private Switch sw = null;
    private MainViewModel myViewModel;

    public static View view_ = null;
    private String text_name;
    private String text_phone;
    private boolean ansSwitch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myViewModel = MainViewModel.getInstance(getActivity().getApplication());
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view_ = view;

        // ------------------- SET LISTENERS -------------------------------------
        add_name_btn = (ImageView) view.findViewById(R.id.add_name);
        add_name_btn.setOnClickListener(new OnClickListener(){
            public void onClick(View view) {
                dialog_name = "name";
                DialogClass dialog = new DialogClass("name");
                dialog.show(getChildFragmentManager(), "dialog_name");
            }});

        add_phone_btn = (ImageView) view.findViewById(R.id.phone_btn);
        add_phone_btn.setOnClickListener(new OnClickListener(){
            public void onClick(View view) {
                dialog_name = "phone";
                DialogClass dialog = new DialogClass("phone");
                dialog.show(getChildFragmentManager(), "dialog_name");
            }});

        sw = (Switch)view_.findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveDataSwitch(isChecked);
                if(!isChecked)
                    clearSp();
            }
        });

        //--------------------- SETTING OBSERVER --------------------------------------

        Observer<String> observeNameChange = new Observer<String>() {
            @Override
            public void onChanged(String t) {
                dialog_name="name";
                ((TextView)view_.findViewById(R.id.name)).setText(t);
                saveDataStrings();
            }
        };
        MutableLiveData<String> nameL = myViewModel.getNameLiveData();
        nameL.observe(getViewLifecycleOwner(), observeNameChange);

        Observer<String> observePhoneChange = new Observer<String>() {
            @Override
            public void onChanged(String t) {
                dialog_name="phone";
             ((TextView)view_.findViewById(R.id.phone_frame)).setText(t);
             saveDataStrings();
            }
        };
        MutableLiveData<String> phoneL = myViewModel.getPhoneLiveData();
        phoneL.observe(getViewLifecycleOwner(), observePhoneChange);


        //------------------------ LOAD SAVE DATA ------------------------------------
        loadData();
        updateData();
        }

    private void saveDataStrings() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT_NAME, ((TextView)view_.findViewById(R.id.name)).getText().toString());
        editor.putString(TEXT_PHONE, ((TextView)view_.findViewById(R.id.phone_frame)).getText().toString());
        editor.apply();
    }
    private void saveDataSwitch(boolean ans){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ANS_SWITCH, ans);
        editor.apply();

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, getActivity().MODE_PRIVATE);
        text_name = sharedPreferences.getString(TEXT_NAME,"Israel Israeli");
        text_phone = sharedPreferences.getString(TEXT_PHONE,"05X-XXXXXXX");
        ansSwitch = sharedPreferences.getBoolean(ANS_SWITCH,false);
    }
    private void updateData(){
        ((TextView)view_.findViewById(R.id.name)).setText(text_name);
        ((TextView)view_.findViewById(R.id.phone_frame)).setText(text_phone);
        ((Switch)view_.findViewById(R.id.switch1)).setChecked(ansSwitch);
    }

    //if the switch is off then we need to clear the sp that hold the ignore lists from the last time
    private void clearSp(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("ignoredList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("ignore");
        editor.apply();
    }

    public static String getDialogClickedName(){
        return dialog_name;
    }

}