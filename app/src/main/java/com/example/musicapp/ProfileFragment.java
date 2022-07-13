package com.example.musicapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private ImageView add_name_btn=null;
    private ImageView add_phone_btn = null;
    private MainViewModel myViewModel;
    private View view_;
    private String dialog_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myViewModel = MainViewModel.getInstance(getActivity().getApplication(), getActivity().getApplication(), getActivity());
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view_ = view;

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
        //adding observer

        Observer<String> observeNameChange = new Observer<String>() {
            @Override
            public void onChanged(String t) {
                if(dialog_name.equals("name"))
                    ((TextView)view_.findViewById(R.id.name)).setText(t);
                else
                    ((TextView)view_.findViewById(R.id.phone_frame)).setText(t);
            }
        };
        MutableLiveData<String> nameL = myViewModel.getInputLiveData();
        try {
            nameL.observe((LifecycleOwner) getContext(), observeNameChange);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        }

}