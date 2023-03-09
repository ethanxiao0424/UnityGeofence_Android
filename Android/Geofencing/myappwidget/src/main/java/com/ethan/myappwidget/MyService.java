package com.ethan.myappwidget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service implements Runnable {
    public static final String TAG = MyAppWidget.TAG;
    public static final String CLICK_EVENT = "android.appwidget.action.Click";
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                update();
            }
        }
    };

    @Override
    public void run() {
        handler.sendEmptyMessage(1);
        handler.postDelayed(this,1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate:(Service) ");
        handler.sendEmptyMessage(1);
        handler.post(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,0,startId) ;
        Log.d(TAG, "onStartCommand:(Service) ");
        if (intent.getAction() != null){
            if (intent.getAction().equals(CLICK_EVENT)){
                Handler toastHandler = new Handler(Looper.getMainLooper());
                toastHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "點擊了小物件", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        setButtonClick();
        return START_NOT_STICKY;
    }



    /**設置按鈕廣播發送事件*/
    private void setButtonClick() {
        ComponentName thisWidget = new ComponentName(this,MyAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.my_app_widget);
        Intent myIntent = new Intent();
        myIntent.setAction(CLICK_EVENT);

        PendingIntent pendingIntent = PendingIntent.getService(this,0,myIntent,0);
        remoteViews.setOnClickPendingIntent(R.id.button_Hello,pendingIntent);
        manager.updateAppWidget(thisWidget,remoteViews);
    }

    /**更新時間*/
    private void update(){
        String time = sdf.format(new Date());
        RemoteViews views = new RemoteViews(getPackageName(),R.layout.my_app_widget);
        views.setTextViewText(R.id.textView_TimeLabel,time);
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(),MyAppWidget.class);
        manager.updateAppWidget(componentName,views);
    }
}