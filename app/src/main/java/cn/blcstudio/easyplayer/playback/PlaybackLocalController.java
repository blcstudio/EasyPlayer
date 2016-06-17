package cn.blcstudio.easyplayer.playback;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import cn.blcstudio.easyplayer.MusicService;
import cn.blcstudio.easyplayer.MusicServiceHolder;
import cn.blcstudio.easyplayer.model.Music;
import cn.blcstudio.easyplayer.playback.queue.QueueHolder;

/**
 * Created by ginger on 2016/6/15.
 */

public class PlaybackLocalController implements PlaybackController {

    // queue holder
    QueueHolder queueHolder = QueueHolder.getInstance();
    MediaPlayer mediaPlayer = new MediaPlayer();

    public PlaybackLocalController() {
    }

    // get palyer
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    // set up queue
    public void setupQueue(List<Music> queue) {
        queueHolder.setupQueue(queue);
    }

    @Override
    public void setupPlayer() {
        Music music = queueHolder.getCurMusic();

        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getUrl());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Music getCurMusic() {
        return queueHolder.getCurMusic();
    }

    @Override
    public void jumpTo(int index) {
        queueHolder.jumpTo(index);
    }

    // start to play a new music
    @Override
    public void start() {
        Music music = queueHolder.getCurMusic();

        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // is playing
    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    // play if pause, pause if play
    @Override
    public void play() {
        if (isPlaying()) {
            mediaPlayer.pause();
        }
        else {
            mediaPlayer.start();
        }
    }

    // next song
    @Override
    public void next() {
        queueHolder.moveToNext();
        setupPlayer();
    }

    // prev song
    @Override
    public void prev() {
        queueHolder.moveToPrev();
        setupPlayer();
    }

    @Override
    public int getCurPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int newPos) {
        mediaPlayer.seekTo(newPos);
    }

    @Override
    public int getTotalTime() {
        return mediaPlayer.getDuration();
    }

    @Override
    public void setQueueType(int newType) {
        queueHolder.setQueueType(newType);
    }

    @Override
    public int getQueueType() {
        return queueHolder.getCurType();
    }
}
