package co.tinode.tindroid;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import co.tinode.tindroid.services.CallConnection;
import co.tinode.tinodesdk.Topic;

/**
 * Receives broadcasts to hang up or decline video/audio call.
 */
public class CallBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "CallNotificationBroadcastReceiver";

    public static final String ACTION_INCOMING_CALL = "co.tinode.tindroid.ACTION_INCOMING_CALL";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive " + intent.getAction());

        // Clear incoming call notification.
        NotificationManager nm = context.getSystemService(NotificationManager.class);
        nm.cancel(CallConnection.NOTIFICATION_TAG_INCOMING_CALL, 0);

        if (ACTION_INCOMING_CALL.equals(intent.getAction())) {
            String topicName = intent.getStringExtra(Const.INTENT_EXTRA_TOPIC);
            int seq = intent.getIntExtra(Const.INTENT_EXTRA_SEQ, -1);
            Topic topic = Cache.getTinode().getTopic(topicName);
            Log.i(TAG, "Hanging up topic=" + topicName + "; seq=" + seq);
            if (topic != null && seq > 0) {
                // Send message to server that the call is declined.
                topic.videoCallHangUp(seq);
            }
            Cache.endCallInProgress();
        }
    }
}
