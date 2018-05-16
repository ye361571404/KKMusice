package com.hua.musice.player.utils;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.text.TextUtils;

import com.hua.musice.player.base.AppApplication;

import java.io.File;

/**
 * Created by Administrator on 2017/1/22.
 */

public class FileUtil {

    private static final String UNKNOWN = "unknown";


    /**
     * sdcard根目录
     */
    public static final String SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    /**
     * 机身根目录
     */
    public static final String DATA_ROOT_PATH = Environment.getDataDirectory() + "/data/" + getPackageName();
    private static final String MUSIC = "Music";


    /**
     * 获取SD卡或机身根目录
     * @return
     */
    public static String getRootDirectory(){
        String universalToeflDirectory = null;
        if (externalMemoryAvailable()) {
            universalToeflDirectory = SDCARD_ROOT_PATH;
        } else {
            universalToeflDirectory = DATA_ROOT_PATH;
        }
        return universalToeflDirectory;
    }

    /**
     * 获取根目录下的应用缓存目录
     * @param appRootDirectory  应用缓存目录
     * @return
     */
    public static String getAppRootDirectory(String appRootDirectory){
        return getRootDirectory() + File.separator + appRootDirectory;
    }


    /**
     * SDCARD是否存在
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取包名
     * @return
     */
    public static String getPackageName(){
        return AppApplication.getContext().getPackageName();
    }



    public static int getAudioDuration(File file){
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(file.getAbsolutePath());
        int duration;
        String keyDuration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        // ensure the duration is a digit, otherwise return null song
        if (keyDuration == null || !keyDuration.matches("\\d+")) return 0;
        duration = Integer.parseInt(keyDuration);
        return duration;
    }


    /**
     * 获取音频文件信息
     * @param retriever
     * @param key
     * @param defaultValue
     * @return
     */
    private static String extractMetadata(MediaMetadataRetriever retriever, int key, String defaultValue) {
        String value = retriever.extractMetadata(key);
        if (TextUtils.isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }


}
