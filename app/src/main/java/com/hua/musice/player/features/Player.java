package com.hua.musice.player.features;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hua.musice.player.base.IPlayback;
import com.hua.musice.player.bean.AudioDecorator;
import com.hua.musice.player.bean.BaseAudio;
import com.hua.musice.player.bean.PlayList;
import com.hua.musice.player.bean.PlayMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/5/16
 * Time: 5:57 PM
 * Desc: Player
 */
public class Player implements IPlayback, MediaPlayer.OnCompletionListener {

    private static final String TAG = "Player";

    private static volatile Player sInstance;

    private MediaPlayer mPlayer;

    private PlayList mPlayList;
    // Default size 2: for service and UI
    private List<Callback> mCallbacks = new ArrayList<>(2);

    // Player status
    private boolean isPaused;

    private Player() {
        mPlayer = new MediaPlayer();
        mPlayList = new PlayList();
        mPlayer.setOnCompletionListener(this);
    }

    public static Player getInstance() {
        if (sInstance == null) {
            synchronized (Player.class) {
                if (sInstance == null) {
                    sInstance = new Player();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void setPlayList(PlayList list) {
        if (list == null) {
            list = new PlayList();
        }
        mPlayList = list;
    }

    @Override
    public boolean play() {
        if (isPaused) {
            mPlayer.start();
            notifyPlayStatusChanged(true);
            return true;
        }
        if (mPlayList.prepare()) {
            AudioDecorator<? extends BaseAudio> song = mPlayList.getCurrentSong();
            try {
                mPlayer.reset();
                String audioPath = song.getAudio().getAudioPath();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(audioPath);
                mPlayer.prepare();
                mPlayer.start();
                song.getAudio().setAudioDuration(mPlayer.getDuration());
                notifyPlayStatusChanged(true);
            } catch (IOException e) {
                Log.e(TAG, "play: ", e);
                notifyPlayStatusChanged(false);
                return false;
            }
            return true;
        }
        return false;
    }




    @Override
    public boolean play(PlayList list) {
        if (list == null) return false;

        isPaused = false;
        setPlayList(list);
        return play();
    }

    @Override
    public boolean play(int startIndex) {
        if(startIndex < 0 || mPlayList == null
                || mPlayList.getSongs() == null || mPlayList.getSongs().isEmpty()
                || startIndex >= mPlayList.getSongs().size()){
            Log.e(TAG, "play: 没有歌曲");
            return false;
        }

        mPlayList.setPlayingIndex(startIndex);
        boolean flag = play();
        if(flag){
            AudioDecorator<? extends BaseAudio> currentSong = mPlayList.getCurrentSong();
            notifyPlaySong(currentSong);
        }
        return flag;
    }

    @Override
    public boolean play(PlayList list, int startIndex) {
        if (list == null || startIndex < 0 || startIndex >= list.getNumOfSongs()){
            return false;
        }

        isPaused = false;
        list.setPlayingIndex(startIndex);
        setPlayList(list);
        return play();
    }



    @Override
    public boolean play(AudioDecorator<? extends BaseAudio> song) {
        if (song == null) return false;

        isPaused = false;
        mPlayList.getSongs().clear();
        mPlayList.getSongs().add(song);
        return play();
    }

    @Override
    public boolean playLast() {
        isPaused = false;
        boolean hasLast = mPlayList.hasLast();
        if (hasLast) {
            AudioDecorator last = mPlayList.last();
            play();
            notifyPlaySong(last);
            return true;
        }
        return false;
    }

    @Override
    public boolean playNext() {
        isPaused = false;
        boolean hasNext = mPlayList.hasNext(false);
        if (hasNext) {
            AudioDecorator next = mPlayList.next();
            play();
            notifyPlaySong(next);
            return true;
        }
        return false;
    }

    @Override
    public boolean pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            isPaused = true;
            notifyPlayStatusChanged(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPlaying() {
        if (mPlayer == null) {
            return false;
        }
        return mPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mPlayer.getCurrentPosition();
    }

    @Nullable
    @Override
    public AudioDecorator getPlayingSong() {
        return mPlayList.getCurrentSong();
    }

    @Override
    public boolean seekTo(int progress) {
        if (mPlayList.getSongs().isEmpty()) return false;

        AudioDecorator currentSong = mPlayList.getCurrentSong();
        if (currentSong != null) {
            if (currentSong.getAudio().getAudioDuration() <= progress) {
                onCompletion(mPlayer);
            } else {
                mPlayer.seekTo(progress);
            }
            return true;
        }
        return false;
    }

    /**设置播放模式 **/
    @Override
    public void setPlayMode(PlayMode playMode) {
        mPlayList.setPlayMode(playMode);
    }

    // Listeners


    /** 播放结束 **/
    @Override
    public void onCompletion(MediaPlayer mp) {
        AudioDecorator next = null;
        // There is only one limited play mode which is list, player should be stopped when hitting the list end
        if (mPlayList.getPlayMode() == PlayMode.LIST && mPlayList.getPlayingIndex() == mPlayList.getNumOfSongs() - 1) {
            // 如果是列表播放模式,并且已经是最后一首,则不再进行任何操作
            // In the end of the list
            // Do nothing, just deliver the callback
        } else if (mPlayList.getPlayMode() == PlayMode.SINGLE) {
            // 单曲循环
            next = mPlayList.getCurrentSong();
            play();
        } else {
            // 列表顺序播放
            boolean hasNext = mPlayList.hasNext(true);
            if (hasNext) {
                next = mPlayList.next();
                play();
            }
        }
        notifyComplete(next);
    }


    @Override
    public void releasePlayer() {
        mPlayList = null;
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        sInstance = null;
    }

    // Callbacks

    @Override
    public void registerCallback(Callback callback) {
        mCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        mCallbacks.remove(callback);
    }

    @Override
    public void removeCallbacks() {
        mCallbacks.clear();
    }


    private void notifyPlayStatusChanged(boolean isPlaying) {
        for (Callback callback : mCallbacks) {
            callback.onPlayStatusChanged(isPlaying);
        }
    }

    private void notifyPlaySong(AudioDecorator<? extends BaseAudio> song){
        for (Callback callback : mCallbacks) {
            callback.onSwitchSong(song);
        }
    }

    private void notifyComplete(AudioDecorator<? extends BaseAudio> song) {
        for (Callback callback : mCallbacks) {
            callback.onComplete(song);
        }
    }

}
