package cn.blcstudio.easyplayer.playback.queue;

import java.util.List;

import cn.blcstudio.easyplayer.model.Music;

/**
 * Created by ginger on 2016/6/15.
 */

public class SingleRepeatQueue extends BaseQueue {

    @Override
    public boolean moveToNext(List<Music> queue) {
        return true;
    }

    @Override
    public boolean moveToPrev(List<Music> queue) {
        return true;
    }
}
