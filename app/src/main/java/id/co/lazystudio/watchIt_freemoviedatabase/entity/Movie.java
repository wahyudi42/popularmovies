package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;

/**
 * Created by faqiharifian on 23/09/16.
 */
public class Movie {
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
        return df.format(popularity);
    }

    public String getVoteAverage() {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(voteAverage);
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
}
