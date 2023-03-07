package com.ethan.mygeofencing;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

public class myUnityActivity extends UnityPlayerActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TESTTAG", "Running MyUnityPlayerActivityOnCreate.");

    }
    @Override
    protected void onStart() {
        super.onStart();
        boolean isFromNotification =
                getIntent().getBooleanExtra("IS_FROM_NOTIFICATION",false);

        if(isFromNotification){
            UnityPlayer.UnitySendMessage("AndroidGeofenceCaller", "GetMessageFromAndroid", "onStart");
            Log.i("TESTTAG", "Running MyUnityPlayerActivityOnStart.");
        }
    }
}