package cn.blcstudio.easyplayer.ui;

import android.app.Activity;

public class CurrentActivityHolder {

    // singleton
    private static CurrentActivityHolder ourInstance = new CurrentActivityHolder();
    public static CurrentActivityHolder getInstance() {
        return ourInstance;
    }
    private CurrentActivityHolder() {
    }

    public static final int PLAYER_ACTIVITY = 19;
    public static final int FULLSCREEN_ACTIVITY = 20;

    // holder
    private Activity curActivity;
    private int curActivityType;

    public int getCurActivityType() {
        return curActivityType;
    }

    public void setCurActivityType(int curActivityType) {
        this.curActivityType = curActivityType;
    }

    public Activity getCurActivity() {
        return curActivity;
    }

    public void setCurActivity(Activity curActivity) {
        this.curActivity = curActivity;
    }
}
