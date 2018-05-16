package com.hua.musice.player.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hua.musice.player.bean.AudioDecorator;
import com.hua.musice.player.bean.PlayMode;
import com.hua.musice.player.service.PlaybackService;


/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/12/16
 * Time: 8:27 AM
 * Desc: MusicPlayerContract
 */
/* package */
public interface MusicPlayerContract {

    interface View extends BaseView<Presenter> {

        void handleError(Throwable error);

        /**
         * 绑定服务
         * @param service
         */
        void onPlaybackServiceBound(PlaybackService service);

        /**
         * 解绑服务
         */
        void onPlaybackServiceUnbound();

        void onSongSetAsFavorite(@NonNull AudioDecorator song);

        void onSongUpdated(@Nullable AudioDecorator song);

        /**
         * 更新播放模式
         * @param playMode
         */
        void updatePlayMode(PlayMode playMode);

        /**
         * 更改播放按钮图标
         * @param play
         */
        void updatePlayToggle(boolean play);

        void updateFavoriteToggle(boolean favorite);
    }

    interface Presenter extends BasePresenter {

        void retrieveLastPlayMode();

        void setSongAsFavorite(AudioDecorator song, boolean favorite);

        /**
         * 启动服务
         */
        void bindPlaybackService();

        /**
         * 解绑服务
         */
        void unbindPlaybackService();
    }
}
