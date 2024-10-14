package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallStateReceiver extends BroadcastReceiver {
    private static final String TAG = "CallStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            // Incoming call detected
            Log.i(TAG, "Incoming call detected. Starting AcceptCallActivity.");

            // Start AcceptCallActivity to answer the call
            Intent finishIntent = new Intent(context, AcceptCallActivity.class);
            finishIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(finishIntent);
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            // Call is answered
            Log.i(TAG, "Call answered.");
        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            // Call ended
            Log.i(TAG, "Call ended.");
        }
    }

}
