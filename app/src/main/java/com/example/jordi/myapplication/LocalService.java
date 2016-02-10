package com.example.jordi.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class LocalService extends Service implements MediaPlayer.OnCompletionListener {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    private MediaPlayer mediaPlayer;

    public LocalService(){

    }

    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * method for clients
     */
    public void initializeMediaPlayer() {

        mediaPlayer = MediaPlayer.create(this, R.raw.gameover);
        mediaPlayer.setOnCompletionListener(this);
        Log.v("inicialitzat", "inicialitzat");
    }
    public void play() {
        try {
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            mediaPlayer.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.stop();
            mediaPlayer.prepare();
            Intent intent = new Intent();
            intent.setAction("song.complete");
            getApplicationContext().sendBroadcast(intent);
            //playPauseButton.setImageResource(R.drawable.play);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}


