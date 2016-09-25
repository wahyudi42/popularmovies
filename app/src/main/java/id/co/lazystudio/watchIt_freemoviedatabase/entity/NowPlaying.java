package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by faqiharifian on 25/09/16.
 */
public class NowPlaying {
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

    public List<Movie> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResult() {
        return totalResult;
    }
}
