package cn.blcstudio.easyplayer.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import cn.blcstudio.easyplayer.model.Music;

/**
 * Created by ginger on 2016/6/15.
 */
public class MusicHelper {

    // singleton
    private static MusicHelper ourInstance = new MusicHelper();
    public static MusicHelper getInstance() {
        return ourInstance;
    }
    private MusicHelper() {
    }

    // sort order
    public static final int ORDER_BY_NAME = 19;
    public static final int ORDER_BY_SINGER = 20;
    public static final int ORDER_BY_ALBUM = 21;

    // get music list
    public List<Music> getMusicList(Context context, int sortOrder) {
        // get content resolver
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            return null;
        }

        // generate sort order query argument
        String sortOrderStr = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        switch (sortOrder) {
            case ORDER_BY_NAME:
                sortOrderStr = MediaStore.Audio.Media.TITLE + " ASC";
                break;
            case ORDER_BY_SINGER:
                sortOrderStr = MediaStore.Audio.Media.ARTIST + " ASC";
                break;
            case ORDER_BY_ALBUM:
                sortOrderStr = MediaStore.Audio.Media.ALBUM + " ASC";
                break;
        }

        // query all the music
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null
                , sortOrderStr);

        if (cursor == null) {
            return null;
        }

        // get music list
        List<Music> musicList = new ArrayList<>();
        cursor.moveToFirst();
        do {
            Music musicItem = new Music();
            // id
            musicItem.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            // title
            musicItem.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            // singer
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if (singer.equals("<unknown>")) singer = "未知艺术家";
            musicItem.setSinger(singer);
            // album
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            if (album.equals("<unknown>")) album = "未知专辑";
            musicItem.setAlbum(album);
            // album id (to get artwork)
            musicItem.setAlbumId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            // file name
            musicItem.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            // url
            musicItem.setUrl(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            // size
            musicItem.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            // time
            musicItem.setTime(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));

            musicList.add(musicItem);
        } while (cursor.moveToNext());
        cursor.close();
        return musicList;
    }
}
