package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by faqiharifian on 29/09/16.
 */
public class Video implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String iso;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;

    public String getId() {
        return id;
    }

    public String getIso() {
        return iso;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
    }

    public Video() {
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.iso = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
