package cn.blcstudio.easyplayer.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.blcstudio.easyplayer.MusicService;
import cn.blcstudio.easyplayer.MusicServiceHolder;
import cn.blcstudio.easyplayer.R;
import cn.blcstudio.easyplayer.model.Music;
import cn.blcstudio.easyplayer.util.MediaUtil;

public class FullscreenActivity extends FragmentActivity {

    // display view
    private ImageView albumArt;
    private TextView title, singer;
    // seek bar
    private TextView curTime, totalTime;
    private SeekBar seekBar;
    // playback button
    private ImageView btnPlay;
    private ImageView btnPrev;
    private ImageView btnNext;

    private boolean isDraging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // display view
        albumArt = (ImageView) findViewById(R.id.full_album_art);
        title = (TextView) findViewById(R.id.full_title);
        singer = (TextView) findViewById(R.id.full_singer);
        // seek bar
        seekBar = (SeekBar) findViewById(R.id.full_seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curTime.setText(DateFormat.format("mm:ss"
                        , progress * MusicServiceHolder.getInstance().getMusicService().getTotalTime() / seekBar.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isDraging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isDraging = false;
                MusicService musicService = MusicServiceHolder.getInstance().getMusicService();
                int position = seekBar.getProgress() * musicService.getTotalTime() / seekBar.getMax();
                musicService.setCurPosition(position);
                updateInfoDisplay();
            }
        });
        curTime = (TextView) findViewById(R.id.cur_time);
        totalTime = (TextView) findViewById(R.id.total_time);
        // playback button
        btnPlay = (ImageButton) findViewById(R.id.full_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicServiceHolder.getInstance().getMusicService().play();
                updateInfoDisplay();
            }
        });
        btnPrev = (ImageButton) findViewById(R.id.full_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicServiceHolder.getInstance().getMusicService().prev();
                updateInfoDisplay();
            }
        });
        btnNext = (ImageButton) findViewById(R.id.full_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicServiceHolder.getInstance().getMusicService().next();
                updateInfoDisplay();
            }
        });

        updateInfoDisplay();
    }

    private final int deplayedTime = 400;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        private MusicService musicService;
        @Override
        public void run() {
            if (musicService == null) {
                musicService = MusicServiceHolder.getInstance().getMusicService();
            }
            if (!isDraging) {
                seekBar.setProgress(musicService.getCurPosition() * seekBar.getMax() / musicService.getTotalTime());
                curTime.setText(DateFormat.format("mm:ss", musicService.getCurPosition()));
            }
            handler.postDelayed(this, deplayedTime);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        CurrentActivityHolder.getInstance().setCurActivity(this);
        CurrentActivityHolder.getInstance().setCurActivityType(CurrentActivityHolder.FULLSCREEN_ACTIVITY);
    }

    public void updateInfoDisplay() {
        if (MusicServiceHolder.getInstance().getMusicService() == null)
            return;

        // get music service
        MusicService musicService = MusicServiceHolder.getInstance().getMusicService();
        Music curMusic = musicService.getCurMusic();

        // album art
        Bitmap avatarBmp = MediaUtil.getArtwork(getApplicationContext()
                , curMusic.getId(), curMusic.getAlbumId());
        if (avatarBmp != null) {
            albumArt.setImageBitmap(avatarBmp);
        } else {
            albumArt.setImageResource(R.drawable.default_album);
        }
        // title
        title.setText(curMusic.getTitle());
        // singer
        singer.setText(curMusic.getSinger());

        // seek bar
        curTime.setText(DateFormat.format("mm:ss", musicService.getCurPosition()));
        totalTime.setText(DateFormat.format("mm:ss", musicService.getTotalTime()));
        seekBar.setProgress(musicService.getCurPosition() * seekBar.getMax() / musicService.getTotalTime());

        // playback button
        if (musicService.isPlaying()) {
            btnPlay.setImageResource(R.drawable.pause);
        } else {
            btnPlay.setImageResource(R.drawable.play);
        }

        // seek bar
        if (musicService.isPlaying()) {
            handler.postDelayed(runnable, deplayedTime);
        } else {
            handler.removeCallbacks(runnable);
        }

        // notification
        MusicServiceHolder.getInstance().showNotification(getApplicationContext());
    }

}
