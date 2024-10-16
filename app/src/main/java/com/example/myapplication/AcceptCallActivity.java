package com.example.myapplication;
import android.telephony.TelephonyManager;
import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import android.view.WindowManager;
import android.content.pm.PackageManager;


import java.util.logging.Logger;

public class AcceptCallActivity extends Activity {
    private static final int REQUEST_CODE = 100;
    private static Logger logger = Logger.getLogger(AcceptCallActivity.class.getName());
    private KeyguardManager keyguardManager;
    private AudioManager audioManager;
    private CallStateReceiver callStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_call); // Ensure this layout exists

        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Button to answer the call
        Button answerButton = findViewById(R.id.answerButton);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptCall();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerCallStateReceiver();
        updateWindowFlags();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (callStateReceiver != null) {
            unregisterReceiver(callStateReceiver);
            callStateReceiver = null;
        }
    }

    private void registerCallStateReceiver() {
        callStateReceiver = new CallStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(callStateReceiver, intentFilter);
    }

    private void updateWindowFlags() {
        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
    }

    private void acceptCall() {
        // Using TelecomManager to answer the call
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        if (telecomManager != null && ActivityCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
            telecomManager.acceptRingingCall();
            Toast.makeText(this, "Call Accepted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission not granted or unsupported device", Toast.LENGTH_SHORT).show();
        }
    }
    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    }

}
