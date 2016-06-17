package cn.blcstudio.easyplayer;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.blcstudio.easyplayer.model.Music;
import cn.blcstudio.easyplayer.playback.PlaybackController;
import cn.blcstudio.easyplayer.playback.PlaybackLocalController;
import cn.blcstudio.easyplayer.ui.CurrentActivityHolder;
import cn.blcstudio.easyplayer.ui.FullscreenActivity;
import cn.blcstudio.easyplayer.ui.PlayerActivity;

public class MusicService extends Service {

    public final MusicBinder binder = new MusicBinder();

    private PlaybackController controller = new PlaybackLocalController();

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setupService(List<Music> queue) {
        //Toast.makeText(getApplicationContext(), "setup service " + queue.size(), Toast.LENGTH_SHORT).show();
        controller.setupQueue(queue);
        controller.setupPlayer();

        controller.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("here", "music play completed, next");
                MusicService musicService = MusicServiceHolder.getInstance().getMusicService();
                musicService.next();
                musicService.play();
                updateInfoDisplay();
            }
        });
    }

    public void updateInfoDisplay() {
        Activity activity = CurrentActivityHolder.getInstance().getCurActivity();
        switch (CurrentActivityHolder.getInstance().getCurActivityType()) {
            case CurrentActivityHolder.PLAYER_ACTIVITY:
                ((PlayerActivity) activity).updateInfoDisplay();
                break;
            case CurrentActivityHolder.FULLSCREEN_ACTIVITY:
                ((FullscreenActivity) activity).updateInfoDisplay();
                break;
        }
    }

    public Music getCurMusic() {
        return controller.getCurMusic();
    }

    public void jumpTo(int index) {
        controller.jumpTo(index);
        controller.start();
    }

    public boolean isPlaying() {
        return controller.isPlaying();
    }

    public void play() {
            controller.play();
    }

    public void next() {
        boolean isPlaying = isPlaying();
        controller.next();
        if (isPlaying) controller.start();
        else controller.setupPlayer();
    }

    public void prev() {
        boolean isPlaying = isPlaying();
        controller.prev();
        if (isPlaying) controller.start();
        else controller.setupPlayer();
    }

    public int getCurPosition() {
        return controller.getCurPosition();
    }

    public void setCurPosition(int position) {
        controller.seekTo(position);
    }

    public int getTotalTime() {
        return controller.getTotalTime();
    }

    public int getCurQueueType() {
        return controller.getQueueType();
    }

    public void setQueueType(int type) {
        controller.setQueueType(type);
    }

    public void test() {
        Toast.makeText(getApplicationContext(), "Service is online", Toast.LENGTH_SHORT).show();
    }
}
