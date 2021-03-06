package cn.blcstudio.easyplayer.playback.queue;

import java.util.List;

import cn.blcstudio.easyplayer.model.Music;

public class ListRepeatQueue extends BaseQueue {

    @Override
    public boolean moveToNext(List<Music> queue) {
        cur = (cur + 1) % queue.size();
        return true;
    }

    @Override
    public boolean moveToPrev(List<Music> queue) {
        cur = (cur + queue.size() - 1) % queue.size();
        return true;
    }
}
