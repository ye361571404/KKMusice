package com.hua.musice.player.bean;

/**
 * Created by Administrator on 2017/2/8.
 */

public interface BaseAudio {



    /**
     * 获取音频名称
     * @return
     */
    String getAudioName();

    /**
     * 获取音频路径
     * @return
     */
    String getAudioPath();

    /**
     * 获取音频时长
     * @return
     */
    int getAudioDuration();

    /**
     * 设置音频时长
     */
    void setAudioDuration(int duration);


    String getAudioArtist();


}
