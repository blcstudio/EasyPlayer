package cn.blcstudio.easyplayer.playback.queue;

import java.util.List;
import java.util.Random;

import cn.blcstudio.easyplayer.model.Music;

public class RandomPlayQueue extends BaseQueue {

    protected  int prev;

    private int getRandom(List<Music> queue) {
        Random random = new Random();
        return random.nextInt(queue.size());
    }

    @Override
    public boolean moveToNext(List<Music> queue) {
        prev = cur;
        cur = getRandom(queue);
        return true;
    }

    @Override
    public boolean moveToPrev(List<Music> queue) {
        cur = prev;
        return true;
    }
}
