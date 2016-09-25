package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by faqiharifian on 26/09/16.
 */
public class Genre {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
