package com.ethan.mygeofencing;

//import needed packages / classes.

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class Bridge extends Application
{
    public static Context context;
    public static String notificationContentTitle = "";
    public static String notificationContentText = "";
    public static String notificationDescription = "";
    public static CharSequence notificationUserVisibleName="";

    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList;
    GeofencingRequest.Builder builder;
    PendingIntent geofencePendingIntent;
    GeofencingRequest geofencingRequest;


    public Bridge(Context ctx)
    {
        context = ctx;
        geofencingClient = LocationServices.getGeofencingClient(context);
        geofenceList = new ArrayList<>();
        builder = new GeofencingRequest.Builder();
    }

    public Bridge(Context ctx, String contentTitle, String contentText, String description, String userVisibleName)
    {
        context = ctx;
        notificationContentTitle = contentTitle;
        notificationContentText = contentText;
        notificationDescription = description;
        notificationUserVisibleName = userVisibleName;

        geofencingClient = LocationServices.getGeofencingClient(context);
        geofenceList = new ArrayList<>();
        builder = new GeofencingRequest.Builder();

    }


    public void addGeoFence(String id, double latitude, double longitude,float radius,int loiteringDelay)
    {
        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(id)
                .setCircularRegion(
                        latitude,
                        longitude,
                        radius
                )
                .setNotificationResponsiveness(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                //.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setLoiteringDelay(loiteringDelay)
                .build());
        Log.i("TESTTAG", "GEOFENCE added an list");
        Log.i("TESTTAG", geofenceList.toString());
    }

    @SuppressLint("MissingPermission")
    public void GeoFenceCompleted()
    {
        Log.i("TESTTAG", "Completing Geofence");
        //builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER );
        builder.addGeofences(geofenceList);
        geofencingRequest =  builder.build();

        geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                        Log.i("TESTTAG", "Geofences added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                        Log.e("TESTTAG", "Couldn't add geofences");
                    }
                });
    }

    public void removeAllGeoFences()
    {
        geofenceList.clear();
        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences removed
                        // ...
                        Log.i("TESTTAG", "removed all geofences");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove geofences
                        // ...
                        Log.e("TESTTAG", "Couldn't remove all geofences");
                    }
                });
    }

    public void removeGeoFences(List<String> ids) {
        geofencingClient.removeGeofences(ids);
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            Log.i("TESTTAG", "used old intent for GeofenceBroadcastReceiver");
            return geofencePendingIntent;
        }
        Log.i("TESTTAG", "Created new intent for GeofenceBroadcastReceiver");
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        intent.putExtra("notificationContentTitle",notificationContentTitle);
        intent.putExtra("notificationContentText",notificationContentText);
        intent.putExtra("notificationDescription",notificationDescription);
        intent.putExtra("notificationUserVisibleName",notificationUserVisibleName);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
}