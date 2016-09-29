package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;

/**
 * Created by faqiharifian on 29/09/16.
 */
public class Image implements Parcelable {
    @SerializedName("file_path")
    private String path;
    @SerializedName("iso_639_1")
    private String iso;

    public String getBackdropPath(Context context,int sizeIndex) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);
        StringBuilder imagePath = new StringBuilder();
        imagePath.append(conf.getBaseUrl());
        imagePath.append(conf.getBackdropSizes().get(sizeIndex));
        imagePath.append(path);
        return imagePath.toString();
    }

    public String getPosterPath(Context context,int sizeIndex){
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);
        StringBuilder imagePath = new StringBuilder();
        imagePath.append(conf.getBaseUrl());
        imagePath.append(conf.getPosterSizes().get(sizeIndex));
        imagePath.append(path);
        return imagePath.toString();
    }

    public String getIso() {
        return iso;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.iso);
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.path = in.readString();
        this.iso = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
