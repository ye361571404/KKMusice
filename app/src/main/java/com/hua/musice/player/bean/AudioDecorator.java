package com.hua.musice.player.bean;

/**
 * Created by Administrator on 2017/2/8.
 */

public class AudioDecorator<T extends BaseAudio> {

    private T audio;

    public AudioDecorator() {

    }

    public AudioDecorator(T audio) {
        this.audio = audio;
    }

    public T getAudio() {
        return audio;
    }

    public void setAudio(T audio) {
        this.audio = audio;
    }


}
