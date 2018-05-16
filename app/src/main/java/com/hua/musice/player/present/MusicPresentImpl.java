package com.hua.musice.player.present;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hua.musice.player.base.MusicPlayerContract;
import com.hua.musice.player.bean.AudioDecorator;
import com.hua.musice.player.bean.PlayMode;
import com.hua.musice.player.service.PlaybackService;
import com.hua.musice.player.utils.PreferenceManager;


/**
 * Created by Administrator on 2017/1/22.
 */

public class MusicPresentImpl implements MusicPlayerContract.Presenter {

    private Context mContext;
    private MusicPlayerContract.View mView;
    private boolean mIsServiceBound;
    private PlaybackService mPlaybackService;

    public MusicPresentImpl(Context context, MusicPlayerContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }


    /**
     * 绑定服务
     */
    @Override
    public void bindPlaybackService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        boolean b = mContext.bindService(new Intent(mContext, PlaybackService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsServiceBound = true;
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


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mPlaybackService = ((PlaybackService.LocalBinder) service).getService();
            mView.onPlaybackServiceBound(mPlaybackService);
            mView.onSongUpdated(mPlaybackService.getPlayingSong());
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mPlaybackService = null;
            mView.onPlaybackServiceUnbound();
        }
    };


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
    public void subscribe() {
        retrieveLastPlayMode();
        bindPlaybackService();
    }

    /**
     * 解绑服务
     */
    @Override
    public void unsubscribe() {
        unbindPlaybackService();
        // Release context reference
        mContext = null;
        mView = null;
    }
}
