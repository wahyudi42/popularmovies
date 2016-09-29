package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;

/**
 * Created by faqiharifian on 29/09/16.
 */
public class Collection implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("poster_path")
    private String posterPath;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath(Context context, int sizeIndex) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);
        StringBuilder imagePath = new StringBuilder();
        imagePath.append(conf.getBaseUrl());
        imagePath.append(conf.getPosterSizes().get(sizeIndex));
        imagePath.append(posterPath);
        return imagePath.toString();
    }

    public String getBackdropPath(Context context, int sizeIndex) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);
        StringBuilder imagePath = new StringBuilder();
        imagePath.append(conf.getBaseUrl());
        imagePath.append(conf.getBackdropSizes().get(sizeIndex));
        imagePath.append(backdropPath);
        return imagePath.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.backdropPath);
        dest.writeString(this.posterPath);
    }

    public Collection() {
    }

    protected Collection(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.backdropPath = in.readString();
        this.posterPath = in.readString();
    }

    public static final Parcelable.Creator<Collection> CREATOR = new Parcelable.Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel source) {
            return new Collection(source);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };
}
