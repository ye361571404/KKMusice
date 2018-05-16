package com.hua.musice.player.base;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/8/16
 * Time: 11:49 PM
 * Desc: BasePresenter
 */
public interface BasePresenter {

    /**
     * 绑定
     */
    void attachView();

    /**
     * 解绑
     */
    void detachView();
}
