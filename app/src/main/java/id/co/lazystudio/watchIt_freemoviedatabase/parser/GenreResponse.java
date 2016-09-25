package id.co.lazystudio.watchIt_freemoviedatabase.parser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.entity.Genre;

/**
 * Created by faqiharifian on 26/09/16.
 */
public class GenreResponse {
    @SerializedName("genres")
    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }
}
