package com.demo.xp;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.media.VolumeProviderCompat;

public class PlayerService extends Service {
    private MediaSessionCompat mediaSession;

    public  static boolean start = true;

    @Override
    public void onCreate() {
        super.onCreate();
//        Notification notification = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(getApplicationContext(), "study").build();
//        }
//        startForeground(1, notification);

        mediaSession = new MediaSessionCompat(this, "com.demo.xp.PlayerService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0) //you simulate a player which plays something.
                .build());

        //this will only work on Lollipop and up, see https://code.google.com/p/android/issues/detail?id=224134
        VolumeProviderCompat myVolumeProvider =
                new VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE, /*max volume*/100, /*initial volume level*/50) {
                    @Override
                    public void onAdjustVolume(int direction) {
                        Intent intent = new Intent("start");
                        intent.setAction("start");

                        if (direction==1) {
 intent.putExtra("start","1");
sendBroadcast(intent);
}
                        if (direction==-1) {
                            intent.putExtra("start","0");
                            sendBroadcast(intent);  }
                        Log.i("sdfkdjlfdsf", "onAdjustVolume: "+direction);
                    }
                };
        mediaSession.setPlaybackToRemote(myVolumeProvider);
        mediaSession.setActive(true);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaSession.release();
    }
}