package id.co.lazystudio.watchIt_freemoviedatabase.parser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;

/**
 * Created by faqiharifian on 25/09/16.
 */
public class MovieListParser {
    @SerializedName("status_code")
    private int statusCode;
    @SerializedName("status_message")
    private String statusMessage;
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Movie> results;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResult;
    @SerializedName("parts")
    private List<Movie> parts;

    public int getPage() {
        return page;
    }

    public List<Movie> getMovies() {
        if(parts != null)
            return parts;
        else
            return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public List<Movie> getParts() {
        return parts;
    }
}
