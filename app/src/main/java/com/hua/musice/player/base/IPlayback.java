package com.hua.musice.player.base;

import android.support.annotation.Nullable;

import com.hua.musice.player.bean.AudioDecorator;
import com.hua.musice.player.bean.BaseAudio;
import com.hua.musice.player.bean.PlayList;
import com.hua.musice.player.bean.PlayMode;


/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/5/16
 * Time: 6:02 PM
 * Desc: IPlayer
 */
public interface IPlayback {

    void setPlayList(PlayList list);

    boolean play();

    boolean play(PlayList list);

    /**
     * 播放指定下标音频
     * @param startIndex    音频下标
     * @return
     */
    boolean play(int startIndex);

    boolean play(PlayList list, int startIndex);

    boolean play(AudioDecorator<? extends BaseAudio> audio);

    boolean playLast();

    boolean playNext();

    boolean pause();

    boolean isPlaying();

    int getProgress();

    AudioDecorator<? extends BaseAudio> getPlayingSong();

    boolean seekTo(int progress);

    void setPlayMode(PlayMode playMode);

    void registerCallback(Callback callback);

    void unregisterCallback(Callback callback);

    void removeCallbacks();

    void releasePlayer();

    /**
     * 播放状态更新回调
     */
    interface Callback {

        /**
         * 更换歌曲
         * @param song
         */
        void onSwitchSong(@Nullable AudioDecorator<? extends BaseAudio> song);

        /**
         * 播放完成回调
         * @param next
         */
        void onComplete(@Nullable AudioDecorator<? extends BaseAudio> next);

        /**
         * 播放和暂停回调
         * @param isPlaying
         */
        void onPlayStatusChanged(boolean isPlaying);
    }
}
