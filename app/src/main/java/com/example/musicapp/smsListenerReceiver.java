package com.example.musicapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Message;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Toast;

//we assign this class in the manifest file
public class smsListenerReceiver extends BroadcastReceiver {
    @Override

    //where the event will be handled
    public void onReceive(Context context, Intent intent) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(7<vol) { // in case vol is lower then 7 its useless to low the sound more
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                7, 0);
                        Thread.sleep(1500);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                vol, 0);
                    }
                }
                catch (Exception e ){
                }
            }
        }).start();


        //reading the msg
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        SmsMessage msg = smsMessages[0];
        if(msg != null)
        {
            String sender = msg.getDisplayOriginatingAddress();
            String body = msg.getDisplayMessageBody();
            String data = "New message from: " + sender + "\nThe message: " + body;
            Toast.makeText(context, data, Toast.LENGTH_LONG).show();
        }

    }
}
