package com.hua.musice.player.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/2/16
 * Time: 4:01 PM
 * Desc: Song
 */
public class Song implements BaseAudio, Parcelable {

    private int id;

    private String title;

    private String displayName;

    private String artist;

    private String album;

    private String path;

    private int duration;

    private int size;

    private boolean favorite;

    public Song() {
        // Empty
    }

    public Song(Parcel in) {
        readFromParcel(in);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.displayName);
        dest.writeString(this.artist);
        dest.writeString(this.album);
        dest.writeString(this.path);
        dest.writeInt(this.duration);
        dest.writeInt(this.size);
        dest.writeInt(this.favorite ? 1 : 0);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.displayName = in.readString();
        this.artist = in.readString();
        this.album = in.readString();
        this.path = in.readString();
        this.duration = in.readInt();
        this.size = in.readInt();
        this.favorite = in.readInt() == 1;
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };



    @Override
    public String getAudioName() {
        return title;
    }

    @Override
    public String getAudioPath() {
        return path;
    }

    @Override
    public int getAudioDuration() {
        return duration;
    }

    @Override
    public void setAudioDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String getAudioArtist() {
        return artist;
    }

}
