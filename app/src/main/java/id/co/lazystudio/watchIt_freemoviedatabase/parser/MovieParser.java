package id.co.lazystudio.watchIt_freemoviedatabase.parser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Keyword;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Video;

/**
 * Created by faqiharifian on 30/09/16.
 */
public class MovieParser extends Movie {
    @SerializedName("images")
    private ImageParser images;
    @SerializedName("videos")
    private VideoParser videos;
    @SerializedName("keywords")
    private KeywordParser keywords;
    @SerializedName("similar")
    private MovieListParser similars;

    public List<Image> getBackdrops(){
        return images.getBackdrops();
    }

    public List<Image> getPosters(){
        return images.getPosters();
    }

    public List<Video> getVideos() {
        return videos.getVideos();
    }

    public List<Keyword> getKeywords() {
        return keywords.getKeywords();
    }

    public List<Movie> getSimilars() {
        return similars.getMovies();
    }
}
