package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;

/**
 * Created by faqiharifian on 23/09/16.
 */
public class Movie implements Parcelable {
    //    genres, collection, productionCompanies, productionCountries
    @SerializedName("id")
    private int id;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("status")
    private String status;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("title")
    private String title;
    @SerializedName("video")
    private boolean video;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("vote_count")
    private int voteCount;

    public Movie(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath(Context context, int index) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);
        StringBuilder imagePath = new StringBuilder();
        imagePath.append(conf.getBaseUrl());
        imagePath.append(conf.getBackdropSizes().get(index));
        imagePath.append(posterPath);
        return imagePath.toString();
    }

    public String getBackdropPath(Context context, int index) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);
        StringBuilder imagePath = new StringBuilder();
        imagePath.append(conf.getBaseUrl());
        imagePath.append(conf.getBackdropSizes().get(index));
        imagePath.append(backdropPath);
        return imagePath.toString();
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.homepage);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.releaseDate);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.voteAverage);
        dest.writeInt(this.voteCount);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.adult = in.readByte() != 0;
        this.homepage = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.releaseDate = in.readString();
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readFloat();
        this.voteCount = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
