package com.hua.musice;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hua.musice.player.base.IPlayback;
import com.hua.musice.player.base.MusicPlayerContract;
import com.hua.musice.player.bean.AudioDecorator;
import com.hua.musice.player.bean.BaseAudio;
import com.hua.musice.player.bean.ExhibitBean;
import com.hua.musice.player.bean.PlayList;
import com.hua.musice.player.bean.PlayMode;
import com.hua.musice.player.bean.Song;
import com.hua.musice.player.permission.DefaultRationale;
import com.hua.musice.player.permission.PermissionSetting;
import com.hua.musice.player.present.MusicPresentImpl;
import com.hua.musice.player.service.PlaybackService;
import com.hua.musice.player.utils.AssetsUtil;
import com.hua.musice.player.utils.PreferenceManager;
import com.hua.musice.player.utils.TimeUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MusicPlayerContract.View,IPlayback.Callback,
        View.OnClickListener,
        AudioManager.OnAudioFocusChangeListener {

    private final String audioBaseUrl = "http://39.107.90.144:8081/guidersrv/SystekGuiderData/ScenicSpotData/audio/";
    private final long UPDATE_PROGRESS_INTERVAL = 1000;

    private LinearLayout llProgress;
    private TextView textViewProgress;
    private SeekBar seekBar;
    private TextView textViewDuration;
    private ConstraintLayout clPlayControls;
    private ImageView ivPlayModeToggle;
    private ImageView ivPlayLast;
    private ImageView ivPlayToggle;
    private ImageView ivPlayNext;
    private ImageView ivFavoriteToggle;

    private Rationale mRationale;
    private PermissionSetting mSetting;

    private MusicPlayerContract.Presenter mPresenter;
    private IPlayback mPlayerService;
    private PlayList<Song> playList;
    private Handler mHandler = new Handler();
    private Runnable mProgressCallback = new Runnable() {
        @Override
        public void run() {
            if (isFinishing()){
                return;
            }
            if (mPlayerService.isPlaying()) {
                int progress = mPlayerService.getProgress();
                updateProgressTextWithDuration(mPlayerService.getProgress());
                if (progress >= 0 && progress <= seekBar.getMax()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBar.setProgress(progress, true);
                    } else {
                        seekBar.setProgress(progress);
                    }
                    mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        llProgress = findViewById(R.id.ll_progress);
        textViewProgress = findViewById(R.id.text_view_progress);
        seekBar =  findViewById(R.id.seek_bar);
        textViewDuration = findViewById(R.id.text_view_duration);
        clPlayControls = findViewById(R.id.cl_play_controls);
        ivPlayModeToggle = findViewById(R.id.iv_play_mode_toggle);
        ivPlayLast = findViewById(R.id.iv_play_last);
        ivPlayToggle = findViewById(R.id.iv_play_toggle);
        ivPlayNext = findViewById(R.id.iv_play_next);
        ivFavoriteToggle = findViewById(R.id.iv_favorite_toggle);
        // 申请权限
        requestPermission(Permission.Group.STORAGE);
    }

    private void initData() {
        mRationale = new DefaultRationale();
        mSetting = new PermissionSetting(this);
        playList = new PlayList();

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
            // song.setDuration(FileUtil.getAudioDuration(audioFile));
            audioDecorator = new AudioDecorator<>(song);
            audioDecoratorList.add(audioDecorator);
        }
        playList.setSongs(audioDecoratorList);

        // 绑定服务
        new MusicPresentImpl(this, this).subscribe();
    }

    private void setListener() {

        ivPlayToggle.setOnClickListener(this);
        ivPlayLast.setOnClickListener(this);
        ivPlayNext.setOnClickListener(this);
        ivPlayModeToggle.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgressTextWithProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mProgressCallback);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(getDuration(seekBar.getProgress()));
                if (mPlayerService.isPlaying()) {
                    mHandler.removeCallbacks(mProgressCallback);
                    mHandler.post(mProgressCallback);
                }
            }
        });
    }

    /**
     * 切换下一首回调
     * @param next
     */
    @Override
    public void onSwitchSong(@Nullable AudioDecorator<? extends BaseAudio> next) {
        onSongUpdated(next);
    }

    @Override
    public void onComplete(@Nullable AudioDecorator<? extends BaseAudio> next) {
        onSongUpdated(next);
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        updatePlayToggle(isPlaying);
        if (isPlaying) {
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        } else {
            mHandler.removeCallbacks(mProgressCallback);
        }
    }

    ///////////服务

    @Override
    public void handleError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 绑定服务
     * @param service
     */
    @Override
    public void onPlaybackServiceBound(PlaybackService service) {
        mPlayerService = service;
        mPlayerService.registerCallback(this);
        mPlayerService.setPlayList(playList);
    }

    /**
     * 解绑服务
     */
    @Override
    public void onPlaybackServiceUnbound() {
        mPlayerService.unregisterCallback(this);
        mPlayerService = null;
    }

    @Override
    public void onSongSetAsFavorite(@NonNull AudioDecorator song) {

    }

    @Override
    public void onSongUpdated(@Nullable AudioDecorator song) {
        if (song == null) {
            ivPlayToggle.setImageResource(R.drawable.ic_play_notif);
            seekBar.setProgress(0);
            updateProgressTextWithProgress(0);
            seekTo(0);
            mHandler.removeCallbacks(mProgressCallback);
            return;
        }
        setTitle(song.getAudio().getAudioName());
        // 设置音频时长
        int duration = getCurrentSongDuration();
        textViewDuration.setText(TimeUtils.formatDuration(duration));
        // 设置进度条长度
        seekBar.setMax(duration);

        mHandler.removeCallbacks(mProgressCallback);
        if (mPlayerService.isPlaying()) {
            mHandler.post(mProgressCallback);
            ivPlayToggle.setImageResource(R.drawable.ic_pause_notif);
        }
    }

    /**
     * 播放模式
     * @param playMode
     */
    @Override
    public void updatePlayMode(PlayMode playMode) {
        if (playMode == null) {
            playMode = PlayMode.getDefault();
        }
        switch (playMode) {
            case LIST:
                // 列表
                // ivPlayModeToggle.setImageResource(R.drawable.ic_play_mode_list);
                break;
            case LOOP:
                // 循环列表
                ivPlayModeToggle.setImageResource(R.drawable.ic_play_mode_loop);
                break;
            case SHUFFLE:
                // 随机
                ivPlayModeToggle.setImageResource(R.drawable.ic_play_mode_shuffle);
                break;
            case SINGLE:
                // 单曲循环
                ivPlayModeToggle.setImageResource(R.drawable.ic_play_mode_single);
                break;
        }
    }


    @Override
    public void updatePlayToggle(boolean play) {
        if(play){
            requestAudioFocus();
        }
        ivPlayToggle.setImageResource(play ? R.drawable.ic_pause_notif : R.drawable.ic_play_notif);
    }

    @Override
    public void updateFavoriteToggle(boolean favorite) {

    }

    @Override
    public void setPresenter(MusicPlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }


    ///////////// listener

    /**
     * 更新进度:
     *      * 时间进度
     *      * 进度条进度
     * @param progress
     */
    private void updateProgressTextWithProgress(int progress) {
        int targetDuration = getDuration(progress);
        textViewProgress.setText(TimeUtils.formatDuration(targetDuration));
    }

    /**
     * 更新进度
     *      * 时间进度
     * @param duration
     */
    private void updateProgressTextWithDuration(int duration) {
        textViewProgress.setText(TimeUtils.formatDuration(duration));
    }

    /**
     * 获取进度
     * @param progress
     * @return
     */
    private int getDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / seekBar.getMax()));
    }

    /**
     * 获取当前播放音频时长
     * @return
     */
    private int getCurrentSongDuration() {
        int duration = 0;
        AudioDecorator playingSong = mPlayerService.getPlayingSong();
        if (playingSong != null) {
            duration = playingSong.getAudio().getAudioDuration();
        }
        return duration;
    }

    private void seekTo(int duration) {
        mPlayerService.seekTo(duration);
    }


    /**
     * 获取音频焦点
     */
    private void requestAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_toggle:
                // 播放和暂停
                onPlayToggle();
                break;
            case R.id.iv_play_last:
                // 上一首
                onPlayLast();
                break;
            case R.id.iv_play_next:
                // 下一首
                onPlayNext();
                break;
            case R.id.iv_play_mode_toggle:
                // 播放模式
                onPlayModeToggleAction();
                break;
        }
    }



    /** 播放和暂停 **/
    public void onPlayToggle(){
        if (mPlayerService == null){
            return;
        }
        if (mPlayerService.isPlaying()) {
            mPlayerService.pause();
        } else {
            mPlayerService.play(0);
        }
    }

    /** 上一首 **/
    public void onPlayLast(){
        if (mPlayerService == null) {
            return;
        }
        mPlayerService.playLast();
        Toast.makeText(this, "上一首", Toast.LENGTH_SHORT).show();
    }

    /** 下一首 **/
    public void onPlayNext(){
        if (mPlayerService == null) {
            return;
        }
        mPlayerService.playNext();
        Toast.makeText(this, "下一首", Toast.LENGTH_SHORT).show();
    }


    /** 设置播放模式 **/
    public void onPlayModeToggleAction() {
        if (mPlayerService == null){
            return;
        }
        // 获取当前模式
        PlayMode current = PreferenceManager.lastPlayMode(this);
        // 切换下一个模式
        PlayMode newMode = PlayMode.switchNextMode(current);
        PreferenceManager.setPlayMode(this, newMode);
        mPlayerService.setPlayMode(newMode);
        updatePlayMode(newMode);
    }

    /**
     * 监听音频焦点
     * @param focusChange
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                /*if (mMediaPlayer == null) initMediaPlayer();
                else if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
                mMediaPlayer.setVolume(1.0f, 1.0f);*/

                Toast.makeText(this, "AUDIOFOCUS_GAIN", Toast.LENGTH_SHORT).show();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                /*if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;*/

                if (mPlayerService != null && mPlayerService.isPlaying()) {
                    mPlayerService.pause();
                }
                Toast.makeText(this, "AUDIOFOCUS_LOSS", Toast.LENGTH_SHORT).show();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                /*if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();*/

                if (mPlayerService != null && mPlayerService.isPlaying()) {
                    mPlayerService.pause();
                }
                Toast.makeText(this, "AUDIOFOCUS_LOSS_TRANSIENT", Toast.LENGTH_SHORT).show();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                /*if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);*/
                Toast.makeText(this, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    ////////    permission

    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        toast(R.string.failure);
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            mSetting.showSetting(permissions);
                        }
                    }
                })
                .start();
    }

    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
