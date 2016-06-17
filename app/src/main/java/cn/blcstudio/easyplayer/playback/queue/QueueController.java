package cn.blcstudio.easyplayer.playback.queue;

import java.util.List;

import cn.blcstudio.easyplayer.model.Music;

// manage queue control

public interface QueueController {

    // get current playing music object
    public Music getCurMusic(List<Music> queue);
    // set current music id
    public void setCurMusicId(int index);
    // get current music id
    public int getCurMusicId();
    // jump to music
    public void jumpTo(int index);
    // move to next music, if none, return false
    public boolean moveToNext(List<Music> queue);
    // move to previous music, if none, return false
    public boolean moveToPrev(List<Music> queue);

}
