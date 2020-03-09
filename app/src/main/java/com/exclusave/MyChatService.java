package com.exclusave;

import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Service;
import android.os.IBinder;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

public class MyChatService extends Service {

    public static final String INTENT_FILTER = "INTENT_FILTER";

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    String UserID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        app_id: '827806', key: '3f749e3c939bc5802992', secret: 'c7e81df3d54460369963', cluster: 'ap2', encrypted: true
//        app_id: '827806',
//                key: '3f749e3c939bc5802992',
//                secret: 'c7e81df3d54460369963',
//                cluster: 'ap2',
//                encrypted: true


        sharedpreferences = getSharedPreferences(MyPREFERENCES, this.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        UserID = sharedpreferences.getString("UserID", "");
        //TODO do something useful

        PusherOptions options = new PusherOptions();
        options.setCluster("ap2");
        Pusher pusher = new Pusher( "3f749e3c939bc5802992", options);

        Channel channel = pusher.subscribe("Exclusave_Channel_Chat_" + UserID);

        channel.bind("Exclusave_Event_Chat_" + UserID, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {

                Intent intent1 = new Intent(INTENT_FILTER);
                intent1.putExtra("message", data);
                sendBroadcast(intent1);
//                System.out.println(data);
                Log.e("PusherMsg", " " + data);
            }
        });

        pusher.connect();


        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}

