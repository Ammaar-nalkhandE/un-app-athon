package com.example.myapplication;//package com.example.myapplication;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//
//public class CallStateReceiver extends BroadcastReceiver {
//    private static final String TAG = "CallStateReceiver";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//
//        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
//            // Incoming call detected
//            Log.i(TAG, "Incoming call detected. Starting AcceptCallActivity.");
//
//            // Start AcceptCallActivity to answer the call
//            Intent finishIntent = new Intent(context, AcceptCallActivity.class);
//            finishIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(finishIntent);
//        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
//            // Call is answered
//            Log.i(TAG, "Call answered.");
//        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
//            // Call ended
//            Log.i(TAG, "Call ended.");
//        }
//    }
//
//}
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telecom.TelecomManager;
import android.util.Log;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;


public class CallStateReceiver extends BroadcastReceiver {
    private static final String TAG = "CallStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            Log.d(TAG, "Phone is ringing");
            acceptIncomingCall(context);
        }
    }

    private void acceptIncomingCall(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            if (telecomManager != null && ActivityCompat.checkSelfPermission(context, "android.permission.ANSWER_PHONE_CALLS"
            ) == PackageManager.PERMISSION_GRANTED) {
                telecomManager.acceptRingingCall();
                Log.d(TAG, "Call accepted automatically");
            } else {
                Log.e(TAG, "Permission not granted or unsupported device");
            }
        } else {
            Log.e(TAG, "This feature is not supported on your Android version");
        }
    }
}
