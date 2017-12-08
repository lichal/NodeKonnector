package crorg.node_konnector;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.v("FIREBASE123", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        notifyUserOfSurpassingFriendScore();
    }


    private void notifyUserOfSurpassingFriendScore() {
        String CHANNEL_ID = "nodeKonnector_channel_0156";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
        mBuilder.setContentTitle("SECRET MESSAGE");
        mBuilder.setContentText("You just received a push notification!");
        mBuilder.setTicker("NODES!");
        mBuilder.setVibrate(new long[]{0, 100, 100, 100, 100, 100, 100, 1500, 1000, 100, 100, 100, 100, 100, 100, 1500});
        mBuilder.setColor(0xff00ffff);  // color for app name title in notifications drawer
        mBuilder.setLights(0xffff00ff, 1000, 500);  // sets flashing lights pattern when phone is locked
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(2);
        mBuilder.setVisibility(VISIBILITY_PUBLIC);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, StartUpScreen.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(StartUpScreen.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 456;
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }
}





