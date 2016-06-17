package cn.blcstudio.easyplayer.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cn.blcstudio.easyplayer.MusicService;
import cn.blcstudio.easyplayer.MusicServiceHolder;
import cn.blcstudio.easyplayer.R;


public class PlayerActivity extends FragmentActivity
        implements PlaybackFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener {

    private ListFragment listFragment;
    private PlaybackFragment playbackFragment;
    private FloatingActionButton btnPlay;
    public boolean isPlaying = false;
    private Intent notificationIntent;

    private MusicService musicService;

    @Override
    protected void onResume() {
        super.onResume();
        if (musicService != null) {
            updateInfoDisplay();
            CurrentActivityHolder.getInstance().setCurActivity(this);
            CurrentActivityHolder.getInstance().setCurActivityType(CurrentActivityHolder.PLAYER_ACTIVITY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // get fragment
        listFragment = (ListFragment) this.getFragmentManager().findFragmentById(R.id.fragment_list);
        playbackFragment = (PlaybackFragment) this.getFragmentManager().findFragmentById(R.id.fragment_playback_controller);

        // start music service
        Intent startIntent = new Intent(this, MusicService.class);
        bindService(startIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        // play button
        btnPlay = (FloatingActionButton) findViewById(R.id.fab_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.play();
                updateInfoDisplay();
            }
        });

        notificationIntent = new Intent(this, FullscreenActivity.class);
    }

    public void updateInfoDisplay() {
        // update floating action button
        if (musicService.isPlaying()) {
            btnPlay.setImageResource(R.drawable.pause);
            isPlaying = true;
        } else {
            btnPlay.setImageResource(R.drawable.play);
            isPlaying = false;
        }
        // update playback controller
        playbackFragment.updateInfoDisplay();
        MusicServiceHolder.getInstance().showNotification(getApplicationContext());
    }

    private ServiceConnection serviceConnection =  new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.setupService(listFragment.getMusicList());
            MusicServiceHolder.getInstance().setMusicService(musicService);
            updateInfoDisplay();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
