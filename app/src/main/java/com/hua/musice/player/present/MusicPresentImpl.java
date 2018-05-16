package com.hua.musice.player.present;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.google.gson.Gson;
import com.hua.musice.player.base.AppApplication;
import com.hua.musice.player.base.MusicPlayerContract;
import com.hua.musice.player.bean.AudioDecorator;
import com.hua.musice.player.bean.ExhibitBean;
import com.hua.musice.player.bean.PlayList;
import com.hua.musice.player.bean.PlayMode;
import com.hua.musice.player.bean.Song;
import com.hua.musice.player.service.PlaybackService;
import com.hua.musice.player.utils.AssetsUtil;
import com.hua.musice.player.utils.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/1/22.
 */

public class MusicPresentImpl implements MusicPlayerContract.Presenter{

    private final String audioBaseUrl = "http://39.107.90.144:8081/guidersrv/SystekGuiderData/ScenicSpotData/audio/";

    private Context mContext;
    private MusicPlayerContract.MainView mView;
    private boolean mIsServiceBound;
    private PlaybackService mPlaybackService;

    public MusicPresentImpl(Context context, MusicPlayerContract.MainView view) {
        mContext = context;
        mView = view;
    }


    /**
     * 绑定服务
     */
    @Override
    public void bindPlaybackService() {
        mIsServiceBound = mContext.bindService(new Intent(mContext, PlaybackService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解绑服务
     */
    @Override
    public void unbindPlaybackService() {
        if (mIsServiceBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsServiceBound = false;
        }
    }

    /**
     * 获取歌曲列表,本地资源
     */
   /* @Override
    public void getPlayList() {
        PlayList<Song> playList = new PlayList();
        String json = AssetsUtil.readFile("data.txt");
        Gson gson = new Gson();
        ExhibitBean exhibit = gson.fromJson(json, ExhibitBean.class);
        List<ExhibitBean.DataBean> exhibitData = exhibit.getData();

        List<AudioDecorator<Song>> audioDecoratorList = new ArrayList<>();
        AudioDecorator<Song> audioDecorator = null;
        Song song = null;
        // 获取 /storage/emulated/0/Music 目录下的音乐(添加读取SD卡权限)
        File directory = Environment.getExternalStoragePublicDirectory("Music");
        File audioFile = null;
        for (int i = 0; i < exhibitData.size(); i++) {
            ExhibitBean.DataBean bean = exhibitData.get(i);
            song = new Song();
            song.setTitle(bean.getName());
            audioFile = new File(directory.getAbsolutePath(), bean.getAudio_url());
            song.setPath(audioFile.getAbsolutePath());
            audioDecorator = new AudioDecorator<>(song);
            audioDecoratorList.add(audioDecorator);
        }
        playList.setSongs(audioDecoratorList);
        mView.bindPlayList(playList);
    }*/

    /**
     * 获取歌曲列表,网络资源
     */
    @Override
    public void getPlayList() {
        PlayList<Song> playList = new PlayList();
        String json = AssetsUtil.readFile("data.txt");
        Gson gson = new Gson();
        ExhibitBean exhibit = gson.fromJson(json, ExhibitBean.class);
        List<ExhibitBean.DataBean> exhibitData = exhibit.getData();

        List<AudioDecorator<Song>> audioDecoratorList = new ArrayList<>();
        AudioDecorator<Song> audioDecorator = null;
        ExhibitBean.DataBean bean = null;
        for (int i = 0; i < exhibitData.size(); i++) {
            bean = exhibitData.get(i);
            audioDecorator = getAudioDecorator(bean);
            audioDecoratorList.add(audioDecorator);
        }
        playList.setSongs(audioDecoratorList);
        setPlayList(playList);
    }


    /**
     * 设置播放模式
     */
    @Override
    public void onPlayModeToggleAction() {
        if (mPlaybackService == null){
            return;
        }
        // 获取当前模式
        PlayMode current = PreferenceManager.lastPlayMode(AppApplication.getContext());
        // 切换下一个模式
        PlayMode newMode = PlayMode.switchNextMode(current);
        PreferenceManager.setPlayMode(AppApplication.getContext(), newMode);
        mPlaybackService.setPlayMode(newMode);
        mView.updatePlayMode(newMode);

    }

    private AudioDecorator<Song> getAudioDecorator(ExhibitBean.DataBean bean) {
        Song song = new Song();
        song.setTitle(bean.getName());
        try {
            String fileName = URLEncoder.encode(bean.getAudio_url(), "utf-8");
            song.setPath(audioBaseUrl + fileName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AudioDecorator<Song> audioDecorator = new AudioDecorator<>(song);
        return audioDecorator;
    }


    /**
     * 连接服务
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mPlaybackService = ((PlaybackService.LocalBinder) service).getService();
            mView.onPlaybackServiceBound(mPlaybackService);
            mView.onSongUpdated(mPlaybackService.getPlayingSong());
        }

        public void onServiceDisconnected(ComponentName className) {
            mPlaybackService = null;
            mView.onPlaybackServiceUnbound();
        }
    };


    public void setPlayList(PlayList<Song> playList){
        mPlaybackService.setPlayList(playList);
    }

    /**
     * 获取上一次的播放模式
     */
    @Override
    public void retrieveLastPlayMode() {
        PlayMode lastPlayMode = PreferenceManager.lastPlayMode(mContext);
        mView.updatePlayMode(lastPlayMode);
    }

    @Override
    public void setSongAsFavorite(AudioDecorator song, boolean favorite) {

    }

    /**
     * 绑定服务
     */
    @Override
    public void attachView() {
        retrieveLastPlayMode();
        bindPlaybackService();
    }

    /**
     * 解绑服务
     */
    @Override
    public void detachView() {
        unbindPlaybackService();
        mContext = null;
        mView = null;
    }
}
