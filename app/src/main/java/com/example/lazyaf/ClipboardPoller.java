package com.example.lazyaf;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.lazyaf.sendEmail;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.support.constraint.Constraints.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ClipboardPoller extends IntentService {
    private static final String ACTION_POLL = "poll";
    private static final String regex = "^[0-9]{6}$";
    private boolean otpFilter = true;
    private long timeout = 5000;
    private String otp = "";

//    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "com.example.lazyaf.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.example.lazyaf.extra.PARAM2";

    public ClipboardPoller() {
        super("ClipboardPoller");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void poll(Context context) {
        Intent intent = new Intent(context, ClipboardPoller.class);
        intent.setAction(ACTION_POLL);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendEmail newClient = new sendEmail();

        if (intent != null) {
            final String action = intent.getAction();

            while(true) {
                try {
                    Thread.sleep(this.timeout);
                    if (ACTION_POLL.equals(action)) {
                        String item = getClipItem();

                        Log.d("ClipItem0:", item);
                        Log.d("Condition-2", String.valueOf(!otp.equals(item)));

                        if (item.length() != 0) {
                            if (!otp.equals(item)) {
                                otp = item;

                                newClient.send(this, otp);

//                                sendBroadcast(item);
                            }
                        }
                    }
                }
                catch (InterruptedException e) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public String getClipItem() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";
        Boolean clipBoardHasData = false;

        if (!(clipboard.hasPrimaryClip())) { clipBoardHasData = false; }
        else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
            clipBoardHasData = false;
        }
        else { clipBoardHasData = true; }

        if (clipBoardHasData) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

            // Gets the clipboard as text.
            pasteData = item.getText().toString();

            if (pasteData != null) {
                // We're alright. Apply filter.
                boolean isThisAnOTP = this.matcher(pasteData);

                if (!isThisAnOTP && this.otpFilter) {
                    pasteData = "";
                }
            }
            else {
                pasteData = "";
                Log.e(TAG, "Clipboard contains an invalid data type");
            }
        }
        else {
            // Do nothing.
        }

        return pasteData;
    }

    private boolean matcher(String otpCandidate) {
        Pattern r = Pattern.compile(regex);

        // Now create matcher object.
        Matcher m = r.matcher(otpCandidate);

        if (m.find( )) {
            return true;
        } else {
            return false;
        }
    }

    private void sendBroadcast (String clip){
        Intent intent = new Intent ("ClipItem"); //put the same message as in the filter you used in the activity when registering the receiver
        intent.putExtra("item", clip);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
