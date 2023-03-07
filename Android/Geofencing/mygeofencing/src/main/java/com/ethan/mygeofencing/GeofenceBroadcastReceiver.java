package com.ethan.mygeofencing;

import static androidx.core.content.ContextCompat.getSystemService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    // ...
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TESTTAG", "GEOFENCE received");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e("TESTTAG", errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        // Test that the reported transition was of interest.

            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {
            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Get the transition details as a String.
            String geofenceTransitionDetails = triggeringGeofences.toString();
            // Send notification and log the transition details.
            sendNotification(context,intent);
            Log.i("TESTTAG", geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e("TESTTAG", "Transition Error");
        }
    }

    private void sendNotification(Context context,Intent BridgeIntent) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.putExtra("IS_FROM_NOTIFICATION", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Log.i("TESTTAG", "GEOFENCE triggered");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, BridgeIntent.getCharSequenceExtra("notificationUserVisibleName").toString())
                .setContentIntent(pendingIntent)
                .setSmallIcon(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_light)
                .setContentTitle(BridgeIntent.getStringExtra("notificationContentTitle"))
                .setContentText(BridgeIntent.getStringExtra("notificationContentText"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = BridgeIntent.getCharSequenceExtra("notificationUserVisibleName");
            String id = BridgeIntent.getCharSequenceExtra("notificationUserVisibleName").toString();
            String description = BridgeIntent.getStringExtra("notificationDescription");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(10000, builder.build());
            Log.i("TESTTAG", "notification Shown");
        }
    }
}