package cn.blcstudio.easyplayer.playback.queue;

import java.util.List;

import cn.blcstudio.easyplayer.model.Music;

/**
 * Created by ginger on 2016/6/15.
 */

public abstract class BaseQueue implements QueueController {
    protected int cur = 0;

    @Override
    public int getCurMusicId() {
        return cur;
    }

    @Override
    public void setCurMusicId(int index) {
        cur = index;
    }

    @Override
    public Music getCurMusic(List<Music> queue) {
        return queue.get(cur);
    }

    @Override
    public void jumpTo(int index) {
        cur = index;
    }
}
