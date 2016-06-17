package cn.blcstudio.easyplayer.playback.queue;

import java.util.List;

import cn.blcstudio.easyplayer.model.Music;

/**
 * Created by ginger on 2016/6/15.
 */

// hold the queue

public class QueueHolder {
    // singleton
    private static QueueHolder ourInstance = new QueueHolder();
    public static QueueHolder getInstance() {
        return ourInstance;
    }
    private QueueHolder() {
        setQueueType(LIST_PLAY);
    }

    // queue type list
    public static final int LIST_PLAY = 19;
    public static final int LIST_REPEAT = 20;
    public static final int SINGLE_REPEAT = 21;
    public static final int RANDOM_PLAY = 22;

    // queue
    private List<Music> queue;
    // controller
    private QueueController controller;
    // current queue type
    private int curType;

    // setup queue
    public void setupQueue(List<Music> _queue) {
        queue = _queue;
    }

    // queue type
    public int getCurType() {
        return curType;
    }

    public void setQueueType(int newType) {
        curType = newType;
        int curMusicId = 0;
        if (controller != null) {
            curMusicId = controller.getCurMusicId();
        }

        switch (curType) {
            case LIST_PLAY:
                controller = new ListPlayQueue();
                break;
            case LIST_REPEAT:
                controller = new ListRepeatQueue();
                break;
            case SINGLE_REPEAT:
                controller = new SingleRepeatQueue();
                break;
            case RANDOM_PLAY:
                controller = new RandomPlayQueue();
                break;
        }
        controller.setCurMusicId(curMusicId);
    }

    // get music path
    public Music getCurMusic() {
        return controller.getCurMusic(queue);
    }

    public void jumpTo(int index) {
        controller.jumpTo(index);
    }

    public void moveToNext() {
        controller.moveToNext(queue);
    }

    public void moveToPrev() {
        controller.moveToPrev(queue);
    }

}
