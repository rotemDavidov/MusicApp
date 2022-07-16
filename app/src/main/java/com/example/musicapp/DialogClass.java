package com.example.musicapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class DialogClass extends DialogFragment {

    public View Viewdialog;
    public String dialog_name;

    public DialogClass(String name) {
        this.dialog_name = name;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Viewdialog = requireActivity().getLayoutInflater().inflate(R.layout.dialog_name, null); //getting the SeekBar view
        if(dialog_name.equals("name")) {
            builder.setTitle("Update Your Name");
        }
        else{
            ((EditText)Viewdialog.findViewById(R.id.input)).setHint("enter your phone number here");
            builder.setTitle("Update Your Phone");

        }
        builder.setView(Viewdialog);
        builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = ((EditText)Viewdialog.findViewById(R.id.input)).getText().toString();
                                //there is a instance because the profile create one of this
                                MainViewModel.getInstance(getActivity().getApplication(), getContext(), getActivity()).setInputLiveData(name);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                );
        return builder.create();
    }

}
