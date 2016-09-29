package id.co.lazystudio.watchIt_freemoviedatabase.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by faqiharifian on 29/09/16.
 */
public class Keyword implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public Keyword() {
    }

    protected Keyword(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Keyword> CREATOR = new Parcelable.Creator<Keyword>() {
        @Override
        public Keyword createFromParcel(Parcel source) {
            return new Keyword(source);
        }

        @Override
        public Keyword[] newArray(int size) {
            return new Keyword[size];
        }
    };
}
