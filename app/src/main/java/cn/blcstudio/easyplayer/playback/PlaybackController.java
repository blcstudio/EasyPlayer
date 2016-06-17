package cn.blcstudio.easyplayer.playback;

import android.media.MediaPlayer;

import java.util.List;

import cn.blcstudio.easyplayer.model.Music;

// this controller hold playing state

public interface PlaybackController {
    public boolean isPlaying();

    // get palyer
    public MediaPlayer getMediaPlayer();

    // setup queue
    public void setupQueue(List<Music> queue);
    // setup player
    public void setupPlayer();

    // get current music
    public Music getCurMusic();
    // set current music
    public void jumpTo(int index);

    // start
    public void start();
    // play or pause
    public void play();
    // next
    public void next();
    // prev
    public void prev();

    // seek position
    public int getCurPosition();
    public void seekTo(int newPos);
    public int getTotalTime();

    // queue type
    public void setQueueType(int newType);
    public int getQueueType();
}
