package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;

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
    @SerializedName("genres")
    private List<Genre> genres;
    @SerializedName("belongs_to_collection")
    private Collection collection;
    @SerializedName("production_companies")
    private List<Company> companies;
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

    public String getRuntime() {
        return runtime+" min";
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath(Context context, int sizeIndex) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);

        return conf.getBaseUrl()+
                conf.getPosterSizes().get(sizeIndex)+
                posterPath;
    }

    public String getBackdropPath(Context context, int sizeIndex) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);

        return conf.getBaseUrl()+
                conf.getBackdropSizes().get(sizeIndex)+
                backdropPath;
    }

    public String getPopularity() {
        DecimalFormat df = new DecimalFormat("#.00");
        return popularity > 0 ? df.format(popularity) : "-";
    }

    public String getVoteAverage() {
        DecimalFormat df = new DecimalFormat("#.00");
        return voteCount > 0 ? df.format(voteAverage) : "-";
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

    public List<Genre> getGenres() {
        return genres;
    }

    public Collection getCollection() {
        return collection;
    }

    public List<Company> getCompanies() {
        return companies;
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
        dest.writeList(this.genres);
        dest.writeParcelable(this.collection, flags);
        dest.writeTypedList(this.companies);
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
        this.genres = new ArrayList<Genre>();
        in.readList(this.genres, Genre.class.getClassLoader());
        this.collection = in.readParcelable(Collection.class.getClassLoader());
        this.companies = in.createTypedArrayList(Company.CREATOR);
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
}
