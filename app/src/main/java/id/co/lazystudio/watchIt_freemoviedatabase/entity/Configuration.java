package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.parser.ErrorParser;

/**
 * Created by faqiharifian on 25/09/16.
 */
public class Configuration extends ErrorParser {
    @SerializedName("images")
    private Image images;

    public Image getImages() {
        return images;
    }

    public static class Image{
        @SerializedName("base_url")
        private String baseUrl;
        @SerializedName("secure_base_url")
        private String secureBaseUrl;
        @SerializedName("backdrop_sizes")
        private List<String> backdropSizes;
        @SerializedName("logo_sizes")
        private List<String> logoSizes;
        @SerializedName("poster_sizes")
        private List<String> posterSizes;
        @SerializedName("profile_sizes")
        private List<String> profileSizes;
        @SerializedName("still_sizes")
        private List<String> stillSizes;

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getSecureBaseUrl() {
            return secureBaseUrl;
        }

        public List<String> getBackdropSizes() {
            return backdropSizes;
        }

        public List<String> getLogoSizes() {
            return logoSizes;
        }

        public List<String> getPosterSizes() {
            return posterSizes;
        }

        public List<String> getProfileSizes() {
            return profileSizes;
        }

        public List<String> getStillSizes() {
            return stillSizes;
        }
    }
}
