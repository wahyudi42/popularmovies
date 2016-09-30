package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;

/**
 * Created by faqiharifian on 29/09/16.
 */
public class Company implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
//    @SerializedName("headquarters")
//    private String headquarters;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("logo_path")
    private String logoPath;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getLogoPath(Context context, int sizeIndex) {
        TmdbConfigurationPreference conf = new TmdbConfigurationPreference(context);
        StringBuilder imagePath = new StringBuilder();
        imagePath.append(conf.getBaseUrl());
        imagePath.append(conf.getBackdropSizes().get(sizeIndex));
        imagePath.append(logoPath);
        return imagePath.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.homepage);
        dest.writeString(this.logoPath);
    }

    public Company() {
    }

    protected Company(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.homepage = in.readString();
        this.logoPath = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel source) {
            return new Company(source);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };
}
