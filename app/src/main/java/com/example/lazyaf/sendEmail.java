package com.example.lazyaf;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class sendEmail extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SENDEMAIL = "send";

    public sendEmail() {
        super("sendEmail");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void send(Context context, String otp) {
        Intent intent = new Intent(context, sendEmail.class);
        intent.putExtra("otp", otp);
        intent.setAction(ACTION_SENDEMAIL);
        context.startService(intent);
        Log.d("otp", otp);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SENDEMAIL.equals(action)) {
                Log.d("otp2", intent.getStringExtra("otp"));
                sendEmail(intent.getStringExtra("otp"));
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void sendEmail(final String otp) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    GmailSender sender = new GmailSender(
                            "doe470770@gmail.com",
                            "password"
                    );

                    sender.sendEmailNewMethod("anmol01shukla@gmail.com", "Dear Olive,"
                            + "\n\n OTP: " + otp);

                    Log.d("here", "Here1");
                }
                catch (Exception e) {
                    Log.d("Exception Sending Email", e.toString());
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }
}
