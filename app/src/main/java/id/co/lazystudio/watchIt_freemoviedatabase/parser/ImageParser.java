package id.co.lazystudio.watchIt_freemoviedatabase.parser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;

/**
 * Created by faqiharifian on 29/09/16.
 */
public class ImageParser {
    @SerializedName("backdrops")
    private List<Image> backdrops;
    @SerializedName("posters")
    private List<Image> posters;

    public List<Image> getBackdrops() {
        return backdrops;
    }

    public List<Image> getPosters() {
        return posters;
    }
}
