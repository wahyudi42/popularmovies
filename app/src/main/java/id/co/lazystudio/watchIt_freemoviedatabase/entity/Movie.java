package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;

/**
 * Created by faqiharifian on 23/09/16.
 */
public class Movie {
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
}
