package id.co.lazystudio.watchIt_freemoviedatabase.parser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.entity.Keyword;

/**
 * Created by faqiharifian on 29/09/16.
 */
public class KeywordParser extends ErrorParser {
    @SerializedName("keywords")
    private List<Keyword> keywords;

    public List<Keyword> getKeywords() {
        return keywords;
    }
}
