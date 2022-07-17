package com.example.musicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class smsListenerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
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
