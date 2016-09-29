package id.co.lazystudio.watchIt_freemoviedatabase.parser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;

/**
 * Created by faqiharifian on 25/09/16.
 */
public class MovieListParser {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Movie> results;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResult;

    public int getPage() {
        return page;
    }

    public List<Movie> getMovies() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResult() {
        return totalResult;
    }
}
