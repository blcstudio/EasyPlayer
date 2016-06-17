package cn.blcstudio.easyplayer;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import cn.blcstudio.easyplayer.model.Music;
import cn.blcstudio.easyplayer.ui.CurrentActivityHolder;
import cn.blcstudio.easyplayer.util.MediaUtil;

/**
 * Created by ginger on 2016/6/16.
 */
public class MusicServiceHolder {

    public static final int NOTIFICATION_ID = 19;

    private MusicService musicService;

    // singleton
    private static MusicServiceHolder ourInstance = new MusicServiceHolder();
    public static MusicServiceHolder getInstance() {
        return ourInstance;
    }
    private MusicServiceHolder() {
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    public void showNotification(Context context) {
        // get current music
        Music curMusic = MusicServiceHolder.getInstance().getMusicService().getCurMusic();
        // generate remote view
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remote_playback);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setTicker("ticker")
                .setAutoCancel(false)
                //.setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContent(remoteViews);
        if (MusicServiceHolder.getInstance().getMusicService().isPlaying()) {
            remoteViews.setImageViewResource(R.id.remote_play, R.drawable.pause);
        } else {
            remoteViews.setImageViewResource(R.id.remote_play, R.drawable.play);
        }
        remoteViews.setImageViewBitmap(R.id.remote_album
                , MediaUtil.getArtwork(context, curMusic.getId(), curMusic.getAlbumId()));
        remoteViews.setTextViewText(R.id.remote_title, curMusic.getTitle());
        remoteViews.setTextViewText(R.id.remote_singer, curMusic.getSinger());

        Activity curActivity = CurrentActivityHolder.getInstance().getCurActivity();
        // play button
        Intent playIntent = new Intent(context, playButtonListener.class);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.remote_play, playPendingIntent);
        // next button
        Intent nextIntent = new Intent(context, nextButtonListener.class);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.remote_next, nextPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MusicServiceHolder.NOTIFICATION_ID, builder.build());
    }

    public static class playButtonListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getInstance().getMusicService().play();
            getInstance().showNotification(context);
        }
    }

    public static class nextButtonListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getInstance().getMusicService().next();
            getInstance().showNotification(context);

        }
    }

}
