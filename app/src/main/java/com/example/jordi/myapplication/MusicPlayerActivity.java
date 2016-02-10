package com.example.jordi.myapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MusicPlayerActivity extends BaseActivity {

    private ImageButton playPauseButton;

    private boolean isPlaying;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;

            mService = binder.getService();

            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;

        }
    };



    LocalService mService;
    boolean bound = false;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT).show();
            playPauseButton.setImageResource(R.drawable.play);
            isPlaying = false;

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        isPlaying = false;

        IntentFilter filter = new IntentFilter();
        filter.addAction("song.complete");
        registerReceiver(receiver, filter);
        super.onResume();

        playPauseButton = (ImageButton) findViewById(R.id.imageButton);
        playPauseButton.setImageResource(R.drawable.play);


        playPauseButton.setOnClickListener(new View.OnClickListener() {
            boolean clickedFirstTime = true;
            @Override
            public void onClick(View v) {
                if (clickedFirstTime) {
                    if (bound) {
                        mService.initializeMediaPlayer();
                    }
                    clickedFirstTime = false;
                }
                if (!isPlaying) {
                    isPlaying = true;
                    if (bound) {
                        mService.play();
                    }
                    playPauseButton.setImageResource(R.drawable.pause);
                }
                else {
                    isPlaying = false;
                    if (bound) {
                        mService.pause();
                    }
                    playPauseButton.setImageResource(R.drawable.play);
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(MusicPlayerActivity.this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.music_player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            SharedPreferences.Editor editorLoggedIn = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE).edit();
            editorLoggedIn.putBoolean("loggedIn", false).apply();
            editorLoggedIn.putString("user",null).apply();
            Intent myIntent = new Intent(this, Login.class);
            this.startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}