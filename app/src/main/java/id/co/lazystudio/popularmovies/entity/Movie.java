package id.co.lazystudio.popularmovies.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import id.co.lazystudio.popularmovies.utils.TmdbConfigurationPreference;

/**
 * Created by faqiharifian on 23/09/16.
 */
public class Movie implements Parcelable {
    //    genres, collection, productionCompanies, productionCountries
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("revenue")
    private long revenue;
    @SerializedName("budget")
    private long budget;
    @SerializedName("belongs_to_collection")
    private Collection collection;
    @SerializedName("status")
    private String status;
    @SerializedName("video")
    private boolean video;

    public Movie(){

    }
    public Movie(int id){
        this.id = id;
    }

    public String getHomepage() {
        return homepage;
    }

    public boolean isAdult() {
        return adult;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRuntime() {
        return runtime+" min";
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String result = "-";
        try{
            Date temp;
            if(!this.releaseDate.equals("")) {
                temp = parser.parse(this.releaseDate);
                result = formatter.format(temp);
            }

        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return result;
    }

    public String getReleaseDateDb(){
        return releaseDate;
    }

    public String getPosterPath(Context context, int sizeIndex) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);

        int index = sizeIndex == -1 ? conf.getPosterSizes().size() - 1 : sizeIndex;

        return conf.getBaseUrl()+
                conf.getPosterSizes().get(index)+
                posterPath;
    }

    public String getPosterPathDb(){
        return posterPath;
    }

    public String getBackdropPath(Context context, int sizeIndex) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);

        int index = sizeIndex == -1 ? conf.getBackdropSizes().size() - 1 : sizeIndex;

        return conf.getBaseUrl()+
                conf.getBackdropSizes().get(index)+
                backdropPath;
    }

    public String getBackdropPathDb(){
        return backdropPath;
    }

    public String getPopularity() {
        DecimalFormat df = new DecimalFormat("#.00");
        return popularity > 0 ? df.format(popularity) : "-";
    }

    public String getPopularityDb(){
        return String.valueOf(popularity);
    }

    public String getVoteAverage() {
        DecimalFormat df = new DecimalFormat("#.00");
        return voteCount > 0 ? df.format(voteAverage) : "-";
    }

    public String getVoteAverageDb(){
        return String.valueOf(voteAverage);
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getRevenue() {
        return "$ "+ NumberFormat.getInstance().format(revenue);
    }

    public String getBudget() {
        return "$ "+ NumberFormat.getInstance().format(budget);
    }

    public Collection getCollection() {
        return collection;
    }

    public String getStatus() {
        return status;
    }

    public boolean isVideo() {
        return video;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.runtime);
        dest.writeString(this.homepage);
        dest.writeString(this.tagline);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeFloat(this.popularity);
        dest.writeFloat(this.voteAverage);
        dest.writeInt(this.voteCount);
        dest.writeLong(this.revenue);
        dest.writeLong(this.budget);
        dest.writeString(this.status);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
    }

    protected Movie(Parcel in) {
        this.adult = in.readByte() != 0;
        this.id = in.readInt();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.runtime = in.readInt();
        this.homepage = in.readString();
        this.tagline = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readFloat();
        this.voteAverage = in.readFloat();
        this.voteCount = in.readInt();
        this.revenue = in.readLong();
        this.budget = in.readLong();
        this.status = in.readString();
        this.video = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }
}
