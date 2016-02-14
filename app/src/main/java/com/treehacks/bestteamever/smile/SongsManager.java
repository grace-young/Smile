package com.treehacks.bestteamever.smile;

/**
 * Created by Meera on 2/13/2016.
 */

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
    // SDCard Path
    public String MEDIA_PATH;
    public AssetFileDescriptor stream;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    // Constructor
    public SongsManager(Context context){
         stream = context.getResources().openRawResourceFd(R.raw.jathiswaram); //can have array with song ids
              //MEDIA_PATH = getRealPathFromURI(R.class.getContext(),path);
    }
    public AssetFileDescriptor getFile(){
        return stream;

    }
    public void setMEDIA_PATH(String path){
        MEDIA_PATH = path;
    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * BUT NO SD CARD ANYMORE
     * */
    public ArrayList<HashMap<String, String>> getPlayList(){
        File home = new File(MEDIA_PATH);

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                song.put("songPath", file.getPath());

                // Adding each song to SongList
                songsList.add(song);
            }
        }
        // return songs list array
        return songsList;
    }



    public String getRealPathFromURI(Context context, Uri contentUri){
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }

    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}

